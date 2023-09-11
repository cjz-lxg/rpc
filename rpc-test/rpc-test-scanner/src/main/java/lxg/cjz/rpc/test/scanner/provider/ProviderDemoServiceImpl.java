package lxg.cjz.rpc.test.scanner.provider;


import lxg.cjz.rpc.annotation.RpcService;
import lxg.cjz.rpc.test.scanner.service.DemoService;

/**
 * @author russel
 * @version 1.0.0
 * @date 2023/8/28
 * @description
 */
@RpcService(interfaceClass = DemoService.class, interfaceClassName = "cjz.lxg.rpc.test.scanner.service.DemoService", version = "1.0.0", group = "cjz")
public class ProviderDemoServiceImpl implements DemoService {

}
