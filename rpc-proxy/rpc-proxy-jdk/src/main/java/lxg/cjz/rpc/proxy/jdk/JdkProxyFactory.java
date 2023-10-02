package lxg.cjz.rpc.proxy.jdk;

import lxg.cjz.rpc.proxy.api.BaseProxyFactory;
import lxg.cjz.rpc.proxy.api.ProxyFactory;
import lxg.cjz.rpc.proxy.api.consumer.Consumer;
import lxg.cjz.rpc.proxy.api.object.ObjectProxy;

import java.lang.reflect.Proxy;
import java.util.Objects;

public class JdkProxyFactory<T> extends BaseProxyFactory<T> {


    @Override
    public <T> T getProxy(Class<T> clazz) {
        //TODO
        return (T) Proxy.newProxyInstance(
                        clazz.getClassLoader(),
                        new Class<?>[]{clazz},
                        objectProxy);
    }
}
