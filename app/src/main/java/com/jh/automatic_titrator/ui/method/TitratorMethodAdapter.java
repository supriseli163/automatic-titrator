package com.jh.automatic_titrator.ui.method;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.jh.automatic_titrator.entity.common.titrator.TitratorMethod;

import java.util.List;
import java.util.Set;

public class TitratorMethodAdapter extends BaseAdapter {

    private List<TitratorMethod> titratorMethods;
    private Set<Integer> checkedPositions;
    private boolean checkedPage = false;
    private boolean checkedAll = false;
    private Context context;
    private int page;

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
