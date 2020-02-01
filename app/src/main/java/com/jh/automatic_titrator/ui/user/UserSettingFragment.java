package com.jh.automatic_titrator.ui.user;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.common.Cache;
import com.jh.automatic_titrator.common.db.AuditHelper;
import com.jh.automatic_titrator.common.db.UserHelper;
import com.jh.automatic_titrator.common.utils.StringUtils;
import com.jh.automatic_titrator.common.utils.ToastUtil;
import com.jh.automatic_titrator.entity.common.User;
import com.jh.automatic_titrator.service.AuditService;
import com.jh.automatic_titrator.service.DBService;
import com.jh.automatic_titrator.service.ExecutorService;
import com.jh.automatic_titrator.ui.BaseActivity;

/**
 * Created by apple on 16/9/24.
 */
public class UserSettingFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private Handler activityHandler;

    public void setActivityHandler(Handler activityHandler) {
        this.activityHandler = activityHandler;
    }

    private UserSettingHandler userSettingHandler;

    private UserHelper userHelper;

    private EditText oldPassword_et;
    private EditText newPassword_et1;
    private EditText newPassword_et2;

    private RadioButton head1;
    private RadioButton head2;
    private RadioButton head3;
    private RadioButton head4;

    private ImageView head;

    private View saveBtn;

    private User user;

    private int checkedHeadId;

    private CheckBox autoLogin_cb;

    private View logoutBtn;

    private AuditHelper auditHelper;

    private Toast mToast;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_setting_fragment, container, false);
        mToast = ToastUtil.createToast(getActivity());
        user = Cache.getUser();
        userHelper = DBService.getUserHelper(getActivity());
        auditHelper = DBService.getAuditHelper(getActivity());
        userSettingHandler = new UserSettingHandler();
        oldPassword_et = (EditText) view.findViewById(R.id.user_setting_old_password);
        newPassword_et1 = (EditText) view.findViewById(R.id.user_setting_new_password1);
        newPassword_et2 = (EditText) view.findViewById(R.id.user_setting_new_password2);
        head = (ImageView) view.findViewById(R.id.user_setting_head);
        head1 = (RadioButton) view.findViewById(R.id.user_setting_head1);
        head1.setChecked(false);
        head2 = (RadioButton) view.findViewById(R.id.user_setting_head2);
        head2.setChecked(false);
        head3 = (RadioButton) view.findViewById(R.id.user_setting_head3);
        head3.setChecked(false);
        head4 = (RadioButton) view.findViewById(R.id.user_setting_head4);
        head4.setChecked(false);
        autoLogin_cb = (CheckBox) view.findViewById(R.id.user_setting_autologin);
        String autoLoginUser = userHelper.getAutoLoginUser();
        autoLogin_cb.setChecked(autoLoginUser != null && autoLoginUser.equals(user.getUserName()));
        checkedHeadId = user.getHeadId();
        head.setImageResource(checkedHeadId);
        switch (checkedHeadId) {
            case R.drawable.head_portrait1:
                head1.setChecked(true);
                break;
            case R.drawable.head_portrait2:
                head2.setChecked(true);
                break;
            case R.drawable.head_portrait3:
                head3.setChecked(true);
                break;
            case R.drawable.head_portrait4:
                head4.setChecked(true);
                break;
        }
        head1.setOnCheckedChangeListener(this);
        head2.setOnCheckedChangeListener(this);
        head3.setOnCheckedChangeListener(this);
        head4.setOnCheckedChangeListener(this);

        saveBtn = view.findViewById(R.id.user_setting_save_btn);
        saveBtn.setOnClickListener(this);

        logoutBtn = view.findViewById(R.id.user_setting_logout_btn);
        logoutBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.user_setting_head1:
                if (isChecked) {
                    head2.setChecked(false);
                    head3.setChecked(false);
                    head4.setChecked(false);
                    checkedHeadId = R.drawable.head_portrait1;
                }
                break;
            case R.id.user_setting_head2:
                if (isChecked) {
                    head1.setChecked(false);
                    head3.setChecked(false);
                    head4.setChecked(false);
                    checkedHeadId = R.drawable.head_portrait2;
                }
                break;
            case R.id.user_setting_head3:
                if (isChecked) {
                    head1.setChecked(false);
                    head2.setChecked(false);
                    head4.setChecked(false);
                    checkedHeadId = R.drawable.head_portrait3;
                }
                break;
            case R.id.user_setting_head4:
                if (isChecked) {
                    head1.setChecked(false);
                    head2.setChecked(false);
                    head3.setChecked(false);
                    checkedHeadId = R.drawable.head_portrait4;
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_setting_save_btn:
                updateUser();
                break;
            case R.id.user_setting_logout_btn:
                doLogout();
                getActivity().finish();
                break;
        }
    }

    private void doLogout() {
        ExecutorService.getInstance().execute(
                new Runnable() {
                    @Override
                    public void run() {
                        AuditService.addAuditService(Cache.getUser().getUserName(), getString(R.string.user), getString(R.string.user_setting), getString(R.string.user_setting_logout), Cache.currentTime(), auditHelper);
                    }
                }
        );
    }

    private void updateUser() {
        String oldPassword = oldPassword_et.getText().toString();
        String newPassword1 = newPassword_et1.getText().toString();
        String newPassword2 = newPassword_et2.getText().toString();
        if (StringUtils.isEmpty(oldPassword) && StringUtils.isEmpty(newPassword1) && StringUtils.isEmpty(newPassword2)) {
            doUpdate(Cache.getUser().getPassword(), Cache.getUser().getPassword());
            return;
        }
        if (oldPassword == null || oldPassword.length() == 0) {
            Toast.makeText(getActivity(), getString(R.string.password_null), Toast.LENGTH_SHORT).show();
            return;
        }
        if (newPassword1 == null || newPassword1.length() == 0) {
            Toast.makeText(getActivity(), getString(R.string.new_password_null), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!newPassword1.equals(newPassword2)) {
            Toast.makeText(getActivity(), getString(R.string.password_illegal), Toast.LENGTH_SHORT).show();
            return;
        }
        doUpdate(oldPassword, newPassword1);
    }

    private void doUpdate(final String oldPassword, final String newPassword) {
        ExecutorService.getInstance().execute(
                new Runnable() {
                    @Override
                    public void run() {
                        int loginStatus = userHelper.login(user.getUserName(), oldPassword);
                        if (loginStatus != 1) {
                            Message.obtain(userSettingHandler, BaseActivity.PASSWORD_ERROR).sendToTarget();
                            return;
                        }
                        user.setPassword(newPassword);
                        user.setHeadId(checkedHeadId);
                        user.setAutoLogin(autoLogin_cb.isChecked());
                        if (autoLogin_cb.isChecked()) {
                            userHelper.setAutoLoginUser(user.getUserName());
                        } else {
                            userHelper.cleanAutoLoginUser();
                        }
                        userHelper.updateUser(user);
                        AuditService.addAuditService(Cache.getUser().getUserName(), getString(R.string.user), getString(R.string.user_setting), getString(R.string.user_setting_saving), Cache.currentTime(), auditHelper);
                        Message.obtain(userSettingHandler, BaseActivity.UPDATE_SUCCESS).sendToTarget();
                        if (activityHandler != null) {
                            Message.obtain(activityHandler, BaseActivity.USER_UPDATE).sendToTarget();
                        }
                    }
                }
        );
    }

    public class UserSettingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BaseActivity.PASSWORD_ERROR:
                    ToastUtil.toastShow(mToast, getString(R.string.password_error));
                    break;
                case BaseActivity.UPDATE_SUCCESS:
                    head.setImageResource(checkedHeadId);
                    oldPassword_et.setText("");
                    newPassword_et1.setText("");
                    newPassword_et2.setText("");
                    ToastUtil.toastShow(mToast, getString(R.string.save_success));
                    break;
            }
        }
    }
}
