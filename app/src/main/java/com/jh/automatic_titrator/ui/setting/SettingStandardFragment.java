package com.jh.automatic_titrator.ui.setting;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.common.Cache;
import com.jh.automatic_titrator.common.trunk.TrunkConst;
import com.jh.automatic_titrator.common.trunk.TrunkData;
import com.jh.automatic_titrator.common.trunk.TrunkListener;
import com.jh.automatic_titrator.common.trunk.TrunkUtil;
import com.jh.automatic_titrator.common.utils.TimeTool;
import com.jh.automatic_titrator.common.utils.ToastUtil;
import com.jh.automatic_titrator.service.AuditService;
import com.jh.automatic_titrator.service.DBService;
import com.jh.automatic_titrator.service.ExecutorService;
import com.jh.automatic_titrator.service.TimeService;
import com.jh.automatic_titrator.ui.BaseActivity;
import com.jh.automatic_titrator.ui.HomePageActivity;
import com.jh.automatic_titrator.ui.window.DateWindow;
import com.jh.automatic_titrator.ui.window.TimeWindow;

/**
 * Created by apple on 16/9/24.
 */
public class SettingStandardFragment extends Fragment implements View.OnClickListener {

    private View view;

    private TextView date_btn;

    private TextView time_btn;

    private CheckBox autoClean_cb;

    private Spinner language_spinner;

    private View saveBtn;

    private boolean isSaving = false;

    private DateWindow dateWindow;

    private TimeWindow timeWindow;

    private String currentDate;

    private String currentTime;

    private ProgressDialog progressDialog;

    private SettingStandardHandler handler;

    private TimeService timeService;

    private BaseActivity baseActivity;

    private Toast mToast;

    private boolean dateChanged = false;

    private boolean timeChanged = false;

