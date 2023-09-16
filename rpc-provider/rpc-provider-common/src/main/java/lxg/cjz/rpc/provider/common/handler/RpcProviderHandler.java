package lxg.cjz.rpc.provider.common.handler;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.util.JSONPObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lxg.cjz.rpc.common.helper.RpcServiceHelper;
import lxg.cjz.rpc.common.threadpool.ServerThreadPool;
import lxg.cjz.rpc.constants.RpcConstants;
import lxg.cjz.rpc.protocol.RpcProtocol;
import lxg.cjz.rpc.protocol.enumeration.RpcStatus;
import lxg.cjz.rpc.protocol.enumeration.RpcType;
import lxg.cjz.rpc.protocol.header.RpcHeader;
import lxg.cjz.rpc.protocol.request.RpcRequest;
import lxg.cjz.rpc.protocol.response.RpcResponse;
import lxg.cjz.rpc.serialization.api.Serialization;
import net.sf.cglib.reflect.FastClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author russel
 * @version 1.0.0
 * @date 2023/9/12
 * @description
 */
public class RpcProviderHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcRequest>> {
    private final Logger logger = LoggerFactory.getLogger(RpcProviderHandler.class);

    private final Map<String, Object> handlerMap;
    private final String reflectType;

    public RpcProviderHandler(String reflectType, Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
        this.reflectType = reflectType;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcProtocol<RpcRequest> protocol) throws Exception {
        ServerThreadPool.submit(()->{
            RpcHeader header = protocol.getHeader();
            header.setMessageType((byte) RpcType.RESPONSE.getType());
            RpcRequest request = protocol.getBody();
            logger.debug("receive request: {}", header.getRequestId());
            RpcProtocol<RpcResponse> responseRpcProtocol = new RpcProtocol<>();
            RpcResponse response = new RpcResponse();
            try {
                Object result = handle(request);
                response.setResult(result);
                response.setAsync(request.isAsync());
                response.setOneWay(request.isOneWay());
                header.setStatus((byte) RpcStatus.SUCCESS.getCode());
            } catch (Throwable throwable) {
                response.setException(throwable.getMessage());
                header.setStatus((byte) RpcStatus.FAIL.getCode());
                logger.debug("rpc provider handle request error: ", throwable);
            }
            responseRpcProtocol.setHeader(header);
            responseRpcProtocol.setBody(response);
            ctx.writeAndFlush(responseRpcProtocol).addListener(future -> {
                if (future.isSuccess()) {
                    logger.debug("send response for request: {}", header.getRequestId());
                } else {
                    logger.error("send response for request {} error", header.getRequestId());
                }
            });
        });
    }

    private Object handle(RpcRequest request) throws Throwable {
        String serviceKey = RpcServiceHelper.buildServiceKey(request.getClassName(), request.getVersion(), request.getGroup());
        Object serviceBean = handlerMap.get(serviceKey);
        if(serviceBean == null){
            throw new RuntimeException(String.format("service not exist: %s : %s", request.getClassName(), request.getMethodName()));
        }
        Class<?> serviceClass = serviceBean.getClass();
        String methodName = request.getMethodName();
        Object[] parameters = request.getParameters();
        Class<?>[] parameterTypes = request.getParameterTypes();
        logger.debug(serviceClass.getName());
        logger.debug(methodName);
        if (parameterTypes != null){
            for (Class<?> parameterType : parameterTypes) {
                logger.debug(parameterType.getName());
            }
        }
        if (parameters != null) {
            for (Object parameter : parameters) {
                logger.debug(parameter.toString());
            }
        }
        return invokeMethod(serviceBean, serviceClass, methodName, parameterTypes, parameters);
    }

    private Object invokeMethod(Object serviceBean, Class<?> serviceClass, String methodName, Class<?>[] parameterTypes, Object[] parameters) throws Throwable {
        switch (reflectType){
            case RpcConstants.REFLECT_TYPE_JDK:
                return invokeJDKMethod(serviceBean, serviceClass, methodName, parameterTypes, parameters);
            case RpcConstants.REFLECT_TYPE_CGLIB:
                return invokeCGLIBMethod(serviceBean, serviceClass, methodName, parameterTypes, parameters);
            default:
                throw new IllegalArgumentException("not support the reflect type: " + reflectType + " now");
        }
    }

    private Object invokeCGLIBMethod(Object serviceBean, Class<?> serviceClass, String methodName, Class<?>[] parameterTypes, Object[] parameters) throws Throwable{
        logger.info("cglib reflect invoke method");
        FastClass serviceFastClass = FastClass.create(serviceClass);
        return serviceClass.getMethod(methodName, parameterTypes).invoke(serviceBean, parameters);
    }

    private Object invokeJDKMethod(Object serviceBean, Class<?> serviceClass, String methodName, Class<?>[] parameterTypes, Object[] parameters) throws Throwable {
        logger.info("jdk reflect invoke method");
        Method method = serviceClass.getMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method.invoke(serviceBean, parameters);
    }
}
