/*
 * Copyright 2015 Anton Tananaev (anton@traccar.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jaunt.client;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.util.Log;

import androidx.preference.PreferenceManager;

public class NetworkManager extends BroadcastReceiver {

    private static final String TAG = NetworkManager.class.getSimpleName();

    private Context context;
    private NetworkHandler handler;
    private ConnectivityManager connectivityManager;

    private SharedPreferences preferences;
    private boolean wifiOnly;
    private boolean chargingOnly;
    private boolean isCharging;

    public NetworkManager(Context context, NetworkHandler handler) {
        this.context = context;
        this.handler = handler;
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get preferences
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        wifiOnly = preferences.getBoolean(MainFragment.KEY_WIFI_ONLY, false);
        chargingOnly = preferences.getBoolean(MainFragment.KEY_CHARGING_ONLY, false);
    }

    public interface NetworkHandler {
        void onNetworkUpdate(boolean isOnline);
    }

    public boolean isOnline() {
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean connection = false;
        if (activeNetwork != null) {
            connection = activeNetwork.isConnectedOrConnecting();
            if (wifiOnly) {
                Log.d(TAG, "__charge : wifi checking" + connection);
                connection = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
                Log.d(TAG, "__charge : wifi changed" + connection);
            }
            if (chargingOnly) {
                Log.d(TAG, "__charge : charging checking" + connection);
                connection = isCharging;
                Log.d(TAG, "__charge : charging changed" + connection);
            }
        }
        return connection;
    }

    public void start() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        context.registerReceiver(this, filter);
    }

    public void stop() {
        context.unregisterReceiver(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if ((intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION) ||
                intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) &&
                handler != null) {
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL;
            boolean isOnline = isOnline();
            Log.i(TAG, "network " + (isOnline ? "on" : "off"));
            handler.onNetworkUpdate(isOnline);
        }
    }

}
