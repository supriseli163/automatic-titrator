package com.jh.automatic_titrator.common.utils;

import com.jh.automatic_titrator.entity.common.titrator.TitratorMethod;
import com.jh.automatic_titrator.entity.common.titrator.TitratorParamsBean;

public class TitratorParamsBeanUtils {

    // 滴定类型
    public static String getTitratorType(TitratorParamsBean bean) {
        if (bean != null && bean.getTitratorMethod() != null) {
            TitratorMethod method = bean.getTitratorMethod();
            return StringUtils.getSecurity(method.getTitratorType());
        }
        return "";
    }

    // 方法名称
    public static String getMethodName(TitratorParamsBean bean) {
        if (bean != null && bean.getTitratorMethod() != null) {
            TitratorMethod method = bean.getTitratorMethod();
            return StringUtils.getSecurity(method.getMethodName());
        }
        return "";
    }

    // 滴定管体积
    public static String getBuretteVolume(TitratorParamsBean bean) {
        if (bean != null && bean.getTitratorMethod() != null) {
            TitratorMethod method = bean.getTitratorMethod();
            double volume = method.getBuretteVolume();
            if (volume > 0) {
                return String.valueOf(volume);
            }
            return "";
        }
        return "";
    }

    // 工作电级
    public static String getWorkElectrodeEnnum(TitratorParamsBean bean) {
        if (bean != null && bean.getTitratorMethod() != null) {
            TitratorMethod method = bean.getTitratorMethod();
            return StringUtils.getSecurity(method.getWorkingElectrode());
        }
        return "";
    }

    // 参比电级(参考电极)
    public static String getReferenceElectrode(TitratorParamsBean bean) {
        if (bean != null && bean.getTitratorMethod() != null) {
            TitratorMethod method = bean.getTitratorMethod();
            double value = method.getReferenceElectrode();
            if (value > 0) {
                return String.valueOf(value);
            } else {
                return "";
            }
        }
        return "";
    }

    // 样品计量单位
    public static String getSampleMeasurementUnit(TitratorParamsBean bean) {
        if (bean != null && bean.getTitratorMethod() != null) {
            TitratorMethod method = bean.getTitratorMethod();
            return StringUtils.getSecurity(method.getSampleMeasurementUnit());
        }
        return "";
    }

    // 滴定显示单位
    public static String getTitrationDisplayUnit(TitratorParamsBean bean) {
        if (bean != null && bean.getTitratorMethod() != null) {
            TitratorMethod method = bean.getTitratorMethod();
            return StringUtils.getSecurity(method.getTitrationDisplayUnit());
        }
        return "";
    }

    // 补液速度
    public static String getReplenishmentSpeed(TitratorParamsBean bean) {
        if (bean != null && bean.getTitratorMethod() != null) {
            TitratorMethod method = bean.getTitratorMethod();
            return StringUtils.getSecurity(method.getReplenishmentSpeed());
        }
        return "";
    }

    // 搅拌速度
    public static String getStiringSpeed(TitratorParamsBean bean) {
        if (bean != null && bean.getTitratorMethod() != null) {
            TitratorMethod method = bean.getTitratorMethod();
            return StringUtils.getSecurity(method.getStiringSpeed());
        }
        return "";
    }

    // 电极平衡时间
    public static String getElectroedEquilibrationTime(TitratorParamsBean bean) {
        if (bean != null && bean.getTitratorMethod() != null) {
            TitratorMethod method = bean.getTitratorMethod();
            return StringUtils.getSecurity(method.getElectroedEquilibrationTime());
        }
        return "";
    }

    // 电极平衡电位
    public static String getElectroedEquilibriumPotential(TitratorParamsBean bean) {
        if (bean != null && bean.getTitratorMethod() != null) {
            TitratorMethod method = bean.getTitratorMethod();
            return StringUtils.getSecurity(method.getElectroedEquilibriumPotential());
        }
        return "";
    }

    // 预搅拌时间
    public static String getPreStiringTime(TitratorParamsBean bean) {
        if (bean != null && bean.getTitratorMethod() != null) {
            TitratorMethod method = bean.getTitratorMethod();
            return StringUtils.getSecurity(method.getPreStiringTime());
        }
        return "";
    }

    // 每次添加体积
    public static String getPerAddVolume(TitratorParamsBean bean) {
        if (bean != null && bean.getTitratorMethod() != null) {
            TitratorMethod method = bean.getTitratorMethod();
            return StringUtils.getSecurity(method.getPerAddVolume());
        }
        return "";
    }

    // 结束体积
    public static String getEndVolume(TitratorParamsBean bean) {
        if (bean != null && bean.getTitratorMethod() != null) {
            TitratorMethod method = bean.getTitratorMethod();
            return StringUtils.getSecurity(method.getEndVolume());
        }
        return "";
    }

    // 滴定速度
    public static String getTitrationSpeed(TitratorParamsBean bean) {
        if (bean != null && bean.getTitratorMethod() != null) {
            TitratorMethod method = bean.getTitratorMethod();
            int value = method.getTitrationSpeed();
            if (value > 0) {
                return String.valueOf(value);
            }
            return "";
        }
        return "";
    }

    // 慢滴体积
    public static String getSlowTitrationVolume(TitratorParamsBean bean) {
        if (bean != null && bean.getTitratorMethod() != null) {
            TitratorMethod method = bean.getTitratorMethod();
            return StringUtils.getSecurity(method.getSlowTitrationVolume());
        }
        return "";
    }

    // 快滴体积
    public static String getFastTitrationVolume(TitratorParamsBean bean) {
        if (bean != null && bean.getTitratorMethod() != null) {
            TitratorMethod method = bean.getTitratorMethod();
            return StringUtils.getSecurity(method.getFastTitrationVolume());
        }
        return "";
    }

    // 修改时间
    public static String getModifyTime(TitratorParamsBean bean) {
        if (bean != null && bean.getTitratorMethod() != null) {
            TitratorMethod method = bean.getTitratorMethod();
            return StringUtils.getSecurity(method.getModifyTime());
        }
        return "";
    }

    // userName
    public static String getUserName(TitratorParamsBean bean) {
        if (bean != null && bean.getTitratorMethod() != null) {
            TitratorMethod method = bean.getTitratorMethod();
            return StringUtils.getSecurity(method.getUserName());
        }
        return "";
    }

    // 预定添加体积
    public static String getPreAddVolume(TitratorParamsBean bean) {
        if (bean != null && bean.getPreTitrant() != null) {
            double titrant = bean.getPreTitrant().getPreAddVolume();
            if (titrant >= 0) {
                return String.valueOf(titrant);
            }
            return "";
        }
        return "";
    }

    // 预滴定后搅拌时间
    public static String getPreAfterStiringTime(TitratorParamsBean bean) {
        if (bean != null && bean.getPreTitrant() != null) {
            double value = bean.getPreTitrant().getPreAfterstiringTime();
            if (value >= 0) {
                return String.valueOf(value);
            }
            return "";
        }
        return "";
    }
}
