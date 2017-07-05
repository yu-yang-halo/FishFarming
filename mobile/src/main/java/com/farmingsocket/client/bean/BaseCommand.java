package com.farmingsocket.client.bean;

/**
 * Created by Administrator on 2017/7/5 0005.
 */

public class BaseCommand {
    private int errcode;
    private int command;

    public int getCommand() {
        return command;
    }

    public void setCommand(int command) {
        this.command = command;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    @Override
    public String toString() {
        return "BaseCommand{" +
                "errcode=" + errcode +
                ", command=" + command +
                '}';
    }
}
