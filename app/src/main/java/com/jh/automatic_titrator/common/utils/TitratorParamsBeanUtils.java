package com.jh.automatic_titrator.common.utils;

import android.util.Log;

import com.jh.automatic_titrator.BaseApplication;
import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.databinding.TitratorMethodFragmentBurettePopup;
import com.jh.automatic_titrator.databinding.TitratorMethodFragmentEndPointPopup;
import com.jh.automatic_titrator.databinding.TitratorSettingMethodFragmentPopupBinding;
import com.jh.automatic_titrator.entity.common.MainTitrant;
import com.jh.automatic_titrator.entity.common.titrator.EndPointSetting;
import com.jh.automatic_titrator.entity.common.titrator.TitratorEndPoint;
import com.jh.automatic_titrator.entity.common.titrator.TitratorMethod;
import com.jh.automatic_titrator.entity.common.titrator.TitratorParamsBean;
import com.jh.automatic_titrator.entity.common.titrator.WorkElectrodeEnnum;

import java.util.ArrayList;
import java.util.List;

import static com.jh.automatic_titrator.entity.common.titrator.WorkElectrodeEnnum.Calcium_Electrode;
import static com.jh.automatic_titrator.entity.common.titrator.WorkElectrodeEnnum.Copper_Electrode;
import static com.jh.automatic_titrator.entity.common.titrator.WorkElectrodeEnnum.Fluoride_Electrode;
import static com.jh.automatic_titrator.entity.common.titrator.WorkElectrodeEnnum.PH_Electrode;
import static com.jh.automatic_titrator.entity.common.titrator.WorkElectrodeEnnum.PH_Mixed_Electrode;
import static com.jh.automatic_titrator.entity.common.titrator.WorkElectrodeEnnum.Platinum_Electrode;
import static com.jh.automatic_titrator.entity.common.titrator.WorkElectrodeEnnum.Silver_Electrode;

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
        String content = "";
        if (bean != null && bean.getPreTitrant() != null) {
            double titrant = bean.getPreTitrant().getPreAddVolume();
            if (titrant >= 0) {
                content = String.valueOf(titrant);
            }
        }
        Log.d("TitratorParamsBeanUtils", "预定添加体积getPreAddVolume: " + content);
        return content;
    }

    // 预滴定后搅拌时间
    public static String getPreAfterStiringTime(TitratorParamsBean bean) {
        String content = "";
        if (bean != null && bean.getPreTitrant() != null) {
            double value = bean.getPreTitrant().getPreAfterStiringTime();
            if (value >= 0) {
                content = String.valueOf(value);
            }
        }
        Log.d("TitratorParamsBeanUtils", "预定添加体积getPreAddVolume: " + content);
        return content;
    }

    // 主滴定剂名称
    public static String getReagentName(TitratorParamsBean bean) {
        String content = "";
        if (bean != null && bean.getMainTitrant() != null) {
            content = bean.getMainTitrant().getReagentName();
        }
        Log.d("TitratorParamsBeanUtils", "主滴定剂名称getReagentName: " + content);
        return content;
    }

    // 主滴定剂浓度
    public static String getTheoreticalConcentration(TitratorParamsBean bean) {
        String content = "";
        if (bean != null && bean.getMainTitrant() != null) {
            double value = bean.getMainTitrant().getTheoreticalConcentration();
            if (value >= 0) {
                content = String.valueOf(value);
            }
        }
        Log.d("TitratorParamsBeanUtils", "主滴定剂浓度getTheoreticalConcentration: " + content);
        return content;
    }

    // 获取滴定终点List:List<String>
    public static List<List<String>> getTitratorEndList(TitratorParamsBean bean) {
        List<List<String>> arrays = new ArrayList<>();
        if (bean != null) {
            arrays.add(getChineseEndPointBar());
            List<TitratorEndPoint> titratorEndPoints = bean.getTitratorEndPoint();
            for (int i = 0; i < CollectionUtils.size(titratorEndPoints); i++) {
                List<String> list = getListFromTitratorEndPoints(titratorEndPoints.get(i));
                arrays.add(list);
            }
        }
        return arrays;
    }

    // 获取滴定辅助试剂List:List<String>
    public static List<List<String>> getAuxiliaryReagentList(TitratorParamsBean bean) {
        List<List<String>> arrays = new ArrayList<>();
        if (bean != null) {
            arrays.add(getAuxiliaryReagent());
            List<EndPointSetting> endPointSettingsList = bean.getEndPointSettings();
            for (int i = 0; i < CollectionUtils.size(endPointSettingsList); i++) {
                List<String> list = getListFromEndPointSetting(endPointSettingsList.get(i));
                arrays.add(list);
            }
        }
        return arrays;
    }

    public static List<String> getListFromTitratorEndPoints(TitratorEndPoint point) {
        List<String> list = new ArrayList<>();
        list.add(String.valueOf(point.getEndPointValue()));
        list.add(String.valueOf(point.getPreControlvalue()));
        list.add(String.valueOf(point.getCorrelationCoefficient()));
        list.add(String.valueOf(point.getResultUnit()));
        return list;
    }

    public static List<String> getListFromEndPointSetting(EndPointSetting point) {
        List<String> list = new ArrayList<>();
        list.add(String.valueOf(point.getBurette()));
        list.add(String.valueOf(point.getReagentName()));
        list.add(String.valueOf(point.getReagentConcentration()).concat(String.valueOf(point.getReagentConcentrationUnit())));
        list.add(String.valueOf(point.getAddVolume()));
        list.add(String.valueOf(point.getAddSpeed()));
        list.add(String.valueOf(point.getAddTime()));
        list.add(String.valueOf(point.getReferenceEndPoint()));
        list.add(String.valueOf(point.getDelayTime()));
        return list;
    }

    // TODO: 2020-05-05 need to change English
    public static List<String> getChineseEndPointBar() {
        boolean isChinese = true;
        List<String> barList = new ArrayList<>();
        if (isChinese) {
            barList.add("终点值");
            barList.add("预控值");
            barList.add("相关系数");
            barList.add("测试结构单位");
        } else {
            barList.add("终点值");
            barList.add("预控值");
            barList.add("相关系数");
            barList.add("测试结构单位");
        }
        return barList;
    }

    // TODO: 2020-05-16 need to change English
    public static List<String> getAuxiliaryReagent() {
        boolean isChinese = true;
        List<String> barList = new ArrayList<>();
        if (isChinese) {
            barList.add("滴定管");
            barList.add("试剂名称");
            barList.add("试剂浓度");
            barList.add("添加体积");
            barList.add("添加速度");
            barList.add("添加时间");
            barList.add("参考终点");
            barList.add("延时时间");
        } else {
            barList.add("终点值");
            barList.add("预控值");
            barList.add("相关系数");
            barList.add("测试结构单位");
        }
        return barList;
    }

    /**
     * 保存等量滴定终点滴定赋值: 终点滴定
     *
     * @param binding
     * @return
     */
    public static TitratorEndPoint saveTitratorEndPoint(TitratorMethodFragmentEndPointPopup binding) {
        TitratorEndPoint endPoint = (TitratorEndPoint) binding.getRoot().getTag();
        endPoint.setResultUnit(getUnitOne(binding.settingMethodPopupResultUnit.getSelectedItemPosition(), R.array.titrator_test_end_unit));
        return endPoint;
    }

    /**
     * 保存等量滴定辅助试剂赋值: 辅助试剂
     *
     * @param binding
     * @return
     */
    public static EndPointSetting saveEndPointSetting(TitratorMethodFragmentBurettePopup binding) {
        EndPointSetting setting = (EndPointSetting) binding.getRoot().getTag();
        // 滴定管 数据结构设计存在可优化点
//        int burette = Integer.parseInt(getUnitOne(binding.settingBurette.getSelectedItemPosition(), R.array.titrator_burette_volume_arrays));
        int burette = binding.settingBurette.getSelectedItemPosition();
        // 滴定名称
        String name = binding.settingReagenName.getText().toString();
        // 试剂浓度
        String reagentConcentrationContent = binding.settingReagenConcentration.getText().toString();
        double reagentConcentration = -1;
        try {
            if (StringUtils.isNotEmpty(reagentConcentrationContent)) {
                reagentConcentration = Double.parseDouble(reagentConcentrationContent);
            }
        } catch (Exception ex) {

        }
        // 试剂浓度单位
        String reagentConcentrationUnit = getUnitOne(binding.settingReagenConcentrationUnit.getSelectedItemPosition(), R.array.titrator_theoretical_concentration_unit);
        // 添加体积
        double addVolume = -1;
        String addVolumeContent = binding.settingAddVolume.getText().toString();
        try {
            if (StringUtils.isNotEmpty(addVolumeContent)) {
                addVolume = Double.parseDouble(addVolumeContent);
            }
        } catch (Exception ex) {

        }
        // 添加速度
        int addSpeed = Integer.parseInt(getUnitOne(binding.settingAddSpeed.getSelectedItemPosition(), R.array.number_speed));
        // 添加时间
        String time = getUnitOne(binding.settingAddTime.getSelectedItemPosition(), R.array.titrator_time);
        // 参考终点
        int referenceEndPoint = Integer.parseInt(getUnitOne(binding.settingReferenceEndPoint.getSelectedItemPosition(), R.array.number_0_5));
        // 延迟时间
        String delayTimeContent = binding.settingDelayTime.getText().toString();
        int delayTime = -1;
        try {
            if (StringUtils.isNotEmpty(delayTimeContent)) {
                delayTime = Integer.parseInt(delayTimeContent);
            }
        } catch (Exception ex) {

        }
        setting.setBurette(burette);
        setting.setReagentName(name);
        setting.setReagentConcentration(reagentConcentration);
        setting.setReagentConcentrationUnit(reagentConcentrationUnit);
        setting.setAddVolume(addVolume);
        setting.setAddSpeed(addSpeed);
        setting.setAddTime(time);
        setting.setReferenceEndPoint(referenceEndPoint);
        setting.setDelayTime(delayTime);
        return setting;
    }

    /**
     * 保存等量滴定参数赋值: 所有参数
     *
     * @param bean
     * @param binding
     * @return
     */
    public static TitratorParamsBean getEqualsTitratorParamsBean(TitratorParamsBean bean, TitratorSettingMethodFragmentPopupBinding binding) {
        // 滴定管体积
        double buretteVolume = binding.titratorBuretteVolume.getSelectedItemPosition();
        // 工作电极
        WorkElectrodeEnnum electrodeEnnum = getWorkElectrodeEnnum(binding.titratorMethodElectrode.getSelectedItemPosition());
        // 参比电极
        double referenceElectrode = Double.parseDouble(getUnitOne(binding.titratorReferenceElectrode.getSelectedItemPosition(), R.array.titrator_reference_electrode_arrays));
        // 样品计量单位
        String sampleMeasurementUnit = getUnitOne(binding.titratorSampleMeasurementUnit.getSelectedItemPosition(), R.array.titrator_sample_measurement_unit_arryas);
        // 滴定显示单位
        String titrationDisplayUnit = getUnitOne(binding.titrationDisplayUnit.getSelectedItemPosition(), R.array.titrator_display_unit_arrays);
        // 补液速度
        String replenishmentSpeed = getUnitOne(binding.titratorReplenishmentSpeed.getSelectedItemPosition(), R.array.titrator_speed_arrays);
        // 搅拌速度
        String stiringSpeed = getUnitOne(binding.titratorStiringSpeed.getSelectedItemPosition(), R.array.titrator_speed_arrays);
        // 理论浓度
        String theoreticalConcentrationUnit = getUnitOne(binding.titratorMainTheoreticalConcentrationUnit.getSelectedItemPosition(), R.array.titrator_theoretical_concentration_unit);
        MainTitrant mainTitrant = bean.getMainTitrant();
        if (mainTitrant == null) {
            mainTitrant = new MainTitrant();
        }
        TitratorMethod method = bean.getTitratorMethod();
        if (method == null) {
            method = new TitratorMethod();
        }
        method.setBuretteVolume(buretteVolume);
        method.setWorkingElectrode(electrodeEnnum);
        method.setReferenceElectrode(referenceElectrode);
        method.setSampleMeasurementUnit(sampleMeasurementUnit);
        method.setTitrationDisplayUnit(titrationDisplayUnit);
        method.setReplenishmentSpeed(replenishmentSpeed);
        method.setStiringSpeed(stiringSpeed);
        mainTitrant.setTheoreticalConcentrationUnit(theoreticalConcentrationUnit);
        bean.setMainTitrant(mainTitrant);
        bean.setTitratorMethod(method);
        return bean;
    }

    public static String getUnitOne(int position, int res) {
        String[] array = BaseApplication.getApplication().getResources().getStringArray(res);
        if (array != null && array.length > position) {
            return array[position];
        }
        return "";
    }

    public static WorkElectrodeEnnum getWorkElectrodeEnnum(int position) {
        WorkElectrodeEnnum electrodeEnnum = null;
        String content = getUnitOne(position, R.array.working_electrode_arrays);
        if (content.equals(PH_Electrode.getName())) {
            electrodeEnnum = PH_Electrode;
        } else if (content.equals(PH_Mixed_Electrode.getName())) {
            electrodeEnnum = PH_Mixed_Electrode;
        } else if (content.equals(Platinum_Electrode.getName())) {
            electrodeEnnum = Platinum_Electrode;
        } else if (content.equals(Silver_Electrode.getName())) {
            electrodeEnnum = Silver_Electrode;
        } else if (content.equals(Calcium_Electrode.getName())) {
            electrodeEnnum = Calcium_Electrode;
        } else if (content.equals(Fluoride_Electrode.getName())) {
            electrodeEnnum = Fluoride_Electrode;
        } else if (content.equals(Copper_Electrode.getName())) {
            electrodeEnnum = Copper_Electrode;
        }
        return electrodeEnnum;
    }

}
