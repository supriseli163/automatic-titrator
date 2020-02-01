package com.jh.automatic_titrator.ui.window;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.PopupWindow;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.common.utils.TimeTool;

/**
 * Created by apple on 2016/10/28.
 */
public class DateWindow extends PopupWindow implements View.OnClickListener {

    private DatePicker datePicker;

    private View submit;

    private boolean isStartDate;

    private DateChangeListener dateChangeListener;

    public DateWindow(Activity activity, int width, int height, boolean focusable, boolean isStartDate) {
        this(LayoutInflater.from(activity).inflate(
                R.layout.date, null), width, height, focusable, isStartDate);
    }

    public DateWindow(View contentView, int width, int height, boolean focusable, boolean isStartDate) {
        super(contentView, width, height, focusable);
        this.isStartDate = isStartDate;
        datePicker = (DatePicker) contentView.findViewById(R.id.datePicker);
        submit = contentView.findViewById(R.id.date_picker_submit);
        submit.setOnClickListener(this);
    }

    private void dateChanged() {
        String date = TimeTool.getDate(datePicker, isStartDate);
        if (dateChangeListener != null) {
            dateChangeListener.onChange(date);
        }
    }

    public void setDateChangeListener(DateChangeListener dateChangeListener) {
        this.dateChangeListener = dateChangeListener;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.date_picker_submit:
                dateChanged();
        }
    }

    public interface DateChangeListener {
        public void onChange(String date);
    }
}
