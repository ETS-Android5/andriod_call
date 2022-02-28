package com.callhippo.bueno.callhippo.service;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.callhippo.bueno.callhippo.IncomingActivity;
import com.callhippo.bueno.callhippo.OngoingActivity;
import com.callhippo.bueno.callhippo.R;
import com.callhippo.bueno.callhippo.Utils.Constants;
import com.google.firebase.analytics.FirebaseAnalytics;

//import org.linphone.core.Call;
//import org.linphone.core.Core;
//import org.linphone.core.CoreListenerStub;
//import org.linphone.core.Factory;
//import org.linphone.core.LogCollectionState;
//import org.linphone.core.ProxyConfig;
//import org.linphone.core.RegistrationState;
//import org.linphone.mediastream.Log;
//import org.linphone.mediastream.Version;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Timer;
import java.util.TimerTask;
//import android.util.Log;


import org.linphone.core.AccountCreator;
import org.linphone.core.Call;
import org.linphone.core.Core;
import org.linphone.core.CoreListenerStub;
import org.linphone.core.Factory;
import org.linphone.core.LogCollectionState;
import org.linphone.core.ProxyConfig;
import org.linphone.core.RegistrationState;
import org.linphone.mediastream.Log;
import org.linphone.mediastream.Version;


import io.socket.client.Socket;

//import com.example.linphone.OngoingCall;
//import com.example.linphone.R;
//import com.example.linphone.activities.CallActivity;

//import androidx.annotation.Nullable;

public class LinphoneService extends Service {

    private static final String START_LINPHONE_LOGS = " ==== Device information dump ====";
    // Keep a static reference to the Service so we can access it from anywhere in the app
    private static LinphoneService sInstance;

    private Handler mHandler;
    private Timer mTimer;

    private Core mCore;
    private CoreListenerStub mCoreListener;
    org.linphone.core.Call incoming_call;
    private FirebaseAnalytics mFirebaseAnalytics;
    SharedPreferences sharedPreferences;
    private final String MY_PREFERANCES = "callhippomaulik";
    private static final String TAG = "LinphoneService";
    private Socket socket;
    SharedPreferences.Editor editor;
    String socket_data="";
    private static final String NOTIFICATION_CHANNEL_ID2 = "my_notification_channel_call";
    String x_to_tr_number="",x_from_tr_number="",x_from_tr_id="",x_ph_trid="",x_incoming_name="",x_department_name="",x_integration_name="",x_ph_network_str="",callNumber="",transferBy="",x_calltransfer="", x_ph_extensioncall="", provider="";
    String lastcall_type="",lastcall_fullName="",lastcall_date="",lastcall_status="";

    public static final String tw_NOTIFICATION = "tw_call_accept_outgoing";
    MediaPlayer mediaPlayer;
    String freeswitchuname="",freeswitchpass="",freeswitchdomain="",freeswitchtranspot="";
    private AccountCreator mAccountCreator;
    ProxyConfig cfg;
    public static boolean isReady() {
        return sInstance != null;
    }

    public static LinphoneService getInstance() {
        return sInstance;
    }

