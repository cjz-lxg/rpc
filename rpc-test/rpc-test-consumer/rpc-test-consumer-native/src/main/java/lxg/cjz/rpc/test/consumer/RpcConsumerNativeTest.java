package lxg.cjz.rpc.test.consumer;

import lxg.cjz.rpc.RpcClient;
import lxg.cjz.rpc.proxy.api.async.IAsyncObjectProxy;
import lxg.cjz.rpc.proxy.api.future.RPCFuture;
import lxg.cjz.rpc.test.api.DemoService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcConsumerNativeTest {
    private static final Logger logger = LoggerFactory.getLogger(RpcConsumerNativeTest.class);

    public static void main(String[] args) {
        RpcClient rpcClient = new RpcClient("1.0.0", "cjz", "jdk", 3000, false, false);
        DemoService demoService = rpcClient.create(DemoService.class);
        String result = demoService.hello("cjz");
        logger.info("返回的结果数据===>>> " + result);
        rpcClient.shutdown();
    }

    @Test
    public void testAsyncInterFacRpc() throws Exception {
RpcClient rpcClient = new RpcClient("1.0.0", "cjz", "jdk", 3000, false, false);
        IAsyncObjectProxy demoService = rpcClient.createAsync(DemoService.class);
        RPCFuture future = demoService.call("hello", "cjz");
        logger.info("返回的结果数据===>>> " + future.get());
        rpcClient.shutdown();
    }
}
