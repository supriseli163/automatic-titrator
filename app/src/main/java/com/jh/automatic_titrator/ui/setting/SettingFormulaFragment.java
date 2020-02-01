package com.jh.automatic_titrator.ui.setting;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.jh.automatic_titrator.common.db.FormulaHelper;
import com.jh.automatic_titrator.common.utils.ToastUtil;
import com.jh.automatic_titrator.entity.common.Formula;
import com.jh.automatic_titrator.service.AuditService;
import com.jh.automatic_titrator.service.DBService;
import com.jh.automatic_titrator.service.ExecutorService;
import com.jh.automatic_titrator.service.FormulaService;
import com.jh.automatic_titrator.ui.BaseActivity;
import com.jh.automatic_titrator.ui.window.FormulaAddWindow;
import com.jh.automatic_titrator.ui.window.FormulaUpdateWindow;
import com.jh.automatic_titrator.ui.window.SimpleDateSearch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 16/9/24.
 */
public class SettingFormulaFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private int currentPage = 1;

    private int count;

    private int pageSize = 15;

    private int pageCount = 0;

    private SettingFormulaAdapter adapter;

    private ListView formulas_lv;

    private List<Formula> formulaList;

    private SettingFormulaHandler handler;

    private CheckBox choosePage;

    private CheckBox chooseAll;

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

    private AuditHelper auditHelper;

    private FormulaHelper formulaHelper;

    private FormulaAddWindow formulaAddWindow;

    private FormulaUpdateWindow formulaUpdateWindow;

    private FormulaService formulaService;

    private Toast mToast;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.setting_formula_fragment, container, false);
        mToast = ToastUtil.createToast(getActivity());
        handler = new SettingFormulaHandler();
        formulaHelper = DBService.getFormulaHelper(this.getActivity());
        count = formulaHelper.count();
        auditHelper = DBService.getAuditHelper(getActivity());

        page = getActivity().getResources().getString(R.string.page);

        data_fragement_page = (TextView) view.findViewById(R.id.setting_formula_page_info);
        formulas_lv = (ListView) view.findViewById(R.id.setting_formula_manager_lv);
        formulaList = formulaHelper.list(1, pageSize);

        adapter = new SettingFormulaAdapter(this.getActivity(), formulaList, false, false);
        formulas_lv.setAdapter(adapter);
        formulas_lv.setVisibility(View.VISIBLE);

        addBtn = view.findViewById(R.id.setting_formula_add_btn);
        addBtn.setOnClickListener(this);
        deleteBtn = view.findViewById(R.id.setting_formula_delete_btn);
        deleteBtn.setOnClickListener(this);
        detailBtn = view.findViewById(R.id.setting_formula_detail_btn);
        detailBtn.setOnClickListener(this);
        searchBtn = view.findViewById(R.id.setting_formula_search_btn);
        searchBtn.setOnClickListener(this);

        nextPage = view.findViewById(R.id.setting_formula_next_btn);
        nextPage.setOnClickListener(this);
        presPage = view.findViewById(R.id.setting_formula_prev_btn);
        presPage.setOnClickListener(this);

        choosePage = (CheckBox) view.findViewById(R.id.setting_formula_singlepage_check);
        chooseAll = (CheckBox) view.findViewById(R.id.setting_formula_allpage_check);
        choosePage.setOnCheckedChangeListener(this);
        chooseAll.setOnCheckedChangeListener(this);

        formulaService = FormulaService.getInstance();

        ensureBtnShow();

        return view;
    }

    private void ensureBtnShow() {
        if (!Cache.containsAuth("formula")) {
            deleteBtn.setVisibility(View.GONE);
            addBtn.setVisibility(View.GONE);
        } else {
            deleteBtn.setVisibility(View.VISIBLE);
            addBtn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_formula_next_btn:
                showNextPage();
                reshowMethodBtn();
                break;
            case R.id.setting_formula_prev_btn:
                showPresPage();
                reshowMethodBtn();
                break;
            case R.id.setting_formula_search_btn:
                search();
                break;
            case R.id.setting_formula_detail_btn:
                showDetail();
                break;
            case R.id.setting_formula_delete_btn:
                delete();
                break;
            case R.id.setting_formula_add_btn:
                add();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.setting_formula_singlepage_check:
                adapter.setCheckedPage(isChecked);
                adapter.notifyDataSetChanged();
                break;
            case R.id.setting_formula_allpage_check:
                adapter.setCheckedAll(isChecked);
                adapter.notifyDataSetChanged();
                break;
        }
    }

    private void showNextPage() {
        ExecutorService.getInstance().execute(
                new Runnable() {
                    @Override
                    public void run() {
                        List<Formula> formulas1 = formulaHelper.list(currentPage + 1, pageSize);
                        formulaList.clear();
                        formulaList.addAll(formulas1);

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
                        List<Formula> formulas1 = formulaHelper.list(startDate, endDate, creator, currentPage - 1, pageSize);
                        formulaList.clear();
                        formulaList.addAll(formulas1);

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
            searchView = new SimpleDateSearch(getActivity());
            searchView.setOnSearchListener(new SimpleDateSearch.OnSearchListener() {
                @Override
                public void onSearch(final String startDate, final String endDate, final String creator) {
                    SettingFormulaFragment.this.startDate = startDate;
                    SettingFormulaFragment.this.endDate = endDate;
                    SettingFormulaFragment.this.creator = creator;
                    doSearch();
                    searchView.dismiss();
                }
            });
        }

        searchView.show();
    }

    private void doSearch() {
        ExecutorService.getInstance().execute(
                new Runnable() {
                    @Override
                    public void run() {
                        count = formulaHelper.count(startDate, endDate, creator);
                        pageCount = (count + pageSize - 1) / pageSize;
                        currentPage = 1;
                        formulaList.clear();
                        formulaList.addAll(formulaHelper.list(startDate, endDate, creator, currentPage, pageSize));
                        Message.obtain(handler, BaseActivity.NEED_REFRESH).sendToTarget();
                    }
                }
        );
    }

    private void add() {
        if (formulaAddWindow == null) {
            formulaAddWindow = new FormulaAddWindow(getActivity());
            formulaAddWindow.setFormulaAddListener(new FormulaAddWindow.FormulaAddListener() {
                @Override
                public void doAdd(Formula formula) {
                    try {
                        formulaHelper.addFormula(formula);
                        List<Formula> formulas = new ArrayList<Formula>();
                        formulas.addAll(Cache.getBaseFormula());
                        formulas.addAll(formulaHelper.listFormula());
                        Cache.refreshFormulas(formulas);
                        formulaService.notifyChanged();

                        AuditService.addAuditService(Cache.getUser().getUserName(), getActivity().getString(R.string.setting), getActivity().getString(R.string.formula), getString(R.string.add), Cache.currentTime(), auditHelper);
                        formulaList.clear();
                        formulaList.addAll(formulaHelper.list(0, pageSize));
                        count = formulaHelper.count();
                        currentPage = 1;
                        Message.obtain(handler, BaseActivity.SAVE_SUCCESS).sendToTarget();
                    } catch (Exception e) {
                        Message.obtain(handler, BaseActivity.SHOW_MSG, getString(R.string.formula_name) + getString(R.string.already_exist)).sendToTarget();
                    }
                }
            });
        }
        formulaAddWindow.show();
    }

    private void showDetail() {
        if (adapter.getCheckedItems().size() != 1) {
            ToastUtil.toastShow(mToast, getString(R.string.only_one_checked));
        } else {
            final int id = adapter.getCheckedItems().get(0).getId();
            ExecutorService.getInstance().execute(
                    new Runnable() {
                        @Override
                        public void run() {
                            Formula formula = formulaHelper.findFormula(id);
                            if (formula != null) {
                                Message.obtain(handler, BaseActivity.SELECT_SUCCESS, formula).sendToTarget();
                            }
                        }
                    }
            );

        }
    }

    private void showFormula(Formula formula) {
        if (formulaUpdateWindow == null) {
            formulaUpdateWindow = new FormulaUpdateWindow(getActivity());
            formulaUpdateWindow.setFormulaUpdateListener(new FormulaUpdateWindow.FormulaUpdateListener() {

                @Override
                public void doUpdate(Formula formula) {
                    Formula formula1 = adapter.getCheckedItems().get(0);
                    formulaHelper.updateFormula(formula1.getFormulaName(), formula);
                    List<Formula> formulas = new ArrayList<Formula>();
                    formulas.addAll(Cache.getBaseFormula());
                    formulas.addAll(formulaHelper.listFormula());
                    Cache.refreshFormulas(formulas);
//                    Cache.refreshFormulas(formulaHelper.listFormula());
                    formulaService.notifyChanged();
                    AuditService.addAuditService(Cache.getUser().getUserName(), getActivity().getString(R.string.setting), getActivity().getString(R.string.formula), getString(R.string.formula_setting_updpate), Cache.currentTime(), auditHelper);
                    formulaList.clear();
                    formulaList.addAll(formulaHelper.list(0, pageSize));
                    count = formulaHelper.count();
                    currentPage = 1;
                    formulaService.notifyChanged();
                    Message.obtain(handler, BaseActivity.SAVE_SUCCESS).sendToTarget();
                }
            });
        }
        formulaUpdateWindow.show(formula);
    }

    private void delete() {
        if (adapter.getCheckedItems().size() == 0) {
            ToastUtil.toastShow(mToast, getString(R.string.no_need_to_delete));
            return;
        }
        if (Cache.isTesting()) {
            ToastUtil.toastShow(mToast, getString(R.string.testing_delete_unlimit));
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
        if (Cache.isTesting()) {
            Message.obtain(handler, BaseActivity.SHOW_MSG, getString(R.string.testing_delete_unlimit)).sendToTarget();
            return;
        }
        ExecutorService.getInstance().execute(
                new Runnable() {
                    @Override
                    public void run() {
                        if (chooseAll.isChecked() && adapter.getCheckedItems().size() == formulaList.size()) {
                            formulaHelper.deleteFormula(startDate, endDate, creator);
                        } else {
                            List<Formula> formulas = adapter.getCheckedItems();
                            List<Integer> ids = new ArrayList<>();
                            for (Formula formula : formulas) {
                                ids.add(formula.getId());
                            }
                            if (ids.contains(1)) {
                                Message.obtain(handler, BaseActivity.SHOW_MSG, getString(R.string.sugger_cannot_delete)).sendToTarget();
                                return;
                            }
                            formulaHelper.deleteFormula(ids);
                        }
                        formulaService.notifyChanged();
                        formulaList.clear();
                        count = formulaHelper.count();
                        if (currentPage * pageSize > count && currentPage > 1) {
                            currentPage = currentPage - 1;
                        }
                        formulaList.addAll(formulaHelper.list(currentPage, pageSize));
                        List<Formula> formulas = new ArrayList<Formula>();
                        formulas.addAll(Cache.getBaseFormula());
                        formulas.addAll(formulaList);
                        Cache.refreshFormulas(formulas);
                        AuditService.addAuditService(Cache.getUser().getUserName(), getActivity().getString(R.string.setting), getActivity().getString(R.string.formula), getString(R.string.delete), Cache.currentTime(), auditHelper);
                        Message.obtain(handler, BaseActivity.DELETE_SUCCESS).sendToTarget();
                    }
                }
        );
    }

    private class SettingFormulaHandler extends Handler {
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
                    if (formulaAddWindow != null) {
                        formulaAddWindow.hide();
                    }
                    if (formulaUpdateWindow != null) {
                        formulaUpdateWindow.hide();
                    }
                    ToastUtil.toastShow(mToast, getString(R.string.save_success));
                    break;
                case BaseActivity.SELECT_SUCCESS:
                    if (msg.obj != null && msg.obj instanceof Formula) {
                        Formula formula = (Formula) msg.obj;
                        showFormula(formula);
                    }
                    break;
                case BaseActivity.SHOW_MSG:
                    if (msg.obj != null) {
                        ToastUtil.toastShow(mToast, String.valueOf(msg.obj));
                    }
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        formulaList.clear();
        formulaList.addAll(formulaHelper.list(currentPage, pageSize));
        count = formulaHelper.count();
        refreshPageCount();
        adapter.notifyDataSetChanged();
        reshowMethodBtn();
    }

    @Override
    public void onResume() {
        super.onResume();
        formulaList.clear();
        formulaList.addAll(formulaHelper.list(currentPage, pageSize));
        count = formulaHelper.count();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (formulaAddWindow != null) {
            formulaAddWindow.destory();
        }
        if (formulaUpdateWindow != null) {
            formulaUpdateWindow.destory();
        }
    }
}
