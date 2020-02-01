package com.jh.automatic_titrator.common.net.wifi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by apple on 2016/11/12.
 */
public class WifiHelper {

    //网络连接列表
    private static List<WifiConfiguration> wifiConfigurations;

    //扫描出的网络连接列表
    private static List<ScanResult> wifiList;

    public static List<ScanResult> getWifiList(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifiManager.startScan();
        wifiList = wifiManager.getScanResults();
        wifiConfigurations = wifiManager.getConfiguredNetworks();
        return wifiList;
    }

    public static WifiInfo getWifiInfo(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wifiManager.getConnectionInfo();
    }

    public static NetworkInfo getNetwork(Context context) {
        ConnectivityManager connectionManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();

        return networkInfo;
    }

    public static InetAddress getAddress() throws SocketException {
        Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
        while (enumeration.hasMoreElements()) {
            NetworkInterface networkInterface = enumeration.nextElement();
            Enumeration<InetAddress> addressEnumeration = networkInterface.getInetAddresses();
            while (addressEnumeration.hasMoreElements()) {
                InetAddress inetAddress = addressEnumeration.nextElement();
                if (!inetAddress.isLoopbackAddress()) {
                    return inetAddress;
                }
            }
        }
        return null;
    }

    public static DhcpInfo getDHCP(Context context) {
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wm.getDhcpInfo();
    }

    public static String changeIntToIp(int ip) {
        StringBuffer sb = new StringBuffer();
        sb.append(String.valueOf((ip & 0xff)));
        sb.append('.');
        sb.append(String.valueOf(((ip >> 8) & 0xff)));
        sb.append('.');
        sb.append(String.valueOf(((ip >> 16) & 0xff)));
        sb.append('.');
        sb.append(String.valueOf(((ip >> 24) & 0xff)));
        return sb.toString();
    }

    public static WifiConfiguration createWifiInfo(Context context, String password, ScanResult scanResult, int type) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + scanResult.SSID + "\"";

        WifiConfiguration tempConfig = isExsits(wifiManager, scanResult.SSID);
        if (tempConfig != null) {
            wifiManager.removeNetwork(tempConfig.networkId);
        }

        if (type == 1) //WIFICIPHER_NOPASS
        {
            config.wepKeys[0] = "";
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        if (type == 2) //WIFICIPHER_WEP
        {
            config.hiddenSSID = true;
            config.wepKeys[0] = "\"" + password + "\"";
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        if (type == 3) //WIFICIPHER_WPA
        {
            config.preSharedKey = "\"" + password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        }
        android.provider.Settings.System.putString(context.getContentResolver(), android.provider.Settings.System.WIFI_USE_STATIC_IP, "0");
        int netWorkId = wifiManager.addNetwork(config);
        wifiManager.enableNetwork(netWorkId, true);
        return config;
    }

    private static WifiConfiguration isExsits(WifiManager wifiManager, String SSID) {
        List<WifiConfiguration> existingConfigs = wifiManager.getConfiguredNetworks();
        for (WifiConfiguration existingConfig : existingConfigs) {
            if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
                return existingConfig;
            }
        }
        return null;
    }

    public static WifiConfiguration updateWifiInfo(Context context, String ssid, InetAddress ip, int prefix, InetAddress gateway, InetAddress dns) throws Exception {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiConfiguration config = isExsits(wifiManager, ssid);
        if (config != null) {
            Object ipAssignment = getEnumValue("android.net.IpConfiguration$IpAssignment", "STATIC");
            callMethod(config, "setIpAssignment", new String[]{"android.net.IpConfiguration$IpAssignment"}, new Object[]{ipAssignment});

            // Then set properties in StaticIpConfiguration.
            Object staticIpConfig = newInstance("android.net.StaticIpConfiguration");
            Object linkAddress = newInstance("android.net.LinkAddress", new Class<?>[]{InetAddress.class, int.class}, new Object[]{ip, prefix});

            setField(staticIpConfig, "ipAddress", linkAddress);
            setField(staticIpConfig, "gateway", gateway);
            getField(staticIpConfig, "dnsServers", ArrayList.class).clear();
            getField(staticIpConfig, "dnsServers", ArrayList.class).add(dns);

            callMethod(config, "setStaticIpConfiguration", new String[]{"android.net.StaticIpConfiguration"}, new Object[]{staticIpConfig});

            int netWorkId = wifiManager.updateNetwork(config);
            wifiManager.enableNetwork(netWorkId, true);
            return config;
        }
        return null;
    }

    public static boolean useDHCP(Context context) {
        return !"1".equals(android.provider.Settings.System.getString(context.getContentResolver(), android.provider.Settings.System.WIFI_USE_STATIC_IP));
    }

    public static void changeDHCP(Context context, boolean use) {
        if (use) {
            android.provider.Settings.System.putString(context.getContentResolver(), android.provider.Settings.System.WIFI_USE_STATIC_IP, "0");
        } else {
            android.provider.Settings.System.putString(context.getContentResolver(), android.provider.Settings.System.WIFI_USE_STATIC_IP, "1");
        }
    }

    private static Object newInstance(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
        return newInstance(className, new Class<?>[0], new Object[0]);
    }

    private static Object newInstance(String className, Class<?>[] parameterClasses, Object[] parameterValues) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException {
        Class<?> clz = Class.forName(className);
        Constructor<?> constructor = clz.getConstructor(parameterClasses);
        return constructor.newInstance(parameterValues);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static Object getEnumValue(String enumClassName, String enumValue) throws ClassNotFoundException {
        Class<Enum> enumClz = (Class<Enum>) Class.forName(enumClassName);
        return Enum.valueOf(enumClz, enumValue);
    }

    private static void setField(Object object, String fieldName, Object value) throws IllegalAccessException, IllegalArgumentException, NoSuchFieldException {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.set(object, value);
    }

    private static <T> T getField(Object object, String fieldName, Class<T> type) throws IllegalAccessException, IllegalArgumentException, NoSuchFieldException {
        Field field = object.getClass().getDeclaredField(fieldName);
        return type.cast(field.get(object));
    }

    private static void callMethod(Object object, String methodName, String[] parameterTypes, Object[] parameterValues) throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        Class<?>[] parameterClasses = new Class<?>[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++)
            parameterClasses[i] = Class.forName(parameterTypes[i]);

        Method method = object.getClass().getDeclaredMethod(methodName, parameterClasses);
        method.invoke(object, parameterValues);
    }
}
