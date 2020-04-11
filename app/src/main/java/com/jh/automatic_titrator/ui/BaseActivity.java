package com.jh.automatic_titrator.ui;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.common.Cache;
import com.jh.automatic_titrator.common.utils.SharedPreferenceUtils;
import com.jh.automatic_titrator.service.DBService;

import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

/**
 * Created by apple on 2016/10/25.
 */
public abstract class BaseActivity<T extends ViewDataBinding> extends AppCompatActivity {

    public static final int DELETE_SUCCESS = 0x00000040;
    public static final int DELETE_FAILED = 0x00000041;
    public static final int DELETE_ENSURE = 0x00000042;
    public static final int DELETE_NO_NEED = 0x00000043;

    public static final int SAVE_SUCCESS = 0x00000030;
    public static final int SAVING = 0x00000031;
    public static final int SAVE_FAILED = 0x00000032;
    public static final int SAVE_INDEX_EXISTS = 0x00000033;

    public static final int UPDATE_SUCCESS = 0x00000020;
    public static final int UPDATE_FAILED = 0x00000021;
    public static final int USER_UPDATE = 0x00000025;

    public static final int SELECT_SUCCESS = 0x00000010;
    public static final int SELECT_FAILED = 0x00000011;

    public static final int FILE_SYSTEM_FULL = 0xFFFFFFFF;

    public static final int NEED_REFRESH = 0x00000005;
    public static final int CHOOSE_NOTHING = 0x00000006;
    public static final int NEED_CHECK_ONE = 0x00000007;
    public static final int NEED_CLEAR = 0x00000008;

    public static final int PASSWORD_SUCCESS = 0x00000015;
    public static final int PASSWORD_ERROR = 0x00000016;

    public static final int FIELD_CANNOT_BE_NULL = 0x0f000000;

    public static final int HOLD_WINDOW_YES = 0x00f00000;
    public static final int HOLD_WINDOW_NO = 0x00f00001;
    public static final int HOLD_BUTTON_YES = 0x00f00002;
    public static final int HOLD_BUTTON_NO = 0x00f00003;

