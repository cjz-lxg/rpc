package lxg.cjz.rpc.codec;

import lxg.cjz.rpc.jdk.JdkSerialization;
import lxg.cjz.rpc.serialization.api.Serialization;

/**
 * @author russel
 * @version 1.0.0
 * @date 2023/9/14
 * @description
 */
public interface RpcCodec {
    default Serialization getJdkSerialization() {
        return new JdkSerialization();
    }
}
