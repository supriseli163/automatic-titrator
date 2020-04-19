package com.jh.automatic_titrator.ui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.common.Cache;
import com.jh.automatic_titrator.common.EBusEvent;
import com.jh.automatic_titrator.common.db.UserHelper;
import com.jh.automatic_titrator.common.utils.SharedPreferenceUtils;
import com.jh.automatic_titrator.common.utils.TimeTool;
import com.jh.automatic_titrator.databinding.ActivityHomePageBinding;
import com.jh.automatic_titrator.entity.common.User;
import com.jh.automatic_titrator.service.DBService;
import com.jh.automatic_titrator.service.TimeService;
import com.jh.automatic_titrator.service.WifiService;
import com.jh.automatic_titrator.ui.data.DataFragment;
import com.jh.automatic_titrator.ui.data.method.ModifyMethodFragment;
import com.jh.automatic_titrator.ui.execute.ExecuteFragment;
import com.jh.automatic_titrator.ui.help.HelpFragment;
import com.jh.automatic_titrator.ui.setting.SettingFragment;
import com.jh.automatic_titrator.ui.test.TestFragment;
import com.jh.automatic_titrator.ui.user.UserFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class HomePageActivity extends BaseActivity<ActivityHomePageBinding> implements View.OnClickListener {

    private static final int REFRESH_TIME = 0x00001000;
    private static final int REFRESH_WIFI = 0x00002000;

    public static String currentUserName;

    protected FragmentManager fragmentManager;
    private String userAccount;

    private TestFragment testFragment;
    private ExecuteFragment executeFragment;
    private ModifyMethodFragment modifyMethodFragment;
    private DataFragment dataFragment;
    private UserFragment userFragment;
    private HelpFragment helpFragment;
    private SettingFragment settingFragment;

    private UserHelper userHelper;
    private User user;
    private Handler handler;
    private int headImg;
    private boolean showLogo;
    private int windowWidth;

    private TimeService mTimeService;
    private WifiService mWifiService;

    @Override
    public void onInit(Bundle savedInstanceState) {
        selectLanguage((String) SharedPreferenceUtils.get(this, "language", "zh"));
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        fragmentManager = getFragmentManager();
        showLogo = showLogo();
        if (showLogo) {
            viewBinding.homePageMenuIcon.setVisibility(View.VISIBLE);
        } else {
            viewBinding.homePageMenuIcon.setVisibility(View.GONE);
        }
        handler = new HomePageHandler();
        Intent intent = getIntent();
        userAccount = intent.getStringExtra("userAccount");
        userHelper = DBService.getUserHelper(this);
        if (userAccount != null) {
            user = userHelper.getUserDetail(userAccount);
            Cache.setUser(user);
        } else {
            user = Cache.getUser();
            userAccount = user.getUserName();
        }
        viewBinding.homeDeviceName.setText(readconf("version").isEmpty() ? "" : readconf("version"));
        viewBinding.homeUserAccountTxt.setText(user.getUserName());
        viewBinding.homeTestLayout.setOnClickListener(this);
        viewBinding.homeTitratorTestLayout.setOnClickListener(this);
        viewBinding.homeDataLayout.setOnClickListener(this);
        viewBinding.homeSettingLayout.setOnClickListener(this);
        viewBinding.homeHelpLayout.setOnClickListener(this);
        viewBinding.homeUserLayout.setOnClickListener(this);
        headImg = user.getHeadId();
        viewBinding.homeUserHeadImg.setImageResource(headImg);
        viewBinding.homeTime.setText(TimeTool.currentDate());
        viewBinding.homePageMenuIcon.setOnClickListener(this);
        showDefaultFragment();
        mTimeService = TimeService.getInstance(this);
        mTimeService.addTimeListener(mTimeListener, R.id.home_page_menu_icon);
        viewBinding.homeWifi.setVisibility(View.GONE);
        mWifiService = WifiService.getInstance(this);
        mWifiService.addWifiListener(mWifiListener, R.id.home_wifi);
        EventBus.getDefault().register(this);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_home_page;
    }

    @Subscribe
    public void onEventMainThread(EBusEvent event) {
        viewBinding.homeDeviceName.setText(event.getMsg());
    }

    private WifiService.WifiListener mWifiListener = new WifiService.WifiListener() {
        @Override
        public void onChange(boolean connected) {
            Message.obtain(handler, REFRESH_WIFI, connected).sendToTarget();
        }
    };

    private TimeService.TimeListener mTimeListener = new TimeService.TimeListener() {
        @Override
        public void timeRefresh(long currentTime) {
            Message.obtain(handler, REFRESH_TIME).sendToTarget();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Cache.setTesting(false);
        mTimeService.stop();
        mWifiService.stop();
    }

    @Override
    public void onClick(View v) {
        clearSelection();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideFragments(fragmentTransaction);
        switch (v.getId()) {
            case R.id.home_test_layout:
                changeToTestFragment();
                if (testFragment == null) {
                    testFragment = new TestFragment();
                    testFragment.setActivityHandler(handler);
                    fragmentTransaction.add(R.id.home_frame, testFragment);
                } else {
                    fragmentTransaction.show(testFragment);
                }
                break;
            case R.id.home_titrator_test_layout:
                changeToExecuteFragment();
                if (executeFragment == null) {
                    executeFragment = new ExecuteFragment();
                    executeFragment.setActivityHandler(handler);
                    fragmentTransaction.add(R.id.home_frame, executeFragment);
                } else {
                    fragmentTransaction.show(executeFragment);
                }
                break;
            case R.id.home_data_layout:
                changeToDataFragment();
                if (dataFragment == null) {
                    dataFragment = new DataFragment();
                    dataFragment.setActivityHandler(handler);
                    fragmentTransaction.add(R.id.home_frame, dataFragment);
                } else {
                    fragmentTransaction.show(dataFragment);
                }
//                if (modifyMethodFragment == null) {
//                    modifyMethodFragment = new ModifyMethodFragment();
//                    modifyMethodFragment.setActivityHandler(handler);
//                    fragmentTransaction.add(R.id.home_frame, modifyMethodFragment);
//                } else {
//                    fragmentTransaction.show(modifyMethodFragment);
//                }
                break;
            case R.id.home_setting_layout:
                changeToSettingFragment();
                if (settingFragment == null) {
                    settingFragment = new SettingFragment();
                    settingFragment.setActivityHandler(handler);
                    fragmentTransaction.add(R.id.home_frame, settingFragment);
                } else {
                    fragmentTransaction.show(settingFragment);
                }
                break;
            case R.id.home_help_layout:
                changeToHelpFragment();
                if (helpFragment == null) {
                    helpFragment = new HelpFragment();
                    helpFragment.setActivityHandler(handler);
                    fragmentTransaction.add(R.id.home_frame, helpFragment);
                } else {
                    fragmentTransaction.show(helpFragment);
                }
                break;
            case R.id.home_user_layout:
                changeToUserFragment();
                if (userFragment == null) {
                    userFragment = new UserFragment();
                    userFragment.setActivityHandler(handler);
                    fragmentTransaction.add(R.id.home_frame, userFragment);
                } else {
                    fragmentTransaction.show(userFragment);
                }
                break;
        }
        fragmentTransaction.commit();
    }

    private void showMenu() {
        viewBinding.homePageMenuIcon.setVisibility(View.GONE);
    }

    private void showDefaultFragment() {
        clearSelection();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideFragments(fragmentTransaction);
        changeToTestFragment();
        testFragment = new TestFragment();
        testFragment.setActivityHandler(handler);
        fragmentTransaction.add(R.id.home_frame, testFragment);
        fragmentTransaction.commit();
    }

    private void changeToTestFragment() {
        viewBinding.homeTestLayout.setBackground(getResources().getDrawable(R.color.icon_choose_true));
        viewBinding.homeTestIcon.setImageResource(R.drawable.left_icon_cs2);
        viewBinding.homeTestTxt.setTextColor(getResources().getColor(R.color.colorWrite));
    }

    private void changeToExecuteFragment() {
//        home_titrator_test_text
        viewBinding.homeTitratorTestLayout.setBackground(getResources().getDrawable(R.color.icon_choose_true));
        viewBinding.homeTitratorTestText.setTextColor(getResources().getColor(R.color.colorWrite));
        viewBinding.homeTitratorTestIcon.setImageResource(R.drawable.left_icon_cs2);
    }

    private void changeToDataFragment() {
        viewBinding.homeDataLayout.setBackground(getResources().getDrawable(R.color.icon_choose_true));
        viewBinding.homeDataIcon.setImageResource(R.drawable.left_icon_sj2);
        viewBinding.homeDataTxt.setTextColor(getResources().getColor(R.color.colorWrite));
    }

    private void changeToSettingFragment() {
        viewBinding.homeSettingLayout.setBackground(getResources().getDrawable(R.color.icon_choose_true));
        viewBinding.homeSettingIcon.setImageResource(R.drawable.left_icon_sz2);
        viewBinding.homeSettingTxt.setTextColor(getResources().getColor(R.color.colorWrite));
    }

    private void changeToHelpFragment() {
        viewBinding.homeHelpLayout.setBackground(getResources().getDrawable(R.color.icon_choose_true));
        viewBinding.homeHelpIcon.setImageResource(R.drawable.left_icon_bz2);
        viewBinding.homeHelpTxt.setTextColor(getResources().getColor(R.color.colorWrite));
    }

    private void changeToUserFragment() {
        viewBinding.homeUserLayout.setBackground(getResources().getDrawable(R.color.icon_choose_true));
        viewBinding.homeUserIcon.setImageResource(R.drawable.left_icon_yh2);
        viewBinding.homeUserTxt.setTextColor(getResources().getColor(R.color.colorWrite));
    }

    private void clearSelection() {
        viewBinding.homeTitratorTestLayout.setBackground(getResources().getDrawable(R.color.icon_choose_false));
        viewBinding.homeTitratorTestIcon.setImageResource(R.drawable.left_icon_cs);
        viewBinding.homeTitratorTestText.setTextColor(getResources().getColor(R.color.color4d6083));

        viewBinding.homeTestLayout.setBackground(getResources().getDrawable(R.color.icon_choose_false));
        viewBinding.homeTestIcon.setImageResource(R.drawable.left_icon_cs);
        viewBinding.homeTestTxt.setTextColor(getResources().getColor(R.color.color4d6083));

        viewBinding.homeDataLayout.setBackground(getResources().getDrawable(R.color.icon_choose_false));
        viewBinding.homeDataIcon.setImageResource(R.drawable.left_icon_sj);
        viewBinding.homeDataTxt.setTextColor(getResources().getColor(R.color.color4d6083));

        viewBinding.homeSettingLayout.setBackground(getResources().getDrawable(R.color.icon_choose_false));
        viewBinding.homeSettingIcon.setImageResource(R.drawable.left_icon_sz);
        viewBinding.homeSettingTxt.setTextColor(getResources().getColor(R.color.color4d6083));

        viewBinding.homeHelpLayout.setBackground(getResources().getDrawable(R.color.icon_choose_false));
        viewBinding.homeHelpIcon.setImageResource(R.drawable.left_icon_bz);
        viewBinding.homeHelpTxt.setTextColor(getResources().getColor(R.color.color4d6083));

        viewBinding.homeUserLayout.setBackground(getResources().getDrawable(R.color.icon_choose_false));
        viewBinding.homeUserIcon.setImageResource(R.drawable.left_icon_yh);
        viewBinding.homeUserTxt.setTextColor(getResources().getColor(R.color.color4d6083));
    }

    private void hideFragments(FragmentTransaction fragmentTransaction) {
        if (testFragment != null) {
            fragmentTransaction.hide(testFragment);
        }
        if (executeFragment != null) {
            fragmentTransaction.hide(executeFragment);
        }
        if (dataFragment != null) {
            fragmentTransaction.hide(dataFragment);
        }
        if (settingFragment != null) {
            fragmentTransaction.hide(settingFragment);
        }
        if (helpFragment != null) {
            fragmentTransaction.hide(helpFragment);
        }
        if (userFragment != null) {
            fragmentTransaction.hide(userFragment);
        }
        if (modifyMethodFragment != null) {
            fragmentTransaction.hide(modifyMethodFragment);
        }
    }

    public void testClick(View view) {
        Toast.makeText(this, "sssss", Toast.LENGTH_LONG);
    }

    public class HomePageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case USER_UPDATE:
                    updateUser();
                    break;
                case NEED_REFRESH:
                    int headId = user.getHeadId();
                    viewBinding.homeUserHeadImg.setImageResource(headId);
                    break;
                case REFRESH_TIME:
                    viewBinding.homeTime.setText(TimeTool.currentDate());
                    break;
                case REFRESH_WIFI:
                    boolean connected = (boolean) msg.obj;
                    if (connected) {
                        viewBinding.homeWifi.setVisibility(View.VISIBLE);
                    } else {
                        viewBinding.homeWifi.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    }

    private void updateUser() {
        new Thread() {
            @Override
            public void run() {
                user = userHelper.getUserDetail(Cache.getUser().getUserName());
                Cache.setUser(user);
                headImg = user.getHeadId();
                Message.obtain(handler, NEED_REFRESH).sendToTarget();
            }
        }.start();
    }
}
