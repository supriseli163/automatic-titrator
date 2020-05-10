package com.jh.automatic_titrator.ui.data.method;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.jh.automatic_titrator.BaseApplication;
import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.common.db.titrator.TitratorParamsBeanHelper;
import com.jh.automatic_titrator.common.utils.ViewUtils;
import com.jh.automatic_titrator.databinding.TitratorDataFragmentBinding;
import com.jh.automatic_titrator.entity.common.titrator.TitratorParamsBean;
import com.jh.automatic_titrator.entity.common.titrator.TitratorTypeEnum;
import com.jh.automatic_titrator.ui.base.BaseFragment;
import com.jh.automatic_titrator.ui.data.method.adapter.MethodListAdapter;
import com.jh.automatic_titrator.ui.data.method.view.ModifyMethodView;

import java.util.List;

public class ModifyMethodFragment extends BaseFragment<TitratorDataFragmentBinding> implements View.OnClickListener {

    private List<TitratorParamsBean> titratorParamsBeanList;
    private MethodListAdapter adapter;
    public TitratorTypeEnum titratorTypeEnum;
    private int pageNum, pageSize;

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
        TitratorParamsBeanHelper helper = new TitratorParamsBeanHelper();
        try {
            // TODO: 2020-05-01 这里count不对
//            pageSize=helper.countMethod();
            titratorParamsBeanList = helper.listMethodByType(titratorTypeEnum, pageNum, pageSize);
            if (titratorParamsBeanList != null) {
                Log.d("ModifyMethodFragment ", "titratorParamsBeanList : " + titratorParamsBeanList.size());
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
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
                removeSelectMethodEvent();
                break;
            case R.id.method_modify_btn:
                onClickModifyMethodEvent();
                break;
            case R.id.method_new_btn:
                switchMethodDetailView(true);
                break;
        }
    }

    private void onClickModifyMethodEvent() {
        TitratorParamsBean bean = adapter.getCurrentBean();
        if (bean == null) {
            showNoSelectMethodToast();
        } else {
            switchMethodDetailView(true);
            binding.modifyViewBg.setBean(bean);
        }
    }

    private void removeSelectMethodEvent() {
        TitratorParamsBeanHelper helper = new TitratorParamsBeanHelper();
        TitratorParamsBean bean = adapter.getCurrentBean();
        if (bean != null) {
            helper.deleteByTitratorMethodId(bean.getTitratorMethod().getId());
        } else {
            showNoSelectMethodToast();
        }
    }

    private void showNoSelectMethodToast() {
        Toast.makeText(activity, "请选择要修改的方法", Toast.LENGTH_LONG).show();
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
