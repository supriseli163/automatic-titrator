package com.jh.automatic_titrator.ui.data;

import android.app.Activity;
import android.content.Context;
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

/**
 * Created by apple on 2016/10/28.
 */
public class DataPrint extends PopupWindow implements View.OnClickListener {

    public static final int PRINT_HORIZONTAL_SIMPLE = 1;
    public static final int PRINT_VERTICAL_SIMPLE = 2;
    public static final int PRINT_VERTICAL_DETAIL = 3;

    private OnPrintListener onPrintListener;

    private RadioButton horizontalSimple;
    private RadioButton verticalSimple;
    private RadioButton verticalDetail;

    private EditText title1Et;
    private EditText title2Et;
    private EditText title3Et;

    private View print;

    private Toast mToast;

    private DataPrintHandler handler;

    private Context mContext;

    public DataPrint(Activity activity, int width, int height, boolean focusable) {
        this(activity, LayoutInflater.from(activity).inflate(
                R.layout.data_print_popup, null), width, height, focusable);
    }

    public DataPrint(Activity activity, View contentView, int width, int height, boolean focusable) {
        super(contentView, width, height, focusable);
        mContext = activity;
        mToast = ToastUtil.createToast(activity);
        handler = new DataPrintHandler();
        horizontalSimple = (RadioButton) contentView.findViewById(R.id.data_print_export_type_horizontal_simple);
        verticalSimple = (RadioButton) contentView.findViewById(R.id.data_print_export_type_vertical_simple);
        verticalDetail = (RadioButton) contentView.findViewById(R.id.data_print_export_type_vertical_detail);
        title1Et = (EditText) contentView.findViewById(R.id.data_print_title_name1);
        title2Et = (EditText) contentView.findViewById(R.id.data_print_title_name2);
        title3Et = (EditText) contentView.findViewById(R.id.data_print_title_name3);

        print = contentView.findViewById(R.id.data_print_save_btn);
        print.setOnClickListener(this);
    }

    public void setOnPrintListener(OnPrintListener onPrintListener) {
        this.onPrintListener = onPrintListener;
    }

    @Override
    public void onClick(View v) {
        if (onPrintListener != null) {
            int printType = PRINT_HORIZONTAL_SIMPLE;
            if (horizontalSimple.isChecked()) {
                printType = PRINT_HORIZONTAL_SIMPLE;
            }
            if (verticalSimple.isChecked()) {
                printType = PRINT_VERTICAL_SIMPLE;
            }
            if (verticalDetail.isChecked()) {
                printType = PRINT_VERTICAL_DETAIL;
            }
            String title1 = title1Et.getText().toString();
            String title2 = title2Et.getText().toString();
            String title3 = title3Et.getText().toString();
            if (title1.trim().length() == 0) {
                Message.obtain(handler, BaseActivity.SHOW_MSG, mContext.getString(R.string.title1_can_not_be_null)).sendToTarget();
                return;
            } else {
                onPrintListener.onPrint(printType, title1.trim(), title2.trim(), title3.trim());
            }
        }
    }

    public interface OnPrintListener {
        void onPrint(int printType, String title1, String title2, String title3);
    }

    private class DataPrintHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BaseActivity.SHOW_MSG:
                    ToastUtil.toastShow(mToast, String.valueOf(msg.obj));
                    break;
            }
        }
    }
}
