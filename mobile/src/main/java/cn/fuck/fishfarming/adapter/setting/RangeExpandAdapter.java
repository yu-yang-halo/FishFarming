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

import com.farmingsocket.client.bean.BaseDevice;
import com.farmingsocket.manager.ConstantsPool;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.List;

import butterknife.ButterKnife;
import cn.fuck.fishfarming.R;
import cn.fuck.fishfarming.application.MyApplication;
import cn.fuck.fishfarming.utils.DateUtils;

/**
 * Created by Administrator on 2017/2/5 0005.
 */

public class RangeExpandAdapter extends BaseExpandableListAdapter {
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

        viewTag.titleView.setText(getGroup(groupPosition).getName());

        viewTag.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                }
            });

        }

        tag= (ViewTag) convertView.getTag();
        BaseDevice collectorInfo=collectorInfos.get(groupPosition);


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
