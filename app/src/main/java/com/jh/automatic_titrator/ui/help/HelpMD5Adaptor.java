package com.jh.automatic_titrator.ui.help;

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
import com.jh.automatic_titrator.entity.common.MD5;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by apple on 16/9/24.
 */
public class HelpMD5Adaptor extends BaseAdapter {

    private Context context;

    private Set<Integer> checkedPositions;

    private boolean checkedPage = false;

    private boolean checkedAll = false;

    private List<MD5> md5List;

    public HelpMD5Adaptor(Context context, List<MD5> md5List, boolean allChecked, boolean pageChecked) {
        this.context = context;
        this.md5List = md5List;
        this.checkedAll = allChecked;
        this.checkedPage = pageChecked;
        checkedPositions = new LinkedHashSet<>();
    }

    public void setCheckedPage(boolean checkedPage) {
        this.checkedPage = checkedPage;
    }

    public void setCheckedAll(boolean checkedAll) {
        this.checkedAll = checkedAll;
    }

    public List<MD5> getCheckedItems() {
        List<MD5> checkItems = new ArrayList<>();
        for (Integer position : checkedPositions) {
            checkItems.add(md5List.get(position));
        }
        return checkItems;
    }

    @Override
    public int getCount() {
        return md5List.size();
    }

    @Override
    public Object getItem(int i) {
        return md5List.get(i);
    }

    @Override
    public long getItemId(int i) {
        return md5List.get(i).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.help_md5_item, null);

        MD5 md5Obj = md5List.get(position);

        CheckBox checkBox = (CheckBox) view.findViewById(R.id.help_md5_item_cb);
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

        TextView sortId = (TextView) view.findViewById(R.id.help_md5_item_no);
        sortId.setText((md5Obj.getId()) + "");

        TextView date = (TextView) view.findViewById(R.id.help_md5_item_createdate);
        date.setText(TimeTool.dateToDate(md5Obj.getCreateDate()));

        TextView time = (TextView) view.findViewById(R.id.help_md5_item_createtime);
        time.setText(TimeTool.dateToTime(md5Obj.getCreateDate()));

        TextView fileName = (TextView) view.findViewById(R.id.help_md5_item_filename);
        fileName.setText(md5Obj.getFileName());

        TextView fileSize = (TextView) view.findViewById(R.id.help_md5_item_filesize);
        long fileSizeValue = md5Obj.getFileSize();
        if (fileSizeValue > 1024 * 1024) {
            fileSize.setText(String.format("%.2fMB", md5Obj.getFileSize() * 1.0 / 1024 / 1024));
        } else if (fileSizeValue > 1024) {
            fileSize.setText(String.format("%.2fKB", md5Obj.getFileSize() * 1.0 / 1024));
        } else {
            fileSize.setText(md5Obj.getFileSize() + "B");
        }

        TextView md5 = (TextView) view.findViewById(R.id.help_md5_item_md5);
        md5.setText(md5Obj.getMd5());

        return view;
    }
}
