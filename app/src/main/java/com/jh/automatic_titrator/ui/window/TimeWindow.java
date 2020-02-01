package com.jh.automatic_titrator.ui.window;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TimePicker;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.common.utils.TimeTool;

/**
 * Created by apple on 2016/10/28.
 */
public class TimeWindow extends PopupWindow implements View.OnClickListener{
    private TimePicker timePicker;

    private View submit;

    private boolean isStartDate;

    private TimeChangeListener timeChangeListener;

    public TimeWindow(Activity activity, int width, int height, boolean focusable) {
        this(LayoutInflater.from(activity).inflate(
                R.layout.time, null), width, height, focusable);
    }

    public TimeWindow(View contentView, int width, int height, boolean focusable) {
        super(contentView, width, height, focusable);
        timePicker = (TimePicker) contentView.findViewById(R.id.timePicker);
        submit = contentView.findViewById(R.id.time_picker_submit);
        submit.setOnClickListener(this);
    }

    public void setTimeChangeListener(TimeChangeListener timeChangeListener) {
        this.timeChangeListener = timeChangeListener;
    }

    private void timeChanged() {
        String time = TimeTool.getTime(timePicker, isStartDate);
        if (timeChangeListener != null) {
            timeChangeListener.onChange(time);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.time_picker_submit:
                timeChanged();
        }
    }

    public interface TimeChangeListener {
        public void onChange(String time);
    }
}
