package com.jh.automatic_titrator.ui.execute;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.databinding.FragmentTitratorTestLayoutBinding;
import com.jh.automatic_titrator.entity.common.titrator.TitratorMethod;
import com.jh.automatic_titrator.entity.test.TiratorExecuteViewBean;
import com.jh.automatic_titrator.factory.FunctionFactory;
import com.jh.automatic_titrator.ui.base.BaseFragment;
import com.jh.automatic_titrator.ui.execute.adapter.MenuAdapter;
import com.jh.automatic_titrator.ui.view.CircleMenuLayout;

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
        MenuAdapter adapter = new MenuAdapter(MenuAdapter.getTestData());
        binding.menuLayout.setAdapter(adapter);
        initMenuLayout();
        binding.menuLayout.setOnItemClickListener(new CircleMenuLayout.OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                Toast.makeText(activity, "点击了第" + (position + 1) + "个", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRefreshFinish() {

            }
        });
    }

    private void initMenuLayout() {
        binding.menuLayout.post(() -> {
            Log.d("ExecuteFragment", "initMenuLayout: ");
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) binding.textContent.getLayoutParams();
            layoutParams.width = (int) (binding.menuLayout.mInnerRadius * 4 / 5 * 2);
            layoutParams.height = (int) (binding.menuLayout.mInnerRadius * 4 / 5 * 2);
            binding.textContent.setLayoutParams(layoutParams);
            binding.textContent.setRTVRadius(binding.menuLayout.mInnerRadius * 4 / 5);
        });
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