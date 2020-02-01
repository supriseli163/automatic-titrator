package com.jh.automatic_titrator.ui.user;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.common.Cache;

/**
 * Created by apple on 16/9/17.
 */
public class UserFragment extends Fragment implements View.OnClickListener {

    private FragmentManager fragmentManager;

    private UserSettingFragment userSettingFragment;

    private UserManagerFragment userManagerFragment;

    private View settingLayout;

    private TextView settingTv;

    private View managerLayout;

    private TextView managerTv;

    private Handler activityHandler;

    public void setActivityHandler(Handler activityHandler) {
        this.activityHandler = activityHandler;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_fragment, container, false);

        fragmentManager = getFragmentManager();

        settingLayout = view.findViewById(R.id.user_setting_layout);
        settingTv = (TextView) view.findViewById(R.id.user_setting_txt);
        settingLayout.setOnClickListener(this);

        managerLayout = view.findViewById(R.id.user_manager_layout);
        managerTv = (TextView) view.findViewById(R.id.user_manager_txt);
        managerLayout.setOnClickListener(this);

        showDefaultFragment();
        ensureFragmentShow();
        return view;
    }

    private void ensureFragmentShow() {
        if (!Cache.containsAuth("usermanager")) {
            managerLayout.setVisibility(View.GONE);
        }
    }

    private void showDefaultFragment() {
        clearSelection();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideFragments(fragmentTransaction);
        changeToSettingFragment();
        userSettingFragment = new UserSettingFragment();
        userSettingFragment.setActivityHandler(activityHandler);
        fragmentTransaction.add(R.id.user_frame, userSettingFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View v) {
        clearSelection();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideFragments(fragmentTransaction);
        switch (v.getId()) {
            case R.id.user_setting_layout:
                changeToSettingFragment();
                if (userSettingFragment == null) {
                    userSettingFragment = new UserSettingFragment();
                    userSettingFragment.setActivityHandler(activityHandler);
                    fragmentTransaction.add(R.id.user_frame, userSettingFragment);
                } else {
                    fragmentTransaction.show(userSettingFragment);
                }
                break;
            case R.id.user_manager_layout:
                changeToManagerFragment();
                if (userManagerFragment == null) {
                    userManagerFragment = new UserManagerFragment();
                    fragmentTransaction.add(R.id.user_frame, userManagerFragment);
                } else {
                    fragmentTransaction.show(userManagerFragment);
                }
                break;
        }
        fragmentTransaction.commit();
    }

    private void changeToSettingFragment() {
        settingLayout.setBackground(getResources().getDrawable(R.drawable.top_tab_cur));
        settingTv.setTextColor(getResources().getColor(R.color.colorWrite));
        settingTv.getPaint().setFakeBoldText(true);
    }

    private void changeToManagerFragment() {
        managerLayout.setBackground(getResources().getDrawable(R.drawable.top_tab_cur));
        managerTv.setTextColor(getResources().getColor(R.color.colorWrite));
        managerTv.getPaint().setFakeBoldText(true);
    }

    private void clearSelection() {
        settingLayout.setBackground(getResources().getDrawable(R.drawable.top_tab));
        settingTv.setTextColor(getResources().getColor(R.color.JH_333333));
        settingTv.getPaint().setFakeBoldText(false);

        managerLayout.setBackground(getResources().getDrawable(R.drawable.top_tab));
        managerTv.setTextColor(getResources().getColor(R.color.JH_333333));
        managerTv.getPaint().setFakeBoldText(false);
    }

    private void hideFragments(FragmentTransaction fragmentTransaction) {
        if (userSettingFragment != null) {
            fragmentTransaction.hide(userSettingFragment);
        }
        if (userManagerFragment != null) {
            fragmentTransaction.hide(userManagerFragment);
        }
    }
}
