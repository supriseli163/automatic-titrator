package com.jh.automatic_titrator.entity.common.titrator;

import com.jh.automatic_titrator.common.utils.StringUtils;
import com.jh.automatic_titrator.entity.common.MainTitrant;

public class TitratorParamsBean {
    // 方法属性
    public TitratorMethod titratorMethod;
    // 滴定属性
    public TitratorEndPoint titratorEndPoint;
    // 主滴定剂量
    public MainTitrant mainTitrant;

    public TitratorMethod getTitratorMethod() {
        return titratorMethod;
    }

    public void setTitratorMethod(TitratorMethod titratorMethod) {
        this.titratorMethod = titratorMethod;
    }

    public TitratorEndPoint getTitratorEndPoint() {
        return titratorEndPoint;
    }

    public void setTitratorEndPoint(TitratorEndPoint titratorEndPoint) {
        this.titratorEndPoint = titratorEndPoint;
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