    public static Core getCore() {
        return sInstance.mCore;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();



        android.util.Log.e("LinphoneService_lp","onCreate ");
        String basePath = getFilesDir().getAbsolutePath();
        Factory.instance().setLogCollectionPath(basePath);
        Factory.instance().enableLogCollection(LogCollectionState.Enabled);
        Factory.instance().setDebugMode(true, getString(R.string.app_name));


        org.linphone.mediastream.Log.i(START_LINPHONE_LOGS);
        dumpDeviceInformation();
        dumpInstalledLinphoneInformation();


        mHandler = new Handler();
        // This will be our main Core listener, it will change activities depending on events
        mCoreListener = new CoreListenerStub() {
            @Override
            public void onCallStateChanged(Core core, Call call, Call.State state, String message) {
//                Toast.makeText(LinphoneService.this, message, Toast.LENGTH_SHORT).show();
                android.util.Log.e("LinphoneService","call_state: "+state+" message: "+message);

                try
                {
                    if(state==Call.State.Error && message.equalsIgnoreCase("Request timeout"))
                    {

                    }

                }
                catch (Exception e)
                {

                }

                if(state == Call.State.OutgoingInit)
                {
                    Log.e(TAG," outgoingcall_lp_service");
                    Intent i = new Intent(getApplicationContext(), OngoingActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    startActivity(i);
                }


                if (state == Call.State.IncomingReceived || call.getState()== Call.State.IncomingEarlyMedia)
                {
                    incoming_call=call;


                    try
                    {

                    }
                    catch (Exception e)
                    {
                      provider="";x_ph_extensioncall="";  x_to_tr_number="";x_from_tr_number="";x_from_tr_id="";x_ph_trid="";x_incoming_name="";x_department_name="";x_integration_name="";x_ph_network_str="";callNumber="";transferBy="";x_calltransfer="";
                    }



                    try
                    {
                        if (Build.VERSION.SDK_INT >= 29)
                        {
                            try
                            {
                                if(x_incoming_name!=null)
                                {
                                    if(!x_incoming_name.equalsIgnoreCase(""))
                                    {
                                        x_incoming_name= URLDecoder.decode(x_incoming_name, "UTF-8");
                                    }
                                }

                            }
                            catch (Exception e){}

                        }
                        else
                        {
                            try
                            {
                                if(!IncomingActivity.isActivityRunning())
                                {
                                    Intent i = new Intent(getApplicationContext(), IncomingActivity.class);


                                    startActivity(i);
                                }
                            }
                            catch (Exception e)
                            {
                                Intent i = new Intent(getApplicationContext(), IncomingActivity.class);

                                startActivity(i);
                            }


                        }

                    }
                    catch (Exception e)
                    {


                        try
                        {
                            if(!IncomingActivity.isActivityRunning())
                            {
                                Intent i = new Intent(getApplicationContext(), IncomingActivity.class);
                                startActivity(i);
                            }
                        }
                        catch (Exception e1)
                        {
                            Intent i = new Intent(getApplicationContext(), IncomingActivity.class);

                            startActivity(i);
                        }


                    }









//                    android.util.Log.e("LinphoneService","incoming_xname"+x_incoming_name+":"+callNumber);



                }
                else if (state == Call.State.Connected)
                {
                    android.util.Log.e("lp","mCoreListener_Connected ");
                }

                else if (state == Call.State.Released)
                {
                    android.util.Log.e("LinphoneService","call_released ");

                    remove_notification();
                }
            }

            @Override
            public void onRegistrationStateChanged(Core lc, ProxyConfig cfg, RegistrationState cstate, String message)
            {
                super.onRegistrationStateChanged(lc, cfg, cstate, message);
                android.util.Log.e("LinphoneService","onRegistrationStateChanged: "+cstate);
            }


        };

        try {
            // Let's copy some RAW resources to the device
            // The default config file must only be installed once (the first time)
            copyIfNotExist(R.raw.linphonerc_default, basePath + "/.linphonerc");
            // The factory config is used to override any other setting, let's copy it each time
            copyFromPackage(R.raw.linphonerc_factory, "linphonerc");
        } catch (IOException ioe) {
         //   Log.e(ioe);
        }

        // Create the Core and add our listener
        mCore = Factory.instance()
                .createCore(basePath + "/.linphonerc", basePath + "/linphonerc", this);
        mCore.addListener(mCoreListener);
        // Core is ready to be configured
        configureCore();
        try
        {
//            socket();
        }
        catch (Exception e)
        {
            android.util.Log.e(TAG,"excp_ss "+e.getMessage());
            // to verify that socket has connected successfully or not check firebase analysis socket connect event (if user has version info 1.4.3 or above (check from chadmin))
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        android.util.Log.e("LinphoneService_lp","onStartCommand ");

        try
        {
            if(sInstance!=null)
            {
                if(getCore()!=null)
                {
                    RegistrationState proxyConfig=getCore().getDefaultProxyConfig().getState();
                    android.util.Log.e(TAG,"proxyConfig "+proxyConfig);

                    if(proxyConfig!=RegistrationState.Ok)
                    {
                        sInstance = this;

                        // Core must be started after being created and configured
                        mCore.start();
                        // We also MUST call the iterate() method of the Core on a regular basis
                        TimerTask lTask =
                                new TimerTask() {
                                    @Override
                                    public void run() {
                                        mHandler.post(
                                                new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        if (mCore != null) {
                                                            mCore.iterate();
                                                        }
                                                    }
                                                });
                                    }
                                };
                        mTimer = new Timer("Linphone scheduler");
                        mTimer.schedule(lTask, 0, 20);

                    }
                }

            }

        }
        catch (Exception e){}

        try
        {
            android.util.Log.e(TAG,"in_socket_c");


        }
        catch (Exception e)
        {
            android.util.Log.e(TAG,"excp_ss "+e.getMessage());
            // to verify that socket has connected successfully or not check firebase analysis socket connect event (if user has version info 1.4.3 or above (check from chadmin))
        }


//        Log.e("LinphoneService","onStartCommand");
        // If our Service is already running, no need to continue
        if (sInstance != null) {
            return START_STICKY;
        }

        // Our Service has been started, we can keep our reference on it
        // From now one the Launcher will be able to call onServiceReady()
        sInstance = this;

        // Core must be started after being created and configured
        try{mCore.start();}catch (Exception e){}
        // We also MUST call the iterate() method of the Core on a regular basis
        TimerTask lTask =
                new TimerTask() {
                    @Override
                    public void run() {
                        mHandler.post(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        if (mCore != null) {
                                            mCore.iterate();
                                        }
                                    }
                                });
                    }
                };
        mTimer = new Timer("Linphone scheduler");
        mTimer.schedule(lTask, 0, 20);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.e("LinphoneService","onDestroy");

        super.onDestroy();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        // For this sample we will kill the Service at the same time we kill the app
