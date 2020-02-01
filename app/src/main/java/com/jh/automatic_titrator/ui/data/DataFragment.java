package com.jh.automatic_titrator.ui.data;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.common.Cache;
import com.jh.automatic_titrator.common.db.AuditHelper;
import com.jh.automatic_titrator.common.db.MD5Helper;
import com.jh.automatic_titrator.common.db.TestHelper;
import com.jh.automatic_titrator.common.file.ExportResult;
import com.jh.automatic_titrator.common.file.FileHelper;
import com.jh.automatic_titrator.common.file.PDFTemplete;
import com.jh.automatic_titrator.common.printer.PrinterTask;
import com.jh.automatic_titrator.common.printer.Sensitive;
import com.jh.automatic_titrator.common.utils.StringUtils;
import com.jh.automatic_titrator.common.utils.TimeTool;
import com.jh.automatic_titrator.common.utils.ToastUtil;
import com.jh.automatic_titrator.entity.common.Test;
import com.jh.automatic_titrator.entity.common.UDisk;
import com.jh.automatic_titrator.service.AuditService;
import com.jh.automatic_titrator.service.DBService;
import com.jh.automatic_titrator.service.ExecutorService;
import com.jh.automatic_titrator.service.SystemService;
import com.jh.automatic_titrator.ui.BaseActivity;
import com.jh.automatic_titrator.ui.listener.KeyboardDismiss;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 16/9/17.
 */
