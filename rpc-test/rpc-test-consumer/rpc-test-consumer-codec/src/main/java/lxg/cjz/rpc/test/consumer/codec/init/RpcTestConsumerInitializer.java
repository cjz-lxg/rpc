package lxg.cjz.rpc.test.consumer.codec.init;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import lxg.cjz.rpc.codec.RpcDecoder;
import lxg.cjz.rpc.codec.RpcEncoder;
import lxg.cjz.rpc.test.consumer.codec.handler.RpcTestConsumerHandler;

/**
 * @author russel
 * @version 1.0.0
 * @date 2023/9/15
 * @description
 */
public class RpcTestConsumerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new RpcEncoder());
        pipeline.addLast(new RpcDecoder());
        pipeline.addLast(new RpcTestConsumerHandler());
    }
}
