package com.callhippo.bueno.callhippo.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import 	androidx.annotation.Nullable;

import com.callhippo.bueno.callhippo.model.numberArray;
import 	com.google.android.material.floatingactionbutton.FloatingActionButton;
import 	androidx.fragment.app.Fragment;
import	androidx.localbroadcastmanager.content.LocalBroadcastManager;
import 	androidx.recyclerview.widget.RecyclerView;

import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.callhippo.bueno.callhippo.BuildConfig;
import com.callhippo.bueno.callhippo.DialerActivity;
import com.callhippo.bueno.callhippo.R;
//import com.callhippo.bueno.callhippo.Utils.ContactsDatabase;
import com.callhippo.bueno.callhippo.Utils.InternetConnectionManager;
import com.callhippo.bueno.callhippo.Utils.SessionManagement_network_strength;
import com.callhippo.bueno.callhippo.Utils.Util;
import com.callhippo.bueno.callhippo.service.LinphoneService;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.hbb20.CountryCodePicker;

import org.json.JSONObject;
import org.linphone.core.AccountCreator;
import org.linphone.core.Address;
import org.linphone.core.CallParams;
import org.linphone.core.Core;
import org.linphone.core.ProxyConfig;
import org.linphone.core.TransportType;
//import org.linphone.core.AccountCreator;
//import org.linphone.core.Address;
//import org.linphone.core.CallParams;
//import org.linphone.core.Core;
//import org.linphone.core.ProxyConfig;
//import org.linphone.core.RegistrationState;
//import org.linphone.core.TransportType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class Dialer_Fragment extends Fragment implements View.OnClickListener, View.OnLongClickListener{
    private static final String TAG = "Dialer_Fragment";
    EditText phoneNumber;
//    private Button mDeleteButton;
//    RoundedImageView rounduserCountry;
//    RelativeLayout phoneNumberLayout;
    LinearLayout plioCall, mHasButton, mOneButton, mTwoButton, mThreeButton, mFourButton, mFiveButton, mSixButton, mSevenButton, mEightButton, mNineButton, mStarButton, mZeroButton;
    static TextView txtUserNumber, textUserName;
    CountryCodePicker ccp;
    FrameLayout bottomupLayout;
    private Vibrator mVibrator;
    private static final int DURATION = 50; // Vibrate duration
    InternetConnectionManager internetConnection;
    SharedPreferences sharedPreferences;
    private final String MY_PREFERANCES = "callhippomaulik";
    SharedPreferences.Editor editor;
    String isIndia="";
    public ArrayList<String> outcallcountries = null;
    Util dialerUtil;
    private Context mcontext;
    TextView l_credits,tx_credits,tx_timezone;
    String credits_toshow="";
    CountryCodePicker codePicker2;
    String is_numberverified="true";
    String device_call_number="";
    private String device_info="",version_info="";

    private float available_credits=1;
    private static final int UPDATE_IMAGE = 1;



    LinearLayout phoneNumberLayout;
    ImageView rounduserCountry;

    public RecyclerView list_contacts;
    private ArrayList<String> contact_name;
    private ArrayList<String> contact_number;


    private BroadcastReceiver broadcastReceiver;
    private BroadcastReceiver broadcastReceiver1;
    String match_char="";
    LinearLayout l_newc;

    static final public String COPA_RESULT1 = "REQUEST_PROCESSED";
    static final public String COPA_MESSAGE1 = "match";

    static final public String COPA_RESULT_CM = "com.callhippo.bueno.callhippo.service.REQUEST_PROCESSED_cm";
    static final public String COPA_MESSAGE_CM = "com.callhippo.bueno.callhippo.service.transferButtonNotify_MSG_cm";

    LinearLayout l_new_contact,l_new_sms;

    private ImageView mDeleteButton;
    int phone_contact_size=0;
//    ContactsDatabase db;
//    ContactsDatabase_phone db_p;
    String cname="";
    SessionManagement_network_strength session_newwork;
    String network_status = "", network_speed = "", network_strength = "";
    private String ss="";
    String last_dial_number="";
    private FirebaseAnalytics mFirebaseAnalytics;

    Boolean is_lp=false;
    private AccountCreator mAccountCreator;
    ProxyConfig cfg;
    Core core;
    String lp_protocol="",lp_domain="",lp_uname="",lp_pass="";
    private Handler mHandler;
    FloatingActionButton fab_call;
    Boolean is_valid=true,check_validation=false;
    private PhoneNumberUtil phoneNumberUtil;

    String last_country_code="";
    Address address;

    int size_provider_db=1;
    int size_endpoint_db=0;
    String last_endpoint_update_time="",last_outcallprice="";
    String api_LastEndpointUpdateTime="",api_LastOutCallPriceUpdateTime="";
