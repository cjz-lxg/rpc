package lxg.cjz.rpc.test.provider.single;

import lxg.cjz.rpc.constants.RpcConstants;
import lxg.cjz.rpc.provider.RpcSingleServer;
import org.junit.Test;

/**
 * @author russel
 * @version 1.0.0
 * @date 2023/9/12
 * @description
 */
public class RpcSingleServerTest {
    @Test
    public void startRpcSingleServer(){
        RpcSingleServer singleServer = new RpcSingleServer("127.0.0.1:27880", "lxg.cjz.rpc.test", RpcConstants.REFLECT_TYPE_CGLIB);
        singleServer.startNettyServer();
    }
}