package com.jh.automatic_titrator.ui.data.method.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.common.db.titrator.TitratorParamsBeanHelper;
import com.jh.automatic_titrator.common.utils.StringUtils;
import com.jh.automatic_titrator.databinding.TitratorSettingMethodFragmentPopupBinding;
import com.jh.automatic_titrator.entity.common.titrator.TitratorEndPoint;
import com.jh.automatic_titrator.entity.common.titrator.TitratorParamsBean;

import java.util.List;

import static com.jh.automatic_titrator.common.utils.TitratorParamsBeanUtils.getTitratorEndList;
import static com.jh.automatic_titrator.common.utils.ViewUtils.setTextViewColor;

public class ModifyMethodView extends RelativeLayout {
    private TitratorSettingMethodFragmentPopupBinding binding;
    private OnModifyMethodOperateListener listener;
    private TitratorParamsBean bean;

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
                bean.getPreTitrant().setPreAfterstiringTime(Double.parseDouble(content));
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
        binding.setBean(bean);
        binding.titratorEndListLayout.setArraysList(getTitratorEndList(bean), new ParamsListItemView.OperateListener() {
            @Override
            public void onAddEvent() {
                // TODO: 2020-05-05 触发弹窗
//                TitratorParamsBeanHelper helper = new TitratorParamsBeanHelper();
//                helper.deleteByTitratorMethodId(bean.getTitratorMethod().getId());
//                helper.insert(bean);
            }

            @Override
            public void onModifyEvent(int position) {
                // TODO: 2020-05-05 触发弹窗修改内容
                TitratorParamsBeanHelper helper = new TitratorParamsBeanHelper();
                helper.deleteByTitratorMethodId(bean.getTitratorMethod().getId());
                helper.insert(bean);
            }

            @Override
            public void onDeleteEvent(int position) {
                List<TitratorEndPoint> endPoints = bean.getTitratorEndPoint();
                endPoints.remove(position);
                TitratorParamsBeanHelper helper = new TitratorParamsBeanHelper();
                helper.deleteByTitratorMethodId(bean.getTitratorMethod().getId());
                helper.insert(bean);
            }
        });
    }

    public interface OnModifyMethodOperateListener {
        void onClickBackToMethodListBtn();
    }
}