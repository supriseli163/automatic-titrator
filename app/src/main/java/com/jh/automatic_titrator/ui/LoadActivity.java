package com.jh.automatic_titrator.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.common.Cache;
import com.jh.automatic_titrator.common.conf.FactorySetting;
import com.jh.automatic_titrator.common.db.AuditHelper;
import com.jh.automatic_titrator.common.db.UserHelper;
import com.jh.automatic_titrator.common.trunk.TrunkConst;
import com.jh.automatic_titrator.common.trunk.TrunkData;
import com.jh.automatic_titrator.common.trunk.TrunkListener;
import com.jh.automatic_titrator.common.trunk.TrunkUtil;
import com.jh.automatic_titrator.common.utils.JsonHelper;
import com.jh.automatic_titrator.common.utils.SharedPreferenceUtils;
import com.jh.automatic_titrator.common.utils.TimeTool;
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

import java.util.ArrayList;
import java.util.List;

public class LoadActivity extends BaseActivity implements View.OnFocusChangeListener, AdapterView.OnItemClickListener, View.OnClickListener {

    public static final int LOGIN_SUCCESS = 200;
    public static final int LOGIN_FAILED = 400;
    public static final int SHOW_MSG = 0xFFFFFFFF;
    public static final int SHOW_HISTORY_USER = 0xFFF00000;
    TrunkUtil trunkUtil;
    private EditText usernameEt;
    private EditText passwordEt;
    private Handler handler;
    private Button loginBtn;
    private Intent intent;
    private boolean showLogo;
    private View logoImage;
    private UserHelper userHelper;
    private View view;
    private Dialog dialog;
    private AuditHelper auditHelper;
    private ListView mLoginUserLv;
    private List<String> mHistoryUsers;
    private HistoryUserAdapter mHistoryUserAdapter;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        selectLanguage((String) SharedPreferenceUtils.get(this, "language", "zh"));
        setContentView(R.layout.activity_load_page);

        mToast = ToastUtil.createToast(this);

        view = findViewById(R.id.activity_load);
        view.setOnClickListener(this);
        trunkUtil = TrunkUtil.getInstance();
        auditHelper = DBService.getAuditHelper(this);
        FactorySetting.ensureFirstInit();

        userHelper = DBService.getUserHelper(this);
        mHistoryUsers = userHelper.historyUser();
        if (mHistoryUsers == null) {
            mHistoryUsers = new ArrayList<>();
        }
        Cache.setWaveLengths(DBService.getWaveLengthHelper(this).getWaveLengths());

        handler = new LoadHandler();


        showLogo = showLogo();
        logoImage = findViewById(R.id.img_logo);
        if (showLogo) {
            logoImage.setVisibility(View.VISIBLE);
        } else {
            logoImage.setVisibility(View.INVISIBLE);
        }
        usernameEt = (EditText) findViewById(R.id.login_user_account);
        mLoginUserLv = (ListView) findViewById(R.id.login_user_list);
        mHistoryUserAdapter = new HistoryUserAdapter(this, mHistoryUsers);
        usernameEt.setOnFocusChangeListener(this);
        mLoginUserLv.setOnItemClickListener(this);
        mLoginUserLv.setVisibility(View.INVISIBLE);
        mLoginUserLv.setAdapter(mHistoryUserAdapter);
        passwordEt = (EditText) findViewById(R.id.login_user_password);
        loginBtn = (Button) findViewById(R.id.login_button);

