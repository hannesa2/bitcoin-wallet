package de.schildbach.wallet.service;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;

import de.schildbach.wallet.Configuration;

public class WlanReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (action.equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)) {
            final Configuration config = new Configuration(PreferenceManager.getDefaultSharedPreferences(context), context.getResources());
            final Intent intentB = new Intent(context, BlockchainServiceImpl.class);

            if (intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false)) {
                // enter Wifi
                if (!isMyServiceRunning(context, BlockchainServiceImpl.class)) {
                    ContextCompat.startForegroundService(context, intentB);
                }
            } else {
                // WLAN lost
                if (config.isPeerWifiOnly()) {
                    context.stopService(intentB);
                }
            }
        }
    }

    private boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