//    private ProxyConfig[] proxyConfigs;

    LinearLayout l_clist;
    String is_auto_switch="";
    String cnn="";
    String auto_switched_countryname="",auto_switched_number="",auto_switched_department="";
    public CountryCodePicker countryFlag;

    public static JSONObject jsonObject_twilio_country = new JSONObject();
    private static final String TAG_twilio = "twilio_login";
    public static final String tw_NOTIFICATION = "tw_call_accept_outgoing";

    public static JSONObject jsonObject_tw_number_verify = new JSONObject();
    String outgoingnumberProvider="";
    DialerActivity dialerActivity;
    String smsRight = "true";
    public String last_call_ext="";
    private Boolean is_internal_call=false;
    String app_first_time="";
    Boolean iscustomplan=true;
    String telephonyProvider = "",isFreeswitch="";
    String freeswitchuname="",freeswitchpass="",freeswitchdomain="",freeswitchtranspot="";
    public ArrayList<numberArray> numberArraysList = new ArrayList<numberArray>();
    public ArrayList<numberArray> numberArraysList_v2 = new ArrayList<numberArray>();
    int page;
    Boolean dontupdate = true;
    public static JSONObject jsonObject_timezone = new JSONObject();
    public static JSONObject jsonObject_timezone_areacode_aus = new JSONObject();
    public static JSONObject jsonObject_timezone_areacode_ca = new JSONObject();
    public static JSONObject jsonObject_timezone_areacode_us = new JSONObject();
    List<String> australia_areacode ;
    List<String> canada_areacode ;
    List<String> us_areacode ;
    Calendar current;
    long millisecconds;
    SimpleDateFormat sdf;
    Date resultDate;
    Time time;
    Boolean initiatCall = true;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mcontext=getActivity().getApplicationContext();
        Log.e(TAG,"Dialer_fragment: onCreate");

        try
        {
            if (getArguments() != null) {
                device_call_number = getArguments().getString("device_call_number");
                device_call_number = sharedPreferences.getString("device_call_number","");
                Log.e(TAG,"number_device "+device_call_number);
                Log.e(TAG,"aaaaaaaaaaaaaaaaaaadebug22 "+device_call_number);
            }
        }
        catch (Exception e)
        {
            device_call_number="";
        }

//        core = LinphoneService.getCore();




    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_dialer_v23, container, false);
        sharedPreferences = mcontext.getSharedPreferences(MY_PREFERANCES, Context.MODE_PRIVATE);
        internetConnection = new InternetConnectionManager(getActivity());
        dialerUtil = new Util(getActivity());
        mVibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        //  the Telephony Manager
        TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        smsRight=sharedPreferences.getString("is_sms_show","");
        app_first_time=sharedPreferences.getString("app_first_time","");
        telephonyProvider = sharedPreferences.getString("telephonyprovider","");


        dialerActivity = new DialerActivity();
        try {
            session_newwork = new SessionManagement_network_strength(mcontext);
            HashMap<String, String> user = session_newwork.getUserDetails();

            network_speed = user.get(SessionManagement_network_strength.KEY_SPEED);
            network_status = user.get(SessionManagement_network_strength.KEY_STATUS);
            network_strength = user.get(SessionManagement_network_strength.KEY_STRENGTH);

            Log.e(TAG, "network_speed " + network_speed);


            if (network_speed != null && !network_speed.equalsIgnoreCase("")) {
                ss = network_speed;
                Log.e(TAG, "network_speed" + ss);
            }


        } catch (Exception e) {
            Log.e(TAG, "Exception_" + e.getMessage());
        }



        list_contacts=(RecyclerView) rootView.findViewById(R.id.list_contacts);
        l_newc=(LinearLayout) rootView.findViewById(R.id.l_newc);

        l_new_contact=(LinearLayout) rootView.findViewById(R.id.l_new_contact);
        l_new_sms=(LinearLayout) rootView.findViewById(R.id.l_new_sms);

        l_clist = rootView.findViewById(R.id.linear_layour_dialer);



        try
        {
            Bundle bundle = this.getArguments();
            if(bundle!=null)
            {
                device_call_number = bundle.getString("device_call_number");
                device_call_number = sharedPreferences.getString("device_call_number","");
                Log.e(TAG,"device_call_number"+device_call_number);

                phoneNumber = (EditText) rootView.findViewById(R.id.phoneNumber);
                phoneNumber.setText(device_call_number);
                Log.e(TAG,"aaaaaaaaaaaaaaaaaaadebug33 "+device_call_number);

            }
        }
        catch (Exception e)
        {
            Log.e(TAG,"device_call_number_exception "+e.getMessage());
        }

