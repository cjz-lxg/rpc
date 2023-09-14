package lxg.cjz.rpc.protocol.header;

import lxg.cjz.rpc.common.id.IdFactory;
import lxg.cjz.rpc.constants.RpcConstants;
import lxg.cjz.rpc.protocol.enumeration.RpcType;

/**
 * @author russel
 * @version 1.0.0
 * @date 2023/9/14
 * @description
 */
public class RpcHeaderFactory {
    public static RpcHeader getRequestHeader(String serializationType) {
        RpcHeader rpcHeader = new RpcHeader();
        long requestId = IdFactory.getId();
        rpcHeader.setRequestId(requestId);
        rpcHeader.setSerializationType(serializationType);
        rpcHeader.setMagic(RpcConstants.MAGIC);
        rpcHeader.setMessageType((byte) RpcType.REQUEST.getType());
        rpcHeader.setStatus((byte) 0x1);
        return rpcHeader;
    }
}
