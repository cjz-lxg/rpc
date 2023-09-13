package lxg.cjz.rpc.provider;

import lxg.cjz.rpc.common.scanner.server.RpcServiceScanner;
import lxg.cjz.rpc.provider.common.server.base.BaseServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author russel
 * @version 1.0.0
 * @date 2023/9/12
 * @description
 */
public class RpcSingleServer extends BaseServer {

    private final static Logger logger = LoggerFactory.getLogger(RpcSingleServer.class);

    public RpcSingleServer(String serverAddress, String scanPackage) {
        super(serverAddress);
        try {
            this.handlerMap= RpcServiceScanner.doScannerWithRpcServiceAnnotationFilterAndRegistryService(scanPackage);
        } catch (Exception e) {
            logger.error("RPC服务初始化错误=========>", e);
        }
    }

        @Override
        public void startNettyServer() {
            super.startNettyServer();
        }
}
