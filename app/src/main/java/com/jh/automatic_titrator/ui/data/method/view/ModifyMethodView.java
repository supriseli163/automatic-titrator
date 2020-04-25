package com.jh.automatic_titrator.ui.data.method.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.jh.automatic_titrator.databinding.TitratorSettingMethodFragmentPopupBinding;

public class ModifyMethodView extends RelativeLayout {
    private TitratorSettingMethodFragmentPopupBinding binding;
    private OnModifyMethodOperateListener listener;

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
    }

    public void setListener(OnModifyMethodOperateListener listener) {
        this.listener = listener;
    }

    public interface OnModifyMethodOperateListener {
        void onClickBackToMethodListBtn();
    }
}