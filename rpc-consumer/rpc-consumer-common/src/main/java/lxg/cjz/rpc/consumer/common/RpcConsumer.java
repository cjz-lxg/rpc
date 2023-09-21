package lxg.cjz.rpc.consumer.common;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lxg.cjz.rpc.consumer.common.common.future.RPCFuture;
import lxg.cjz.rpc.consumer.common.handler.RpcConsumerHandler;
import lxg.cjz.rpc.consumer.common.initializer.RpcConsumerInitializer;
import lxg.cjz.rpc.protocol.RpcProtocol;
import lxg.cjz.rpc.protocol.request.RpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author russel
 * @version 1.0.0
 * @date 2023/9/16
 * @description
 */
public class RpcConsumer {
    private static final Logger logger = LoggerFactory.getLogger(RpcConsumer.class);
    private final Bootstrap bootstrap;

    private final EventLoopGroup eventLoopGroup;

    private static volatile RpcConsumer instance;

    private static Map<String, RpcConsumerHandler> handlerMap = new ConcurrentHashMap<>();

    private RpcConsumer() {
        bootstrap = new Bootstrap();
        eventLoopGroup = new NioEventLoopGroup(4);
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                .handler(new RpcConsumerInitializer());
    }

    public static RpcConsumer getInstance() {
        if (instance == null) {
            synchronized (RpcConsumer.class) {
                if (instance == null) {
                    instance = new RpcConsumer();
                }
            }
        }
        return instance;
    }

    public void close() {
        eventLoopGroup.shutdownGracefully();
    }

    /**
     * 发送请求
     */
    public RPCFuture sendRequest(RpcProtocol<RpcRequest> protocol)throws  Exception {
        //TODO 暂时写死,后续引入注册中心后,从注册中心获取服务地址
        String server = "127.0.0.1";
        int port = 27880;
        String key = server.concat("_").concat(String.valueOf(port));
        RpcConsumerHandler handler = handlerMap.get(key);
        //TODO 后续优化思路:连接池化,异步检查,错误重拾机制
        if (handler == null) {
            handler=getRpcConsumerHandler(server,port);
            handlerMap.put(key,handler);
        } else if (!handler.getChannel().isActive()) {
            handler.close();
            handler=getRpcConsumerHandler(server,port);
            handlerMap.put(key,handler);
        }
        return handler.sendRequest(protocol);
    }

    private RpcConsumerHandler getRpcConsumerHandler(String server, int port) {
        //TODO 加入连接异常检测机制
        ChannelFuture channelFuture = bootstrap.connect(server, port);
        channelFuture.addListener((ChannelFutureListener) listener -> {
            if (listener.isSuccess()) {
                logger.info("connect rpc server {} on port {} success", server, port);
            } else {
                logger.error("connect rpc server {} on port {} failed", server, port);
                channelFuture.cause().printStackTrace();
                eventLoopGroup.shutdownGracefully();
            }
        });
        return channelFuture.channel().pipeline().get(RpcConsumerHandler.class);
    }

}
