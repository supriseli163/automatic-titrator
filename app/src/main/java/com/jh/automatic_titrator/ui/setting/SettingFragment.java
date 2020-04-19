package com.jh.automatic_titrator.ui.setting;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.common.Cache;
import com.jh.automatic_titrator.common.db.UserHelper;
import com.jh.automatic_titrator.common.utils.ToastUtil;
import com.jh.automatic_titrator.databinding.SettingFragmentBinding;
import com.jh.automatic_titrator.entity.common.titrator.TitratorTypeEnum;
import com.jh.automatic_titrator.entity.method.TiratorExecuteMethodViewBean;
import com.jh.automatic_titrator.ui.BaseActivity;
import com.jh.automatic_titrator.ui.data.method.ModifyMethodFragment;
import com.jh.automatic_titrator.ui.data.method.TiratorMethod;
import com.jh.automatic_titrator.ui.listener.KeyboardDismiss;

import androidx.databinding.DataBindingUtil;

/**
 * Created by apple on 16/9/17.
 */
public class SettingFragment extends Fragment implements View.OnClickListener {

    private SettingFragmentBinding binding;
    private TiratorMethod tiratorMethod;
    private FragmentManager fragmentManager;

    private SettingAuditFragment settingAuditFragment;

    private ModifyMethodFragment modifyMethodFragment;

    private SettingCorrectingFragment settingCorrectingFragment;

    private SettingFormulaFragment settingFormulaFragment;

    private SettingInitialFragment settingInitialFragment;

    private SettingMethodFragment settingMethodFragment;

    private SettingNetWorkFragment settingNetWorkFragment;

    private SettingStandardFragment settingStandardFragment;

    private SettingAutographFragment settingAutographFragment;

    private SettingCloudsFragment settingCloudsFragment;

    private View auditLayout;

    private TextView auditTv;

    private View correctingLayout;

    private TextView correctingTv;

    private View formulaLayout;

    private TextView formulaTv;

    private View initialLayout;

    private TextView initialTv;

    private View methodLayout;

    private TextView methodTv;

    private View networkLayout;

    private TextView networkTv;

    private View standardLayout;

    private TextView standardTv;

    private View cloudsLayout;
    private TextView cloudsTv;

    private View autographLayout;
    private TextView autographTv;

    private Handler activityHandler;

    private SettingHandler handler;

    private UserHelper userHelper;

    private Fragment currentFragment;

    private Toast mToast;

    public void setActivityHandler(Handler activityHandler) {
        this.activityHandler = activityHandler;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_fragment, container, false);
        binding = DataBindingUtil.bind(view);
        initData();
        if (binding != null) {
            binding.titratorEqual.setOnClickListener(this);
            binding.titratorDynamic.setOnClickListener(this);
            binding.titratorManual.setOnClickListener(this);
            binding.titratorEndPoint.setOnClickListener(this);
            binding.titratorStopForver.setOnClickListener(this);
        }
        mToast = ToastUtil.createToast(getActivity());

        fragmentManager = getFragmentManager();

        auditLayout = view.findViewById(R.id.setting_audit_layout);
        auditTv = (TextView) view.findViewById(R.id.setting_audit_txt);
        auditLayout.setOnClickListener(this);

        correctingLayout = view.findViewById(R.id.setting_correcting_layout);
        correctingTv = (TextView) view.findViewById(R.id.setting_correcting_txt);
        correctingLayout.setOnClickListener(this);

        formulaLayout = view.findViewById(R.id.setting_formula_layout);
        formulaTv = (TextView) view.findViewById(R.id.setting_formula_txt);
        formulaLayout.setOnClickListener(this);

        initialLayout = view.findViewById(R.id.setting_initail_layout);
        initialTv = (TextView) view.findViewById(R.id.setting_initail_txt);
        initialLayout.setOnClickListener(this);

        methodLayout = view.findViewById(R.id.setting_method_layout);
        methodTv = (TextView) view.findViewById(R.id.setting_method_txt);
        methodLayout.setOnClickListener(this);

        networkLayout = view.findViewById(R.id.setting_network_layout);
        networkTv = (TextView) view.findViewById(R.id.setting_network_txt);
        networkLayout.setOnClickListener(this);

        standardLayout = view.findViewById(R.id.setting_standard_layout);
        standardTv = (TextView) view.findViewById(R.id.setting_standard_txt);
        standardLayout.setOnClickListener(this);

        cloudsLayout = view.findViewById(R.id.setting_clouds_layout);
        cloudsTv = (TextView) view.findViewById(R.id.setting_clouds_txt);
        cloudsLayout.setOnClickListener(this);

