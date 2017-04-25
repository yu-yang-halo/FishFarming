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

import com.kaopiz.kprogresshud.KProgressHUD;
import com.kyleduo.switchbutton.SwitchButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import cn.farmFish.service.webserviceApi.WebServiceApi;
import cn.farmFish.service.webserviceApi.WebServiceCallback;
import cn.farmFish.service.webserviceApi.bean.CollectorInfo;
import cn.farmFish.service.webserviceApi.bean.SensorInfo;
import cn.fuck.fishfarming.R;
import cn.netty.farmingsocket.SPackage;
import cn.netty.farmingsocket.SocketClientManager;
import cn.netty.farmingsocket.data.ICmdPackageProtocol;
import cn.netty.farmingsocket.data.IDataCompleteCallback;

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

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView= LayoutInflater.from(ctx).inflate(R.layout.adapter_expand_setting_group,null);
        }
        TextView titleView= ButterKnife.findById(convertView,R.id.titleView);
        final Button saveBtn=ButterKnife.findById(convertView,R.id.button3);

        titleView.setText(getGroup(groupPosition).getPondName());

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] ranges=collectorInfos.get(groupPosition).getRange();
                if(ranges!=null&&ranges.length==2){
                    hud= KProgressHUD
                            .create(ctx).setLabel("阈值数据保存中...").show();
                    SocketClientManager.getInstance().getHandler().rangSetOrGet(ICmdPackageProtocol.MethodType.POST, ranges[1], ranges[0], new IDataCompleteCallback() {
                        @Override
                        public void onDataComplete(SPackage spackage) {
                            if(spackage==null){
                                return;
                            }
                            if (spackage.getCmdword()==19&&spackage.getRang().length==2){
                               if(spackage.getRang()[0]>=0&&spackage.getRang()[1]>=0){
                                   mainUIHandeler.post(new Runnable() {
                                       @Override
                                       public void run() {
                                           hud.dismiss();
                                           showToastMessage("阈值保存成功");
                                       }
                                   });
                               }
                            }


                        }
                    });
                }


            }
        });


        return convertView;
    }

    private void showToastMessage(String message){
        Toast.makeText(ctx,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=LayoutInflater.from(ctx).inflate(R.layout.adapter_expand_range_child,null);
        }
        final TextView modeLabel=ButterKnife.findById(convertView,R.id.textView16);
        EditText min_edit=ButterKnife.findById(convertView,R.id.min_edit);
        EditText max_edit=ButterKnife.findById(convertView,R.id.max_edit);
        final SwitchButton switchButton=ButterKnife.findById(convertView,R.id.switchButton);
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int modeVal= ICmdPackageProtocol.MANUAL_MODE;
                if(switchButton.isChecked()){
                    modeVal= ICmdPackageProtocol.AUTO_MODE;
                    modeLabel.setText("自动");
                }else{
                    modeLabel.setText("手动");
                }

                hud= KProgressHUD
                        .create(ctx).setLabel("模式设置中...").show();

                SocketClientManager.getInstance().getHandler().modeStatusSetOrGet(ICmdPackageProtocol.MethodType.POST,
                        (short) modeVal, new IDataCompleteCallback() {
                            @Override
                            public void onDataComplete(SPackage spackage) {
                                if(spackage==null){
                                    return;
                                }
                                if (spackage.getCmdword()==21){
                                    Log.v("Mode", spackage.getMode()==ICmdPackageProtocol.MANUAL_MODE?"手动":"自动");
                                    collectorInfos.get(groupPosition).setMode(spackage.getMode());
                                    mainUIHandeler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            hud.dismiss();
                                            notifyDataSetChanged();
                                            showToastMessage("设置成功");
                                        }
                                    });

                                }
                            }
                        });
            }
        });

        max_edit.addTextChangedListener(new TextWatcherImpl(groupPosition,true));
        min_edit.addTextChangedListener(new TextWatcherImpl(groupPosition,false));

        CollectorInfo collectorInfo=collectorInfos.get(groupPosition);

        if(collectorInfo.getRange()!=null&&collectorInfo.getRange().length==2){
            min_edit.setText(String.valueOf(collectorInfo.getRange()[0]));
            max_edit.setText(String.valueOf(collectorInfo.getRange()[1]));
        }else{
            min_edit.setText(String.valueOf(-1));
            max_edit.setText(String.valueOf(-1));
        }

        if(collectorInfo.getMode()== ICmdPackageProtocol.AUTO_MODE){
            switchButton.setChecked(true);
            modeLabel.setText("自动");
        }else{
            switchButton.setChecked(false);
            modeLabel.setText("手动");
        }


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public  class TextWatcherImpl implements TextWatcher {
        private int pos;
        private boolean isMax;
        public TextWatcherImpl(int pos,boolean isMax){
            this.pos=pos;
            this.isMax=isMax;
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
            float val= Float.parseFloat(s.toString());

            if(isMax){
                int[] range=collectorInfos.get(pos).getRange();

                if(range!=null&&range.length==2){
                    collectorInfos.get(pos).setRange(new int[]{range[0],(int)val});
                }


            }else{
                int[] range=collectorInfos.get(pos).getRange();
                if(range!=null&&range.length==2){
                    collectorInfos.get(pos).setRange(new int[]{(int)val,range[1]});
                }
            }


        }
    }
}
