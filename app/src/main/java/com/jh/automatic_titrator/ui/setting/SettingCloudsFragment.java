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
 * Created by Administrator on 2018/2/20.
 */

public class SettingCloudsFragment extends Fragment {

    private View view;
    private View btn_save;
    private boolean showLogo;
    private EditText et_setting_clouds_httpaddr;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.setting_clouds_fragment, container, false);
        HomePageActivity homePageActivity = (HomePageActivity) getActivity();

        et_setting_clouds_httpaddr = (EditText) view.findViewById(R.id.setting_clouds_httpaddr);
        et_setting_clouds_httpaddr.setText(homePageActivity.readconf("http").isEmpty()?"https://www.jiahangchn.com":homePageActivity.readconf("http"));
        //et_setting_clouds_httpaddr.setText("https://www.jiahangchn.com");

        showLogo = homePageActivity.showLogo();
        if (showLogo) {
        } else {
            et_setting_clouds_httpaddr.setText(homePageActivity.readconf("http").isEmpty()?"":homePageActivity.readconf("http"));
        }

        btn_save = view.findViewById(R.id.setting_clouds_save_btn);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomePageActivity homePageActivity = (HomePageActivity) getActivity();
                homePageActivity.writeconf("http", et_setting_clouds_httpaddr.getText().toString());
                Toast.makeText(getActivity(), getString(R.string.save_success), Toast.LENGTH_SHORT).show();

            }
        });
        return view;
    }


}
