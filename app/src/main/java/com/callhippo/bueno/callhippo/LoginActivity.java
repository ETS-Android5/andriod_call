package com.callhippo.bueno.callhippo;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
//import android.support.annotation.NonNull;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.callhippo.bueno.callhippo.Utils.ActivityHelper;
//import com.callhippo.bueno.callhippo.Utils.ContactsDatabase;
import com.callhippo.bueno.callhippo.Utils.InternetConnectionManager;
import com.callhippo.bueno.callhippo.Utils.SessionManagement;
import com.callhippo.bueno.callhippo.Utils.SessionManagement_ParentID;
import com.callhippo.bueno.callhippo.Utils.SessionManagement_app_intro;
import com.callhippo.bueno.callhippo.Utils.SessionManagement_nps;
import com.callhippo.bueno.callhippo.Utils.Util;
import com.callhippo.bueno.callhippo.service.LinphoneService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.safetynet.SafetyNet;
//import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.linphone.core.AccountCreator;
import org.linphone.core.Core;
import org.linphone.core.CoreListenerStub;
import org.linphone.core.ProxyConfig;
import org.linphone.core.RegistrationState;
import org.linphone.core.TransportType;
//import org.linphone.core.AccountCreator;
//import org.linphone.core.Core;
//import org.linphone.core.CoreListenerStub;
//import org.linphone.core.ProxyConfig;
//import org.linphone.core.RegistrationState;
//import org.linphone.core.TransportType;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LoginActivity extends AppCompatActivity implements EventListener,GoogleApiClient.OnConnectionFailedListener {
    // login change
    private static final String TAG = "LoginActivity";
    Button btn_login,btn_verify;
    EditText editfullName, editTextPasswod,editenterOtp,ed_uname,ed_pwd,ed_domain;
    TextView textTest;
    String sip_uname="",sip_pwd="",sip_domain="",protocol="";
    Button bt_tcp,bt_udp;
    public static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 123;
    public static final int MY_PERMISSIONS_READ_EXTERNAL_STORAGE = 124;
    public static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 125;
    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 127;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_CONTACTS = 128;
    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor editor;
    private final String  MY_PREFERANCES = "callhippomaulik";
    Util loginUtil;
    InternetConnectionManager internetConnection;
    String deviceToken;
    public ArrayList<String> outcallcountries = null; private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 007;
    private String login_email_google="";
    private String db_loginname="",db_loginpass="";
    private int db_loginID=0;
    private int db_login_credential_size=0;
    private Button btn_signup;
    private TextView tx_signup;
    String is_login_gmail_api="";
    SessionManagement session;
    SessionManagement_app_intro session_appintro;
    SessionManagement_ParentID session_parentid;
    private String device_info="",version_info="",iosdevice_info="";
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    private FirebaseAnalytics mFirebaseAnalytics;

    SessionManagement_nps sessio_nps;
