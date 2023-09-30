package lxg.cjz.rpc.proxy.jdk;

import lxg.cjz.rpc.proxy.api.consumer.Consumer;
import lxg.cjz.rpc.proxy.api.object.ObjectProxy;

import java.lang.reflect.Proxy;
import java.util.Objects;

public class JdkProxyFactory<T> {

    /**
     * 服务版本号
     */
    private String serviceVersion;
    /**
     * 服务分组
     */
    private String serviceGroup;
    /**
     * 超时时间，默认15s
     */
    private long timeout = 15000;
    /**
     * 服务消费者
     */
    private Consumer consumer;
    /**
     * 序列化类型
     */
    private String serializationType;

    /**
     * 是否异步调用
     */
    private boolean async;
    /**
     * 是否单向调用
     */
    private boolean oneway;

    public JdkProxyFactory(String serviceVersion, String serviceGroup, String serializationType, long timeout, Consumer consumer, boolean async, boolean oneway) {
        this.serviceVersion = serviceVersion;
        this.timeout = timeout;
        this.serviceGroup = serviceGroup;
        this.consumer = consumer;
        this.serializationType = serializationType;
        this.async = async;
        this.oneway = oneway;
    }
    public <T> T getProxy(Class<T> clazz) {
        Object instance = Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class<?>[]{clazz},
                new ObjectProxy<T>(clazz, serviceVersion, serviceGroup, timeout, consumer, serializationType, async, oneway)
        );
        return (T) instance;
    }
}
