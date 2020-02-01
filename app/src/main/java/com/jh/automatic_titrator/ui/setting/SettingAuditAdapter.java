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
import com.jh.automatic_titrator.entity.common.Audit;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by apple on 16/9/24.
 */
public class SettingAuditAdapter extends BaseAdapter {

    private List<Audit> audits;

    private Set<Integer> checkedPositions;

    private boolean checkedPage = false;

    private boolean checkedAll = false;

    private Context context;

    private int page;

    public SettingAuditAdapter(Context context, List<Audit> audits, boolean allChecked, boolean pageChecked, int page) {
        this.context = context;
        this.audits = audits;
        this.checkedPositions = new LinkedHashSet<>();
        this.checkedAll = allChecked;
        this.checkedPage = pageChecked;
        this.page = page;
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

    public void clearChecked() {
        checkedPositions.clear();
    }

    public List<Audit> getCheckedItems() {
        List<Audit> checkItems = new ArrayList<>();
        for (Integer position : checkedPositions) {
            checkItems.add(audits.get(position));
        }
        return checkItems;
    }

    @Override
    public int getCount() {
        return audits.size();
    }

    @Override
    public Object getItem(int position) {
        return audits.get(position);
    }

    @Override
    public long getItemId(int position) {
        return audits.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.setting_audit_item, null);

        Audit audit = audits.get(position);

        CheckBox checkBox = (CheckBox) view.findViewById(R.id.setting_audit_item_cb);
        if (checkedPage || checkedAll) {
            checkedPositions.add(position);
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    checkedPositions.add(position);
                } else {
                    checkedPositions.remove(position);
                }
            }
        });

        TextView sort = (TextView) view.findViewById(R.id.setting_audit_item_sort_number);
        sort.setText(audit.getId() + "");

        TextView operatorText = (TextView) view.findViewById(R.id.setting_audit_item_opereator);
        operatorText.setText(audit.getOperator());


        TextView dateText = (TextView) view.findViewById(R.id.setting_audit_item_date);
        dateText.setText(TimeTool.dateToDate(audit.getDate()));

        TextView timeText = (TextView) view.findViewById(R.id.setting_audit_item_time);
        timeText.setText(TimeTool.dateToTime(audit.getDate()));

        TextView fragmentText = (TextView) view.findViewById(R.id.setting_audit_item_fragment);
        fragmentText.setText(audit.getFragment());

        TextView subfragmentText = (TextView) view.findViewById(R.id.setting_audit_item_subfragment);
        if(audit.getSubFragment() != null && !audit.getSubFragment().equals("null")) {
            subfragmentText.setText(audit.getSubFragment());
        }

        TextView eventText = (TextView) view.findViewById(R.id.setting_audit_item_event);
        eventText.setText(audit.getEvent());

        return view;
    }
}