//    ContactsDatabase db_contacts;

    private AccountCreator mAccountCreator;
    private CoreListenerStub mCoreListener;

    Boolean is_lp=false;
    ProxyConfig cfg;
    String lp_protocol="";
    String lp_domain="";
    private Handler mHandler;
    String login_google_uniqid="";

    LinearLayout l_reset_pwd,l_signin,l_otp;
    EditText ed_email_reset;
    Button btn_reset_password;
    public ArrayList<ActiveSubUsers> extensionNumber=null;
    TextView facts_view;
    String lp_uname_fs="",lp_pass_fs="",lp_domain_fs="",lp_transport_fs="";
    LinearLayout l_bottom;
    Boolean isUser2FA_on=false;
    String email="";
    TextView resendOtp;
    RadioGroup rdg;
    RadioButton r_tcp,r_udp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHelper.initialize(this);
        setContentView(R.layout.activity_login_n_v4);
        mHandler = new Handler();

        try
        {
//            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        }
        catch (Exception e)
        {

        }
        new ServiceWaitThread().start();



//        try
//        {
//            db_contacts = new ContactsDatabase(this);
//            db_contacts.deleteAllItems();
//        }
//        catch (Exception e)
//        {
//
//        }


        sharedPreferences = getSharedPreferences(MY_PREFERANCES,MODE_PRIVATE);
        try
        {
            session = new SessionManagement(getApplicationContext());
            session_appintro = new SessionManagement_app_intro(getApplicationContext());
            session_appintro.createIntroSession("app_intro_true");

            session_parentid = new SessionManagement_ParentID(getApplicationContext());
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








//        if(sharedPreferences.getString(Constants.APP_INTRO,"").equals("")){
//            Log.e(TAG, "onCreate: APPIntro- "+sharedPreferences.getString(Constants.APP_INTRO,"") );
//            Intent intent = new Intent(LoginActivity.this, IntroActivity.class);
//            startActivity(intent);
//            finish();
//        }else{
//            Log.e(TAG, "onCreate: APPIntro- "+sharedPreferences.getString(Constants.APP_INTRO,"") );
//        }
        loginUtil = new Util(this);
        Log.e(TAG, "onCreate: "+getIntent().getExtras() );
        Log.e(TAG, "onCreate: "+ FirebaseMessaging.getInstance().toString());

        internetConnection = new InternetConnectionManager(this);
        Log.e(TAG, ""+sharedPreferences.getString("isloginactive",""));

        btn_login = (Button) findViewById(R.id.btn_login_new);
        editfullName = (EditText) findViewById(R.id.txtfullname);
        editTextPasswod = (EditText) findViewById(R.id.txtpassword);
        textTest = (TextView) findViewById(R.id.textTest);
        editenterOtp = findViewById(R.id.txtenterOtp);
        btn_verify= findViewById(R.id.btn_verify);

        ed_uname=findViewById(R.id.ed_sip_uname);
        sip_uname=ed_uname.getText().toString();

        ed_pwd=findViewById(R.id.ed_sip_pwd);
        sip_pwd=ed_pwd.getText().toString();

        ed_domain=findViewById(R.id.ed_sip_domain);
        sip_domain=ed_domain.getText().toString();


        rdg=findViewById(R.id.rd_group);
        r_tcp=findViewById(R.id.rd_tcp);
        r_udp=findViewById(R.id.rd_udp);








        try
        {
            getDeviceInformation();
        }
        catch (Exception e)
        {
            device_info="NA";
            version_info="NA";
            iosdevice_info="NA";
        }

        try
        {
            if(Build.VERSION.SDK_INT >= 23)
            {
                askPermissions();
            }
        }
        catch (Exception e)
        {

        }




        mCoreListener = new CoreListenerStub() {
            @Override
            public void onCallStateChanged(Core core, org.linphone.core.Call call, org.linphone.core.Call.State state, String message) {
//                Toast.makeText(LinphoneService.this, message, Toast.LENGTH_SHORT).show();
//                android.util.Log.e("LinphoneService","call_state: "+state+" message: "+message);

            }

            @Override
            public void onRegistrationStateChanged(Core lc, ProxyConfig cfg, RegistrationState cstate, String message) {
                super.onRegistrationStateChanged(lc, cfg, cstate, message);

                android.util.Log.e(TAG,"onRegistrationStateChanged: "+cstate);
                btn_login.setEnabled(true);

                if(cstate== RegistrationState.Ok)
                {
                    Intent intent = new Intent(LoginActivity.this, DialerActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(0,0);

                    try
                    {
                        if(!DialerActivity.actvitiyRunning)
                        {
//                            Intent intent = new Intent(LoginActivity.this, DialerActivity.class);
//                            startActivity(intent);
//                            finish();
//                            overridePendingTransition(0,0);

                        }
                    }
                    catch (Exception e)
                    {
//                        Intent intent = new Intent(LoginActivity.this, DialerActivity.class);
//                        startActivity(intent);
//                        finish();
//                        overridePendingTransition(0,0);
                    }


                }
            }


        };

        l_reset_pwd=findViewById(R.id.l_reset_pwd);
        l_signin=findViewById(R.id.l_sign_in);
        l_otp=findViewById(R.id.l_otp);
        ed_email_reset=findViewById(R.id.edtfullname_fpwd);
        btn_reset_password = findViewById(R.id.btn_resetpass);





        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                login_endpoints_new(0,"9879879870","9879879870","137.117.92.197",lp_transport_fs);
                try
                {
                    editor = sharedPreferences.edit();
                    editor.putString("flag_dialer_firsttime","true");
                    editor.commit();
                }
                catch (Exception e)
                {

                }


                String userLoginType="callhippo";
                v.setEnabled(false);
                if(internetConnection.isConnectingToInternet()){
                        String new_fullname = editfullName.getText().toString();
                        String new_password = editTextPasswod.getText().toString();



                        Log.e("LoginActivity", "Login Token :" +sharedPreferences.getString("token",""));
//                        Log.e(TAG,"device_token "+deviceToken);
//                        if(deviceToken==null)
//                        {
//                            deviceToken=sharedPreferences.getString("token","");
//                        }

                        deviceToken = FirebaseInstanceId.getInstance().getToken();
                        editor = sharedPreferences.edit();
                        editor.putString("token",""+deviceToken);
                        editor.commit();

                    Log.e(TAG,"device_token "+deviceToken);

                        if(deviceToken==null)
                        {
                        }
                        else if(deviceToken.equalsIgnoreCase(""))
                        {
                            deviceToken = FirebaseInstanceId.getInstance().getToken();
                        }


                        if(r_tcp.isSelected())
                        {
                            protocol="TCP";
                        }
                        else
                        {
                            protocol="UDP";
                        }

                        if(protocol==null || protocol.equalsIgnoreCase(""))
                        {
                            protocol="TCP";
                        }

                        editor = sharedPreferences.edit();
                        editor.putString("freeswitchuname", sip_uname);
                        editor.putString("freeswitchpass", sip_pwd);
                        editor.putString("freeswitchdom", sip_domain);
                        editor.putString("freeswitchtrans", protocol);
                        editor.commit();


                        login_endpoints(0,sip_uname,sip_pwd,sip_domain,protocol);

//                      final Login_Request login_request = new Login_Request(new_fullname,new_password,"android",deviceToken);
//                        final Login_Request_Google login_request = new Login_Request_Google(new_fullname,new_password,"android",deviceToken,"callhippo",device_info,version_info);

                    }

                    v.setEnabled(true);
            }
        });


    }

    private void lp_login2() {
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        Log.e(TAG,"requestCode: "+requestCode+":"+resultCode);
        try
        {
            if (requestCode == RC_SIGN_IN) {
                Log.e(TAG,"requestCode_if");

            }
            else
            {
                Log.e(TAG,"requestCode_else");
//                try
//                {
//                    mGoogleApiClient.stopAutoManage(LoginActivity.this);
//                    mGoogleApiClient.disconnect();
//                }
//                catch (Exception e)
//                {
//                    Log.e(TAG,"excep_gdisconnect "+e.getMessage());
//                }
            }
        }
        catch (Exception e)
        {

        }

    }


    @Override
    public void onStart() {
        super.onStart();


    }




    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);

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

        device_info= Build.MODEL+"->"+ Build.BRAND + "(" + Build.VERSION.RELEASE + ")"+"->"+Build.VERSION.SDK_INT ;
        version_info=BuildConfig.VERSION_NAME;
        iosdevice_info= String.valueOf(Build.VERSION.SDK_INT);

        Log.d("DeviceInfoMODEL", "--->" + Build.MODEL);
        Log.d("DeviceInfoBRAND", "--->" + Build.BRAND + " (" + Build.VERSION.RELEASE + ")");
