package com.jh.automatic_titrator.ui.data.method;

import android.os.Bundle;
import android.os.Handler;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.common.Cache;
import com.jh.automatic_titrator.databinding.TitratorDataFragmentBinding;
import com.jh.automatic_titrator.entity.common.titrator.TitratorTypeEnum;
import com.jh.automatic_titrator.entity.method.TiratorExecuteMethodViewBean;
import com.jh.automatic_titrator.ui.base.BaseFragment;

public class ModifyMethodFragment extends BaseFragment<TitratorDataFragmentBinding> {

    private TiratorMethod tiratorMethod;

    @Override
    public void initView(Bundle savedInstanceState) {
        tiratorMethod = Cache.getTiratorMethod();
        initData();
        binding.setBean(tiratorMethod.tiratorExecuteMethodViewBean);
    }

    private void initData() {
        if (tiratorMethod == null) {
            tiratorMethod = new TiratorMethod();
            tiratorMethod.tiratorExecuteMethodViewBean = new TiratorExecuteMethodViewBean();
            tiratorMethod.tiratorExecuteMethodViewBean.setCurrentEnum(TitratorTypeEnum.EqualTitrator);
        }
    }

    @Override
    public int getResLayout() {
        return R.layout.titrator_data_fragment;
    }

    @Override
    public void setActivityHandler(Handler handler) {

    }
}
