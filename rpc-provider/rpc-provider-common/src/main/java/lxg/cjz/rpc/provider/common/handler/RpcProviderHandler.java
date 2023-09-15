package lxg.cjz.rpc.provider.common.handler;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.util.JSONPObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lxg.cjz.rpc.protocol.RpcProtocol;
import lxg.cjz.rpc.protocol.enumeration.RpcType;
import lxg.cjz.rpc.protocol.header.RpcHeader;
import lxg.cjz.rpc.protocol.request.RpcRequest;
import lxg.cjz.rpc.protocol.response.RpcResponse;
import lxg.cjz.rpc.serialization.api.Serialization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author russel
 * @version 1.0.0
 * @date 2023/9/12
 * @description
 */
public class RpcProviderHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcRequest>> {
    private final Logger logger = LoggerFactory.getLogger(RpcProviderHandler.class);

    private final Map<String, Object> handlerMap;

    public RpcProviderHandler(Map<String, Object> handlerMap){
        this.handlerMap = handlerMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcProtocol<RpcRequest> protocol) throws Exception {
        logger.info("RPC提供者收到的数据为====>>> " + JSONObject.toJSONString(protocol));
        logger.info("handlerMap中存放的数据如下所示：");
        for(Map.Entry<String, Object> entry : handlerMap.entrySet()){
            logger.info(entry.getKey() + " === " + entry.getValue());
        }
        RpcHeader header = protocol.getHeader();
        RpcRequest rpcRequest = protocol.getBody();
        header.setMessageType(((byte) RpcType.RESPONSE.getType()));
        RpcProtocol<RpcResponse> rpcResponseRpcProtocol = new RpcProtocol<>();
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setResult("交互成功");
        rpcResponse.setAsync(rpcRequest.isAsync());
        rpcResponse.setOneWay(rpcRequest.isOneWay());
        rpcResponseRpcProtocol.setHeader(header);
        rpcResponseRpcProtocol.setBody(rpcResponse);
        //直接返回数据
        ctx.writeAndFlush(rpcResponseRpcProtocol);
    }
}
