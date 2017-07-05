package cn.fuck.fishfarming.fragment;

import android.content.Context;
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
import cn.fuck.fishfarming.R;
import cn.fuck.fishfarming.adapter.AlertAdapter;
import cn.fuck.fishfarming.application.MyApplication;

/**
 * Created by Administrator on 2016/12/3 0003.
 */

public class AlertFragment extends BaseFragment{
    Handler mainHandler = new Handler(Looper.getMainLooper());
    ListView alertListView;
    TextView tips;
    MyApplication myApp;
    AlertAdapter alertAdapter;

    Set<String> messages=new HashSet<>();

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
        tips= (TextView) view.findViewById(R.id.textView13);
        alertListView= (ListView) view.findViewById(R.id.alertListView);

        alertAdapter=new AlertAdapter(getActivity(),null);
        alertListView.setAdapter(alertAdapter);

        myApp= (MyApplication)getActivity().getApplicationContext();




        return view;
    }

    @Override
    public void onStart() {
        super.onStart();


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
