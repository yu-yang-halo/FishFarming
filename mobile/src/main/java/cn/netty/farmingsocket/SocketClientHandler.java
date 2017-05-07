package cn.netty.farmingsocket;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import cn.netty.farmingsocket.SPackage.DeviceType;
import cn.netty.farmingsocket.data.ICmdPackageProtocol;
import cn.netty.farmingsocket.data.IDataCompleteCallback;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;

public class SocketClientHandler extends ChannelInboundHandlerAdapter implements ICmdPackageProtocol{
	private static final ByteBuf HEARTBEAT_SEQUENCE = Unpooled
			.unreleasableBuffer(Unpooled.copiedBuffer("Heartbeat", CharsetUtil.UTF_8)); // 1
	private ChannelHandlerContext currentContext;
	private Set<IDataCompleteCallback> completeCallbacks;


	private String deviceID;
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		this.currentContext=ctx;
		System.out.println("userEventTriggered....");
		if (evt instanceof IdleStateEvent) { // 2
			IdleStateEvent event = (IdleStateEvent) evt;
			String type = "";
			if (event.state() == IdleState.READER_IDLE) {
				type = "read idle";
			} else if (event.state() == IdleState.WRITER_IDLE) {
				type = "write idle";
			} else if (event.state() == IdleState.ALL_IDLE) {
				type = "all idle";
			}
			sendFuckHeart(ctx);
			System.out.println(ctx.channel().remoteAddress() + "超时类型：" + type);
		} else {
			super.userEventTriggered(ctx, evt);
		}
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelActive(ctx);
		this.currentContext=ctx;