//        Log.e(TAG+"_pro","size_provider_db "+size_provider_db);






        last_call_ext = sharedPreferences.getString("last_call_ext","");


        initUI(rootView);
        Log.e(TAG, "last COuntry code:" + ccp.getSelectedCountryCode());
        Log.e(TAG, "Now COuntry code:" + ccp.getSelectedCountryCode());


        is_auto_switch = sharedPreferences.getString("auto_switch_settings","");
        freeswitchuname = sharedPreferences.getString("freeswitchuname","");
        freeswitchpass = sharedPreferences.getString("freeswitchpass","");
        freeswitchdomain = sharedPreferences.getString("freeswitchdom","");
        freeswitchtranspot = sharedPreferences.getString("freeswitchtrans","");

        try {
            if (freeswitchuname!=null)
            {
                if (!freeswitchuname.equalsIgnoreCase(""))
                {
                    if (LinphoneService.isReady())
                    {
                        try
                        {
                            Log.e(TAG,"linphonestate "+LinphoneService.getCore().getDefaultProxyConfig().getState().toString());
                            if(LinphoneService.getCore().getDefaultProxyConfig().getState().toString().equalsIgnoreCase("None") ||
                                LinphoneService.getCore().getDefaultProxyConfig().getState().toString().equalsIgnoreCase("Failed") )
                            {
                                Log.e(TAG,"linphone_login_dialer");

                                try {
                                    lp_login2(freeswitchuname, freeswitchpass, freeswitchdomain, freeswitchtranspot);
                                } catch (Exception e) {
                                }
                            }
                        }
                        catch (Exception e)
                        {
                            try
                            {
                                lp_login2(freeswitchuname, freeswitchpass, freeswitchdomain, freeswitchtranspot);
                            } catch (Exception ef) { }

                        }
                    } else {
                        try {
                            mHandler = new Handler();
                            // If it's not, let's start it
                            mcontext.startService(new Intent(getActivity(), LinphoneService.class));
                            // And wait for it to be ready, so we can safely use it afterwards
                            new ServiceWaitThread().start();
                        } catch (Exception e) { }
                    }
    //                lp_login2(freeswitchuname,freeswitchpass,freeswitchdomain,freeswitchtranspot);
                }
            }
        } catch (Exception e) {
        }





        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);




        try
        {
            getDeviceInformation();
        }
        catch (Exception e)
        {
            device_info="NA";
            version_info="NA";
        }


        tx_timezone = (TextView) rootView.findViewById(R.id.txt_timezone);



        String timezone_local= sharedPreferences.getString("displayTimezone","");
        if(timezone_local.equalsIgnoreCase("false"))
        {
            tx_timezone.setVisibility(View.GONE);
        }

        fab_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String input=phoneNumber.getText().toString();
                input = input.replaceAll("\\s+", "").replaceAll("\\p{P}", "").replace("\u2014", "");

                  address = core.interpretUrl(input);
                    core.inviteAddress(address);

                    CallParams callParams;
                    callParams = core.createCallParams(null);
                    core.inviteAddressWithParams(address,callParams);

            }
        });

//




        return rootView;
    }






    /**
     * Initializes the UI components
     */
    private void initUI(View v) {
        initializeViews(v);
        setClickListeners();
    }

    /**
     * Initializes the views from XML
     */
    private void initializeViews(View v) {
        phoneNumber = (EditText) v.findViewById(R.id.phoneNumber);




        try
        {
            phoneNumber.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    phoneNumber.setCursorVisible(true);
                    phoneNumber.setShowSoftInputOnFocus(false);
                    return false;
                }
            });
        }
        catch (Exception e){}


        fab_call = (FloatingActionButton) v.findViewById(R.id.fab_call);
        plioCall = (LinearLayout) v.findViewById(R.id.PlioCall);
        mOneButton = (LinearLayout) v.findViewById(R.id.btnOne);
        mTwoButton = (LinearLayout) v.findViewById(R.id.btntwo);
        mThreeButton = (LinearLayout) v.findViewById(R.id.btnthree);
        mFourButton = (LinearLayout) v.findViewById(R.id.btnfour);
        mFiveButton = (LinearLayout) v.findViewById(R.id.btnfive);
        mSixButton = (LinearLayout) v.findViewById(R.id.btnsix);
        mSevenButton = (LinearLayout) v.findViewById(R.id.btnseven);
        mEightButton = (LinearLayout) v.findViewById(R.id.btneight);
        mNineButton = (LinearLayout) v.findViewById(R.id.btnnine);

        mZeroButton = (LinearLayout) v.findViewById(R.id.btnzero);
        mStarButton = (LinearLayout) v.findViewById(R.id.btnstar);
        mHasButton = (LinearLayout) v.findViewById(R.id.btnhas);
