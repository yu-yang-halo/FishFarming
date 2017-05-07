package cn.fuck.fishfarming.adapter.control;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.farmingsocket.TcpSocketService;
import com.farmingsocket.manager.ConstantsPool;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.kyleduo.switchbutton.SwitchButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import cn.farmFish.service.webserviceApi.bean.CollectorInfo;
import cn.fuck.fishfarming.R;
import cn.fuck.fishfarming.application.MyApplication;

/**
 * Created by Administrator on 2016/12/7.
 */

public class ControlItemAdapter extends BaseAdapter {
    private Context ctx;
    private Map<String,String> dict;
    private List<String> datas;
    private String[] electrics;
    private String deviceId;
    static Handler nettyHandler=new Handler(Looper.myLooper());
    KProgressHUD hud;
    CollectorInfo collectorInfo;
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

    public void setCollectorInfo(CollectorInfo collectorInfo) {
        this.collectorInfo = collectorInfo;
        if(collectorInfo!=null){
            this.electrics=collectorInfo.getDeviceElectricsArr();
        }

    }

    @Override
    public int getCount() {
        if(electrics==null){
            return 0;
        }
        return electrics.length;
    }

    @Override
    public Object getItem(int i) {
        if(electrics==null){
            return null;
        }
        return electrics[i];
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
        nameLabel.setText(electrics[i]);
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
                if(collectorInfo.getMode()== ConstantsPool.AUTO_MODE&&i<5){
                    Toast.makeText(ctx,"处于自动模式,无法手动控制",Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                    return;
                }
                boolean isChecked=!switch1.isSelected();

                MyApplication application= (MyApplication) ctx.getApplicationContext();
                application.showDialog("设置中...");

                TcpSocketService.getInstance().setDeviceId(collectorInfo.getDeviceID());
                TcpSocketService.getInstance().sendFuckControlCmd(i + 1, isChecked ? 1 : 0, deviceId);
            }
        });


        return view;
    }

}
