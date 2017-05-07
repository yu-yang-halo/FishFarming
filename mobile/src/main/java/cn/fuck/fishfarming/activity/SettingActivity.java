package cn.fuck.fishfarming.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;


import java.util.ArrayList;
import cn.fuck.fishfarming.R;
import cn.fuck.fishfarming.fragment.AlertSettingFragment;
import cn.fuck.fishfarming.fragment.RangeSettingFragment;

import cn.fuck.fishfarming.utils.ViewFindUtils;


/**
 * Created by Administrator on 2016/12/10 0010.
 */

public class SettingActivity extends StatusBarActivity  implements OnTabSelectListener {
    private Context mContext = this;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private final String[] mTitles = {
            "预警设置", "阈值设置"
    };
    private MyPagerAdapter mAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_settings);
        initCustomActionBar();
        tvTitle.setText("个人中心");

        mFragments.add(new AlertSettingFragment());
        mFragments.add(new RangeSettingFragment());


        View decorView = getWindow().getDecorView();
        ViewPager vp = ViewFindUtils.find(decorView,R.id.vp);
        mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        vp.setAdapter(mAdapter);

        /** 默认 */
        SlidingTabLayout tabLayout_1 = ViewFindUtils.find(decorView, R.id.tl_1);
        tabLayout_1.setViewPager(vp);

        tabLayout_1.setOnTabSelectListener(this);

    }
    @Override
    public void onTabSelect(int position) {
       // Toast.makeText(mContext, "onTabSelect&position--->" + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTabReselect(int position) {
      //  Toast.makeText(mContext, "onTabReselect&position--->" + position, Toast.LENGTH_SHORT).show();
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }

}
