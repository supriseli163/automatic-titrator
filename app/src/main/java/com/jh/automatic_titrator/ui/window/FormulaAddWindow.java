package com.jh.automatic_titrator.ui.window;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
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
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by apple on 2017/2/12.
 */

public class FormulaAddWindow implements View.OnClickListener {

    private Context mContext;

    private View mView;

    private EditText mNameEt;

    private EditText mSimpleNameEt;

    private EditText mUnitEt;

    private Spinner mDecimalSp;

    private Spinner mShowPercentSp;

    private EditText mDescEt;

    private Dialog mDialog;

    private View mAddBtn;

    private View mConversionAddBtn;

    private ListView mConversionLv;

    private SettingConversionAdapter mSettingConversionAdapter;

    private List<Conversion> mConversions;

    private FormulaAddHandler mFormulaAddHandler;

    private FormulaAddListener mFormulaAddListener;

    private Toast mToast;

    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public FormulaAddWindow(Context mContext) {
        this.mContext = mContext;
        mToast = ToastUtil.createToast(mContext);
        mFormulaAddHandler = new FormulaAddWindow.FormulaAddHandler();
        mView = LayoutInflater.from(mContext).inflate(R.layout.setting_formula_add, null);
        mNameEt = (EditText) mView.findViewById(R.id.formula_add_name);
        mSimpleNameEt = (EditText) mView.findViewById(R.id.formula_add_simple_name);
        mUnitEt = (EditText) mView.findViewById(R.id.formula_add_unit);

        mDecimalSp = (Spinner) mView.findViewById(R.id.formula_add_decimal);
        String[] concentrationDecimals = mContext.getResources().getStringArray(R.array.number_decimal);
        ArrayAdapter<String> concentrationDecimalAdapter = new ArrayAdapter<String>(mContext, R.layout.spinner_checked1, concentrationDecimals);
        concentrationDecimalAdapter.setDropDownViewResource(R.layout.spinner_item1);
        mDecimalSp.setAdapter(concentrationDecimalAdapter);


        mShowPercentSp = (Spinner) mView.findViewById(R.id.formula_add_show_percent);
        String[] concentrationShowPercents = mContext.getResources().getStringArray(R.array.bool);
        ArrayAdapter<String> concentrationShowPercentAdapter = new ArrayAdapter<String>(mContext, R.layout.spinner_checked1, concentrationShowPercents);
        concentrationShowPercentAdapter.setDropDownViewResource(R.layout.spinner_item1);
        mShowPercentSp.setAdapter(concentrationShowPercentAdapter);


        mDescEt = (EditText) mView.findViewById(R.id.formula_add_desc);

        mConversionAddBtn = mView.findViewById(R.id.formula_add_conversion_add);
        mConversionAddBtn.setOnClickListener(this);
        mConversionLv = (ListView) mView.findViewById(R.id.formula_add_conversion_list);

        mConversions = new ArrayList<>();
        mSettingConversionAdapter = new SettingConversionAdapter(mConversions, mContext, true, new SettingConversionAdapter.DeleteListener() {
            @Override
            public void delete(int position) {
                lock.writeLock().lock();
                try {
                    if (position < mConversions.size()) {
                        mConversions.remove(position);
                        modify2 = System.currentTimeMillis();
                    }
                } finally {
                    lock.writeLock().unlock();
                }
            }
        });
        mConversionLv.setAdapter(mSettingConversionAdapter);

        mAddBtn = mView.findViewById(R.id.formula_add_save_btn);
        mAddBtn.setOnClickListener(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mContext.getString(R.string.add));

        builder.setView(mView);
        mDialog = builder.create();
        thread.start();
    }

    public void setFormulaAddListener(FormulaAddListener FormulaAddListener) {
        this.mFormulaAddListener = FormulaAddListener;
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
                    if (modify1 != modify2) {
                        Message.obtain(mFormulaAddHandler, BaseActivity.NEED_REFRESH).sendToTarget();
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

    public void show() {
        lock.writeLock().lock();
        try {
            mConversions.clear();
            mConversions.add(Conversion.defaultConversion());
        } finally {
            lock.writeLock().unlock();
        }
        mSettingConversionAdapter.notifyDataSetChanged();
        mConversionAddBtn.setVisibility(View.VISIBLE);
        mDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.formula_add_conversion_add:
                lock.writeLock().lock();
                try {
                    mConversions.add(Conversion.defaultConversion());
                    modify2 = System.currentTimeMillis();
                } finally {
                    lock.writeLock().unlock();
                }
                break;
            case R.id.formula_add_save_btn:
                addFormula();
                break;
        }
    }

    private void addFormula() {
        if (mFormulaAddListener != null) {
            ExecutorService.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Formula formula = createFormula();
                        mFormulaAddListener.doAdd(formula);
                    } catch (Exception e) {
                        Message.obtain(mFormulaAddHandler, BaseActivity.SHOW_MSG, e.getMessage()).sendToTarget();
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
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mView.getWindowToken(), 0);
    }

    private class FormulaAddHandler extends Handler {
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

    public interface FormulaAddListener {
        void doAdd(Formula formula);
    }
}
