package lxg.cjz.rpc.proxy.api;

import lxg.cjz.rpc.proxy.api.config.ProxyConfig;
import lxg.cjz.rpc.proxy.api.object.ObjectProxy;

public abstract class BaseProxyFactory<T> implements ProxyFactory {
    protected ObjectProxy<T> objectProxy;

    @Override
    public <T> void init(ProxyConfig<T> proxyConfig) {
        //TODO 丑陋
        this.objectProxy = new ObjectProxy(
                proxyConfig.getClazz(),
                proxyConfig.getServiceVersion(),
                proxyConfig.getServiceGroup(),
                proxyConfig.getTimeout(),
                proxyConfig.getConsumer(),
                proxyConfig.getSerializationType(),
                proxyConfig.isAsync(),
                proxyConfig.isOneway());

    }
}
