package lxg.cjz.rpc.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lxg.cjz.rpc.common.utils.SerializationUtils;
import lxg.cjz.rpc.constants.RpcConstants;
import lxg.cjz.rpc.protocol.enumeration.RpcType;

import java.nio.charset.StandardCharsets;
import java.util.List;

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
        String serialization = SerializationUtils.getSubString(serializationTypeByteBuf.toString(StandardCharsets.UTF_8));
        int dataLength = in.readInt();
        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
            return;
        }
        byte[] data = new byte[dataLength];
        in.readBytes(data);

        if (RpcType.findByType(msgType) == null) {
            return;
        }



    }
}
