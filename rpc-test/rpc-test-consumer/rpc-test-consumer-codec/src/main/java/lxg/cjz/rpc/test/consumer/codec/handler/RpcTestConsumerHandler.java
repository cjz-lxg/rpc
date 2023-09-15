package lxg.cjz.rpc.test.consumer.codec.handler;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lxg.cjz.rpc.protocol.RpcProtocol;
import lxg.cjz.rpc.protocol.header.RpcHeaderFactory;
import lxg.cjz.rpc.protocol.request.RpcRequest;
import lxg.cjz.rpc.protocol.response.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author russel
 * @version 1.0.0
 * @date 2023/9/15
 * @description
 */
public class RpcTestConsumerHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcResponse>> {

    private static final Logger logger = LoggerFactory.getLogger(RpcTestConsumerHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcProtocol<RpcResponse> rpcResponseRpcProtocol) throws Exception {
        logger.info("服务消费者收到的消息======>{}", JSONObject.toJSONString(rpcResponseRpcProtocol));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("发送消息......");
        RpcProtocol<RpcRequest> protocol = new RpcProtocol<>();
        protocol.setHeader(RpcHeaderFactory.getRequestHeader("jdk"));
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setClassName("cjz.lxg.rpc.test.DemoService");
        rpcRequest.setGroup("cjz");
        rpcRequest.setMethodName("hello");
        rpcRequest.setParameters(new Object[]{"cjz"});
        rpcRequest.setParameterTypes(new Class[]{String.class});
        rpcRequest.setVersion("1.0.0");
        rpcRequest.setAsync(false);
        rpcRequest.setOneWay(false);
        protocol.setBody(rpcRequest);
        logger.info("服务消费者发送的消息======>{}", JSONObject.toJSONString(protocol));
        ctx.writeAndFlush(protocol);
        logger.info("发送消息成功......");
    }
}
