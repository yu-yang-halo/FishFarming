package cn.fuck.fishfarming.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Region;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/12/8.
 */

public class ClipView extends ImageView {

    private float percentage;

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public ClipView(Context context) {
        super(context);
    }

    public ClipView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClipView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ClipView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        // left top right bottom
        // left左边距离x起点的位置   right右边距离x起点的位置

        if(percentage>1){
            percentage=1.0f;
        }

        canvas.clipRect(canvas.getWidth()*percentage,0,canvas.getWidth(),canvas.getHeight(), Region.Op.REPLACE);//设置显示范围

        canvas.drawColor(Color.WHITE);



    }
}
