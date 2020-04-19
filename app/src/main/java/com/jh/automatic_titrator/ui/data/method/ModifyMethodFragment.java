package com.jh.automatic_titrator.ui.data.method;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.jh.automatic_titrator.BaseApplication;
import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.common.Cache;
import com.jh.automatic_titrator.databinding.TitratorDataFragmentBinding;
import com.jh.automatic_titrator.entity.common.titrator.TitratorParamsBean;
import com.jh.automatic_titrator.entity.common.titrator.TitratorTypeEnum;
import com.jh.automatic_titrator.ui.base.BaseFragment;
import com.jh.automatic_titrator.ui.data.method.adapter.MethodListAdapter;

import java.util.ArrayList;
import java.util.List;

public class ModifyMethodFragment extends BaseFragment<TitratorDataFragmentBinding> implements View.OnClickListener {

    private TiratorMethod tiratorMethod;
    private List<TitratorParamsBean> titratorParamsBeanList;
    private MethodListAdapter adapter;

    @Override
    public void initView(Bundle savedInstanceState) {
        tiratorMethod = Cache.getTiratorMethod();
        initView();
    }

    private void initData() {
        // TODO: 2020-04-19 根据当前数据类型这里从数据库获取
        titratorParamsBeanList = new ArrayList<>();
    }

    private void initView() {
        adapter = new MethodListAdapter(BaseApplication.getApplication());
        initMetHodListView();
    }

    private void initMetHodListView(){
        initData();
        adapter.setParamsBeanList(titratorParamsBeanList);
        binding.dataManagerLv.setAdapter(adapter);
    }

    @Override
    public int getResLayout() {
        return R.layout.titrator_data_fragment;
    }

    @Override
    public void setActivityHandler(Handler handler) {

    }

    public void updateCurrentMethod(TitratorTypeEnum typeEnum) {
        initData();
        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
}
