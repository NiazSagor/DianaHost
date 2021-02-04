package com.angik.dianahost;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

public class MyApp extends Application {

    public static boolean IS_CONNECTED = true;

    @Override
    public void onCreate() {
        super.onCreate();

        this.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                    boolean noConnection = intent.getBooleanExtra(
                            ConnectivityManager.EXTRA_NO_CONNECTIVITY, false
                    );

                    IS_CONNECTED = !noConnection;
                }
            }
        }, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }
}
