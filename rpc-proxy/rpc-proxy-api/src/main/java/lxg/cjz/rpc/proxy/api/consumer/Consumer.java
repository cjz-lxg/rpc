package lxg.cjz.rpc.proxy.api.consumer;

import lxg.cjz.rpc.protocol.RpcProtocol;
import lxg.cjz.rpc.protocol.request.RpcRequest;
import lxg.cjz.rpc.proxy.api.future.RPCFuture;

public interface Consumer {

    RPCFuture sendRequest(RpcProtocol<RpcRequest> protocol) throws Exception;

}
