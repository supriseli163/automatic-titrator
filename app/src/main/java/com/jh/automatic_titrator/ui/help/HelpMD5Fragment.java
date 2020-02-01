package com.jh.automatic_titrator.ui.help;

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
import com.jh.automatic_titrator.common.db.MD5Helper;
import com.jh.automatic_titrator.common.file.FileHelper;
import com.jh.automatic_titrator.common.utils.ToastUtil;
import com.jh.automatic_titrator.entity.common.MD5;
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
public class HelpMD5Fragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private static final int EXPORT_COMPLETED = 110010;

    private ListView help_md5_lv;

    private HelpMD5Adaptor adaptor;

    private MD5Helper md5Helper;

    private View deleteBtn;

    private List<MD5> md5s;

    private MD5Handler handler;

    private View searchBtn;

    private View exportBtn;

    private SimpleDateSearch searchView;

    private View view;

    private AuditHelper auditHelper;

    private Toast mToast;

    private int currentPage = 1;

    private int count;

    private int pageSize = 15;

    private int pageCount = 0;

    private String startDate;

    private String endDate;

    private String operator;

    private String page;

    private View nextPage;

    private View presPage;

    private TextView helpMd5Page;

    private CheckBox choosePage;

    private CheckBox chooseAll;

    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.help_md5_fragment, container, false);
        mToast = ToastUtil.createToast(getActivity());
        help_md5_lv = (ListView) view.findViewById(R.id.help_md5_lv);
        md5Helper = DBService.getMD5Helper(getActivity());
        count = md5Helper.count();
        md5s = md5Helper.list(startDate, endDate, operator, currentPage, pageSize);
        auditHelper = DBService.getAuditHelper(getActivity());
        adaptor = new HelpMD5Adaptor(getActivity(), md5s, false, false);

        page = getActivity().getResources().getString(R.string.page);

        help_md5_lv.setAdapter(adaptor);

        searchBtn = view.findViewById(R.id.help_md5_search_btn);
        searchBtn.setOnClickListener(this);
        exportBtn = view.findViewById(R.id.help_md5_export_btn);
        exportBtn.setOnClickListener(this);

        nextPage = view.findViewById(R.id.help_md5_next_btn);
        nextPage.setOnClickListener(this);
        presPage = view.findViewById(R.id.help_md5_prev_btn);
        presPage.setOnClickListener(this);

        choosePage = (CheckBox) view.findViewById(R.id.help_md5_singlepage_check);
        chooseAll = (CheckBox) view.findViewById(R.id.help_md5_allpage_check);
        choosePage.setOnCheckedChangeListener(this);
        chooseAll.setOnCheckedChangeListener(this);

        helpMd5Page = (TextView) view.findViewById(R.id.help_md5_page_info);

        handler = new MD5Handler();

        searchBtn = view.findViewById(R.id.help_md5_search_btn);
        searchBtn.setOnClickListener(this);

        return view;
    }

    public void doSearch() {
        ExecutorService.getInstance().execute(
                new Runnable() {
                    @Override
                    public void run() {
                        count = md5Helper.count(startDate, endDate, operator);
                        pageCount = (count + pageSize - 1) / pageSize;
                        currentPage = 1;
                        md5s.clear();
                        md5s.addAll(md5Helper.list(startDate, endDate, operator, currentPage, pageSize));
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
            case R.id.help_md5_next_btn:
                showNextPage();
                reshowMethodBtn();
                break;
            case R.id.help_md5_prev_btn:
                showPresPage();
                reshowMethodBtn();
                break;
            case R.id.help_md5_search_btn:
                search();
                break;
            case R.id.help_md5_export_btn:
                export();
                break;
        }
    }

    private void showNextPage() {
        ExecutorService.getInstance().execute(
                new Runnable() {
                    @Override
                    public void run() {
                        List<MD5> md5List = md5Helper.list(startDate, endDate, operator, currentPage + 1, pageSize);
                        md5s.clear();
                        md5s.addAll(md5List);

                        currentPage = currentPage + 1;
                        adaptor.setCheckedPage(false);
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
                        List<MD5> md5List = md5Helper.list(startDate, endDate, operator, currentPage - 1, pageSize);
                        md5s.clear();
                        md5s.addAll(md5List);

                        currentPage = currentPage - 1;
                        adaptor.setCheckedPage(false);
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
                    HelpMD5Fragment.this.startDate = startDate;
                    HelpMD5Fragment.this.endDate = endDate;
                    HelpMD5Fragment.this.operator = creator;
                    doSearch();
                    searchView.dismiss();
                }
            });
        }

        searchView.show();
    }

    private void export() {
        if (adaptor.getCheckedItems().size() <= 0) {
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
                        List<MD5> md5List;
                        if (chooseAll.isChecked()) {
                            md5List = md5Helper.list(startDate, endDate, operator);
                        } else {
                            md5List = adaptor.getCheckedItems();
                        }
                        try {
                            Message.obtain(handler, BaseActivity.HOLD_WINDOW_YES, getString(R.string.exporting_excel)).sendToTarget();
                            FileHelper.writeAsExcelMd5(getActivity(), dir, fileName + ".xls", md5List);
                            AuditService.addAuditService(Cache.getUser().getUserName(), getString(R.string.help), getString(R.string.help_md5), getString(R.string.export), Cache.currentTime(), auditHelper);
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
            case R.id.help_md5_singlepage_check:
                adaptor.setCheckedPage(isChecked);
                adaptor.notifyDataSetChanged();
                break;
            case R.id.help_md5_allpage_check:
                adaptor.setCheckedAll(isChecked);
                adaptor.notifyDataSetChanged();
                break;
        }
    }


    private class MD5Handler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BaseActivity.NEED_REFRESH:
                    adaptor.notifyDataSetChanged();
                    choosePage.setChecked(false);
                    reshowMethodBtn();
                    refreshPageCount();
                    break;
                case BaseActivity.DELETE_SUCCESS:
                    ToastUtil.toastShow(mToast, getString(R.string.delete_success));
                    adaptor.notifyDataSetChanged();
                    refreshPageCount();
                    reshowMethodBtn();
                    break;
                case EXPORT_COMPLETED:
                    ToastUtil.toastShow(mToast, String.valueOf(msg.obj));
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
                        holdWindow(String.valueOf(msg.obj));
                    }
                    break;
            }
        }
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
        helpMd5Page.setText(currentPage + "/" + pageCount + " " + page);
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
}
