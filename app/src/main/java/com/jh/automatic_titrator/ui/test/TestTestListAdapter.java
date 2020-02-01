package com.jh.automatic_titrator.ui.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.common.Cache;
import com.jh.automatic_titrator.entity.common.SingleResult;

import java.util.List;

/**
 * Created by apple on 16/10/10.
 */
public class TestTestListAdapter extends BaseAdapter {

    private List<SingleResult> singleResults;

    private Context context;

    private List<Integer> checkedPositions;

    public TestTestListAdapter(List<SingleResult> singleResults, Context context) {
        this.singleResults = singleResults;
        this.context = context;
    }

    public void setSingleResults(List<SingleResult> singleResults) {
        this.singleResults = singleResults;
    }

    @Override
    public int getCount() {
        return singleResults.size();
    }

    @Override
    public Object getItem(int position) {
        return singleResults.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = (View) LayoutInflater.from(context).inflate(R.layout.test_test_res_item, null);

        SingleResult singleResult = singleResults.get(position);
        TextView no = (TextView) view.findViewById(R.id.test_test_res_no);
//        no.setText(singleResult.getDate());
        no.setText(position + 1 + "");

        TextView temperature = (TextView) view.findViewById(R.id.test_test_res_temperature);
        temperature.setText(singleResult.getRealTemperature());

        TextView value = (TextView) view.findViewById(R.id.test_test_res_value);
        value.setText(String.format("%." + Cache.getTestMethod().getDecimals() + "f", singleResult.getRes()) + singleResult.getResUnit());
        return view;
    }
}