    public static final int SHOW_MSG = 0x00ff0000;
    public T viewBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_layout);
        RelativeLayout container = findViewById(R.id.base_view);
        View layout = LayoutInflater.from(this).inflate(getLayoutRes(), container, false);
        container.removeAllViews();
        container.addView(layout);
        viewBinding = DataBindingUtil.bind(container.getChildAt(0));
        onInit(savedInstanceState);
    }

    public abstract void onInit(Bundle savedInstanceState);

    public abstract int getLayoutRes();

    public void selectLanguage(String language) {
        //设置语言类型
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        int version = android.os.Build.VERSION.SDK_INT;
        switch (language) {
            case "en":
                if (version > 16) {
                    configuration.setLocale(Locale.US);
                } else {
                    configuration.locale = Locale.US;
                }
                break;
            case "zh":
                if (version > 16) {
                    configuration.setLocale(Locale.CHINESE);
                } else {
                    configuration.locale = Locale.CHINESE;
                }
                break;
            default:
                if (version > 16) {
                    configuration.setLocale(Locale.getDefault());
                } else {
                    configuration.locale = Locale.getDefault();
                }
                break;
        }
        resources.updateConfiguration(configuration, displayMetrics);

        //保存设置语言的类型
        SharedPreferenceUtils.put(this, "language", language);
    }

    public String getLanguage() {
        return (String) SharedPreferenceUtils.get(this, "language", "zh");
    }

    public void setShowLogo(boolean setShowLogo) {
        SharedPreferenceUtils.put(this, "showlogo", setShowLogo);
    }

    public boolean showLogo() {
        return (boolean) SharedPreferenceUtils.get(this, "showlogo", initShowLogo());
    }

    public void setInitShowLogo(boolean setShowLogo) {
        SharedPreferenceUtils.put(this, "initShowlogo", setShowLogo);
    }

    public boolean initShowLogo() {
        return (boolean) SharedPreferenceUtils.get(this, "initShowlogo", true);
    }

    public void timeAdd(long addTime) {
        SharedPreferenceUtils.put(this, "timeadd", addTime);
        Cache.setDateOffset(addTime);
    }

    public long getTimeAdd() {
        return (long) SharedPreferenceUtils.get(this, "timeadd", 0l);
    }

    public void temperatureAdd(String addTime) {
        SharedPreferenceUtils.put(this, "temperatureAdd", addTime);
    }

    public String getTemperatureAdd() {
        return (String) SharedPreferenceUtils.get(this, "temperatureAdd", "0.0");
    }

    public void setAutoClean(boolean autoClean) {
        SharedPreferenceUtils.put(this, "autoClean", autoClean ? 1 : 0);
    }

    public boolean getAutoClean() {
        int autoClean = (int) SharedPreferenceUtils.get(this, "autoClean", 0);
        return autoClean != 0;
    }

    public float getInitAutoClean3() {
        float initAutoClean3 = (float) SharedPreferenceUtils.get(this, "initAutoClean3", 0.001f);
        return initAutoClean3;
    }

    public float getInitAutoClean4() {
        float initAutoClean4 = (float) SharedPreferenceUtils.get(this, "initAutoClean4", 0.001f);
        return initAutoClean4;
    }

    public void setInitAutoClean3(float initAutoClean3) {
        SharedPreferenceUtils.put(this, "initAutoClean3", initAutoClean3);
    }

    public void setInitAutoClean4(float initAutoClean4) {
        SharedPreferenceUtils.put(this, "initAutoClean4", initAutoClean4);
    }

    public float getAutoClean3() {
        float initAutoClean3 = (float) SharedPreferenceUtils.get(this, "autoClean3", getInitAutoClean3());
        return initAutoClean3;
    }

    public float getAutoClean4() {
        float initAutoClean4 = (float) SharedPreferenceUtils.get(this, "autoClean4", getInitAutoClean4());
        return initAutoClean4;
    }

    public void setAutoClean3(float autoClean3) {
        SharedPreferenceUtils.put(this, "autoClean3", autoClean3);
    }

    public void setAutoClean4(float autoClean4) {
        SharedPreferenceUtils.put(this, "autoClean4", autoClean4);
    }

    public void recover() {
        SharedPreferenceUtils.put(this, "autoClean3", getInitAutoClean3());
        SharedPreferenceUtils.put(this, "autoClean4", getInitAutoClean4());
        SharedPreferenceUtils.put(this, "showlogo", showLogo());
        SharedPreferenceUtils.put(this, "autoClean", true);
        DBService.clearAllData(this);
    }

    public void cover() {
        SharedPreferenceUtils.put(this, "initAutoClean3", getAutoClean3());
        SharedPreferenceUtils.put(this, "initAutoClean4", getAutoClean4());
        SharedPreferenceUtils.put(this, "initShowlogo", showLogo());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Cache.cleanAll();
//        DBService.close(this);
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            View v = getCurrentFocus();
//            if (isShouldHideInput(v, ev)) {
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                if (imm != null && v != null) {
//                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//                }
//            }
//            return super.dispatchTouchEvent(ev);
//        }
//        // 必不可少，否则所有的组件都不会有TouchEvent了
//        if (getWindow().superDispatchTouchEvent(ev)) {
//            return true;
//        }
//        return onTouchEvent(ev);
//    }

    private boolean isShouldHideInput(View v, MotionEvent ev) {
        return !(v instanceof EditText);
    }

    public void writeconf(String key, String val) {
        SharedPreferences appConf = getSharedPreferences("com.jh.automatic_titrator.myconf_preferences", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = appConf.edit();
        prefsEditor.putString(key, val);
        prefsEditor.commit();
    }

    public String readconf(String key) {
        String str;
        SharedPreferences appConf = getSharedPreferences("com.jh.automatic_titrator.myconf_preferences", MODE_PRIVATE);
        if (!appConf.contains(key)) {
            str = new String("");
            return str;
        }
        str = appConf.getString(key, "");
        return str;
    }

//    public boolean getAutoTest() {
//        int autoTest = (int) SharedPreferenceUtils.get(this, "autoTest", 0);
//        return autoTest != 0;
//    }
//
//    public void setAutoTest(boolean autoTest) {
//        SharedPreferenceUtils.put(this, "autoTest", autoTest ? 1 : 0);
//    }
}
