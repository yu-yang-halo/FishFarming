package cn.fuck.fishfarming.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import com.farmingsocket.client.bean.BaseDevice;
import com.farmingsocket.manager.UIManager;
import java.util.List;
import butterknife.ButterKnife;
import cn.fuck.fishfarming.R;
import cn.fuck.fishfarming.adapter.setting.RangeExpandAdapter;
import cn.fuck.fishfarming.application.MyApplication;


/**
 * Created by Administrator on 2017/2/23 0023.
 */

public class RangeSettingFragment extends BaseFragment{
    ExpandableListView expandSettingListView;
    MyApplication myApp;
    RangeExpandAdapter adapter;
    List<BaseDevice> collectorInfos;
    Handler mainHandler = new Handler(Looper.getMainLooper());

    int selectPos=-1;

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


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    MyApplication myApplication;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View convertView=inflater.inflate(R.layout.fr_alertsetting,null);
        myApplication= (MyApplication) getActivity().getApplication();
        expandSettingListView=ButterKnife.findById(convertView,R.id.expandSettingListView);

        myApp= (MyApplication)getActivity().getApplicationContext();
        collectorInfos=myApp.getBaseInfo().getDevice();
        adapter=new RangeExpandAdapter(collectorInfos,getActivity());
        expandSettingListView.setAdapter(adapter);


        expandSettingListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if(selectPos>=0&&selectPos!=groupPosition){
                    expandSettingListView.collapseGroup(selectPos);
                }
                selectPos=groupPosition;


            }
        });
        expandSettingListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });

        return convertView;
    }

    @Override
    public void onStop() {
        super.onStop();
        collapseGroupAll();
    }

    private void collapseGroupAll(){
        if(adapter!=null&&expandSettingListView!=null){
            for (int i=0;i< adapter.getGroupCount();i++){
                expandSettingListView.collapseGroup(i);
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){
            collapseGroupAll();
        }
    }
}