    private boolean languageChanged = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.setting_standard_fragment, container, false);
        mToast = ToastUtil.createToast(getActivity());
        handler = new SettingStandardHandler();
        language_spinner = (Spinner) view.findViewById(R.id.setting_standard_language);
        String[] languages = this.getActivity().getResources().getStringArray(R.array.language);
        ArrayAdapter<String> languageAdapter = new ArrayAdapter<>(this.getActivity(), R.layout.spinner_checked, languages);
        languageAdapter.setDropDownViewResource(R.layout.spinner_item);
        language_spinner.setAdapter(languageAdapter);

        date_btn = (TextView) view.findViewById(R.id.setting_standard_date);
        date_btn.setOnClickListener(this);
        time_btn = (TextView) view.findViewById(R.id.setting_standard_time);
        time_btn.setOnClickListener(this);

        autoClean_cb = (CheckBox) view.findViewById(R.id.setting_standard_auto_clean);

        saveBtn = view.findViewById(R.id.setting_standard_save_btn);
        saveBtn.setOnClickListener(this);

        timeService = TimeService.getInstance((BaseActivity) getActivity());
        timeService.addTimeListener(timeListener, R.id.setting_standard_save_btn);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        autoClean_cb.setChecked(Cache.isAutoClean());

        Activity activity = SettingStandardFragment.this.getActivity();
        if (activity instanceof BaseActivity) {
            baseActivity = (BaseActivity) activity;
        }

        ensureButton();
        return view;
    }

    private void ensureButton() {
        if (Cache.containsAuth("timeupdate")) {
            date_btn.setEnabled(true);
            time_btn.setEnabled(true);
        } else {
            date_btn.setEnabled(false);
            time_btn.setEnabled(false);
        }
        if (Cache.containsAuth("language")) {
            language_spinner.setEnabled(true);
        } else {
            language_spinner.setEnabled(false);
        }
    }

    private void refreshTime(long time) {
        String date = TimeTool.dateFormatter(time, "yyyy-MM-dd HH:mm:ss");
        if (!dateChanged) {
            currentDate = TimeTool.dateToDate(date);
        }
        if (!timeChanged) {
            currentTime = TimeTool.dateToMinite(date);
        }

        date_btn.setText(currentDate);
        time_btn.setText(currentTime);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_standard_save_btn:
                doSave();
                break;
            case R.id.setting_standard_date:
                if (!Cache.containsAuth("dateupdate")) {
                    return;
                }
                setDate();
                break;
            case R.id.setting_standard_time:
                if (!Cache.containsAuth("dateupdate")) {
                    return;
                }
                setTime();
                break;
        }
    }

    private void setDate() {
        if (dateWindow == null) {
            DisplayMetrics outMetrics = new DisplayMetrics();
            this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
            float dpx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, this.getActivity().getResources().getDisplayMetrics());
            dateWindow = new DateWindow(this.getActivity(), (int) (400 * dpx), (int) (300 * dpx), true, true);
        }
        dateWindow.setTouchable(true);
        dateWindow.setFocusable(true);
        dateWindow.setOutsideTouchable(true);
        dateWindow.setBackgroundDrawable(new BitmapDrawable());
        dateWindow.setDateChangeListener(new DateWindow.DateChangeListener() {
            @Override
            public void onChange(String date) {
                String date1 = TimeTool.dateToDate(date);
                dateChanged = true;
                date_btn.setText(date1);
                dateWindow.dismiss();
            }
        });
        dateWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    private void setTime() {
        if (timeWindow == null) {
            DisplayMetrics outMetrics = new DisplayMetrics();
            this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
            float dpx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, this.getActivity().getResources().getDisplayMetrics());
            timeWindow = new TimeWindow(this.getActivity(), (int) (300 * dpx), (int) (300 * dpx), true);
        }
        timeWindow.setTouchable(true);
        timeWindow.setFocusable(true);
        timeWindow.setOutsideTouchable(true);
        timeWindow.setBackgroundDrawable(new BitmapDrawable());
        timeWindow.setTimeChangeListener(new TimeWindow.TimeChangeListener() {
            @Override
            public void onChange(String time) {
                time_btn.setText(time);
                timeChanged = true;
                timeWindow.dismiss();
            }
        });
        timeWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    public void doSave() {
        progressDialog.setMessage(getString(R.string.saving));
        progressDialog.setCancelable(false);
        progressDialog.show();
        ExecutorService.getInstance().execute(
                new Runnable() {
                    @Override
                    public void run() {
                        boolean autoClean = autoClean_cb.isChecked();
                        String date = date_btn.getText().toString();
                        String time = time_btn.getText().toString();
                        Object item = language_spinner.getSelectedItem();
                        String language = ("中文".equals(item) || "Chinese".equals(item)) ? "zh" : "en";

                        if (baseActivity != null) {
                            languageChanged = !language.equals(baseActivity.getLanguage());
                            if (languageChanged){
                                AuditService.addAuditService(Cache.getUser().getUserName(), getString(R.string.setting), getString(R.string.stardand_setting), getString(R.string.alter_language), Cache.currentTime(), DBService.getAuditHelper(getActivity()));
                            }
                            baseActivity.selectLanguage(language);
                            baseActivity.setAutoClean(autoClean);
                            Cache.setAutoClean(autoClean);
                            if (timeChanged || dateChanged) {
                                baseActivity.timeAdd(TimeTool.getTimeOffset(date + " " + time));
                                AuditService.addAuditService(Cache.getUser().getUserName(), getString(R.string.setting), getString(R.string.stardand_setting), getString(R.string.alter_date), Cache.currentTime(), DBService.getAuditHelper(getActivity()));
                            }
                            timeChanged = true;
                        }
                        TrunkUtil.getInstance().addListener(new TrunkListener() {
                            @Override
                            public int getListenType() {
                                return TrunkConst.TYPE_AUTOCLEAN;
                            }

                            @Override
                            public void notifyData(TrunkData trunkData) {
                                TrunkUtil.getInstance().removeListener(R.id.setting_standard_save_btn);
                                Message.obtain(handler, BaseActivity.SAVE_SUCCESS).sendToTarget();
                            }
                        }, R.id.setting_standard_save_btn);
                        if (autoClean) {
                            TrunkUtil.getInstance().sendAutoClean(Cache.getAutoClean4());
                        } else {
                            TrunkUtil.getInstance().sendAutoClean(0);
                        }
                        if (baseActivity.getAutoClean() == autoClean) {
                            AuditService.addAuditService(Cache.getUser().getUserName(), getString(R.string.setting), getString(R.string.stardand_setting), getString(R.string.alter_auto_clean), Cache.currentTime(), DBService.getAuditHelper(getActivity()));
                        }
                    }
                }
        );
    }

    private TimeService.TimeListener timeListener = new TimeService.TimeListener() {
        @Override
        public void timeRefresh(long currentTime) {
            if (timeChanged || dateChanged) {
                return;
            }
            Message.obtain(handler, BaseActivity.NEED_REFRESH, currentTime).sendToTarget();
        }
    };

    private class SettingStandardHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BaseActivity.NEED_REFRESH:
                    refreshTime((long) msg.obj);
                    break;
                case BaseActivity.SAVE_SUCCESS:
                    ToastUtil.toastShow(mToast, getString(R.string.save_success));
                    progressDialog.dismiss();
                    if (languageChanged) {
                        baseActivity.finish();
                        Intent intent = new Intent(baseActivity, HomePageActivity.class);
                        startActivity(intent);
                    }
                    break;
            }
        }
    }
}
