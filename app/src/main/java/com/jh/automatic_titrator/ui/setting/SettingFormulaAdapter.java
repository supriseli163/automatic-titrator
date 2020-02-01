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
import com.jh.automatic_titrator.common.utils.TimeTool;
import com.jh.automatic_titrator.entity.common.Formula;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by apple on 16/9/24.
 */
public class SettingFormulaAdapter extends BaseAdapter {

    private List<Formula> formulas;

    private Set<Integer> checkedPositions;

    private boolean checkedPage = false;

    private boolean checkedAll = false;

    private Context context;

    public SettingFormulaAdapter(Context context, List<Formula> formulas, boolean allChecked, boolean pageChecked) {
        this.context = context;
        this.formulas = formulas;
        this.checkedPositions = new HashSet<>();
        this.checkedAll = allChecked;
        this.checkedPage = pageChecked;
    }

    public boolean isCheckedAll() {
        return checkedAll;
    }

    public void setCheckedAll(boolean checkedAll) {
        this.checkedAll = checkedAll;
    }

    public boolean isCheckedPage() {
        return checkedPage;
    }

    public void setCheckedPage(boolean checkedPage) {
        this.checkedPage = checkedPage;
    }

    public List<Formula> getCheckedItems() {
        List<Formula> checkItems = new ArrayList<>();
        for (Integer position : checkedPositions) {
            checkItems.add(formulas.get(position));
        }
        return checkItems;
    }

    @Override
    public int getCount() {
        return formulas.size();
    }

    @Override
    public Object getItem(int position) {
        return formulas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return formulas.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.setting_formula_item, null);

        Formula formula = formulas.get(position);

        CheckBox checkBox = (CheckBox) view.findViewById(R.id.setting_formula_item_cb);
        if (checkedPage || checkedAll && formula.getId() != 1) {
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

        TextView sort = (TextView) view.findViewById(R.id.setting_formula_item_sort_number);
        sort.setText(formula.getId() + "");

        TextView nameText = (TextView) view.findViewById(R.id.setting_formula_item_name);
        nameText.setText(formula.getFormulaName());

        TextView simpleNameText = (TextView) view.findViewById(R.id.setting_formula_item_simplename);
        simpleNameText.setText(formula.getSimpleName());

        TextView unitText = (TextView) view.findViewById(R.id.setting_formula_item_unit);
        unitText.setText(formula.getUnit());

        TextView decimalText = (TextView) view.findViewById(R.id.setting_formula_item_decimal);
        decimalText.setText(formula.getDecimal() + "");

        TextView showPercentText = (TextView) view.findViewById(R.id.setting_formula_item_showpercent);
        showPercentText.setText(formula.isShowPercent() ? "%" : "");

        TextView dateText = (TextView) view.findViewById(R.id.setting_formula_item_date);
        dateText.setText(TimeTool.dateToDate(formula.getCreateDate()));

        TextView timeText = (TextView) view.findViewById(R.id.setting_formula_item_time);
        timeText.setText(TimeTool.dateToTime(formula.getCreateDate()));

        TextView descText = (TextView) view.findViewById(R.id.setting_formula_item_desc);
        descText.setText(formula.getDesc());

        return view;
    }
}
