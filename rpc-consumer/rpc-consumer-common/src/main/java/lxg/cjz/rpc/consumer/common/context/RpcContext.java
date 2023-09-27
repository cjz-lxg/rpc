package lxg.cjz.rpc.consumer.common.context;

import lxg.cjz.rpc.consumer.common.future.RPCFuture;

/**
 * @author russel
 * @version 1.0.0
 * @date 2023/9/26
 * @description
 */
public class RpcContext {

    private RpcContext() {

    }

    private static final RpcContext AGENT = new RpcContext();

    private static final InheritableThreadLocal<RPCFuture> RPC_FUTURE_INHERITABLE_THREAD_LOCAL
            = new InheritableThreadLocal<>();

    public static RpcContext getContext() {
        return AGENT;
    }

    public void setRpcFuture(RPCFuture rpcFuture) {
        RPC_FUTURE_INHERITABLE_THREAD_LOCAL.set(rpcFuture);
    }

    public RPCFuture getRpcFuture() {
        return RPC_FUTURE_INHERITABLE_THREAD_LOCAL.get();
    }

    public void removeRpcFuture() {
        RPC_FUTURE_INHERITABLE_THREAD_LOCAL.remove();
    }

}
