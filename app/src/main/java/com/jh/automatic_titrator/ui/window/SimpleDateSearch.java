package com.jh.automatic_titrator.ui.window;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.common.utils.TimeTool;
import com.jh.automatic_titrator.ui.listener.KeyboardDismiss;

/**
 * Created by apple on 2016/10/28.
 */
public class SimpleDateSearch implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

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

    public SimpleDateSearch(Activity activity) {
        this.mActivity = activity;
        mView = LayoutInflater.from(mActivity).inflate(R.layout.simple_date_search, null);
        startDate = (DatePicker) mView.findViewById(R.id.simple_date_search_start);
        endDate = (DatePicker) mView.findViewById(R.id.simple_date_search_end);
        creator = (EditText) mView.findViewById(R.id.simple_date_search_creator);
        startLayout = mView.findViewById(R.id.simple_date_search_start_layout);
        endLayout = mView.findViewById(R.id.simple_date_search_end_layout);

        useStartTime = (CheckBox) mView.findViewById(R.id.simple_date_search_use_starttime);
        useEndTime = (CheckBox) mView.findViewById(R.id.simple_date_search_use_endtime);
        useStartTime.setOnCheckedChangeListener(this);
        useEndTime.setOnCheckedChangeListener(this);

        btn = mView.findViewById(R.id.simple_date_search_search_btn);
        btn.setOnClickListener(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle(mActivity.getString(R.string.search));
        builder.setView(mView);
        mDialog = builder.create();
        mDialog.setOnDismissListener(new KeyboardDismiss(mActivity));
    }

    public SimpleDateSearch(Activity activity, String txName) {
        this(activity);
        TextView textView = (TextView) mView.findViewById(R.id.simple_date_search_creator_tx);
        textView.setText(txName);
    }

    public void setOnSearchListener(OnSearchListener onSearchListener) {
        this.onSearchListener = onSearchListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.simple_date_search_search_btn:
                if (onSearchListener != null) {
                    String startDateStr = null;
                    if (useStartTime.isChecked()) {
                        startDateStr = TimeTool.getDate(startDate, true);
                    }
                    String endDateStr = null;
                    if (useEndTime.isChecked()) {
                        endDateStr = TimeTool.getDate(endDate, false);
                    }
                    String creatorStr = creator.getText().toString();
                    onSearchListener.onSearch(startDateStr, endDateStr, creatorStr);
                }
                break;
        }
    }

    public void show() {
        mDialog.show();
    }

    public void hide() {
        mDialog.hide();
    }

    public void dismiss() {
        mDialog.dismiss();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.simple_date_search_use_starttime:
                if (isChecked) {
                    startLayout.setVisibility(View.VISIBLE);
                } else {
                    startLayout.setVisibility(View.GONE);
                }
                break;
            case R.id.simple_date_search_use_endtime:
                if (isChecked) {
                    endLayout.setVisibility(View.VISIBLE);
                } else {
                    endLayout.setVisibility(View.GONE);
                }
                break;
        }
    }

    public interface OnSearchListener {
        void onSearch(String startDate, String endDate, String creator);
    }
}
