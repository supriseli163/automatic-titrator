package com.jh.automatic_titrator.ui.window;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.common.db.MethodHelper;
import com.jh.automatic_titrator.common.utils.StringUtils;
import com.jh.automatic_titrator.common.utils.ToastUtil;
import com.jh.automatic_titrator.entity.common.titrator.TitratorMethod;
import com.jh.automatic_titrator.service.DBService;
import com.jh.automatic_titrator.service.ExecutorService;
import com.jh.automatic_titrator.service.TitorMethodService;
import com.jh.automatic_titrator.ui.BaseActivity;

public class MethodAddWindow implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, AdapterView.OnItemSelectedListener  {

    private Toast mToast;
    private MethodAddWindowsHandler methodAddWindowsHandler;
    private MethodHelper methodHelper;
    private TitorMethodService titorMethodService;

    private View mView;
    private Context mContext;
    private EditText methodName_tv;
    private EditText titratorType_tv;
    private EditText buretteVolume_tv;
    private EditText workingElectrode_tv;
    private EditText referenceElectrode_tv;
    private EditText sampleMeasurementUnit_tv;
    private EditText titrationDisplayUnit_tv;
    private EditText replenishmentSpeed_tv;
    private EditText stiringSpeed_tv;
    private EditText electroedEquilibrationTime_tv;
    private EditText electroedEquilibriumPotential_tv;
    private EditText preStiringTime_tv;
    private EditText perAddVolume_tv;
    private EditText endVolume_tv;
    private EditText titrationSpeed_tv;
    private EditText slowTitrationVolume_tv;
    private EditText fastTitrationVolume_tv;

    private OnSaveListener onSaveListener;
    MethodAddWindowsHandler handler;
    private AlertDialog dialog;

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    public MethodAddWindow() {

    }

    private void doSave() {
        if(onSaveListener != null) {
            ExecutorService.getInstance().execute(
                    new Runnable() {
                        @Override
                        public void run() {
                            try {
                                checkFields();
                                TitratorMethod titratorMethod = buildTestMethod();
                                onSaveListener.onSave(titratorMethod);
                            } catch (Exception ex) {
                                Message.obtain(handler, 0, ex.getMessage()).sendToTarget();
                            }
                        }
                    }
            );
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titrator_method_alter_save_button:
                doSave();
                break;
        }
    }

    public MethodAddWindow(Context mContext) {
        this.mContext = mContext;
        mToast = ToastUtil.createToast(mContext);
        methodHelper = DBService.getMethodHelper(mContext);
        mView = LayoutInflater.from(mContext).inflate(R.layout.titrator_method_fragemnt_detail, null);
        methodName_tv = (EditText)mView.findViewById(R.id.titrator_method_input_name);
        titratorType_tv = (EditText)mView.findViewById(R.id.titrator_method_input_type);
        buretteVolume_tv = (EditText)mView.findViewById(R.id.titrator_method_burette_volume);
        workingElectrode_tv = (EditText)mView.findViewById(R.id.titrator_method_working_electrode);
        referenceElectrode_tv = (EditText)mView.findViewById(R.id.titrator_method_reference_electrode);
        sampleMeasurementUnit_tv = (EditText)mView.findViewById(R.id.titrator_method_sample_measurement_unit);
        titrationDisplayUnit_tv = (EditText)mView.findViewById(R.id.titrator_method_titration_display_unit);
        replenishmentSpeed_tv = (EditText)mView.findViewById(R.id.titrator_method_titration_replenishment_speed);
    }

    public void show() {
        dialog.show();
    }

    private void checkFields() throws Exception {
        String canNotBeNul = mContext.getString(R.string.can_not_be_null);
        if(StringUtils.isEmpty(methodName_tv.getText().toString().trim())) {
            String method_name = mContext.getString(R.string.method);
            throw new IllegalArgumentException();
        }
    }

    public void dismiss() {
        dialog.dismiss();
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mView.getWindowToken(), 0);
    }

    public void setOnSaveListener(OnSaveListener onSaveListener) {
        this.onSaveListener = onSaveListener;
    }

    public TitratorMethod buildTestMethod() throws Exception {
        TitratorMethod titratorMethod = new TitratorMethod();
        titratorMethod.setMethodName(methodName_tv.getText().toString());
        return titratorMethod;
    }

    public interface OnSaveListener {
        void onSave(TitratorMethod titratorMethod);
    }

    public class MethodAddWindowsHandler extends Handler {
        private ArrayAdapter arrayAdapter;

        public MethodAddWindowsHandler(ArrayAdapter arrayAdapter) {
            this.arrayAdapter = arrayAdapter;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BaseActivity.NEED_REFRESH:
                    arrayAdapter.notifyDataSetChanged();
                    break;
                default:
                    if (msg.obj != null) {
                        ToastUtil.toastShow(mToast, String.valueOf(msg.obj));
                    }
                    break;
            }
        }
    }
}
