package lxg.cjz.rpc.protocol.header;

import java.io.Serializable;

/**
 * @author russel
 * @version 1.0.0
 * @date 2023/9/14
 * @description
 */
public class RpcHeader implements Serializable {
    private static final long serialVersionUID = 6011436680686290298L;

    /*
    +---------------------------------------------------------------+
    | 魔数 2byte | 报文类型 1byte | 状态 1byte | 消息 ID 8byte    |
    +---------------------------------------------------------------+
    |           序列化类型 16byte      | 数据长度 4byte    |
    +---------------------------------------------------------------+
    */

    private short magic;
    private byte messageType;
    private byte status;
    private long requestId;
    private String serializationType;
    private int dataLength;

    public short getMagic() {
        return magic;
    }

    public void setMagic(short magic) {
        this.magic = magic;
    }

    public byte getMessageType() {
        return messageType;
    }

    public void setMessageType(byte messageType) {
        this.messageType = messageType;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public String getSerializationType() {
        return serializationType;
    }

    public void setSerializationType(String serializationType) {
        this.serializationType = serializationType;
    }

    public int getDataLength() {
        return dataLength;
    }

    public void setDataLength(int dataLength) {
        this.dataLength = dataLength;
    }
}