public class DataFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private static final int EXPORT_COMPLETED = 110010;

    TestHelper testHelper;

    private View view;

    private List<Test> tests;

    private int currentPage = 1;

    private int count;

    private int pageSize = 15;

    private int pageCount = 0;

    private DataListAdapter adapter;

    private ListView data_manager_lv;

    private TextView data_fragement_page;

    private View nextPage;

    private View presPage;

    private View deleteChoose;

    private CheckBox chooseAll;

    private CheckBox choosePage;

    private DataHandler handler;

    private int pageBtnWidth;

    private String page;

    private View data_export_btn;
    private View data_print_btn;
    private View data_sensitive_btn;
    private View data_search_btn;
    private View data_detail_btn;
    private View data_delete_btn;

    private String startDate;

    private String endDate;

    private String creator;

    private String sampleName;

    private Double gt;

    private Double lt;

    private Handler activityHandler;

    private DataExport dataExport;

    private DataPrint dataPrint;

    private DataSearch dataSearch;

    private AuditHelper auditHelper;

    private ProgressDialog progressDialog;

    private Toast mToast;

    private KeyboardDismiss keyboardDismiss;

    public void setActivityHandler(Handler activityHandler) {
        this.activityHandler = activityHandler;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.data_fragment, container, false);

        mToast = ToastUtil.createToast(getActivity());
        keyboardDismiss = new KeyboardDismiss(getActivity());

        testHelper = DBService.getTestHelper(getActivity());
        auditHelper = DBService.getAuditHelper(getActivity());
        tests = testHelper.listTest(currentPage, pageSize);

        data_fragement_page = (TextView) view.findViewById(R.id.data_fragement_page);

        count = testHelper.count();

        page = getActivity().getResources().getString(R.string.page);

        adapter = new DataListAdapter(tests, this.getActivity());
        data_manager_lv = (ListView) view.findViewById(R.id.data_manager_lv);
        data_manager_lv.setAdapter(adapter);

        choosePage = (CheckBox) view.findViewById(R.id.data_list_choosepage);
        choosePage.setOnCheckedChangeListener(this);
        chooseAll = (CheckBox) view.findViewById(R.id.data_list_chooseall);
        chooseAll.setOnCheckedChangeListener(this);

        nextPage = view.findViewById(R.id.data_list_next_btn);
        pageBtnWidth = nextPage.getWidth();
        presPage = view.findViewById(R.id.data_list_prev_btn);
        nextPage.setOnClickListener(this);
        presPage.setOnClickListener(this);

        data_export_btn = view.findViewById(R.id.data_export_btn);
        data_export_btn.setOnClickListener(this);
        data_print_btn = view.findViewById(R.id.data_print_btn);
        data_print_btn.setOnClickListener(this);
        data_sensitive_btn = view.findViewById(R.id.data_sensitive_btn);
        data_sensitive_btn.setOnClickListener(this);
        data_search_btn = view.findViewById(R.id.data_search_btn);
        data_search_btn.setOnClickListener(this);
        data_detail_btn = view.findViewById(R.id.data_detail_btn);
        data_detail_btn.setOnClickListener(this);
        data_delete_btn = view.findViewById(R.id.data_delete_btn);
        data_delete_btn.setOnClickListener(this);

        handler = new DataHandler();
        ensureBtnShow();
        return view;
    }

    private void ensureBtnShow() {
        if (!Cache.containsAuth("dataexport")) {
            data_export_btn.setVisibility(View.GONE);
        }
        if (!Cache.containsAuth("dataprint")) {
            data_print_btn.setVisibility(View.GONE);
            data_sensitive_btn.setVisibility(View.GONE);
        }
        if (!Cache.containsAuth("datasearch")) {
            data_search_btn.setVisibility(View.GONE);
        }
        if (!Cache.containsAuth("datadelete")) {
            data_delete_btn.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        refreshPageCount();
        reshowDataBtn();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.data_list_next_btn:
                showNextPage();
                reshowDataBtn();
                break;
            case R.id.data_list_prev_btn:
                showPresPage();
                reshowDataBtn();
                break;
            case R.id.data_export_btn:
                export();
                break;
            case R.id.data_print_btn:
                print();
                break;
            case R.id.data_sensitive_btn:
                sensitive();
                break;
            case R.id.data_search_btn:
                search();
                break;
            case R.id.data_detail_btn:
                showDetail();
                break;
            case R.id.data_delete_btn:
                delete();
                break;
        }
    }

    private void delete() {
        if (adapter.getCheckedList().size() == 0) {
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
                        if (chooseAll.isChecked()) {
                            deleteAll();
                        } else {
                            List<Test> tests = adapter.getCheckedList();
                            List<Integer> ids = new ArrayList<>();
                            for (Test test : tests) {
                                ids.add(test.getId());
                            }
                            testHelper.deleteTest(ids);
                        }
                        tests.clear();
                        count = testHelper.count();
                        if (currentPage * pageSize > count && currentPage > 1) {
                            currentPage = currentPage - 1;
                        }
                        tests.addAll(testHelper.listTest(currentPage, pageSize));
                        AuditService.addAuditService(Cache.getUser().getUserName(), getString(R.string.data), null, getString(R.string.data_delete), Cache.currentTime(), auditHelper);
                        Message.obtain(handler, BaseActivity.DELETE_SUCCESS).sendToTarget();
                    }
                }
        );
    }

    private void showDetail() {

        if (adapter.getCheckedList().size() != 1) {
            Message.obtain(handler, BaseActivity.NEED_CHECK_ONE).sendToTarget();
            return;
        }

        Test test = adapter.getCheckedList().get(0);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.data_detail, null);
        TextView testname = (TextView) view.findViewById(R.id.data_detail_name);
        testname.setText(test.getTestName());
        TextView testid = (TextView) view.findViewById(R.id.data_detail_sort_number);
        testid.setText(test.getId() + "");
        TextView sampleNo = (TextView) view.findViewById(R.id.data_detail_sample_no);
        sampleNo.setText(test.getTestId());
        TextView testMethod = (TextView) view.findViewById(R.id.data_detail_test_method);
        testMethod.setText(test.getTestMethod());
        TextView createdate = (TextView) view.findViewById(R.id.data_detail_date);
        createdate.setText(TimeTool.dateToDate(test.getDate()));
        TextView createtime = (TextView) view.findViewById(R.id.data_detail_time);
        createtime.setText(TimeTool.dateToTime(test.getDate()));
        TextView temperature = (TextView) view.findViewById(R.id.data_detail_temperature);
        temperature.setText(test.getRealTemperature());
        TextView temperature1 = (TextView) view.findViewById(R.id.data_detail_temperature_setting);
        temperature1.setText(test.getWantedTemperature());
        TextView concentration = (TextView) view.findViewById(R.id.data_detail_concentration);
        if (test.getConcentration() != null) {
            concentration.setText(test.getConcentration());
        } else {
            concentration.setText("");
        }
        TextView opticalRotation = (TextView) view.findViewById(R.id.data_detail_automatic_titrator);
        opticalRotation.setText(String.format("%." + test.getDecimal() + "f", test.getOptical()));
        TextView testTubeLength = (TextView) view.findViewById(R.id.data_detail_testtubelength);
        if (test.getTubelength() != null) {
            testTubeLength.setText(test.getTubelength());
        } else {
            testTubeLength.setText("");
        }
        TextView specificRotation = (TextView) view.findViewById(R.id.data_detail_specific_rotation);
        if (test.getSpecificrotation() != null) {
            specificRotation.setText(test.getSpecificrotation());
        } else {
            specificRotation.setText("");
        }
        TextView waveLength = (TextView) view.findViewById(R.id.data_detail_wavelength);
        waveLength.setText(test.getWavelength() + "nm");
        TextView resulttype = (TextView) view.findViewById(R.id.data_detail_resType);
        if (StringUtils.isNotEmpty(test.getSimpleFormulaName())) {
            resulttype.setText(test.getFormulaName() + "(" + test.getSimpleFormulaName() + ")");
        } else {
            resulttype.setText(test.getFormulaName());
        }
        TextView result = (TextView) view.findViewById(R.id.data_detail_res);
        result.setText(String.format("%." + test.getDecimal() + "f%s", test.getRes(), test.getFormulaUnit()));
        TextView creator = (TextView) view.findViewById(R.id.data_detail_creator);
        creator.setText(test.getTestCreator());
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle(test.getTestName() + "-" + test.getTestId());

        builder.setView(view);
        builder.create().show();
        ExecutorService.getInstance().execute(
                new Runnable() {
                    @Override
                    public void run() {
                        AuditService.addAuditService(Cache.getUser().getUserName(), getString(R.string.data), null, getString(R.string.data_detail), Cache.currentTime(), auditHelper);
                    }
                }
        );
    }

    public void reshowDataBtn() {
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

    private void showNextPage() {
        ExecutorService.getInstance().execute(
                new Runnable() {
                    @Override
                    public void run() {
                        List<Test> tests1 = testHelper.listTest(currentPage + 1, pageSize);
                        tests.clear();
                        tests.addAll(tests1);
                        currentPage = currentPage + 1;
                        adapter.setCheckedPage(false);
                        Message.obtain(handler, BaseActivity.NEED_REFRESH).sendToTarget();
                    }
                }
        );
    }

    private void showPresPage() {
        ExecutorService.getInstance().execute(
                new Runnable() {
                    @Override
                    public void run() {
                        List<Test> tests1 = testHelper.listTest(currentPage - 1, pageSize);

                        tests.clear();
                        tests.addAll(tests1);
                        currentPage = currentPage - 1;
                        adapter.setCheckedPage(false);
                        Message.obtain(handler, BaseActivity.NEED_REFRESH).sendToTarget();
                    }
                }
        );
    }

    private void deleteAll() {
        testHelper.deleteAll(startDate, endDate, creator);
    }

    private void export() {
        final List<Test> tests = adapter.getCheckedList();
        if (tests.size() > 0) {
            final List<UDisk> uDisks = SystemService.getUDisksNew(getActivity());
            if (uDisks == null || uDisks.size() == 0) {
                Message.obtain(handler, BaseActivity.SHOW_MSG, getString(R.string.udisk_not_found)).sendToTarget();
                return;
            }
            if (dataExport == null) {
                DisplayMetrics outMetrics = new DisplayMetrics();
                DataFragment.this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
                float dpx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, DataFragment.this.getActivity().getResources().getDisplayMetrics());
                dataExport = new DataExport(DataFragment.this.getActivity(), (int) (300 * dpx), (int) (480 * dpx), true);
            }
            dataExport.setTouchable(true);
            dataExport.setFocusable(true);
            dataExport.setOutsideTouchable(true);
            dataExport.setBackgroundDrawable(new BitmapDrawable());
            dataExport.setOnExportListener(new DataExport.OnExportListener() {
                @Override
                public void onExport(final String fileName, final int type, String title1, String title2, String title3) {
                    List<Test> tests1;
                    if (chooseAll.isChecked()) {
                        tests1 = testHelper.listTest(startDate, endDate, sampleName, gt, lt, creator);
                    } else {
                        tests1 = new ArrayList<Test>();
                        tests1.addAll(adapter.getCheckedList());
                    }
                    switch (type) {
                        case DataExport.EXPORT_EXCEL_SIMPLE:
//                        exportToExcelSimple(getTempTests(), getTempFileDir(), fileName, title1, title2, title3);
                            exportToExcelSimple(tests1, uDisks.get(0).getFilePath(), fileName, title1, title2, title3);
                            break;
                        case DataExport.EXPORT_EXCEL_DETAIL:
//                        exportToExcelDetail(getTempTests(), getTempFileDir(), fileName, title1, title2, title3);
                            exportToExcelDetail(tests1, uDisks.get(0).getFilePath(), fileName, title1, title2, title3);
                            break;
                        case DataExport.EXPORT_PDF_HSIMPLE:
//                        exportToPDFHSimple(getTempTests(), getTempFileDir(), fileName, title1, title2, title3);
                            exportToPDFHSimple(tests1, uDisks.get(0).getFilePath(), fileName, title1, title2, title3);
                            break;
                        case DataExport.EXPORT_PDF_VSIMPLE:
//                        exportToPDFVSimple(getTempTests(), getTempFileDir(), fileName, title1, title2, title3);
                            exportToPDFVSimple(tests1, uDisks.get(0).getFilePath(), fileName, title1, title2, title3);
                            break;
                        case DataExport.EXPORT_PDF_VDETAIL:
//                        exportToPDFVDetail(getTempTests(), getTempFileDir(), fileName, title1, title2, title3);
                            exportToPDFVDetail(tests1, uDisks.get(0).getFilePath(), fileName, title1, title2, title3);
                            break;
                    }
                    AuditService.addAuditService(Cache.getUser().getUserName(), getString(R.string.data), null, getString(R.string.data_export), Cache.currentTime(), auditHelper);
                    dataExport.dismiss();
                }
            });
            dataExport.setOnDismissListener(keyboardDismiss);
            dataExport.showAtLocation(view, Gravity.CENTER, 0, 0);
        } else {
            Message.obtain(handler, BaseActivity.SHOW_MSG, getString(R.string.checked_ensure)).sendToTarget();
        }
    }

    private void exportToExcelSimple(final List<Test> tests, final String fileDir, final String fileName, String title1, String title2, String title3) {
        try {
            Message.obtain(handler, BaseActivity.HOLD_WINDOW_YES, getString(R.string.exporting_excel)).sendToTarget();
            FileHelper.writeAsExcelSimple(DataFragment.this.getActivity(), fileDir, fileName, title1, title2, title3, Cache.getUser().getUserName(), tests);
            Message.obtain(handler, EXPORT_COMPLETED, getString(R.string.export_success));
            Message.obtain(handler, BaseActivity.HOLD_WINDOW_NO).sendToTarget();
        } catch (Exception e) {
            e.printStackTrace();
            Message.obtain(handler, EXPORT_COMPLETED, e);
            Message.obtain(handler, BaseActivity.HOLD_WINDOW_NO).sendToTarget();
        }
    }

    private void exportToExcelDetail(final List<Test> tests, final String fileDir, final String fileName, String title1, String title2, String title3) {
        try {
            Message.obtain(handler, BaseActivity.HOLD_WINDOW_YES, getString(R.string.exporting_excel)).sendToTarget();
            FileHelper.writeAsExcelDetail(DataFragment.this.getActivity(), fileDir, fileName, title1, title2, title3, Cache.getUser().getUserName(), tests);
            Message.obtain(handler, EXPORT_COMPLETED, getString(R.string.export_success));
            Message.obtain(handler, BaseActivity.HOLD_WINDOW_NO).sendToTarget();
        } catch (Exception e) {
            e.printStackTrace();
            Message.obtain(handler, EXPORT_COMPLETED, e);
            Message.obtain(handler, BaseActivity.HOLD_WINDOW_NO).sendToTarget();
        }
    }

    private void exportToPDFHSimple(final List<Test> tests, final String fileDir, final String fileName, final String title1, final String title2, final String title3) {
        PDFTemplete.writePDFHorizentalSimple(getActivity(), fileDir, fileName, title1, title2, title3, Cache.getUser(), R.string.gen_pdf_file, tests, new ExportResult() {
            @Override
            public void failed(String msg) {
                Message.obtain(handler, EXPORT_COMPLETED, msg).sendToTarget();
            }

            @Override
            public void success() {
                if (fileDir.endsWith("/")) {
                    MD5Helper.checkSum(fileDir + fileName);
                } else {
                    MD5Helper.checkSum(fileDir + "/" + fileName);
                }
                Message.obtain(handler, EXPORT_COMPLETED, getString(R.string.save_success)).sendToTarget();
            }
        });
        Message.obtain(handler, EXPORT_COMPLETED, getString(R.string.export_success));
    }

    private void exportToPDFVSimple(final List<Test> tests, final String fileDir, final String fileName, final String title1, final String title2, final String title3) {
        PDFTemplete.writePDFVerticalSimple(getActivity(), fileDir, fileName, title1, title2, title3, Cache.getUser(), R.string.gen_pdf_file, tests, new ExportResult() {
            @Override
            public void failed(String msg) {
                Message.obtain(handler, EXPORT_COMPLETED, msg).sendToTarget();
            }

            @Override
            public void success() {
                if (fileDir.endsWith("/")) {
                    MD5Helper.checkSum(fileDir + fileName);
                } else {
                    MD5Helper.checkSum(fileDir + "/" + fileName);
                }
                Message.obtain(handler, EXPORT_COMPLETED, getString(R.string.save_success)).sendToTarget();
            }
        });
        Message.obtain(handler, EXPORT_COMPLETED, getString(R.string.export_success));
    }

    private void exportToPDFVDetail(final List<Test> tests, final String fileDir, final String fileName, final String title1, final String title2, final String title3) {
        PDFTemplete.writePDFVerticalDetail(getActivity(), fileDir, fileName, title1, title2, title3, Cache.getUser(), R.string.gen_pdf_file, tests, new ExportResult() {
            @Override
            public void failed(String msg) {
                Message.obtain(handler, EXPORT_COMPLETED, msg).sendToTarget();
            }

            @Override
            public void success() {
                if (fileDir.endsWith("/")) {
                    MD5Helper.checkSum(fileDir + fileName);
                } else {
                    MD5Helper.checkSum(fileDir + "/" + fileName);
                }
                Message.obtain(handler, EXPORT_COMPLETED, getString(R.string.save_success)).sendToTarget();
            }
        });
    }

    private void print() {
        final List<Test> tests = adapter.getCheckedList();
        if (tests.size() > 0) {
            if (dataPrint == null) {
                DisplayMetrics outMetrics = new DisplayMetrics();
                this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
                float dpx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, this.getActivity().getResources().getDisplayMetrics());
                dataPrint = new DataPrint(this.getActivity(), (int) (300 * dpx), (int) (350 * dpx), true);
            }
            dataPrint.setTouchable(true);
            dataPrint.setFocusable(true);
            dataPrint.setOutsideTouchable(true);
            dataPrint.setBackgroundDrawable(new BitmapDrawable());
            dataPrint.setOnPrintListener(new DataPrint.OnPrintListener() {
                @Override
                public void onPrint(final int type, final String title1, final String title2, final String title3) {
                    switch (type) {
                        case DataPrint.PRINT_HORIZONTAL_SIMPLE:
//                        printHorizontalSimple(getTempTests(), title1, title2, title3);
                            printHorizontalSimple(tests, title1, title2, title3);
                            break;
                        case DataPrint.PRINT_VERTICAL_SIMPLE:
//                        printVerticalSimple(getTempTests(), title1, title2, title3);
                            printVerticalSimple(tests, title1, title2, title3);
                            break;
                        case DataPrint.PRINT_VERTICAL_DETAIL:
//                        printVerticalDetail(getTempTests(), title1, title2, title3);
                            printVerticalDetail(tests, title1, title2, title3);
                    }
                    AuditService.addAuditService(Cache.getUser().getUserName(), getString(R.string.data), null, getString(R.string.data_print), Cache.currentTime(), auditHelper);
                }
            });
            dataPrint.setOnDismissListener(keyboardDismiss);
            dataPrint.showAtLocation(view, Gravity.CENTER, 0, 0);
        } else {
            ToastUtil.toastShow(mToast, getString(R.string.checked_ensure));
        }
    }

    private void sensitive() {
        final List<Test> tests = adapter.getCheckedList();
        if (tests.size() > 0) {
            Message.obtain(handler, BaseActivity.HOLD_WINDOW_YES, getString(R.string.exporting_excel)).sendToTarget();
            ExecutorService.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    Sensitive.printTests(DataFragment.this.getActivity(), tests);
                    Message.obtain(handler, EXPORT_COMPLETED, getString(R.string.export_success));
                }
            });

            Message.obtain(handler, BaseActivity.HOLD_WINDOW_NO).sendToTarget();
        } else {
            ToastUtil.toastShow(mToast, getString(R.string.checked_ensure));
        }
    }

    private void printHorizontalSimple(final List<Test> tests, final String title1, final String title2, final String title3) {
        final String tempDir = getTempFileDir();
        PDFTemplete.writePDFHorizentalSimple(getActivity(), tempDir, "temp", title1, title2, title3, Cache.getUser(), R.string.gen_pdf_file, tests, new ExportResult() {
            @Override
            public void failed(String msg) {
                Message.obtain(handler, EXPORT_COMPLETED, msg).sendToTarget();
            }

            @Override
            public void success() {
                String fileName = tempDir + "temp.pdf";
                Message.obtain(handler, EXPORT_COMPLETED, getString(R.string.changing_to_print)).sendToTarget();
                PrinterTask.printPDF(getActivity(), fileName);
            }
        });
    }

    private void printVerticalSimple(final List<Test> tests, final String title1, final String title2, final String title3) {
        final String tempDir = getTempFileDir();
        PDFTemplete.writePDFVerticalSimple(getActivity(), tempDir, "temp", title1, title2, title3, Cache.getUser(), R.string.gen_pdf_file, tests, new ExportResult() {
            @Override
            public void failed(String msg) {
                Message.obtain(handler, EXPORT_COMPLETED, msg).sendToTarget();
            }

            @Override
            public void success() {
                String fileName = tempDir + "temp.pdf";
                Message.obtain(handler, EXPORT_COMPLETED, getString(R.string.changing_to_print)).sendToTarget();
                PrinterTask.printPDF(getActivity(), fileName);
            }
        });
    }

    private void printVerticalDetail(final List<Test> tests, final String title1, final String title2, final String title3) {
        final String tempDir = getTempFileDir();
        PDFTemplete.writePDFVerticalDetail(getActivity(), tempDir, "temp", title1, title2, title3, Cache.getUser(), R.string.gen_pdf_file, tests, new ExportResult() {
            @Override
            public void failed(String msg) {
                Message.obtain(handler, EXPORT_COMPLETED, msg).sendToTarget();
            }

            @Override
            public void success() {
                String fileName = tempDir + "/temp.pdf";
                Message.obtain(handler, EXPORT_COMPLETED, getString(R.string.changing_to_print)).sendToTarget();
                PrinterTask.printPDF(getActivity(), fileName);
            }
        });
    }

    /**
     * 根据文件后缀名获得对应的MIME类型。
     *
     * @param file
     */
    private String getMIMEType(File file) {

        String type = "*/*";
        String fName = file.getName();
        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }
        /* 获取文件的后缀名 */
        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (end == "") return type;

        return type;
    }

    private void search() {
        if (dataSearch == null) {
            dataSearch = new DataSearch(this.getActivity());
            dataSearch.setOnSearchListener(new DataSearch.OnSearchListener() {
                @Override
                public void onSearch(final String startDate, final String endDate, String sampleName, Double gt, Double lt, final String creator) {
                    DataFragment.this.startDate = startDate;
                    DataFragment.this.endDate = endDate;
                    DataFragment.this.creator = creator;
                    DataFragment.this.sampleName = sampleName;
                    DataFragment.this.gt = gt;
                    DataFragment.this.lt = lt;

                    doSearch();
                    dataSearch.dismiss();
                }
            });
        }
        dataSearch.show();
    }

    private void doSearch() {
        new Thread() {
            @Override
            public void run() {
                count = testHelper.count(startDate, endDate, sampleName, gt, lt, creator);
                currentPage = 1;
                tests.clear();
                tests.addAll(testHelper.listTest(startDate, endDate, sampleName, gt, lt, creator, currentPage, pageSize));
                AuditService.addAuditService(Cache.getUser().getUserName(), getString(R.string.data), null, getString(R.string.data_search), Cache.currentTime(), auditHelper);
                Message.obtain(handler, BaseActivity.NEED_REFRESH).sendToTarget();
            }
        }.start();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        tests.clear();
        tests.addAll(testHelper.listTest(currentPage, pageSize));
        count = testHelper.count();
        refreshPageCount();
        adapter.notifyDataSetChanged();
        reshowDataBtn();
    }

    @Override
    public void onResume() {
        super.onResume();
        tests.clear();
        tests.addAll(testHelper.listTest(currentPage, pageSize));
        count = testHelper.count();
        refreshPageCount();
        adapter.notifyDataSetChanged();
        reshowDataBtn();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.data_list_choosepage:
                adapter.setCheckedPage(isChecked);
                adapter.notifyDataSetChanged();
                break;
            case R.id.data_list_chooseall:
                adapter.setCheckedAll(isChecked);
                adapter.notifyDataSetChanged();
                break;
        }
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

    private List<Test> getTempTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            Test test = new Test();
            test.setDecimal(4);
            test.setId(i);
            test.setDate(TimeTool.currentDate());
            test.setRes(100.1);
            test.setFormulaName("旋光度");
            test.setSimpleFormulaName("α");
            test.setFormulaUnit("°");
            test.setWantedTemperature("12K");
            test.setRealTemperature("12K");
            test.setTestCreator("admin");
            test.setTestId("100" + (100 + i));
            test.setTestMethod("糖度");
            test.setTestName("糖度测试");
            tests.add(test);
        }
        return tests;
    }

    private String getTempFileDir() {
        return Environment.getExternalStorageDirectory() + "/";
    }

    public class DataHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BaseActivity.NEED_REFRESH:
                    adapter.notifyDataSetChanged();
                    choosePage.setChecked(false);
                    refreshPageCount();
                    reshowDataBtn();
                    break;
                case BaseActivity.NEED_CHECK_ONE:
                    ToastUtil.toastShow(mToast, getString(R.string.only_one_checked));
                    break;
                case BaseActivity.DELETE_SUCCESS:
                    ToastUtil.toastShow(mToast, getString(R.string.delete_success));
                    adapter.notifyDataSetChanged();
                    refreshPageCount();
                    reshowDataBtn();
                    break;
                case EXPORT_COMPLETED:
                    Object obj = msg.obj;
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    if (obj instanceof Exception) {
                        ToastUtil.toastShow(mToast, String.valueOf(msg.obj));
                    } else {
                        ToastUtil.toastShow(mToast, String.valueOf(msg.obj));
                    }
                    break;
                case BaseActivity.SHOW_MSG:
                    obj = msg.obj;
                    if (obj != null) {
                        ToastUtil.toastShow(mToast, String.valueOf(msg.obj));
                    }
                    break;
                case BaseActivity.HOLD_WINDOW_YES:
                    if (msg.obj != null) {
                        holdWindow(String.valueOf(msg.obj));
                    }
                    break;
                case BaseActivity.HOLD_WINDOW_NO:
                    dissmisDialog();
                    break;
            }
        }
    }
}
