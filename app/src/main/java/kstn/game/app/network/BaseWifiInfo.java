package kstn.game.app.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import kstn.game.logic.network.WifiInfo;

public class BaseWifiInfo implements WifiInfo {
    private final Context context;
    private final int ipTethering = NetworkUtil.ipStringToInt("192.168.43.1");
    private final int maskTethering = NetworkUtil.ipStringToInt("255.255.255.0");

    public BaseWifiInfo(Context context) {
        this.context = context;
    }

    private boolean isTethering(WifiManager wifiManager) {
        try {
            final Method method = wifiManager.getClass().getDeclaredMethod("isWifiApEnabled");
            method.setAccessible(true);
            return (Boolean) method.invoke(wifiManager);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("No method");
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Can't call method");
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Can't call method");
        }
    };

    @Override
    public boolean isConnected() {
        ConnectivityManager connManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return mWifi.isConnected();
    }

    @Override
    public int getIP() {
        WifiManager wm = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        int ip = NetworkUtil.littleToBigEndian(wm.getConnectionInfo().getIpAddress());
        if (ip != 0)
            return ip;
        else if (isTethering(wm))
            return ipTethering;
        else
            return 0;
    }

    @Override
    public int getMask() {
        WifiManager wm = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        int mask = NetworkUtil.littleToBigEndian(wm.getDhcpInfo().netmask);
        if (mask != 0)
            return mask;
        else if (isTethering(wm))
            return maskTethering;
        else
            return 0;
    }
}
