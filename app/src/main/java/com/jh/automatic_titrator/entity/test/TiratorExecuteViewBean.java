package com.jh.automatic_titrator.entity.test;

import android.widget.Toast;

import com.jh.automatic_titrator.BR;
import com.jh.automatic_titrator.BaseApplication;

import java.io.Serializable;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class TiratorExecuteViewBean extends BaseObservable implements Serializable {

    public boolean isTestTabSelect;

    public boolean isTestSampleSelect;

    public boolean isAtlasShow;

    public boolean isEnableClick = true;

    @Bindable
    public boolean isAtlasShow() {
        return isAtlasShow;
    }

    @Bindable
    public boolean isEnableClick() {
//        Toast.makeText(BaseApplication.getApplication(), "可点击：" + isEnableClick, Toast.LENGTH_SHORT).show();
        return isEnableClick;
    }

    public void updateEnable() {
        isEnableClick = !isEnableClick;
        notifyPropertyChanged(BR._all);
    }

    public void updateAtlasStatus() {
        isAtlasShow = !isAtlasShow;
        notifyPropertyChanged(BR._all);
    }

    public void setTestTabSelect() {
        clearAllStatus();
        isTestTabSelect = true;
        notifyPropertyChanged(BR._all);
    }

    public void setTestSampleSelect() {
        clearAllStatus();
        isTestSampleSelect = true;
        notifyPropertyChanged(BR._all);
    }

    @Bindable
    public boolean isTestTabSelect() {
        return isTestTabSelect;
    }

    @Bindable
    public boolean isTestSampleSelect() {
        return isTestSampleSelect;
    }

    public void clearAllStatus() {
        isTestSampleSelect = false;
        isTestTabSelect = false;
    }
}