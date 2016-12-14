package cn.videocore;

import com.hikvision.netsdk.HCNetSDK;
import com.hikvision.netsdk.INT_PTR;
import com.hikvision.netsdk.NET_DVR_ALARMINCFG_V30;
import com.hikvision.netsdk.NET_DVR_ALARMOUTCFG_V30;
import com.hikvision.netsdk.NET_DVR_ALARMOUTSTATUS_V30;
import com.hikvision.netsdk.NET_DVR_AP_INFO_LIST;
import com.hikvision.netsdk.NET_DVR_COMPRESSIONCFG_V30;
import com.hikvision.netsdk.NET_DVR_COMPRESSION_AUDIO;
import com.hikvision.netsdk.NET_DVR_DDNSPARA_V30;
import com.hikvision.netsdk.NET_DVR_DECODERCFG_V30;
import com.hikvision.netsdk.NET_DVR_DEVICECFG;
import com.hikvision.netsdk.NET_DVR_DEVICECFG_V40;
import com.hikvision.netsdk.NET_DVR_DIGITAL_CHANNEL_STATE;
import com.hikvision.netsdk.NET_DVR_EXCEPTION_V40;
import com.hikvision.netsdk.NET_DVR_IPALARMOUTCFG;
import com.hikvision.netsdk.NET_DVR_IPPARACFG_V40;
import com.hikvision.netsdk.NET_DVR_NETCFG_V30;
import com.hikvision.netsdk.NET_DVR_NTPPARA;
import com.hikvision.netsdk.NET_DVR_PICCFG_V30;
import com.hikvision.netsdk.NET_DVR_PRESET_NAME_ARRAY;
import com.hikvision.netsdk.NET_DVR_PREVIEW_DISPLAYCFG;
import com.hikvision.netsdk.NET_DVR_PTZCFG;
import com.hikvision.netsdk.NET_DVR_RECORD_V30;
import com.hikvision.netsdk.NET_DVR_SHOWSTRING_V30;
import com.hikvision.netsdk.NET_DVR_TIME;
import com.hikvision.netsdk.NET_DVR_UPNP_NAT_STATE;
import com.hikvision.netsdk.NET_DVR_USER_V30;
import com.hikvision.netsdk.NET_DVR_VIDEOEFFECT;
import com.hikvision.netsdk.NET_DVR_WIFI_CFG;
import com.hikvision.netsdk.NET_DVR_WIFI_CONNECT_STATUS;
import com.hikvision.netsdk.NET_DVR_WORKSTATE_V30;
import com.hikvision.netsdk.NET_DVR_ZEROCHANCFG;
import com.hikvision.netsdk.NET_IPC_AUX_ALARMCFG;

public class ConfigTest {
	private final static String 	TAG						= "ConfigTest";
	private final static INT_PTR ptrINT = new INT_PTR();
	
