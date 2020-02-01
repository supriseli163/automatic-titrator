package com.jh.automatic_titrator.ui.test;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.common.Cache;
import com.jh.automatic_titrator.common.Observer;
import com.jh.automatic_titrator.ui.BaseActivity;

/**
 * Created by apple on 16/9/17.
 */
public class TestFragment extends Fragment implements View.OnClickListener, Observer {

    private FragmentManager fragmentManager;

    private TestTestFragment testTestFragment;

    private TestSettingFragment testSettingFragment;

    private View testLayout;

    private TextView testTv;

    private View settingLayout;

    private TextView settingTv;

    private Handler activityHandler;

    private TestHandler handler;

    public void setActivityHandler(Handler activityHandler) {
        this.activityHandler = activityHandler;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test_fragment, container, false);

        fragmentManager = getFragmentManager();
        handler = new TestHandler();

        testLayout = view.findViewById(R.id.test_test_layout);
        testTv = (TextView) view.findViewById(R.id.test_test_txt);
        testLayout.setOnClickListener(this);

        settingLayout = view.findViewById(R.id.test_setting_layout);
        if (!Cache.containsAuth("paramsetting")) {
            settingLayout.setVisibility(View.GONE);
        }
        settingTv = (TextView) view.findViewById(R.id.test_setting_txt);
        settingLayout.setOnClickListener(this);

        showDefaultFragment();

        return view;
    }

    private void showDefaultFragment() {
        clearSelection();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideFragments(fragmentTransaction);
        changeToTestFragment();
        testTestFragment = new TestTestFragment();
        fragmentTransaction.add(R.id.test_frame, testTestFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View v) {
        clearSelection();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideFragments(fragmentTransaction);
        switch (v.getId()) {
            case R.id.test_test_layout:
                changeToTestFragment();
                if (testTestFragment == null) {
                    testTestFragment = new TestTestFragment();
                    fragmentTransaction.add(R.id.test_frame, testTestFragment);
                } else {
                    fragmentTransaction.show(testTestFragment);
                }
                break;
            case R.id.test_setting_layout:
                changeToSettingFragment();
                if (testSettingFragment == null) {
                    testSettingFragment = new TestSettingFragment();
                    if (testTestFragment != null) {
                        testSettingFragment.addObserver(testTestFragment);
                        testSettingFragment.addObserver(this);
                    }
                    fragmentTransaction.add(R.id.test_frame, testSettingFragment);

                } else {
                    fragmentTransaction.show(testSettingFragment);
                }
                break;
        }
        fragmentTransaction.commit();
    }

    private void changeToTestFragment() {
        testLayout.setBackground(getResources().getDrawable(R.drawable.top_tab_cur));
        testTv.setTextColor(getResources().getColor(R.color.colorWrite));
        testTv.getPaint().setFakeBoldText(true);
    }

    private void changeToSettingFragment() {
        settingLayout.setBackground(getResources().getDrawable(R.drawable.top_tab_cur));
        settingTv.setTextColor(getResources().getColor(R.color.colorWrite));
        settingTv.getPaint().setFakeBoldText(true);
    }

    private void clearSelection() {
        testLayout.setBackground(getResources().getDrawable(R.drawable.top_tab));
        testTv.setTextColor(getResources().getColor(R.color.JH_333333));
        testTv.getPaint().setFakeBoldText(false);

        settingLayout.setBackground(getResources().getDrawable(R.drawable.top_tab));
        settingTv.setTextColor(getResources().getColor(R.color.JH_333333));
        settingTv.getPaint().setFakeBoldText(false);
    }

    private void hideFragments(FragmentTransaction fragmentTransaction) {
        if (testTestFragment != null) {
            fragmentTransaction.hide(testTestFragment);
        }
        if (testSettingFragment != null) {
            fragmentTransaction.hide(testSettingFragment);
        }
    }

    @Override
    public void notifyDataChanged(int sig) {
        Message.obtain(handler, BaseActivity.NEED_REFRESH).sendToTarget();
    }

    public class TestHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BaseActivity.NEED_REFRESH:
                    clearSelection();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    hideFragments(fragmentTransaction);

                    changeToTestFragment();
                    if (testTestFragment == null) {
                        testTestFragment = new TestTestFragment();
                        fragmentTransaction.add(R.id.test_frame, testTestFragment);
                    } else {
                        fragmentTransaction.show(testTestFragment);
                    }

                    fragmentTransaction.commit();
                    break;
            }
        }
    }
}
