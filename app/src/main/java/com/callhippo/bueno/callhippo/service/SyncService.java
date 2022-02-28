package com.callhippo.bueno.callhippo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
//import android.support.annotation.Nullable;
import android.util.Log;

import com.callhippo.bueno.callhippo.adapter.SyncAdapter;

import androidx.annotation.Nullable;

/**
 * Created by HP on 11-09-2020.
 */

public class SyncService extends Service {

    private static final Object sSyncAdapterLock = new Object();
    private static SyncAdapter mSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.e("syncService","Sync Service created.");
        synchronized (sSyncAdapterLock){
            if(mSyncAdapter == null){
                mSyncAdapter = new SyncAdapter(getApplicationContext(),
                        true);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("syncService","Sync Service binded.");
        return mSyncAdapter.getSyncAdapterBinder();
    }
}
