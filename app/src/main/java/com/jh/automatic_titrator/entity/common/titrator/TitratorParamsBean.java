package com.jh.automatic_titrator.entity.common.titrator;

import com.jh.automatic_titrator.common.utils.CollectionUtils;
import com.jh.automatic_titrator.common.utils.StringUtils;
import com.jh.automatic_titrator.entity.common.MainTitrant;
import com.jh.automatic_titrator.entity.common.PreTitrant;

import java.util.ArrayList;
import java.util.List;

public class TitratorParamsBean {
    // 方法属性
    public TitratorMethod titratorMethod;
    // 滴定属性
    public List<TitratorEndPoint> titratorEndPoints;
    // 主滴定剂量
    public MainTitrant mainTitrant;
    // 预滴定
    public PreTitrant preTitrant;

    // 辅助试剂
    public List<EndPointSetting> endPointSettings;

    public TitratorParamsBean() {
        this.titratorMethod = new TitratorMethod();
        titratorEndPoints = new ArrayList<>();
        mainTitrant = new MainTitrant();
        preTitrant = new PreTitrant();
    }

    // 是否选中
    private boolean isCheck;

    public PreTitrant getPreTitrant() {
        return preTitrant;
    }

    public void setPreTitrant(PreTitrant preTitrant) {
        this.preTitrant = preTitrant;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public TitratorMethod getTitratorMethod() {
        return titratorMethod;
    }

    public void setTitratorMethod(TitratorMethod titratorMethod) {
        this.titratorMethod = titratorMethod;
    }

    public List<TitratorEndPoint> getTitratorEndPoint() {
        return titratorEndPoints;
    }

    public void setTitratorEndPoints(List<TitratorEndPoint> titratorEndPoints) {
        this.titratorEndPoints = titratorEndPoints;
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

    public void updateEndPointSetting(EndPointSetting setting) {
        if (setting == null) {
            return;
        }
        if (endPointSettings == null) {
            endPointSettings = new ArrayList<>();
        }
        boolean hasSomeData = false;
        for (int i = 0; i < CollectionUtils.size(endPointSettings); i++) {
            EndPointSetting data = endPointSettings.get(i);
            if (data != null && data.getId() == setting.getId()) {
                hasSomeData = true;
                endPointSettings.set(i, setting);
                break;
            }
        }
        if (!hasSomeData) {
            endPointSettings.add(setting);
        }
    }

    public void updateTitratorEndPoint(TitratorEndPoint point) {
        if (point == null) {
            return;
        }
        if (titratorEndPoints == null) {
            titratorEndPoints = new ArrayList<>();
        }
        boolean hasSomeData = false;
        for (int i = 0; i < CollectionUtils.size(titratorEndPoints); i++) {
            TitratorEndPoint data = titratorEndPoints.get(i);
            if (data != null && data.getId() == point.getId()) {
                hasSomeData = true;
                titratorEndPoints.set(i, point);
                break;
            }
        }
        if (!hasSomeData) {
            titratorEndPoints.add(point);
        }
    }
}