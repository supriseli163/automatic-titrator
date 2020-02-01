package com.jh.automatic_titrator.ui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.common.Cache;
import com.jh.automatic_titrator.common.EBusEvent;
import com.jh.automatic_titrator.common.db.UserHelper;
import com.jh.automatic_titrator.common.utils.SharedPreferenceUtils;
import com.jh.automatic_titrator.common.utils.TimeTool;
import com.jh.automatic_titrator.entity.common.User;
import com.jh.automatic_titrator.service.DBService;
import com.jh.automatic_titrator.service.TimeService;
import com.jh.automatic_titrator.service.WifiService;
import com.jh.automatic_titrator.ui.data.DataFragment;
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
public class HomePageActivity extends BaseActivity implements View.OnClickListener {

    private static final int REFRESH_TIME = 0x00001000;
    private static final int REFRESH_WIFI = 0x00002000;

    public static String currentUserName;

    protected FragmentManager fragmentManager;

    private String userAccount;

    private TestFragment testFragment;

    private DataFragment dataFragment;

    private UserFragment userFragment;

    private HelpFragment helpFragment;

    private SettingFragment settingFragment;

    private TextView userName;

    private View testLayout;
    private ImageView testImage;
    private TextView testText;

    private View dataLayout;
    private ImageView dataImage;
    private TextView dataText;

    private View settingLayout;
    private ImageView settingImage;
    private TextView settingText;

    private View helpLayout;
    private ImageView helpImage;
    private TextView helpText;

    private View userLayout;
    private ImageView userImage;
    private TextView userText;
    private TextView tv_thisDevType;

    private UserHelper userHelper;

    private ImageView userHead;

    private User user;

    private Handler handler;

    private int headImg;

    private boolean showLogo;

    private View logoImage;

    //private View menu;

    private View layout;

//    private ScaleAnimation menuHideAnimation;
//
//    private ScaleAnimation menuShowAnimation;

//    private ScaleAnimation layoutHideAnimation;
//
//    private ScaleAnimation layoutShowAnimation;

    private int windowWidth;

    private View mMenuBtn;

    //private View mLogo1;

    private TextView mTimeTv;

    private TimeService mTimeService;

    private WifiService mWifiService;

