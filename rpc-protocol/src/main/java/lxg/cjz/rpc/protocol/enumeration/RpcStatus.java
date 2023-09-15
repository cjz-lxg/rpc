package lxg.cjz.rpc.protocol.enumeration;

/**
 * @author russel
 * @version 1.0.0
 * @date 2023/9/15
 * @description
 */
public enum RpcStatus {

    SUCCESS(0),
    FAIL(1);

    private final int code;

    RpcStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
