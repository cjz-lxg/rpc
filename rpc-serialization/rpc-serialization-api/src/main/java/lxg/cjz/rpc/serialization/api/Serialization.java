package lxg.cjz.rpc.serialization.api;

/**
 * @author russel
 * @version 1.0.0
 * @date 2023/9/14
 * @description
 */
public interface Serialization {
    /**
     * 序列化
     */
    <T> byte[] serialize(T obj);

    <T> T deserialize(byte[] data, Class<T> cls);


}
