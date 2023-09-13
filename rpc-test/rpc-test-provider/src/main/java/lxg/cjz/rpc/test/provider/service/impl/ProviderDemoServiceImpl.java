package lxg.cjz.rpc.test.provider.service.impl;

import lxg.cjz.rpc.annotation.RpcService;
import lxg.cjz.rpc.test.provider.service.DemoService;

/**
 * @author russel
 * @version 1.0.0
 * @date 2023/9/12
 * @description
 */
@RpcService(interfaceClass = DemoService.class,
        interfaceClassName = "lxg.cjz.rpc.test.scanner.service.DemoService",
        version = "1.0.0",
        group = "vjz")
public class ProviderDemoServiceImpl {
}
