package cn.farmFish.service.webserviceApi.bean;

/**
 * Created by Administrator on 2016/12/10 0010.
 */

public class VideoInfo {
    private String F_ID;
    private String F_CollectorID;
    private String F_Name;
    private String F_UserName;
    private String F_UserPwd;
    private String F_OutIPAddr;
    private int    F_IndexCode;


    public String getF_ID() {
        return F_ID;
    }

    public void setF_ID(String f_ID) {
        F_ID = f_ID;
    }

    public String getF_CollectorID() {
        return F_CollectorID;
    }

    public void setF_CollectorID(String f_CollectorID) {
        F_CollectorID = f_CollectorID;
    }

    public String getF_Name() {
        return F_Name;
    }

    public void setF_Name(String f_Name) {
        F_Name = f_Name;
    }

    public String getF_UserName() {
        return F_UserName;
    }

    public void setF_UserName(String f_UserName) {
        F_UserName = f_UserName;
    }

    public String getF_UserPwd() {
        return F_UserPwd;
    }

    public void setF_UserPwd(String f_UserPwd) {
        F_UserPwd = f_UserPwd;
    }

    public String getF_OutIPAddr() {
        return F_OutIPAddr;
    }

    public void setF_OutIPAddr(String f_OutIPAddr) {
        F_OutIPAddr = f_OutIPAddr;
    }

    public int getF_IndexCode() {
        return F_IndexCode;
    }

    public void setF_IndexCode(int f_IndexCode) {
        F_IndexCode = f_IndexCode;
    }


    @Override
    public String toString() {
        return "VideoInfo{" +
                "F_ID='" + F_ID + '\'' +
                ", F_CollectorID='" + F_CollectorID + '\'' +
                ", F_Name='" + F_Name + '\'' +
                ", F_UserName='" + F_UserName + '\'' +
                ", F_UserPwd='" + F_UserPwd + '\'' +
                ", F_OutIPAddr='" + F_OutIPAddr + '\'' +
                ", F_IndexCode=" + F_IndexCode +
                '}';
    }
}
