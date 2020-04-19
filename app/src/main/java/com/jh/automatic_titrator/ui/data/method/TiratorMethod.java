package com.jh.automatic_titrator.ui.data.method;

import com.jh.automatic_titrator.entity.common.titrator.TitratorParamsBean;
import com.jh.automatic_titrator.entity.method.TiratorExecuteMethodViewBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TiratorMethod {
    /**
     * 存储当前选择方法、方法属性
     */
    public TiratorExecuteMethodViewBean tiratorExecuteMethodViewBean;

    private Map<String, List<TitratorParamsBean>> titratorParamsBeanMap = new HashMap<>();


    public TiratorExecuteMethodViewBean getTiratorExecuteMethodViewBean() {
        return tiratorExecuteMethodViewBean;
    }

    public void setTiratorExecuteMethodViewBean(TiratorExecuteMethodViewBean tiratorExecuteMethodViewBean) {
        this.tiratorExecuteMethodViewBean = tiratorExecuteMethodViewBean;
    }

    public Map<String, List<TitratorParamsBean>> getTitratorParamsBeanMap() {
        // TODO: 2020-04-19 这里从数据库查到表，然后聚合返回
        return titratorParamsBeanMap;
    }

    public void setTitratorParamsBeanMap(Map<String, List<TitratorParamsBean>> titratorParamsBeanMap) {
        this.titratorParamsBeanMap = titratorParamsBeanMap;
    }
}
