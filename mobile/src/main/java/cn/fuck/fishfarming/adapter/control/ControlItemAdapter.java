package cn.fuck.fishfarming.adapter.control;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.farmingsocket.client.bean.BaseDevice;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import cn.fuck.fishfarming.R;
import cn.fuck.fishfarming.application.MyApplication;

/**
 * Created by Administrator on 2016/12/7.
 */

public class ControlItemAdapter extends BaseAdapter {
    private Context ctx;
    private Map<String,String> dict;
    private List<String> datas;
    private List<Map<String,String>> electrics;
    private String deviceId;
    static Handler nettyHandler=new Handler(Looper.myLooper());
    KProgressHUD hud;
    BaseDevice collectorInfo;
    public ControlItemAdapter(Context ctx){
        this.ctx=ctx;
    }

    public void setDict(Map<String, String> dict) {
        this.dict = dict;
        if(dict!=null){
            this.datas=new ArrayList<String>(dict.values());
        }

    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setCollectorInfo(BaseDevice collectorInfo) {
        this.collectorInfo = collectorInfo;
        if(collectorInfo!=null){
            this.electrics=collectorInfo.getSwitchs();
        }

    }

    @Override
    public int getCount() {
        if(electrics==null){
            return 0;
        }
        return electrics.size();
    }

    @Override
    public Object getItem(int i) {
        if(electrics==null){
            return null;
        }
        return electrics.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        if(view==null){
            view=LayoutInflater.from(ctx).inflate(R.layout.adapter_control_item,null);
        }

        TextView nameLabel=ButterKnife.findById(view,R.id.textView10);

        final Button switch1=ButterKnife.findById(view,R.id.switch1);
        nameLabel.setText(electrics.get(i).keySet().toString());
        if(dict!=null&&dict.get(deviceId)!=null){
            //00000000
            String value=dict.get(deviceId);
            char status=value.charAt(i);
            switch1.setSelected(status=='0'?false:true);
            switch1.setText(status=='0'?"关":"开");
        }else{
            switch1.setSelected(false);
            switch1.setText("关");
        }
        switch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked=!switch1.isSelected();

                MyApplication application= (MyApplication) ctx.getApplicationContext();
                application.showDialog("设置中...");

            }
        });


        return view;
    }

}
