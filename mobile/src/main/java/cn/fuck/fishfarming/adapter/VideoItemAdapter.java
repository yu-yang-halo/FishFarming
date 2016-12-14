package cn.fuck.fishfarming.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.ButterKnife;
import cn.farmFish.service.webserviceApi.bean.VideoInfo;
import cn.fuck.fishfarming.R;

/**
 * Created by Administrator on 2016/12/10 0010.
 */

public class VideoItemAdapter extends BaseAdapter {
    private Context ctx;
    private List<VideoInfo> videoInfoList;
    public VideoItemAdapter(Context ctx, List<VideoInfo> videoInfoList){
        this.ctx=ctx;
        this.videoInfoList=videoInfoList;
    }
    @Override
    public int getCount() {
        if(videoInfoList!=null){
            return videoInfoList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(videoInfoList!=null){
            return videoInfoList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=LayoutInflater.from(ctx).inflate(R.layout.adapter_video_item,null);
        }
        TextView  nameLabel=ButterKnife.findById(convertView,R.id.textView12);
        ImageView imageView=ButterKnife.findById(convertView,R.id.imageView10);

        nameLabel.setText(videoInfoList.get(position).getF_Name());



        return convertView;
    }
}
