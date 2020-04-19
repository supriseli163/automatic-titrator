package com.jh.automatic_titrator.ui.data.method;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.common.Cache;
import com.jh.automatic_titrator.databinding.TitratorDataFragmentBinding;
import com.jh.automatic_titrator.entity.common.titrator.TitratorTypeEnum;
import com.jh.automatic_titrator.entity.method.TiratorExecuteMethodViewBean;
import com.jh.automatic_titrator.ui.base.BaseFragment;

public class ModifyMethodFragment extends BaseFragment<TitratorDataFragmentBinding> implements View.OnClickListener {

    private TiratorMethod tiratorMethod;

    @Override
    public void initView(Bundle savedInstanceState) {
        tiratorMethod = Cache.getTiratorMethod();
        initData();
        initView();
    }

    private void initData() {
        if (tiratorMethod == null) {
            tiratorMethod = new TiratorMethod();
            tiratorMethod.tiratorExecuteMethodViewBean = new TiratorExecuteMethodViewBean();
            tiratorMethod.tiratorExecuteMethodViewBean.setCurrentEnum(TitratorTypeEnum.EqualTitrator);
            binding.setBean(tiratorMethod.tiratorExecuteMethodViewBean);
        }
    }

    private void initView() {
        binding.titratorEqual.setOnClickListener(this);
        binding.titratorDynamic.setOnClickListener(this);
        binding.titratorManual.setOnClickListener(this);
        binding.titratorEndPoint.setOnClickListener(this);
        binding.titratorStopForver.setOnClickListener(this);
    }

    @Override
    public int getResLayout() {
        return R.layout.titrator_data_fragment;
    }

    @Override
    public void setActivityHandler(Handler handler) {

    }

    private void updateCurrentMethod(TitratorTypeEnum typeEnum) {
        tiratorMethod.tiratorExecuteMethodViewBean.setCurrentEnum(typeEnum);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titrator_equal:
                updateCurrentMethod(TitratorTypeEnum.EqualTitrator);
                break;
            case R.id.titrator_dynamic:
                updateCurrentMethod(TitratorTypeEnum.DynamicTitrator);
                break;
            case R.id.titrator_manual:
                updateCurrentMethod(TitratorTypeEnum.ManualTitrator);
                break;
            case R.id.titrator_end_point:
                updateCurrentMethod(TitratorTypeEnum.EndPointTitrator);
                break;
            case R.id.titrator_stop_forver:
                updateCurrentMethod(TitratorTypeEnum.StopForverTitrator);
                break;
        }
    }
}
