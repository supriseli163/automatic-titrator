package com.jh.automatic_titrator.ui.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.common.utils.TimeTool;
import com.jh.automatic_titrator.entity.common.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by apple on 16/10/10.
 */
public class DataListAdapter extends BaseAdapter {

    private List<Test> tests;

    private Context context;

    private Set<Integer> checkedPositions;

    private boolean checkedPage = false;

    private boolean checkedAll = false;

    public DataListAdapter(List<Test> tests, Context context) {
        this.tests = tests;
        this.context = context;
        checkedPositions = new LinkedHashSet<>();
    }

    public DataListAdapter(List<Test> tests, Context context, boolean allChecked) {
        this.tests = tests;
        this.context = context;
        checkedPositions = new HashSet<>();
    }

    @Override
    public void notifyDataSetChanged() {
        checkedPositions.clear();
        super.notifyDataSetChanged();
    }

    public List<Test> getCheckedList() {
        List<Test> checkedTest = new ArrayList<>();
        for (int position : checkedPositions) {
            checkedTest.add(tests.get(position));
        }
        return checkedTest;
    }

    public boolean isCheckedPage() {
        return checkedPage;
    }

    public void setCheckedPage(boolean checkedPage) {
        this.checkedPage = checkedPage;
    }

    public boolean isCheckedAll() {
        return checkedAll;
    }

    public void setCheckedAll(boolean checkedAll) {
        this.checkedAll = checkedAll;
    }

    @Override
    public int getCount() {
        return tests.size();
    }

    @Override
    public Object getItem(int position) {
        return tests.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.data_item, null);

        final Test test = tests.get(position);
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.data_item_cb);
        if (checkedPage || checkedAll) {
            checkedPositions.add(position);
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }
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

        TextView sortId = (TextView) view.findViewById(R.id.data_item_sort);
        sortId.setText(test.getId() + "");

        TextView testname = (TextView) view.findViewById(R.id.data_item_testname);
        testname.setText(test.getTestName());

        TextView testid = (TextView) view.findViewById(R.id.data_item_testid);
        testid.setText(test.getTestId());

        TextView createdate = (TextView) view.findViewById(R.id.data_item_createdate);
        createdate.setText(TimeTool.dateToDate(test.getDate()));

        TextView createtime = (TextView) view.findViewById(R.id.data_item_createtime);
        createtime.setText(TimeTool.dateToTime(test.getDate()));

        TextView temperature = (TextView) view.findViewById(R.id.data_item_temperature);
        temperature.setText(test.getRealTemperature());

        TextView resulttype = (TextView) view.findViewById(R.id.data_item_resulttype);
        resulttype.setText(test.getFormulaName());

        TextView result = (TextView) view.findViewById(R.id.data_item_result);
        result.setText(String.format("%." + test.getDecimal() + "f%s", test.getRes(), test.getFormulaUnit()));

        TextView creator = (TextView) view.findViewById(R.id.data_item_creator);
        creator.setText(test.getTestCreator());

        return view;
    }
}
