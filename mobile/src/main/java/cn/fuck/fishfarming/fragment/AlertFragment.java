package cn.fuck.fishfarming.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.fuck.fishfarming.R;

/**
 * Created by Administrator on 2016/12/3 0003.
 */

public class AlertFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fr_alert,null);

        return view;
    }
}
