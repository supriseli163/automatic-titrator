package com.jh.automatic_titrator.ui.pdf;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.common.utils.TimeTool;
import com.jh.automatic_titrator.entity.common.Test;

import java.util.List;

/**
 * Created by apple on 2017/2/19.
 */

public class SimpleVerticalAdaptor extends BaseAdapter {

    private Context context;

    private List<Test> tests;

    public SimpleVerticalAdaptor(Context context, List<Test> tests) {
        this.context = context;
        this.tests = tests;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.export_simple_vertical_item, null);

        Test test = tests.get(position);

        TextView sortId = (TextView) view.findViewById(R.id.export_svi_sort);
        sortId.setText(test.getId() + "");

        TextView testname = (TextView) view.findViewById(R.id.export_svi_testname);
        testname.setText(test.getTestName());

        TextView testid = (TextView) view.findViewById(R.id.export_svi_testid);
        testid.setText(test.getTestId());

        TextView createdate = (TextView) view.findViewById(R.id.export_svi_createdate);
        createdate.setText(TimeTool.dateToDate(test.getDate()));

        TextView createtime = (TextView) view.findViewById(R.id.export_svi_createtime);
        createtime.setText(TimeTool.dateToTime(test.getDate()));

        TextView temperature = (TextView) view.findViewById(R.id.export_svi_temperature);
        temperature.setText(test.getRealTemperature());

        TextView resulttype = (TextView) view.findViewById(R.id.export_svi_resulttype);
        resulttype.setText(test.getFormulaName());

        TextView result = (TextView) view.findViewById(R.id.export_svi_result);
        result.setText(test.getRes() + test.getFormulaUnit());

        TextView creator = (TextView) view.findViewById(R.id.export_svi_creator);
        creator.setText(test.getTestCreator());
        return view;
    }
}