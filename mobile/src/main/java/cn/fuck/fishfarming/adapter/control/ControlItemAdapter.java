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

import com.farmingsocket.client.WebSocketReqImpl;
import com.farmingsocket.client.bean.BaseDevice;
import com.farmingsocket.client.bean.UControlItem;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import cn.fuck.fishfarming.R;
import cn.fuck.fishfarming.application.DataHelper;
import cn.fuck.fishfarming.application.MyApplication;

/**
 * Created by Administrator on 2016/12/7.
 */

public class ControlItemAdapter extends BaseAdapter {
    private Context ctx;
    private List<UControlItem> uControlItems;
    private BaseDevice collectorInfo;
    private Map<String,String> stringSwitchNameMap;
    public ControlItemAdapter(Context ctx){
        this.ctx=ctx;
    }

    public void setControlItems(List<UControlItem> uControlItems) {
        this.uControlItems = uControlItems;
    }


    public void setCollectorInfo(BaseDevice collectorInfo) {
        this.collectorInfo = collectorInfo;
        this.stringSwitchNameMap= collectorInfo.getStringSwitchMap();
    }

    @Override
    public int getCount() {
        if(uControlItems==null){
            return 0;
        }
        return uControlItems.size();
    }

    @Override
    public Object getItem(int i) {
        if(uControlItems==null){
            return null;
        }
        return uControlItems.get(i);
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

        final UControlItem uControlItem=uControlItems.get(i);
        uControlItem.setName(stringSwitchNameMap.get(uControlItem.getNumber()));

        nameLabel.setText(uControlItem.getName());


        if(uControlItem!=null){
            String status= uControlItem.getStatus();

            switch1.setSelected("00".equals(status)?false:true);
            switch1.setText("00".equals(status)?"关":"开");
        }else{
            switch1.setSelected(false);
            switch1.setText("关");
        }
        switch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked=!switch1.isSelected();

                WebSocketReqImpl.getInstance().controlDevice(collectorInfo.getMac(),
                        collectorInfo.getGprsmac(),
                        uControlItem.getNumber(),
                        isChecked?1:0);



            }
        });


        return view;
    }

}
