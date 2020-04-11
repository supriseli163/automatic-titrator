package com.jh.automatic_titrator.ui.execute;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.common.Observer;
import com.jh.automatic_titrator.service.ExecutorService;
import com.jh.automatic_titrator.ui.BaseActivity;
import com.jh.automatic_titrator.ui.test.TestFragment;

public class ExecuteFragment extends Fragment implements View.OnClickListener, Observer,
        CompoundButton.OnCheckedChangeListener {

    private FragmentManager fragmentManager;

    private ExecuteGraphFragment executeGraphFragment;

    private View executeLayout;

    private View testTv;

    private View settingLayout;

    private Handler activityHandler;

    private ExecuteHandler handler;

    public void setActivityHandler(Handler activityHandler) {
        this.activityHandler = activityHandler;
    }

    @Override
    public void onClick(View v) {
        clearSelection();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideFragments(fragmentTransaction);
        switch (v.getId()) {
            case R.id.test_
        }
    }

    private void doUpdate() {
        ExecutorService.getInstance().execute(
                new Runnable() {
                    @Override
                    public void run() {

                    }
                }
        );
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.execute_fragment, container, false);
        fragmentManager = getFragmentManager();
        handler = new ExecuteHandler();



    }

    @Override
    public void notifyDataChanged(int sig) {

    }

    private void showDefaultFragment() {
        clearSelection();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

    }

    public class ExecuteHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BaseActivity.NEED_REFRESH:

            }
            super.handleMessage(msg);
        }
    }

    private void clearSelection() {

    }

    private void hideFragments(FragmentTransaction fragmentTransaction) {
//        if(executeLayout != null) {
//            fragmentTransaction.hide(fragmentTransaction);
//        }
//        if(executeLayout != null) {
//            fragmentTransaction.hide()
//        }
    }
}
