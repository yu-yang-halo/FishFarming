package cn.fuck.fishfarming.fragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.farmingsocket.manager.ConstantsPool;
import com.farmingsocket.manager.ReceiveUI;
import com.farmingsocket.manager.UIManager;
import com.kaopiz.kprogresshud.KProgressHUD;

import cn.fuck.fishfarming.activity.BaseActivity;
import cn.fuck.fishfarming.activity.LoginActivity;
import cn.fuck.fishfarming.utils.ConstantUtils;

/**
 * Created by Administrator on 2017/5/19 0019.
 */

public class BaseFragment extends Fragment implements ReceiveUI {
    protected Handler mainHandler=new Handler(Looper.getMainLooper());
    protected KProgressHUD hud;
    @Override
    public void update(UIManager o, final Object arg, final int command) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {

                if(hud!=null){
                    hud.dismiss();
                }
                switch (command){
                    case ConstantsPool.ERROR_CODE_READ_TIMEOUT:
                        showToast("数据读取超时");
                        break;
                    case ConstantsPool.ERROR_CODE_CONNECT_CLOSED:
                        showToast("网络连接已关闭");
                        break;
                    case ConstantsPool.ERROR_CODE_CONNECT_CLOSING:
                        showToast("网络连接即将关闭");
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
                }
                if(command>0x1000){
                    Intent intent=new Intent();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.setClass(getActivity(),LoginActivity.class);
                    startActivity(intent);
                }

            }
        });


    }

    protected void showToast(String message){
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
    }


}
