package com.farmingsocket.client.bean;

/**
 * Created by Administrator on 2017/7/5 0005.
 */

public class BaseCommand {
    private int errcode;
    private int command;
    private String errmsg="";


    public int getCommand() {
        return command;
    }

    public void setCommand(int command) {
        this.command = command;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public int getErrcode() {
        return errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    @Override
    public String toString() {
        return "BaseCommand{" +
                "errcode=" + errcode +
                ", command=" + command +
                ", errmsg='" + errmsg + '\'' +
                '}';
    }
}
