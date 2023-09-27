package lxg.cjz.rpc.test.consumer.handler;

import lxg.cjz.rpc.consumer.common.RpcConsumer;
import lxg.cjz.rpc.consumer.common.callback.AsyncRPCCallback;
import lxg.cjz.rpc.consumer.common.future.RPCFuture;
import lxg.cjz.rpc.protocol.RpcProtocol;
import lxg.cjz.rpc.protocol.header.RpcHeaderFactory;
import lxg.cjz.rpc.protocol.request.RpcRequest;
import org.slf4j.Logger;

/**
 * @author russel
 * @version 1.0.0
 * @date 2023/9/16
 * @description
 */
public class RpcConsumerHandlerTest {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(RpcConsumerHandlerTest.class);
    public static void main(String[] args) throws Exception {
        RpcConsumer consumer = RpcConsumer.getInstance();
        RPCFuture rpcFuture = consumer.sendRequest(getRpcRequestProtocol());
        rpcFuture.addCallback(new AsyncRPCCallback() {
            @Override
            public void onSuccess(Object result) {
                logger.info("从服务消费者获取到的数据===>>>" + result);
            }
            @Override
            public void onException(Exception e) {
                logger.info("抛出了异常===>>>" + e);
            }
        });
        Thread.sleep(200);
        //rpcFuture.get();
        consumer.close();
    }

    private static RpcProtocol<RpcRequest> getRpcRequestProtocol() {
        //模拟发送数据
        RpcProtocol<RpcRequest> protocol = new RpcProtocol<>();
        protocol.setHeader(RpcHeaderFactory.getRequestHeader("jdk"));
        RpcRequest request = new RpcRequest();
        request.setClassName("lxg.cjz.rpc.test.api.DemoService");
        request.setGroup("cjz");
        request.setMethodName("hello");
        request.setParameters(new Object[]{"cjz"});
        request.setParameterTypes(new Class[]{String.class});
        request.setVersion("1.0.0");
        request.setAsync(false);
        request.setOneWay(false);
        protocol.setBody(request);
        return protocol;
    }
}
