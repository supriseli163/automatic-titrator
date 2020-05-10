package com.jh.automatic_titrator.entity.common.titrator;

import com.jh.automatic_titrator.BaseApplication;
import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.common.utils.StringUtils;

import java.util.List;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

/**
 * 滴定方法
 */
public class TitratorMethod extends BaseObservable {

    //测试id
    private int id;
    //滴定类型
    private String titratorType;
    //方法名
    private String methodName;
    //滴定管体积
    private double buretteVolume;
    //工作电级
    private WorkElectrodeEnnum workingElectrode;
    //参比电级(参考电极)
    private double referenceElectrode;
    //样品计量单位
    private String sampleMeasurementUnit;
    //滴定显示单位
    private String titrationDisplayUnit;
    //补液速度
    private String replenishmentSpeed;
    //搅拌速度
    private String stiringSpeed;
    //电极平衡时间
    private String electroedEquilibrationTime;
    //电极平衡电位
    private String electroedEquilibriumPotential;
    //预搅拌时间
    private String preStiringTime;
    //每次添加体积
    private String perAddVolume;
    //结束体积
    private String endVolume;
    //滴定速度
    private int titrationSpeed;
    //慢滴体积
    private String slowTitrationVolume;
    //快滴体积
    private String fastTitrationVolume;

