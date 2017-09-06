package cn.fuck.fishfarming.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/5/5.
 */

public class AlwaysClickButton extends AppCompatImageButton {
    private float x,tempX=-1;
    private float y,tempY=-1;
    Timer timer = null;

    int mPar = 0;
    private Context mContext;
    LVMuiltClickCallBack m_CallBack=null;

    private final MyHandler myHandler = new MyHandler();
    private class MyHandler extends Handler {
        public MyHandler( ){

        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    if (m_CallBack!=null)
                        m_CallBack.onMuiltClick(mPar,0);
                    break;
                case 1:
                    if (m_CallBack!=null)
                        m_CallBack.onMuiltClick(mPar,1);
                    break;

            }
        }
    }

    public AlwaysClickButton(Context context) {
        super(context);
        mContext = context;
    }

    public interface LVMuiltClickCallBack{
        public void onMuiltClick(int par, int isSend);
    }

    public void setValve(int par,LVMuiltClickCallBack callback)
    {
        mPar = par;
        m_CallBack = callback;
    }


    public AlwaysClickButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }

    public AlwaysClickButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {


    }
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    long startTime=0;
    long endTime=0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        //获取相对屏幕的坐标，即以屏幕左上角为原点
        x = event.getRawX();
        y = event.getRawY();   //statusBarHeight是系统状态栏的高度
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:    //捕获手指触摸按下动作
                Message message = new Message();
                message.obj =1 ;
                message.what =1 ;
                myHandler.sendMessage(message);

                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.obj =1 ;
                        message.what =1 ;
                        myHandler.sendMessage(message);

                    }
                }, 300, 100);

                break;
            case MotionEvent.ACTION_MOVE:   //捕获手指触摸移动动作
                break;
            case MotionEvent.ACTION_UP:    //捕获手指触摸离开动作
            case MotionEvent.ACTION_CANCEL:
                endTime=System.currentTimeMillis();
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                    Message message1 = new Message();
                    message1.obj =1 ;
                    message1.what =0 ;
                    myHandler.sendMessage(message1);
                }
                break;

        }
        return true;
    }
}
