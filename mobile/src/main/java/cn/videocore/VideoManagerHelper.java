package cn.videocore;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.hikvision.netsdk.ADDR_QUERY_TYPE;
import com.hikvision.netsdk.ExceptionCallBack;
import com.hikvision.netsdk.HCNetSDK;
import com.hikvision.netsdk.INT_PTR;
import com.hikvision.netsdk.NET_DVR_CHECK_DDNS_RET;
import com.hikvision.netsdk.NET_DVR_DEVICEINFO_V30;
import com.hikvision.netsdk.NET_DVR_QUERY_COUNTRYID_COND;
import com.hikvision.netsdk.NET_DVR_QUERY_COUNTRYID_RET;
import com.hikvision.netsdk.NET_DVR_QUERY_DDNS_COND;
import com.hikvision.netsdk.NET_DVR_QUERY_DDNS_RET;
import com.hikvision.netsdk.PTZCommand;

import cn.farmFish.service.webserviceApi.bean.VideoInfo;

/**
 * Created by Administrator on 2016/12/12.
 */

public class VideoManagerHelper {



    private static final String TAG="VideoManagerHelper";

    private	int 			m_iStartChan 			= 0;				// start channel no
    private int				m_iChanNum				= 0;
    private int             m_iLogID                = -1;

    private int             m_iPlayID               = -1;

    private boolean         m_bMultiPlay            = false;