//        Log.d("DeviceInfoMANUFACTURER", "--->" + Build.MANUFACTURER);
//        Log.d("DeviceInfoSERIAL", "--->" + Build.SERIAL);
        Log.d("DeviceInfoSDK_INT", "--->" + Build.VERSION.SDK_INT +" "+ BuildConfig.VERSION_NAME);
//        Log.d("DeviceInfoDEVICE", "--->" + Build.DEVICE);

        Log.d("Device_device_info", "--->" + device_info);
        Log.d("Device_version_info", "--->" + version_info);


    }


    private boolean askPermissions() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.READ_CONTACTS))           permissionsNeeded.add("Read Contacts");
        if (!addPermission(permissionsList, Manifest.permission.WRITE_CONTACTS))           permissionsNeeded.add("write Contacts");
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE)) permissionsNeeded.add("Write logs to sd card");
        if (!addPermission(permissionsList, Manifest.permission.RECORD_AUDIO)) permissionsNeeded.add("Audio record");


        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                //String message = "You need to grant access to " + permissionsNeeded.get(0);
                //for (int i = 1; i < permissionsNeeded.size(); i++) message = message + ", " + permissionsNeeded.get(i);


                ActivityCompat.requestPermissions(this,
                        permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);

                return false;
            }

            ActivityCompat.requestPermissions(this,
                    permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return false;
        }

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try
        {
            if(is_lp)
            {
          //      LinphoneService.getCore().removeListener(mCoreListener);
            }
        }
        catch (Exception e)
        {

        }


    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission))     return false;
        }


        return true;
    }
    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        try
        {
            switch (requestCode)
            {
                case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
                {
                    Map<String, Integer> perms = new HashMap<String, Integer>();
                    //Initial
                    perms.put(Manifest.permission.READ_CONTACTS,           PackageManager.PERMISSION_GRANTED);
                    perms.put(Manifest.permission.WRITE_CONTACTS,           PackageManager.PERMISSION_GRANTED);
                    perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                    perms.put(Manifest.permission.RECORD_AUDIO, PackageManager.PERMISSION_GRANTED);

                    //Fill with results
                    for (int i = 0; i < permissions.length; i++) perms.put(permissions[i], grantResults[i]);

                    //Check for ACCESS_FINE_LOCATION
                    if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            ) {
                        // All Permissions Granted
                    } else {
                        // Permission Denied
//                    Toast.makeText(LoginActivity.this, "Some permissions were denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
                default:
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
        catch (Exception e)
        {

        }

    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

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
                            onServiceReady();
                        }
                    });
        }
    }


