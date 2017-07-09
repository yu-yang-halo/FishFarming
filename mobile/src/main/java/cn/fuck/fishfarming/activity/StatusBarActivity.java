package cn.fuck.fishfarming.activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import cn.fuck.fishfarming.R;

/**
 * Created by Administrator on 2016/12/3 0003.
 */

public class StatusBarActivity extends BaseActivity {
    protected  TextView tvTitle;
    protected  Button leftBtn;
    protected  Button rightBtn;
    protected  Button fullScreenBtn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.colorTheme);

    }

    protected boolean initCustomActionBar() {
        ActionBar mActionbar = getActionBar();
        if (mActionbar == null) {
            return false;
        }
        mActionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mActionbar.setDisplayShowCustomEnabled(true);
        mActionbar.setCustomView(R.layout.custom_actionbar);
        tvTitle = (TextView) mActionbar.getCustomView().findViewById(R.id.titleView);
        rightBtn=(Button) mActionbar.getCustomView().findViewById(R.id.rightBtn);
        fullScreenBtn=(Button) mActionbar.getCustomView().findViewById(R.id.fullScreenBtn);
        leftBtn=(Button) mActionbar.getCustomView().findViewById(R.id.leftBtn);
        leftBtn.setVisibility(View.VISIBLE);
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        return true;
    }

    protected void showToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