//        mDeleteButton = (Button) v.findViewById(R.id.btnDelete);

        mDeleteButton = (ImageView) v.findViewById(R.id.btnDelete);

//        txtUserNumber = (TextView) v.findViewById(R.id.textUserNumber);
        textUserName = (TextView) v.findViewById(R.id.textUserName);
//        phoneNumberLayout = (RelativeLayout) v.findViewById(R.id.phoneNumberLayout);

        bottomupLayout = (FrameLayout) v.findViewById(R.id.bottomupLayout);
//        rounduserCountry = (RoundedImageView) v.findViewById(R.id.imgdiaerUsercountry);

        phoneNumberLayout = (LinearLayout) v.findViewById(R.id.phoneNumberLayout); // V2 UI
        rounduserCountry = (ImageView) v.findViewById(R.id.imgdiaerUsercountry); // V2 UI

        countryFlag = v.findViewById(R.id.country_flag_d);

        ccp = (CountryCodePicker) v.findViewById(R.id.ccp);
        Typeface typeFace=Typeface.createFromAsset(getContext().getAssets(),"fonts/dialpad_font.ttf");
        ccp.setTypeFace(typeFace);
        codePicker2 = v.findViewById(R.id.ccp_dialer_2);
        codePicker2.setClickable(false);


        contact_name = new ArrayList<>();
        contact_number = new ArrayList<>();
