package cn.netty.farmingsocket;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import cn.netty.farmingsocket.data.ICmdPackageProtocol;
import cn.netty.farmingsocket.data.IDataCompleteCallback;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

public class SocketClientManager{
	private static final int READ_IDEL_TIME_OUT = 12;
	private static final int WRITE_IDEL_TIME_OUT = 12;
	private static final int ALL_IDEL_TIME_OUT = 12;
	private static final String HOST_IP="183.78.182.98";
	private static final int HOST_PORT =9101;
	
	private ICmdPackageProtocol handler;
	private static SocketClientManager instance;
	
	private SocketClientManager(){

	}
	public static SocketClientManager getInstance(){
		synchronized (SocketClientManager.class) {
			if(instance==null){
				instance=new SocketClientManager();
				
			}
		}
		return instance;
	}

	public ICmdPackageProtocol getHandler() {
		return handler;
	}
	
	public void closeConnect(){
		if(handler!=null){
			handler.closeContext();
		}

	}
	
	public void beginConnect(String deviceId,final IDataCompleteCallback completeCallback){

		//closeConnect();

		handler=new SocketClientHandler();
		handler.setDeviceId(deviceId);
		handler.registerDataCompleteCallback(completeCallback,false);
		new Thread(new Runnable() {
			@Override
			public void run() {

				EventLoopGroup workerGroup = new NioEventLoopGroup();
				try {
					Bootstrap b = new Bootstrap(); // (1)
					b.group(workerGroup); // (2)
					b.channel(NioSocketChannel.class); // (3)
					b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
					b.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new SocketEncoder(),new SocketDecoder(),(SocketClientHandler)handler
									);
							/**
                             * new IdleStateHandler(READ_IDEL_TIME_OUT,
							 WRITE_IDEL_TIME_OUT, ALL_IDEL_TIME_OUT, TimeUnit.SECONDS)
							 */
						}
					});

					// Start the client.
					ChannelFuture f = b.connect(HOST_IP,HOST_PORT).sync(); // (5)
					// Wait until the connection is closed.
					f.channel().closeFuture().sync();
				}catch (InterruptedException e) {
					Log.e("InterruptedException",e.getMessage());
					e.printStackTrace();
				}
				finally {
					workerGroup.shutdownGracefully();
				}
			}
		}).start();


	}

    
    
    
    
    
}
