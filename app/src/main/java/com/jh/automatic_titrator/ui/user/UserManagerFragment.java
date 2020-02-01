package com.jh.automatic_titrator.ui.user;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.common.Cache;
import com.jh.automatic_titrator.common.db.AuditHelper;
import com.jh.automatic_titrator.common.db.UserHelper;
import com.jh.automatic_titrator.common.utils.StringUtils;
import com.jh.automatic_titrator.common.utils.TimeTool;
import com.jh.automatic_titrator.common.utils.ToastUtil;
import com.jh.automatic_titrator.entity.common.Role;
import com.jh.automatic_titrator.entity.common.User;
import com.jh.automatic_titrator.service.AuditService;
import com.jh.automatic_titrator.service.DBService;
import com.jh.automatic_titrator.service.ExecutorService;
import com.jh.automatic_titrator.ui.BaseActivity;
import com.jh.automatic_titrator.ui.listener.KeyboardDismiss;
import com.jh.automatic_titrator.ui.window.UserDetailPopupWindow;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by apple on 16/9/24.
 */
public class UserManagerFragment extends Fragment implements View.OnClickListener {

    private static final int PASSWORD_ILLEGAL = 0xFF;
    private static final int USER_ILLEGAL = 0xFE;

    private EditText username_tv;
    private EditText password1_tv;
    private EditText password2_tv;

    private ListView userListView;

    private Spinner userAddType;

    private UserManagerAdapter userManagerAdapter;

    private List<User> userList;

    private UserHelper userHelper;

    private View view;

    private View save_btn;

    private UserDetailPopupWindow userDetail;

    private Handler handler;

    private String auth = "";

    private String desc = "";

    private View delete_btn;
    private View detail_btn;
    private View edit_username;
    private View edit_password;

    private AuditHelper auditHelper;

