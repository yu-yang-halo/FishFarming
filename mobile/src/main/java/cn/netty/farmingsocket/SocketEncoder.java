package cn.netty.farmingsocket;


import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

public class SocketEncoder extends MessageToByteEncoder<SPackage>{

	@Override
	protected void encode(ChannelHandlerContext ctx, SPackage msg, ByteBuf out) throws Exception {

		out.writeBytes(msg.getNeedSendDataPackages());
	}

    
}