    //滴定终点
    private List<TitratorEndPoint> titratorEndPoints;
    //修改时间
    private String modifyTime;
    //修改人
    private String userName;

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
//        this.modifyTime = StringUtils.getCurrentTime();
        this.modifyTime = modifyTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String modifyTime) {
//        this.userName = StringUtils.getCurrentUserName();
        this.userName = userName;
    }

    public List<TitratorEndPoint> getTitratorEndPoints() {
        return titratorEndPoints;
    }

    public void setTitratorEndPoints(List<TitratorEndPoint> titratorEndPoints) {
        this.titratorEndPoints = titratorEndPoints;
    }

    @Bindable
    public String getTitratorTypeText() {
        return StringUtils.getContent(R.string.titrator_type_text, String.valueOf(titratorType));
    }

    @Bindable
    public String getBuretteVolumeText() {
        // TODO: 2020-04-12 单位
        return StringUtils.getContent(R.string.burette_volume_text, String.valueOf(buretteVolume));
    }

    @Bindable
    public String getMethodNameText() {
        return StringUtils.getContent(R.string.method_name_text, methodName);
    }

    @Bindable
    public String getWorkingElectrodeText() {
        if (workingElectrode != null) {
            return StringUtils.getContent(R.string.working_electrode_text, String.valueOf(workingElectrode.getDesc()));
        } else {
            return "";
        }
    }

    @Bindable
    public String getReferenceElectrodeText() {
        return StringUtils.getContent(R.string.reference_electrode_text, String.valueOf(referenceElectrode));
    }

    @Bindable
    public String getSampleMeasurementUnitText() {
        return StringUtils.getContent(R.string.sample_measurement_unit_text, sampleMeasurementUnit);
    }

    @Bindable
    public String getTitrationDisplayUnitText() {
        return StringUtils.getContent(R.string.titration_display_unit_text, titrationDisplayUnit);
    }

    @Bindable
    public String getReplenishmentSpeedText() {
        return StringUtils.getContent(R.string.replenishment_speed_text, replenishmentSpeed);
    }

    @Bindable
    public String getStiringSpeedText() {
        return StringUtils.getContent(R.string.stiring_speed_text, stiringSpeed);
    }

    @Bindable
    public String getElectroedEquilibrationTimeText() {
        return StringUtils.getContent(R.string.electroed_equilibration_time_text, electroedEquilibrationTime);
    }

    @Bindable
    public String getElectroedEquilibriumPotentialText() {
        return StringUtils.getContent(R.string.electroed_equilibrium_potential_text, electroedEquilibriumPotential);
    }

    @Bindable
    public String getPreStiringTimeText() {
        return StringUtils.getContent(R.string.prestiring_time_text, preStiringTime);
    }

    @Bindable
    public String getPerAddVolumeText() {
        return StringUtils.getContent(R.string.per_addvolume_text, perAddVolume);
    }

    @Bindable
    public String getEndVolumeText() {
        return StringUtils.getContent(R.string.end_volume_text, endVolume);
    }

    @Bindable
    public String getTitrationSpeedText() {
        return StringUtils.getContent(R.string.titration_speed_text, String.valueOf(titrationSpeed));
    }

    @Bindable
    public String getSlowTitrationVolumeText() {
        return StringUtils.getContent(R.string.fast_titration_volume_text, slowTitrationVolume);
    }

    @Bindable
    public String getFastTitrationVolumeText() {
        return StringUtils.getContent(R.string.fast_titration_volume_text, fastTitrationVolume);
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setTitratorType(String titratorType) {
        this.titratorType = titratorType;
        notifyPropertyChanged(com.jh.automatic_titrator.BR.titratorTypeText);
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
        notifyPropertyChanged(com.jh.automatic_titrator.BR.methodNameText);
    }

    public void setBuretteVolume(double buretteVolume) {
        this.buretteVolume = buretteVolume;
        notifyPropertyChanged(com.jh.automatic_titrator.BR.buretteVolumeText);
    }

    public void setWorkingElectrode(String content) {
        if (WorkElectrodeEnnum.Calcium_Electrode.getDesc().equals(content)) {
            this.workingElectrode = WorkElectrodeEnnum.Calcium_Electrode;
        } else if (WorkElectrodeEnnum.Copper_Electrode.getDesc().equals(content)) {
            this.workingElectrode = WorkElectrodeEnnum.Copper_Electrode;
        } else if (WorkElectrodeEnnum.PH_Electrode.getDesc().equals(content)) {
            this.workingElectrode = WorkElectrodeEnnum.PH_Electrode;
        } else if (WorkElectrodeEnnum.Platinum_Electrode.getDesc().equals(content)) {
            this.workingElectrode = WorkElectrodeEnnum.Platinum_Electrode;
        } else if (WorkElectrodeEnnum.Fluoride_Electrode.getDesc().equals(content)) {
            this.workingElectrode = WorkElectrodeEnnum.Fluoride_Electrode;
        } else if (WorkElectrodeEnnum.PH_Mixed_Electrode.getDesc().equals(content)) {
            this.workingElectrode = WorkElectrodeEnnum.PH_Mixed_Electrode;
        } else if (WorkElectrodeEnnum.Silver_Electrode.getDesc().equals(content)) {
            this.workingElectrode = WorkElectrodeEnnum.Silver_Electrode;
        }
        notifyPropertyChanged(com.jh.automatic_titrator.BR.workingElectrodeText);
    }

    public void setReferenceElectrode(double referenceElectrode) {
        this.referenceElectrode = referenceElectrode;
        notifyPropertyChanged(com.jh.automatic_titrator.BR.referenceElectrodeText);
    }

    public void setReferenceElectrode(String content) {
        String[] arrays = BaseApplication.getApplication().getResources().getStringArray(R.array.titrator_reference_electrode_arrays);
        if (arrays[0].equals(content)) {
            this.referenceElectrode = 0;
        } else if (arrays[1].equals(content)) {
            this.referenceElectrode = 1;
        } else if (arrays[2].equals(content)) {
            this.referenceElectrode = 2;
        }
        notifyPropertyChanged(com.jh.automatic_titrator.BR.referenceElectrodeText);
    }

    public void setSampleMeasurementUnit(String sampleMeasurementUnit) {
        this.sampleMeasurementUnit = sampleMeasurementUnit;
        notifyPropertyChanged(com.jh.automatic_titrator.BR.sampleMeasurementUnitText);
    }

    public void setTitrationDisplayUnit(String titrationDisplayUnit) {
        this.titrationDisplayUnit = titrationDisplayUnit;
        notifyPropertyChanged(com.jh.automatic_titrator.BR.titrationDisplayUnitText);
    }

    public void setReplenishmentSpeed(String replenishmentSpeed) {
        this.replenishmentSpeed = replenishmentSpeed;
        notifyPropertyChanged(com.jh.automatic_titrator.BR.replenishmentSpeedText);
    }

    public void setStiringSpeed(String stiringSpeed) {
        this.stiringSpeed = stiringSpeed;
        notifyPropertyChanged(com.jh.automatic_titrator.BR.stiringSpeedText);
    }

    public void setElectroedEquilibrationTime(String electroedEquilibrationTime) {
        this.electroedEquilibrationTime = electroedEquilibrationTime;
        notifyPropertyChanged(com.jh.automatic_titrator.BR.electroedEquilibrationTimeText);
    }

    public void setElectroedEquilibriumPotential(String electroedEquilibriumPotential) {
        this.electroedEquilibriumPotential = electroedEquilibriumPotential;
        notifyPropertyChanged(com.jh.automatic_titrator.BR.electroedEquilibriumPotentialText);

    }

    public void setPreStiringTime(String preStiringTime) {
        this.preStiringTime = preStiringTime;
        notifyPropertyChanged(com.jh.automatic_titrator.BR.preStiringTimeText);

    }

    public void setPerAddVolume(String perAddVolume) {
        this.perAddVolume = perAddVolume;
        notifyPropertyChanged(com.jh.automatic_titrator.BR.perAddVolumeText);
    }

    public void setEndVolume(String endVolume) {
        this.endVolume = endVolume;
        notifyPropertyChanged(com.jh.automatic_titrator.BR.endVolumeText);
    }

    public void setTitrationSpeed(int titrationSpeed) {
        this.titrationSpeed = titrationSpeed;
        notifyPropertyChanged(com.jh.automatic_titrator.BR.titrationSpeedText);
    }

    public void setSlowTitrationVolume(String slowTitrationVolume) {
        this.slowTitrationVolume = slowTitrationVolume;
        notifyPropertyChanged(com.jh.automatic_titrator.BR.slowTitrationVolumeText);
    }

    public void setFastTitrationVolume(String fastTitrationVolume) {
        this.fastTitrationVolume = fastTitrationVolume;
        notifyPropertyChanged(com.jh.automatic_titrator.BR.fastTitrationVolumeText);
    }

    public int getId() {
        return id;
    }

    public String getTitratorType() {
        return titratorType;
    }

    public String getMethodName() {
        return methodName;
    }

    public double getBuretteVolume() {
        return buretteVolume;
    }

    public String getWorkingElectrode() {
        if (workingElectrode == null) {
            return "";
        }
        return workingElectrode.getDesc();
    }

    public WorkElectrodeEnnum getWorkElectrodeEnnum() {
        return workingElectrode;
    }

    public double getReferenceElectrode() {
        return referenceElectrode;
    }

    public String getSampleMeasurementUnit() {
        return sampleMeasurementUnit;
    }

    public String getTitrationDisplayUnit() {
        return titrationDisplayUnit;
    }

    public String getReplenishmentSpeed() {
        return replenishmentSpeed;
    }

    public String getStiringSpeed() {
        return stiringSpeed;
    }

    public String getElectroedEquilibrationTime() {
        return electroedEquilibrationTime;
    }

    public String getElectroedEquilibriumPotential() {
        return electroedEquilibriumPotential;
    }

    public String getPreStiringTime() {
        return preStiringTime;
    }

    public String getPerAddVolume() {
        return perAddVolume;
    }

    public String getEndVolume() {
        return endVolume;
    }

    public int getTitrationSpeed() {
        return titrationSpeed;
    }

    public String getSlowTitrationVolume() {
        return slowTitrationVolume;
    }

    public String getFastTitrationVolume() {
        return fastTitrationVolume;
    }
}
