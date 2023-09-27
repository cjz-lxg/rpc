package lxg.cjz.rpc.test.provider.service.impl;

import lxg.cjz.rpc.annotation.RpcService;
import lxg.cjz.rpc.test.api.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author russel
 * @version 1.0.0
 * @date 2023/9/12
 * @description
 */
@RpcService(interfaceClass = DemoService.class,
        interfaceClassName = "lxg.cjz.rpc.test.api.DemoService",
        version = "1.0.0",
        group = "cjz")
public class ProviderDemoServiceImpl implements DemoService {

    private static final Logger logger = LoggerFactory.getLogger(ProviderDemoServiceImpl.class);
    @Override
    public String hello(String name) {
        logger.info("receive the argument:[{}]", name);
        return "Hello " + name;
    }
}
