package com.jh.automatic_titrator.entity.test;

import android.util.Log;

import java.io.Serializable;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

public class TiratorExecuteViewBean extends BaseObservable implements Serializable {

    public boolean isTestTabSelect;

    public boolean isTestSampleSelect;

    public boolean isAtlasShow;

    @Bindable
    public boolean isAtlasShow() {
        return isAtlasShow;
    }

    public void updateAtlasStatus() {
        isAtlasShow = !isAtlasShow;
        notifyPropertyChanged(BR.atlasShow);
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