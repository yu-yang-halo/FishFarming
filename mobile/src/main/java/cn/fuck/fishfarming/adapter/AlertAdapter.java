package cn.fuck.fishfarming.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.fuck.fishfarming.R;

/**
 * Created by Administrator on 2017/3/19 0019.
 */

public class AlertAdapter extends BaseAdapter {
    List<String> messages;
    Context context;
    public AlertAdapter(Context context, List<String> messages){
        this.context=context;
        this.messages=messages;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    @Override
    public int getCount() {
        if(messages!=null){
            return messages.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(messages!=null){
            return messages.get(position);
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
            convertView=LayoutInflater.from(context).inflate(R.layout.adapter_alert_message,null);
        }

        TextView messageView= (TextView) convertView.findViewById(R.id.textView17);
        messageView.setText(messages.get(position));

        return convertView;
    }
}
