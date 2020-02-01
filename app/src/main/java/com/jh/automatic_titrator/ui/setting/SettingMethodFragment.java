package com.jh.automatic_titrator.ui.setting;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.common.Cache;
import com.jh.automatic_titrator.common.db.AuditHelper;
import com.jh.automatic_titrator.common.db.TestMethodHelper;
import com.jh.automatic_titrator.common.db.WaveLengthHelper;
import com.jh.automatic_titrator.common.utils.JsonHelper;
import com.jh.automatic_titrator.common.utils.ToastUtil;
import com.jh.automatic_titrator.entity.Concentration;
import com.jh.automatic_titrator.entity.OpticalRotation;
import com.jh.automatic_titrator.entity.SpecificRotation;
import com.jh.automatic_titrator.entity.common.Formula;
import com.jh.automatic_titrator.entity.common.SettingConfig;
import com.jh.automatic_titrator.entity.common.TestMethod;
import com.jh.automatic_titrator.entity.common.User;
import com.jh.automatic_titrator.service.AuditService;
import com.jh.automatic_titrator.service.DBService;
import com.jh.automatic_titrator.service.ExecutorService;
import com.jh.automatic_titrator.service.TestMethodService;
import com.jh.automatic_titrator.ui.BaseActivity;
import com.jh.automatic_titrator.ui.window.SimpleDateSearch;
import com.jh.automatic_titrator.ui.window.TestMethodAddWindow;
import com.jh.automatic_titrator.ui.window.TestMethodUpdateWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 16/9/24.
 */
