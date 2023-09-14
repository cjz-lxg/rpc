package lxg.cjz.rpc.jdk;

import lxg.cjz.rpc.common.exception.SerializerException;
import lxg.cjz.rpc.serialization.api.Serialization;

import java.io.*;

/**
 * @author russel
 * @version 1.0.0
 * @date 2023/9/14
 * @description
 */
public class JdkSerialization implements Serialization {
    @Override
    public <T> byte[] serialize(T obj) {
        if (obj == null) {
            throw new SerializerException("序列化对象为null")
        }
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(os);
            out.writeObject(obj);
            return os.toByteArray();
        } catch (IOException e) {
            throw new SerializerException(e.getMessage(), e);
        }
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> cls) {
        if (data == null) {
            throw new SerializerException("反序列化数据为null");
        }
        try {
            ByteArrayInputStream is = new ByteArrayInputStream(data);
            ObjectInputStream in = new ObjectInputStream(is);
            Object obj = in.readObject();
            if (cls.isInstance(obj)) {
                return cls.cast(obj);
            } else {
                throw new Exception("反序列化对象与类型不匹配");
            }
        } catch (Exception e) {
            throw new SerializerException(e.getMessage(), e);
        }
    }
}
