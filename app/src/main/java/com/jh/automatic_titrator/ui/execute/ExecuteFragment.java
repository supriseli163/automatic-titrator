package com.jh.automatic_titrator.ui.execute;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.databinding.FragmentTitratorTestLayoutBinding;
import com.jh.automatic_titrator.entity.common.titrator.TitratorMethod;
import com.jh.automatic_titrator.entity.test.TiratorExecuteViewBean;
import com.jh.automatic_titrator.factory.FunctionFactory;
import com.jh.automatic_titrator.ui.base.BaseFragment;

public class ExecuteFragment extends BaseFragment<FragmentTitratorTestLayoutBinding> implements View.OnClickListener {

    @Override
    public void initView(Bundle savedInstanceState) {
        TiratorExecuteViewBean bean = new TiratorExecuteViewBean();
        TitratorMethod titratorMethod = new TitratorMethod();
        binding.setBean(bean);
        binding.setMethod(titratorMethod);
        onTiratorTestTranceClick();
        binding.titratorTestEntrance.setOnClickListener(this);
        binding.titratorSampleEntrance.setOnClickListener(this);
        binding.titratorAtlasSwitchBtn.setOnClickListener(this);
        binding.titratorOperateStartBtn.setOnClickListener(this);
        binding.titratorOperateStopBtn.setOnClickListener(this);
    }

    @Override
    public int getResLayout() {
        return R.layout.fragment_titrator_test_layout;
    }

    @Override
    public void setActivityHandler(Handler handler) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titrator_test_entrance:
                onTiratorTestTranceClick();
                break;
            case R.id.titrator_sample_entrance:
                onTiratorSampleTranceClick();
                break;
            case R.id.titrator_atlas_switch_btn:
                onTitratorAtlasSwitch();
                break;
            case R.id.titrator_operate_start_btn:
                onStartTest();
                break;
            case R.id.titrator_operate_stop_btn:
                onStopTest();
                break;
        }
    }

    private void onStopTest() {
        FunctionFactory.stop();
    }

    private void onStartTest() {
        TiratorExecuteViewBean bean = binding.getBean();
        if (bean != null) {
            checkEnableClick();
            bean.updateEnable();
        }
        FunctionFactory.start();
    }

    private void checkEnableClick() {
        Log.d("clickListen:", "checkEnableClick");
    }

    private void onTitratorAtlasSwitch() {
        TiratorExecuteViewBean bean = binding.getBean();
        if (bean != null) {
            bean.updateAtlasStatus();
        }
    }

    private void onTiratorTestTranceClick() {
        TiratorExecuteViewBean bean = binding.getBean();
        if (bean != null) {
            bean.setTestTabSelect();
        }
    }

    private void onTiratorSampleTranceClick() {
        TiratorExecuteViewBean bean = binding.getBean();
        if (bean != null) {
            bean.setTestSampleSelect();
        }
    }
}