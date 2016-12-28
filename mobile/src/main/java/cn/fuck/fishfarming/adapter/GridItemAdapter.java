package cn.fuck.fishfarming.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.ButterKnife;
import cn.fuck.fishfarming.R;

import static butterknife.ButterKnife.findById;

/**
 * Created by Administrator on 2016/11/27 0027.
 */

public class GridItemAdapter extends BaseAdapter{
    private String[] titles;
    private int[]    images;
    private Context ctx;
    private GridView gridView;

    public GridView getGridView() {
        return gridView;
    }

    public void setGridView(GridView gridView) {
        this.gridView = gridView;
    }

    public GridItemAdapter(Context ctx, String[] titles, int[] images){
           this.titles=titles;
           this.images=images;
           this.ctx=ctx;

    }
    @Override
    public int getCount(){
        if(this.titles==null){
            return 0;
        }
        return this.titles.length;
    }

    @Override
    public Object getItem(int position) {

        return position;
    }

    @Override
    public long getItemId(int position) {

        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView= LayoutInflater.from(ctx).inflate(R.layout.adapter_gird_item,null);
            ImageView imageView=ButterKnife.findById(convertView,R.id.imageView8);
            TextView  titleView=ButterKnife.findById(convertView,R.id.textView7);
            TextView   dotView=ButterKnife.findById(convertView,R.id.dotView);
            viewHolder.imageView=imageView;
            viewHolder.titleView=titleView;
            viewHolder.dotView=dotView;
            convertView.setTag(viewHolder);
        }

        viewHolder= (ViewHolder) convertView.getTag();
        viewHolder.titleView.setText(titles[position]);
        viewHolder.imageView.setImageResource(images[position]);

        if(position==3){
            viewHolder.dotView.setVisibility(View.VISIBLE);
        }else {
            viewHolder.dotView.setVisibility(View.GONE);
        }

//获取GridView的高度
        int height=gridView.getHeight();
        int width=gridView.getWidth();
//设置每个item的高度为GridView的1/3
        AbsListView.LayoutParams lp1=new AbsListView.LayoutParams(parent.getWidth()/2,parent.getHeight()/3);
        convertView.setLayoutParams(lp1);
        return convertView;
    }

    class  ViewHolder{
        ImageView imageView;
        TextView  titleView;
        TextView  dotView;

    }

}
