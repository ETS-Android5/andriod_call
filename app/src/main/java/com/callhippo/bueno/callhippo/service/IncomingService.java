package com.callhippo.bueno.callhippo.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

/**
 * Created by appit on 4/21/2017.
 */

public class IncomingService extends Service {
    private static final String TAG = "IncomingService";

    SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate: Called");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Toast.makeText(IncomingService.this, "Incoming Service Called", Toast.LENGTH_SHORT).show();
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: Called");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(101);
        stopSelf();
        Toast.makeText(this,"Call Ended",Toast.LENGTH_LONG).show();
//        Intent intent = new Intent(this, DialerActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
        Log.e(TAG, "onTaskRemoved: Called");
    }
}
