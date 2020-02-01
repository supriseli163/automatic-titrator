package com.jh.automatic_titrator.ui.window;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.service.ExecutorService;
import com.jh.automatic_titrator.ui.BaseActivity;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by apple on 2017/2/22.
 */

public class UserDetailPopupWindow extends PopupWindow implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private Button savebtn;

    private OnSaveListener onSaveListener;

    private EditText descEt;

    private ImageView close;

    private CheckBox paramsetting;
    private CheckBox datadelete;
    private CheckBox datasearch;
    private CheckBox dataexport;
    private CheckBox dataprint;
    private CheckBox dataupload;
    private CheckBox recovery;
    private CheckBox language;
    private CheckBox timeupdate;
    private CheckBox correct;
    private CheckBox method;
    private CheckBox usermanager;
    private CheckBox audit;
    private CheckBox wavelength;
    private CheckBox fomula;
    private CheckBox cloud;
    private CheckBox network;

    public AuthChangeHandler authChangeHandler;

    private Set<String> auth;

    private String desc;

    public UserDetailPopupWindow(Activity activity, int width, int height, boolean focusable) {
        this(LayoutInflater.from(activity).inflate(
                R.layout.user_manager_detail_popup, null), width, height, focusable);
    }

    public UserDetailPopupWindow(View contentView, int width, int height, boolean focusable) {
        this(contentView, width, height, focusable, new HashSet<String>());
    }

    public UserDetailPopupWindow(final View contentView, int width, int height, boolean focusable, Set<String> auth) {
        super(contentView, width, height, focusable);
        authChangeHandler = new AuthChangeHandler();
        this.auth = auth;
        this.savebtn = (Button) contentView.findViewById(R.id.user_detail_popup_savebtn);
        savebtn.setOnClickListener(this);
        descEt = (EditText) contentView.findViewById(R.id.user_detail_popup_user_desc);
        close = (ImageView) contentView.findViewById(R.id.user_detail_popup_close);
        close.setOnClickListener(this);

        paramsetting = (CheckBox) contentView.findViewById(R.id.user_detail_popup_paramsetting);
        paramsetting.setOnCheckedChangeListener(this);
        datadelete = (CheckBox) contentView.findViewById(R.id.user_detail_popup_datadelte);
        datadelete.setOnCheckedChangeListener(this);
        datasearch = (CheckBox) contentView.findViewById(R.id.user_detail_popup_datasearch);
        datasearch.setOnCheckedChangeListener(this);
        dataexport = (CheckBox) contentView.findViewById(R.id.user_detail_popup_dataexport);
        dataexport.setOnCheckedChangeListener(this);
        dataprint = (CheckBox) contentView.findViewById(R.id.user_detail_popup_dataprint);
        dataprint.setOnCheckedChangeListener(this);
        dataupload = (CheckBox) contentView.findViewById(R.id.user_detail_popup_dataupload);
        dataupload.setOnCheckedChangeListener(this);
        recovery = (CheckBox) contentView.findViewById(R.id.user_detail_popup_recovery);
        recovery.setOnCheckedChangeListener(this);
        language = (CheckBox) contentView.findViewById(R.id.user_detail_popup_language);
        language.setOnCheckedChangeListener(this);
        timeupdate = (CheckBox) contentView.findViewById(R.id.user_detail_popup_timeupdate);
        timeupdate.setOnCheckedChangeListener(this);
        correct = (CheckBox) contentView.findViewById(R.id.user_detail_popup_correct);
        correct.setOnCheckedChangeListener(this);
        method = (CheckBox) contentView.findViewById(R.id.user_detail_popup_method);
        method.setOnCheckedChangeListener(this);
        usermanager = (CheckBox) contentView.findViewById(R.id.user_detail_popup_usermanager);
        usermanager.setOnCheckedChangeListener(this);
        audit = (CheckBox) contentView.findViewById(R.id.user_detail_popup_audit);
        audit.setOnCheckedChangeListener(this);
        wavelength = (CheckBox) contentView.findViewById(R.id.user_detail_popup_wavelength);
        wavelength.setOnCheckedChangeListener(this);
        fomula = (CheckBox) contentView.findViewById(R.id.user_detail_popup_fomulaadd);
        fomula.setOnCheckedChangeListener(this);
        cloud = (CheckBox) contentView.findViewById(R.id.user_detail_popup_fileserver);
        cloud.setOnCheckedChangeListener(this);
        network = (CheckBox) contentView.findViewById(R.id.user_detail_popup_networksetting);
        network.setOnCheckedChangeListener(this);
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                InputMethodManager imm = (InputMethodManager) contentView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(contentView.getWindowToken(), 0);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_detail_popup_savebtn:
                if (onSaveListener != null) {
                    ExecutorService.getInstance().execute(
                            new Runnable() {
                                @Override
                                public void run() {
                                    onSaveListener.onSave(auth, descEt.getText().toString());
                                }
                            }
                    );
                }
                break;
            case R.id.user_detail_popup_close:
                this.dismiss();
                break;
        }
    }

    public void setOnSaveListener(OnSaveListener onSaveListener) {
        this.onSaveListener = onSaveListener;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.user_detail_popup_paramsetting:
                if (isChecked) {
                    auth.add("paramsetting");
                } else {
                    auth.remove("paramsetting");
                }
                break;
            case R.id.user_detail_popup_datadelte:
                if (isChecked) {
                    auth.add("datadelete");
                } else {
                    auth.remove("datadelete");
                }
                break;
            case R.id.user_detail_popup_datasearch:
                if (isChecked) {
                    auth.add("datasearch");
                } else {
                    auth.remove("datasearch");
                }
                break;
            case R.id.user_detail_popup_dataexport:
                if (isChecked) {
                    auth.add("dataexport");
                } else {
                    auth.remove("dataexport");
                }
                break;
            case R.id.user_detail_popup_dataprint:
                if (isChecked) {
                    auth.add("dataprint");
                } else {
                    auth.remove("dataprint");
                }
                break;
            case R.id.user_detail_popup_dataupload:
                if (isChecked) {
                    auth.add("dataupload");
                } else {
                    auth.remove("dataupload");
                }
                break;
            case R.id.user_detail_popup_recovery:
                if (isChecked) {
                    auth.add("recovery");
                } else {
                    auth.remove("recovery");
                }
                break;
            case R.id.user_detail_popup_language:
                if (isChecked) {
                    auth.add("language");
                } else {
                    auth.remove("language");
                }
                break;
            case R.id.user_detail_popup_timeupdate:
                if (isChecked) {
                    auth.add("timeupdate");
                } else {
                    auth.remove("timeupdate");
                }
                break;
            case R.id.user_detail_popup_correct:
                if (isChecked) {
                    auth.add("correct");
                } else {
                    auth.remove("correct");
                }
                break;
            case R.id.user_detail_popup_method:
                if (isChecked) {
                    auth.add("method");
                } else {
                    auth.remove("method");
                }
                break;
            case R.id.user_detail_popup_usermanager:
                if (isChecked) {
                    auth.add("usermanager");
                } else {
                    auth.remove("usermanager");
                }
                break;
            case R.id.user_detail_popup_audit:
                if (isChecked) {
                    auth.add("audit");
                } else {
                    auth.remove("audit");
                }
                break;
            case R.id.user_detail_popup_wavelength:
                if (isChecked) {
                    auth.add("wavelength");
                } else {
                    auth.remove("wavelength");
                }
                break;
            case R.id.user_detail_popup_fomulaadd:
                if (isChecked) {
                    auth.add("formula");
                } else {
                    auth.remove("formula");
                }
                break;
            case R.id.user_detail_popup_fileserver:
                if (isChecked) {
                    auth.add("cloud");
                } else {
                    auth.remove("cloud");
                }
                break;
            case R.id.user_detail_popup_networksetting:
                if (isChecked) {
                    auth.add("network");
                } else {
                    auth.remove("network");
                }
                break;
        }
    }

    public class AuthChangeHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BaseActivity.NEED_REFRESH:
                    freshAuth();
                    break;
            }
        }
    }

    public void show(Set<String> auth, int userType, boolean add) {
        this.auth = auth;
        freshAuth();
        recovery.setChecked(false);
        recovery.setEnabled(false);
        language.setChecked(false);
        language.setEnabled(false);
        audit.setChecked(false);
        audit.setEnabled(false);
        switch (userType) {
            case 1:
                if (add) {
                    paramsetting.setChecked(true);
                    datadelete.setChecked(true);
                    datasearch.setChecked(true);
                    dataexport.setChecked(true);
                    dataupload.setChecked(true);
                    dataprint.setChecked(true);
                    timeupdate.setChecked(true);
                    correct.setChecked(true);
                    method.setChecked(true);
                    usermanager.setChecked(true);
                    wavelength.setChecked(true);
                    fomula.setChecked(true);
                    cloud.setChecked(true);
                    network.setChecked(true);
                }
                break;
            case 2:
                timeupdate.setChecked(false);
                timeupdate.setEnabled(false);
                usermanager.setChecked(false);
                usermanager.setEnabled(false);
                if (add) {
                    paramsetting.setChecked(true);
                }
                break;
        }
        setTouchable(true);
        setFocusable(true);
        descEt.setText(desc);
        showAtLocation(getContentView(), Gravity.CENTER, 0, 0);
    }

    private void freshAuth() {
        paramsetting.setChecked(auth.contains("paramsetting") || auth.contains("all"));
        datadelete.setChecked(auth.contains("datadelete") || auth.contains("all"));
        datasearch.setChecked(auth.contains("datasearch") || auth.contains("all"));
        dataexport.setChecked(auth.contains("dataexport") || auth.contains("all"));
        dataprint.setChecked(auth.contains("dataprint") || auth.contains("all"));
        dataupload.setChecked(auth.contains("dataupload") || auth.contains("all"));
        recovery.setChecked(auth.contains("recovery") || auth.contains("all"));
        language.setChecked(auth.contains("language") || auth.contains("all"));
        timeupdate.setChecked(auth.contains("timeupdate") || auth.contains("all"));
        correct.setChecked(auth.contains("correct") || auth.contains("all"));
        method.setChecked(auth.contains("method") || auth.contains("all"));
        usermanager.setChecked(auth.contains("usermanager") || auth.contains("all"));
        audit.setChecked(auth.contains("audit") || auth.contains("all"));
        wavelength.setChecked(auth.contains("wavelength") || auth.contains("all"));
        fomula.setChecked(auth.contains("fomula") || auth.contains("all"));
        cloud.setChecked(auth.contains("cloud") || auth.contains("all"));
        network.setChecked(auth.contains("network") || auth.contains("all"));
        descEt.setText(desc);
    }

    public Set<String> getAuth() {
        return auth;
    }

    public void setAuth(Set<String> auth) {
        this.auth = auth;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public interface OnSaveListener {
        void onSave(Set<String> auth, String desc);
    }
}

