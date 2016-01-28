
package com.android.server.wifi;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.BaseDhcpStateMachine;
import android.net.DhcpStateMachine;
import android.net.TrafficStats;
import android.net.dhcp.DhcpClient;
import android.net.ip.IpReachabilityMonitor;
import android.os.Handler;
import android.os.IBinder;
import android.os.ServiceManager;
import android.provider.Settings;

import com.android.internal.util.StateMachine;
import com.android.server.wifi.hotspot2.SupplicantBridge;
import com.android.server.wifi.hotspot2.omadm.MOManager;
import com.android.server.wifi.hotspot2.osu.OSUManager;

/**
 * This class allows overriding objects with mocks to write unit tests
 */
public class FrameworkFacade {
    public static final String TAG = "FrameworkFacade";

    public BaseWifiLogger makeBaseLogger() {
        return new BaseWifiLogger();
    }

    public BaseWifiLogger makeRealLogger(WifiStateMachine stateMachine, WifiNative wifiNative) {
        return new WifiLogger(stateMachine, wifiNative);
    }

    public int getIntegerSetting(Context context, String name, int def) {
        return Settings.Global.getInt(context.getContentResolver(), name, def);
    }

    public long getLongSetting(Context context, String name, long def) {
        return Settings.Global.getLong(context.getContentResolver(), name, def);
    }

    public String getStringSetting(Context context, String name) {
        return Settings.Global.getString(context.getContentResolver(), name);
    }

    public IBinder getService(String serviceName) {
        return ServiceManager.getService(serviceName);
    }

    public PendingIntent getBroadcast(Context context, int requestCode, Intent intent, int flags) {
        return PendingIntent.getBroadcast(context, requestCode, intent, flags);
    }

    public OSUManager makeOsuManager(WifiConfigStore wifiConfigStore, Context context,
             SupplicantBridge supplicantBridge, MOManager moManager,
             WifiStateMachine wifiStateMachine) {
        return new OSUManager(wifiConfigStore, context,
                supplicantBridge, moManager, wifiStateMachine);
    }

    public SupplicantStateTracker makeSupplicantStateTracker(Context context,
             WifiStateMachine wifiStateMachine, WifiConfigStore configStore, Handler handler) {
        return new SupplicantStateTracker(context, wifiStateMachine, configStore, handler);
    }

    public WifiApConfigStore makeApConfigStore(Context context, Handler handler) {
        return WifiApConfigStore.makeWifiApConfigStore(context, handler);
    }

    public long getTxPackets(String iface) {
        return TrafficStats.getTxPackets(iface);
    }

    public long getRxPackets(String iface) {
        return TrafficStats.getRxPackets(iface);
    }

    public BaseDhcpStateMachine makeDhcpStateMachine(
            Context context, StateMachine controller, String intf) {
        if (getIntegerSetting(context, Settings.Global.LEGACY_DHCP_CLIENT, 0) == 1) {
            return DhcpStateMachine.makeDhcpStateMachine(context, controller, intf);
        } else {
            return DhcpClient.makeDhcpStateMachine(context, controller, intf);
        }
    }

    public IpReachabilityMonitor makeIpReachabilityMonitor(
            Context context, String iface, IpReachabilityMonitor.Callback callback) {
        return new IpReachabilityMonitor(context, iface, callback);
    }

}