		System.out.println("TimeClientHandler active");
		sendFuckHeart(ctx);
        modeStatusSetOrGet(ICmdPackageProtocol.MethodType.GET, ICmdPackageProtocol.MANUAL_MODE, null);
		rangSetOrGet(ICmdPackageProtocol.MethodType.GET, 0, 0, null);
		timeSetOrGet(MethodType.GET,(short) 0,null);

	}



	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		super.channelUnregistered(ctx);
		System.out.println("channel 已被注销");
		if(completeCallbacks!=null){
			completeCallbacks.clear();
			completeCallbacks=null;
		}

	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
		Log.e("channelInactive","连接已断开");
		if(this.completeCallbacks!=null&&this.completeCallbacks.size()>0){
			for (IDataCompleteCallback callback:completeCallbacks){
				callback.onDataComplete(null);
			}
		}

	}

	public void sendFuckHeart(ChannelHandlerContext ctx) {
		if(ctx==null){
			ctx=currentContext;
		}
		SPackage data0 = new SPackage(DeviceType.Water, deviceID,
				new byte[] { 0x12, 0x78, (byte) 0xA0, (byte) 0x9C, 0x00, 0x00, 0x00, 0x00 }, (short) 0x0002,
				(byte) 0x00, (short) 1);

		data0.setContents(new byte[] { (byte) 0xFF });
		ctx.writeAndFlush(data0).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);


		SPackage data1 = new SPackage(DeviceType.Android, deviceID,
				new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 }, (short) 0x0001, (byte) 0x00, (short) 0);

		ctx.writeAndFlush(data1).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
		//ctx.writeAndFlush(data0).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);


	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		this.currentContext=ctx;
		SPackage m = (SPackage) msg;
		if(this.completeCallbacks!=null&&this.completeCallbacks.size()>0){
			for (IDataCompleteCallback callback:completeCallbacks){
				callback.onDataComplete(m);
			}
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}

	@Override
	public void registerDataCompleteCallback(IDataCompleteCallback callback,boolean tempYN){
		if(completeCallbacks==null){
			completeCallbacks=new HashSet<>();
		}
		if(callback==null){
			return;
		}
		completeCallbacks.add(callback);

	}


	@Override
	public void closeContext() {

		if(currentContext!=null){
			if(!currentContext.isRemoved()){

				currentContext.close();
				System.out.println("连接手动关闭完成");
			}else {
				System.out.println("连接已经被关闭...");
			}
		}
	}

	@Override
	public void sendFuckHeart() {
		// TODO Auto-generated method stub
		if(currentContext==null||currentContext.isRemoved()){
			return;
		}
		SPackage data1 = new SPackage(DeviceType.Android, deviceID,
				new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 }, (short) 0x0001, (byte) 0x00, (short) 0);

		currentContext.writeAndFlush(data1).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);

	}

	@Override
	public void sendFuckControlCmd(int num, int cmdStatus, String deviceId,IDataCompleteCallback complete) {
		if(currentContext==null||currentContext.isRemoved()){

			return;
		}
		registerDataCompleteCallback(complete,true);

		SPackage controlCmdData = new SPackage(DeviceType.Android, deviceID,
				new byte[] { 0x12, 0x78,(byte)0xA0, (byte)0x9C, 0x00, 0x00, 0x00, 0x00 },
				(short) 0x10,
				(byte) 0x02, (short) 0x0002);

		controlCmdData.setContents(new byte[]{(byte)num, (byte) cmdStatus});

		currentContext.writeAndFlush(controlCmdData).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
	}

	/**
	 * mode 0xAC表示自动模式，0xDC表示手动模式
	 * @param type
	 * @param mode
     */
	@Override
	public void modeStatusSetOrGet(MethodType type,short mode,IDataCompleteCallback completeCallback){
		if(currentContext==null||currentContext.isRemoved()){
			return;
		}
		registerDataCompleteCallback(completeCallback,true);
		if(type==MethodType.GET){
			SPackage controlCmdData = new SPackage(DeviceType.Android, deviceID,
					new byte[] { (byte)0xFF,(byte)0xFF,(byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF },
					(short) 21,
					(byte) 0x01, (short) 0);


			currentContext.writeAndFlush(controlCmdData).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
		}else{
			SPackage controlCmdData = new SPackage(DeviceType.Android, deviceID,
					new byte[] { (byte)0xFF,(byte)0xFF,(byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF },
					(short) 21,
					(byte) 0x02, (short) 2);

			controlCmdData.setContents(new byte[]{(byte)0x05, (byte) mode});


			currentContext.writeAndFlush(controlCmdData).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
		}



	}

	@Override
	public void timeSetOrGet(MethodType type, short time, IDataCompleteCallback completeCallback) {
		if(currentContext==null||currentContext.isRemoved()){
			return;
		}
		registerDataCompleteCallback(completeCallback,true);
		if(type==MethodType.GET){
			SPackage controlCmdData = new SPackage(DeviceType.Android, deviceID,
					new byte[] { (byte)0xFF,(byte)0xFF,(byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF },
					(short) 24,
					(byte) 0x01, (short) 0);


			currentContext.writeAndFlush(controlCmdData).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
		}else{
			SPackage controlCmdData = new SPackage(DeviceType.Android, deviceID,
					new byte[] { (byte)0xFF,(byte)0xFF,(byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF },
					(short) 24,
					(byte) 0x02, (short) 3);

			controlCmdData.setContents(new byte[]{(byte)0xA5,(byte)0x5A, (byte) time});


			currentContext.writeAndFlush(controlCmdData).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
		}

	}

	@Override
	public void rangSetOrGet(MethodType type , int max, int min,IDataCompleteCallback completeCallback){
		if(currentContext==null||currentContext.isRemoved()){
			return;
		}
		registerDataCompleteCallback(completeCallback,true);
		if(type==MethodType.GET){
			SPackage controlCmdData = new SPackage(DeviceType.Android, deviceID,
					new byte[] { (byte)0xFF,(byte)0xFF,(byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF },
					(short) 19,
					(byte) 0x01, (short) 0);


			currentContext.writeAndFlush(controlCmdData).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
		}else{
			SPackage controlCmdData = new SPackage(DeviceType.Android, deviceID,
					new byte[] { (byte)0xFF,(byte)0xFF,(byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF },
					(short) 19,
					(byte) 0x02, (short) 4);

			controlCmdData.setContents(new byte[]{(byte)0xAA,0x55, (byte) (max&0xFF), (byte) (min&0xFF)});


			currentContext.writeAndFlush(controlCmdData).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
		}


	}



	@Override
	public void setDeviceId(String deviceId) {
        this.deviceID=deviceId;
	}
}
