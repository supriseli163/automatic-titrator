package com.jh.automatic_titrator.entity.method;

import com.jh.automatic_titrator.entity.common.titrator.TitratorTypeEnum;

import java.io.Serializable;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class TiratorExecuteMethodViewBean extends BaseObservable implements Serializable {

    // 当前滴定方式
    private TitratorTypeEnum currentEnum;
    // 滴定方法对应滴定数据
//    private TiratorExecuteMethodViewBean bean;

    @Bindable
    public boolean isEqualTitrator() {
        return TitratorTypeEnum.EqualTitrator == currentEnum;
    }

    public void setCurrentEnum(TitratorTypeEnum typeEnum) {
        currentEnum = typeEnum;
        notifyAllSelectStatus();
    }

    @Bindable
    public boolean isDynamicTitrator() {
        return TitratorTypeEnum.DynamicTitrator == currentEnum;
    }

    @Bindable
    public boolean isManualTitrator() {
        return TitratorTypeEnum.ManualTitrator == currentEnum;
    }

    @Bindable
    public boolean isEndPointTitrator() {
        return TitratorTypeEnum.EndPointTitrator == currentEnum;
    }

    @Bindable
    public boolean isStopFoverTitrator() {
        return TitratorTypeEnum.StopForverTitrator == currentEnum;
    }

    public void notifyAllSelectStatus() {
        notifyPropertyChanged(com.jh.automatic_titrator.BR.equalTitrator);
        notifyPropertyChanged(com.jh.automatic_titrator.BR.dynamicTitrator);
        notifyPropertyChanged(com.jh.automatic_titrator.BR.manualTitrator);
        notifyPropertyChanged(com.jh.automatic_titrator.BR.endVolumeText);
        notifyPropertyChanged(com.jh.automatic_titrator.BR.stopFoverTitrator);
    }
}