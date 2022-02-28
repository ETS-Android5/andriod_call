package com.callhippo.bueno.callhippo;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Handler;
import android.provider.ContactsContract;

//import androidx.core.app.Fragment;
//import androidx.core.app.FragmentManager;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.callhippo.bueno.callhippo.Utils.Constants;
import com.callhippo.bueno.callhippo.Utils.InternetConnectionManager;
import com.callhippo.bueno.callhippo.Utils.SessionManagement;
import com.callhippo.bueno.callhippo.Utils.SessionManagement_app_intro;
import com.callhippo.bueno.callhippo.Utils.Util;
import com.callhippo.bueno.callhippo.fragment.Dialer_Fragment;
import com.callhippo.bueno.callhippo.service.LinphoneService;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class Splash_newActivity extends AppCompatActivity  {

    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor editor;
    private final String  MY_PREFERANCES = "callhippomaulik";
    private static final String TAG = "Splash_activity";
    private String db_loginname="",db_loginpass="";
    private int db_loginID=0;
    private int db_login_credential_size=0;

    InternetConnectionManager internetConnection;
    Util loginUtil;

    public ArrayList<String> outcallcountries = null;
    String set_loginwithgmail="",set_call_rating="";

    SessionManagement session;
    SessionManagement_app_intro session_appintro;
    private String login_status="",app_intro="";
    String deviceToken="";
    private String device_info="",version_info="";
    private FirebaseAnalytics mFirebaseAnalytics;
    String useremail="NA";
    private Handler mHandler;
    String device_call_number="";
    Fragment dialer_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_new);
        if (LinphoneService.isReady()) {
            onServiceReady();
            Log.e(TAG,"onServiceReady");
        } else {
            // If it's not, let's start it
            try{startService(new Intent(this, LinphoneService.class));}catch (Exception e){}
            // And wait for it to be ready, so we can safely use it afterwards
            new ServiceWaitThread().start();
            Log.e(TAG,"startService");
        }

        try
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                Window window = getWindow();
                int flag = window.getDecorView().getSystemUiVisibility();
                flag |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                window.setStatusBarColor(getResources().getColor(R.color.white));
                window.getDecorView().setSystemUiVisibility(flag);
            }
        }
        catch (Exception e)
        {

        }
        mHandler = new Handler();
        sharedPreferences = getSharedPreferences(MY_PREFERANCES,MODE_PRIVATE);

        try
        {
            editor = sharedPreferences.edit();
            editor.putString("flag_dialer_firsttime","true");
            editor.commit();
        }
        catch (Exception e)
        {

        }

        try
        {
            // to clear number on dilaer input edittext
            editor = sharedPreferences.edit();
            editor.putString("typed_number_dialer","");
            editor.commit();
        }
        catch (Exception e)
        {

        }

        try
        {
            useremail=sharedPreferences.getString("user_emailid","");
            if(useremail.equalsIgnoreCase(""))
            {
                useremail="NA";
            }
        }
        catch (Exception e)
        {
            useremail="NA";
        }





        try
        {
            session = new SessionManagement(getApplicationContext());
            HashMap<String, String> user = session.getUserDetails();
            login_status = user.get(SessionManagement.KEY_STATUS);

            if(login_status==null)
            {
                login_status="";
            }


            session_appintro = new SessionManagement_app_intro(getApplicationContext());
            HashMap<String, String> intro = session_appintro.getUserDetails();
            app_intro=intro.get(SessionManagement_app_intro.KEY_APP_INTRO);

            if(app_intro==null)
            {
                app_intro="";
            }


            Log.e(TAG,"login_status :"+login_status +" app_intro: "+app_intro);
        }
        catch (Exception e)
        {
            Log.e(TAG,"Exception_session_management");
            login_status="";
            app_intro="";
        }

        try
        {
            getDeviceInformation();
        }
        catch (Exception e)
        {
            device_info="NA";
            version_info="NA";
        }



        try
        {
//            editor = sharedPreferences.edit();
//            editor.putString("set_call_rating", "on"); // for test version
//            editor.commit();




        }
        catch (Exception e)
        {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub


                    initi();

                }
            }, 0);

        }


//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                // TODO Auto-generated method stub
//
//
//                initi();
//
//            }
//        }, 3000);


        initi();
