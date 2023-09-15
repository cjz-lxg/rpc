package lxg.cjz.rpc.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lxg.cjz.rpc.common.utils.SerializationUtils;
import lxg.cjz.rpc.constants.RpcConstants;
import lxg.cjz.rpc.protocol.RpcProtocol;
import lxg.cjz.rpc.protocol.enumeration.RpcType;
import lxg.cjz.rpc.protocol.header.RpcHeader;
import lxg.cjz.rpc.protocol.request.RpcRequest;
import lxg.cjz.rpc.protocol.response.RpcResponse;
import lxg.cjz.rpc.serialization.api.Serialization;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

/**
 * @author russel
 * @version 1.0.0
 * @date 2023/9/14
 * @description
 */
public class RpcDecoder extends ByteToMessageDecoder implements RpcCodec {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < RpcConstants.MESSAGE_HEADER_SIZE) {
            return;
        }
        in.markReaderIndex();

        short magic = in.readShort();
        if (magic != RpcConstants.MAGIC) {
            throw new IllegalArgumentException("非法魔数:" + magic);
        }
        byte msgType = in.readByte();
        byte status = in.readByte();
        long requestId = in.readLong();
        ByteBuf serializationTypeByteBuf = in.readBytes(SerializationUtils.MAX_SERIALIZATION_TYPE_COUNT);
        String serializationType = SerializationUtils.getSubString(serializationTypeByteBuf.toString(StandardCharsets.UTF_8));
        int dataLength = in.readInt();
        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
            return;
        }
        byte[] data = new byte[dataLength];
        in.readBytes(data);

        RpcHeader rpcHeader = new RpcHeader();
        rpcHeader.setMagic(magic);
        rpcHeader.setStatus(status);
        rpcHeader.setRequestId(requestId);
        rpcHeader.setMessageType(msgType);
        rpcHeader.setSerializationType(serializationType);
        rpcHeader.setDataLength(dataLength);
        //TODO Serialization是拓展点
        Serialization serialization = getJdkSerialization();
        switch (Objects.requireNonNull(RpcType.findByType(msgType))) {
            case REQUEST:
                RpcRequest rpcRequest = serialization.deserialize(data, RpcRequest.class);
                Objects.requireNonNull(rpcRequest);
                RpcProtocol<RpcRequest> rpcProtocol = new RpcProtocol<>();
                rpcProtocol.setHeader(rpcHeader);
                rpcProtocol.setBody(rpcRequest);
                out.add(rpcProtocol);
                break;
            case RESPONSE:
                RpcResponse rpcResponse = serialization.deserialize(data, RpcResponse.class);
                Objects.requireNonNull(rpcResponse);
                RpcProtocol<RpcResponse> rpcResponseRpcProtocol = new RpcProtocol<>();
                rpcResponseRpcProtocol.setHeader(rpcHeader);
                rpcResponseRpcProtocol.setBody(rpcResponse);
                out.add(rpcResponseRpcProtocol);
                break;
            case HEARTBEAT:
                //TODO
                break;
            default:
                break;
        }
    }
}
