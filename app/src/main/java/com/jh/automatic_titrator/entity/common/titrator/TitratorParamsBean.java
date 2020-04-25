package com.jh.automatic_titrator.entity.common.titrator;

import com.jh.automatic_titrator.common.utils.StringUtils;
import com.jh.automatic_titrator.entity.common.MainTitrant;

import java.util.List;

public class TitratorParamsBean {
    // 方法属性
    public TitratorMethod titratorMethod;
    // 滴定属性
    public List<TitratorEndPoint> titratorEndPoint;
    // 主滴定剂量
    public MainTitrant mainTitrant;

    //滴定终点设置
    public List<EndPointSetting> endPointSettings;

    public TitratorMethod getTitratorMethod() {
        return titratorMethod;
    }

    public void setTitratorMethod(TitratorMethod titratorMethod) {
        this.titratorMethod = titratorMethod;
    }

    public List<TitratorEndPoint> getTitratorEndPoint() {
        return titratorEndPoint;
    }

    public void setTitratorEndPoint(List<TitratorEndPoint> titratorEndPoint) {
        this.titratorEndPoint = titratorEndPoint;
    }

    public List<EndPointSetting> getEndPointSettings() {
        return endPointSettings;
    }

    public void setEndPointSettings(List<EndPointSetting> endPointSettings) {
        this.endPointSettings = endPointSettings;
    }

    public MainTitrant getMainTitrant() {
        return mainTitrant;
    }

    public void setMainTitrant(MainTitrant mainTitrant) {
        this.mainTitrant = mainTitrant;
    }

    public String getMethodName() {
        return titratorMethod != null ? StringUtils.getSecurity(titratorMethod.getMethodName()) : "";
    }
}
