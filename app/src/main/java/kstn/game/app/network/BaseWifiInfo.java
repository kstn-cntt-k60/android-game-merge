package kstn.game.app.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import kstn.game.logic.network.WifiInfo;

public class BaseWifiInfo implements WifiInfo {
    private final Context context;

    public BaseWifiInfo(Context context) {
        this.context = context;
    }

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
        return NetworkUtil.littleToBigEndian(wm.getConnectionInfo().getIpAddress());
    }

    @Override
    public int getMask() {
        WifiManager wm = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        return NetworkUtil.littleToBigEndian(wm.getDhcpInfo().netmask);
    }
}
