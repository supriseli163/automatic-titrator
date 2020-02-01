package com.jh.automatic_titrator.ui.setting;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.ui.HomePageActivity;

/**
 * Created by Administrator on 2018/2/21.
 */

public class SettingAutographFragment extends Fragment {

    private View view;
    private View btn_save;
    private EditText et_autograph;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.setting_autograph_fragment, container, false);

        HomePageActivity homePageActivity = (HomePageActivity) getActivity();

        et_autograph = (EditText) view.findViewById(R.id.setting_autograph_name);
        et_autograph.setText(homePageActivity.readconf("autograph").isEmpty()?"":homePageActivity.readconf("autograph"));

        btn_save = view.findViewById(R.id.setting_autograhp_save_btn);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomePageActivity homePageActivity = (HomePageActivity) getActivity();
                homePageActivity.writeconf("autograph", et_autograph.getText().toString());
                Toast.makeText(getActivity(), getString(R.string.save_success), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
