package lxg.cjz.rpc.protocol.response;

import lxg.cjz.rpc.protocol.base.RpcMessage;

/**
 * @author russel
 * @version 1.0.0
 * @date 2023/9/14
 * @description
 */
public class RpcResponse extends RpcMessage {
    private static final long serialVersionUID = 425335064405584525L;
    /**
     * 返回结果
     */
    private Object result;
    /**
     * 异常信息
     */
    private Exception exception;

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception e) {
        this.exception = e;
    }
}
