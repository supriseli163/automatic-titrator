package com.jh.automatic_titrator.ui.execute.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.entity.common.titrator.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MenuAdapter extends BaseAdapter {
    private List<MenuItem> mList;

    public static List<MenuItem> getTestData() {
        List<MenuItem> items = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            MenuItem item = new MenuItem();
            item.setContent("" + i + 1);
            items.add(item);
        }
        return items;
    }

    public MenuAdapter(List<MenuItem> itemList) {
        this.mList = itemList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        View itemView = mInflater.inflate(R.layout.execute_circle_menu_item, parent, false);
        initMenuItem(itemView, position);
        return itemView;
    }

    // 初始化菜单项
    private void initMenuItem(View itemView, int position) {
        // 获取数据项
        final MenuItem item = (MenuItem) getItem(position);
        TextView iv = (TextView) itemView
                .findViewById(R.id.iv_icon);
        // 数据绑定
        iv.setText("" + (position + 1));
    }
}