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
import com.jh.automatic_titrator.entity.common.TestMethod;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by apple on 16/9/24.
 */
public class SettingMethodAdapter extends BaseAdapter {

    private List<TestMethod> testMethods;

    private Set<Integer> checkedPositions;

    private boolean checkedPage = false;

    private boolean checkedAll = false;

    private Context context;

    private int page;

    private String[] temperatureType;

    public SettingMethodAdapter(Context context, List<TestMethod> testMethods, boolean allChecked, boolean pageChecked, int page, String[] temperatureType) {
        this.context = context;
        this.testMethods = testMethods;
        this.checkedPositions = new HashSet<>();
        this.checkedAll = allChecked;
        this.checkedPage = pageChecked;
        this.page = page;
        this.temperatureType = temperatureType;
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

    public List<TestMethod> getCheckedItems() {
        List<TestMethod> checkItems = new ArrayList<>();
        for (Integer position : checkedPositions) {
            checkItems.add(testMethods.get(position));
        }
        return checkItems;
    }

    @Override
    public int getCount() {
        return testMethods.size();
    }

    @Override
    public Object getItem(int position) {
        return testMethods.get(position);
    }

    @Override
    public long getItemId(int position) {
        return testMethods.get(position).getId();
    }

    @Override
    public void notifyDataSetChanged() {
        checkedPositions.clear();
        super.notifyDataSetChanged();
    }

    public interface ItemCheckedListener {
        public void onItemChecked(boolean checked, int position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.setting_method_fragment_item, null);

        TestMethod testMethod = testMethods.get(position);

        CheckBox checkBox = (CheckBox) view.findViewById(R.id.setting_method_item_cb);
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

        TextView sort = (TextView) view.findViewById(R.id.setting_method_item_sort);
        sort.setText(testMethod.getId() + "");

        TextView testName = (TextView) view.findViewById(R.id.setting_method_item_testname);
        testName.setText(testMethod.getTestName());


        TextView resultType = (TextView) view.findViewById(R.id.setting_method_item_resulttype);
        resultType.setText(testMethod.getFormulaName() + "");

        TextView temperature = (TextView) view.findViewById(R.id.setting_method_item_temperature);
        if (testMethod.isUseTemperature()) {
            temperature.setText(testMethod.getTemperature() + " " + temperatureType[testMethod.getTemperatureType()]);
        } else {
            temperature.setText("-  -");
        }

        TextView autotest = (TextView) view.findViewById(R.id.setting_method_item_autotest);
        String autoTestStr = null;
        if (testMethod.isAutoTest()) {
            autoTestStr = context.getString(R.string.yes);
        } else {
            autoTestStr = context.getString(R.string.no);
        }
        autotest.setText(autoTestStr);

        TextView time = (TextView) view.findViewById(R.id.setting_method_item_createtime);
        time.setText(testMethod.getCreateDate());

        TextView creator = (TextView) view.findViewById(R.id.setting_method_item_creator);
        creator.setText(testMethod.getCreator());

        return view;
    }
}