//        login();

        Bundle bundle1 = new Bundle();
        dialer_fragment = new Dialer_Fragment();
       // final FragmentManager DialerManager = getSupportFragmentManager();

        if(dialer_fragment==null)
        {
            Log.e(TAG,"DialerManager_null");
        }
        try{
            if(!device_call_number.equalsIgnoreCase(""))
            {
                bundle1.putString("device_call_number", device_call_number );
                editor = sharedPreferences.edit();
                editor.putString("device_call_number",device_call_number);
                editor.commit();
            }
            else
            {
                bundle1.putString("device_call_number", "" );
                editor = sharedPreferences.edit();
                editor.putString("device_call_number",device_call_number);
                editor.commit();
            }
            Log.e(TAG,"bundle________ "+bundle1);
            dialer_fragment.setArguments(bundle1);
        }catch (Exception e){
                Log.e(TAG,"exp_44 "+e.getMessage());
        }


    }
    private static final String[] projection={
//            ContactsContract.Data.DATA1,ContactsContract.Data.DATA2,ContactsContract.Data.DATA3
            ContactsContract.CommonDataKinds.Phone.NUMBER
    };

    private void onServiceReady() {
        // Once the service is ready, we can move on in the application
        // We'll forward the intent action, type and extras so it can be handled
        // by the next activity if needed, it's not the launcher job to do that
//        Intent intent = new Intent();
//        intent.setClass(Splash_newActivity.this, LoginActivity.class);
//        if (getIntent() != null && getIntent().getExtras() != null) {
//            intent.putExtras(getIntent().getExtras());
//        }
//        intent.setAction(getIntent().getAction());
//        intent.setType(getIntent().getType());
//        startActivity(intent);
    }

    // This thread will periodically check if the Service is ready, and then call onServiceReady
    private class ServiceWaitThread extends Thread {
        public void run() {
            while (!LinphoneService.isReady()) {
                try {
                    sleep(30);
                } catch (InterruptedException e) {
                    throw new RuntimeException("waiting thread sleep() has been interrupted");
                }
            }
            // As we're in a thread, we can't do UI stuff in it, must post a runnable in UI thread
            mHandler.post(
                    new Runnable() {
                        @Override
                        public void run() {
                            onServiceReady();
                        }
                    });
        }
    }



    private void initi()
    {





        login();



    }

    private void login()
    {


        if(db_login_credential_size>0 && !db_loginname.equalsIgnoreCase("") && !db_loginpass.equalsIgnoreCase(""))
        {
            Log.e(TAG,"into_autologin_yes");


            if(sharedPreferences.getString("isloginactive","").equals("true"))
            {

                Log.e(TAG,"into_autologin_prefernce : true");

                Intent intent = new Intent(Splash_newActivity.this, DialerActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_left);

            }
            else
            {
                Log.e(TAG,"into_autologin_preference : false");

                Intent intent = new Intent(Splash_newActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_left);

                // when local db for user login credential is not null but share prefernce is null do login again
//                auto_login_api();

            }

        }
        else if(sharedPreferences.getString("isloginactive","").equals("true"))
        {
            Log.e(TAG, "initializemainfunction: "+sharedPreferences.getString("isloginactive",""));

            Intent intent = new Intent(Splash_newActivity.this, DialerActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_left);
        }
//        else if(login_status.equalsIgnoreCase("is_Login_true") || session.isLoggedIn())
        else if(login_status.equalsIgnoreCase("is_Login_true"))
        {
            Log.e(TAG,"new_session_true");
            Intent intent = new Intent(Splash_newActivity.this, DialerActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_left);
        }
        else
        {
            Log.e(TAG,"Login_else");
            Intent intent = new Intent(Splash_newActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_left);
        }


    }






    private void getDeviceInformation() {

          /*String model = Build.MODEL + " " + android.os.Build.BRAND + " ("
                + android.os.Build.VERSION.RELEASE + ")"
                + " API-" + android.os.Build.VERSION.SDK_INT + " : " + Build.DEVICE
                + " : " + Build.ID + " : " + Build.MANUFACTURER + " : " + Build.BOARD
                + " : " + Build.SERIAL + " : " + Build.TYPE;*/

//        phoneModel = Build.MODEL;
//        phoneBrand = Build.BRAND + " (" + Build.VERSION.RELEASE + ")";
//        phoneManufacture = Build.MANUFACTURER;
//        phoneSerial = Build.SERIAL;
//        phoneOSVersion = Integer.toString(Build.VERSION.SDK_INT);
//        phoneDeviceID = Build.ID;

        device_info= Build.MODEL+"->"+ Build.BRAND + "(" + Build.VERSION.RELEASE + ")"+"->"+Build.VERSION.SDK_INT;
        version_info=BuildConfig.VERSION_NAME;

        Log.d("DeviceInfoMODEL", "--->" + Build.MODEL);
        Log.d("DeviceInfoBRAND", "--->" + Build.BRAND + " (" + Build.VERSION.RELEASE + ")");
        Log.d("DeviceInfoMANUFACTURER", "--->" + Build.MANUFACTURER);
//        Log.d("DeviceInfoSERIAL", "--->" + Build.SERIAL);
        Log.d("DeviceInfoSDK_INT", "--->" + Build.VERSION.SDK_INT +" "+ BuildConfig.VERSION_NAME);
//        Log.d("DeviceInfoDEVICE", "--->" + Build.DEVICE);

        Log.d("Device_device_info", "--->" + device_info);
        Log.d("Device_version_info", "--->" + version_info);


    }


}
