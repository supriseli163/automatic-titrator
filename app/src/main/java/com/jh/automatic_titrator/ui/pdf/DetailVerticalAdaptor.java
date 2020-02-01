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

public class DetailVerticalAdaptor extends BaseAdapter {

    private Context context;

    private List<Test> tests;

    public DetailVerticalAdaptor(Context context, List<Test> tests) {
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
        View view = LayoutInflater.from(context).inflate(R.layout.export_detail_vertical_item, null);

        Test test = tests.get(position);

        ((TextView) view.findViewById(R.id.export_detail_sort_number)).setText(test.getId() + "");
        ((TextView) view.findViewById(R.id.export_detail_sample_no)).setText(test.getTestId());
        ((TextView) view.findViewById(R.id.export_detail_name)).setText(test.getTestName());
        ((TextView) view.findViewById(R.id.export_detail_date)).setText(TimeTool.dateToDate(test.getDate()));
        ((TextView) view.findViewById(R.id.export_detail_time)).setText(TimeTool.dateToTime(test.getDate()));
        ((TextView) view.findViewById(R.id.export_detail_resType)).setText(test.getSimpleFormulaName());
        ((TextView) view.findViewById(R.id.export_detail_res)).setText(String.format("%." + test.getDecimal() + "f%s", test.getRes(), test.getFormulaUnit()));
        ((TextView) view.findViewById(R.id.export_detail_temperature_setting)).setText(test.getWantedTemperature());
        ((TextView) view.findViewById(R.id.export_detail_temperature)).setText(test.getRealTemperature());
        ((TextView) view.findViewById(R.id.export_detail_wavelength)).setText(test.getWavelength());
        if (test.getTubelength() != null) {
            ((TextView) view.findViewById(R.id.export_detail_testtubelength)).setText(test.getWavelength() + "cm");
        }
        ((TextView) view.findViewById(R.id.export_detail_automatic_titrator)).setText(String.format("%." + test.getDecimal() + "f", test.getOptical()));
        ((TextView) view.findViewById(R.id.export_detail_sugger)).setText(String.format("%." + test.getDecimal() + "f", test.getOptical() * 2.888));
        if (test.getSpecificrotation() != null) {
            ((TextView) view.findViewById(R.id.export_detail_specific_rotation)).setText(test.getSpecificrotation() + "");
        }
        if (test.getConcentration() != null) {
            ((TextView) view.findViewById(R.id.export_detail_concentration)).setText(test.getConcentration());
        }
        ((TextView) view.findViewById(R.id.export_detail_test_method)).setText(test.getTestMethod());
        ((TextView) view.findViewById(R.id.export_detail_creator)).setText(test.getTestCreator());

        return view;
    }
}