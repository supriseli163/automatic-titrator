package com.jh.automatic_titrator.ui.setting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.entity.common.StandardData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by apple on 16/9/24.
 */
public class SettingCorrectingAdapter extends BaseAdapter {

    private List<StandardData> standardDatas;

    private Set<Integer> checkedPositions;

    private Context context;

    private String[] temperatureType;


    public SettingCorrectingAdapter(List<StandardData> standardDatas, Context context, String[] temperatureType) {
        this.standardDatas = standardDatas;
        this.context = context;
        this.temperatureType = temperatureType;
        checkedPositions = new HashSet<>();
    }

    public List<StandardData> getCheckedItems() {
        List<StandardData> checkItems = new ArrayList<>();
        for (Integer position : checkedPositions) {
            checkItems.add(standardDatas.get(position));
        }
        return checkItems;
    }

    public Set<Integer> getCheckedPositions() {
        return checkedPositions;
    }

    public void resetCheckedPositions() {
        checkedPositions.clear();
    }

    @Override
    public int getCount() {
        return standardDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return standardDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return standardDatas.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.setting_correcting_item, null);

        StandardData standardData = standardDatas.get(position);

        CheckBox checkBox = (CheckBox) view.findViewById(R.id.setting_correcting_item_cb);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkedPositions.add(position);
                } else {
                    checkedPositions.remove(position);
                }
            }
        });

        TextView testValue = (TextView) view.findViewById(R.id.setting_correcting_item_test_value);
        if (standardData.getTestValue() != 0) {
            testValue.setText(String.format("%.4f", standardData.getTestValue()));
        }

        TextView standardValue = (TextView) view.findViewById(R.id.setting_correcting_item_standard_value);
        if (standardData.getStandardValue() != 0) {
            standardValue.setText(String.format("%.4f", standardData.getStandardValue()));
        }


        TextView tempValue = (TextView) view.findViewById(R.id.setting_correcting_item_temp);
        if (standardData.getTemperature() != 0) {
            tempValue.setText(String.format("%.1f°C", standardData.getTemperature()));
        }

        return view;
    }
}
