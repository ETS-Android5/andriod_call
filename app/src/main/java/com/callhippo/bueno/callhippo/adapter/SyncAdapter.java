package com.callhippo.bueno.callhippo.adapter;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;

import com.callhippo.bueno.callhippo.model.MyContact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by HP on 11-09-2020.
 */

public class SyncAdapter extends AbstractThreadedSyncAdapter{


    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        Log.e("syncAdapter","Sync Adapter created.");
    }

    @Override
    public void onPerformSync(Account account, Bundle bundle, String s, ContentProviderClient contentProviderClient, SyncResult syncResult) {
        Log.e("syncAdapter","Sync Adapter called.");
    }



}
