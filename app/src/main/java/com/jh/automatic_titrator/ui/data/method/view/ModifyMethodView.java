package com.jh.automatic_titrator.ui.data.method.view;

import android.app.AlertDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jh.automatic_titrator.BaseApplication;
import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.common.utils.SoftKeyboardUtil;
import com.jh.automatic_titrator.common.utils.StringUtils;
import com.jh.automatic_titrator.databinding.TitratorMethodFragmentBurettePopup;
import com.jh.automatic_titrator.databinding.TitratorMethodFragmentEndPointPopup;
import com.jh.automatic_titrator.databinding.TitratorSettingMethodFragmentPopupBinding;
import com.jh.automatic_titrator.entity.common.titrator.EndPointSetting;
import com.jh.automatic_titrator.entity.common.titrator.TitratorEndPoint;
import com.jh.automatic_titrator.entity.common.titrator.TitratorParamsBean;

import java.util.ArrayList;
import java.util.List;

import androidx.databinding.DataBindingUtil;

import static com.jh.automatic_titrator.common.utils.TitratorParamsBeanUtils.getAuxiliaryReagentList;
import static com.jh.automatic_titrator.common.utils.TitratorParamsBeanUtils.getListFromTitratorEndPoints;
import static com.jh.automatic_titrator.common.utils.TitratorParamsBeanUtils.getTitratorEndList;
import static com.jh.automatic_titrator.common.utils.ViewUtils.setTextViewColor;

public class ModifyMethodView extends RelativeLayout {
    private TitratorSettingMethodFragmentPopupBinding binding;
    private OnModifyMethodOperateListener listener;
    private TitratorParamsBean bean;
    private AlertDialog settingDialog,dialog2;
    private boolean isCreate;

    public ModifyMethodView(Context context) {
        this(context, null);
    }

    public ModifyMethodView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ModifyMethodView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        binding = TitratorSettingMethodFragmentPopupBinding.inflate(inflater, this, true);
        binding.returnMethodListBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClickBackToMethodListBtn();
                }
            }
        });
        // 方法名
        binding.titratorMethodName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = s != null ? s.toString() : "";
                checkBean();
                bean.getTitratorMethod().setMethodName(content);
            }
        });
        // 滴定管体积
        binding.titratorBuretteVolume.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                checkBean();
                bean.getTitratorMethod().setBuretteVolume(parent.getItemIdAtPosition(position));
                setTextViewColor(R.color.fontBlack, (TextView) view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        // 工作电极
        binding.titratorMethodElectrode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                checkBean();
                String content = parent.getItemAtPosition(position).toString();
                bean.getTitratorMethod().setWorkingElectrode(content);
                setTextViewColor(R.color.fontBlack, (TextView) view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // 参比电极
        binding.titratorReferenceElectrode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                checkBean();
                String content = parent.getItemAtPosition(position).toString();
                bean.getTitratorMethod().setReferenceElectrode(content);
                setTextViewColor(R.color.fontBlack, (TextView) view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // 样品计量单位
        binding.titratorSampleMeasurementUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                checkBean();
                String content = parent.getItemAtPosition(position).toString();
                bean.getTitratorMethod().setSampleMeasurementUnit(content);
                setTextViewColor(R.color.fontBlack, (TextView) view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // 滴定显示单位
        binding.titrationDisplayUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                checkBean();
                String content = parent.getItemAtPosition(position).toString();
                bean.getTitratorMethod().setSampleMeasurementUnit(content);
                setTextViewColor(R.color.fontBlack, (TextView) view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // 补液速度
        binding.titratorReplenishmentSpeed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                checkBean();
                String content = parent.getItemAtPosition(position).toString();
                bean.getTitratorMethod().setReplenishmentSpeed(content);
                setTextViewColor(R.color.fontBlack, (TextView) view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // 搅拌速度
        binding.titratorStiringSpeed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                checkBean();
                String content = parent.getItemAtPosition(position).toString();
                bean.getTitratorMethod().setStiringSpeed(content);
                setTextViewColor(R.color.fontBlack, (TextView) view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // 电极平衡时间
        binding.titratorElectroedEquilibrationTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkBean();
                String content = s != null ? s.toString() : "";
                content = StringUtils.isEmpty(content) ? "0" : content;
                bean.getTitratorMethod().setElectroedEquilibrationTime(content);
            }
        });
        // 电极平衡电位
        binding.titratorEletroedEquilibriumPotential.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkBean();
                String content = s != null ? s.toString() : "";
                bean.getTitratorMethod().setElectroedEquilibriumPotential(content);
            }
        });
        // 预搅拌时间
        binding.titratorPreStiringTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = s != null ? s.toString() : "";
                bean.getTitratorMethod().setPreStiringTime(content);
            }
        });
        // 每次添加体积
        binding.titratorPerAddVolume.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkBean();
                String content = s != null ? s.toString() : "";
                bean.getTitratorMethod().setPerAddVolume(content);
            }
        });
        // 结束体积
        binding.titratorEndVolume.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkBean();
                String content = s != null ? s.toString() : "";
                bean.getTitratorMethod().setEndVolume(content);
            }
        });
        // 滴定速度
        binding.titrationSpeed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                checkBean();
                String content = parent.getItemAtPosition(position).toString();
                bean.getTitratorMethod().setStiringSpeed(content);
                setTextViewColor(R.color.fontBlack, (TextView) view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // 慢滴体积
        binding.titratorSlowVolume.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkBean();
                String content = s != null ? s.toString() : "";
                bean.getTitratorMethod().setSlowTitrationVolume(content);
            }
        });
        // 快滴体积
        binding.titratorFastTitrationVolume.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkBean();
                String content = s != null ? s.toString() : "";
                bean.getTitratorMethod().setFastTitrationVolume(content);
            }
        });
        // 预滴定后搅拌时间
        binding.titratorPreAfterStiringTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkBean();
                String content = s != null ? s.toString() : "0";
                bean.getPreTitrant().setPreAfterStiringTime(Double.parseDouble(content));
            }
        });
        // 预滴定添加体积
        binding.titratorPreAddVolume.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkBean();
                String content = s != null ? s.toString() : "0";
                bean.getPreTitrant().setPreAddVolume(Double.parseDouble(content));
            }
        });
        // 主滴定剂名称
        binding.titratorMainReagentName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkBean();
                String content = s != null ? s.toString() : "";
                bean.getMainTitrant().setReagentName(content);
            }
        });
        // 主滴定剂理论浓度
        binding.titratorMainTheoreticalConcentration.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        // 主滴定剂理论浓度
        binding.titratorMainTheoreticalConcentrationUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                checkBean();
                String content = parent.getItemAtPosition(position).toString();
