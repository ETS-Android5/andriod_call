package com.callhippo.bueno.callhippo;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import 	androidx.annotation.NonNull;
import	androidx.annotation.Nullable;
import 	androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.navigation.NavigationView;
import androidx.core.app.ActivityCompat;
import 	androidx.fragment.app.Fragment;
import 	androidx.fragment.app.FragmentManager;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import 	androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import 	androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.callhippo.bueno.callhippo.Utils.ActivityHelper;
import com.callhippo.bueno.callhippo.Utils.Constants;
//import com.callhippo.bueno.callhippo.Utils.ContactsDatabase;
import com.callhippo.bueno.callhippo.Utils.InternetConnectionManager;
import com.callhippo.bueno.callhippo.Utils.PermissionUtil;
import com.callhippo.bueno.callhippo.Utils.SessionManagement;
import com.callhippo.bueno.callhippo.Utils.SessionManagement_network_strength;
import com.callhippo.bueno.callhippo.Utils.SessionManagement_new_points;
import com.callhippo.bueno.callhippo.Utils.SessionManagement_nps;
import com.callhippo.bueno.callhippo.Utils.SmoothActionBarDrawerToggle;
import com.callhippo.bueno.callhippo.Utils.TinyDB;
import com.callhippo.bueno.callhippo.Utils.Util;
import com.callhippo.bueno.callhippo.fragment.Dialer_Fragment;
import com.callhippo.bueno.callhippo.model.MyContact;
import com.callhippo.bueno.callhippo.service.LinphoneService;
//import com.crashlytics.android.Crashlytics;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.wootric.androidsdk.Wootric;


//import org.linphone.core.AccountCreator;
//import org.linphone.core.Core;
//import org.linphone.core.CoreListenerStub;
//import org.linphone.core.ProxyConfig;
//import org.linphone.core.RegistrationState;
//import org.linphone.core.TransportType;

import org.linphone.core.AccountCreator;
import org.linphone.core.Core;
import org.linphone.core.CoreListenerStub;
import org.linphone.core.ProxyConfig;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;
//import io.fabric.sdk.android.Fabric;


/**
 * Created by Appitsimple11 on 24-03-2017.
 */

public class DialerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "DialerActivity";
    //Begin Permission intialize
    private static String[] PERMISSIONS_CONTACT = {Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS};
    private static final int REQUEST_CONTACTS = 1;
    private static final int REQUEST_RECORD_AUDIO = 2;
    private int lastselectedposition=0;
    private static SharedPreferences sharedPreferences;
    private final String MY_PREFERANCES = "callhippomaulik";
    private static SharedPreferences.Editor editor;
    TextView drawerUsername;
//    TextView toolbar_title;
    public static TextView toolbar_title;
    public static MaterialSearchView searchView;                     // Searchview for contacts fragment
    private static DrawerLayout drawer;
    Util dialerUtil;
    private static NavigationView navigationView;
    MenuItem MenuItem;                      // handle Menuitem Visibility
    boolean searchoption = false;            // for show a searchview in Contacts fragment not any fragment
    public static boolean actvitiyRunning = false;
    public static boolean actvitiybackground = false;
    public static InternetConnectionManager internetConnection;
    private static AppCompatActivity MyActivity;
   static  GoogleApiClient mGoogleApiClient;
 static    SessionManagement session;

    SessionManagement_new_points session_new_points;
    String updated_points="";
//    String appOnboard="false";
    private Dialog new_points,credit_dlog;
    private String billing_rights="";
    SessionManagement_network_strength session_network;

    private Button btn_addcredits;
    final CharSequence[] radio_credits = {"$200", "$100", "$50","$30"};
    AlertDialog alertdialog2;
    public static TextView txt_credits_display;
    private LinearLayout l_ac;
    private RadioButton radioButton;
    String credit_amount="";

    TextView drawerUserEmail,drawerUserLeaderbadge;
    ImageView drawerImagebadge;
    private FirebaseAnalytics mFirebaseAnalytics;
    String useremail="";
    String device_call_number="";

   static SessionManagement_nps session_nps;
    String s_nps_status="";
    String nps_show="";
    Date date_login = null;
    Date date_open = null;
    String appOnboard = "false";
