package com.jh.automatic_titrator.ui.method;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.jh.automatic_titrator.R;

public class MethodTabFragment extends Fragment implements View.OnClickListener {

    private View saveButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.titrator_method_fragemnt_detail, container, false);
        saveButton = view.findViewById(R.id.setting_method_alter_save_button);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onClick(View v) {

    }
}