//                bean.getMainTitrant().setTheoreticalConcentration(content);
                // todo设置理论单位
                setTextViewColor(R.color.fontBlack, (TextView) view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void checkBean() {
        if (bean == null) {
            bean = new TitratorParamsBean();
        }
    }

    public void setListener(OnModifyMethodOperateListener listener) {
        this.listener = listener;
    }

    public void setBean(TitratorParamsBean bean) {
        // 刷新方法数据
        this.bean = bean;
        binding.setBean(bean);
        binding.titratorExtraParamsListLayout.setArraysList(getAuxiliaryReagentList(bean), new ParamsListItemView.OperateListener() {
            @Override
            public void onAddEvent(int position) {
                showBuretteDialog(null,position);
            }

            @Override
            public void onModifyEvent(int position) {

            }

            @Override
            public void onDeleteEvent(int position) {

            }
        });
        binding.titratorEndListLayout.setArraysList(getTitratorEndList(bean), new ParamsListItemView.OperateListener() {
            @Override
            public void onAddEvent(int position) {
                isCreate = true;
                showEndPointDialog(null, position);
                // TODO: 2020-05-05 触发弹窗
//                TitratorParamsBeanHelper helper = new TitratorParamsBeanHelper();
//                helper.deleteByTitratorMethodId(bean.getTitratorMethod().getId());
//                helper.insert(bean);
            }

            @Override
            public void onModifyEvent(int position) {
                // TODO: 2020-05-05 触发弹窗修改内容
                isCreate = false;
                TitratorEndPoint point = bean.getTitratorEndPoint().get(position);
                showEndPointDialog(point, position);
//                TitratorParamsBeanHelper helper = new TitratorParamsBeanHelper();
//                helper.deleteByTitratorMethodId(bean.getTitratorMethod().getId());
//                helper.insert(bean);
            }

            @Override
            public void onDeleteEvent(int position) {
                List<TitratorEndPoint> endPoints = bean.getTitratorEndPoint();
                endPoints.remove(position);
//                TitratorParamsBeanHelper helper = new TitratorParamsBeanHelper();
//                helper.deleteByTitratorMethodId(bean.getTitratorMethod().getId());
//                helper.insert(bean);
            }
        });
    }

    // 辅助试剂弹窗
    private void showBuretteDialog(EndPointSetting setting, final int position) {
        List<EndPointSetting> settingList = bean.getEndPointSettings();
        if (settingList == null) {
            settingList = new ArrayList<>();
        }
        if (setting == null) {
            setting = new EndPointSetting();
        }
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View mView = inflater.inflate(R.layout.tirator_method_fragment_burette_popup, null);
        TitratorMethodFragmentBurettePopup pointPopup = DataBindingUtil.bind(mView);
        pointPopup.settingMethodPopupCancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                settingDialog.dismiss();
            }
        });
        pointPopup.settingMethodPopupSaveButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                settingDialog.dismiss();
            }
        });
        // 设置参数
        // TODO: 2020-05-16 待补充
        pointPopup.getRoot().setTag(setting);
        AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext());
        builder2.setTitle("辅助试剂");
        if (settingDialog != null && settingDialog.isShowing()) {
            settingDialog.dismiss();
        }
        mView.measure(0, 0);
        int measuredWidth = mView.getMeasuredWidth();//测量得到的textview的宽
        settingDialog = builder2.create();
        WindowManager.LayoutParams params = settingDialog.getWindow().getAttributes();
        params.width = measuredWidth;
        settingDialog.getWindow().setAttributes(params);
        settingDialog.setView(mView);
        if (settingDialog != null && !settingDialog.isShowing()) {
            settingDialog.show();
        }
    }

    // 终点滴定设置弹窗
    private void showEndPointDialog(TitratorEndPoint point, final int position) {
        List<TitratorEndPoint> endPointList = bean.getTitratorEndPoint();
        if (endPointList == null) {
            endPointList = new ArrayList<>();
        }
        if (point == null) {
            point = new TitratorEndPoint();
        }
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View mView = inflater.inflate(R.layout.tirator_method_fragment_end_point_popup, null);

        TitratorMethodFragmentEndPointPopup pointPopup = DataBindingUtil.bind(mView);
        pointPopup.settingEndPointValue.setText(String.valueOf(point.getEndPointValue()));
        pointPopup.settingPreControlValue.setText(String.valueOf(point.getPreControlvalue()));
        pointPopup.settingMethodCorrelationCoefficient.setText(String.valueOf(point.getCorrelationCoefficient()));
        int index = getSelectIndex(point.getResultUnit(), R.array.titrator_test_end_unit);
        if (index < 0) {
//            pointPopup.settingMethodPopupResultUnit.getChildAt(0).setVisibility(INVISIBLE);
        } else {
//            pointPopup.settingMethodPopupResultUnit.getChildAt(0).setVisibility(VISIBLE);
            pointPopup.settingMethodPopupResultUnit.setSelection(index);
        }
        pointPopup.getRoot().setTag(point);
        pointPopup.settingEndPointValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = s != null ? s.toString() : "";
                double value = 0;
                try {
                    value = Double.parseDouble(content);
                } catch (Exception ex) {

                }
                ((TitratorEndPoint) pointPopup.getRoot().getTag()).setEndPointValue(value);
            }
        });
        pointPopup.settingPreControlValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = s != null ? s.toString() : "";
                double value = 0;
                try {
                    value = Double.parseDouble(content);
                } catch (Exception ex) {

                }
                ((TitratorEndPoint) pointPopup.getRoot().getTag()).setPreControlvalue(value);
            }
        });
        pointPopup.settingMethodCorrelationCoefficient.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = s != null ? s.toString() : "";
                double value = 0;
                try {
                    value = Double.parseDouble(content);
                } catch (Exception ex) {

                }
                ((TitratorEndPoint) pointPopup.getRoot().getTag()).setCorrelationCoefficient(value);
            }
        });
        pointPopup.settingMethodPopupResultUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TitratorEndPoint) pointPopup.getRoot().getTag()).setResultUnit(String.valueOf(parent.getItemIdAtPosition(position)));
                setTextViewColor(R.color.fontWhite, (TextView) view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        pointPopup.settingMethodPopupSaveButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TitratorEndPoint point1 = ((TitratorEndPoint) pointPopup.getRoot().getTag());
                bean.getTitratorEndPoint().add(point1);
                if (isCreate) {
                    binding.titratorEndListLayout.addItemData(getListFromTitratorEndPoints(point1), position);
                } else {
                    binding.titratorEndListLayout.modifyItem(getListFromTitratorEndPoints(point1), position);
                }
                dialog2.cancel();
                SoftKeyboardUtil.hideSoftKeyboard();
            }
        });
        pointPopup.settingMethodPopupCancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.cancel();
            }
        });
        AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext());
        builder2.setTitle("滴定终点");
        if (dialog2 != null && dialog2.isShowing()) {
            dialog2.dismiss();
        }
        mView.measure(0, 0);
        int measuredWidth = mView.getMeasuredWidth();//测量得到的textview的宽
        dialog2 = builder2.create();
        WindowManager.LayoutParams params = dialog2.getWindow().getAttributes();
        params.width = measuredWidth;
        dialog2.getWindow().setAttributes(params);
        dialog2.setView(mView);
        if (dialog2 != null && !dialog2.isShowing()) {
            dialog2.show();
        }
    }

    private int getSelectIndex(String value, int resId) {
        String[] array = BaseApplication.getApplication().getResources().getStringArray(resId);
        for (int i = 0; i < array.length; i++) {
            String content = array[i];
            if (content != null && content.equals(value)) {
                return i;
            }
        }
        return 0;
    }

    public interface OnModifyMethodOperateListener {
        void onClickBackToMethodListBtn();
    }


}