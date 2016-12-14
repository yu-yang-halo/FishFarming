package cn.netty.farmingsocket;


import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class SocketDecoder extends ByteToMessageDecoder { // (1)
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) { // (2)
    	  
    	if (in.readableBytes() < 25) {
            return;
        }
    	
    	byte[] bytes=new byte[in.readableBytes()];
    	
    	in.readBytes(bytes);
    	
        out.add(new SPackage(bytes));
    }
    
    
}