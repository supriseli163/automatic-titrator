package com.jh.automatic_titrator.ui.setting;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.common.Cache;
import com.jh.automatic_titrator.common.EBusEvent;
import com.jh.automatic_titrator.common.db.UserHelper;
import com.jh.automatic_titrator.common.db.WaveLengthHelper;
import com.jh.automatic_titrator.common.trunk.TrunkConst;
import com.jh.automatic_titrator.common.trunk.TrunkData;
import com.jh.automatic_titrator.common.trunk.TrunkListener;
import com.jh.automatic_titrator.common.trunk.TrunkUtil;
import com.jh.automatic_titrator.common.utils.ToastUtil;
import com.jh.automatic_titrator.entity.common.StandardData;
import com.jh.automatic_titrator.service.DBService;
import com.jh.automatic_titrator.service.ExecutorService;
import com.jh.automatic_titrator.service.TestMethodService;
import com.jh.automatic_titrator.service.WaveLengthService;
import com.jh.automatic_titrator.ui.BaseActivity;
import com.jh.automatic_titrator.ui.HomePageActivity;
import com.jh.automatic_titrator.ui.listener.KeyboardDismiss;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by apple on 16/9/24.
 */
public class SettingInitialFragment extends Fragment implements View.OnClickListener {

    public static final int TEMPERATURE_ADD = 0xF0000001;

    public static final int RECOVERY_ENSURE_DLG = 0xF0000002;

    public static final int COVER_ENSURE_DLG = 0xF0000003;

    public static final int REMOVE_LOGO = 0xF0000004;

    private EditText temperatureEt;

    private ImageView temperatureAdd;

    private ImageView temperatureDetele;

    private EditText wavelength1;
    private EditText wavelength2;
    private EditText wavelength3;
    private EditText wavelength4;
    private EditText wavelength5;
    private EditText wavelength6;
    private EditText wavelength7;
    private EditText wavelength8;

    private EditText autoClean3;

    private EditText autoClean4;

    private View factorySave_btn;

    private View factoryRecover_btn;

    private WaveLengthHelper waveLengthHelper;

    private CheckBox checkBox;

    private View checkBoxLayout;

    private View saveBtn;

    private SettingInitialHandler settingInitialHandler;

    private Map<Integer, String> wavelengthMap;

    private BaseActivity baseActivity;

    private TrunkUtil trunkUtil;

    private Dialog dialog;

    private View view;

    private float temperatureAddValue;

    private Toast mToast;

    private EditText et_changeV;

    private Button btn_changeV;

//    private Spinner changeV_spinner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.setting_initial_fragment, container, false);
        mToast = ToastUtil.createToast(getActivity());
        waveLengthHelper = DBService.getWaveLengthHelper(getActivity());

        Activity activity = getActivity();
        baseActivity = (BaseActivity) activity;

        temperatureEt = (EditText) view.findViewById(R.id.setting_initial_temperature);
        temperatureEt.setText(baseActivity.getTemperatureAdd());
        temperatureAddValue = Float.parseFloat(baseActivity.getTemperatureAdd());
        temperatureEt.setEnabled(false);

        temperatureAdd = (ImageView) view.findViewById(R.id.setting_initial_temperatureadd);
        temperatureAdd.setOnClickListener(this);
        temperatureDetele = (ImageView) view.findViewById(R.id.setting_initial_temperaturedelete);
        temperatureDetele.setOnClickListener(this);
        wavelength1 = (EditText) view.findViewById(R.id.setting_initail_wavelength1);
        wavelength2 = (EditText) view.findViewById(R.id.setting_initail_wavelength2);
        wavelength3 = (EditText) view.findViewById(R.id.setting_initail_wavelength3);
        wavelength4 = (EditText) view.findViewById(R.id.setting_initail_wavelength4);
        wavelength5 = (EditText) view.findViewById(R.id.setting_initail_wavelength5);
        wavelength6 = (EditText) view.findViewById(R.id.setting_initail_wavelength6);
        wavelength7 = (EditText) view.findViewById(R.id.setting_initail_wavelength7);
        wavelength8 = (EditText) view.findViewById(R.id.setting_initail_wavelength8);