//        Log.e(TAG,"usernumber_size_");


    }

    private void setClickListeners() {
        mZeroButton.setOnClickListener(this);
        mZeroButton.setOnLongClickListener(this);

        mOneButton.setOnClickListener(this);
        mTwoButton.setOnClickListener(this);
        mThreeButton.setOnClickListener(this);
        mFourButton.setOnClickListener(this);
        mFiveButton.setOnClickListener(this);
        mSixButton.setOnClickListener(this);
        mSevenButton.setOnClickListener(this);
        mEightButton.setOnClickListener(this);
        mNineButton.setOnClickListener(this);
        mStarButton.setOnClickListener(this);
        mHasButton.setOnClickListener(this);
        mDeleteButton.setOnClickListener(this);
        mDeleteButton.setOnLongClickListener(this);

    }

    private void keyPressed(int keyCode) {
        mVibrator.vibrate(DURATION);
        KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
        phoneNumber.onKeyDown(keyCode, event);
//        Button b = (Button)v;
//        b.setBackground(getResources().getDrawable(R.drawable.circle_outline_press));
//        b.setTextColor(getResources().getColor(R.color.colorwhite));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOne: {
                keyPressed(KeyEvent.KEYCODE_1);
                return;
            }
            case R.id.btntwo: {
                keyPressed(KeyEvent.KEYCODE_2);
                return;
            }
            case R.id.btnthree: {
                keyPressed(KeyEvent.KEYCODE_3);
                return;
            }
            case R.id.btnfour: {
                keyPressed(KeyEvent.KEYCODE_4);
                return;
            }
            case R.id.btnfive: {
                keyPressed(KeyEvent.KEYCODE_5);
                return;
            }
            case R.id.btnsix: {
                keyPressed(KeyEvent.KEYCODE_6);
                return;
            }
            case R.id.btnseven: {
                keyPressed(KeyEvent.KEYCODE_7);
                return;
            }
            case R.id.btneight: {
                keyPressed(KeyEvent.KEYCODE_8);
                return;
            }
            case R.id.btnnine: {
                keyPressed(KeyEvent.KEYCODE_9);
                return;
            }
            case R.id.btnzero: {
                keyPressed(KeyEvent.KEYCODE_0);
                return;
            }
            case R.id.btnhas: {
                keyPressed(KeyEvent.KEYCODE_POUND);
                return;
            }
            case R.id.btnstar: {
                keyPressed(KeyEvent.KEYCODE_STAR);
                return;
            }
            case R.id.btnDelete: {
                keyPressed(KeyEvent.KEYCODE_DEL);
                return;
            }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.btnDelete: {
                Editable digits = phoneNumber.getText();
                digits.clear();
                return true;
            }
            case R.id.btnzero: {
                keyPressed(KeyEvent.KEYCODE_PLUS);
                return true;
            }
        }
        return false;
    }



    @Override
    public void onPause() {
        super.onPause();

        try
        {
            Log.e(TAG+"_state", "onPause: Called" );
            Log.e(TAG+"_state","typed_number_dialer "+phoneNumber.getText().toString());

            editor = sharedPreferences.edit();
            editor.putString("typed_number_dialer",phoneNumber.getText().toString());
            editor.commit();
        }
        catch (Exception e)
        {

        }

    }

    @Override
    public void onResume() {
        super.onResume();




        try
        {
            mHandler = new Handler();
            if (LinphoneService.isReady()) {
                core = LinphoneService.getCore();
                Log.e(TAG,"onServiceReady");
            } else {
                // If it's not, let's start it
                mcontext.startService(new Intent(getActivity(), LinphoneService.class));
                // And wait for it to be ready, so we can safely use it afterwards
                new ServiceWaitThread().start();
                Log.e(TAG,"startService");
            }
        }
        catch (Exception e){}






        LocalBroadcastManager.getInstance(getActivity()).registerReceiver((broadcastReceiver),
                new IntentFilter(COPA_RESULT1)
        );

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver((broadcastReceiver),
                new IntentFilter(COPA_RESULT_CM)
        );


        try
        {
            editor = sharedPreferences.edit();
            editor.putString("addcontact_from_detail", "");
            editor.putString("addcontact_from_detail_page", "0");
            editor.commit();
        }
        catch (Exception e)
        {

        }




    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG, "onStop: Called" );

        if(broadcastReceiver != null) {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadcastReceiver);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: Called" );
    }









    private void getDeviceInformation() {


        device_info= Build.MODEL+"->"+ Build.BRAND + "(" + Build.VERSION.RELEASE + ")"+"->"+Build.VERSION.SDK_INT ;
        version_info= BuildConfig.VERSION_NAME;

        Log.d("DeviceInfoMODEL", "--->" + Build.MODEL);
        Log.d("DeviceInfoBRAND", "--->" + Build.BRAND + " (" + Build.VERSION.RELEASE + ")");
//        Log.d("DeviceInfoMANUFACTURER", "--->" + Build.MANUFACTURER);
//        Log.d("DeviceInfoSERIAL", "--->" + Build.SERIAL);
        Log.d("DeviceInfoSDK_INT", "--->" + Build.VERSION.SDK_INT +" "+ BuildConfig.VERSION_NAME);
//        Log.d("DeviceInfoDEVICE", "--->" + Build.DEVICE);

        Log.d("Device_device_info", "--->" + device_info);
        Log.d("Device_version_info", "--->" + version_info);


    }







    private void lp_login2(String username,String password , String domain,String protocol)
    {


        Log.e(TAG+"linphone_login","android_sipUname "+username);
        Log.e(TAG+"linphone_login","android_sipPasswd "+password);
        Log.e(TAG+"linphone_login","android_sipdomain "+domain);
        Log.e(TAG+"linphone_login","android_siptransptyp "+protocol);

        mAccountCreator = LinphoneService.getCore().createAccountCreator(null);
        if(mAccountCreator!=null)
        {
            mAccountCreator.setUsername(username);
            mAccountCreator.setDomain(domain);
            mAccountCreator.setPassword(password);

            if (protocol.equalsIgnoreCase("TLS")) {
                mAccountCreator.setTransport(TransportType.Tls);
            } else if (protocol.equalsIgnoreCase("TCP")) {
                mAccountCreator.setTransport(TransportType.Tcp);
            } else if (protocol.equalsIgnoreCase("UDP")) {
                mAccountCreator.setTransport(TransportType.Udp);
            } else {
                mAccountCreator.setTransport(TransportType.Tcp);
            }


            // This will automatically create the proxy config and auth info and add them to the Core
//        cfg = mAccountCreator.configure();
            cfg = mAccountCreator.createProxyConfig();
            // Make sure the newly created one is the default
            LinphoneService.getCore().setDefaultProxyConfig(cfg);
        }

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
                        }
                    });
        }
    }







































}