    private Toast mToast;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.user_manager_fragment, container, false);
        mToast = ToastUtil.createToast(getActivity());
        handler = new UserManagerHandler();
        userListView = (ListView) view.findViewById(R.id.user_manager_lv);

        userHelper = DBService.getUserHelper(this.getActivity());
        auditHelper = DBService.getAuditHelper(this.getActivity());
        userList = userHelper.listUser(Cache.getUser().getUserName());
        userManagerAdapter = new UserManagerAdapter(userList, this.getActivity(), handler, userHelper);
        userListView.setAdapter(userManagerAdapter);

        userAddType = (Spinner) view.findViewById(R.id.user_manager_add_type);
        String[] userTypes = getActivity().getResources().getStringArray(R.array.user_type);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(), R.layout.spinner_checked, userTypes);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        userAddType.setAdapter(adapter);

        if (Cache.getUser().getRole().equals(Role.manager)) {
            userAddType.setSelection(1);
            userAddType.setEnabled(false);
        } else {
            userAddType.setSelection(0);
            userAddType.setEnabled(true);
        }

        save_btn = view.findViewById(R.id.user_manager_save_btn);
        save_btn.setOnClickListener(this);

        username_tv = (EditText) view.findViewById(R.id.user_manager_username);
        password1_tv = (EditText) view.findViewById(R.id.user_manager_password1);
        password2_tv = (EditText) view.findViewById(R.id.user_manager_password2);

        delete_btn = view.findViewById(R.id.user_manager_delete_btn);
        delete_btn.setOnClickListener(this);
        detail_btn = view.findViewById(R.id.user_manager_detail_btn);
        detail_btn.setOnClickListener(this);

        edit_username = view.findViewById(R.id.user_manager_detail_update_username);
        edit_username.setOnClickListener(this);
        edit_password = view.findViewById(R.id.user_manager_detail_update_password);
        edit_password.setOnClickListener(this);

        if (Cache.getUser().getRole().equals(Role.admin)) {
            edit_username.setVisibility(View.VISIBLE);
            edit_password.setVisibility(View.VISIBLE);
        } else {
            edit_username.setVisibility(View.INVISIBLE);
            edit_password.setVisibility(View.INVISIBLE);
        }

        ensureBtnShow();
        return view;
    }

    private void ensureBtnShow() {
        if (!Cache.containsAuth("delete")) {
            delete_btn.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_manager_detail_btn:
                showUserDetails();
                break;
            case R.id.user_manager_save_btn:
                addUserDetails();
                break;
            case R.id.user_manager_delete_btn:
                deleteUser();
                break;
            case R.id.user_manager_detail_update_username:
                updateUsername();
                break;
            case R.id.user_manager_detail_update_password:
                updatePassword();
                break;
        }
    }

    private void saveUser() {
        new Thread() {
            @Override
            public void run() {
                doSaveUser();
            }
        }.start();
    }

    public void doSaveUser() {
        Message msg;
        if (StringUtils.isEmpty(username_tv.getText().toString())) {
            msg = Message.obtain(handler, USER_ILLEGAL);
        } else if (StringUtils.notEmptyAndEquals(password1_tv.getText().toString(), password2_tv.getText().toString())) {
            User user = userHelper.getUserDetail(username_tv.getText().toString());
            if (user != null) {
                msg = Message.obtain(handler, BaseActivity.SAVE_INDEX_EXISTS);
            } else {
                Message.obtain(handler, BaseActivity.SAVING);
                user = new User();
                user.setUserName(username_tv.getText().toString());
                user.setPassword(password1_tv.getText().toString());
                user.setHeadId(R.drawable.head_portrait1);
                user.setAuth(auth);
                int index = userAddType.getSelectedItemPosition() + 1;
                user.setRole(Role.getRole(index));
                user.setDesc(desc);
                user.setCreateDate(TimeTool.currentDate());
                user.setCreator(Cache.getUser().getUserName());
                userHelper.addUser(user);
                AuditService.addAuditService(Cache.getUser().getUserName(), getString(R.string.user), getString(R.string.user_manager), getString(R.string.user_manager_add), Cache.currentTime(), auditHelper);
                msg = Message.obtain(handler, BaseActivity.SAVE_SUCCESS);
            }
        } else {
            msg = Message.obtain(handler, PASSWORD_ILLEGAL);
        }
        msg.sendToTarget();
    }

    private void deleteUser() {
        ExecutorService.getInstance().execute(
                new Runnable() {
                    @Override
                    public void run() {
                        List<User> checkedUsers = userManagerAdapter.getCheckedUsers();
                        if (checkedUsers == null || checkedUsers.size() == 0) {
                            Message msg = Message.obtain(handler, BaseActivity.DELETE_NO_NEED);
                            msg.sendToTarget();
                        } else {
                            Message msg = Message.obtain(handler, BaseActivity.DELETE_ENSURE);
                            msg.sendToTarget();
                        }
                    }
                }
        );
    }

    private void ensureDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setMessage(this.getActivity().getString(R.string.delete_ensure));
        builder.setTitle(this.getActivity().getString(R.string.prompt));
        builder.setPositiveButton(this.getActivity().getString(R.string.submit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                doDelete();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(this.getActivity().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void doDelete() {
        List<User> checkedUsers = userManagerAdapter.getCheckedUsers();
        userHelper.deleteUsers(checkedUsers);
        if (checkedUsers.size() == 1) {
            AuditService.addAuditService(Cache.getUser().getUserName(), getString(R.string.user), getString(R.string.user_manager), getString(R.string.user_manager_delete), Cache.currentTime(), auditHelper);
        } else {
            AuditService.addAuditService(Cache.getUser().getUserName(), getString(R.string.user), getString(R.string.user_manager), getString(R.string.user_manager_delete_batch), Cache.currentTime(), auditHelper);
        }
        Message.obtain(handler, BaseActivity.DELETE_SUCCESS).sendToTarget();
    }

    public void updateUserList() {
        ExecutorService.getInstance().execute(
                new Runnable() {
                    @Override
                    public void run() {
                        userList.clear();
                        userList.addAll(userHelper.listUser(Cache.getUser().getUserName()));
                        Message.obtain(handler, BaseActivity.NEED_REFRESH).sendToTarget();
                    }
                }
        );
    }

    private void addUserDetails() {
        if (userDetail == null) {
            DisplayMetrics outMetrics = new DisplayMetrics();
            this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
            float dpx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, this.getActivity().getResources().getDisplayMetrics());
            userDetail = new UserDetailPopupWindow(this.getActivity(), (int) (300 * dpx), (int) (500 * dpx), true);
        }

        // 设置好参数之后再show
        userDetail.setOnSaveListener(new UserDetailPopupWindow.OnSaveListener() {
            @Override
            public void onSave(Set<String> auth, String desc) {
                if (auth != null && auth.size() > 0) {
                    UserManagerFragment.this.desc = desc;
                    StringBuilder stringBuilder = new StringBuilder();
                    for (String authItem : auth) {
                        stringBuilder.append(authItem).append("|");
                    }
                    if (stringBuilder.length() >= 1) {
                        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                    }
                    UserManagerFragment.this.auth = stringBuilder.toString();
                } else {
                    UserManagerFragment.this.auth = "";
                }
                doSaveUser();
            }
        });
        userDetail.setDesc("");
        userDetail.show(new HashSet<String>(), userAddType.getSelectedItemPosition() + 1, true);
    }

    private void showUserDetails() {
        List<User> users = userManagerAdapter.getCheckedUsers();
        if (users.size() != 1) {
            ToastUtil.toastShow(mToast, getString(R.string.only_one_checked));
            return;
        }
        if (userDetail == null) {
            DisplayMetrics outMetrics = new DisplayMetrics();
            this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
            float dpx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, this.getActivity().getResources().getDisplayMetrics());
            userDetail = new UserDetailPopupWindow(this.getActivity(), (int) (300 * dpx), (int) (500 * dpx), true);
        }

        final User user = users.get(0);
        String authStr = user.getAuth();
        Set<String> authSet = new HashSet<>();
        if (authStr != null && !authStr.equals("")) {
            String[] auths = authStr.split("\\|");
            for (String auth : auths) {
                authSet.add(auth);
            }
        }

        userDetail.setOnSaveListener(new UserDetailPopupWindow.OnSaveListener() {
            @Override
            public void onSave(Set<String> auth, String desc) {
                StringBuilder stringBuilder = new StringBuilder();
                for (String authItem : auth) {
                    stringBuilder.append(authItem).append("|");
                }
                if (stringBuilder.length() >= 1) {
                    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                }
                user.setAuth(stringBuilder.toString());
                user.setDesc(desc);
                userHelper.updateUser(user);
                AuditService.addAuditService(Cache.getUser().getUserName(), getString(R.string.user), getString(R.string.user_manager), getString(R.string.user_manager_alter), Cache.currentTime(), auditHelper);
                Message.obtain(handler, BaseActivity.UPDATE_SUCCESS).sendToTarget();
            }
        });
        // 设置好参数之后再show
        userDetail.setDesc(user.getDesc());
        userDetail.show(authSet, user.getRole().ordinal(), false);
    }

    private void updateUsername() {
        List<User> users = userManagerAdapter.getCheckedUsers();
        if (users.size() != 1) {
            ToastUtil.toastShow(mToast, getString(R.string.only_one_checked));
            return;
        }
        final View passwordInputView = LayoutInflater.from(getActivity()).inflate(R.layout.input_username, null);

        final EditText newUserName = (EditText) passwordInputView.findViewById(R.id.input_username_et);

        final String oldUserName = users.get(0).getUserName();

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle(getString(R.string.prompt));
        builder.setMessage(getString(R.string.username));
        builder.setView(passwordInputView);
        builder.setPositiveButton(this.getActivity().getString(R.string.submit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (newUserName.getText() != null && newUserName.getText().toString().trim().length() != 0) {
                    String nun = newUserName.getText().toString();
                    User existUser = userHelper.getUserDetail(nun);
                    if (existUser == null) {
                        userHelper.changeUsername(oldUserName, nun);
                        dialog.dismiss();
                        AuditService.addAuditService(Cache.getUser().getUserName(), getString(R.string.user), getString(R.string.user_manager), getString(R.string.update_username), Cache.currentTime(), auditHelper);
                        Message.obtain(handler, BaseActivity.SAVE_SUCCESS).sendToTarget();

                    } else {
                        ToastUtil.toastShow(mToast, getString(R.string.user_exists));
                    }
                } else {
                    ToastUtil.toastShow(mToast, getString(R.string.user_cannot_be_null));
                }
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

    private void updatePassword() {
        List<User> users = userManagerAdapter.getCheckedUsers();
        if (users.size() != 1) {
            ToastUtil.toastShow(mToast, getString(R.string.only_one_checked));
            return;
        }
        final View passwordInputView = LayoutInflater.from(getActivity()).inflate(R.layout.input_password, null);

        final EditText password = (EditText) passwordInputView.findViewById(R.id.input_password_et);

        final String oldUserName = users.get(0).getUserName();

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle(getString(R.string.prompt));
        builder.setMessage(getString(R.string.password));
        builder.setView(passwordInputView);
        builder.setPositiveButton(this.getActivity().getString(R.string.submit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (password.getText() != null && password.getText().toString().trim().length() != 0) {
                    userHelper.changePassword(oldUserName, password.getText().toString().trim());
                    dialog.dismiss();
                    Message.obtain(handler, BaseActivity.SAVE_SUCCESS).sendToTarget();
                    AuditService.addAuditService(Cache.getUser().getUserName(), getString(R.string.user), getString(R.string.user_manager), getString(R.string.update_password), Cache.currentTime(), auditHelper);
                } else {
                    ToastUtil.toastShow(mToast, getString(R.string.user_cannot_be_null));
                }
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

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        updateUserList();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUserList();
    }

    public class UserManagerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BaseActivity.NEED_REFRESH:
                    userManagerAdapter.notifyDataSetChanged();
                    break;
                case BaseActivity.SAVING:
                    ToastUtil.toastShow(mToast, getString(R.string.saving));
                    break;
                case BaseActivity.SAVE_SUCCESS:
                    if (userDetail != null) {
                        userDetail.dismiss();
                    }
                    ToastUtil.toastShow(mToast, getString(R.string.save_success));
                    updateUserList();
                    break;
                case BaseActivity.DELETE_ENSURE:
                    ensureDelete();
                    break;
                case BaseActivity.DELETE_SUCCESS:
                    ToastUtil.toastShow(mToast, getString(R.string.delete_success));
                    updateUserList();
                    break;
                case PASSWORD_ILLEGAL:
                    ToastUtil.toastShow(mToast, getString(R.string.password_illegal));
                    break;
                case BaseActivity.DELETE_NO_NEED:
                    ToastUtil.toastShow(mToast, getString(R.string.no_need_to_delete));
                    break;
                case BaseActivity.SAVE_INDEX_EXISTS:
                    ToastUtil.toastShow(mToast, getString(R.string.user_exists));
                    break;
                case BaseActivity.UPDATE_SUCCESS:
                    if (userDetail != null) {
                        userDetail.dismiss();
                    }
                    ToastUtil.toastShow(mToast, getString(R.string.save_success));
                    break;
                case USER_ILLEGAL:
                    ToastUtil.toastShow(mToast, getString(R.string.user_cannot_be_null));
                    break;
            }
        }
    }
}
