package com.farmingsocket.manager;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.farmingsocket.client.bean.BaseCommand;
import com.kaopiz.kprogresshud.KProgressHUD;
import cn.fuck.fishfarming.activity.LoginActivity;

/**
 * Created by Administrator on 2017/9/6.
 */

public final class BaseUIDelegate implements ReceiveUI{
    private Context ctx;
    private ReceiveUI receiveUI;
    protected Handler mainHandler=new Handler(Looper.getMainLooper());

    public BaseUIDelegate(Context ctx,ReceiveUI receiveUI){
        this.ctx=ctx;
        this.receiveUI=receiveUI;
    }

    @Override
    public void update(UIManager o, final Object arg, final int command) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {

                switch (command){
                    case ConstantsPool.ERROR_CODE_READ_TIMEOUT:
                        showToast("数据读取超时");
                        break;
                    case ConstantsPool.ERROR_CODE_CONNECT_CLOSED:
                        showToast("网络连接已关闭");
                        break;
                    case ConstantsPool.ERROR_CODE_CONNECT_CLOSING:
                        showToast("网络连接关闭");
                        break;
                    case ConstantsPool.ERROR_CODE_CONNECT_FAILURE:
                        showToast("网络连接失败");
                        break;
                    case ConstantsPool.ERROR_CODE_CONNECT_OPEN:
                        showToast("网络连接打开");
                        break;
                    case ConstantsPool.ERROR_CODE_CONNECT_TIMEOUT:
                        showToast("网络连接超时");
                        break;
                    case ConstantsPool.COMMAND_ERROR:
                        if(arg!=null){
                            BaseCommand baseCommand= (BaseCommand) arg;
                            showToast(baseCommand.getErrmsg());
                        }
                        break;
                }
                if(command>0x1000){
                    if(receiveUI.getClass()==LoginActivity.class){
                        return;
                    }
                    Intent intent=new Intent();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.setClass(ctx,LoginActivity.class);
                    ctx.startActivity(intent);
                }

            }
        });


    }

    protected void showToast(String message){
        Toast.makeText(ctx,message,Toast.LENGTH_SHORT).show();
    }

}
