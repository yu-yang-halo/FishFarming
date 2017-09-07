package cn.fuck.fishfarming.activity;

/**
 * Created by Administrator on 2016/11/30 0030.
 */


import android.content.Context;
        import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

        import android.widget.RelativeLayout;

        import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

import butterknife.ButterKnife;
import cn.fuck.fishfarming.R;
import cn.fuck.fishfarming.fragment.AlertFragment;
import cn.fuck.fishfarming.fragment.FragmentProtocol;
import cn.fuck.fishfarming.fragment.MoreFragment;
import cn.fuck.fishfarming.fragment.RealDataFragment;
import cn.fuck.fishfarming.fragment.RemoteControlFragment;

public class TabActivity extends StatusBarActivity {
    public static String KEY_POSITION="tab_position";
    private Context mContext = this;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private String[] mTitles = {"实时", "控制", "预警","更多"};
    private int[] mIconUnselectIds = {
            R.mipmap.tab_realdata0,
            R.mipmap.tab_control0, R.mipmap.tab_alert0,R.mipmap.tab_more0};
    private int[] mIconSelectIds = {
            R.mipmap.tab_realdata1,
            R.mipmap.tab_control1, R.mipmap.tab_alert1,R.mipmap.tab_more1};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private View mDecorView;
    private CommonTabLayout mTabLayout_1;
    public RelativeLayout mainRelativeLayout;
    public int currentPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_tab);
        mainRelativeLayout= (RelativeLayout) findViewById(R.id.mainRelativeLayout);

        currentPos=getIntent().getIntExtra(KEY_POSITION,0);
        if(currentPos>1){
            currentPos=currentPos-1;
        }
        if(currentPos>=3){
            currentPos=3;
        }

        initCustomActionBar();



        mFragments.add(RealDataFragment.getRealDataFragment(0));
        mFragments.add(RemoteControlFragment.getRemoteControlFragment(1));
        mFragments.add(new AlertFragment());
        mFragments.add(new MoreFragment());

        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        mDecorView = getWindow().getDecorView();

        /** with nothing */
        mTabLayout_1 = ButterKnife.findById(mDecorView, R.id.tl_1);
        mTabLayout_1.setTabData(mTabEntities, this, R.id.currentPageFragment, mFragments);
        mTabLayout_1.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                currentPos=position;
                setSelectPos(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        setSelectPos(currentPos);

        //设置未读消息红点
       // mTabLayout_1.showDot(3);

    }


    public void setSelectPos(int position){
        mTabLayout_1.setCurrentTab(position);
        tvTitle.setText(mTitles[position]);

        if(position==1){
            rightBtn.setVisibility(View.VISIBLE);
        }else{
            rightBtn.setVisibility(View.GONE);

        }
        fullScreenBtn.setVisibility(View.GONE);

    }


    public void setShowFullScreenBtn(){
        fullScreenBtn.setVisibility(View.VISIBLE);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("WebSocketReqImpl","onDestroy");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.e("WebSocketReqImpl","onLowMemory");
    }
}

