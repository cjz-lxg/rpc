package lxg.cjz.rpc.test.scanner.service.impl;

import lxg.cjz.rpc.annotation.RpcReference;
import lxg.cjz.rpc.test.scanner.service.ConsumerBusinessService;
import lxg.cjz.rpc.test.scanner.service.DemoService;

/**
 * @author russel
 * @version 1.0.0
 * @date 2023/9/11
 * @description
 */
public class ConsumerBusinessServiceImpl implements ConsumerBusinessService {
    @RpcReference(registryType = "zookeeper", registryAddress = "127.0.0.1:2181", version = "1.0.0", group = "cjz")
    private DemoService demoService;

}
