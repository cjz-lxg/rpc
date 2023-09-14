package lxg.cjz.rpc.protocol;

import lxg.cjz.rpc.protocol.header.RpcHeader;

import java.io.Serializable;

/**
 * @author russel
 * @version 1.0.0
 * @date 2023/9/14
 * @description
 */
public class RpcProtocol<T> implements Serializable {
    private static final long serialVersionUID = 292789485166173277L;

    private RpcHeader header;
    private T body;

    public RpcHeader getHeader() {
        return header;
    }

    public void setHeader(RpcHeader header) {
        this.header = header;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}
