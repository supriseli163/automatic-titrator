package com.jh.automatic_titrator.ui.setting;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.common.utils.TimeTool;

/**
 * Created by apple on 2016/10/28.
 */
public class AuditSearch extends PopupWindow implements View.OnClickListener {

    public static final int PRINTVERTICAL = 1;
    public static final int PRINTHORIZONTAL = 2;

    private OnSearchListener onSearchListener;

    private DatePicker startDate;

    private DatePicker endDate;

    private EditText operatorText;

    private View btn;

    public AuditSearch(Activity activity, int width, int height, boolean focusable) {
        this(LayoutInflater.from(activity).inflate(
                R.layout.audit_search_popup, null), width, height, focusable);
    }

    public AuditSearch(View contentView, int width, int height, boolean focusable) {
        super(contentView, width, height, focusable);
        startDate = (DatePicker) contentView.findViewById(R.id.audit_search_start);
        endDate = (DatePicker) contentView.findViewById(R.id.audit_search_end);
        operatorText = (EditText) contentView.findViewById(R.id.audit_search_operator);
        btn = contentView.findViewById(R.id.audit_search_search_btn);
        btn.setOnClickListener(this);
    }

    public void setOnSearchListener(OnSearchListener onSearchListener) {
        this.onSearchListener = onSearchListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.data_search_search_btn:
                if(onSearchListener != null) {
                    String startDateStr = TimeTool.getDate(startDate, true);
                    String endDateStr = TimeTool.getDate(endDate, false);
                    String operator = operatorText.getText().toString();
                    onSearchListener.onSearch(startDateStr, endDateStr, operator);
                }
                break;
        }
    }

    public interface OnSearchListener {
        void onSearch(String startDate, String endDate, String operator);
    }
}
