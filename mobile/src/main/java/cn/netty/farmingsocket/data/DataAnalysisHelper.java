package cn.netty.farmingsocket.data;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import cn.fuck.fishfarming.cache.JsonObjectManager;
import cn.fuck.fishfarming.utils.ConstantUtils;
import cn.netty.farmingsocket.SPackage;

public class DataAnalysisHelper {


	/**

	 * 通过byte数组取得float

	 *

	 * @param b

	 * @param index

	 *            第几位开始取.

	 * @return

	 */

	public static float getFloat(byte[] b, int index) {

		int l;

		l = b[index + 3];

		l &= 0xff;

		l |= ((long) b[index + 2] << 8);

		l &= 0xffff;

		l |= ((long) b[index + 1] << 16);

		l &= 0xffffff;

		l |= ((long) b[index + 0] << 24);

		return Float.intBitsToFloat(l);

	}
	public static String getBytesString(byte[] bytes) {

		if(bytes.length<4){
			return "00000000";
		}
		return String.format("%02x%02x%02x%02x",bytes[0],bytes[1],bytes[2],bytes[3]);
	}
	
	public static Map analysisData(SPackage spackage){

		Map<String,String> dict=new HashMap<String, String>();

		if(spackage.getCmdword()==3){
			if(spackage.getLength()==45){
				//5*8==40  5 1e
				byte[] contents=spackage.getContents();

				for(int i=0;i<spackage.getLength();i=i+5){

					 byte[] floatBaytes=new byte[4];

					 System.arraycopy(contents,i+1,floatBaytes,0,4);

					 float value=getFloat(floatBaytes,0);
					 if(contents[i]==0x1e){
						 dict.put(contents[i]+"",getBytesString(floatBaytes));
					 }else{
						 if(value<=0){
							 continue;
						 }
						 dict.put(contents[i]+"",
								 String.format("%s|%.2f|%.1f|%s",
								 ConstantUtils.CONTENTS.get(contents[i]+""),
								 value,
								 20.0,
								 ConstantUtils.UNITS.get(contents[i]+"")));
					 }
				}
			}

		}else if(spackage.getCmdword()==15){
			if(spackage.getLength()==16){
                //0100 0200 0300 0400 0500 0600 0700 0800
				byte[] contents=spackage.getContents();

				StringBuffer sb=new StringBuffer();

				for(int i=0;i<spackage.getLength();i=i+2){
					 sb.append(contents[i+1]+"");
				}
				Log.v("status",sb.toString());

				dict.put("30",sb.toString());


			}
		}


		return  dict;
	}



}
