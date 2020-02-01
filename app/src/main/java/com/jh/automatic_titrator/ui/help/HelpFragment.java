package com.jh.automatic_titrator.ui.help;

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

/**
 * Created by apple on 16/9/17.
 */
public class HelpFragment extends Fragment implements View.OnClickListener{

    private FragmentManager fragmentManager;

    private HelpMD5Fragment helpMD5Fragment;

    private HelpUpdateFragment helpUpdateFragment;

    private HelpInstructionFragment helpInstructionFragment;

    private View md5Layout;

    private TextView md5Tv;

    private View updateLayout;

    private TextView updateTv;

    private TextView instructionTv;

    private View instructionLayout;

    private Handler activityHandler;

    public void setActivityHandler(Handler activityHandler) {
        this.activityHandler = activityHandler;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.help_fragment, container, false);

        fragmentManager = getFragmentManager();

        md5Layout = view.findViewById(R.id.help_md5_layout);
        md5Tv = (TextView) view.findViewById(R.id.help_md5_txt);
        md5Layout.setOnClickListener(this);

        updateLayout = view.findViewById(R.id.help_update_layout);
        updateTv = (TextView) view.findViewById(R.id.help_update_txt);
        updateLayout.setOnClickListener(this);

        instructionLayout = view.findViewById(R.id.help_instruction_layout);
        instructionTv = (TextView) view.findViewById(R.id.help_instruction_txt);
        instructionLayout.setOnClickListener(this);

        showDefaultFragment();

        return view;
    }

    private void showDefaultFragment() {
        clearSelection();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideFragments(fragmentTransaction);
        changeToMd5Fragment();
        helpMD5Fragment = new HelpMD5Fragment();
        fragmentTransaction.add(R.id.help_frame, helpMD5Fragment);
        fragmentTransaction.commit();
    }

    private void changeToMd5Fragment() {
        md5Layout.setBackground(getResources().getDrawable(R.drawable.top_tab_cur));
        md5Tv.setTextColor(getResources().getColor(R.color.colorWrite));
        md5Tv.getPaint().setFakeBoldText(true);
    }

    private void changeToUpdateFragment() {
        updateLayout.setBackground(getResources().getDrawable(R.drawable.top_tab_cur));
        updateTv.setTextColor(getResources().getColor(R.color.colorWrite));
        updateTv.getPaint().setFakeBoldText(true);
    }

    private void changeToInstauctionFragment() {
        instructionLayout.setBackground(getResources().getDrawable(R.drawable.top_tab_cur));
        instructionTv.setTextColor(getResources().getColor(R.color.colorWrite));
        instructionTv.getPaint().setFakeBoldText(true);
    }


    private void clearSelection() {
        md5Layout.setBackground(getResources().getDrawable(R.drawable.top_tab));
        md5Tv.setTextColor(getResources().getColor(R.color.JH_333333));
        md5Tv.getPaint().setFakeBoldText(false);

        updateLayout.setBackground(getResources().getDrawable(R.drawable.top_tab));
        updateTv.setTextColor(getResources().getColor(R.color.JH_333333));
        updateTv.getPaint().setFakeBoldText(false);

        instructionLayout.setBackground(getResources().getDrawable(R.drawable.top_tab));
        instructionTv.setTextColor(getResources().getColor(R.color.JH_333333));
        instructionTv.getPaint().setFakeBoldText(false);
    }

    private void hideFragments(FragmentTransaction fragmentTransaction) {
        if (helpMD5Fragment != null) {
            fragmentTransaction.hide(helpMD5Fragment);
        }
        if (helpUpdateFragment != null) {
            fragmentTransaction.hide(helpUpdateFragment);
        }
        if (helpInstructionFragment != null) {
            fragmentTransaction.hide(helpInstructionFragment);
        }
    }

    @Override
    public void onClick(View v) {
        clearSelection();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideFragments(fragmentTransaction);
        switch (v.getId()) {
            case R.id.help_md5_layout:
                changeToMd5Fragment();
                if (helpMD5Fragment == null) {
                    helpMD5Fragment = new HelpMD5Fragment();
                    fragmentTransaction.add(R.id.help_frame, helpMD5Fragment);
                } else {
                    fragmentTransaction.show(helpMD5Fragment);
                }
                break;
            case R.id.help_update_layout:
                changeToUpdateFragment();
                if (helpUpdateFragment == null) {
                    helpUpdateFragment = new HelpUpdateFragment();
                    fragmentTransaction.add(R.id.help_frame, helpUpdateFragment);
                } else {
                    fragmentTransaction.show(helpUpdateFragment);
                }
                break;
            case R.id.help_instruction_layout:
                changeToInstauctionFragment();
                if (helpInstructionFragment == null) {
                    helpInstructionFragment = new HelpInstructionFragment();
                    fragmentTransaction.add(R.id.help_frame, helpInstructionFragment);
                } else {
                    fragmentTransaction.show(helpInstructionFragment);
                }
                break;
        }
        fragmentTransaction.commit();
    }
}