        autoClean3 = (EditText) view.findViewById(R.id.setting_initail_autoclean_3);
        autoClean4 = (EditText) view.findViewById(R.id.setting_initail_autoclean_4);
        autoClean3.setText(Cache.getAutoClean3() + "");
        autoClean4.setText(Cache.getAutoClean4() + "");

        checkBoxLayout = view.findViewById(R.id.setting_initail_movelogo_layout);
        checkBox = (CheckBox) view.findViewById(R.id.setting_initail_movelogo);
        checkBox.setChecked(!baseActivity.showLogo());
        if (checkBox.isChecked()) {
            checkBoxLayout.setBackground(getResources().getDrawable(R.drawable.icon_on));
        } else {
            checkBoxLayout.setBackground(getResources().getDrawable(R.drawable.icon_off));
        }
        checkBoxLayout.setOnClickListener(this);
//        checkBox.setOnClickListener(this);
        saveBtn = view.findViewById(R.id.setting_initail_save);

        factorySave_btn = view.findViewById(R.id.setting_initail_factory_save_btn);
        factorySave_btn.setOnClickListener(this);
        factoryRecover_btn = view.findViewById(R.id.setting_initail_factory_recover_btn);
        factoryRecover_btn.setOnClickListener(this);

        et_changeV = (EditText) view.findViewById(R.id.et_changeV);
        HomePageActivity homePageActivity = (HomePageActivity) getActivity();
        et_changeV.setText(homePageActivity.readconf("version").isEmpty()?"":homePageActivity.readconf("version"));
        btn_changeV = (Button) view.findViewById(R.id.btn_changeV);
        btn_changeV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = et_changeV.getText().toString();
                HomePageActivity homePageActivity = (HomePageActivity) getActivity();
                homePageActivity.writeconf("version", str);
                EventBus.getDefault().post(new EBusEvent(str));
            }
        });

        settingInitialHandler = new SettingInitialHandler();

        saveBtn.setOnClickListener(this);
        trunkUtil = TrunkUtil.getInstance();
        loadInitDate();
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            loadInitDate();
        }
    }

    public void loadInitDate() {
        wavelengthMap = waveLengthHelper.getWaveLengthMap();
        Activity activity = getActivity();
        if (activity instanceof BaseActivity) {
            BaseActivity baseActivity = (BaseActivity) activity;
//            baseActivity.setShowLogo(!baseActivity.showLogo());
            checkBox.setChecked(!baseActivity.showLogo());
            autoClean3.setText(String.format("%.3f", baseActivity.getAutoClean3()));
//            baseActivity.setAutoClean3(baseActivity.getInitAutoClean3());
            autoClean4.setText(String.format("%.4f", baseActivity.getAutoClean4()));
//            baseActivity.setAutoClean4(baseActivity.getInitAutoClean4());
        }
        if (wavelengthMap.containsKey(0)) {
            wavelength1.setText(String.valueOf(wavelengthMap.get(0)));
        }
        if (wavelengthMap.containsKey(1)) {
            wavelength2.setText(String.valueOf(wavelengthMap.get(1)));
        }
        if (wavelengthMap.containsKey(2)) {
            wavelength3.setText(String.valueOf(wavelengthMap.get(2)));
        }
        if (wavelengthMap.containsKey(3)) {
            wavelength4.setText(String.valueOf(wavelengthMap.get(3)));
        }
        if (wavelengthMap.containsKey(4)) {
            wavelength5.setText(String.valueOf(wavelengthMap.get(4)));
        }
        if (wavelengthMap.containsKey(5)) {
            wavelength6.setText(String.valueOf(wavelengthMap.get(5)));
        }
        if (wavelengthMap.containsKey(6)) {
            wavelength7.setText(String.valueOf(wavelengthMap.get(6)));
        }
        if (wavelengthMap.containsKey(7)) {
            wavelength8.setText(String.valueOf(wavelengthMap.get(7)));
        }
    }

    @Override
    public void onClick(final View v) {
        ExecutorService.getInstance().execute(
                new Runnable() {
                    @Override
                    public void run() {
                        switch (v.getId()) {
                            case R.id.setting_initail_save:
                                saveWaveLength();
                                Message.obtain(settingInitialHandler, BaseActivity.SAVE_SUCCESS).sendToTarget();
                                break;
                            case R.id.setting_initial_temperatureadd:
                                temperateAdd();
                                break;
                            case R.id.setting_initial_temperaturedelete:
                                temperateDelete();
                                break;
                            case R.id.setting_initail_factory_save_btn:
                                Message.obtain(settingInitialHandler, COVER_ENSURE_DLG).sendToTarget();
                                break;
                            case R.id.setting_initail_factory_recover_btn:
                                Message.obtain(settingInitialHandler, RECOVERY_ENSURE_DLG).sendToTarget();
                                break;
                            case R.id.setting_initail_movelogo_layout:
                                Message.obtain(settingInitialHandler, REMOVE_LOGO).sendToTarget();
                                break;
                        }
                    }
                }
        );
    }

    private void temperateAdd() {
        temperatureAddValue = temperatureAddValue + 0.1f;
        Message.obtain(settingInitialHandler, TEMPERATURE_ADD).sendToTarget();
    }

    private void temperateDelete() {
        temperatureAddValue = temperatureAddValue - 0.1f;
        Message.obtain(settingInitialHandler, TEMPERATURE_ADD).sendToTarget();
    }

    private String[] saveWaveLength() {
        String[] waveLengths = new String[8];
        String wavelengthStr1 = wavelength1.getText().toString();
        if (wavelengthStr1 != null && wavelengthStr1.length() > 0) {
            waveLengths[0] = wavelengthStr1;
        }
        String wavelengthStr2 = wavelength2.getText().toString();
        if (wavelengthStr2 != null && wavelengthStr2.length() > 0) {
            waveLengths[1] = wavelengthStr2;
        }
        String wavelengthStr3 = wavelength3.getText().toString();
        if (wavelengthStr3 != null && wavelengthStr3.length() > 0) {
            waveLengths[2] = wavelengthStr3;
        }
        String wavelengthStr4 = wavelength4.getText().toString();
        if (wavelengthStr4 != null && wavelengthStr4.length() > 0) {
            waveLengths[3] = wavelengthStr4;
        }
        String wavelengthStr5 = wavelength5.getText().toString();
        if (wavelengthStr5 != null && wavelengthStr5.length() > 0) {
            waveLengths[4] = wavelengthStr5;
        }
        String wavelengthStr6 = wavelength6.getText().toString();
        if (wavelengthStr6 != null && wavelengthStr6.length() > 0) {
            waveLengths[5] = wavelengthStr6;
        }
        String wavelengthStr7 = wavelength7.getText().toString();
        if (wavelengthStr7 != null && wavelengthStr7.length() > 0) {
            waveLengths[6] = wavelengthStr7;
        }
        String wavelengthStr8 = wavelength8.getText().toString();
        if (wavelengthStr8 != null && wavelengthStr8.length() > 0) {
            waveLengths[7] = wavelengthStr8;
        }
        boolean showLogo = !checkBox.isChecked();
        Activity activity = getActivity();
        if (activity instanceof BaseActivity) {
            BaseActivity baseActivity = (BaseActivity) activity;
            baseActivity.setShowLogo(showLogo);
            String autoClean3Str = autoClean3.getText().toString();
            if (autoClean3Str != null && autoClean3Str.length() != 0) {
                baseActivity.setAutoClean3(Float.parseFloat(autoClean3Str));
            }
            Cache.setAutoClean3(Float.parseFloat(autoClean3Str));
            String autoClean4Str = autoClean4.getText().toString();
            if (autoClean4Str != null && autoClean4Str.length() != 0) {
                baseActivity.setAutoClean4(Float.parseFloat(autoClean4Str));
            }
            Cache.setAutoClean4(Float.parseFloat(autoClean4Str));
            baseActivity.temperatureAdd(String.format("%.1f", temperatureAddValue));
        }
        waveLengthHelper.refreshWaveLength(waveLengths);
        Cache.setWaveLengths(waveLengthHelper.getWaveLengths());
        saveTemperatureAdd();
        WaveLengthService.getInstance().notifyChanged();
        return waveLengths;
    }

    public class SettingInitialHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BaseActivity.SAVE_SUCCESS:
                    ToastUtil.toastShow(mToast, getString(R.string.save_success));
                    break;
                case BaseActivity.HOLD_WINDOW_YES:
                    showDialog(msg.obj);
                    break;
                case BaseActivity.HOLD_WINDOW_NO:
                    hideDialog(msg.obj);
                    break;
                case BaseActivity.HOLD_BUTTON_YES:
                    view.setEnabled(false);
                    break;
                case BaseActivity.HOLD_BUTTON_NO:
                    view.setEnabled(true);
                    break;
                case TEMPERATURE_ADD:
                    temperatureEt.setText(String.format("%.1f", temperatureAddValue));
                    break;
                case RECOVERY_ENSURE_DLG:
                    ensureRecovery();
                    break;
                case COVER_ENSURE_DLG:
                    ensureCover();
                    break;
                case BaseActivity.NEED_REFRESH:
                    loadInitDate();
                    break;
                case REMOVE_LOGO:
                    passwordPass(getView());
                    break;
            }
        }
    }

    private void passwordPass(final View v) {
        final View passwordInputView = LayoutInflater.from(getActivity()).inflate(R.layout.input_password, null);

        final EditText password = (EditText) passwordInputView.findViewById(R.id.input_password_et);

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle(getString(R.string.prompt));
        builder.setMessage(getString(R.string.password));
        builder.setView(passwordInputView);
        builder.setPositiveButton(this.getActivity().getString(R.string.submit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (password.getText().toString().equals(UserHelper.SUPER_PASSWORD)) {
                    checkBox.setChecked(!checkBox.isChecked());
                } else {
                    checkBox.setChecked(checkBox.isChecked());
                    ToastUtil.toastShow(mToast, getString(R.string.password_error));
                }
                if (checkBox.isChecked()) {
                    checkBoxLayout.setBackground(getResources().getDrawable(R.drawable.icon_on));
                } else {
                    checkBoxLayout.setBackground(getResources().getDrawable(R.drawable.icon_off));
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            builder.setOnDismissListener(new KeyboardDismiss(getActivity()));
        }
        builder.create().show();
    }

    private void showDialog(Object obj) {
        if (dialog == null) {
            dialog = new ProgressDialog(getActivity());
        }
        dialog.setTitle(String.valueOf(obj));
        dialog.setCancelable(false);
        dialog.setDismissMessage(Message.obtain(settingInitialHandler));
        dialog.show();
    }

    private void hideDialog(Object obj) {
        dialog.dismiss();
        ToastUtil.toastShow(mToast, String.valueOf(obj));
    }

    private void ensureRecovery() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle(getString(R.string.prompt));
        builder.setMessage(getString(R.string.ensure_recovery));
        builder.setPositiveButton(this.getActivity().getString(R.string.submit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                doRecovery();
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

    public void doRecovery() {
        ExecutorService.getInstance().execute(
                new Runnable() {
                    @Override
                    public void run() {
                        Message.obtain(settingInitialHandler, BaseActivity.HOLD_WINDOW_YES, getString(R.string.restore_factory_settings)).sendToTarget();
                        DBService.clearAllData(getActivity());
                        Log.d("recover db", "success");
                        TestMethodService.getInstance().onChange(R.id.setting_initail_save);
                        Log.d("recover method", "success");
                        Cache.setWaveLengths(waveLengthHelper.getWaveLengths());
                        waveLengthHelper.recoverInitWaveLength();
                        Log.d("recover wavelength", "success");
                        wavelengthMap = waveLengthHelper.getWaveLengthMap();

                        WaveLengthService.getInstance().notifyChanged();
                        recover();
                        Message.obtain(settingInitialHandler, BaseActivity.HOLD_WINDOW_NO, getString(R.string.recover_success)).sendToTarget();
                        Message.obtain(settingInitialHandler, BaseActivity.NEED_REFRESH, getString(R.string.recover_success)).sendToTarget();
                    }
                }
        );
    }

    private void recover() {
        Map<Integer, String> map = waveLengthHelper.getWaveLengthMap();
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            recoverSingleWaveLength(entry.getKey() + 1);
        }
        loadTemperatureAdd();
        recoverSuccess();
    }

    private void recoverSingleWaveLength(int waveLength) {
        settingTestMethod(waveLength);
        settingClean();
        reloadStandardDatas(waveLength);
    }

    private void recoverSuccess() {
        final Future<Void> future = ExecutorService.getInstance().submit(
                new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        final ArrayBlockingQueue<TrunkData> trunkDatas = new ArrayBlockingQueue<TrunkData>(1);
                        trunkUtil.addListener(new TrunkListener() {
                            @Override
                            public int getListenType() {
                                return TrunkConst.TYPE_RECOVER;
                            }

                            @Override
                            public void notifyData(TrunkData trunkData) {
                                trunkUtil.removeListener(R.id.setting_initail_factory_recover_btn);
                                trunkDatas.offer(trunkData);
                            }
                        }, R.id.setting_initail_factory_recover_btn);
                        for (int m = 0; m < 3; m++) {
                            trunkUtil.sendCmd(TrunkConst.RECOVER);
                            for (int k = 0; k < 3; k++) {
                                try {
                                    TrunkData trunkData = trunkDatas.poll(1000, TimeUnit.MILLISECONDS);
                                    Log.d("recover", "recovering");
                                    if (trunkData != null) {
                                        Log.d("recover", "recover success");
                                        return null;
                                    }
                                } catch (Exception e) {
                                    //IGNORE
                                }
                            }
                        }
                        return null;
                    }
                }
        );
        try {
            future.get();
        } catch (Exception e) {
            e.printStackTrace();
            Message.obtain(settingInitialHandler, BaseActivity.SHOW_MSG, e.getMessage()).sendToTarget();
        }
    }

    private void reloadStandardDatas(final int waveLength) {
        List<StandardData> standardDatas = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            final int j = i + 1;
            final Future<StandardData> future = ExecutorService.getInstance().submit(
                    new Callable<StandardData>() {
                        @Override
                        public StandardData call() throws Exception {
                            final ArrayBlockingQueue<TrunkData> trunkDatas = new ArrayBlockingQueue<TrunkData>(1);
                            trunkUtil.addListener(new TrunkListener() {
                                @Override
                                public int getListenType() {
                                    return TrunkConst.TYPE_RECOVER_STANDARD;
                                }

                                @Override
                                public void notifyData(TrunkData trunkData) {
                                    trunkUtil.removeListener(R.id.setting_initail_factory_recover_btn);
                                    trunkDatas.offer(trunkData);
                                }
                            }, R.id.setting_initail_factory_recover_btn);
                            for (int m = 0; m < 3; m++) {
                                trunkUtil.sendStandardRecover(waveLength, j);
                                for (int k = 0; k < 3; k++) {
                                    try {
                                        TrunkData trunkData = trunkDatas.poll(1000, TimeUnit.MILLISECONDS);
                                        Log.d("recover", "loading " + waveLength + " standard value " + j);
                                        if (trunkData != null) {
                                            Log.d("recover", "load " + waveLength + " standard value success" + j);
                                            return (StandardData) trunkData.getData();
                                        }
                                    } catch (Exception e) {
                                        //IGNORE
                                    }
                                }
                            }
                            throw new Exception("recovery failed");
                        }
                    }
            );
            try {
                standardDatas.add(future.get());
            } catch (Exception e) {
                Message.obtain(settingInitialHandler, BaseActivity.SHOW_MSG, e.getMessage()).sendToTarget();
            }
        }
        List<StandardData> newData = new ArrayList<>();
        for (int i = 0; i < standardDatas.size(); i++) {
            if (standardDatas.get(i).getTestValue() != 0) {
                newData.add(standardDatas.get(i));
            }
        }
        standardDatas.clear();
        standardDatas.addAll(newData);
        DBService.getStandardValueHelper(getActivity()).refreshStandardDatas(standardDatas, waveLength);
    }

    private void settingTestMethod(final int waveLength) {
        final Future<Void> future = ExecutorService.getInstance().submit(
                new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        final ArrayBlockingQueue<TrunkData> trunkDatas = new ArrayBlockingQueue<TrunkData>(1);
                        trunkUtil.addListener(new TrunkListener() {
                            @Override
                            public int getListenType() {
                                return TrunkConst.TYPE_TESTSETTING;
                            }

                            @Override
                            public void notifyData(TrunkData trunkData) {
                                trunkUtil.removeListener(R.id.setting_initail_factory_recover_btn);
                                trunkDatas.offer(trunkData);
                                Log.d("recover", "setting method success");
                            }
                        }, R.id.setting_initail_factory_recover_btn);
                        trunkUtil.setting(waveLength, true);
                        while (true) {
                            try {
                                TrunkData trunkData = trunkDatas.poll(1000, TimeUnit.MILLISECONDS);
                                Log.d("recover", "setting method");
                                if (trunkData != null) {
                                    Log.d("recover", "setting method success");
                                    return null;
                                }
                            } catch (Exception e) {
                                //IGNORE
                            }
                        }
                    }
                }
        );
        try {
            future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void settingClean() {
        final Future<Void> future = ExecutorService.getInstance().submit(
                new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        final ArrayBlockingQueue<TrunkData> trunkDatas = new ArrayBlockingQueue<TrunkData>(1);
                        trunkUtil.addListener(new TrunkListener() {
                            @Override
                            public int getListenType() {
                                return TrunkConst.TYPE_CLEAN;
                            }

                            @Override
                            public void notifyData(TrunkData trunkData) {
                                trunkUtil.removeListener(R.id.setting_initail_factory_recover_btn);
                                trunkDatas.offer(trunkData);
                            }
                        }, R.id.setting_initail_factory_recover_btn);
                        trunkUtil.clean();
                        while (true) {
                            try {
                                TrunkData trunkData = trunkDatas.poll(1000, TimeUnit.MILLISECONDS);
                                Log.d("recover", "cleaning");
                                if (trunkData != null) {
                                    Log.d("recover", "clean success");
                                    return null;
                                }
                            } catch (Exception e) {
                                //IGNORE
                            }
                        }
                    }
                }
        );
        try {
            future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void loadTemperatureAdd() {
        final Future<TrunkData> future = ExecutorService.getInstance().submit(
                new Callable<TrunkData>() {
                    @Override
                    public TrunkData call() throws Exception {
                        final ArrayBlockingQueue<TrunkData> trunkDatas = new ArrayBlockingQueue<TrunkData>(1);
                        trunkUtil.addListener(new TrunkListener() {
                            @Override
                            public int getListenType() {
                                return TrunkConst.TYPE_TEMPRETURE_ADD_INIT;
                            }

                            @Override
                            public void notifyData(TrunkData trunkData) {
                                trunkUtil.removeListener(R.id.setting_initail_factory_recover_btn);
                                trunkDatas.offer(trunkData);
                            }
                        }, R.id.setting_initail_factory_recover_btn);
                        trunkUtil.sendTemperatureInit();
                        while (true) {
                            try {
                                TrunkData trunkData = trunkDatas.poll(1000, TimeUnit.MILLISECONDS);
                                Log.d("recover", "loading temperature_add");
                                if (trunkData != null) {
                                    Log.d("recover", "load temperature_add success");
                                    return trunkData;
                                }
                            } catch (Exception e) {
                                //IGNORE
                            }
                        }
                    }
                }
        );
        try {
            float temperatue = (float) (future.get().getData());
            baseActivity.temperatureAdd(String.format("%.1f", temperatue));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void saveTemperatureAdd() {
        final Future<TrunkData> future = ExecutorService.getInstance().submit(
                new Callable<TrunkData>() {
                    @Override
                    public TrunkData call() throws Exception {
                        final ArrayBlockingQueue<TrunkData> trunkDatas = new ArrayBlockingQueue<TrunkData>(1);
                        trunkUtil.addListener(new TrunkListener() {
                            @Override
                            public int getListenType() {
                                return TrunkConst.TYPE_TEMPRETURE_ADD;
                            }

                            @Override
                            public void notifyData(TrunkData trunkData) {
                                trunkUtil.removeListener(R.id.setting_initail_factory_recover_btn);
                                trunkDatas.offer(trunkData);
                            }
                        }, R.id.setting_initail_factory_recover_btn);
                        trunkUtil.sendTemperatureAdd(temperatureAddValue);
                        while (true) {
                            try {
                                TrunkData trunkData = trunkDatas.poll(1000, TimeUnit.MILLISECONDS);
                                Log.d("recover", "saving temperature_add");
                                if (trunkData != null) {
                                    Log.d("recover", "saving temperature_add success");
                                    return trunkData;
                                }
                            } catch (Exception e) {
                                //IGNORE
                            }
                        }
                    }
                }
        );
        try {
            future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    private void ensureCover() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle(getString(R.string.prompt));
        builder.setMessage(getString(R.string.ensure_cover));
        builder.setPositiveButton(this.getActivity().getString(R.string.submit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                doCover();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(this.getActivity().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }

    private void doCover() {
        ExecutorService.getInstance().execute(
                new Runnable() {
                    @Override
                    public void run() {
                        waveLengthHelper.coverInitWaveLength(saveWaveLength());
                        WaveLengthService.getInstance().notifyChanged();
                        Cache.setWaveLengths(waveLengthHelper.getWaveLengths());
                        Activity activity = getActivity();
                        if (activity instanceof BaseActivity) {
                            BaseActivity baseActivity = (BaseActivity) activity;
                            baseActivity.cover();
                            baseActivity.setShowLogo(!checkBox.isChecked());
                            baseActivity.setInitShowLogo(!checkBox.isChecked());
                            String autoClean3Str = autoClean3.getText().toString();
                            if (autoClean3Str != null && autoClean3Str.length() != 0) {
                                baseActivity.setAutoClean3(Float.parseFloat(autoClean3Str));
                                baseActivity.setInitAutoClean3(Float.parseFloat(autoClean3Str));
                            }
                            Cache.setAutoClean3(Float.parseFloat(autoClean3Str));
                            String autoClean4Str = autoClean4.getText().toString();
                            if (autoClean4Str != null && autoClean4Str.length() != 0) {
                                baseActivity.setAutoClean4(Float.parseFloat(autoClean4Str));
                                baseActivity.setInitAutoClean4(Float.parseFloat(autoClean4Str));
                            }
                            Cache.setAutoClean4(Float.parseFloat(autoClean4Str));
                        }

                        Message.obtain(settingInitialHandler, BaseActivity.HOLD_WINDOW_YES, "正在写入出场设置").sendToTarget();
                        trunkUtil.addListener(new TrunkListener() {
                            @Override
                            public int getListenType() {
                                return TrunkConst.TYPE_SAVE_FACTORY;
                            }

                            @Override
                            public void notifyData(TrunkData trunkData) {
                                trunkUtil.removeListener(R.id.setting_initail_factory_save_btn);
                                Message.obtain(settingInitialHandler, BaseActivity.NEED_REFRESH, "recover sucess").sendToTarget();
                                Message.obtain(settingInitialHandler, BaseActivity.HOLD_WINDOW_NO, "出场设置写入完成").sendToTarget();
                            }
                        }, R.id.setting_initail_factory_save_btn);
                        trunkUtil.sendCmd(TrunkConst.SETFACTORY);
                    }
                }
        );
    }
}
