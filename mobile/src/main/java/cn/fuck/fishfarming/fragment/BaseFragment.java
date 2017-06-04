package cn.fuck.fishfarming.fragment;

import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.farmingsocket.manager.ReceiveUI;
import com.farmingsocket.manager.UIManager;

import cn.fuck.fishfarming.utils.ConstantUtils;

/**
 * Created by Administrator on 2017/5/19 0019.
 */

public class BaseFragment extends Fragment implements ReceiveUI {
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void update(UIManager o, final Object arg) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (arg instanceof Integer) {
                    int errorCode = (int) arg;
                    switch (errorCode) {
                        case ConstantUtils.ERROR_CODE_SCOKET_COLOSE:
                            showMessage("连接已关闭");
                            break;
                        case ConstantUtils.ERROR_CODE_SCOKET_LOGIN_TIMEOUT:
                            showMessage("socket登录超时");
                            break;
                        case ConstantUtils.ERROR_CODE_SCOKET_READ_TIMEOUT:
                            showMessage("读取超时异常，已关闭连接");
                            break;
                        case ConstantUtils.ERROR_CODE_ERROR_PROTOCAL:
                            showMessage("协议解析异常，已关闭连接");
                            break;
                    }

                }
            }
        });

    }


}
