package com.jh.automatic_titrator.ui.method;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.app.Fragment;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.common.Cache;
import com.jh.automatic_titrator.common.db.AuditHelper;
import com.jh.automatic_titrator.common.db.MethodHelper;
import com.jh.automatic_titrator.common.db.UserHelper;
import com.jh.automatic_titrator.entity.common.User;
import com.jh.automatic_titrator.entity.common.titrator.TitratorMethod;
import com.jh.automatic_titrator.service.AuditService;
import com.jh.automatic_titrator.service.DBService;
import com.jh.automatic_titrator.service.ExecutorService;
import com.jh.automatic_titrator.service.TitorMethodService;
import com.jh.automatic_titrator.ui.BaseActivity;
import com.jh.automatic_titrator.ui.window.MethodAddWindow;
import com.jh.automatic_titrator.ui.window.MethodUpdateWindow;

import java.util.List;

public class MethodFragment extends Fragment implements View.OnClickListener {

    private FragmentManager fragmentManager;
    private MethodEditFragment methodEditFragment;
    private MethodTabFragment methodTabFragment;

    private View executeMethodView;
    private User user;
    private UserHelper userHelper;
    private AuditHelper auditHelper;
    private MethodHelper methodHelper;
    private Handler activityHandler;

    //Btn
    private View createTitorMethodBtn;
    private View deleteMethodBtn;
    private View importMethodBtn;
    private View exportMethodBtn;
    private View saveMethodBtn;
    private View runMethodBtn;
    private View commitMethodBtn;
    private View createBtn;
    private View updateBtn;
    private View deleteBtn;

    private ProgressDialog progressDialog;
    private CheckBox autoClean_cb;
    private boolean languageChanged = false;
    private boolean timeChanged = false;
    private boolean dateChanged = false;

    private TitorMethodService titorMethodService;
    private MethodAddWindow methodAddWindow;
    private MethodUpdateWindow methodUpdateWindow;

    public void setActivityHandler(Handler activityHandler) {
        this.activityHandler = activityHandler;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.titrator_method_fragemnt_detail, container, false);
        user = Cache.getUser();
        auditHelper = DBService.getAuditHelper(getActivity());

        fragmentManager = getFragmentManager();

        createTitorMethodBtn = view.findViewById(R.id.data_detail_creator);
        createTitorMethodBtn.setOnClickListener(this);

        deleteMethodBtn = view.findViewById(R.id.data_delete_btn);
        deleteMethodBtn.setOnClickListener(this);

        importMethodBtn = view.findViewById(R.id.titor_method_alter_save_button_tv);
        importMethodBtn.setOnClickListener(this);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titor_method_alter_save_button_tv:
                add();
                break;
            case R.id.setting_standard_date:
                if (!Cache.containsAuth("dateupdate")) {
                    return;
                }
                break;
        }
    }

    public void add() {
        if(methodAddWindow == null) {
            methodAddWindow = new MethodAddWindow(getActivity());
        }
        methodAddWindow.show();
        methodAddWindow.setOnSaveListener(new MethodAddWindow.OnSaveListener() {
            @Override
            public void onSave(TitratorMethod titratorMethod) {
                doSave(titratorMethod);
            }
        });
    }

    public void doSave(TitratorMethod titratorMethod) {
        methodHelper.saveMethod(titratorMethod);
        AuditService.addAuditService(Cache.getUser().getUserName(),
                getActivity().getString(R.string.method),
                getActivity().getString(R.string.method_manager),
                getString(R.string.add),
                Cache.currentTime(),
                auditHelper);
        methodAddWindow.dismiss();
        titorMethodService.onChange(R.id.titor_method_alter_save_button_tv);
        Message.obtain(activityHandler, BaseActivity.SAVE_SUCCESS).sendToTarget();
    }


    private void showPresPage() {
        ExecutorService.getInstance().execute(
                new Runnable() {
                    @Override
                    public void run() {
                        List<TitratorMethod> titratorMethods = methodHelper.listAll();
                    }
                }
        );
    }
}
