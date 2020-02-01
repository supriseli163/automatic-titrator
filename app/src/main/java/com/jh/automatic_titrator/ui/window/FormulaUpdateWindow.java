package com.jh.automatic_titrator.ui.window;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.common.Cache;
import com.jh.automatic_titrator.common.formula.FormulaUtil;
import com.jh.automatic_titrator.common.utils.TimeTool;
import com.jh.automatic_titrator.common.utils.ToastUtil;
import com.jh.automatic_titrator.entity.common.Conversion;
import com.jh.automatic_titrator.entity.common.Formula;
import com.jh.automatic_titrator.service.ExecutorService;
import com.jh.automatic_titrator.ui.BaseActivity;
import com.jh.automatic_titrator.ui.setting.SettingConversionAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 2017/2/12.
 */

public class FormulaUpdateWindow implements View.OnClickListener {

    private Context mContext;

    private View mView;

    private EditText mNameEt;

    private EditText mSimpleNameEt;

    private EditText mUnitEt;

    private Spinner mDecimalSp;

    private Spinner mShowPercentSp;

    private EditText mDescEt;

    private Dialog mDialog;

    private View mSaveBtn;

    private TextView mSaveTv;

    private View mConversionAddBtn;

    private ListView mConversionLv;

    private SettingConversionAdapter mSettingConversionAdapter;

    private List<Conversion> mConversions;

    private FormulaUpdateHandler mFormulaUpdateHandler;

    private FormulaUpdateListener mFormulaUpdateListener;

    private boolean mEditable = false;

    private Toast mToast;

    public FormulaUpdateWindow(Context mContext) {
        this.mContext = mContext;
        mToast = ToastUtil.createToast(mContext);
        mFormulaUpdateHandler = new FormulaUpdateHandler();
        mView = LayoutInflater.from(mContext).inflate(R.layout.setting_formula_update, null);
        mNameEt = (EditText) mView.findViewById(R.id.formula_update_name);
        mSimpleNameEt = (EditText) mView.findViewById(R.id.formula_update_simple_name);
        mUnitEt = (EditText) mView.findViewById(R.id.formula_update_unit);

        mDecimalSp = (Spinner) mView.findViewById(R.id.formula_update_decimal);
        String[] concentrationDecimals = mContext.getResources().getStringArray(R.array.number_decimal);
        ArrayAdapter<String> concentrationDecimalAdapter = new ArrayAdapter<String>(mContext, R.layout.spinner_checked1, concentrationDecimals);
        concentrationDecimalAdapter.setDropDownViewResource(R.layout.spinner_item1);
        mDecimalSp.setAdapter(concentrationDecimalAdapter);


        mShowPercentSp = (Spinner) mView.findViewById(R.id.formula_update_show_percent);
        String[] concentrationShowPercents = mContext.getResources().getStringArray(R.array.bool);
        ArrayAdapter<String> concentrationShowPercentAdapter = new ArrayAdapter<String>(mContext, R.layout.spinner_checked1, concentrationShowPercents);
        concentrationShowPercentAdapter.setDropDownViewResource(R.layout.spinner_item1);
        mShowPercentSp.setAdapter(concentrationShowPercentAdapter);


        mDescEt = (EditText) mView.findViewById(R.id.formula_update_desc);

        mConversionAddBtn = mView.findViewById(R.id.formula_update_conversion_add);
        mConversionAddBtn.setOnClickListener(this);
        mConversionLv = (ListView) mView.findViewById(R.id.formula_update_conversion_list);

        mConversions = new ArrayList<>();
        mSettingConversionAdapter = new SettingConversionAdapter(mConversions, mContext, true, new SettingConversionAdapter.DeleteListener() {
            @Override
            public void delete(int position) {
                mConversions.remove(position);
                modify2 = System.currentTimeMillis();
            }
        });
        mConversionLv.setAdapter(mSettingConversionAdapter);

        mSaveBtn = mView.findViewById(R.id.formula_update_save_btn);
        mSaveBtn.setOnClickListener(this);
        mSaveTv = (TextView) mView.findViewById(R.id.formula_update_save_txt);

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mContext.getString(R.string.add));