    private String[] getDomainIPPort(String szSvrAddr, String szDevNickName)
    {
        String[] ipAndPortArr=new String[]{"112.27.209.98","8900"};
        //www.hik-online.com   tld52345678
        NET_DVR_QUERY_COUNTRYID_COND struCountryIDCond = new NET_DVR_QUERY_COUNTRYID_COND();
        NET_DVR_QUERY_COUNTRYID_RET struCountryIDRet = new NET_DVR_QUERY_COUNTRYID_RET();
        struCountryIDCond.wCountryID = 248; //248 is for china,other country's ID please see the interface document
        System.arraycopy(szSvrAddr.getBytes(), 0, struCountryIDCond.szSvrAddr, 0, szSvrAddr.getBytes().length);
        System.arraycopy("Android NetSDK".getBytes(), 0, struCountryIDCond.szClientVersion, 0, "Android NetSDK".getBytes().length);

        if(HCNetSDK.getInstance().NET_DVR_GetAddrInfoByServer(ADDR_QUERY_TYPE.QUERYSVR_BY_COUNTRYID, struCountryIDCond, struCountryIDRet))
        {
            System.out.println("QUERYSVR_BY_COUNTRYID succ,resolve:" + CommonMethod.toValidString(new String(struCountryIDRet.szResolveSvrAddr)));
        }
        else
        {
            //System.out.println("QUERYSVR_BY_COUNTRYID failed:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
            INT_PTR int_ptr=new INT_PTR();
            int_ptr.iValue=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("QUERYSVR_BY_COUNTRYID failed:" + HCNetSDK.getInstance().NET_DVR_GetErrorMsg(int_ptr));

        }
        //follow code show how to get dvr/ipc address from the area resolve server by nickname or serial no.
        NET_DVR_QUERY_DDNS_COND struDDNSCond = new NET_DVR_QUERY_DDNS_COND();
        NET_DVR_QUERY_DDNS_RET struDDNSQueryRet = new NET_DVR_QUERY_DDNS_RET();
        NET_DVR_CHECK_DDNS_RET struDDNSCheckRet = new NET_DVR_CHECK_DDNS_RET();
        System.arraycopy("Android NetSDK".getBytes(), 0, struDDNSCond.szClientVersion, 0, "Android NetSDK".getBytes().length);
        System.arraycopy(struCountryIDRet.szResolveSvrAddr, 0, struDDNSCond.szResolveSvrAddr, 0, struCountryIDRet.szResolveSvrAddr.length);
        System.arraycopy(szDevNickName.getBytes(), 0, struDDNSCond.szDevNickName, 0,szDevNickName.getBytes().length);//your dvr/ipc nickname
        System.arraycopy("serial no.".getBytes(), 0, struDDNSCond.szDevSerial, 0, "serial no.".getBytes().length);//your dvr/ipc serial no.
        if(HCNetSDK.getInstance().NET_DVR_GetAddrInfoByServer(ADDR_QUERY_TYPE.QUERYDEV_BY_NICKNAME_DDNS, struDDNSCond, struDDNSQueryRet))
        {

            ipAndPortArr[0]= CommonMethod.toValidString(new String(struDDNSQueryRet.szDevIP));
            ipAndPortArr[1]= struDDNSQueryRet.wCmdPort+"";

            System.out.println("QUERYDEV_BY_NICKNAME_DDNS succ,ip:" +  ipAndPortArr[0] + ", SDK port:" + struDDNSQueryRet.wCmdPort + ", http port" +  ipAndPortArr[1]);



        }
        else
        {
            //System.out.println("QUERYDEV_BY_NICKNAME_DDNS failed:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
            INT_PTR int_ptr=new INT_PTR();
            int_ptr.iValue=HCNetSDK.getInstance().NET_DVR_GetLastError();
            System.out.println("QUERYDEV_BY_NICKNAME_DDNS failed:" + HCNetSDK.getInstance().NET_DVR_GetErrorMsg(int_ptr));
        }

        //if you get the dvr/ipc address failed from the area reolve server,you can check the reason show as follow
        if(HCNetSDK.getInstance().NET_DVR_GetAddrInfoByServer(ADDR_QUERY_TYPE.CHECKDEV_BY_NICKNAME_DDNS, struDDNSCond, struDDNSCheckRet))
        {
            System.out.println("CHECKDEV_BY_NICKNAME_DDNS succ,ip:" + CommonMethod.toValidString(new String(struDDNSCheckRet.struQueryRet.szDevIP)) + ", SDK port:" + struDDNSCheckRet.struQueryRet.wCmdPort + ", http port" + struDDNSCheckRet.struQueryRet.wHttpPort + ",region:" + struDDNSCheckRet.wRegionID + ",status:" + struDDNSCheckRet.byDevStatus);
        }
        else
        {
            System.out.println("CHECKDEV_BY_NICKNAME_DDNS failed:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }



        return ipAndPortArr;

    }

    public void  logout(){
        if (!HCNetSDK.getInstance().NET_DVR_Logout_V30(m_iLogID))
        {
            Log.e(TAG, " NET_DVR_Logout is failed!");
            return;
        }
    }

    public boolean loginDevice(VideoInfo videoInfo)
    {

        initeSdk();

        String[] arr=videoInfo.getF_OutIPAddr().split("\\|");

        if(arr.length!=2){
            return false;
        }


        String szSvrAddr=arr[0];
        String szDevNickName=arr[1];

        String m_oUser=videoInfo.getF_UserName();
        String m_oPsd=videoInfo.getF_UserPwd();


        //动态获取服务器ip与端口
        String[] ipAndPortArr=getDomainIPPort(szSvrAddr,szDevNickName);


        String m_oIPAddr=ipAndPortArr[0];
        String m_oPort  =ipAndPortArr[1];


        // get instance
        NET_DVR_DEVICEINFO_V30 m_oNetDvrDeviceInfoV30 = new NET_DVR_DEVICEINFO_V30();
        if (null == m_oNetDvrDeviceInfoV30)
        {
            Log.e(TAG, "HKNetDvrDeviceInfoV30 new is failed!");
            return false;
        }
        String strIP = m_oIPAddr;
        int	nPort = Integer.parseInt(m_oPort);
        String strUser = m_oUser;
        String strPsd = m_oPsd;
        // call NET_DVR_Login_v30 to login on, port 8000 as default
        m_iLogID = HCNetSDK.getInstance().NET_DVR_Login_V30(strIP, nPort, strUser, strPsd, m_oNetDvrDeviceInfoV30);
        if (m_iLogID < 0)
        {
            Log.e(TAG, "NET_DVR_Login is failed!Err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
            return false;
        }
        if(m_oNetDvrDeviceInfoV30.byChanNum > 0)
        {
            m_iStartChan = m_oNetDvrDeviceInfoV30.byStartChan;
            m_iChanNum = m_oNetDvrDeviceInfoV30.byChanNum;
        }
        else if(m_oNetDvrDeviceInfoV30.byIPChanNum > 0)
        {
            m_iStartChan = m_oNetDvrDeviceInfoV30.byStartDChan;
            m_iChanNum = m_oNetDvrDeviceInfoV30.byIPChanNum + m_oNetDvrDeviceInfoV30.byHighDChanNum * 256;
        }
        Log.i(TAG, "NET_DVR_Login is Successful!");

        if (m_iLogID < 0)
        {
            Log.e(TAG, "This device logins failed!");
            return false;
        }
        // get instance of exception callback and set
        ExceptionCallBack oexceptionCbf = getExceptiongCbf();
        if (oexceptionCbf == null)
        {
            Log.e(TAG, "ExceptionCallBack object is failed!");
            return false;
        }

        if (!HCNetSDK.getInstance().NET_DVR_SetExceptionCallBack(oexceptionCbf))
        {
            Log.e(TAG, "NET_DVR_SetExceptionCallBack is failed!");
            return false;
        }

        Log.i(TAG, "Login sucess ****************************1***************************");

        return true;
    }
    public int getStatusBarHeight(Activity ctx) {

        int result = 0;
        int resourceId = ctx.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = ctx.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    private int caculateColumns(int size){
        if(size<=1){
            return 1;
        }else if(size<=4){
            return 2;
        }else if(size<=9){
            return 3;
        }else if(size<=16){
            return 4;
        }
        return 0;
    }

    public void ptzControl(final int pos, final int direct){
        Log.e(TAG,"pos "+pos +" direct "+direct);
         //PTZCommand.PAN_LEFT
        new Thread(new Runnable() {
            @Override
            public void run() {

                if(!HCNetSDK.getInstance().NET_DVR_PTZControl_Other(m_iLogID, m_iStartChan+pos, direct, 0))
                {
                    Log.e(TAG, "start : " + HCNetSDK.getInstance().NET_DVR_GetLastError());
                }
                else
                {
                    Log.i(TAG, "start  succ");
                }

                try {
                    new Thread().sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(!HCNetSDK.getInstance().NET_DVR_PTZControl_Other(m_iLogID, m_iStartChan+pos,direct, 1))
                {
                    Log.e(TAG, "stop : " + HCNetSDK.getInstance().NET_DVR_GetLastError());
                }
                else
                {
                    Log.i(TAG, "stop  succ");
                }
            }
        }).start();

    }

    public void startMultiPreview(final PlaySurfaceView[] playView, final int pos, final int existNum)
    {

        new Thread(new Runnable() {
            @Override
            public void run() {
                int mode=caculateColumns(existNum);
                if(pos<0){

                    for(int i = 0; i <mode*mode; i++)
                    {
                        if(playView[i]!= null)
                        {
                            if(i<existNum){
                                playView[i].startPreview(m_iLogID, m_iStartChan + i);
                            }
                        }

                    }
                    if(playView[0]!=null){
                        m_iPlayID = playView[0].m_iPreviewHandle;
                    }

                }else{
                    if(playView[pos] != null)
                    {
                        playView[pos].startPreview(m_iLogID, m_iStartChan + pos);
                        m_iPlayID = playView[pos].m_iPreviewHandle;
                    }


                }

            }
        }).start();



    }
    public void stopMultiPreview(final PlaySurfaceView[] playView)
    {

        stopPreview(playView);

    }

    private void stopPreview(PlaySurfaceView[] playView){
        int i = 0;
        for(i = 0; i < playView.length;i++)
        {
            if(playView[i]!=null){
                playView[i].stopPreview();
                playView[i]=null;
            }
        }
        m_iPlayID = -1;
    }


    private ExceptionCallBack getExceptiongCbf()
    {
        ExceptionCallBack oExceptionCbf = new ExceptionCallBack()
        {
            public void fExceptionCallBack(int iType, int iUserID, int iHandle)
            {
                System.out.println("recv exception, type:" + iType);
            }
        };
        return oExceptionCbf;
    }

    private boolean initeSdk()
    {
        //init net sdk
        if (!HCNetSDK.getInstance().NET_DVR_Init())
        {
            Log.e(TAG, "HCNetSDK init is failed!");
            return false;
        }
        HCNetSDK.getInstance().NET_DVR_SetLogToFile(3, "/mnt/sdcard/sdklog/",true);
        return true;
    }

    public int getM_iStartChan() {
        return m_iStartChan;
    }

    public void setM_iStartChan(int m_iStartChan) {
        this.m_iStartChan = m_iStartChan;
    }

    public int getM_iChanNum() {
        return m_iChanNum;
    }

    public void setM_iChanNum(int m_iChanNum) {
        this.m_iChanNum = m_iChanNum;
    }

    public int getM_iLogID() {
        return m_iLogID;
    }

    public void setM_iLogID(int m_iLogID) {
        this.m_iLogID = m_iLogID;
    }

}
