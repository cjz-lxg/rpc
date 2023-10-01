package lxg.cjz.rpc.proxy.api.async;

import lxg.cjz.rpc.proxy.api.future.RPCFuture;

public interface IAsyncObjectProxy {

    public RPCFuture call(String funcName, Object... args);
}
