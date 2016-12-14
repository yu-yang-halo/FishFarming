package cn.fuck.fishfarming.adapter.history;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.renderer.YAxisRenderer;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.ButterKnife;
import cn.farmFish.service.webserviceApi.bean.CollWantData;
import cn.fuck.fishfarming.R;
import cn.fuck.fishfarming.utils.ConstantUtils;

/**
 * Created by Administrator on 2016/12/9.
 */

public class HisItemAdapter extends BaseAdapter {
    private Context ctx;
    private Map<String,List<CollWantData>> dicts;
    private List<String> keys;
    private List<List<CollWantData>> values;


    public Map<String, List<CollWantData>> getDicts() {
        return dicts;
    }

    public void setDicts(Map<String,List<CollWantData>> dicts) {
        this.dicts = dicts;

        if(dicts!=null){
            this.values=new ArrayList<>(dicts.values());
            this.keys=new ArrayList<>(dicts.keySet());
        }



    }

    public HisItemAdapter(Context ctx, Map<String,List<CollWantData>> dicts){
        this.ctx   = ctx;
        setDicts(dicts);



    }
    @Override
    public int getCount() {
        if(dicts==null){
            return 0;
        }
        return dicts.size();
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
        if(convertView==null){
            convertView= LayoutInflater.from(ctx).inflate(R.layout.adapter_his_item,null);

        }


        LineChart mChart= ButterKnife.findById(convertView,R.id.lineChart);

        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

            }

            @Override
            public void onNothingSelected() {
            }
        });

        // no description text
        mChart.getDescription().setEnabled(false);

        // enable touch gestures
        mChart.setTouchEnabled(false);

        // enable scaling and dragging
        mChart.setDragEnabled(false);
        mChart.setScaleEnabled(false);
        mChart.setScaleXEnabled(true);
        mChart.setScaleYEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);
        setData(position,mChart);

        Legend l = mChart.getLegend();

        l.setForm(Legend.LegendForm.LINE);

        l.setTextSize(11f);
        l.setTextColor(ColorTemplate.getHoloBlue());
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setTextSize(11f);
        xAxis.setTextColor(ColorTemplate.getHoloBlue());
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if(value<10){
                    return String.format("0%.0f:00",value);
                }else{
                    return String.format("%.0f:00",value);
                }

            }
        });





        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawAxisLine(true);





        mChart.setDrawGridBackground(true);



        return convertView;
    }



    private void setData(int position,LineChart mChart) {

        List<CollWantData> items=values.get(position);
        String title= ConstantUtils.CONTENTS.get(keys.get(position));
        if(title==null){
            title="";
        }

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        for(CollWantData data:items){
            yVals1.add(new Entry(data.getReviceTime(), data.getValue()));
        }



        LineDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);

            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(yVals1, title);

            set1.setAxisDependency(YAxis.AxisDependency.LEFT);

            mChart.setDrawGridBackground(false);

            set1.setFillColor(ColorTemplate.getHoloBlue());
            set1.setHighLightColor(Color.rgb(244, 117, 117));
            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setDrawCircles(false);




            // create a data object with the datasets
            LineData data = new LineData(set1);
            data.setValueTextColor(ColorTemplate.getHoloBlue());
            data.setValueTextSize(9f);


            // set data
            mChart.setData(data);
        }
    }
}
