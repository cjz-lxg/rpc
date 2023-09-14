package lxg.cjz.rpc.protocol.base;

import java.io.Serializable;

/**
 * @author russel
 * @version 1.0.0
 * @date 2023/9/14
 * @description
 */
public class RpcMessage implements Serializable {
    /**
     * 是否单向发送
     */
    private boolean oneWay;
    /**
     * 是否异步发送
     */
    private boolean async;

    public boolean isOneWay() {
        return oneWay;
    }

    public void setOneWay(boolean oneWay) {
        this.oneWay = oneWay;
    }

    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }
}
