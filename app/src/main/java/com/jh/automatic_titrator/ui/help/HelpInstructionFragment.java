package com.jh.automatic_titrator.ui.help;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.ui.HomePageActivity;

/**
 * Created by apple on 16/9/24.
 * 4008089310
 */
public class HelpInstructionFragment extends Fragment {

    private TextView tv_tel;
    private boolean showLogo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.help_instruction_fragment, container, false);
        Log.i("testSetting", "start");

        HomePageActivity homePageActivity = (HomePageActivity) getActivity();
        showLogo = homePageActivity.showLogo();
        tv_tel = (TextView) view.findViewById(R.id.tv_tel);
        //mLogo1 = findViewById(R.id.home_page_logo1);
        if (showLogo) {
            tv_tel.setVisibility(View.VISIBLE);
            //mLogo1.setVisibility(View.VISIBLE);
        } else {
            tv_tel.setVisibility(View.GONE);
            //mLogo1.setVisibility(View.GONE);
        }

        return view;
    }
}