        autographLayout = view.findViewById(R.id.setting_autograph_layout);
        autographTv = (TextView) view.findViewById(R.id.setting_autograph_txt);
        autographLayout.setOnClickListener(this);

        handler = new SettingHandler();

        showDefaultFragment();
        ensureFragmentShow();
        return view;
    }

    private void initData() {
        if (tiratorMethod == null && binding != null) {
            tiratorMethod = new TiratorMethod();
            tiratorMethod.tiratorExecuteMethodViewBean = new TiratorExecuteMethodViewBean();
            tiratorMethod.tiratorExecuteMethodViewBean.setCurrentEnum(TitratorTypeEnum.EqualTitrator);
            binding.setBean(tiratorMethod.tiratorExecuteMethodViewBean);
        }
    }

    //这个函数什么意思 ，不懂
    private void ensureFragmentShow() {
        if (!Cache.containsAuth("network")) {
            networkLayout.setVisibility(View.GONE);
        }
        if (!Cache.containsAuth("recovery")) {
            initialLayout.setVisibility(View.GONE);
        }
        if (!Cache.containsAuth("correct")) {
            correctingLayout.setVisibility(View.GONE);
        }
    }

    private void showDefaultFragment() {
        clearSelection();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideFragments(fragmentTransaction);
        changeToStandardFragment();
        settingMethodFragment = new SettingMethodFragment();
        fragmentTransaction.add(R.id.setting_frame, settingMethodFragment);
        fragmentTransaction.commit();
    }

    private void clearSelection() {
        auditLayout.setBackground(getResources().getDrawable(R.drawable.top_tab_new));
        auditTv.setTextColor(getResources().getColor(R.color.JH_333333));
        auditTv.getPaint().setFakeBoldText(false);

        correctingLayout.setBackground(getResources().getDrawable(R.drawable.top_tab_new));
        correctingTv.setTextColor(getResources().getColor(R.color.JH_333333));
        correctingTv.getPaint().setFakeBoldText(false);

        formulaLayout.setBackground(getResources().getDrawable(R.drawable.top_tab_new));
        formulaTv.setTextColor(getResources().getColor(R.color.JH_333333));
        formulaTv.getPaint().setFakeBoldText(false);

        initialLayout.setBackground(getResources().getDrawable(R.drawable.top_tab_new));
        initialTv.setTextColor(getResources().getColor(R.color.JH_333333));
        initialTv.getPaint().setFakeBoldText(false);

        methodLayout.setBackground(getResources().getDrawable(R.drawable.top_tab_new));
        methodTv.setTextColor(getResources().getColor(R.color.JH_333333));
        methodTv.getPaint().setFakeBoldText(false);

        networkLayout.setBackground(getResources().getDrawable(R.drawable.top_tab_new));
        networkTv.setTextColor(getResources().getColor(R.color.JH_333333));
        networkTv.getPaint().setFakeBoldText(false);

        standardLayout.setBackground(getResources().getDrawable(R.drawable.top_tab_new));
        standardTv.setTextColor(getResources().getColor(R.color.JH_333333));
        standardTv.getPaint().setFakeBoldText(false);

        cloudsLayout.setBackground(getResources().getDrawable(R.drawable.top_tab_new));
        cloudsTv.setTextColor(getResources().getColor(R.color.JH_333333));
        cloudsTv.getPaint().setFakeBoldText(false);

        autographLayout.setBackground(getResources().getDrawable(R.drawable.top_tab_new));
        autographTv.setTextColor(getResources().getColor(R.color.JH_333333));
        autographTv.getPaint().setFakeBoldText(false);
    }

    private void hideFragments(FragmentTransaction fragmentTransaction) {
        binding.titratorTestFunctionBg.setVisibility(View.GONE);
        if (modifyMethodFragment != null) {
            fragmentTransaction.hide(modifyMethodFragment);
        }
        if (settingAuditFragment != null) {
            fragmentTransaction.hide(settingAuditFragment);
        }
        if (settingCorrectingFragment != null) {
            fragmentTransaction.hide(settingCorrectingFragment);
        }
        if (settingFormulaFragment != null) {
            fragmentTransaction.hide(settingFormulaFragment);
        }
        if (settingInitialFragment != null) {
            fragmentTransaction.hide(settingInitialFragment);
        }
        if (settingMethodFragment != null) {
            fragmentTransaction.hide(settingMethodFragment);
        }
        if (settingNetWorkFragment != null) {
            fragmentTransaction.hide(settingNetWorkFragment);
        }
        if (settingStandardFragment != null) {
            fragmentTransaction.hide(settingStandardFragment);
        }
        if (settingCloudsFragment != null) {
            fragmentTransaction.hide(settingCloudsFragment);
        }
        if (settingAutographFragment != null) {
            fragmentTransaction.hide(settingAutographFragment);
        }
    }

    private void changeToAuditFragment() {
        auditLayout.setBackground(getResources().getDrawable(R.drawable.top_tab_cur_new));
        auditTv.setTextColor(getResources().getColor(R.color.colorWrite));
        auditTv.getPaint().setFakeBoldText(true);
    }

    private void changeToCorrectingFragment() {
        correctingLayout.setBackground(getResources().getDrawable(R.drawable.top_tab_cur_new));
        correctingTv.setTextColor(getResources().getColor(R.color.colorWrite));
        correctingTv.getPaint().setFakeBoldText(true);
    }

    private void changeToFormulaFragment() {
        formulaLayout.setBackground(getResources().getDrawable(R.drawable.top_tab_cur_new));
        formulaTv.setTextColor(getResources().getColor(R.color.colorWrite));
        formulaTv.getPaint().setFakeBoldText(true);
    }

    private void changeToInitailFragment() {
        initialLayout.setBackground(getResources().getDrawable(R.drawable.top_tab_cur_new));
        initialTv.setTextColor(getResources().getColor(R.color.colorWrite));
        initialTv.getPaint().setFakeBoldText(true);
    }

    private void changeToMethodFragment() {
        binding.titratorTestFunctionBg.setVisibility(View.VISIBLE);
        methodLayout.setBackground(getResources().getDrawable(R.drawable.top_tab_cur_new));
        methodTv.setTextColor(getResources().getColor(R.color.colorWrite));
        methodTv.getPaint().setFakeBoldText(true);
    }

    private void changeToNetworkFragment() {
        networkLayout.setBackground(getResources().getDrawable(R.drawable.top_tab_cur_new));
        networkTv.setTextColor(getResources().getColor(R.color.colorWrite));
        networkTv.getPaint().setFakeBoldText(true);
    }

    private void changeToStandardFragment() {
        standardLayout.setBackground(getResources().getDrawable(R.drawable.top_tab_cur_new));
        standardTv.setTextColor(getResources().getColor(R.color.colorWrite));
        standardTv.getPaint().setFakeBoldText(true);
    }

    private void changeToCloudsFragment() {
        cloudsLayout.setBackground(getResources().getDrawable(R.drawable.top_tab_cur_new));
        cloudsTv.setTextColor(getResources().getColor(R.color.colorWrite));
        cloudsTv.getPaint().setFakeBoldText(true);
    }

    private void changeToAutographFragment() {
        autographLayout.setBackground(getResources().getDrawable(R.drawable.top_tab_cur_new));
        autographTv.setTextColor(getResources().getColor(R.color.colorWrite));
        autographTv.getPaint().setFakeBoldText(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_initail_layout:
                passwordPass(v);
                break;
            case R.id.setting_autograph_layout:
                passwordPass(v);
                break;
            case R.id.setting_correcting_layout:
                if (Cache.isTesting()) {
                    ToastUtil.toastShow(mToast, getString(R.string.testing_delete_unlimit));
                    break;
                }
            case R.id.setting_audit_layout:
            case R.id.setting_formula_layout:
            case R.id.setting_method_layout:
            case R.id.setting_network_layout:
            case R.id.setting_standard_layout:
            case R.id.setting_clouds_layout:
                changeFragment(v);
                break;
            case R.id.titrator_equal:
                updateCurrentMethod(TitratorTypeEnum.EqualTitrator);
                break;
            case R.id.titrator_dynamic:
                updateCurrentMethod(TitratorTypeEnum.DynamicTitrator);
                break;
            case R.id.titrator_manual:
                updateCurrentMethod(TitratorTypeEnum.ManualTitrator);
                break;
            case R.id.titrator_end_point:
                updateCurrentMethod(TitratorTypeEnum.EndPointTitrator);
                break;
            case R.id.titrator_stop_forver:
                updateCurrentMethod(TitratorTypeEnum.StopForverTitrator);
                break;
        }
    }

    private void changeFragment(View v) {
        clearSelection();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideFragments(fragmentTransaction);
        switch (v.getId()) {
            case R.id.setting_audit_layout:
                changeToAuditFragment();
                if (settingAuditFragment == null) {
                    settingAuditFragment = new SettingAuditFragment();
                    fragmentTransaction.add(R.id.setting_frame, settingAuditFragment);
                } else {
                    fragmentTransaction.show(settingAuditFragment);
                }
                currentFragment = settingAuditFragment;
                break;
            case R.id.setting_correcting_layout:
                changeToCorrectingFragment();
                if (settingCorrectingFragment == null) {
                    settingCorrectingFragment = new SettingCorrectingFragment();
                    fragmentTransaction.add(R.id.setting_frame, settingCorrectingFragment);
                } else {
                    fragmentTransaction.show(settingCorrectingFragment);
                }

                currentFragment = settingCorrectingFragment;

                break;
            case R.id.setting_formula_layout:
                changeToFormulaFragment();
                if (settingFormulaFragment == null) {
                    settingFormulaFragment = new SettingFormulaFragment();
                    fragmentTransaction.add(R.id.setting_frame, settingFormulaFragment);
                } else {
                    fragmentTransaction.show(settingFormulaFragment);
                }

                currentFragment = settingFormulaFragment;

                break;
            case R.id.setting_initail_layout:
                changeToInitailFragment();
                if (settingInitialFragment == null) {
                    settingInitialFragment = new SettingInitialFragment();
                    fragmentTransaction.add(R.id.setting_frame, settingInitialFragment);
                } else {
                    fragmentTransaction.show(settingInitialFragment);
                }

                currentFragment = settingInitialFragment;

                break;
            case R.id.setting_method_layout:
                changeToMethodFragment();
                if (modifyMethodFragment == null) {
                    modifyMethodFragment = new ModifyMethodFragment();
                    fragmentTransaction.add(R.id.setting_frame, modifyMethodFragment);
                } else {
                    fragmentTransaction.show(modifyMethodFragment);
                }

                currentFragment = modifyMethodFragment;

                break;
            case R.id.setting_network_layout:
                changeToNetworkFragment();
                if (settingNetWorkFragment == null) {
                    settingNetWorkFragment = new SettingNetWorkFragment();
                    fragmentTransaction.add(R.id.setting_frame, settingNetWorkFragment);
                } else {
                    fragmentTransaction.show(settingNetWorkFragment);
                }

                currentFragment = settingNetWorkFragment;

                break;
            case R.id.setting_standard_layout:
                changeToStandardFragment();
                if (settingStandardFragment == null) {
                    settingStandardFragment = new SettingStandardFragment();
                    fragmentTransaction.add(R.id.setting_frame, settingStandardFragment);
                } else {
                    fragmentTransaction.show(settingStandardFragment);
                }
                currentFragment = settingStandardFragment;

                break;

            case R.id.setting_clouds_layout:
                changeToCloudsFragment();
                if (settingCloudsFragment == null) {
                    settingCloudsFragment = new SettingCloudsFragment();
                    fragmentTransaction.add(R.id.setting_frame, settingCloudsFragment);
                } else {
                    fragmentTransaction.show(settingCloudsFragment);
                }

                currentFragment = settingCloudsFragment;

                break;
            case R.id.setting_autograph_layout:
                changeToAutographFragment();
                if (settingAutographFragment == null) {
                    settingAutographFragment = new SettingAutographFragment();
                    fragmentTransaction.add(R.id.setting_frame, settingAutographFragment);
                } else {
                    fragmentTransaction.show(settingAutographFragment);
                }
                currentFragment = settingAutographFragment;

                break;
        }
        fragmentTransaction.commit();
    }

    private void passwordPass(final View v) {
        if (Cache.isTesting()) {
            Message.obtain(handler, BaseActivity.SHOW_MSG, "正在测试，不能进行校正或出厂设置").sendToTarget();
            return;
        }
        final View passwordInputView = LayoutInflater.from(getActivity()).inflate(R.layout.input_password, null);

        final EditText password = (EditText) passwordInputView.findViewById(R.id.input_password_et);

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle(getString(R.string.prompt));
        builder.setMessage(getString(R.string.password));
        builder.setView(passwordInputView);
        builder.setPositiveButton(this.getActivity().getString(R.string.submit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (password.getText().toString().equals(UserHelper.SUPER_PASSWORD)) {
                    changeFragment(v);
                } else {
                    ToastUtil.toastShow(mToast, getString(R.string.password_error));
                }
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(this.getActivity().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            builder.setOnDismissListener(new KeyboardDismiss(getActivity()));
        }
        builder.create().show();
    }

    private void updateCurrentMethod(TitratorTypeEnum typeEnum) {
        tiratorMethod.tiratorExecuteMethodViewBean.setCurrentEnum(typeEnum);
        modifyMethodFragment.updateCurrentMethod(typeEnum);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            showDefaultFragment();
        }
        if (settingCorrectingFragment != null) {
            settingCorrectingFragment.onHiddenChanged(hidden);
        }
    }

    public class SettingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BaseActivity.SHOW_MSG:
                    if (msg.obj != null) {
                        ToastUtil.toastShow(mToast, String.valueOf(msg.obj));
                    }
                    break;
            }
        }
    }
}