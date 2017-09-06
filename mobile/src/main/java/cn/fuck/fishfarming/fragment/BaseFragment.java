package cn.fuck.fishfarming.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.farmingsocket.manager.BaseUIDelegate;
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
    protected   int index;
    protected Handler mainHandler=new Handler(Looper.getMainLooper());
    protected KProgressHUD hud;
    private BaseUIDelegate delegate;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        delegate=new BaseUIDelegate(getActivity(),this);
    }

    @Override
    public void update(UIManager o, Object arg, int command) {
        if(delegate!=null){
            delegate.update(o,arg,command);
        }
    }

}
