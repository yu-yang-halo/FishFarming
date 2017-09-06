package cn.fuck.fishfarming.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import cn.fuck.fishfarming.utils.ConvertUtils;

/**
 * @author sunday
 * 2013-12-04
 */
public class KeyboardDigitalEdit extends EditText implements View.OnClickListener{
	private final static String TAG = "EditTextWithDel";
	DigitalDialog.Builder dialog;
	private Context mContext;
	private int Max = 99;
	private int Min = 1;
	private int Value=0;
	private int ParMsg = 0;
	private float x,tempX=-1;
	private float y,tempY=-1;
	private DigitalDialog.Builder.LVCallback pParentCallback;
	private  boolean enable;
	private int overLoadValue;
	public KeyboardDigitalEdit(Context context) {
		super(context);
		mContext = context;
		setClickable(true);
		init();
	}
	public void setOverLoadEnable(boolean enable,int overLoadValue){
		this.enable=enable;
		this.overLoadValue=overLoadValue;
		initEditStyle(getText());
	}

	private void initEditStyle(CharSequence text){
		if(enable){
			int value= ConvertUtils.toInt(text);
			if(value>=overLoadValue){
				setTextColor(Color.parseColor("#ff0000"));
			}else{
				setTextColor(Color.rgb(0,0,0));
			}
		}
	}

	@Override
	public void setText(CharSequence text, BufferType type) {
		super.setText(text, type);
		initEditStyle(text);
	}

	@Override
	public void onClick(View view)
	{

	}

	public void setValue(int max,int min, int par)
	{
		Max = max;
		Min = min;

		ParMsg = par;
	}

	public void setMax(int max)
	{
		Max = max;
	}

	public void setMin(int min)
	{
		Min = min;
	}

	public int getMax()
	{
		return Max;
	}

	public int getMin()
	{
		return Min;
	}

	public void setLVCallback(DigitalDialog.Builder.LVCallback pContent)
	{
		pParentCallback = pContent;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//获取相对屏幕的坐标，即以屏幕左上角为原点
		x = event.getRawX();
		y = event.getRawY();   //statusBarHeight是系统状态栏的高度
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:    //捕获手指触摸按下动作

				tempX=x;
				tempY=y;
				break;
			case MotionEvent.ACTION_MOVE:   //捕获手指触摸移动动作

				break;
			case MotionEvent.ACTION_UP:    //捕获手指触摸离开动作

				if(event.getX()>0&&event.getY()>0){
					onShowDialog();
				}
				break;
		}

		return true;
	}

	public void onShowDialog()
	{
		if (isEnabled())
		{
			String i = this.getText().toString();
			int value=ConvertUtils.toInt(i);


			dialog = new DigitalDialog.Builder( mContext, pParentCallback,Max,Min,value,ParMsg);
			DigitalDialog dlg = dialog.create();
			dialog.setTitle("数字键盘");


			dlg.show();
			Window dialogWindow = dlg.getWindow();

			WindowManager.LayoutParams lp = dialogWindow.getAttributes();
			dialogWindow.setGravity(Gravity.BOTTOM);

		}

	}

	public KeyboardDigitalEdit(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		init();
	}

	public KeyboardDigitalEdit(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}
	
	private void init() {
		this.setTextColor(Color.rgb(0,0,0));
	}


    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

}
