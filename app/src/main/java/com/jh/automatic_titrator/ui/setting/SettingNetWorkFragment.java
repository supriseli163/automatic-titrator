package com.jh.automatic_titrator.ui.setting;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.net.DhcpInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.jh.automatic_titrator.R;
import com.jh.automatic_titrator.common.net.wifi.WifiHelper;
import com.jh.automatic_titrator.common.utils.ToastUtil;
import com.jh.automatic_titrator.service.ExecutorService;
import com.jh.automatic_titrator.service.WifiService;
import com.jh.automatic_titrator.ui.BaseActivity;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by apple on 16/9/24.
 */
public class SettingNetWorkFragment extends Fragment implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private RadioGroup networkList;

    private CheckBox dhcp_cb;
    private EditText ip_et;
    private EditText subnetmark_et;
    private EditText gateway_et;
    private EditText dns_et;

    private List<ScanResult> scanResults;

    private DhcpInfo dhcpInfo;

    private boolean isFirstConn = true;

    private Handler handler;

    private WifiInfo wifiInfo;

    private View saveBtn;

    private volatile int checkedId;

    private Toast mToast;

    private volatile boolean connected = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_network_fragment, container, false);

        mToast = ToastUtil.createToast(getActivity());

        handler = new NetworkHandler();
        dhcp_cb = (CheckBox) view.findViewById(R.id.setting_network_dhcp);

        ip_et = (EditText) view.findViewById(R.id.setting_network_ip);
        subnetmark_et = (EditText) view.findViewById(R.id.setting_network_subnet_mask);
        gateway_et = (EditText) view.findViewById(R.id.setting_network_gateway);
        dns_et = (EditText) view.findViewById(R.id.setting_network_dns);

        networkList = (RadioGroup) view.findViewById(R.id.setting_network_list);
        networkList.setOnCheckedChangeListener(this);

        scanResults = WifiHelper.getWifiList(getActivity());

        saveBtn = view.findViewById(R.id.setting_network_save_btn);
        saveBtn.setOnClickListener(this);

        if (WifiHelper.useDHCP(getActivity())) {
            dhcp_cb.setChecked(true);
            ip_et.setEnabled(false);
            subnetmark_et.setEnabled(false);
            dns_et.setEnabled(false);
            gateway_et.setEnabled(false);
            saveBtn.setVisibility(View.GONE);
        } else {
            dhcp_cb.setChecked(false);
            ip_et.setEnabled(true);
            subnetmark_et.setEnabled(true);
            dns_et.setEnabled(true);
            gateway_et.setEnabled(true);
            saveBtn.setVisibility(View.VISIBLE);
        }
        dhcp_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ip_et.setEnabled(false);
                    subnetmark_et.setEnabled(false);
                    dns_et.setEnabled(false);
                    gateway_et.setEnabled(false);
                    saveBtn.setVisibility(View.GONE);
                } else {
                    ip_et.setEnabled(true);
                    subnetmark_et.setEnabled(true);
                    dns_et.setEnabled(true);
                    gateway_et.setEnabled(true);
                    saveBtn.setVisibility(View.VISIBLE);
                }
            }
        });
        refreshNetwork();
        refreshWifiList();
        WifiService.getInstance(getActivity()).addWifiListener(new WifiService.WifiListener() {
            @Override
            public void onChange(boolean connected) {
                Activity activity = getActivity();
                if (SettingNetWorkFragment.this.connected != connected) {
                    if (activity != null && !activity.isDestroyed()) {
                        scanResults = WifiHelper.getWifiList(activity);
                        Message.obtain(handler, BaseActivity.NEED_REFRESH).sendToTarget();
                    }
                }
                SettingNetWorkFragment.this.connected = connected;
            }
        }, R.id.setting_network_list);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        WifiService.getInstance(getActivity()).removeWifiListener(R.id.setting_network_list);
    }

    private void refreshNetwork() {
        try {
            dhcpInfo = WifiHelper.getDHCP(getActivity());
            wifiInfo = WifiHelper.getWifiInfo(getActivity());

            if (dhcpInfo != null && dhcpInfo.ipAddress != 0) {
                subnetmark_et.setText(WifiHelper.changeIntToIp(dhcpInfo.netmask));
                gateway_et.setText(WifiHelper.changeIntToIp(dhcpInfo.gateway));
                dns_et.setText(WifiHelper.changeIntToIp(dhcpInfo.dns1));
                ip_et.setText(WifiHelper.changeIntToIp(dhcpInfo.ipAddress));
            } else {
                subnetmark_et.setText(WifiHelper.changeIntToIp(0));
                gateway_et.setText(WifiHelper.changeIntToIp(0));
                dns_et.setText(WifiHelper.changeIntToIp(0));
                ip_et.setText(WifiHelper.changeIntToIp(0));
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), R.string.network_get_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void refreshWifiList() {
        isFirstConn = true;
        networkList.removeAllViews();
        int i = 0;
        if (scanResults != null) {
            for (ScanResult scanResult : scanResults) {
                RadioButton radioButton = new RadioButton(getActivity());
                if (scanResult.SSID == null || scanResult.SSID.trim().length() == 0) {
                    radioButton.setText(scanResult.BSSID);
                } else {
                    radioButton.setText(scanResult.SSID);
                }
                radioButton.setTextColor(getResources().getColor(R.color.fontGray));
                radioButton.setTextSize(14);
                radioButton.setId(i);

                i++;
                if (wifiInfo != null && wifiInfo.getSSID().equals("\"" + scanResult.SSID + "\"")) {
                    radioButton.setChecked(true);
                    checkedId = i - 1;
                }
                networkList.addView(radioButton);
            }
        }
        isFirstConn = false;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (isFirstConn) {
            isFirstConn = false;
            return;
        }
        final ScanResult scanresult = scanResults.get(checkedId);
        View passwordInputView = LayoutInflater.from(getActivity()).inflate(R.layout.input_password, null);

        final EditText password = (EditText) passwordInputView.findViewById(R.id.input_password_et);

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle(getString(R.string.prompt));
        builder.setMessage(getString(R.string.password));
        builder.setView(passwordInputView);
        builder.setPositiveButton(this.getActivity().getString(R.string.submit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                changeNetwork(scanresult, password.getText().toString());
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

    private void changeNetwork(ScanResult scanresult, String password) {
        int type = 1;
        if (scanresult.capabilities.toUpperCase().startsWith("[WPA")) {
            type = 3;
        } else if (scanresult.capabilities.toUpperCase().startsWith("[WEP")) {
            type = 2;
        }
        WifiHelper.createWifiInfo(getActivity(), password, scanresult, type);
        Message.obtain(handler, BaseActivity.NEED_REFRESH).sendToTarget();
    }

    private void changeNetwork(String ssid, InetAddress ip, InetAddress submark, InetAddress gateway, InetAddress dns) {
        try {
            int prefix = getSubarkPrefix(submark);
            WifiHelper.updateWifiInfo(getActivity(), ssid, ip, prefix, gateway, dns);
            Message.obtain(handler, BaseActivity.NEED_REFRESH).sendToTarget();
            Message.obtain(handler, BaseActivity.SAVE_SUCCESS).sendToTarget();
        } catch (Exception e) {
            e.printStackTrace();
            Message.obtain(handler, BaseActivity.SHOW_MSG, getString(R.string.static_ip_not_supported)).sendToTarget();
        }
    }

    private int getSubarkPrefix(InetAddress submark) {
        int count = 0;
        for (byte b : submark.getAddress()) {
            int a = (b & 0xff);
            if (a == 255) {
                count += 8;
            } else {
                count += (int) (Math.log(b + 1) / Math.log(2));
                break;
            }
        }
        return count;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_network_save_btn:
                ExecutorService.getInstance().execute(
                        new Runnable() {
                            @Override
                            public void run() {
                                int position = checkedId;
                                try {
                                    InetAddress ip = InetAddress.getByName(ip_et.getText().toString());
                                    InetAddress subnetmark = InetAddress.getByName(subnetmark_et.getText().toString());
                                    InetAddress gateway = InetAddress.getByName(gateway_et.getText().toString());
                                    InetAddress dns = InetAddress.getByName(dns_et.getText().toString());
                                    changeNetwork(scanResults.get(position).SSID, ip, subnetmark, gateway, dns);
                                } catch (UnknownHostException e) {
                                    Message.obtain(handler, BaseActivity.SHOW_MSG, getString(R.string.illegal_ip)).sendToTarget();
                                }
                            }
                        }
                );
                break;
        }
    }

    public class NetworkHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BaseActivity.NEED_REFRESH:
                    refreshWifiList();
                    refreshNetwork();
                    break;
                case BaseActivity.SAVE_SUCCESS:
                    ToastUtil.toastShow(mToast, getString(R.string.save_success));
                    break;
                case BaseActivity.SHOW_MSG:
                    ToastUtil.toastShow(mToast, String.valueOf(msg.obj));
            }
        }
    }
}
