package com.jh.automatic_titrator.ui.data.method.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.jh.automatic_titrator.databinding.TitratorSettingMethodFragmentPopupBinding;
import com.jh.automatic_titrator.entity.common.titrator.TitratorParamsBean;

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
        binding.titratorBuretteVolume.setOnItemClickListener((parent, view, position, id) -> {
            checkBean();
            bean.getTitratorMethod().setBuretteVolume(parent.getItemIdAtPosition(position));
        });
        // 工作电极
        binding.titratorMethodElectrode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                checkBean();
                String content = parent.getItemAtPosition(position).toString();
                bean.getTitratorMethod().setWorkingElectrode(content);
            }
        });
        // 参比电极
        binding.titratorReferenceElectrode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                checkBean();
                String content = parent.getItemAtPosition(position).toString();
                bean.getTitratorMethod().setReferenceElectrode(content);
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

    }

    public interface OnModifyMethodOperateListener {
        void onClickBackToMethodListBtn();
    }
}