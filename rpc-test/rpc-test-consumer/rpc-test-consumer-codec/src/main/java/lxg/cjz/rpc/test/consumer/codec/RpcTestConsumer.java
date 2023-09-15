package lxg.cjz.rpc.test.consumer.codec;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lxg.cjz.rpc.test.consumer.codec.init.RpcTestConsumerInitializer;

/**
 * @author russel
 * @version 1.0.0
 * @date 2023/9/15
 * @description
 */
public class RpcTestConsumer {
    public static void main(String[] args) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup eventExecutors = new NioEventLoopGroup(4);
        try {
            bootstrap.group(eventExecutors)
                    .channel(NioSocketChannel.class)
                    .handler(new RpcTestConsumerInitializer());
            bootstrap.connect("127.0.0.1", 27880).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            Thread.sleep(2000);
            eventExecutors.shutdownGracefully();
        }
    }
}
