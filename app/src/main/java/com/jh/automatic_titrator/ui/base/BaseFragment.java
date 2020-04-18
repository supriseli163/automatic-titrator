package com.jh.automatic_titrator.ui.base;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

public abstract class BaseFragment<T extends ViewDataBinding> extends Fragment {

    public T binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getResLayout(), container, false);
        initView(savedInstanceState);
        return binding.getRoot();
    }

    public abstract void initView(Bundle savedInstanceState);

    public abstract int getResLayout();

    public abstract void setActivityHandler(Handler handler);
}