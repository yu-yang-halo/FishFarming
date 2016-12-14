package cn.fuck.fishfarming.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import cn.fuck.fishfarming.R;
import cn.fuck.fishfarming.activity.HistoryActivity;
import cn.fuck.fishfarming.activity.NewsActivity;

/**
 * Created by Administrator on 2016/12/3 0003.
 */

public class MoreFragment extends Fragment {
    private ListView listView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.v("MoreFragment","onAttach");
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("MoreFragment","onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View  view=inflater.inflate(R.layout.fr_more,null);
        listView= ButterKnife.findById(view,R.id.listView);
        List<Map<String,Object>> data=new ArrayList<>();

        String[] titles=new String[]{"知识库","历史数据","电量统计"};
        int[] images=new int[]{R.mipmap.main_news,R.mipmap.main_history,R.mipmap.main_battery};

        for (int i=0;i<titles.length;i++){
            Map<String,Object> map=new HashMap<>();
            map.put("iconName",images[i]);
            map.put("titleName",titles[i]);
            data.add(map);
        }



        String[] from=new String[]{"iconName","titleName"};

        int[]    to=new int[]{R.id.iconImage,R.id.titleContent};

        SimpleAdapter simpleAdapter=new SimpleAdapter(getActivity(),data,R.layout.fr_adapter_more,from,to);


        listView.setAdapter(simpleAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent();

                switch (i){
                    case 0:

                        intent.setClass(getActivity(), NewsActivity.class);
                        break;
                    case 1:
                        intent.setClass(getActivity(), HistoryActivity.class);
                        break;
                    case 2:

                        break;

                }

                if(i<2){
                    startActivity(intent);
                }

            }
        });

        Log.v("MoreFragment","onCreateView");

        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v("MoreFragment","onActivityCreated");
    }
    @Override
    public void onStart() {
        super.onStart();
        Log.v("MoreFragment","onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v("MoreFragment","onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.v("MoreFragment","onDestroyView");
    }
}
