package lxg.cjz.rpc.test.consumer.handler;

import lxg.cjz.rpc.consumer.common.RpcConsumer;
import lxg.cjz.rpc.consumer.common.common.future.RPCFuture;
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
        RPCFuture result = consumer.sendRequest(getRpcRequestProtocol());
        logger.info("result:{}", result);
        //Thread.sleep(2000);
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
