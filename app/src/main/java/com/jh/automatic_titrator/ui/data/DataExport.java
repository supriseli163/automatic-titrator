package com.jh.automatic_titrator.ui.data;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.Toast;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.common.utils.ToastUtil;
import com.jh.automatic_titrator.ui.BaseActivity;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by apple on 2016/10/28.
 */
public class DataExport extends PopupWindow implements View.OnClickListener {

    public static final int EXPORT_EXCEL_SIMPLE = 1;
    public static final int EXPORT_EXCEL_DETAIL = 2;
    public static final int EXPORT_PDF_HSIMPLE = 3;
    public static final int EXPORT_PDF_VSIMPLE = 4;
    public static final int EXPORT_PDF_VDETAIL = 5;

    private View view;

    private OnExportListener onExportListener;

    private RadioButton typeExcelSimple;
    private RadioButton typeExcelDetail;
    private RadioButton typePDFHSimple;
    private RadioButton typePDFVSimple;
    private RadioButton typePDFVDetail;

    private EditText fileName;

    private View saveBtn;

    private Activity activity;

    private DataExportHandler handler;

    private EditText mTitle1;
    private EditText mTitle2;
    private EditText mTitle3;

    private Toast mToast;

    public DataExport(Activity activity, int width, int height, boolean focusable) {
        this(activity, LayoutInflater.from(activity).inflate(
                R.layout.data_export_popup, null), width, height, focusable);
    }

    public DataExport(Activity activity, View contentView, int width, int height, boolean focusable) {
        this(activity, contentView, width, height, focusable, new HashSet<String>());
    }

    public DataExport(Activity activity, View contentView, int width, int height, boolean focusable, Set<String> auth) {
        super(contentView, width, height, focusable);
        mToast = ToastUtil.createToast(activity);
        view = contentView;
        this.activity = activity;
        handler = new DataExportHandler();
        typeExcelSimple = (RadioButton) view.findViewById(R.id.data_export_export_type_excel_simple);
        typeExcelDetail = (RadioButton) view.findViewById(R.id.data_export_export_type_excel_detail);
        typePDFHSimple = (RadioButton) view.findViewById(R.id.data_export_export_type_pdf_hsimple);
        typePDFVSimple = (RadioButton) view.findViewById(R.id.data_export_export_type_pdf_vsimple);
        typePDFVDetail = (RadioButton) view.findViewById(R.id.data_export_export_type_pdf_vdetail);

        mTitle1 = (EditText) view.findViewById(R.id.data_export_title1);
        mTitle2 = (EditText) view.findViewById(R.id.data_export_title2);
        mTitle3 = (EditText) view.findViewById(R.id.data_export_title3);

        fileName = (EditText) view.findViewById(R.id.data_export_file_name);

        saveBtn = view.findViewById(R.id.data_export_save_btn);
        saveBtn.setOnClickListener(this);
    }

    public void setOnExportListener(OnExportListener onExportListener) {
        this.onExportListener = onExportListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.data_export_save_btn:
                String fileType = null;
                if (onExportListener != null) {
                    int type = 0;
                    if (typeExcelSimple.isChecked()) {
                        type = EXPORT_EXCEL_SIMPLE;
                    }
                    if (typeExcelDetail.isChecked()) {
                        type = EXPORT_EXCEL_DETAIL;
                    }
                    if (typePDFHSimple.isChecked()) {
                        type = EXPORT_PDF_HSIMPLE;
                    }
                    if (typePDFVSimple.isChecked()) {
                        type = EXPORT_PDF_VSIMPLE;
                    }
                    if (typePDFVDetail.isChecked()) {
                        type = EXPORT_PDF_VDETAIL;
                    }
                    String name = fileName.getText().toString();
                    if (name.trim().length() == 0) {
                        Message.obtain(handler, BaseActivity.SHOW_MSG, activity.getString(R.string.file_name_can_not_be_null)).sendToTarget();
                        return;
                    }

                    String titleStr1 = mTitle1.getText().toString();
                    if (name.trim().length() == 0) {
                        Message.obtain(handler, BaseActivity.SHOW_MSG, activity.getString(R.string.title1_can_not_be_null)).sendToTarget();
                        return;
                    }

                    String titleStr2 = mTitle2.getText().toString();
                    String titleStr3 = mTitle3.getText().toString();
                    onExportListener.onExport(name.trim(), type, titleStr1, titleStr2, titleStr3);
                }
                break;
        }
    }

    public interface OnExportListener {
        void onExport(String fileName, int exportType, String title1, String title2, String title3);
    }

    public class DataExportHandler extends Handler {
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
