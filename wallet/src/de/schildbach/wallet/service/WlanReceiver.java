package de.schildbach.wallet.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.preference.PreferenceManager;

import de.schildbach.wallet.Configuration;

public class WlanReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (action.equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)) {
            if (intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false)) {
                // enter Wifi
            } else {
                // WLAN lost
                final Configuration config = new Configuration(PreferenceManager.getDefaultSharedPreferences(context), context.getResources());
                if (config.isPeerWifiOnly()) {
                    final Intent intentB = new Intent(context, BlockchainServiceImpl.class);
                    context.stopService(intentB);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        context.startForegroundService(intentB);
                    } else {
                        context.startService(intentB);
                    }
                }
            }
        }
    }
}
