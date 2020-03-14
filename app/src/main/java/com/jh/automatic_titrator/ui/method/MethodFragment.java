package com.jh.automatic_titrator.ui.method;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.app.Fragment;
import android.widget.CheckBox;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.common.Cache;
import com.jh.automatic_titrator.common.db.AuditHelper;
import com.jh.automatic_titrator.common.db.UserHelper;
import com.jh.automatic_titrator.entity.common.User;
import com.jh.automatic_titrator.service.DBService;
import com.jh.automatic_titrator.service.ExecutorService;
import com.jh.automatic_titrator.service.TitorMethodService;

public class MethodFragment extends Fragment implements View.OnClickListener {

    private FragmentManager fragmentManager;
    private View executeMethodView;
    private User user;
    private UserHelper userHelper;
    private AuditHelper auditHelper;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.titrator_method_fragemnt_detail, container, false);
        user = Cache.getUser();
        auditHelper = DBService.getAuditHelper(getActivity());

        fragmentManager = getFragmentManager();

        createTitorMethodBtn = view.findViewById(R.id.data_detail_creator);
        createTitorMethodBtn.setOnClickListener(this);

        deleteMethodBtn = view.findViewById(R.id.data_detail_creator);
        deleteMethodBtn.setOnClickListener(this);


        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_method_alter_save_button:
                doSave();
                break;
            case R.id.setting_standard_date:
                if (!Cache.containsAuth("dateupdate")) {
                    return;
                }
                break;
        }
    }

    public void doSave() {
        progressDialog.setMessage(getString(R.string.save));
        progressDialog.setCancelable(false);
        progressDialog.show();
        ExecutorService.getInstance().execute(
                new Runnable() {
                    @Override
                    public void run() {
                        boolean autoClean = autoClean_cb.isChecked();

                    }
                }
        );
    }

}
