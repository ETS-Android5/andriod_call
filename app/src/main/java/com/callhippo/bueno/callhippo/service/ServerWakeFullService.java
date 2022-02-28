package com.callhippo.bueno.callhippo.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.sip.SipAudioCall;
import android.net.sip.SipProfile;
import android.util.Log;

/**
 * Created by appit on 5/22/2017.
 */

public class ServerWakeFullService extends BroadcastReceiver {
    String TAG = "ServerWakeFullService";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "onReceive: Called");


    }
}