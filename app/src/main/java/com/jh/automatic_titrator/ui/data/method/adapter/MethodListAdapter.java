package com.jh.automatic_titrator.ui.data.method.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.jh.automatic_titrator.BaseApplication;
import com.jh.automatic_titrator.common.utils.ToastUtil;
import com.jh.automatic_titrator.databinding.TitratorMethodFragmentItemBinding;
import com.jh.automatic_titrator.entity.common.titrator.TitratorParamsBean;

import java.util.List;

public class MethodListAdapter extends BaseAdapter {

    private List<TitratorParamsBean> paramsBeanList;

    private LayoutInflater inflate;
    private int currentPosition = -1;

    public MethodListAdapter(Context context) {
        this.inflate = LayoutInflater.from(context);
    }

    public List<TitratorParamsBean> getParamsBeanList() {
        return paramsBeanList;
    }

    public void setParamsBeanList(List<TitratorParamsBean> paramsBeanList) {
        this.paramsBeanList = paramsBeanList;
    }

    @Override
    public int getCount() {
        return paramsBeanList != null ? paramsBeanList.size() : 0;
    }

    @Override
    public TitratorParamsBean getItem(int position) {
        return position >= 0 && position < getCount() ? paramsBeanList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TitratorMethodFragmentItemBinding binding;
        if (convertView == null) {
            binding = TitratorMethodFragmentItemBinding.inflate(inflate, parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        } else {
            binding = (TitratorMethodFragmentItemBinding) convertView.getTag();
        }
        TitratorParamsBean bean = getItem(position);
        binding.settingMethodItemCb.setOnCheckedChangeListener(null);
        binding.settingMethodItemCb.setChecked(bean.isCheck());
        binding.settingMethodItemCb.setTag(position);
        binding.settingMethodItemCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int position = (int) binding.settingMethodItemCb.getTag();
                currentPosition = position;
                refreshCheckStatus(position);
            }
        });
        binding.settingMethodItemTestname.setText(bean.getMethodName());
        binding.endPointInfoShowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = ToastUtil.createToast(BaseApplication.getApplication());
                ToastUtil.toastShow(toast, "查看滴定终点");
            }
        });
        binding.auxiliaryReagentShowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = ToastUtil.createToast(BaseApplication.getApplication());
                ToastUtil.toastShow(toast, "查看方法试剂");
            }
        });
        return convertView;
    }

    private void refreshCheckStatus(int selectPosition) {
        for (int i = 0; i < getCount(); i++) {
            TitratorParamsBean bean = getItem(i);
            bean.setCheck(i == selectPosition);
        }
        notifyDataSetChanged();
    }

    public TitratorParamsBean getCurrentBean() {
        return getItem(currentPosition);
    }
}