    private View mWifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        selectLanguage((String) SharedPreferenceUtils.get(this, "language", "zh"));
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_home_page);

        fragmentManager = getFragmentManager();

        View window = findViewById(R.id.home_page_layout);

        showLogo = showLogo();
        logoImage = findViewById(R.id.home_page_menu_icon);
        //mLogo1 = findViewById(R.id.home_page_logo1);
        if (showLogo) {
            logoImage.setVisibility(View.VISIBLE);
            //mLogo1.setVisibility(View.VISIBLE);
        } else {
            logoImage.setVisibility(View.GONE);
            //mLogo1.setVisibility(View.GONE);
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

        tv_thisDevType = (TextView) findViewById(R.id.home_device_name);
        tv_thisDevType.setText(readconf("version").isEmpty()?"":readconf("version"));

        userName = (TextView) findViewById(R.id.home_user_account_txt);
        userName.setText(user.getUserName());

        testLayout = findViewById(R.id.home_test_layout);
        testImage = (ImageView) findViewById(R.id.home_test_icon);
        testText = (TextView) findViewById(R.id.home_test_txt);
        testLayout.setOnClickListener(this);

        dataLayout = findViewById(R.id.home_data_layout);
        dataImage = (ImageView) findViewById(R.id.home_data_icon);
        dataText = (TextView) findViewById(R.id.home_data_txt);
        dataLayout.setOnClickListener(this);

        settingLayout = findViewById(R.id.home_setting_layout);
        settingImage = (ImageView) findViewById(R.id.home_setting_icon);
        settingText = (TextView) findViewById(R.id.home_setting_txt);
        settingLayout.setOnClickListener(this);

        helpLayout = findViewById(R.id.home_help_layout);
        helpImage = (ImageView) findViewById(R.id.home_help_icon);
        helpText = (TextView) findViewById(R.id.home_help_txt);
        helpLayout.setOnClickListener(this);

        userLayout = findViewById(R.id.home_user_layout);
        userImage = (ImageView) findViewById(R.id.home_user_icon);
        userText = (TextView) findViewById(R.id.home_user_txt);
        userLayout.setOnClickListener(this);

        userHead = (ImageView) findViewById(R.id.home_user_head_img);
        headImg = user.getHeadId();
        userHead.setImageResource(headImg);

        mTimeTv = (TextView) findViewById(R.id.home_time);
        mTimeTv.setText(TimeTool.currentDate());

        //menu = findViewById(R.id.home_page_menu);
//        menuShowAnimation = new ScaleAnimation(0, 1, 1, 1);
//        menuShowAnimation.setDuration(300);
//        menuHideAnimation = new ScaleAnimation(1, 0, 1, 1);
//        menuHideAnimation.setDuration(300);

        layout = findViewById(R.id.home_page_title);
        windowWidth = window.getWidth();
        float scale = ((float) windowWidth) / layout.getWidth();
//        layoutShowAnimation = new ScaleAnimation(1, scale, 1, 1);
//        layoutShowAnimation.setDuration(300);
//        layoutHideAnimation = new ScaleAnimation(scale, 1, 1, 1);
//        layoutHideAnimation.setDuration(300);

        mMenuBtn = findViewById(R.id.home_page_menu_icon);
        mMenuBtn.setOnClickListener(this);

        showDefaultFragment();

        mTimeService = TimeService.getInstance(this);
        mTimeService.addTimeListener(mTimeListener, R.id.home_page_menu_icon);

        mWifi = findViewById(R.id.home_wifi);
        mWifi.setVisibility(View.GONE);
        mWifiService = WifiService.getInstance(this);
        mWifiService.addWifiListener(mWifiListener, R.id.home_wifi);

        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onEventMainThread(EBusEvent event) {
        tv_thisDevType.setText(event.getMsg());
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
//        Cache.setCorrectTesting(false);
        mTimeService.stop();
        mWifiService.stop();
    }

    @Override
    public void onClick(View v) {
//        if (v.getId() == R.id.home_page_menu_icon) {
//            showMenu();
//            return;
//        }
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
            case R.id.home_data_layout:
                changeToDataFragment();
                if (dataFragment == null) {
                    dataFragment = new DataFragment();
                    dataFragment.setActivityHandler(handler);
                    fragmentTransaction.add(R.id.home_frame, dataFragment);
                } else {
                    fragmentTransaction.show(dataFragment);
                }
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
        //menu.startAnimation(menuHideAnimation);
        //layout.startAnimation(layoutShowAnimation);

        //menu.setVisibility(View.GONE);
        //mMenuBtn.setVisibility(View.VISIBLE);
//        layout.setMinimumWidth(windowWidth);
    }

    private void showMenu() {
        //menu.startAnimation(menuShowAnimation);
        //menu.setVisibility(View.VISIBLE);
        mMenuBtn.setVisibility(View.GONE);
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
        testLayout.setBackground(getResources().getDrawable(R.color.icon_choose_true));
        testImage.setImageResource(R.drawable.left_icon_cs2);
        testText.setTextColor(getResources().getColor(R.color.colorWrite));
    }

    private void changeToDataFragment() {
        dataLayout.setBackground(getResources().getDrawable(R.color.icon_choose_true));
        dataImage.setImageResource(R.drawable.left_icon_sj2);
        dataText.setTextColor(getResources().getColor(R.color.colorWrite));
    }

    private void changeToSettingFragment() {
        settingLayout.setBackground(getResources().getDrawable(R.color.icon_choose_true));
        settingImage.setImageResource(R.drawable.left_icon_sz2);
        settingText.setTextColor(getResources().getColor(R.color.colorWrite));
    }

    private void changeToHelpFragment() {
        helpLayout.setBackground(getResources().getDrawable(R.color.icon_choose_true));
        helpImage.setImageResource(R.drawable.left_icon_bz2);
        helpText.setTextColor(getResources().getColor(R.color.colorWrite));
    }

    private void changeToUserFragment() {
        userLayout.setBackground(getResources().getDrawable(R.color.icon_choose_true));
        userImage.setImageResource(R.drawable.left_icon_yh2);
        userText.setTextColor(getResources().getColor(R.color.colorWrite));
    }

    private void clearSelection() {
        testLayout.setBackground(getResources().getDrawable(R.color.icon_choose_false));
        testImage.setImageResource(R.drawable.left_icon_cs);
        testText.setTextColor(getResources().getColor(R.color.color4d6083));

        dataLayout.setBackground(getResources().getDrawable(R.color.icon_choose_false));
        dataImage.setImageResource(R.drawable.left_icon_sj);
        dataText.setTextColor(getResources().getColor(R.color.color4d6083));

        settingLayout.setBackground(getResources().getDrawable(R.color.icon_choose_false));
        settingImage.setImageResource(R.drawable.left_icon_sz);
        settingText.setTextColor(getResources().getColor(R.color.color4d6083));

        helpLayout.setBackground(getResources().getDrawable(R.color.icon_choose_false));
        helpImage.setImageResource(R.drawable.left_icon_bz);
        helpText.setTextColor(getResources().getColor(R.color.color4d6083));

        userLayout.setBackground(getResources().getDrawable(R.color.icon_choose_false));
        userImage.setImageResource(R.drawable.left_icon_yh);
        userText.setTextColor(getResources().getColor(R.color.color4d6083));
    }

    private void hideFragments(FragmentTransaction fragmentTransaction) {
        if (testFragment != null) {
            fragmentTransaction.hide(testFragment);
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
                    userHead.setImageResource(headId);
                    break;
                case REFRESH_TIME:
                    mTimeTv.setText(TimeTool.currentDate());
                    break;
                case REFRESH_WIFI:
                    boolean connected = (boolean) msg.obj;
                    if (connected) {
                        mWifi.setVisibility(View.VISIBLE);
                    } else {
                        mWifi.setVisibility(View.GONE);
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
