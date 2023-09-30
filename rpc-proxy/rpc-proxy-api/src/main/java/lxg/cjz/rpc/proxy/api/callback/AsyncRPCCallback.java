package lxg.cjz.rpc.proxy.api.callback;

/**
 * @author russel
 * @version 1.0.0
 * @date 2023/9/27
 * @description
 */
public interface AsyncRPCCallback {
    void onSuccess(Object result);

    void onException(Exception e);
}
