package cn.fuck.fishfarming.activity.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.fuck.fishfarming.R;
import cn.fuck.fishfarming.activity.StatusBarActivity;
import cn.fuck.fishfarming.cache.ContentBox;
import cn.fuck.fishfarming.utils.ConstantUtils;

public class ServerListUi extends StatusBarActivity {
    @BindView(R.id.listView) ListView listView;
    private MyAdapter myAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_server_list);
        ButterKnife.bind(this);
        myAdapter = new MyAdapter(this,ConstantUtils.SERVERS);

        listView.setAdapter(myAdapter);

        initCustomActionBar();
        tvTitle.setText("选择服务中心");
    }



    public static class MyAdapter extends BaseAdapter{

        private List<ConstantUtils.ServerItem> itemList;
        private Context ctx;
        public MyAdapter(Context ctx, List<ConstantUtils.ServerItem> itemList)
        {
            this.itemList = itemList;
            this.ctx = ctx;
        }

        @Override
        public int getCount() {
            if(itemList == null)
            {
                return 0;
            }else
            {
                return itemList.size();
            }
        }

        @Override
        public Object getItem(int position) {
            if(itemList == null)
            {
                return null;
            }else
            {
                return itemList.get(position);
            }

        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            MyHolder myHolder;
            if(convertView == null)
            {
                convertView = LayoutInflater.from(ctx).inflate(R.layout.adapter_server_list_item,null);

                myHolder = new MyHolder();
                myHolder.tvName = (TextView) convertView.findViewById(R.id.tv_serverName);
                myHolder.radioButton = (RadioButton) convertView.findViewById(R.id.radioBtn);
                convertView.setTag(myHolder);
            }
            myHolder = (MyHolder) convertView.getTag();

            ConstantUtils.ServerItem item = itemList.get(position);

            myHolder.tvName.setText(item.itemName);

            String serverAddr = ContentBox.getValueString(ctx,ContentBox.KEY_SERVER,ContentBox.DEFAULT_SERVER);

            if(serverAddr.equals(item.itemServer))
            {
                myHolder.radioButton.setChecked(true);
            }else
            {
                myHolder.radioButton.setChecked(false);
            }

            myHolder.radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ConstantUtils.ServerItem item = itemList.get(position);
                    ContentBox.loadString(ctx,ContentBox.KEY_SERVER,item.itemServer);
                    notifyDataSetChanged();
                }
            });

            myHolder.tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ConstantUtils.ServerItem item = itemList.get(position);
                    ContentBox.loadString(ctx,ContentBox.KEY_SERVER,item.itemServer);
                    notifyDataSetChanged();
                }
            });


            return convertView;
        }

        public List<ConstantUtils.ServerItem> getItemList() {
            return itemList;
        }

        public void setItemList(List<ConstantUtils.ServerItem> itemList) {
            this.itemList = itemList;
        }

        public class MyHolder
        {
            public TextView tvName;
            public RadioButton radioButton;
        }
    }
}
