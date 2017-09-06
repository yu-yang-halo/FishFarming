package cn.fuck.fishfarming.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.farmingsocket.manager.UIManager;
import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.fuck.fishfarming.R;
import cn.fuck.fishfarming.activity.ui.AlertConfigUi;
import cn.fuck.fishfarming.adapter.AlertAdapter;
import cn.fuck.fishfarming.application.MyApplication;

/**
 * Created by Administrator on 2016/12/3 0003.
 */

public class AlertFragment extends BaseFragment{
    Handler mainHandler = new Handler(Looper.getMainLooper());
    @BindView(R.id.alertListView)
    ListView alertListView;
    @BindView(R.id.textView13)
    TextView tips;
    MyApplication myApp;
    AlertAdapter alertAdapter;
    Set<String> messages=new HashSet<>();

    @OnClick(R.id.btnAlertConfig) void toAlertConfigUi(){
        Intent intent=new Intent(getActivity(), AlertConfigUi.class);
        getActivity().startActivity(intent);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        UIManager.getInstance().addObserver(this);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        UIManager.getInstance().deleteObserver(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fr_alert,null);
        ButterKnife.bind(this,view);

        alertAdapter=new AlertAdapter(getActivity(),null);
        alertListView.setAdapter(alertAdapter);

        myApp= (MyApplication)getActivity().getApplicationContext();




        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        UIManager.getInstance().setCurrentObject(this);
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

    }

}
