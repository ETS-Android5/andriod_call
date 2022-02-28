/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.callhippo.bueno.callhippo.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
//import android.support.annotation.NonNull;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
//import androidx.core.content.LocalBroadcastManager;
import android.util.Log;

import com.callhippo.bueno.callhippo.DialerActivity;
import com.callhippo.bueno.callhippo.IncomingActivity;
import com.callhippo.bueno.callhippo.R;
import com.callhippo.bueno.callhippo.Utils.Constants;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private NotificationCompat.Builder builder;
    private int notification_id;
    SharedPreferences sharedPreferences;
    private final String MY_PREFERANCES = "callhippomaulik";
    SharedPreferences.Editor editor;private static final String NOTIFICATION_CHANNEL_ID = "my_notification_channel";
    public static String smsUUID,smsTime,smsContent,smsAuther,readStatus,threadName,threadID,sms_from,sms_to,chnumberID,threadNameTitle;
    static final public String COPA_RESULT = "";
    static final public String COPA_MESSAGE = "";
    private LocalBroadcastManager broadcaster;
    Boolean is_lp=false;
    private FirebaseAnalytics mFirebaseAnalytics;
    public static final String tw_NOTIFICATION = "tw_call_incoming_firebase";
    Context context;

    public static Ringtone mRingTone_10,mRingTone_10_p;
    public static Uri notification_10,notification_10_p;
    String masking="";
    public static PhoneNumberUtil phoneNumberUtil;
    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.


        String xph=remoteMessage.getData().get("twi_params");
        if(xph!=null)
        {
            try {
                xph= URLDecoder.decode(xph, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.e(TAG,"x-ph1:"+xph);
        }


        sharedPreferences = getSharedPreferences(MY_PREFERANCES, MODE_PRIVATE);
        context=this;

        try{
        masking = sharedPreferences.getString("masking","");
        }catch(Exception e){}

        if (remoteMessage.getNotification()!= null){
            String messagebody = remoteMessage.getNotification().getBody();
            String title = remoteMessage.getNotification().getTitle();
        }


        if(remoteMessage.getData().containsKey("calllogs")){

            String calllog=remoteMessage.getData().get("calllogs");
        }































    }



    // [END receive_message]



































}
