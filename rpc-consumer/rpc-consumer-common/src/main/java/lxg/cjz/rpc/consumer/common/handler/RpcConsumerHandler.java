package lxg.cjz.rpc.consumer.common.handler;

import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lxg.cjz.rpc.constants.RpcConstants;
import lxg.cjz.rpc.consumer.common.common.future.RPCFuture;
import lxg.cjz.rpc.protocol.RpcProtocol;
import lxg.cjz.rpc.protocol.enumeration.RpcType;
import lxg.cjz.rpc.protocol.header.RpcHeader;
import lxg.cjz.rpc.protocol.request.RpcRequest;
import lxg.cjz.rpc.protocol.response.RpcResponse;
import org.apache.dubbo.rpc.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author russel
 * @version 1.0.0
 * @date 2023/9/16
 * @description
 */
public class RpcConsumerHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcResponse>> {

    private static final Logger logger = LoggerFactory.getLogger(RpcConsumerHandler.class);
    private volatile Channel channel;
    private SocketAddress remotePeer;

    // 存放请求编号和响应对象之间的映射关系
    private final Map<Long, RPCFuture> pendingRPC = new ConcurrentHashMap<>();

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public SocketAddress getRemotePeer() {
        return remotePeer;
    }

    public void setRemotePeer(SocketAddress remotePeer) {
        this.remotePeer = remotePeer;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        this.channel = ctx.channel();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.remotePeer = this.channel.remoteAddress();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcProtocol<RpcResponse> responseProtocol) throws Exception {
        if (responseProtocol == null || responseProtocol.getHeader().getMessageType() != RpcType.RESPONSE.getType()) {
            throw new RpcException("rpc response is null or not response type");
        }
        logger.info("client receive msg:{}", JSONObject.toJSONString(responseProtocol));
        RpcHeader header = responseProtocol.getHeader();
        long requestId = header.getRequestId();
        RPCFuture rpcFuture = pendingRPC.remove(requestId);
        if (rpcFuture != null) {
            rpcFuture.done(responseProtocol);
        }
    }

    public RPCFuture sendRequest(RpcProtocol<RpcRequest> protocol) {
        if (channel == null || !channel.isActive()) {
            //TODO优化点:失败重试和失败抛出异常
            throw new RpcException("channel is not active or null");
        }
        RPCFuture rpcFuture = this.getRpcFuture(protocol);
        logger.info("client send msg:{}", JSONObject.toJSONString(protocol));
        channel.writeAndFlush(protocol);
        return rpcFuture;
    }

    private RPCFuture getRpcFuture(RpcProtocol<RpcRequest> protocol) {
        RPCFuture rpcFuture = new RPCFuture(protocol);
        RpcHeader header = protocol.getHeader();
        long requestId = header.getRequestId();
        pendingRPC.put(requestId, rpcFuture);
        return rpcFuture;
    }

    public void close() {
        channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE)
                .addListener(future -> {
                    if (future.isSuccess()) {
                        logger.info("资源成功关闭");
                    } else {
                        logger.error("资源关闭失败", future.cause());
                    }
                });
    }

}
