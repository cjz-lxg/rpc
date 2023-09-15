package lxg.cjz.rpc.common.helper;

/**
 * @author russel
 * @version 1.0.0
 * @date 2023/9/15
 * @description
 */
public class RpcServiceHelper {

    public static String buildServiceKey(String serviceName, String serviceVersion,String ServiceGroup) {
        return String.join("#", serviceName, serviceVersion,ServiceGroup);
    }
}