        intent = new Intent();
        intent.setClass(LoadActivity.this, HomePageActivity.class);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = usernameEt.getText().toString();
                String password = passwordEt.getText().toString();
                doLogin(userName, password);
            }
        });
        checkTemperature();

        usernameEt.requestFocus();
        Cache.setDateOffset(getTimeAdd());
        String userName = userHelper.getAutoLoginUser();
        if (userName != null) {
            doAutoLogin(userName);
            return;
        }
    }

    private void checkTemperature() {
        TrunkUtil.getInstance().addListener(new TrunkListener() {
            @Override
            public int getListenType() {
                return TrunkConst.TYPE_TEMPRETURE_STATE;
            }

            @Override
            public void notifyData(TrunkData trunkData) {
                switch ((int) trunkData.getData()) {
                    case 0:
                        Message.obtain(handler, SHOW_MSG, "温度模块正常").sendToTarget();
                        break;
                    case 1:
                        Message.obtain(handler, SHOW_MSG, "温度模块异常").sendToTarget();
                        break;
                    case 2:
                        Message.obtain(handler, SHOW_MSG, "温度不在控制范围内").sendToTarget();
                        break;
                }
            }
        }, R.id.activity_load);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mHistoryUsers.clear();
        mHistoryUsers.addAll(userHelper.historyUser());
        mHistoryUserAdapter.notifyDataSetChanged();
        passwordEt.setText("");
    }

    private void loadCache(User user) {
        Cache.setWaveLengths(DBService.getWaveLengthHelper(this).getWaveLengths());
        Cache.setAutoClean(getAutoClean());
        Cache.setAutoClean3(getAutoClean3());
        Cache.setAutoClean4(getAutoClean4());
        List<Formula> formulas = new ArrayList<>();
        formulas.addAll(Cache.getBaseFormula());
        formulas.addAll(DBService.getFormulaHelper(this).listFormula());
        Cache.refreshFormulas(formulas);
        if (user.getSettingConfig() != null && user.getSettingConfig().length() > 0) {
            try {
                readUserValues(user.getSettingConfig());
                setCacheFormula();
                return;
            } catch (Exception e) {
                //ignore
            }
        }
        readSystemValues();
        setCacheFormula();
    }

    private void setCacheFormula() {
        Formula formula = Cache.getFormulaByName(Cache.getTestMethod().getFormulaName());
        if (formula == null) {
            Cache.setCurrentFormula(Cache.getBaseFormula().get(0));
            return;
        }
        if (!(formula instanceof OpticalRotation) &&
                !(formula instanceof Concentration) &&
                !(formula instanceof SpecificRotation)) {
            formula = DBService.getFormulaHelper(this).findFormula(Cache.getTestMethod().getFormulaName());
            Cache.setCurrentFormula(formula);
        } else {
            Cache.setCurrentFormula(formula);
        }
    }

    private void readSystemValues() {
        Cache.setTestId(TimeTool.getTimeedId());
        Cache.setTestName(getString(R.string.default_test_name));
        Cache.setTestMethod(getDefaultMethod());
        Log.d("loadCache", "loadCache: success");
    }

    private void readUserValues(String userValues) {
        SettingConfig settingConfig = JsonHelper.fromJson(userValues, SettingConfig.class);
        Cache.setTestId(settingConfig.getTestId());
        Cache.setTestMethod(settingConfig.getTestMethod());
        Cache.setTestName(settingConfig.getTestName());
    }

    private void checkHandShake(final int wavelength) {
        new Thread() {
            @Override
            public void run() {
                //此处放用户设置的波长
                trunkUtil.chooseWaveLength(wavelength);
                trunkUtil.addListener(new TrunkListener() {
                    @Override
                    public int getListenType() {
                        return TrunkConst.TYPE_WAVELENGTH_CHOOSE;
                    }

                    @Override
                    public void notifyData(TrunkData trunkData) {
                        Message.obtain(handler, SHOW_MSG, "初始化成功").sendToTarget();
                        trunkUtil.removeListener(R.id.login_button);
                    }
                }, R.id.login_button);
            }
        }.start();

    }

    public void doAutoLogin(final String userName) {
        ExecutorService.getInstance().execute(
                new Runnable() {
                    @Override
                    public void run() {
                        User user = userHelper.getUserDetail(userName);
                        loadCache(user);
                        intent.putExtra("userAccount", userName);
                        checkHandShake(Cache.getWaveLengthPosition(Cache.getTestMethod().getWaveLength()) + 1);
                        startActivity(intent);
                        AuditService.addAuditService(userName, getString(R.string.login), null, getString(R.string.login_login), Cache.currentTime(), auditHelper);
                    }
                }
        );
    }

    public void doLogin(final String userAccount, final String userPassword) {
        ExecutorService.getInstance().execute(
                new Runnable() {
                    @Override
                    public void run() {
                        Message.obtain(handler, BaseActivity.HOLD_WINDOW_YES, getString(R.string.logining)).sendToTarget();
                        switch (userHelper.login(userAccount, userPassword)) {
                            case -1:
                                Message.obtain(handler, BaseActivity.HOLD_WINDOW_NO, getString(R.string.error_account)).sendToTarget();
                                break;
                            case 0:
                                Message.obtain(handler, BaseActivity.HOLD_WINDOW_NO, getString(R.string.null_account_info)).sendToTarget();
                                break;
                            case 1:
                                userHelper.cleanAutoLoginUser();
                                if (userAccount.equals(UserHelper.SUPER_ADMIN)) {
                                    loadCache(userHelper.getUserDetail("admin"));
                                } else {
                                    loadCache(userHelper.getUserDetail(userAccount));
                                }
                                Log.d("login", "login: success");
                                checkHandShake(Cache.getWaveLengthPosition(Cache.getTestMethod().getWaveLength() + 1));
                                AuditService.addAuditService(userAccount, getString(R.string.login), null, getString(R.string.login_login), Cache.currentTime(), auditHelper);
                                Message.obtain(handler, LOGIN_SUCCESS).sendToTarget();
                                Message.obtain(handler, BaseActivity.HOLD_WINDOW_NO, getString(R.string.login_success)).sendToTarget();
                                break;
                            case 2:
                                Message.obtain(handler, BaseActivity.HOLD_WINDOW_NO, getString(R.string.user_locked)).sendToTarget();
                                break;
                        }
                    }
                }
        );
    }

    public TestMethod getDefaultMethod() {
        TestMethod defaultMethod = new TestMethod();
        defaultMethod.setTestName(getString(R.string.default_method_name));
        defaultMethod.setAutoTest(false);
        defaultMethod.setAccuracy(0);
        defaultMethod.setAtlasX(0);
        defaultMethod.setAtlasY(0);
        defaultMethod.setUseTemperature(false);
        defaultMethod.setCreateDate(TimeTool.currentDate());
        defaultMethod.setDecimals(OpticalRotation.DEFAULT_DECIMAL);
        defaultMethod.setWaveLength("589");
//        defaultMethod.setTestTubeLength(20.d);
        defaultMethod.setTestCount(1);
        defaultMethod.setTemperatureType(0);
//        defaultMethod.setSpecificRotation(0d);
        defaultMethod.setFormulaName(new OpticalRotation().getFormulaName());
//        defaultMethod.setConcentration(0);
//        defaultMethod.setAutoTestInterval(1);
//        defaultMethod.setAutoTestTimes(1);

        return defaultMethod;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.login_user_account:
                if (hasFocus) {
                    passwordEt.setText("");
                    mLoginUserLv.setVisibility(View.VISIBLE);
                } else {
                    mLoginUserLv.setVisibility(View.INVISIBLE);
                }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String item = mHistoryUserAdapter.getItem(position).toString();
        mLoginUserLv.setVisibility(View.INVISIBLE);
        usernameEt.setText(item);
        passwordEt.requestFocus();
        passwordEt.setText("");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_load:
                mLoginUserLv.setVisibility(View.GONE);
                break;
            case R.id.login_user_password:
                mLoginUserLv.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void showDialog(Object obj) {
        if (dialog == null) {
            dialog = new ProgressDialog(LoadActivity.this);
        }
        dialog.setTitle(String.valueOf(obj));
        dialog.setCancelable(false);
        dialog.setDismissMessage(Message.obtain(handler));
        dialog.show();
    }

    private void hideDialog(Object obj) {
        dialog.dismiss();
        mToast.setText(obj + "");
        mToast.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showLogo = showLogo();
//        if (showLogo) {
//            logoImage.setVisibility(View.VISIBLE);
//        } else {
//            logoImage.setVisibility(View.GONE);
//        }
    }

    public class LoadHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOGIN_SUCCESS:
                    intent.putExtra("userAccount", usernameEt.getText().toString());
                    startActivity(intent);
                    break;
                case LOGIN_FAILED:
                    mToast.setText(msg.obj + "");
                    mToast.show();
                    break;
                case SHOW_MSG:
                    mToast.setText(msg.obj + "");
                    mToast.show();
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
                case SHOW_HISTORY_USER:
                    mLoginUserLv.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }
}
