package lxg.cjz.rpc.consumer.common.initializer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import lxg.cjz.rpc.codec.RpcDecoder;
import lxg.cjz.rpc.codec.RpcEncoder;
import lxg.cjz.rpc.consumer.common.handler.RpcConsumerHandler;


/**
 * @author russel
 * @version 1.0.0
 * @date 2023/9/16
 * @description
 */
public class RpcConsumerInitializer extends ChannelInitializer<SocketChannel> {


    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new RpcEncoder())
                .addLast(new RpcDecoder())
                .addLast(new RpcConsumerHandler());
    }
}
