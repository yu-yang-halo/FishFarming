package cn.netty.farmingsocket;


import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class SocketDecoder extends ByteToMessageDecoder { // (1)
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) { // (2)
    	  
    	if (in.readableBytes() < 26) {
            return;
        }

    	byte[] headBytes=new byte[26];

        in.readBytes(headBytes,0,26);
        short length=(short) ((headBytes[21]<<8)|(headBytes[22]&0xFF));

        byte[] finshPackets=new byte[length+26];
        System.arraycopy(headBytes,0,finshPackets,0,headBytes.length);
        if(length>0){
            byte[] bodyBytes=new byte[length];
            in.readBytes(bodyBytes,0,length);
            System.arraycopy(bodyBytes,0,finshPackets,26,bodyBytes.length);
        }

        System.out.println("读取了一个完整的包");
        out.add(new SPackage(finshPackets));




    }
    
    
}