	public static void Test_Time(int iUserID)
	{
		NET_DVR_TIME struTimeCfg = new NET_DVR_TIME();
		if(!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID, HCNetSDK.getInstance().NET_DVR_GET_TIMECFG, 0, struTimeCfg))
		{
			System.out.println("NET_DVR_GET_TIMECFG faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetErrorMsg(ptrINT));
		}
		else
		{
			System.out.println("NET_DVR_GET_TIMECFG succ:" + struTimeCfg.ToString());
		}
		if(!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID, HCNetSDK.getInstance().NET_DVR_SET_TIMECFG, 0, struTimeCfg))
		{
			System.out.println("NET_DVR_SET_TIMECFG faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetErrorMsg(ptrINT));
		}
		else
		{
			System.out.println("NET_DVR_SET_TIMECFG succ:" + struTimeCfg.ToString());
		}
	}
	public static void Test_XMLAbility(int iUserID)
	{
//		NET_DVR_SDKLOCAL_CFG	sdkCfg = new NET_DVR_SDKLOCAL_CFG();
//		HCNetSDK.getInstance().NET_DVR_GetSDKLocalConfig(sdkCfg);
//		sdkCfg.byEnableAbilityParse = 1;
//		HCNetSDK.getInstance().NET_DVR_SetSDKLocalConfig(sdkCfg);
//		String strSDCard = Environment.getExternalStorageDirectory().getPath();
//		String path=(getApplicationContext()).getPackageResourcePath();
//		HCNetSDK.getInstance().NET_DVR_SetSimAbilityPath(path, strSDCard);

		byte[] arrayOutBuf = new byte[64*1024];
		INT_PTR intPtr = new INT_PTR();
		String strInput = new String("<AudioVideoCompressInfo><AudioChannelNumber>1</AudioChannelNumber><VoiceTalkChannelNumber>1</VoiceTalkChannelNumber><VideoChannelNumber>1</VideoChannelNumber></AudioVideoCompressInfo>");
		byte[] arrayInBuf = new byte[8*1024];
		arrayInBuf = strInput.getBytes();
		if(!HCNetSDK.getInstance().NET_DVR_GetXMLAbility(iUserID, HCNetSDK.DEVICE_ENCODE_ALL_ABILITY_V20,arrayInBuf, strInput.length(), arrayOutBuf, 64*1024, intPtr))
		{
			System.out.println("get DEVICE_ENCODE_ALL_ABILITY_V20 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("get DEVICE_ENCODE_ALL_ABILITY_V20 succ!");
		}
	}
	public static void Test_PTZProtocol(int iUserID)
	{
		 NET_DVR_PTZCFG struPtzCfg = new NET_DVR_PTZCFG();
		 if (!HCNetSDK.getInstance().NET_DVR_GetPTZProtocol(iUserID, struPtzCfg))
		 {
			System.out.println("NET_DVR_GetPTZProtocol faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}else
		{
			System.out.println("NET_DVR_GetPTZProtocol succ!");
		}
	}
	public static void Test_PresetName(int iUserID, int iChan)
	{
		NET_DVR_PRESET_NAME_ARRAY struPresetNameArray = new NET_DVR_PRESET_NAME_ARRAY();
		if(!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID, HCNetSDK.NET_DVR_GET_PRESET_NAME, iChan, struPresetNameArray))
		{
			System.out.println("NET_DVR_GET_PRESET_NAME faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("NET_DVR_GET_PRESET_NAME succ!");
		}
	}
	public static void Test_ShowString(int iUserID, int iChan)
	{
		NET_DVR_SHOWSTRING_V30 struShowString = new NET_DVR_SHOWSTRING_V30();
		if(!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID, HCNetSDK.getInstance().NET_DVR_GET_SHOWSTRING_V30, iChan, struShowString))
		{
			System.out.println("NET_DVR_GET_SHOWSTRING_V30 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("NET_DVR_GET_SHOWSTRING_V30 succ!");
		}
		
		if(!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID, HCNetSDK.getInstance().NET_DVR_SET_SHOWSTRING_V30, iChan, struShowString))
		{
			System.out.println("NET_DVR_SET_SHOWSTRING_V30 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("NET_DVR_SET_SHOWSTRING_V30 succ!");
		}
	}
	public static void Test_DigitalChannelState(int iUserID)
	{
		NET_DVR_DIGITAL_CHANNEL_STATE struChanState = new NET_DVR_DIGITAL_CHANNEL_STATE();
		if(!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID, HCNetSDK.getInstance().NET_DVR_GET_DIGITAL_CHANNEL_STATE, 0, struChanState))
		{
			System.out.println("NET_DVR_GET_DIGITAL_CHANNEL_STATE faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("NET_DVR_GET_DIGITAL_CHANNEL_STATE succ!");
			System.out.println("analog channel 1 and 2:" + (int)struChanState.byAnalogChanState[0] + "-" + (int)struChanState.byAnalogChanState[1] + 
					",digital channel 1 and 2:" + (int)struChanState.byDigitalChanState[0] + "-" + (int)struChanState.byDigitalChanState[1]);
		}
	}

	public static void Test_DDNSPara(int iUserID)
	{
		NET_DVR_DDNSPARA_V30 struDDNS = new NET_DVR_DDNSPARA_V30();
		if(!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID, HCNetSDK.getInstance().NET_DVR_GET_DDNSCFG_V30, 0, struDDNS))
		{
			System.out.println("NET_DVR_GET_DDNSCFG_V30 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("NET_DVR_GET_DDNSCFG_V30 succ!");
		}
		
		struDDNS.struDDNS[4].sDomainName = CommonMethod.string2ASCII("111222333444", 64);
		if(!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID, HCNetSDK.getInstance().NET_DVR_SET_DDNSCFG_V30, 0, struDDNS))
		{
			System.out.println("NET_DVR_SET_DDNSCFG_V30 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("NET_DVR_SET_DDNSCFG_V30 succ!");
		}
	}
	public static void Test_APInfoList(int iUserID)
	{
		NET_DVR_AP_INFO_LIST struAPInfoList = new NET_DVR_AP_INFO_LIST();
		if(!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID, HCNetSDK.getInstance().NET_DVR_GET_AP_INFO_LIST, 0, struAPInfoList))
		{
			System.out.println("NET_DVR_GET_AP_INFO_LIST faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("NET_DVR_GET_AP_INFO_LIST succ!");
			int i = 0;
			for(i = 0; i < struAPInfoList.dwCount; i++)
			{
				System.out.println("AP[" + i + "]SSID:[" + new String(struAPInfoList.struApInfo[i].sSsid) + "]");
			}
		}
	}
	public static void Test_WifiCfg(int iUserID)
	{
		NET_DVR_WIFI_CFG struWifiCfg = new NET_DVR_WIFI_CFG();
		if(!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID, HCNetSDK.getInstance().NET_DVR_GET_WIFI_CFG, 0, struWifiCfg))
		{
			System.out.println("NET_DVR_GET_WIFI_CFG faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("NET_DVR_GET_WIFI_CFG succ!");
		}
		
		if(!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID, HCNetSDK.getInstance().NET_DVR_SET_WIFI_CFG, 0, struWifiCfg))
		{
			System.out.println("NET_DVR_SET_WIFI_CFG faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("NET_DVR_SET_WIFI_CFG succ!");
		}
	}
	public static void Test_WifiStatus(int iUserID)
	{
		NET_DVR_WIFI_CONNECT_STATUS struStatus = new NET_DVR_WIFI_CONNECT_STATUS();
		if(!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID, HCNetSDK.getInstance().NET_DVR_GET_WIFI_STATUS, 0, struStatus))
		{
			System.out.println("NET_DVR_GET_WIFI_STATUS faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("NET_DVR_GET_WIFI_STATUS succ!");
		}
	}
	public static void Test_UpnpNatState(int iUserID)
	{
		NET_DVR_UPNP_NAT_STATE struUpnpNat = new NET_DVR_UPNP_NAT_STATE();
		if(!HCNetSDK.getInstance().NET_DVR_GetUpnpNatState(iUserID, struUpnpNat))
		{
			System.out.println("NET_DVR_GetUpnpNatState faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("NET_DVR_GetUpnpNatState succ!");
		}
	}
	public static void Test_UserCfg(int iUserID)
	{
		NET_DVR_USER_V30 struUserCfg = new NET_DVR_USER_V30();
		if(!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID, HCNetSDK.NET_DVR_GET_USERCFG_V30, 0, struUserCfg))
		{
			System.out.println("NET_DVR_GET_USERCFG_V30 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("NET_DVR_GET_USERCFG_V30 succ!" + new String(struUserCfg.struUser[0].sUserName));
		}
		if(!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID, HCNetSDK.NET_DVR_SET_USERCFG_V30, 0, struUserCfg))
		{
			System.out.println("NET_DVR_SET_USERCFG_V30 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("NET_DVR_SET_USERCFG_V30 succ!");
		}
	}
	
	public static void Test_DeviceCfg(int iUserID)
	{
		NET_DVR_DEVICECFG struDeviceCfg = new NET_DVR_DEVICECFG();
		if(!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID, HCNetSDK.NET_DVR_GET_DEVICECFG, 0, struDeviceCfg))
		{
			System.out.println("NET_DVR_GET_DEVICECFG faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("NET_DVR_GET_DEVICECFG succ!" );
		}
	    if(!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID, HCNetSDK.NET_DVR_SET_DEVICECFG, 0, struDeviceCfg))
	    {
			System.out.println("NET_DVR_SET_DEVICECFG faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetErrorMsg(ptrINT));
		}
		else
		{
			System.out.println("NET_DVR_SET_DEVICECFG succ!" );
		}
			
   }
	
public static void Test_DeviceCfg_V40(int iUserID)
	{
		NET_DVR_DEVICECFG_V40 struDeviceCfg = new NET_DVR_DEVICECFG_V40();
		if(!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID, HCNetSDK.NET_DVR_GET_DEVICECFG_V40, 0, struDeviceCfg))
		{
			System.out.println("NET_DVR_GET_DEVICECFG_V40 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("NET_DVR_GET_DEVICECFG_V40 succ!" + new String(struDeviceCfg.byDevTypeName));
		}
//	    if(!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID, HCNetSDK.getInstance().NET_DVR_SET_DEVICECFG_V40, 0, struDeviceCfg))
//	    {
//			System.out.println("NET_DVR_SET_DEVICECFG_V40 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetErrorMsg(ptrINT));  
//		}
//		else
//		{
//			System.out.println("NET_DVR_SET_DEVICECFG_V40 succ:" + struDeviceCfg.byDevTypeName);
//		}
			
      }

public static void Test_ExceptionCfg_V40(int iUserID, int iChan)
{
	NET_DVR_EXCEPTION_V40 struExceptionCfg = new NET_DVR_EXCEPTION_V40();
	if(!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID, HCNetSDK.NET_DVR_GET_EXCEPTIONCFG_V40, iChan, struExceptionCfg))
	{
		System.out.println("NET_DVR_GET_EXCEPTIONCFG_V40 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
	}
	else
	{
		System.out.println("NET_DVR_GET_EXCEPTIONCFG_V40 succ! " );
	}
		
  }
		
	public static void Test_PicCfg(int iUserID, int iChan)
	{
		NET_DVR_PICCFG_V30 struPiccfg = new NET_DVR_PICCFG_V30();
		if(!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID, HCNetSDK.NET_DVR_GET_PICCFG_V30, iChan, struPiccfg))
		{
			System.out.println("NET_DVR_GET_PICCFG_V30 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("NET_DVR_GET_PICCFG_V30 succ!" + new String(struPiccfg.sChanName));
		}
		for(int i = 0; i <= 14; i++)
		{
			for(int j = 0; j <= 21; j++)
			{
				struPiccfg.struMotion.byMotionScope[i][j] = 1;
			}
		}
		struPiccfg.struMotion.byEnableHandleMotion = 0;
		if(!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID, HCNetSDK.NET_DVR_SET_PICCFG_V30, iChan, struPiccfg))
		{
			System.out.println("NET_DVR_SET_PICCFG_V30 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("NET_DVR_SET_PICCFG_V30 succ!");
		}
	}
	public static void Test_ZeroChanCfg(int iUserID)
	{
		NET_DVR_ZEROCHANCFG struZeroChanCfg = new NET_DVR_ZEROCHANCFG();
		if(!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID, HCNetSDK.NET_DVR_GET_ZEROCHANCFG, 1, struZeroChanCfg)){
			System.out.println("NET_DVR_GET_ZEROCHANCFG faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}else{
			System.out.println("NET_DVR_GET_ZEROCHANCFG succ!");
		}
		if(!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID, HCNetSDK.NET_DVR_SET_ZEROCHANCFG, 1, struZeroChanCfg)){
			System.out.println("NET_DVR_SET_ZEROCHANCFG faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}else{
			System.out.println("NET_DVR_SET_ZEROCHANCFG succ!");
		}
	}
	public static void Test_WorkState(int iUserID)
	{
		NET_DVR_WORKSTATE_V30 struWorkState = new NET_DVR_WORKSTATE_V30();
		if (!HCNetSDK.getInstance().NET_DVR_GetDVRWorkState_V30(iUserID, struWorkState))
		{
			System.out.println("NET_DVR_GetDVRWorkState_V30 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}else
		{
			System.out.println("NET_DVR_GetDVRWorkState_V30 succ!");
		}
	}
	public static void Test_RecordCfg(int iUserID, int iChan)
	{
		NET_DVR_RECORD_V30 struRecordCfg = new NET_DVR_RECORD_V30();
		if (!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID, HCNetSDK.NET_DVR_GET_RECORDCFG_V30, iChan, struRecordCfg))
		{
			System.out.println("NET_DVR_GET_RECORDCFG_V30 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}else
		{
			System.out.println("NET_DVR_GET_RECORDCFG_V30 succ!");
		}
		if (!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID, HCNetSDK.NET_DVR_SET_RECORDCFG_V30, iChan, struRecordCfg))
		{
			System.out.println("NET_DVR_SET_RECORDCFG_V30 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}else
		{
			System.out.println("NET_DVR_SET_RECORDCFG_V30 succ!");
		}
	}
	public static void Test_AuxAlarmCfg(int iUserID, int iChan)
	{
		NET_IPC_AUX_ALARMCFG struAuxAlarm = new NET_IPC_AUX_ALARMCFG();
		if (!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID, HCNetSDK.NET_IPC_GET_AUX_ALARMCFG, iChan, struAuxAlarm))
		{
			System.out.println("NET_IPC_GET_AUX_ALARMCFG faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}else
		{
			System.out.println("NET_IPC_GET_AUX_ALARMCFG succ!");
		}
		if (!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID, HCNetSDK.NET_IPC_SET_AUX_ALARMCFG, iChan, struAuxAlarm))
		{
			System.out.println("NET_IPC_GET_AUX_ALARMCFG faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}else{
			System.out.println("NET_IPC_GET_AUX_ALARMCFG succ!");
		}
	}
	public static void Test_AlarminCfg(int iUserID)
	{
		NET_DVR_ALARMINCFG_V30 struAlarmIn = new NET_DVR_ALARMINCFG_V30();
		if (!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID, HCNetSDK.NET_DVR_GET_ALARMINCFG_V30, 0, struAlarmIn))
		{
			System.out.println("NET_DVR_GET_ALARMINCFG_V30 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}else
		{
			System.out.println("NET_DVR_GET_ALARMINCFG_V30 succ!");
		}
		if (!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID, HCNetSDK.NET_DVR_SET_ALARMINCFG_V30, 0, struAlarmIn))
		{
			System.out.println("NET_DVR_SET_ALARMINCFG_V30 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}else
		{
			System.out.println("NET_DVR_SET_ALARMINCFG_V30 succ!");
		}
	}
	public static void Test_AlarmOutCfg(int iUserID)
	{
		NET_DVR_ALARMOUTCFG_V30 struAlarmOut = new NET_DVR_ALARMOUTCFG_V30();
		if (!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID, HCNetSDK.NET_DVR_GET_ALARMOUTCFG_V30, 0, struAlarmOut))
		{
			System.out.println("NET_DVR_GET_ALARMOUTCFG_V30 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}else
		{
			System.out.println("NET_DVR_GET_ALARMOUTCFG_V30 succ!");
		}
		if (!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID, HCNetSDK.NET_DVR_SET_ALARMOUTCFG_V30, 0, struAlarmOut))
		{
			System.out.println("NET_DVR_SET_ALARMOUTCFG_V30 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}else
		{
			System.out.println("NET_DVR_SET_ALARMOUTCFG_V30 succ!");
		}
	}
	public static void Test_DecoderCfg(int iUserID)
	{
		NET_DVR_DECODERCFG_V30 struDecoderCfg = new NET_DVR_DECODERCFG_V30();
		if (!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID, HCNetSDK.NET_DVR_GET_DECODERCFG_V30, 1, struDecoderCfg))
		{
			System.out.println("NET_DVR_GET_DECODERCFG_V30 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}else
		{
			System.out.println("NET_DVR_GET_DECODERCFG_V30 succ!");
		}
		struDecoderCfg.wDecoderAddress = 1;   			
		if (!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID, HCNetSDK.NET_DVR_SET_DECODERCFG_V30, 1, struDecoderCfg))
		{
			System.out.println("NET_DVR_SET_DECODERCFG_V30 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}else
		{
			System.out.println("NET_DVR_SET_DECODERCFG_V30 succ!");
		}
	}
	public static void Test_NTPPara(int iUserID)
	{
		NET_DVR_NTPPARA NtpPara = new NET_DVR_NTPPARA();
		if (!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID, HCNetSDK.NET_DVR_GET_NTPCFG, 0, NtpPara))
		{
			System.out.println("get NtpPara faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}else
		{
			System.out.println("get NtpPara succ!");
		}
		NtpPara.byEnableNTP = 1;	        			
		if (!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID, HCNetSDK.NET_DVR_SET_NTPCFG, 0, NtpPara))
		{
			System.out.println("Set NtpPara faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}else
		{
			System.out.println("Set NtpPara succ!");
		}
	}
	public static void Test_IPAlarmOutCfg(int iUserID)
	{
		NET_DVR_IPALARMOUTCFG struIpAlarmOut = new NET_DVR_IPALARMOUTCFG();
		if(!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID, HCNetSDK.NET_DVR_GET_IPALARMOUTCFG, 0, struIpAlarmOut))
		{
			System.out.println("NET_DVR_GET_IPALARMOUTCFG faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("NET_DVR_GET_IPALARMOUTCFG succ!");
		}
	}
	public static void Test_IPParaCfg(int iUserID)
	{
		NET_DVR_IPPARACFG_V40 IpPara = new NET_DVR_IPPARACFG_V40();
		if (!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID, HCNetSDK.NET_DVR_GET_IPPARACFG_V40, 0, IpPara))
		{
			System.out.println("NET_DVR_GET_IPPARACFG_V40 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}else
		{
			System.out.println("NET_DVR_GET_IPPARACFG_V40 succ!AChan:" + IpPara.dwAChanNum + ",DChan:" + IpPara.dwDChanNum);
		}        						
		if (!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID, HCNetSDK.NET_DVR_SET_IPPARACFG_V40, 0, IpPara))
		{
			System.out.println("NET_DVR_SET_IPPARACFG_V40 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}else
		{
			System.out.println("NET_DVR_SET_IPPARACFG_V40 succ!");
		}
	}
	public static void Test_NetCfg(int iUserID)
	{
		NET_DVR_NETCFG_V30 NetCfg = new NET_DVR_NETCFG_V30();
		if (!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID, HCNetSDK.NET_DVR_GET_NETCFG_V30, 0, NetCfg))
		{
			System.out.println("get net cfg faied!"+ " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}else
		{
			System.out.println("get net cfg succ!");
			System.out.println("alarm host ip: " + new String(NetCfg.struAlarmHostIpAddr.sIpV4));
			System.out.println("Etherner host ip: " + new String(NetCfg.struEtherNet[0].struDVRIP.sIpV4));
			System.out.println("Etherner mask: " + new String(NetCfg.struEtherNet[0].struDVRIPMask.sIpV4));
		}
		if (!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID, HCNetSDK.NET_DVR_SET_NETCFG_V30, 0 , NetCfg))
		{
			System.out.println("Set net cfg faied!"+ " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}else
		{
			System.out.println("Set net cfg succ!");
		}
	}
	public static void Test_CompressionCfg(int iUserID, int iChan)
	{
		NET_DVR_COMPRESSIONCFG_V30 CompressionCfg = new NET_DVR_COMPRESSIONCFG_V30();
		if (!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID, HCNetSDK.NET_DVR_GET_COMPRESSCFG_V30, iChan, CompressionCfg))
		{
			System.out.println("get CompressionCfg failed!" + "err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}else
		{
			System.out.println("get CompressionCfg succ! resolution: " + CompressionCfg.struNetPara.byResolution);
		}
		CompressionCfg.struNetPara.dwVideoFrameRate = 1;
		if (!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID, HCNetSDK.NET_DVR_SET_COMPRESSCFG_V30, iChan, CompressionCfg))
		{
			System.out.println("Set CompressionCfg failed!" + "err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}else
		{
			System.out.println("Set CompressionCfg succ!" );
		}
	}
	public static void Test_CompressCfgAud(int iUserID)
	{
		NET_DVR_COMPRESSION_AUDIO AudioCompress = new NET_DVR_COMPRESSION_AUDIO();
		if (!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID, HCNetSDK.NET_DVR_GET_COMPRESSCFG_AUD, 1, AudioCompress))
		{
			System.out.println("get AudioCompress failed!" + "err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}else
		{
			System.out.println("get AudioCompress succ! type: " + AudioCompress.byAudioEncType);
		}
	}
	public static void Test_AlarmOutStatus(int iUserID)
	{
		NET_DVR_ALARMOUTSTATUS_V30 AlarmOut = new NET_DVR_ALARMOUTSTATUS_V30();
		if (!HCNetSDK.getInstance().NET_DVR_SetAlarmOut(iUserID, 0x00ff, 1))
		{
			System.out.println("Set alarm out failed!" + "err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}else
		{
			System.out.println("Set alarm out succ!");
		}
		if (!HCNetSDK.getInstance().NET_DVR_GetAlarmOut_V30(iUserID, AlarmOut))
		{
			System.out.println("Get alarm out failed!" + "err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}else
		{
			System.out.println("Get alarm out succ!");
			for (int i = 0; i< HCNetSDK.MAX_ALARMOUT_V30; i++)
			{
				System.out.print((int)AlarmOut.Output[i]);
			}
			System.out.println();
		}
	}
	public static void Test_VideoEffect(int iPreviewID)
	{
		NET_DVR_VIDEOEFFECT VideoEffect = new NET_DVR_VIDEOEFFECT();
		if (!HCNetSDK.getInstance().NET_DVR_ClientGetVideoEffect(iPreviewID, VideoEffect))
		{
			System.out.println("NET_DVR_ClientGetVideoEffect failed:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("NET_DVR_ClientGetVideoEffect succ"+ VideoEffect.iBrightValue + 
					VideoEffect.iContrastValue+VideoEffect.iSaturationValue+VideoEffect.iHueValue);
		}
		VideoEffect.iBrightValue += 1;
		if(!HCNetSDK.getInstance().NET_DVR_ClientSetVideoEffect(iPreviewID, VideoEffect))
		{
			System.out.println("NET_DVR_ClientSetVideoEffect failed:" +  + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("NET_DVR_ClientSetVideoEffect succ");
		}
	}

//	public static void Test_InquestControlCDW(int iUserId)
//	{
//		NET_DVR_INQUEST_ROOM InquestRoom = new NET_DVR_INQUEST_ROOM();
//		
//		InquestRoom.byRoomIndex = 1;
//		InquestRoom.byFileType = 1;
//		if (!HCNetSDK.getInstance().NET_DVR_RemoteControl(iUserId, HCNetSDK.NET_DVR_INQUEST_PAUSE_CDW, InquestRoom))
//		{
//			System.out.println("NET_DVR_INQUEST_PAUSE_CDW" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
//		}
//		else
//		{
//			System.out.println("NET_DVR_INQUEST_PAUSE_CDW success");
//		}
//		
//		if (!HCNetSDK.getInstance().NET_DVR_RemoteControl(iUserId, HCNetSDK.NET_DVR_INQUEST_RESUME_CDW, InquestRoom))
//		{
//			System.out.println("NET_DVR_INQUEST_RESUME_CDW" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
//		}
//		else
//		{
//			System.out.println("NET_DVR_INQUEST_RESUME_CDW success");
//		}
//		
//	}
	
//	public static void Test_AreaMaskCfg(int iUserId, int iChan)
//	{
//		NET_DVR_AREA_MASK_CFG AreaMaskCfg = new NET_DVR_AREA_MASK_CFG();
//		
//		if (!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserId, HCNetSDK.NET_DVR_GET_AREA_MASK_CFG, 34, AreaMaskCfg))
//		{
//			System.out.println("NET_DVR_GET_AREA_MASK_CFG" + iChan + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());			
//		}
//		else
//		{
//			System.out.println("NET_DVR_GET_AREA_MASK_CFG success");
//		}
//		
//		if (!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserId, HCNetSDK.NET_DVR_SET_AREA_MASK_CFG, 34, AreaMaskCfg))
//		{
//			System.out.println("NET_DVR_SET_AREA_MASK_CFG" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());			
//		}
//		else
//		{
//			System.out.println("NET_DVR_SET_AREA_MASK_CFG success");
//		}		
//	}
	
//	public static void Test_AudioDiaCritical(int iUserId, int iChan)
//	{
//		NET_DVR_AUDIO_DIACRITICAL_CFG AudioDiaCritical = new NET_DVR_AUDIO_DIACRITICAL_CFG();
//		
//		if (!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserId, HCNetSDK.NET_DVR_GET_AUDIO_DIACRITICAL_CFG, 34, AudioDiaCritical))
//		{
//			System.out.println("NET_DVR_GET_AUDIO_DIACRITICAL_CFG" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());			
//		}
//		else
//		{
//			System.out.println("NET_DVR_GET_AUDIO_DIACRITICAL_CFG success");
//		}
//		
//		if (!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserId, HCNetSDK.NET_DVR_SET_AUDIO_DIACRITICAL_CFG, 34, AudioDiaCritical))
//		{
//			System.out.println("NET_DVR_SET_AUDIO_DIACRITICAL_CFG" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());			
//		}
//		else
//		{
//			System.out.println("NET_DVR_SET_AUDIO_DIACRITICAL_CFG success");
//		}		
//	}
	public static void Test_Preview_display(int iUserID, int iChan)
	{ 	
		NET_DVR_PREVIEW_DISPLAYCFG struPreviewDisplay = new NET_DVR_PREVIEW_DISPLAYCFG();
		if (!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID, HCNetSDK.NET_DVR_GET_PREVIEW_DISPLAYCFG, 1, struPreviewDisplay))
		{
			System.out.println("get Preview Display failed!" + "err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}else
		{
			System.out.println("get Preview Display succ! Mode: " + struPreviewDisplay.byCorrectMode + " MountType:" + struPreviewDisplay.byMountType);
			System.out.println("get Preview Display succ! RealTimeOutput: " + struPreviewDisplay.byRealTimeOutput);
		}
		//Set
		//struPreviewDisplay.byMountType = 2;
		//struPreviewDisplay.byRealTimeOutput = 3;
		if(!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID, HCNetSDK.NET_DVR_SET_PREVIEW_DISPLAYCFG, 1, struPreviewDisplay))
		{
			System.out.println("Set Preview Display failed!" + "err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("NET_DVR_SET_PREVIEW_DISPLAYCFG succ");
		}
	}
	public static void Text_FISHEYE_ABILITY(int iUserID)
	{

		byte[] arrayOutBuf = new byte[64*1024];
		INT_PTR intPtr = new INT_PTR();
		String strInput = new String("<FishEyeIPCAbility version==\"2.0\"><channelNO>1</channelNO></FishEyeIPCAbility>");
		byte[] arrayInBuf = new byte[8*1024];
		arrayInBuf = strInput.getBytes();
		if(!HCNetSDK.getInstance().NET_DVR_GetXMLAbility(iUserID, HCNetSDK.FISHEYE_ABILITY,arrayInBuf, strInput.length(), arrayOutBuf, 64*1024, intPtr))
		{
			System.out.println("get FISHEYE_ABILITY faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("get FISHEYE_ABILITY succ!");
		}
	}
	public static void TEST_Config(int iPreviewID, int iUserID, int iChan)
	{
//		Test_Time(iUserID);
//		Test_XMLAbility(iUserID);
//		Test_PTZProtocol(iUserID);
		Test_PresetName(iUserID, iChan);
//		Test_ShowString(iUserID, iChan);
//		Test_DigitalChannelState(iUserID);
//		Test_DDNSPara(iUserID);
//		Test_APInfoList(iUserID);
//		Test_WifiCfg(iUserID);
//		Test_WifiStatus(iUserID);
//		Test_UpnpNatState(iUserID);
//		Test_UserCfg(iUserID);
//		Test_DeviceCfg(iUserID);
//		Test_PicCfg(iUserID, iChan);
//		Test_ZeroChanCfg(iUserID);
//		Test_WorkState(iUserID);
//		Test_RecordCfg(iUserID, iChan);
//		Test_AuxAlarmCfg(iUserID, iChan);
//		Test_AlarminCfg(iUserID);
//		Test_AlarmOutCfg(iUserID);
//		Test_DecoderCfg(iUserID);
//		Test_NTPPara(iUserID);
//		Test_IPAlarmOutCfg(iUserID);
//		Test_IPParaCfg(iUserID);
//		Test_NetCfg(iUserID);
//		Test_CompressionCfg(iUserID, iChan);
//		Test_CompressCfgAud(iUserID);
//		Test_AlarmOutStatus(iUserID);
		Test_VideoEffect(iPreviewID);
//		Test_DeviceCfg_V40(iUserID);
//		Test_ExceptionCfg_V40(iUserID, iChan);
//		Test_InquestControlCDW(iUserID);
//		Test_AreaMaskCfg(iUserID, iChan);
//		Test_AudioDiaCritical(iUserID, iChan);
//		Test_Preview_display(iUserID, iChan);
//		Text_FISHEYE_ABILITY(iUserID);
	}
}