        builder.setView(mView);
        mDialog = builder.create();
        thread.start();
    }

    public void setFormulaUpdateListener(FormulaUpdateListener formulaUpdateListener) {
        this.mFormulaUpdateListener = formulaUpdateListener;
    }

    private volatile long modify1 = 0;

    private volatile long modify2 = 0;

    private volatile boolean showing = true;

    private Thread thread = new Thread() {
        @Override
        public void run() {
            while (showing) {
                try {
                    Thread.sleep(100);
                    if (modify1 != modify2 && mView.isEnabled()) {
                        Message.obtain(mFormulaUpdateHandler, BaseActivity.NEED_REFRESH).sendToTarget();
                        modify1 = modify2;
                    }
                } catch (Exception e) {
                    //DONOTHING
                }
            }
        }
    };

    public void destory() {
        showing = false;
        mDialog.dismiss();
    }

    public void show(Formula formula) {
        mEditable = false;
        mConversions.clear();
        mConversions.addAll(formula.getConversions());
        modify2 = System.currentTimeMillis();
        disAbleAll();
        mConversionAddBtn.setVisibility(View.GONE);
        mSettingConversionAdapter.notifyDataSetChanged();
        mNameEt.setText(formula.getFormulaName());
        mSimpleNameEt.setText(formula.getSimpleName());
        mUnitEt.setText(formula.getUnit());
        mShowPercentSp.setSelection(formula.isShowPercent() ? 0 : 1);
        mDecimalSp.setSelection(formula.getDecimal() - 1);
        mDescEt.setText(formula.getDesc());
        mSaveTv.setText(mContext.getString(R.string.alter));
        if (!Cache.containsAuth("formula") || Cache.isTesting() || formula.getFormulaName().equals("国际糖度")) {
            mSaveBtn.setVisibility(View.INVISIBLE);
        } else {
            mSaveBtn.setVisibility(View.VISIBLE);
        }
        mDialog.show();
    }

    private void disAbleAll() {
        mView.setEnabled(false);
        mSettingConversionAdapter.setEditable(false);
        mNameEt.setEnabled(false);
        mSimpleNameEt.setEnabled(false);
        mDecimalSp.setEnabled(false);
        mUnitEt.setEnabled(false);
        mDescEt.setEnabled(false);
        mShowPercentSp.setEnabled(false);
    }

    private void enableAll() {
        mEditable = true;
        mView.setEnabled(true);
        mSettingConversionAdapter.setEditable(true);
        mSettingConversionAdapter.notifyDataSetChanged();
        mNameEt.setEnabled(true);
        mSimpleNameEt.setEnabled(true);
        mDecimalSp.setEnabled(true);
        mUnitEt.setEnabled(true);
        mDescEt.setEnabled(true);
        mShowPercentSp.setEnabled(true);
        if (mConversions.size() < 5) {
            mConversionAddBtn.setVisibility(View.VISIBLE);
        } else {
            mConversionAddBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.formula_update_conversion_add:
                mConversions.add(Conversion.defaultConversion());
                modify2 = System.currentTimeMillis();
                break;
            case R.id.formula_update_save_btn:
                if (mEditable) {
                    updateFormula();
                } else {
                    mSaveTv.setText(mContext.getString(R.string.save));
                    enableAll();
                }
                break;
        }
    }

    private void updateFormula() {
        if (mFormulaUpdateListener != null) {
            ExecutorService.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Formula formula = createFormula();
                        mFormulaUpdateListener.doUpdate(formula);
                    } catch (Exception e) {
                        Message.obtain(mFormulaUpdateHandler, BaseActivity.SHOW_MSG, e.getMessage()).sendToTarget();
                        e.printStackTrace();
                    }
                }
            });

        }
    }

    private Formula createFormula() throws Exception {
        Formula formula = new Formula();

        checkInput();
        formula.setCreateDate(TimeTool.currentDate());
        formula.setCreator(Cache.getUser().getUserName());
        formula.setDecimal(mDecimalSp.getSelectedItemPosition() + 1);
        formula.setDesc(mDescEt.getText().toString());
        formula.setUnit(mUnitEt.getText().toString());
        formula.setFormulaName(mNameEt.getText().toString());
        formula.setSimpleName(mSimpleNameEt.getText().toString());
        formula.setShowPercent(mShowPercentSp.getSelectedItemPosition() == 0);
        formula.setConversions(mConversions);

        int formulaLegalState = FormulaUtil.checkFormulaLegal(formula);
        switch (formulaLegalState) {
            case FormulaUtil.CONVERSION_NULL:
                String range = mContext.getString(R.string.conversion_range);
                String canNotBeNull = mContext.getString(R.string.can_not_be_null);
                throw new IllegalArgumentException(range + " " + canNotBeNull);
            case FormulaUtil.RANGE_ILLEGAL:
                range = mContext.getString(R.string.conversion_range);
                String rangeIllegal = mContext.getString(R.string.range_illegal);
                throw new IllegalArgumentException(range + " " + rangeIllegal);
        }
        return formula;
    }

    private void checkInput() {
        String canNotBeNull = mContext.getString(R.string.can_not_be_null);
        if (mNameEt.getText().length() == 0) {
            String formulaName = mContext.getString(R.string.formula_name);
            throw new IllegalArgumentException(formulaName + " " + canNotBeNull);
        }
        if (mConversions.size() > 0) {
            for (Conversion conversion : mConversions) {
                String range = mContext.getString(R.string.conversion_range);
                if (conversion.getStart() == null || conversion.getEnd() == null) {
                    throw new IllegalArgumentException(range + " " + canNotBeNull);
                }
                if (conversion.getStart() > conversion.getEnd()) {
                    String illegal = mContext.getString(R.string.field_illegal);
                    throw new IllegalArgumentException(range + " " + illegal);
                }
            }
        }
    }

    public void hide() {
        mDialog.dismiss();
    }

    private class FormulaUpdateHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BaseActivity.NEED_REFRESH:
                    mSettingConversionAdapter.notifyDataSetChanged();
                    if (mConversions.size() >= 5) {
                        mConversionAddBtn.setVisibility(View.GONE);
                    } else {
                        mConversionAddBtn.setVisibility(View.VISIBLE);
                    }
                    break;
                case BaseActivity.SHOW_MSG:
                    if (msg.obj != null) {
                        ToastUtil.toastShow(mToast, String.valueOf(msg.obj));
                    }
            }
        }
    }

    public interface FormulaUpdateListener {
        void doUpdate(Formula formula);
    }
}
