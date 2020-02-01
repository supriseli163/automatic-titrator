package com.jh.automatic_titrator.ui.setting;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.common.Cache;
import com.jh.automatic_titrator.common.db.AuditHelper;
import com.jh.automatic_titrator.common.file.FileHelper;
import com.jh.automatic_titrator.common.utils.ToastUtil;
import com.jh.automatic_titrator.entity.common.Audit;
import com.jh.automatic_titrator.entity.common.UDisk;
import com.jh.automatic_titrator.service.AuditService;
import com.jh.automatic_titrator.service.DBService;
import com.jh.automatic_titrator.service.ExecutorService;
import com.jh.automatic_titrator.service.SystemService;
import com.jh.automatic_titrator.ui.BaseActivity;
import com.jh.automatic_titrator.ui.window.SimpleDateSearch;

import java.util.List;

/**
 * Created by apple on 16/9/24.
 */
public class SettingAuditFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private static final int EXPORT_COMPLETED = 110010;

    private int currentPage = 1;

    private int count;

    private int pageSize = 15;

    private int pageCount = 0;

    private SettingAuditAdapter adapter;

    private ListView audit_lv;

    private List<Audit> auditList;

    private CheckBox choosePage;

    private CheckBox chooseAll;

    private View nextPage;

    private View presPage;

    private String page;

    private View searchBtn;
    private View exportBtn;
    private View deleteBtn;

    private SimpleDateSearch searchView;

    private AuditHelper auditHelper;

    private TextView setting_audit_page;

    private String startDate;
    private String endDate;
    private String operator;

    private SettingAuditHandler handler;

    private View view;

    private Toast mToast;

    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.setting_audit_fragment, container, false);
        mToast = ToastUtil.createToast(getActivity());
        auditHelper = DBService.getAuditHelper(getActivity());

        count = auditHelper.count();

        setting_audit_page = (TextView) view.findViewById(R.id.setting_audit_page_info);

        page = getActivity().getResources().getString(R.string.page);

        audit_lv = (ListView) view.findViewById(R.id.setting_audit_lv);
        auditList = auditHelper.listAudit(null, null, null, 1, pageSize);
        adapter = new SettingAuditAdapter(this.getActivity(), auditList, false, false, 1);
        audit_lv.setAdapter(adapter);
        audit_lv.setVisibility(View.VISIBLE);

        searchBtn = view.findViewById(R.id.setting_audit_search_btn);
        searchBtn.setOnClickListener(this);
        exportBtn = view.findViewById(R.id.setting_audit_export_btn);
        exportBtn.setOnClickListener(this);
        deleteBtn = view.findViewById(R.id.setting_audit_delete_btn);
        deleteBtn.setOnClickListener(this);

        nextPage = view.findViewById(R.id.setting_audit_next_btn);
        nextPage.setOnClickListener(this);
        presPage = view.findViewById(R.id.setting_audit_prev_btn);
        presPage.setOnClickListener(this);

        choosePage = (CheckBox) view.findViewById(R.id.setting_audit_singlepage_check);
        chooseAll = (CheckBox) view.findViewById(R.id.setting_audit_allpage_check);
        choosePage.setOnCheckedChangeListener(this);
        chooseAll.setOnCheckedChangeListener(this);

        handler = new SettingAuditHandler();

        checkAuth();
        return view;
    }

    public void checkAuth() {
        if (!Cache.containsAuth("delete_audit")) {
            deleteBtn.setVisibility(View.VISIBLE);
        } else {
            deleteBtn.setVisibility(View.GONE);
        }
    }

    public void doSearch() {
        ExecutorService.getInstance().execute(
                new Runnable() {
                    @Override
                    public void run() {
                        count = auditHelper.countAudit(startDate, endDate, operator);
                        pageCount = (count + pageSize - 1) / pageSize;
                        currentPage = 1;
                        auditList.clear();
                        auditList.addAll(auditHelper.listAudit(startDate, endDate, operator, currentPage, pageSize));
                        Message.obtain(handler, BaseActivity.NEED_REFRESH).sendToTarget();
                    }
                }
        );
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            doSearch();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_audit_next_btn:
                showNextPage();
                reshowMethodBtn();
                break;
            case R.id.setting_audit_prev_btn:
                showPresPage();
                reshowMethodBtn();
                break;
            case R.id.setting_audit_search_btn:
                search();
                break;
            case R.id.setting_audit_export_btn:
                export();
                break;
            case R.id.setting_audit_delete_btn:
                delete();
                break;
        }
    }

    private void showNextPage() {
        ExecutorService.getInstance().execute(
                new Runnable() {
                    @Override
                    public void run() {
                        List<Audit> auditList1 = auditHelper.listAudit(startDate, endDate, operator, currentPage + 1, pageSize);
                        auditList.clear();
                        auditList.addAll(auditList1);

                        currentPage = currentPage + 1;
                        adapter.setCheckedPage(false);
                        Message message = Message.obtain(handler, BaseActivity.NEED_REFRESH);
                        message.sendToTarget();
                    }
                }
        );
    }

    private void showPresPage() {
        ExecutorService.getInstance().execute(
                new Runnable() {
                    @Override
                    public void run() {
                        List<Audit> auditList1 = auditHelper.listAudit(startDate, endDate, operator, currentPage - 1, pageSize);
                        auditList.clear();
                        auditList.addAll(auditList1);

                        currentPage = currentPage - 1;
                        adapter.setCheckedPage(false);
                        Message message = Message.obtain(handler, BaseActivity.NEED_REFRESH);
                        message.sendToTarget();
                    }
                }
        );
    }

    private void search() {
        if (searchView == null) {
            searchView = new SimpleDateSearch(this.getActivity(), getString(R.string.operator));
            searchView.setOnSearchListener(new SimpleDateSearch.OnSearchListener() {
                @Override
                public void onSearch(final String startDate, final String endDate, final String creator) {
                    SettingAuditFragment.this.startDate = startDate;
                    SettingAuditFragment.this.endDate = endDate;
                    SettingAuditFragment.this.operator = creator;
                    doSearch();
                    searchView.dismiss();
                }
            });
        }

        searchView.show();
    }

    private void export() {
        if (adapter.getCheckedItems().size() <= 0) {
            ToastUtil.toastShow(mToast, getString(R.string.checked_ensure));
            return;
        } else {
            final List<UDisk> uDisks = SystemService.getUDisksNew(getActivity());
            if (uDisks == null || uDisks.size() == 0) {
                Message.obtain(handler, BaseActivity.SHOW_MSG, getString(R.string.udisk_not_found)).sendToTarget();
                return;
            }
            View fileNameView = LayoutInflater.from(getActivity()).inflate(R.layout.input_filename, null);

            final EditText fileName = (EditText) fileNameView.findViewById(R.id.input_filename_et);

            AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
            builder.setTitle(getString(R.string.prompt));
            builder.setMessage(getString(R.string.file_name));
            builder.setView(fileNameView);
            builder.setPositiveButton(this.getActivity().getString(R.string.submit), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (fileName.getText().toString().trim().length() == 0) {
                        ToastUtil.toastShow(mToast, getString(R.string.file_name_can_not_be_null));
                    } else {
                        doExport(uDisks.get(0).getFilePath(), fileName.getText().toString());
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
            builder.create().show();
        }
    }

    private void doExport(final String dir, final String fileName) {
        ExecutorService.getInstance().execute(
                new Runnable() {
                    @Override
                    public void run() {
                        List<Audit> audits;
                        if (chooseAll.isChecked()) {
                            audits = auditHelper.listAudit(startDate, endDate, operator);
                        } else {
                            audits = adapter.getCheckedItems();
                        }
                        try {
                            Message.obtain(handler, BaseActivity.HOLD_WINDOW_YES, getString(R.string.exporting_excel)).sendToTarget();
                            FileHelper.writeAsExcelAudit(getActivity(), dir, fileName + ".xls", audits);
//                            FileHelper.writeAsExcelAudit(getActivity(), getTempFileDir(), fileName + ".xls", audits);
                            AuditService.addAuditService(Cache.getUser().getUserName(), getString(R.string.setting), getString(R.string.audit_setting), getString(R.string.export), Cache.currentTime(), auditHelper);
                            Message.obtain(handler, BaseActivity.HOLD_WINDOW_NO).sendToTarget();
                            Message.obtain(handler, EXPORT_COMPLETED, getString(R.string.export_success)).sendToTarget();
                        } catch (Exception e) {
                            Message.obtain(handler, BaseActivity.HOLD_WINDOW_NO).sendToTarget();
                            Message.obtain(handler, EXPORT_COMPLETED, e);
                        }
                    }
                }
        );
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.setting_audit_singlepage_check:
                adapter.setCheckedPage(isChecked);
                adapter.notifyDataSetChanged();
                break;
            case R.id.setting_audit_allpage_check:
                adapter.setCheckedAll(isChecked);
                adapter.notifyDataSetChanged();
                break;
        }
    }

    private void altertDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle(getString(R.string.prompt));
        builder.setMessage(getString(R.string.delete_export_ensure));
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

    private void delete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle(getString(R.string.prompt));
        builder.setMessage(getString(R.string.delete_ensure));
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
        ExecutorService.getInstance().execute(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            auditHelper.deleteAudit(startDate, endDate, operator);
                            AuditService.addAuditService(Cache.getUser().getUserName(), getString(R.string.setting), getString(R.string.audit_setting), getString(R.string.audit_setting_delete), Cache.currentTime(), auditHelper);
                            auditList.clear();
                            auditList.addAll(auditHelper.listAudit(startDate, endDate, operator));
                            Message.obtain(handler, BaseActivity.DELETE_SUCCESS).sendToTarget();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }

    @Override
    public void onStart() {
        super.onStart();
        refreshPageCount();
        reshowMethodBtn();
    }

    public void reshowMethodBtn() {
        if (currentPage >= pageCount) {
            nextPage.setVisibility(View.GONE);
        } else {
            nextPage.setVisibility(View.VISIBLE);
        }
        if (currentPage <= 1) {
            presPage.setVisibility(View.GONE);
        } else {
            presPage.setVisibility(View.VISIBLE);
        }
    }

    private void refreshPageCount() {
        if (count % pageSize == 0) {
            pageCount = count / pageSize;
        } else {
            pageCount = count / pageSize + 1;
        }
        if (count == 0) {
            pageCount = 1;
        }
        setting_audit_page.setText(currentPage + "/" + pageCount + " " + page);
    }

    private void holdWindow(String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
        }
        progressDialog.setTitle(message);
        progressDialog.show();
    }

    private void dissmisDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    private String getTempFileDir() {
        return Environment.getExternalStorageDirectory() + "/hanon/";
    }

    public class SettingAuditHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BaseActivity.NEED_REFRESH:
                    adapter.clearChecked();
                    adapter.notifyDataSetChanged();
                    choosePage.setChecked(false);
                    reshowMethodBtn();
                    refreshPageCount();
                    break;
                case BaseActivity.DELETE_SUCCESS:
                    ToastUtil.toastShow(mToast, getString(R.string.delete_success));
                    adapter.clearChecked();
                    adapter.notifyDataSetChanged();
                    refreshPageCount();
                    reshowMethodBtn();
                    break;
                case EXPORT_COMPLETED:
                    ToastUtil.toastShow(mToast, String.valueOf(msg.obj));
//                    altertDelete();
                    break;
                case BaseActivity.HOLD_WINDOW_YES:
                    if (msg.obj != null) {
                        holdWindow(String.valueOf(msg.obj));
                    }
                    break;
                case BaseActivity.HOLD_WINDOW_NO:
                    dissmisDialog();
                    break;
                case BaseActivity.SHOW_MSG:
                    if (msg.obj != null) {
                        ToastUtil.toastShow(mToast, String.valueOf(msg.obj));
                    }
                    break;
            }
        }
    }
}