//    private void onServiceReady() {
//        // Once the service is ready, we can move on in the application
//        // We'll forward the intent action, type and extras so it can be handled
//        // by the next activity if needed, it's not the launcher job to do that
////        Intent intent = new Intent();
//
//        mAccountCreator = LinphoneService.getCore().createAccountCreator(null);
//        if(mAccountCreator!=null)
//        {
//            mAccountCreator.setUsername(sharedPreferences.getString("username", ""));
//            mAccountCreator.setDomain(lp_domain);
//            mAccountCreator.setPassword(sharedPreferences.getString("password", ""));
////                                        mAccountCreator.setTransport(TransportType.Tcp);
//
//            if(lp_protocol.equalsIgnoreCase("TLS"))
//            {
//                mAccountCreator.setTransport(TransportType.Tls);
//            }
//            else if(lp_protocol.equalsIgnoreCase("TCP"))
//            {
//                mAccountCreator.setTransport(TransportType.Tcp);
//            }
//            else if(lp_protocol.equalsIgnoreCase("UDP"))
//            {
//                mAccountCreator.setTransport(TransportType.Udp);
//            }
//            else
//            {
//                mAccountCreator.setTransport(TransportType.Tcp);
//            }
//
//
//            // This will automatically create the proxy config and auth info and add them to the Core
////            cfg = mAccountCreator.configure();
//            cfg = mAccountCreator.createProxyConfig();
//            // Make sure the newly created one is the default
//            LinphoneService.getCore().setDefaultProxyConfig(cfg);
////            LinphoneService.getCore().addListener(mCoreListener);
//
////            try{LinphoneService.getInstance().socket();}catch (Exception e){}
//        }
//
//
//    }
private void onServiceReady() {
    // Once the service is ready, we can move on in the application
    // We'll forward the intent action, type and extras so it can be handled
    // by the next activity if needed, it's not the launcher job to do that
//        Intent intent = new Intent();

    Log.e(TAG,"service_ready");
    mAccountCreator = LinphoneService.getCore().createAccountCreator(null);
    if(mAccountCreator!=null)
    {
        mAccountCreator.setUsername(lp_uname_fs);
        mAccountCreator.setDomain(lp_domain);
        mAccountCreator.setPassword(lp_pass_fs);
        mAccountCreator.setTransport(TransportType.Tcp);

//        if(lp_transport_fs.equalsIgnoreCase("TLS"))
//        {
//            mAccountCreator.setTransport(TransportType.Tls);
//        }
//        else if(lp_transport_fs.equalsIgnoreCase("TCP"))
//        {
//            mAccountCreator.setTransport(TransportType.Tcp);
//        }
//        else if(lp_transport_fs.equalsIgnoreCase("UDP"))
//        {
//            mAccountCreator.setTransport(TransportType.Udp);
//        }
//        else
//        {
//            mAccountCreator.setTransport(TransportType.Tcp);
//        }


        // This will automatically create the proxy config and auth info and add them to the Core
//            cfg = mAccountCreator.configure();
        cfg = mAccountCreator.createProxyConfig();
        // Make sure the newly created one is the default
        LinphoneService.getCore().setDefaultProxyConfig(cfg);
            LinphoneService.getCore().addListener(mCoreListener);

//            try{LinphoneService.getInstance().socket();}catch (Exception e){}
    }





}

    public void login_endpoints(int i,String uname,String pass,String domain,String transport_type)
    {
        mAccountCreator = LinphoneService.getCore().createAccountCreator(null);


        Log.e(TAG,"login_endpoint");
        uname="5e0ec6c84a7d363d5f5f961b-android";
        pass="5e0ec6c84a7d363d5f5f961b";
        domain="sip.callhippo.com";

        if(mAccountCreator!=null)
        {
            mAccountCreator.setUsername(uname);
            mAccountCreator.setDomain(domain);
            mAccountCreator.setPassword(pass);
            mAccountCreator.setTransport(TransportType.Tcp);

//            if(transport_type.equalsIgnoreCase("TLS"))
//            {
//                mAccountCreator.setTransport(TransportType.Tls);
//            }
//            else if(transport_type.equalsIgnoreCase("TCP"))
//            {
//                mAccountCreator.setTransport(TransportType.Tcp);
//            }
//            else if(transport_type.equalsIgnoreCase("UDP"))
//            {
//                mAccountCreator.setTransport(TransportType.Udp);
//            }
//            else
//            {
//                mAccountCreator.setTransport(TransportType.Tcp);
//            }


            // This will automatically create the proxy config and auth info and add them to the Core
//            cfg = mAccountCreator.configure();
            cfg = mAccountCreator.createProxyConfig();
//            cfg.setServerAddr("3.230.166.211");
            // Make sure the newly created one is the default
            LinphoneService.getCore().setDefaultProxyConfig(cfg);
        }

    }


    @Override
    protected void onStop() {
        super.onStop();

        try
        {
            mGoogleApiClient.stopAutoManage(LoginActivity.this);
            mGoogleApiClient.disconnect();
        }
        catch (Exception e)
        {

        }
    }

}