public class SettingMethodFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private int currentPage = 1;

    private int count;

    private int pageSize = 15;

    private int pageCount = 0;

    private SettingMethodAdapter adapter;

    private ListView methods_lv;

    private List<TestMethod> testMethodList;

    private SettingMethodHandler handler;

    private CheckBox choosePage;

    private CheckBox chooseAll;

    private TestMethodHelper testMethodHelper;

    private View nextPage;

    private View presPage;

    private String page;

    private TextView data_fragement_page;

    private View addBtn;

    private View deleteBtn;

    private View searchBtn;

    private View detailBtn;

    private View view;

    private SimpleDateSearch searchView;

    private String startDate;

    private String endDate;

    private String creator;

    private String[] temperatureType;

    private WaveLengthHelper waveLengthHelper;

    private TestMethodAddWindow testMethodAddWindow;
    private TestMethodUpdateWindow testMethodUpdateWindow;

    private TestMethodService testMethodService;

    private AuditHelper auditHelper;

    private Toast mToast;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.setting_method_fragment, container, false);
        mToast = ToastUtil.createToast(getActivity());
        handler = new SettingMethodHandler();
        testMethodHelper = DBService.getTestMethodHelper(this.getActivity());
        count = testMethodHelper.count();
        auditHelper = DBService.getAuditHelper(getActivity());
        waveLengthHelper = DBService.getWaveLengthHelper(this.getActivity());

        page = getActivity().getResources().getString(R.string.page);

        temperatureType = getResources().getStringArray(R.array.temperature_type);

        data_fragement_page = (TextView) view.findViewById(R.id.setting_method_page_info);
        methods_lv = (ListView) view.findViewById(R.id.setting_method_manager_lv);
        testMethodList = testMethodHelper.list(1, pageSize);
        adapter = new SettingMethodAdapter(this.getActivity(), testMethodList, false, false, 1, temperatureType);
        methods_lv.setAdapter(adapter);
        methods_lv.setVisibility(View.VISIBLE);

        addBtn = view.findViewById(R.id.setting_method_add_btn);
        addBtn.setOnClickListener(this);
        deleteBtn = view.findViewById(R.id.setting_method_delete_btn);
        deleteBtn.setOnClickListener(this);
        detailBtn = view.findViewById(R.id.setting_method_detail_btn);
        detailBtn.setOnClickListener(this);
        searchBtn = view.findViewById(R.id.setting_method_search_btn);
        searchBtn.setOnClickListener(this);

        nextPage = view.findViewById(R.id.setting_method_next_btn);
        nextPage.setOnClickListener(this);
        presPage = view.findViewById(R.id.setting_method_prev_btn);
        presPage.setOnClickListener(this);

        choosePage = (CheckBox) view.findViewById(R.id.setting_method_singlepage_check);
        chooseAll = (CheckBox) view.findViewById(R.id.setting_method_allpage_check);
        choosePage.setOnCheckedChangeListener(this);
        chooseAll.setOnCheckedChangeListener(this);

        ensureBtnShow();

        testMethodService = TestMethodService.getInstance();
        testMethodService.addTestMethodListener(R.id.setting_method_add_btn, new TestMethodService.TestMethodChangedListener() {
            @Override
            public void onChange() {
                testMethodList.clear();
                testMethodList.addAll(testMethodHelper.list(0, pageSize));
                count = testMethodHelper.count();
                currentPage = 1;
                Message.obtain(handler, BaseActivity.NEED_REFRESH).sendToTarget();
            }
        });

        return view;
    }

    private void ensureBtnShow() {
        if (!Cache.containsAuth("method")) {
            deleteBtn.setVisibility(View.GONE);
            addBtn.setVisibility(View.GONE);
        } else {
            deleteBtn.setVisibility(View.VISIBLE);
            addBtn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        refreshPageCount();
        reshowMethodBtn();
    }

    public void showDetail() {
        List<TestMethod> testMethods = adapter.getCheckedItems();
        if (testMethods.size() != 1) {
            Message.obtain(handler, BaseActivity.NEED_CHECK_ONE).sendToTarget();
            return;
        }

        TestMethod testMethod = testMethods.get(0);

        if (testMethodUpdateWindow == null) {
            testMethodUpdateWindow = new TestMethodUpdateWindow(getActivity(), waveLengthHelper);
            testMethodUpdateWindow.setOnSaveListener(new TestMethodUpdateWindow.OnSaveListener() {
                @Override
                public void onSave(TestMethod testMethod) {
                    testMethodHelper.updateTestMethod(testMethod);
                    if (testMethod.getTestName().equals(Cache.getTestMethod().getTestName())) {
                        Log.d("on save", String.format("onSave[%s][%s]", Cache.getTestName(), testMethod.getTestName()));
                        Cache.setTestMethod(testMethod);
                        SettingConfig settingConfig = new SettingConfig();
                        settingConfig.setTestId(Cache.getTestId());
                        settingConfig.setTestName(Cache.getTestName());
                        settingConfig.setTestMethod(Cache.getTestMethod());
                        User user = Cache.getUser();
                        user.setSettingConfig(JsonHelper.toJson(settingConfig));
                        DBService.getUserHelper(getActivity()).updateUser(user);
                        if (testMethod.getTestName().equals(Cache.getTestMethod().getTestName())) {
                            Formula formula = Cache.getFormulaByName(testMethod.getFormulaName());
                            if (!(formula instanceof OpticalRotation) &&
                                    !(formula instanceof Concentration) &&
                                    !(formula instanceof SpecificRotation)) {
                                formula = DBService.getFormulaHelper(getActivity()).findFormula(testMethod.getFormulaName());
                                Cache.setCurrentFormula(formula);
                            } else {
                                Cache.setCurrentFormula(formula);
                            }
                        }
                    } else {
                        Log.d("on save1", String.format("onSave[%s][%s]", Cache.getTestName(), testMethod.getTestName()));
                    }
                    AuditService.addAuditService(Cache.getUser().getUserName(), getActivity().getString(R.string.setting), getActivity().getString(R.string.method_manager), getString(R.string.method_setting_update), Cache.currentTime(), auditHelper);
                    testMethodUpdateWindow.dismiss();
                    testMethodService.onChange(R.id.setting_method_detail_btn);
                    Message.obtain(handler, BaseActivity.SAVE_SUCCESS).sendToTarget();
                }
            });
        }
        testMethodUpdateWindow.show(testMethod);
    }

    public void add() {
        if (testMethodAddWindow == null) {
            testMethodAddWindow = new TestMethodAddWindow(getActivity(), waveLengthHelper);
        }
        testMethodAddWindow.show();
        testMethodAddWindow.setOnSaveListener(new TestMethodAddWindow.OnSaveListener() {
            @Override
            public void onSave(TestMethod testMethod) {
                doSave(testMethod);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_method_next_btn:
                showNextPage();
                reshowMethodBtn();
                break;
            case R.id.setting_method_prev_btn:
                showPresPage();
                reshowMethodBtn();
                break;
            case R.id.setting_method_search_btn:
                search();
                break;
            case R.id.setting_method_detail_btn:
                showDetail();
                break;
            case R.id.setting_method_delete_btn:
                delete();
                break;
            case R.id.setting_method_add_btn:
                add();
                break;
        }
    }

    private void search() {
        if (searchView == null) {
            searchView = new SimpleDateSearch(this.getActivity());
            searchView.setOnSearchListener(new SimpleDateSearch.OnSearchListener() {
                @Override
                public void onSearch(final String startDate, final String endDate, final String creator) {
                    SettingMethodFragment.this.startDate = startDate;
                    SettingMethodFragment.this.endDate = endDate;
                    SettingMethodFragment.this.creator = creator;
                    doSearch();
                    searchView.dismiss();
                }
            });
        }

        searchView.show();
    }

    private void doSearch() {
        new Thread() {
            @Override
            public void run() {
                count = testMethodHelper.count(startDate, endDate, creator);
                currentPage = 1;
                testMethodList.clear();
                testMethodList.addAll(testMethodHelper.list(startDate, endDate, creator, currentPage, pageSize));
                Message.obtain(handler, BaseActivity.NEED_REFRESH).sendToTarget();
            }
        }.start();
    }

    private void delete() {
        if (adapter.getCheckedItems().size() == 0) {
            ToastUtil.toastShow(mToast, getString(R.string.no_need_to_delete));
            return;
        }
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
                        if (chooseAll.isChecked() && adapter.getCheckedItems().size() == testMethodList.size()) {
                            deleteAll();
                        } else {
                            List<TestMethod> methods = adapter.getCheckedItems();
                            List<Integer> ids = new ArrayList<>();
                            for (TestMethod testMethod : methods) {
                                ids.add(testMethod.getId());
                            }
                            testMethodHelper.deleteTestMethods(ids);
                        }
                        testMethodList.clear();
                        count = testMethodHelper.count();
                        if (currentPage * pageSize > count && currentPage > 1) {
                            currentPage = currentPage - 1;
                        }
                        testMethodList.addAll(testMethodHelper.list(currentPage, pageSize));
                        testMethodService.onChange(R.id.setting_method_detail_btn);
                        AuditService.addAuditService(Cache.getUser().getUserName(), getActivity().getString(R.string.setting), getActivity().getString(R.string.method_manager), getString(R.string.delete), Cache.currentTime(), auditHelper);
                        Message.obtain(handler, BaseActivity.DELETE_SUCCESS).sendToTarget();
                    }
                }
        );
    }

    public void doSave(TestMethod testMethod) {
        testMethodHelper.addTestMethod(testMethod);
        AuditService.addAuditService(Cache.getUser().getUserName(), getActivity().getString(R.string.setting), getActivity().getString(R.string.method_manager), getString(R.string.add), Cache.currentTime(), auditHelper);
        testMethodAddWindow.dismiss();
        testMethodService.onChange(R.id.setting_method_detail_btn);
        Message.obtain(handler, BaseActivity.SAVE_SUCCESS).sendToTarget();
    }

    private void deleteAll() {
        testMethodHelper.deleteAll(startDate, endDate, creator);
    }

    private void showNextPage() {
        ExecutorService.getInstance().execute(
                new Runnable() {
                    @Override
                    public void run() {
                        List<TestMethod> testMethod1 = testMethodHelper.list(startDate, endDate, creator, currentPage + 1, pageSize);
                        testMethodList.clear();
                        testMethodList.addAll(testMethod1);

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
                        List<TestMethod> testMethod1 = testMethodHelper.list(startDate, endDate, creator, currentPage - 1, pageSize);

                        testMethodList.clear();
                        testMethodList.addAll(testMethod1);
                        currentPage = currentPage - 1;
                        adapter.setCheckedPage(false);
                        Message message = Message.obtain(handler, BaseActivity.NEED_REFRESH);
                        message.sendToTarget();
                    }
                }
        );
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.setting_method_singlepage_check:
                adapter.setCheckedPage(isChecked);
                adapter.notifyDataSetChanged();
                break;
            case R.id.setting_method_allpage_check:
                adapter.setCheckedAll(isChecked);
                adapter.notifyDataSetChanged();
                break;
        }
    }

    public class SettingMethodHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BaseActivity.NEED_REFRESH:
                    adapter.notifyDataSetChanged();
                    choosePage.setChecked(false);
                    reshowMethodBtn();
                    refreshPageCount();
                    break;
                case BaseActivity.NEED_CHECK_ONE:
                    ToastUtil.toastShow(mToast, getString(R.string.only_one_checked));
                    break;
                case BaseActivity.DELETE_SUCCESS:
                    ToastUtil.toastShow(mToast, getString(R.string.delete_success));
                    choosePage.setChecked(false);
                    chooseAll.setChecked(false);
                    adapter.setCheckedPage(false);
                    adapter.setCheckedAll(false);
                    adapter.notifyDataSetChanged();
                    refreshPageCount();
                    reshowMethodBtn();
                    break;
                case BaseActivity.SAVE_SUCCESS:
                    adapter.notifyDataSetChanged();
                    choosePage.setChecked(false);
                    reshowMethodBtn();
                    refreshPageCount();
                    ToastUtil.toastShow(mToast, getString(R.string.save_success));
                    break;
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        testMethodList.clear();
        testMethodList.addAll(testMethodHelper.list(currentPage, pageSize));
        count = testMethodHelper.count();
        refreshPageCount();
        adapter.notifyDataSetChanged();
        reshowMethodBtn();
    }

    @Override
    public void onResume() {
        super.onResume();
        testMethodList.clear();
        testMethodList.addAll(testMethodHelper.list(currentPage, pageSize));
        count = testMethodHelper.count();
        refreshPageCount();
        reshowMethodBtn();
        adapter.notifyDataSetChanged();
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
        data_fragement_page.setText(currentPage + "/" + pageCount + " " + page);
    }
}
