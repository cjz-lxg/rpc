package lxg.cjz.rpc;

import lxg.cjz.rpc.consumer.common.RpcConsumer;
import lxg.cjz.rpc.proxy.api.async.IAsyncObjectProxy;
import lxg.cjz.rpc.proxy.api.config.ProxyConfig;
import lxg.cjz.rpc.proxy.api.object.ObjectProxy;
import lxg.cjz.rpc.proxy.jdk.JdkProxyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcClient {
    private final Logger logger = LoggerFactory.getLogger(RpcClient.class);
    /**
     * 服务版本
     */
    private String serviceVersion;
    /**
     * 服务分组
     */
    private String serviceGroup;
    /**
     * 序列化类型
     */
    private String serializationType;
    /**
     * 超时时间
     */
    private long timeout;
    /**
     * 是否异步调用
     */
    private boolean async;
    /**
     * 是否单向调用
     */
    private boolean oneway;
    public RpcClient(String serviceVersion, String serviceGroup, String serializationType, long timeout, boolean async, boolean oneway) {
        this.serviceVersion = serviceVersion;
        this.timeout = timeout;
        this.serviceGroup = serviceGroup;
        this.serializationType = serializationType;
        this.async = async;
        this.oneway = oneway;
    }
    public <T> T create(Class<T> interfaceClass) {
        JdkProxyFactory<T> proxyFactory = new JdkProxyFactory<>();
        ProxyConfig<T> proxyConfig = new ProxyConfig<T>(interfaceClass, serviceVersion, serviceGroup, timeout, RpcConsumer.getInstance(), serializationType, async, oneway);
        proxyFactory.init(proxyConfig);
        return proxyFactory.getProxy(interfaceClass);
    }

    public <T> IAsyncObjectProxy createAsync(Class<T> interfaceClass) {
        return new ObjectProxy<T>(interfaceClass, serviceVersion, serviceGroup, timeout, RpcConsumer.getInstance(), serializationType, async, oneway);
    }

    public void shutdown() {
        RpcConsumer.getInstance().close();
    }
}
