package cn.fuck.fishfarming.adapter.control;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.kyleduo.switchbutton.SwitchButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import cn.farmFish.service.webserviceApi.bean.CollectorInfo;
import cn.fuck.fishfarming.R;
import cn.fuck.fishfarming.view.ClipView;
import cn.netty.farmingsocket.SPackage;
import cn.netty.farmingsocket.SocketClientManager;
import cn.netty.farmingsocket.data.ICmdPackageProtocol;
import cn.netty.farmingsocket.data.IDataCompleteCallback;

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
    SetCallback setCallback;
    KProgressHUD hud;
    CollectorInfo collectorInfo;
    public ControlItemAdapter(Context ctx){
        this.ctx=ctx;

        this.setCallback=new SetCallback(ctx);


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



        //00000000
        String value=dict.get("30");
        char status=value.charAt(i);




        TextView nameLabel=ButterKnife.findById(view,R.id.textView10);

        SwitchButton switch1=ButterKnife.findById(view,R.id.switch1);
        nameLabel.setText(electrics[i]);
        switch1.setCheckedImmediatelyNoEvent(status=='0'?false:true);





        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v("switch","change..."+isChecked+" "+i);


                if(collectorInfo.getMode()== ICmdPackageProtocol.AUTO_MODE&&i<5){
                    Toast.makeText(ctx,"处于自动模式,无法手动控制",Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                    return;
                }

                SocketClientManager.getInstance().getHandler().sendFuckControlCmd(i + 1, isChecked ? 1 : 0, deviceId, setCallback);
            }
        });





        return view;
    }

    private static class SetCallback implements IDataCompleteCallback{
        Context ctx;
        SetCallback(Context ctx){
            this.ctx=ctx;
        }

        @Override
        public void onDataComplete(final SPackage spackage) {
            nettyHandler.post(new Runnable() {
                @Override
                public void run() {

                    if(spackage==null){
                        Toast.makeText(ctx,"连接已断开",Toast.LENGTH_SHORT).show();
                    }else{
                        if(spackage.getCmdword()==15&&spackage.getLength()==16){
                            Toast.makeText(ctx,"设置成功",Toast.LENGTH_SHORT).show();
                        }

                    }


                }
            });
        }
    }


}
