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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.farmingsocket.TcpSocketService;
import com.farmingsocket.manager.ConstantsPool;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.kyleduo.switchbutton.SwitchButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import cn.farmFish.service.webserviceApi.bean.CollectorInfo;
import cn.fuck.fishfarming.R;
import cn.fuck.fishfarming.application.MyApplication;
import cn.fuck.fishfarming.utils.DateUtils;

/**
 * Created by Administrator on 2017/2/5 0005.
 */

public class RangeExpandAdapter extends BaseExpandableListAdapter {
    private List<CollectorInfo> collectorInfos;
    private Context ctx;
    private KProgressHUD hud;

    Handler mainUIHandeler=new Handler(Looper.getMainLooper());

    public RangeExpandAdapter(List<CollectorInfo> collectorInfos, Context ctx){
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
    public CollectorInfo getGroup(int groupPosition) {
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
        Button saveBtn;

    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent) {
        GraoupViewTag viewTag = null;
        if(convertView==null){
            convertView= LayoutInflater.from(ctx).inflate(R.layout.adapter_expand_setting_group,null);
            viewTag=new GraoupViewTag();
            viewTag.titleView= ButterKnife.findById(convertView,R.id.titleView);
            viewTag.saveBtn=ButterKnife.findById(convertView,R.id.button3);
            convertView.setTag(viewTag);

        }
        viewTag= (GraoupViewTag) convertView.getTag();

        viewTag.titleView.setText(getGroup(groupPosition).getPondName());

        viewTag.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TcpSocketService.getInstance().setDeviceId(collectorInfos.get(groupPosition).getDeviceID());
                int[] ranges=collectorInfos.get(groupPosition).getRange();
                int time=collectorInfos.get(groupPosition).getTime();
                if(ranges!=null&&ranges.length==2){
                    MyApplication myApplication= (MyApplication) ctx.getApplicationContext();
                    myApplication.showDialog("阈值数据保存中...");

                    TcpSocketService.getInstance().rangSetOrGet(ConstantsPool.MethodType.POST, ranges[1], ranges[0]);
                }
                if(time>0){
                    TcpSocketService.getInstance().timeSetOrGet(ConstantsPool.MethodType.POST, (short) time);
                }
            }
        });


        return convertView;
    }

    private void showToastMessage(String message){
        Toast.makeText(ctx,message,Toast.LENGTH_SHORT).show();
    }
    class ViewTag{
        EditText min_edit;
        EditText max_edit;
        EditText timeEdit;
        Button switchButton;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewTag tag;
        if(convertView==null){
            convertView=LayoutInflater.from(ctx).inflate(R.layout.adapter_expand_range_child,null);
            tag=new ViewTag();

            tag.min_edit=ButterKnife.findById(convertView,R.id.min_edit);
            tag.max_edit=ButterKnife.findById(convertView,R.id.max_edit);
            tag.timeEdit=ButterKnife.findById(convertView,R.id.timeEdit);
            tag.switchButton=ButterKnife.findById(convertView,R.id.switchButton);
            convertView.setTag(tag);
            tag.max_edit.addTextChangedListener(new TextWatcherImpl(groupPosition,1));
            tag.min_edit.addTextChangedListener(new TextWatcherImpl(groupPosition,0));
            tag.timeEdit.addTextChangedListener(new TextWatcherImpl(groupPosition,2));
            tag.switchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Button btn= (Button) v;

                    int modeVal= ConstantsPool.AUTO_MODE;

                    if(btn.isSelected()){
                        modeVal= ConstantsPool.MANUAL_MODE;
                    }

                    MyApplication myApplication= (MyApplication) ctx.getApplicationContext();
                    myApplication.showDialog("模式设置中...");
                    TcpSocketService.getInstance().setDeviceId(collectorInfos.get(groupPosition).getDeviceID());
                    TcpSocketService.getInstance().modeStatusSetOrGet(ConstantsPool.MethodType.POST,
                            (short) modeVal);
                }
            });

        }

        tag= (ViewTag) convertView.getTag();
        CollectorInfo collectorInfo=collectorInfos.get(groupPosition);

        if(collectorInfo.getRange()!=null&&collectorInfo.getRange().length==2){
            tag.min_edit.setText(String.valueOf(collectorInfo.getRange()[0]));
            tag.max_edit.setText(String.valueOf(collectorInfo.getRange()[1]));
        }else{
            tag.min_edit.setText(String.valueOf(-1));
            tag.max_edit.setText(String.valueOf(-1));
        }

        tag.timeEdit.setText(String.valueOf(collectorInfo.getTime()));




        if(collectorInfo.getMode()== ConstantsPool.AUTO_MODE){
            tag.switchButton.setSelected(true);
            tag.switchButton.setText("自动");
        }else{
            tag.switchButton.setSelected(false);
            tag.switchButton.setText("手动");
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

            if(type==1){
                int[] range=collectorInfos.get(pos).getRange();

                if(range!=null&&range.length==2){
                    collectorInfos.get(pos).setRange(new int[]{range[0],(int)val});
                }


            }else if(type==0){
                int[] range=collectorInfos.get(pos).getRange();
                if(range!=null&&range.length==2){
                    collectorInfos.get(pos).setRange(new int[]{(int)val,range[1]});
                }
            }else if(type==2){
                collectorInfos.get(pos).setTime( val);
            }


        }
    }
}
