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
import cn.fuck.fishfarming.R;
import cn.fuck.fishfarming.view.ClipView;
import cn.netty.farmingsocket.SPackage;
import cn.netty.farmingsocket.SocketClientManager;
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
    Handler nettyHandler=new Handler(Looper.myLooper());

    KProgressHUD hud;
    public ControlItemAdapter(Map<String,String> dict, Context ctx,String[] electrics,String deviceId){
        this.ctx=ctx;
        this.dict=dict;
        this.datas=new ArrayList<String>(dict.values());

        this.electrics=electrics;
        this.deviceId=deviceId;



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
        switch1.setCheckedImmediately(status=='0'?false:true);


        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v("switch","change..."+isChecked+" "+i);

                hud= KProgressHUD
                        .create(ctx).setLabel("设置中...")
                        .show();

                SocketClientManager.getInstance().getHandler().sendFuckControlCmd(i + 1, isChecked ? 1 : 0, deviceId, new IDataCompleteCallback() {
                    @Override
                    public void onDataComplete(SPackage spackage) {

                        nettyHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if(hud!=null){
                                    hud.dismiss();
                                }
                                Toast.makeText(ctx,"设置成功",Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
            }
        });





        return view;
    }


}