//    public static MenuItem menu_sync;

    LinearLayout l_logout;


    TextView t_version;
    String app_vcode="";

    String TAG_FRAGMENT="FragmentTranscation";
    Fragment calllog_fragment;
    Fragment dialer_fragment;

    SmoothActionBarDrawerToggle mDrawerToggle;
    FragmentManager fragmentManager;
    String flag_for_idle="";


    private AccountCreator mAccountCreator;
    private CoreListenerStub mCoreListener;
    Boolean is_lp=false;
    ProxyConfig cfg;
    Core core;
//    private Handler mHandler;

    private Dialog rankDialog;

    String a="";

    private static final String NOTIFICATION_CHANNEL_ID2 = "my_notification_channel2";
    String x_to_tr_number="",x_from_tr_number="",x_from_tr_id="",x_ph_trid="",x_incoming_name="",x_department_name="",x_integration_name="",x_ph_network_str="",callNumber="",transferBy="",x_calltransfer="",x_ph_extensioncall="";
    String lastcall_type="",lastcall_fullName="",lastcall_date="",lastcall_status="";

    private static final String TAG_twilio = "twilio_login";

    public static Switch aSwitch;
    String smsRight = "true";
    public String last_call_ext="";
    public String app_first_open="false";

    public ArrayList<String> mNames= new ArrayList<>();
    public ArrayList<String> mNumbers=null ;
    public ArrayList<String> mNumbers_v1=new ArrayList<>() ;
    public ArrayList<String> Contact_id = new ArrayList<>();
    public ArrayList<String> rawcontactId = null;
    public ArrayList<MyContact> myContactsarralist = null;
    public static HashMap<String,String> rawcontactidMap;
    public static  String first="";
    private Handler mHandler;
    String telephonyprovider="";
    int page=0;
    String user_email_db="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHelper.initialize(this);
        setContentView(R.layout.activity_main_v2);

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

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        sharedPreferences = getSharedPreferences(MY_PREFERANCES, MODE_PRIVATE);






        try
        {
            String is_lp1=sharedPreferences.getString("is_lp","");

            if(is_lp1.equalsIgnoreCase("true"))
            {
                is_lp=true;
            }
            else if(is_lp1.equalsIgnoreCase("false"))
            {
                is_lp=false;
            }
            Log.e(TAG,"is_lp_flag "+is_lp);

            Log.e(TAG,"is_lp_flag "+is_lp);


        }catch (Exception e){}

        MyActivity = this;
        dialerUtil = new Util(this);
        actvitiyRunning = true;
        internetConnection = new InternetConnectionManager(this);
        session_network = new SessionManagement_network_strength(getApplicationContext());
        initi();
        try{mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);}catch (Exception e){}



        try {
            appOnboard = sharedPreferences.getString("appOnboard","false");
        } catch (Exception e) { }



        try{fragmentManager=getSupportFragmentManager();}catch (Exception e){}


        user_email_db = sharedPreferences.getString("useremaildb","");
        try {
            if (user_email_db==null){user_email_db="";}
        } catch (Exception e) {
            e.printStackTrace();
        }
        app_first_open = sharedPreferences.getString("firsttime_login","");
        Log.e(TAG,"app_first "+app_first_open+" useremaildb "+user_email_db+" useremail "+useremail);
        useremail=sharedPreferences.getString("user_emailid","");
        editor = sharedPreferences.edit();
        editor.putString("useremaildb",useremail);
        editor.commit();




        Log.e("DialerActivity", "FromNumber- " + sharedPreferences.getString("fromnumber", ""));
        Log.e("DialerActivity", "ContactName- " + sharedPreferences.getString("contactname", ""));


        Intent pendingIntent = getIntent();
        int position = pendingIntent.getIntExtra(Constants.SELECTED_POSITION, 0);
        if (position == 0) {
            selectedposition(position);
        } else if (position == 3) {

            try
            {
                editor = sharedPreferences.edit();
                editor.putString("flag_dialer_firsttime","true");
                editor.commit();

            }
            catch (Exception e)
            {

            }


            selectedposition(position);
            navigationView.getMenu().getItem(3).setChecked(true);
        }
        else if(position==1)
        {
            try
            {
                editor = sharedPreferences.edit();
                editor.putString("flag_dialer_firsttime","true");
                editor.commit();
            }
            catch (Exception e)
            {

            }


            selectedposition(position);
            navigationView.getMenu().getItem(1).setChecked(true);
        }
        else if(position==4)
        {
            try
            {
                editor = sharedPreferences.edit();
                editor.putString("flag_dialer_firsttime","true");
                editor.commit();

                selectedposition(6);
                navigationView.getMenu().getItem(6).setChecked(true);
            }
            catch (Exception e)
            {

            }
        }
        else if(position==10)
        {
            try
            {
                editor = sharedPreferences.edit();
                editor.putString("flag_dialer_firsttime","true");
                editor.commit();

                selectedposition(4);
                navigationView.getMenu().getItem(4).setChecked(true);
            }
            catch (Exception e){}
        }

        Log.e(TAG, "onCreate: " + position);

        mCoreListener = new CoreListenerStub() {
            @Override
            public void onCallStateChanged(Core core, org.linphone.core.Call call, org.linphone.core.Call.State state, String message) {
//                Toast.makeText(LinphoneService.this, message, Toast.LENGTH_SHORT).show();
                android.util.Log.e("LinphoneService_dp","call_state: "+state+" message: "+message);

                try
                {
                    if(state== org.linphone.core.Call.State.Error && message.equalsIgnoreCase("Request timeout"))
                    {

                        String uname="";
                    }

                }
                catch (Exception e)
                {

                }


                if (state == org.linphone.core.Call.State.IncomingReceived || call.getState()== org.linphone.core.Call.State.IncomingEarlyMedia) {
                    // For this sample we will automatically answer incoming calls
//                    android.util.Log.e("lp","mCoreListener_IncomingReceived ");
                    org.linphone.core.Call incoming_call=call;

//                    String x_to_tr_number="",x_from_tr_number="",x_from_tr_id="",x_ph_trid="",x_incoming_name="",x_department_name="",x_integration_name="",x_ph_network_str="",callNumber="",transferBy="",x_calltransfer="";
//                    String lastcall_type="",lastcall_fullName="",lastcall_date="",lastcall_status="";

                    try
                    {

                        android.util.Log.e("lastcall_type",""+lastcall_type);


//                        Log.e("","X-PH-From :"+x_incoming_name+" ");
                        android.util.Log.e("LinphoneService_dp","X-PH-From: "+x_incoming_name+" X-PH-Fromnumber: "+callNumber);

                        if(x_from_tr_id!=null)
                        {
                            x_ph_trid=x_from_tr_id;
                        }
                        else
                        {
                            x_ph_trid="";
                        }

                        if(x_ph_network_str==null)
                        {
                            x_ph_network_str="";
                        }



                    }
                    catch (Exception e)
                    {
                        x_ph_extensioncall="";x_to_tr_number="";x_from_tr_number="";x_from_tr_id="";x_ph_trid="";x_incoming_name="";x_department_name="";x_integration_name="";x_ph_network_str="";callNumber="";x_calltransfer="";
                    }

















                }
                else if (state == org.linphone.core.Call.State.Connected)
                {
                    android.util.Log.e("lp_dp","mCoreListener_Connected ");
                }
            }

//            @Override
//            public void onRegistrationStateChanged(Core lc, ProxyConfig cfg, RegistrationState cstate, String message)
//            {
//                super.onRegistrationStateChanged(lc, cfg, cstate, message);
//
//                android.util.Log.e("LinphoneService_dp","onRegistrationStateChanged: "+cstate);
//            }


        };



        app_first_open = sharedPreferences.getString("firsttime_login","");


    }








    private void initi()
    {
//        sharedPreferences = getSharedPreferences(MY_PREFERANCES, MODE_PRIVATE);
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
//        internetConnection = new InternetConnectionManager(this);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        aSwitch = findViewById(R.id.switch1);
        telephonyprovider =sharedPreferences.getString("telephonyprovider","");





        if(sharedPreferences.getString("ThinQaccess","").equalsIgnoreCase("true"))
        {
            aSwitch.setVisibility(View.VISIBLE);
        }
        else
        {
            aSwitch.setVisibility(View.GONE);
        }



        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View navHeaderView = navigationView.getHeaderView(0);
        drawerUsername = (TextView) navHeaderView.findViewById(R.id.drawerProfileName);
        drawerUserEmail = (TextView) navHeaderView.findViewById(R.id.drawerProfileEmail);
        drawerUserLeaderbadge = (TextView) navHeaderView.findViewById(R.id.drawerLeaderBadgetext);
        drawerImagebadge = (ImageView) navHeaderView.findViewById(R.id.drawer_image_badge);

        String profilename = sharedPreferences.getString("fullname", "");

        Log.e(TAG,"sms_right:"+smsRight);




        String badgeText = sharedPreferences.getString("gamification_text","");

        if (badgeText.equalsIgnoreCase("Beginner"))
        {
            drawerUserLeaderbadge.setText("Beginner");
            drawerImagebadge.setImageResource(R.drawable.ic_beginer);
        } else if (badgeText.equalsIgnoreCase("Sharpshooter"))
        {
            drawerUserLeaderbadge.setText("Sharpshooter");
            drawerImagebadge.setImageResource(R.drawable.ic_silver);
        }else if(badgeText.equalsIgnoreCase("Calling Master"))
        {
            drawerUserLeaderbadge.setText("Calling Master");
            drawerImagebadge.setImageResource(R.drawable.ic_gold);
        }else if (badgeText.equalsIgnoreCase("The King"))
        {
            drawerUserLeaderbadge.setText("The King");
            drawerImagebadge.setImageResource(R.drawable.ic_platinum);
        }else if (badgeText.equalsIgnoreCase("Ultimate Champion"))
        {
            drawerUserLeaderbadge.setText("Ultimate Champion");
            drawerImagebadge.setImageResource(R.drawable.ic_champion);
        }


        drawerUsername.setText(profilename);
        try
        {
            useremail=sharedPreferences.getString("user_emailid","");
            drawerUserEmail.setText(useremail);
        }
        catch (Exception e)
        {
            useremail="";
            Log.e(TAG,"Exception_useremail "+e.getMessage());
        }

        try
        {
            app_vcode= BuildConfig.VERSION_NAME;
        }
        catch (Exception e)
        {
            app_vcode="";
        }

        mDrawerToggle  = new SmoothActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();




        t_version=(TextView) findViewById(R.id.txt_version);
        try{t_version.setText("v "+app_vcode);}catch (Exception e){}

        l_logout=(LinearLayout) findViewById(R.id.l_logout);
        l_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                doLogout();
            }
        });





        try
        {
            session = new SessionManagement(getApplicationContext());
        }
        catch (Exception e)
        {

        }



    }





    public void selectedposition(int position) {
        switch (position) {
            case 0:
                if (MenuItem != null) {
                    MenuItem.setVisible(false);
                }
//                if (menu_sync != null) {
//                    menu_sync.setVisible(false);
//                }
                lastselectedposition = 0;
                searchoption = false;
//                toolbar_title.setText("Dialpad");
                toolbar_title.setText("");
                Bundle bundle = new Bundle();
                searchView.setVisibility(View.GONE);
                if(sharedPreferences.getString("ThinQaccess","").equalsIgnoreCase("true"))
                {
                    aSwitch.setVisibility(View.VISIBLE);
                }
//                aSwitch.setVisibility(View.VISIBLE);

                try
                {
                    Bundle bundle1 = new Bundle();
                    mFirebaseAnalytics.logEvent("ch_menu_dialer", bundle1);
                }
                catch (Exception e)
                {

                }

                dialer_fragment = new Dialer_Fragment();
                final FragmentManager DialerManager = getSupportFragmentManager();

                if(dialer_fragment==null)
                {
                    Log.e(TAG,"DialerManager_null");
                }

                try
                {

                    if(!device_call_number.equalsIgnoreCase(""))
                    {
                        bundle.putString("device_call_number", device_call_number );
                    }
                    else
                    {
                        bundle.putString("device_call_number", "" );
                    }
                    dialer_fragment.setArguments(bundle);
                }
                catch (Exception e)
                {

                }


                try
                {
                    flag_for_idle=sharedPreferences.getString("flag_dialer_firsttime","");
                }
                catch (Exception e)
                {
                    flag_for_idle="";
                }

                Log.e(TAG,"flag_dialer_firsttime "+flag_for_idle);
                try
                {
                    if(flag_for_idle.equalsIgnoreCase("true"))
                    {
                        editor = sharedPreferences.edit();
                        editor.putString("flag_dialer_firsttime","false");
                        editor.commit();

                        DialerManager.beginTransaction().replace(R.id.callhippo_frame, dialer_fragment,TAG_FRAGMENT ).commit();
                    }
                    else
                    {

                        mDrawerToggle.runWhenIdle(new Runnable() {
                            @Override
                            public void run() {

                                try
                                {
                                    Fragment fragment_new = fragmentManager.findFragmentByTag(TAG_FRAGMENT);
                                    ConstraintLayout constraintLayout = fragment_new.getView().findViewById(R.id.constraintLayout);
                                    constraintLayout.setVisibility(View.GONE);
                                }
                                catch (Exception e){}

                                DialerManager.beginTransaction().replace(R.id.callhippo_frame, dialer_fragment, TAG_FRAGMENT).commit();


                            }
                        });

                    }
                }
                catch (Exception e)
                {
                    DialerManager.beginTransaction().replace(R.id.callhippo_frame, dialer_fragment, TAG_FRAGMENT).commit();
                }

//                DialerManager.beginTransaction().replace(R.id.callhippo_frame, dialer_fragment, Constants.FRAGMENT_TAG_DIALER).commit();
                break;
            case 1:
                if (MenuItem != null) {
                    MenuItem.setVisible(false);
                }
//                if (menu_sync != null) {
//                    menu_sync.setVisible(false);
//                }
                aSwitch.setVisibility(View.GONE);
                lastselectedposition = 1;
                searchoption = false;
                toolbar_title.setText("All Calls/Inbox");


                break;
            case 2:
                if (MenuItem != null) {
                    MenuItem.setVisible(true);
                }
//                if (menu_sync != null) {
//                    menu_sync.setVisible(true);
//                }

                lastselectedposition = 2;
                searchoption = true;
                toolbar_title.setText("Contacts");
                searchView.setVisibility(View.VISIBLE);
                aSwitch.setVisibility(View.GONE);

                try
                {
                    Bundle bundle3 = new Bundle();
                    mFirebaseAnalytics.logEvent("ch_menu_contact", bundle3);
                }
                catch (Exception e)
                {

                }

//                final Fragment contact_fragment = new Contacts_Fragment();


                break;
            case 3:
                if (MenuItem != null) {
                    MenuItem.setVisible(false);
                }
//                if (menu_sync != null) {
//                    menu_sync.setVisible(false);
//                }



                break;

            case 4:
                if (MenuItem != null) {
                    MenuItem.setVisible(false);
                }

                break;

            case 5:


                break;
            case 6:
                if (MenuItem != null) {
                    MenuItem.setVisible(false);
                }

                searchoption = false;
//                toolbar_title.setText("Call Reminder");
                searchView.setVisibility(View.GONE);
                aSwitch.setVisibility(View.GONE);


                break;
            case 7:
                if (MenuItem != null) {
                    MenuItem.setVisible(false);
                }
                lastselectedposition = 5;
                searchoption = false;



                break;

            case 8 :

                if (MenuItem != null) {
                    MenuItem.setVisible(false);
                }

                break;

            default:
                break;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,@NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_RECORD_AUDIO) {
            // BEGIN_INCLUDE(permission_result)
            // Received permission result for recording audio permission.
            Log.i(TAG, "Received response for Recording Audio permission request.");

            // Check if the only required permission has been granted
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Recording Audio permission has been granted, preview can be displayed
                Log.i(TAG, "Recording Audio permission has now been granted. Showing preview.");
                permissionAccessDialog(getResources().getString(R.string.permision_available_call),"Not");

            } else {
                Log.i(TAG, "Recording Audio permission was NOT granted.");

                permissionAccessDialog(getResources().getString(R.string.permissions_audio_not_granted),"Not");
            }
            // END_INCLUDE(permission_result)

        } else if (requestCode == REQUEST_CONTACTS) {
            Log.i(TAG, "Received response for contact permissions request.");

            // We have requested multiple permissions for contacts, so all of them need to be
            // checked.
            if (PermissionUtil.verifyPermissions(grantResults)) {
                // All required permissions have been granted, display contacts fragment.

                permissionAccessDialog(getResources().getString(R.string.permision_available_contacts),"Not");
                editor = sharedPreferences.edit();
                editor.putBoolean(Constants.SH_IS_CONTACT_PERMISSION, true);
                editor.commit();
            } else {
                Log.i(TAG, "Contacts permissions were NOT granted.");
                permissionAccessDialog(getResources().getString(R.string.permissions_not_granted),"Not");
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }



    private static void requestContactsPermissions() {
        // BEGIN_INCLUDE(contacts_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(MyActivity,
                Manifest.permission.READ_CONTACTS)
                || ActivityCompat.shouldShowRequestPermissionRationale(MyActivity,
                Manifest.permission.WRITE_CONTACTS)) {

            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example, if the request has been denied previously.
            Log.i(TAG,
                    "Displaying contacts permission rationale to provide additional context.");

            // Display a Dialog with an explanation and a button to trigger the request.
            try
            {
                permissionAccessDialog(MyActivity.getResources().getString(R.string.permission_contacts_rationale),"contact");
            }
            catch (Exception e)
            {
                Log.e(TAG,"Exception_contact_permission "+e.getMessage());
            }


        } else {
            // Contact permissions have not been granted yet. Request them directly.
            ActivityCompat.requestPermissions(MyActivity, PERMISSIONS_CONTACT, REQUEST_CONTACTS);
        }
        // END_INCLUDE(contacts_permission_request)
    }


    public static void requestAudioPermission(){
        Log.i(TAG, "Audio permission has NOT been granted. Requesting permission.");

        // BEGIN_INCLUDE(audio_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(MyActivity,
                Manifest.permission.RECORD_AUDIO)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            Log.i(TAG,
                    "Displaying Audio permission rationale to provide additional context.");

            permissionAccessDialog(MyActivity.getResources().getString(R.string.permission_camera_rationale),"audio");
        } else {

            // Audio permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(MyActivity, new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_RECORD_AUDIO);
        }
        // END_INCLUDE(Audio_permission_request)
    }




    public void doLogout() {




    }




    @Override
    protected void onResume() {
        super.onResume();
        Log.e("DialerActivity", "onResume: Called");
        actvitiybackground = false;
        Log.e("DialerActivity", "onResume: " + sharedPreferences.getString("logfromnumber", ""));
        Log.e("DialerActivity", "onResume: " + sharedPreferences.getString("logfromname", ""));

         last_call_ext = sharedPreferences.getString("last_call_ext","");
        Log.e(TAG, "outgoing_number_reminder: " + sharedPreferences.getString("outgoing_number_reminder", ""));
        Log.e(TAG, "CallReminder_status: " + sharedPreferences.getString("CallReminder_status", ""));
        Log.e(TAG,"last_call_ext "+last_call_ext);


        try
        {
            mHandler = new Handler();
            if (LinphoneService.isReady()) {
                core = LinphoneService.getCore();
                core.addListener(mCoreListener);
                Log.e(TAG,"onServiceReady");
            } else {
                // If it's not, let's start it
                startService(new Intent(this, LinphoneService.class));
                // And wait for it to be ready, so we can safely use it afterwards
                new ServiceWaitThread().start();
                Log.e(TAG,"startService");
            }
        }
        catch (Exception e){Log.e(TAG,"Exception_core "+e.getMessage());}


        try
        {
            if(sharedPreferences.getString("addcredit_page","").equalsIgnoreCase("true"))
            {
                editor = sharedPreferences.edit();
                editor.putString("addcredit_page", "false");
                editor.commit();

                navigationView.getMenu().getItem(0).setChecked(true);
                selectedposition(0);

            }
        }
        catch (Exception e)
        {

        }

        try
        {
            editor = sharedPreferences.edit();
            editor.putString("addcredit_page", "false");
            editor.commit();
        }
        catch (Exception e)
        {

        }

        try
        {
            if(sharedPreferences.getString("call_reminder_page","").equalsIgnoreCase("true"))
            {
                editor = sharedPreferences.edit();
                editor.putString("call_reminder_page", "false");
                editor.commit();

                navigationView.getMenu().getItem(0).setChecked(true);
                selectedposition(0);

            }
        }
        catch (Exception e)
        {

        }

        try
        {
            editor = sharedPreferences.edit();
            editor.putString("call_reminder_page", "false");
            editor.commit();
        }
        catch (Exception e)
        {

        }

        try
        {
            if(sharedPreferences.getString("Settings_screen","").equalsIgnoreCase("true"))
            {
                editor = sharedPreferences.edit();
                editor.putString("Settings_screen", "false");
                editor.commit();

                navigationView.getMenu().getItem(0).setChecked(true);
                selectedposition(0);

            }
        }
        catch (Exception e)
        {

        }

        try
        {
            editor = sharedPreferences.edit();
            editor.putString("Settings_screen", "false");
            editor.commit();
        }
        catch (Exception e)
        {

        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("DialerActivity", "onPause: Called");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("DialerActivity", "onRestart: Called");
    }

    @Override
    protected void onStop() {
        actvitiybackground = true;
        try{rankDialog.dismiss();}catch (Exception e){}
        super.onStop();
        Log.e("DialerActivity", "onStop: isLogin: " + sharedPreferences.getString("isloginactive", ""));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("DialerActivity", "onDestroy: Called- " + sharedPreferences.getBoolean("isItreceive", false));
        actvitiyRunning = false;
        if(is_lp)
        {
            if(mCoreListener!=null)
            {
                LinphoneService.getCore().removeListener(mCoreListener);
            }

        }

        try
        {
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy G 'at' HH:mm:ss z");
            String current_time = df.format(Calendar.getInstance().getTime());

            Bundle bundle8 = new Bundle();
            bundle8.putString("app_close_time",current_time+" "+useremail);
            mFirebaseAnalytics.logEvent("ch_close", bundle8);
        }
        catch (Exception e)
        {

        }

        try
        {
            editor =sharedPreferences.edit();
            editor.putString("outgoing_number_reminder","");
            editor.putString("networkStrengthRandomString_reminder","");
            editor.commit();
        }
        catch (Exception e)
        {

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem = menu.findItem(R.id.action_search);
//        menu_sync = menu.findItem(R.id.action_sync);
        searchView.setMenuItem(MenuItem);
        if (searchoption) {
            MenuItem.setVisible(true);
//            menu_sync.setVisible(true);
        } else {
            MenuItem.setVisible(false);
//            menu_sync.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();
       drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        if (id == R.id.nav_dialer) {
            selectedposition(0);
        } else if (id == R.id.nav_calllog) {
            selectedposition(1);
        } else if (id == R.id.nav_contacts) {
            selectedposition(2);
        } else if (id == R.id.nav_message) {
            selectedposition(3);
        }
        else if(id==R.id.nav_credits)
        {
            selectedposition(4);
        }
        else if(id==R.id.nav_feedback)
        {
            selectedposition(5);
        }
        else if (id == R.id.nav_logout) {

            doLogout();
        }
        else if(id == R.id.nav_call_reminder)
        {
            selectedposition(6);
        }
        else if(id == R.id.nav_setting)
        {
            selectedposition(7);
        }
        else if (id == R.id.nav_leader_board){
            selectedposition(8);
        }
        return true;
    }

    public static MaterialSearchView getsearchview() {
        return searchView;
    }




    @Override
    public void onBackPressed() {
            if (searchView.isSearchOpen())
            {
                    Log.e(TAG,"backpressed");
                searchView.closeSearch();
            }
            else
            {

                try {
                    DrawerLayout drawer = findViewById(R.id.drawer_layout);


                    device_call_number = "";

                    try {
                        editor = sharedPreferences.edit();
                        editor.putString("typed_number_dialer", "");
                        editor.commit();
                    } catch (Exception e) {

                    }

                    try {
                        editor = sharedPreferences.edit();
                        editor.putString("flag_dialer_firsttime", "true");
                        editor.commit();
                    } catch (Exception e) {

                    }


                    Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);
                    if (fragment != dialer_fragment) {
                        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                        Log.e(TAG, "onBackPressed: remove All Calls");
                        navigationView.getMenu().getItem(0).setChecked(true);
                        selectedposition(0);

                    } else {
                        if (dialer_fragment != null) {
                            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                                getSupportFragmentManager().popBackStack();
                            } else {
                                this.finish();
                            }
                        } else {
                            try {
                                navigationView.getMenu().getItem(0).setChecked(true);
                                selectedposition(0);
                            } catch (Exception e) {
                            }

                        }
                    }

                    if (drawer.isDrawerOpen(GravityCompat.START)) {
                        drawer.closeDrawer(GravityCompat.START);
                    }

                } catch (Exception e) {
                    Log.e(TAG, "excp_bck:" + e.getMessage());
                }
            }
    }


    public static void permissionAccessDialog(String errorMessage, final String permissionname){

        final AlertDialog alertDialog = new AlertDialog.Builder(MyActivity).create();
        LayoutInflater inflater = MyActivity.getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.permission_dialog, null);
        TextView textError = (TextView) alertLayout.findViewById(R.id.textpermissionmessage);
        Button btnAcceptPermission = (Button)alertLayout.findViewById(R.id.btnAccepOk);
        alertDialog.setView(alertLayout);
        textError.setText(errorMessage);
        btnAcceptPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(permissionname.equals("contact")){
                    ActivityCompat
                            .requestPermissions(MyActivity, PERMISSIONS_CONTACT,
                                    REQUEST_CONTACTS);
                }else if(permissionname.equals("audio")){
                    ActivityCompat.requestPermissions(MyActivity, new String[]{Manifest.permission.RECORD_AUDIO},
                            REQUEST_RECORD_AUDIO);
                }else{

                }
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }



    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }











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
//                            onServiceReady();
                            core = LinphoneService.getCore();
                            core.addListener(mCoreListener);
                        }
                    });
        }
    }
















}