//        stopSelf();

        try
        {
//            Log.e("LinphoneService","onTaskRemoved");
////            mCore.removeListener(mCoreListener);
            mTimer.cancel();
//
//            // A stopped Core can be started again
////       `` mCore.stop();
//            // To ensure resources are freed, we must ensure it will be garbage collected
            mCore = null;
//            // Don't forget to free the singleton as well
            sInstance = null;
//
        }
        catch (Exception e){}
//
        super.onTaskRemoved(rootIntent);
    }

    private void configureCore() {
        // We will create a directory for user signed certificates if needed
        String basePath = getFilesDir().getAbsolutePath();
        String userCerts = basePath + "/user-certs";
        File f = new File(userCerts);
        if (!f.exists()) {
            if (!f.mkdir()) {
                Log.e(userCerts + " can't be created.");
            }
        }
        mCore.setUserCertificatesPath(userCerts);
    }

    private void dumpDeviceInformation() {
        StringBuilder sb = new StringBuilder();
        sb.append("DEVICE=").append(Build.DEVICE).append("\n");
        sb.append("MODEL=").append(Build.MODEL).append("\n");
        sb.append("MANUFACTURER=").append(Build.MANUFACTURER).append("\n");
        sb.append("SDK=").append(Build.VERSION.SDK_INT).append("\n");
        sb.append("Supported ABIs=");
        for (String abi : Version.getCpuAbis()) {
            sb.append(abi).append(", ");
        }
        sb.append("\n");
        Log.i(sb.toString());
    }

    private void dumpInstalledLinphoneInformation() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException nnfe) {
            Log.e(nnfe);
        }

        if (info != null) {
            Log.i(
                    "[Service] Linphone version is ",
                    info.versionName + " (" + info.versionCode + ")");
        } else {
            Log.i("[Service] Linphone version is unknown");
        }
    }

    private void copyIfNotExist(int ressourceId, String target) throws IOException {
        File lFileToCopy = new File(target);
        if (!lFileToCopy.exists()) {
            copyFromPackage(ressourceId, lFileToCopy.getName());
        }
    }

    private void copyFromPackage(int ressourceId, String target) throws IOException {
        FileOutputStream lOutputStream = openFileOutput(target, 0);
        InputStream lInputStream = getResources().openRawResource(ressourceId);
        int readByte;
        byte[] buff = new byte[8048];
        while ((readByte = lInputStream.read(buff)) != -1) {
            lOutputStream.write(buff, 0, readByte);
        }
        lOutputStream.flush();
        lOutputStream.close();
        lInputStream.close();
    }

    public void pick_call()
    {
        if(incoming_call!=null)
        {
            android.util.Log.e(TAG,"pickup_linphone");
            incoming_call.accept();
        }

    }





    public void remove_notification()
    {
        try
        {
            NotificationManager manager2 = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if(manager2!=null)
            {
                manager2.cancel(200);
            }
        }
        catch (Exception e)
        {

        }


    }













}
