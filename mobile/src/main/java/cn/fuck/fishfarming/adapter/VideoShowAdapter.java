package cn.fuck.fishfarming.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import butterknife.ButterKnife;
import cn.farmFish.service.webserviceApi.bean.VideoInfo;
import cn.fuck.fishfarming.R;
import cn.videocore.PlaySurfaceView;

import static butterknife.ButterKnife.findById;

/**
 * Created by Administrator on 2016/12/12.
 */

public class VideoShowAdapter extends BaseAdapter {
    Context ctx;
    List<VideoInfo> videoInfos;
    private int mode;
    private PlaySurfaceView[]  playView=new PlaySurfaceView[16];

    public PlaySurfaceView[] getPlayView() {
        return playView;
    }




    public VideoShowAdapter(Context ctx, List<VideoInfo> videoInfos){
        this.ctx=ctx;
        this.videoInfos=videoInfos;
        this.mode=caculateColumns(videoInfos.size());
    }

    @Override
    public int getCount() {
        if(videoInfos!=null){
            return videoInfos.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(videoInfos!=null){
            return videoInfos.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            viewHolder=new ViewHolder();

            convertView= LayoutInflater.from(ctx).inflate(R.layout.adapter_video_show,null);
            viewHolder.surfaceView=ButterKnife.findById(convertView,R.id.surfaceView);
            viewHolder.surfaceView.setZOrderOnTop(true);
            viewHolder.surfaceView.setParam(parent.getWidth()/mode,parent.getHeight()/mode);
            convertView.setTag(viewHolder);
        }
        viewHolder= (ViewHolder) convertView.getTag();
        playView[position]=viewHolder.surfaceView;


        return convertView;
    }
    class ViewHolder{
        PlaySurfaceView surfaceView;
    }

    private int caculateColumns(int size){
        if(size<=1){
            return 1;
        }else if(size<=4){
            return 2;
        }else if(size<=9){
            return 3;
        }else if(size<=16){
            return 4;
        }
        return 0;
    }
}
