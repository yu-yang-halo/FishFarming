package com.farmingsocket.helper;

import java.util.Date;

public class YYLogger {

	private static final boolean ENABLE_LOG=true;
	
	
	public static void info(String tag,String message){
		if(ENABLE_LOG){
			System.out.println("["+tag+"] "+message  +"----- [ "+new Date()+" ]");
		}
	
	}
	
	public static void debug(String tag,String message){
		if(ENABLE_LOG){
			System.err.println("["+tag+"] "+message);
		}
		
	}
	
}
