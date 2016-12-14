package cn.fuck.fishfarming.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hikvision.netsdk.PTZCommand;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.farmFish.service.webserviceApi.WebServiceApi;
import cn.farmFish.service.webserviceApi.WebServiceCallback;
import cn.farmFish.service.webserviceApi.bean.CollectorInfo;
import cn.farmFish.service.webserviceApi.bean.VideoInfo;
import cn.fuck.fishfarming.R;
import cn.fuck.fishfarming.activity.TabActivity;
import cn.fuck.fishfarming.adapter.VideoItemAdapter;
import cn.fuck.fishfarming.adapter.VideoShowAdapter;
import cn.fuck.fishfarming.application.MyApplication;
import cn.videocore.PlaySurfaceView;
import cn.videocore.VideoManagerHelper;

/**
 * Created by Administrator on 2016/12/3 0003.
 */

public class VideoFragment extends Fragment implements FragmentProtocol {
    @BindView(R.id.containerView)
    FrameLayout containerView;
    @BindView(R.id.controlGridView)
    GridView videoItemGridView;
    @BindView(R.id.ptzControlGridView)
    GridView ptzControlGridView;


    String[] ptzControls=new String[]{"左上","上仰","右上","左转","自动","右转","左下","下俯","右下"};
    int[] ptzControlVals=new int[]{PTZCommand.UP_LEFT,PTZCommand.TILT_UP,PTZCommand.UP_RIGHT,
                                   PTZCommand.PAN_LEFT,PTZCommand.PAN_AUTO,PTZCommand.PAN_RIGHT,
                                   PTZCommand.DOWN_LEFT,PTZCommand.TILT_DOWN,PTZCommand.DOWN_RIGHT};

    List<VideoInfo> results;
    VideoManagerHelper helper=new VideoManagerHelper();

    private PlaySurfaceView[]  playView=new PlaySurfaceView[16];

    TabActivity tabActivity;
    int selectPos=-1;
    boolean hasLoginYN=false;
    int mode;
    boolean onHiddenChanged=true;
    boolean onStartYN=false;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        tabActivity= (TabActivity) context;
        Log.e("VideoF","onAttach");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e("VideoF","onCreateView");
        View view=inflater.inflate(R.layout.fr_video,null);
        ButterKnife.bind(this,view);


        MyApplication myApp= (MyApplication) getActivity().getApplicationContext();
        results=myApp.getVideoInfos();
        ptzControlGridViewInit();
        videoItemViewInit(results);
        mode= caculateColumns(results.size());


        return view;
    }


    @Override
    public void closeVideo() {
        helper.stopMultiPreview(playView);
        clearPlayVideoArr();
    }

    @Override
    public void screenScale(int type) {
        if(type==0){
            new VideoShowTask(-1).execute();
        }

    }



    private void showVideo(int pos){

        if(results!=null&&results.size()>0){
            int mode= caculateColumns(results.size());
            if(initPlayVideoArr(mode,pos)>0){
                helper.startMultiPreview(playView,pos,results.size());
            }

        }
    }

    private int initPlayVideoArr(int mode,int pos){
        int size=mode*mode;

        if(pos>=0){

            if(playView[pos] == null)
            {
                playView[pos]=new PlaySurfaceView(getActivity());
                playView[pos].setParam(containerView.getWidth(),containerView.getHeight());
                playView[pos].setBackgroundColor(getResources().getColor(R.color.colorBlack));
                playView[pos].setZOrderOnTop(true);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT);
                params.topMargin = 0;
                params.leftMargin = 0;
                params.gravity = Gravity.TOP | Gravity.LEFT;
                containerView.addView(playView[pos],params);
            }
        }else {
            for(int i = 0; i <size; i++)
            {

                if(playView[i] == null)
                {
                    playView[i]=new PlaySurfaceView(getActivity());
                    playView[i].setParam(containerView.getWidth()/mode,containerView.getHeight()/mode);
                    playView[i].setBackgroundColor(getResources().getColor(R.color.colorBlack));
                    playView[i].setZOrderOnTop(true);
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.WRAP_CONTENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT);
                    params.topMargin =(i/mode) * playView[i].getCurHeight();
                    params.leftMargin = (i%mode) * playView[i].getCurWidth();
                    params.gravity = Gravity.TOP | Gravity.LEFT;
                    containerView.addView(playView[i],params);
                }
            }
        }


        return 1;
    }

    private void clearPlayVideoArr(){
        if(containerView!=null){
            containerView.removeAllViews();
        }
    }



    class VideoShowTask extends AsyncTask<String,String,String>{
        KProgressHUD hud;
        int pos;
        public VideoShowTask(int pos){
            this.pos=pos;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            hud=KProgressHUD
                    .create(getActivity()).setLabel("视频加载中...")
                    .show();

        }

        @Override
        protected String doInBackground(String... params) {
            helper.stopMultiPreview(playView);
            if(hasLoginYN){
                return null;
            }
            if(results!=null&&results.size()>0){
                hasLoginYN=helper.loginDevice(results.get(0));
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            clearPlayVideoArr();
            if(pos<0){
                videoItemGridView.setVisibility(View.VISIBLE);
                ptzControlGridView.setVisibility(View.GONE);
                selectPos=-1;
            }else{
                videoItemGridView.setVisibility(View.GONE);
                ptzControlGridView.setVisibility(View.VISIBLE);
                tabActivity.setShowFullScreenBtn();
                selectPos=pos;
            }
            showVideo(pos);
            if(hud!=null){
                hud.dismiss();
            }
        }
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


    private void ptzControlGridViewInit(){
        List<Map<String,String>> list=new ArrayList<>();

        for(String str:ptzControls){
            Map<String,String> dict=new HashMap<>();
            dict.put("name",str);
            list.add(dict);
        }
        String[] from=new String[]{"name"};
        int[] to=new int[]{R.id.ptzBtn};
        SimpleAdapter adapter=new SimpleAdapter(getActivity(),list,R.layout.adapter_video_item0,from,to);
        ptzControlGridView.setNumColumns(3);
        ptzControlGridView.setAdapter(adapter);
        ptzControlGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(selectPos<0){
                    return;
                }
                Log.v("onItemClick",selectPos+" onItemClick  position "+position);
                helper.ptzControl(selectPos,ptzControlVals[position]);

            }
        });
    }

    private void videoItemViewInit(final List<VideoInfo> results){

        if(results!=null){
                VideoItemAdapter videoItemAdapter=new VideoItemAdapter(getActivity(),results);
                videoItemGridView.setNumColumns(4);
                videoItemGridView.setAdapter(videoItemAdapter);
                videoItemGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        new VideoShowTask(position).execute();
                    }
                });

            }



    }
    @Override
    public void onStop() {
        super.onStop();

        Log.e("VideoF","onStop");
    }




    @Override
    public void onStart() {
        super.onStart();
        Log.e("VideoF","onStart");
        onStartYN=true;
        if(!onHiddenChanged){
            new VideoShowTask(selectPos).execute();
        }



    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("VideoF","onPause");
        onStartYN=false;
        closeVideo();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("VideoF","onDestroyView");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e("VideoF","onDetach");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e("VideoF","onViewCreated");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("VideoF","onActivityCreated");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        onHiddenChanged=hidden;

        Log.e("VideoF","onHiddenChanged "+hidden);

        if(onStartYN){
            if(!hidden){
                new VideoShowTask(selectPos).execute();
            }else{
                closeVideo();
            }
        }


    }

}
