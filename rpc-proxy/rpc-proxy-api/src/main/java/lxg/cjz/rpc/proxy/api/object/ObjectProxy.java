package lxg.cjz.rpc.proxy.api.object;

import lxg.cjz.rpc.protocol.RpcProtocol;
import lxg.cjz.rpc.protocol.header.RpcHeaderFactory;
import lxg.cjz.rpc.protocol.request.RpcRequest;
import lxg.cjz.rpc.proxy.api.consumer.Consumer;
import lxg.cjz.rpc.proxy.api.future.RPCFuture;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.ResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

public class ObjectProxy<T> implements InvocationHandler {

    private static final Logger logger = LoggerFactory.getLogger(ObjectProxy.class);

    /**
     * 接口的Class对象
     */
    private Class<T> clazz;
    /**
     * 服务版本号
     */
    private String serviceVersion;
    /**
     * 服务分组
     */
    private String serviceGroup;
    /**
     * 超时时间，默认15s
     */
    private long timeout = 15000;
    /**
     * 服务消费者
     */
    private Consumer consumer;
    /**
     * 序列化类型
     */
    private String serializationType;

    /**
     * 是否异步调用
     */
    private boolean async;

    /**
     * 是否单向调用
     */
    private boolean oneway;

    public ObjectProxy(Class<T> clazz) {
        this.clazz = clazz;
    }

    public ObjectProxy(Class<T> clazz, String serviceVersion, String serviceGroup, long timeout, Consumer consumer, String serializationType, boolean async, boolean oneway) {
        this.clazz = clazz;
        this.serviceVersion = serviceVersion;
        this.serviceGroup = serviceGroup;
        this.timeout = timeout;
        this.consumer = consumer;
        this.serializationType = serializationType;
        this.async = async;
        this.oneway = oneway;
    }

    public void setClazz(Class<T> clazz) {
        this.clazz = clazz;
    }

    public void setServiceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
    }

    public void setServiceGroup(String serviceGroup) {
        this.serviceGroup = serviceGroup;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }

    public void setSerializationType(String serializationType) {
        this.serializationType = serializationType;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    public void setOneway(boolean oneway) {
        this.oneway = oneway;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class == method.getDeclaringClass()) {
            String name = method.getName();
            switch (name) {
                case "equals":
                    return proxy == args[0];
                case "hashCode":
                    return System.identityHashCode(proxy);
                case "toString":
                    return proxy.getClass().getName() + "@" +
                            Integer.toHexString(System.identityHashCode(proxy)) +
                            ", with InvocationHandler " + this;
                default:
                    throw new IllegalStateException(String.valueOf(method));
            }
        }
        RpcProtocol<RpcRequest> requestRpcProtocol = new RpcProtocol<RpcRequest>();
        requestRpcProtocol.setHeader(RpcHeaderFactory.getRequestHeader(serializationType));
        RpcRequest request = new RpcRequest();
        request.setVersion(this.serviceVersion);
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setGroup(this.serviceGroup);
        request.setParameters(args);
        request.setAsync(async);
        request.setOneWay(oneway);
        requestRpcProtocol.setBody(request);
        // Debug
        logger.debug(method.getDeclaringClass().getName());
        logger.debug(method.getName());
        for (Class<?> parameterType : method.getParameterTypes()) {
            logger.debug(parameterType.getName());
        }
        for (Object arg : args) {
            logger.debug(arg.toString());
        }
        RPCFuture rpcFuture = this.consumer.sendRequest(requestRpcProtocol);
        return rpcFuture == null ? null : timeout > 0 ? rpcFuture.get(timeout, TimeUnit.MILLISECONDS) : rpcFuture.get();
    }

}
