package cn.fuck.fishfarming.adapter.setting;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.farmingsocket.client.IWebSocketReq;
import com.farmingsocket.client.WebSocketReqImpl;
import com.farmingsocket.client.bean.BaseDevice;
import com.farmingsocket.client.bean.UThresholdItem;
import com.farmingsocket.manager.ConstantsPool;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.videogo.ui.util.Constants;

import java.util.List;

import butterknife.ButterKnife;
import cn.fuck.fishfarming.R;
import cn.fuck.fishfarming.application.DataHelper;
import cn.fuck.fishfarming.application.MyApplication;
import cn.fuck.fishfarming.utils.ConvertUtils;
import cn.fuck.fishfarming.utils.DateUtils;
import cn.fuck.fishfarming.view.DigitalDialog;
import cn.fuck.fishfarming.view.KeyboardDigitalEdit;

/**
 * Created by Administrator on 2017/2/5 0005.
 */

public class RangeExpandAdapter extends BaseExpandableListAdapter{
    private List<BaseDevice> collectorInfos;
    private Context ctx;
    private KProgressHUD hud;


    Handler mainUIHandeler=new Handler(Looper.getMainLooper());

    public RangeExpandAdapter(List<BaseDevice> collectorInfos, Context ctx){
        this.ctx=ctx;
        this.collectorInfos=collectorInfos;
    }


    @Override
    public int getGroupCount() {
        if(collectorInfos!=null){

            return collectorInfos.size();
        }
        return 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public BaseDevice getGroup(int groupPosition) {
        if(collectorInfos!=null){
            return collectorInfos.get(groupPosition);
        }
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }



    class GraoupViewTag{
        TextView titleView;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent) {
        GraoupViewTag viewTag = null;
        if(convertView==null){
            convertView= LayoutInflater.from(ctx).inflate(R.layout.adapter_expand_setting_group,null);
            viewTag=new GraoupViewTag();
            viewTag.titleView= ButterKnife.findById(convertView,R.id.titleView);
            convertView.setTag(viewTag);

        }
        viewTag= (GraoupViewTag) convertView.getTag();

        viewTag.titleView.setText(getGroup(groupPosition).getName());



        return convertView;
    }

    private void showToastMessage(String message){
        Toast.makeText(ctx,message,Toast.LENGTH_SHORT).show();
    }
    class ViewTag{
        KeyboardDigitalEdit min_edit;
        KeyboardDigitalEdit max_edit;
        KeyboardDigitalEdit timeEdit;
        Button switchButton;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewTag tag;
        final BaseDevice collectorInfo=collectorInfos.get(groupPosition);
        if(convertView==null){
            convertView=LayoutInflater.from(ctx).inflate(R.layout.adapter_expand_range_child,null);
            tag=new ViewTag();

            tag.min_edit=ButterKnife.findById(convertView,R.id.min_edit);
            tag.max_edit=ButterKnife.findById(convertView,R.id.max_edit);
            tag.timeEdit=ButterKnife.findById(convertView,R.id.timeEdit);

            final KeyboardDigitalEdit min_edit= tag.min_edit;
            final KeyboardDigitalEdit max_edit= tag.max_edit;
            final KeyboardDigitalEdit timeEdit= tag.timeEdit;

            DigitalDialog.Builder.LVCallback lvCallback=new DigitalDialog.Builder.LVCallback() {
                @Override
                public void onConfirmClick(int value, int par) {
                    switch (par){
                        case 0x1000:
                            int max=ConvertUtils.toInt(max_edit.getText());
                            if(value>max){
                                showToastMessage("最小值不得大于最大值");
                            }else{
                                min_edit.setText(String.valueOf(value));
                                WebSocketReqImpl.getInstance().configThreshold(collectorInfo.getMac(),
                                        collectorInfo.getGprsmac(),value,max);
                            }



                            break;
                        case 0x1001:
                            int min=ConvertUtils.toInt(min_edit.getText());
                            if(value<min){
                                showToastMessage("最大值不得小于最小值");
                            }else{
                                max_edit.setText(String.valueOf(value));
                                WebSocketReqImpl.getInstance().configThreshold(collectorInfo.getMac(),
                                        collectorInfo.getGprsmac(),min,value);
                            }
                            break;
                        case 0x1002:
                            timeEdit.setText(String.valueOf(value));
                            WebSocketReqImpl.getInstance().configPeriod(collectorInfo.getMac(),value);
                            break;
                    }
                }
            };

            tag.min_edit.setLVCallback(lvCallback);
            tag.min_edit.setValue(100,0,0x1000);
            tag.max_edit.setLVCallback(lvCallback);
            tag.max_edit.setValue(100,0,0x1001);
            tag.timeEdit.setLVCallback(lvCallback);
            tag.timeEdit.setValue(100,0,0x1002);

            tag.switchButton=ButterKnife.findById(convertView,R.id.switchButton);
            convertView.setTag(tag);
            tag.max_edit.addTextChangedListener(new TextWatcherImpl(groupPosition,1));
            tag.min_edit.addTextChangedListener(new TextWatcherImpl(groupPosition,0));
            tag.timeEdit.addTextChangedListener(new TextWatcherImpl(groupPosition,2));
            tag.switchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button btn= (Button) v;
                    MyApplication myApplication= (MyApplication) ctx.getApplicationContext();
                    myApplication.showDialog("模式设置中...");

                    if(btn.isSelected()){
                       //当前为自动-->手动
                        WebSocketReqImpl.getInstance().configMode(collectorInfo.getMac(),
                                collectorInfo.getGprsmac(),2);
                    }else{
                        //当前为手动-->自动
                        WebSocketReqImpl.getInstance().configMode(collectorInfo.getMac(),
                                collectorInfo.getGprsmac(),1);
                    }



                }
            });

        }

        tag= (ViewTag) convertView.getTag();


        UThresholdItem uThresholdItem=DataHelper.getMyApp().getUThresholdItem(collectorInfo.getMac());

        if(uThresholdItem!=null){
            if(uThresholdItem.baseMode!=null){
                if(uThresholdItem.baseMode.getModel()== ConstantsPool.MODE_MANUAL){
                    tag.switchButton.setText("手动");
                    tag.switchButton.setSelected(false);
                }else{
                    tag.switchButton.setText("自动");
                    tag.switchButton.setSelected(true);
                }
            }
            if(uThresholdItem.baseThreshold!=null){
                tag.max_edit.setText(String.valueOf(uThresholdItem.baseThreshold.getMax()));
                tag.min_edit.setText(String.valueOf(uThresholdItem.baseThreshold.getMin()));
            }
            if(uThresholdItem.baseTimePeriod!=null){
                tag.timeEdit.setText(String.valueOf(uThresholdItem.baseTimePeriod.getMinute()));
            }

        }


        return convertView;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public  class TextWatcherImpl implements TextWatcher {
        private int pos;
        private int type;
        public TextWatcherImpl(int pos,int  type){
            this.pos=pos;
            this.type=type;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            Log.e("beforeTextChanged","s "+s +" start "+start +" count "+count+" after "+after);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.e("onTextChanged","s "+s +" start "+start +" count "+count+" before "+before);
        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.e("afterTextChanged","s "+s);
            if(s.toString().isEmpty()){
                return;
            }

            int val= DateUtils.toInt(s.toString());



        }
    }
}
