package com.jh.automatic_titrator.ui.data;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.common.utils.StringUtils;
import com.jh.automatic_titrator.common.utils.TimeTool;
import com.jh.automatic_titrator.ui.listener.KeyboardDismiss;

/**
 * Created by apple on 2016/10/28.
 */
public class DataSearch implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private Activity mActivity;

    private OnSearchListener onSearchListener;

    private DatePicker startDate;

    private DatePicker endDate;

    private EditText creator;

    private View btn;

    private Dialog mDialog;

    private View mView;

    private CheckBox useStartTime;

    private CheckBox useEndTime;

    private View startLayout;
    private View endLayout;

    private EditText sampleName;

    private EditText gt;

    private EditText lt;

    public DataSearch(Activity activity) {
        this.mActivity = activity;
        mView = LayoutInflater.from(mActivity).inflate(R.layout.data_search_popup, null);
        startDate = (DatePicker) mView.findViewById(R.id.data_search_start);
        endDate = (DatePicker) mView.findViewById(R.id.data_search_end);
        creator = (EditText) mView.findViewById(R.id.data_search_creator);
        startLayout = mView.findViewById(R.id.data_search_start_layout);
        endLayout = mView.findViewById(R.id.data_search_end_layout);
        gt = (EditText) mView.findViewById(R.id.data_search_scale_gt);
        lt = (EditText) mView.findViewById(R.id.data_search_scale_lt);
        sampleName = (EditText) mView.findViewById(R.id.data_search_sampleName);

        useStartTime = (CheckBox) mView.findViewById(R.id.data_search_use_starttime);
        useEndTime = (CheckBox) mView.findViewById(R.id.data_search_use_endtime);
        useStartTime.setOnCheckedChangeListener(this);
        useEndTime.setOnCheckedChangeListener(this);

        btn = mView.findViewById(R.id.data_search_search_btn);
        btn.setOnClickListener(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle(mActivity.getString(R.string.search));
        builder.setView(mView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            builder.setOnDismissListener(new KeyboardDismiss(mActivity));
        }
        mDialog = builder.create();
    }


    public void setOnSearchListener(OnSearchListener onSearchListener) {
        this.onSearchListener = onSearchListener;
    }

    public void show() {
        mDialog.show();
    }

    public void dismiss() {
        mDialog.dismiss();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.data_search_use_starttime:
                if (isChecked) {
                    startLayout.setVisibility(View.VISIBLE);
                } else {
                    startLayout.setVisibility(View.GONE);
                }
                break;
            case R.id.data_search_use_endtime:
                if (isChecked) {
                    endLayout.setVisibility(View.VISIBLE);
                } else {
                    endLayout.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.data_search_search_btn:
                if (onSearchListener != null) {
                    String startDateStr = null;
                    if (useStartTime.isChecked()) {
                        startDateStr = TimeTool.getDate(startDate, true);
                    }
                    String endDateStr = null;
                    if (useEndTime.isChecked()) {
                        endDateStr = TimeTool.getDate(endDate, false);
                    }
                    String creatorStr = creator.getText().toString().trim();
                    String sampleNameStr = sampleName.getText().toString().trim();
                    String gtStr = gt.getText().toString();
                    String ltStr = lt.getText().toString();
                    Double gtValue = null;
                    if (StringUtils.isNotEmpty(gtStr)) {
                        gtValue = Double.parseDouble(gtStr);
                    }
                    Double ltValue = null;
                    if (StringUtils.isNotEmpty(ltStr)) {
                        ltValue = Double.parseDouble(ltStr);
                    }
                    onSearchListener.onSearch(startDateStr, endDateStr, sampleNameStr, gtValue, ltValue, creatorStr);
                }
                break;
        }
    }

    public interface OnSearchListener {
        void onSearch(String startDate, String endDate, String sampleName, Double gt, Double lt, String creator);
    }
}
