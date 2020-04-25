package com.jh.automatic_titrator.ui.data.method;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.jh.automatic_titrator.BaseApplication;
import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.common.utils.ViewUtils;
import com.jh.automatic_titrator.databinding.TitratorDataFragmentBinding;
import com.jh.automatic_titrator.entity.common.titrator.TitratorParamsBean;
import com.jh.automatic_titrator.entity.common.titrator.TitratorTypeEnum;
import com.jh.automatic_titrator.ui.base.BaseFragment;
import com.jh.automatic_titrator.ui.data.method.adapter.MethodListAdapter;
import com.jh.automatic_titrator.ui.data.method.view.ModifyMethodView;

import java.util.ArrayList;
import java.util.List;

public class ModifyMethodFragment extends BaseFragment<TitratorDataFragmentBinding> implements View.OnClickListener {

    private List<TitratorParamsBean> titratorParamsBeanList;
    private MethodListAdapter adapter;
    public TitratorTypeEnum titratorTypeEnum;

    public static ModifyMethodFragment getInstance(TitratorTypeEnum titratorTypeEnum) {
        ModifyMethodFragment fragment = new ModifyMethodFragment();
        fragment.titratorTypeEnum = titratorTypeEnum;
        return fragment;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        initView();
    }

    private void initData() {
        // TODO: 2020-04-19 根据当前数据类型这里从数据库获取
        titratorParamsBeanList = new ArrayList<>();
    }

    private void initView() {
        adapter = new MethodListAdapter(BaseApplication.getApplication());
        initMetHodListView();
        binding.methodModifyBtn.setOnClickListener(this);
        binding.methodNewBtn.setOnClickListener(this);
        binding.methodDeleteBtn.setOnClickListener(this);
    }

    private void initMetHodListView() {
        initData();
        switchMethodDetailView(false);
        adapter.setParamsBeanList(titratorParamsBeanList);
        binding.dataManagerLv.setAdapter(adapter);
        binding.methodNewBtn.setOnClickListener(this);
        binding.methodModifyBtn.setOnClickListener(this);
        binding.methodDeleteBtn.setOnClickListener(this);
        binding.modifyViewBg.setListener(new ModifyMethodView.OnModifyMethodOperateListener() {
            @Override
            public void onClickBackToMethodListBtn() {
                switchMethodDetailView(false);
            }
        });
    }

    @Override
    public int getResLayout() {
        return R.layout.titrator_data_fragment;
    }

    @Override
    public void setActivityHandler(Handler handler) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.method_delete_btn:
                break;
            case R.id.method_modify_btn:
                break;
            case R.id.method_new_btn:
                switchMethodDetailView(true);
                break;
        }
    }

    /**
     * 切换编辑方法、新建方法视图
     *
     * @param modify
     */
    private void switchMethodDetailView(boolean modify) {
        ViewUtils.setViewVisibleOrGone(binding.modifyViewBg, modify);
        ViewUtils.setViewVisibleOrGone(binding.methodListBg, !modify);
    }
}
