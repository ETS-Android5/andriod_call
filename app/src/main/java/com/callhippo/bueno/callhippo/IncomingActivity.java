package com.callhippo.bueno.callhippo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.PowerManager;
import android.os.Vibrator;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.design.widget.*;
//import android.support.design.widget.FloatingActionButton;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
//import androidx.core.content.LocalBroadcastManager;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.app.NotificationCompat;
//import android.support.v7.view.ContextThemeWrapper;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.callhippo.bueno.callhippo.Utils.ActivityHelper;
import com.callhippo.bueno.callhippo.Utils.Constants;
//import com.callhippo.bueno.callhippo.Utils.ContactsDatabase;
import com.callhippo.bueno.callhippo.Utils.CustomautoCompleteTextview;
import com.callhippo.bueno.callhippo.Utils.InternetConnectionManager;
import com.callhippo.bueno.callhippo.Utils.SessionManagement_ParentID;
import com.callhippo.bueno.callhippo.Utils.SessionManagement_network_strength;
import com.callhippo.bueno.callhippo.service.LinphoneService;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

//import org.linphone.core.CallStats;
//import org.linphone.core.Core;
//import org.linphone.core.CoreListenerStub;
//import org.linphone.core.ProxyConfig;
//import org.linphone.core.RegistrationState;
//import org.linphone.core.StreamType;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.linphone.core.Call;
import org.linphone.core.Core;

import static com.callhippo.bueno.callhippo.service.MyFirebaseMessagingService.mRingTone_10;

/**
 * Created by Appitsimple11 on 27-03-2017.
 */

public class IncomingActivity extends AppCompatActivity {
    public final static String TAG = "IncomingActivity";
    private int notification_id;
    public boolean isnotification=false;
    private RemoteViews remoteViews;
    String incomingNumber=""; String incoming_uid="";
    ImageView btnincomingHangup, btnincomingReceive,btnMute,btnSpeaker,btnDialer,btnHold;
    TextView textViewIncomingfromnumber,textViewIntime,t_dash;
    LinearLayout callanswerLayout;
    private CountDownTimer countDownTimer; // built in android class CountDownTimer
    private long totalTimeCountInMilliseconds;
//    private PulsatorLayout mPulsator;
    Boolean speakerToggle = false, muteToggle = false , holdToggle=false,dialpadToggle=false,forwardToggle=false,recordToggle=false,noteToggle=false;
    AudioManager mAudioManager;
    Ringtone mRingTone;
    Uri notification;
    SharedPreferences sharedPreferences;
    private final String  MY_PREFERANCES = "callhippomaulik";
    SharedPreferences.Editor editor;
    boolean isItreceive= false; // for incomingcall reject or incomingcall hangup
    boolean removefromatask = false;   // application forcefully closed by swipe
    Vibrator mVibrator;
    final int DURATION = 50; // Vibrate duration
    EditText phoneNumber;
    //Begin initialize Proximity Sensor Code
    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;
    private int field = 0x00000020;
    //End initialize Proximity Sensor Code
    private static Messenger mBoundServiceMessenger;
    private static boolean mServiceConnected = false;
    private static boolean isreceived = false;
    private final Messenger mActivityMessenger = new Messenger(
            new ActivityHandler(this));
    private VoiceBroadcastReceiver voiceBroadcastReceiver;
    public static final String ACTION_INCOMING_CALL_FINISHED = "com.callhippo.bueno.callhippo.CLOSED_VIEW";
    public static AppCompatActivity mcontext;
    InternetConnectionManager internetConnection;
    private static final String NOTIFICATION_CHANNEL_ID = "my_notification_channel";
    private String planName="";

    ImageView btn_calltransfer;
//    ListView lv_subusers;
    RecyclerView lv_subusers;
    GridView gv_subusers;
    private ArrayList<String> su_username;
    private ArrayList<String> su_userid;
    private ArrayList<String> su_available;

    private String tr_userid="";
    AlertDialog alertDialog_subuser,alretDialog_tr2;
    public static AlertDialog alretDialog_tr4;
    public static AlertDialog alretDialog_tr3;
    LinearLayout dlg_l_blindtransfer,dlg_l_talktouser;
    TextView dlg_tr_uname,dlg_tr_talkto;
    ImageView dlg_back;
    private String x_from_tr_number="",x_to_tr_number="",x_ph_tr_id="",x_incoming_name="",x_ph_extensioncall="",x_ph_Maincall="",x_ph_postCallSurvey="",x_ph_Blindtransfer="",x_ph_recordPermission="",x_ph_recordControl="";
    private String parentID="";
    SessionManagement_ParentID session_parentid;
//    Button dlg_btn_fromnumber,dlg_btn_tonumber,dlg_btn_transfer,dlg_btn_merge,dlg_btn_hangup;
    ImageView dlg_close;
    //    String endConference="",removeSwitch="",removeFromCaller="";
    String fromCallerNumber="",toCallNumber="";
    public static String fromCallerNumber1="",toCallNumber1="";
    Boolean endConference,removeSwitch,removeFromCaller;
    ImageView btn_active1,btn_deactive1,btn_active2,btn_deactive2;
    public static LinearLayout dlg4_l_fromname,dlg4_l_toname;
    String calltrasnfer_option="",calltrasnfer_option_l="";
    LinearLayout l_calltransfer;
    String afterDecode="";

    private BroadcastReceiver broadcastReceiver;
    static final public String COPA_RESULT = "com.callhippo.bueno.callhippo.service.REQUEST_PROCESSED";
    static final public String COPA_MESSAGE = "com.callhippo.bueno.callhippo.service.transferButtonNotify_MSG";

    public static boolean actvitiyRunning = false;

    TextView txt_incoming_department;
    String temp_match="";
    private static final int UPDATE_IMAGE = 1;
    private String x_department_name="";
    private TextView txt_toname_v2,txt_tonumber_v2;
    private LinearLayout l_mute,l_hold,l_loud,l_dialpad,l_hangup,l_forward,l_record,l_notes,l_takenotes,l_survey;

    ImageView img_mute,img_hold,img_loud,img_dialpad,img_forward,img_record,img_survey,img_notes;
    TextView t_mute,t_hold,t_loud,t_dialpad,t_forward,t_record,t_survey,t_notes;

    LinearLayout mHasButton, mOneButton, mTwoButton, mThreeButton, mFourButton, mFiveButton, mSixButton, mSevenButton, mEightButton, mNineButton, mStarButton, mZeroButton;

    ImageView back_dialpad;
    LinearLayout l_dialpad_top,l_main_calling;
    LinearLayout l_mhf;
    LinearLayout l_subuser_list,l_tranfertype,l_switch_call,l_conf_call;

    LinearLayout dlg_btn_fromnumber,dlg_btn_tonumber,dlg_btn_transfer,dlg_btn_merge,dlg_btn_hangup;

    TextView txt_from_merge,txt_to_merge;
    ImageView img_from_merge,img_to_merge;

    LinearLayout l_receive_m,l_hangup_m,l_dialing,l_oncall,l_oncallNew;
    Vibrator vibrator;
    AudioManager am;


    String incoming_cname="";
    String x_integration_name="";
    TextView t_integration;
    String x_ph_network_str="";
    SessionManagement_network_strength session_newwork;
    String network_status = "", network_speed = "", network_strength = "";
    private String ss="";
    String acw_callerName="";

  //  Core core;
    Boolean is_lp=false;
  //  private CoreListenerStub mCoreListener;

    private FirebaseAnalytics mFirebaseAnalytics;
    FloatingActionButton  fab_ans,fab_hangup_m, fab_hangup;

    private int lastState = TelephonyManager.CALL_STATE_IDLE;
    String incoming_reminder_number="",incoming_reminder_number_display="",networkStrengthRandomString_reminder="";
    AlertDialog dialog = null;
    Boolean call_received=false;
    String call_duration="";
    String to_acw="",from_acw="";

    LinearLayout l_lastcall;
    ImageView img_cross;

    ImageView img_lasttype;
    String lastcall_setting="";
    TextView t_lastname,t_laststatus,t_lastdate;
    String get_lastcallnumber="";
    Boolean is_pstn_call=false;

    Boolean switch_layout_en=false;
    Boolean is_forward=false;

//    CallStats state1;
    Boolean is_snackbar=false;
 //   Core core1;
    Snackbar snackbar;
    String x_ph_transfer_by="";
    TextView t_transfer_name;
    String x_calltransfer="";

    String lastcall_type="",lastcall_fullName="",lastcall_date="",lastcall_status="";
    Boolean is_acw_open=false;

    Boolean is_forward_hold=false;
    Boolean start_network=false;
    Boolean is_rating_open=false;

    ImageView ic_back_sublist;
    String is_accept_notification="";
    String speed_from="",speed_to="";
    String speed_low ="";
    Boolean f_spbar=true;
    List<String> spd_array = new ArrayList<String>();
    List<String> low_spd_array = new ArrayList<String>();

    RecyclerView list_tr_contacts;
//    ContactsDatabase db;

    LinearLayout l_history_tab, l_subuser_tab;
    EditText edt_searchtext;
    TextView tt_contact,tt_subuser;
    LinearLayout l_newcontact;

    static final public String COPA_RESULT1 = "REQUEST_PROCESSED1";
    static final public String COPA_MESSAGE1 = "match1";
    String COPA_MESSAGE2 = "value1";
    String contact_search="",new_number="";
    TextView txt_typed_number;

    public static final String tw_NOTIFICATION = "tw_call_incoming_firebase";
    private static final String TAG_twilio = "twilio_Callreminder";
    CustomautoCompleteTextview autoCompleteTextView;
    ArrayList<String> taglist;
    Boolean isTransferdcall = false;
    String istagginginplan = "";
    public static PhoneNumberUtil phoneNumberUtil;

    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;

//    String recordingStatus_B = "";
//    String recordingStatus_C = "";
//    String Compliance_inPlan = "";
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHelper.initialize(this);
        //try{LinphoneService.getCore().stopRinging();}catch (Exception e){}
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
        sharedPreferences = getSharedPreferences(MY_PREFERANCES,MODE_PRIVATE);
        setContentView(R.layout.activity_incoming_new_calltag_v99);
//        Intent intent = new Intent(getApplicationContext(), KeepAliveService.class);
//        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
        mcontext = this;
        planName = sharedPreferences.getString("displayname","");
        calltrasnfer_option = sharedPreferences.getString("callTransfer","");
        actvitiyRunning = true;
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


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


        }catch (Exception e){}




        txt_typed_number= findViewById(R.id.txt_typed_numbers);
        l_newcontact= findViewById(R.id.l_new_contact);
        tt_contact=findViewById(R.id.txt_contact);
        tt_subuser=findViewById(R.id.txt_highlight_detail);
        tt_subuser.setTextColor(getResources().getColor(R.color.colorPrimary));

        edt_searchtext= findViewById(R.id.edt_srch);
        l_history_tab=findViewById(R.id.l_history_tab);
        l_subuser_tab = findViewById(R.id.l_detail_tab);

        lv_subusers =  findViewById(R.id.list_subusers);
        list_tr_contacts= findViewById(R.id.list_subusers_contacts);
//        contacts = db.getAllContacts();









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

            if(Build.VERSION.SDK_INT >= 23)
            {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Audio permission has not been granted.
                    Log.i(TAG, "recording permissions has NOT been granted. Requesting permissions.");
                    try{requestAudioPermissions();}catch (Exception e2){}
                    Toast.makeText(IncomingActivity.this, "Please allow audio permission", Toast.LENGTH_SHORT).show();
                    editor = sharedPreferences.edit();
                    editor.putBoolean(Constants.SH_IS_AUDIO_PERMISSION, false);
                    editor.commit();
                } else {
                    // Audio permissions is already available, show the camera preview.
                    Log.i(TAG, "Audio permission has already been granted. Displaying camera preview.");
                    editor = sharedPreferences.edit();
                    editor.putBoolean(Constants.SH_IS_AUDIO_PERMISSION, true);
                    editor.commit();
                }

            }

        }
        catch (Exception e)
        {

        }

        try {
            session_newwork = new SessionManagement_network_strength(getApplicationContext());
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


        l_lastcall= findViewById(R.id.l_lastcall);
        img_cross = findViewById(R.id.img_close);

        img_lasttype = findViewById(R.id.img_lasttype);
        t_lastname = findViewById(R.id.txt_lastname);
        t_lastdate = findViewById(R.id.txt_lasttime);
        t_laststatus = findViewById(R.id.txt_laststatus);

        lastcall_setting=sharedPreferences.getString("last_calldetail_setting","");





        //Begin initialize Proximity Sensor Code
        try {
            // Yeah, this is hidden field.
            field = PowerManager.class.getClass().getField("PROXIMITY_SCREEN_OFF_WAKE_LOCK").getInt(null);
        } catch (Throwable ignored) {
        }

        powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(field, getLocalClassName());
        //End initialize Proximity Sensor Code

        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        totalTimeCountInMilliseconds = 60 * 500* 1000;
//        Log.e(TAG, "onCreate: "+mCommManager.toString());
        internetConnection = new InternetConnectionManager(this);
        mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
//        mAudioManager.setMode(AudioManager.MODE_RINGTONE);

        Log.e(TAG,"call_mode: "+mAudioManager.getMode());


        mAudioManager.setSpeakerphoneOn(false);
//        startService(new Intent(IncomingActivity.this, IncomingService.class));
//        btnMute = (ImageView) findViewById(R.id.incomingMute);
//        btnSpeaker = (ImageView) findViewById(R.id.incomingSpeaker);
//        btnHold = (ImageView) findViewById(R.id.incomingHold);
//        btnDialer = (ImageView) findViewById(R.id.incomingdialer);
        btnincomingReceive = (ImageView) findViewById(R.id.btnIncomingReceive);
        btnincomingHangup = (ImageView) findViewById(R.id.btnIncomingHangup);
        callanswerLayout = (LinearLayout) findViewById(R.id.callreceive);
//        textViewIncomingfromnumber = (TextView) findViewById(R.id.textViewIncomingfromnumber);
        textViewIntime = (TextView) findViewById(R.id.textViewIncomingtime);
        t_dash  = findViewById(R.id.dash);
//        mPulsator = (PulsatorLayout) findViewById(R.id.pulsator);

        txt_incoming_department=(TextView) findViewById(R.id.incoming_department);

        t_integration=(TextView) findViewById(R.id.txt_integration_name);

        t_transfer_name = findViewById(R.id.txt_transfer_name);

//        btn_calltransfer=(ImageView) findViewById(R.id.ic_calltrans);
//        l_calltransfer=(LinearLayout) findViewById(R.id.l_calltras);

        lv_subusers =  findViewById(R.id.list_subusers);

        l_hangup_m=(LinearLayout) findViewById(R.id.l_hangup_m);
        l_dialing=(LinearLayout) findViewById(R.id.l_dialing);
        l_oncall=(LinearLayout) findViewById(R.id.l_oncall);
        l_oncallNew=(LinearLayout) findViewById(R.id.l_oncall_new);

        fab_ans=(FloatingActionButton) findViewById(R.id.fabAnswer);
        fab_hangup_m=(FloatingActionButton) findViewById(R.id.fabHangup_m);
        fab_hangup=(FloatingActionButton) findViewById(R.id.fabHangup);


        su_username = new ArrayList<>();
        su_userid = new ArrayList<>();
        su_available = new ArrayList<>();

//        btnSpeaker.setEnabled(false);
//        btnMute.setEnabled(false);
//        btnDialer.setEnabled(false);
//        btnHold.setEnabled(false);
//        btn_calltransfer.setEnabled(false);
//        l_calltransfer.setEnabled(false);


        l_mute=(LinearLayout) findViewById(R.id.l_mute);
        l_hold=(LinearLayout) findViewById(R.id.l_hold);
        l_forward=(LinearLayout) findViewById(R.id.l_forward);
        l_loud=(LinearLayout) findViewById(R.id.l_loud);
        l_dialpad=(LinearLayout) findViewById(R.id.l_dialpad);
        l_hangup=(LinearLayout) findViewById(R.id.l_hangup);
        l_record = findViewById(R.id.l_record);
        l_survey = findViewById(R.id.l_postcall);
        l_takenotes =findViewById(R.id.takenotes);
        l_notes = findViewById(R.id.l_notes_v1);

        img_mute=(ImageView) findViewById(R.id.ic_mute);
        img_hold=(ImageView) findViewById(R.id.ic_hold);
        img_loud=(ImageView) findViewById(R.id.ic_loud);
        img_dialpad=(ImageView) findViewById(R.id.ic_dialer);
        img_forward=(ImageView) findViewById(R.id.ic_forward);
        img_record = findViewById(R.id.ic_record);
        img_survey = findViewById(R.id.ic_postcall);
        img_notes =findViewById(R.id.ic_notes);

        t_mute=(TextView) findViewById(R.id.tx_mute);
        t_hold=(TextView) findViewById(R.id.tx_hold);
        t_loud=(TextView) findViewById(R.id.tx_loud);
        t_dialpad=(TextView) findViewById(R.id.tx_dialer);
        t_forward=(TextView) findViewById(R.id.tx_forward);
        t_notes = findViewById(R.id.tx_notes);
        t_record = findViewById(R.id.tx_record);
        t_survey =findViewById(R.id.tx_postcall);


        txt_toname_v2=(TextView) findViewById(R.id.txt_toname);
        txt_tonumber_v2=(TextView) findViewById(R.id.txt_tonumber);

        back_dialpad=(ImageView) findViewById(R.id.img_back_dialpad) ;

        l_main_calling=(LinearLayout) findViewById(R.id.top_layout);
        l_dialpad_top=(LinearLayout) findViewById(R.id.l_dialpad_top);

        lv_subusers =  findViewById(R.id.list_subusers);
        l_subuser_list=(LinearLayout) findViewById(R.id.l_subuser_listing);

        l_tranfertype=(LinearLayout) findViewById(R.id.transfer_call_layout);
        l_switch_call=(LinearLayout) findViewById(R.id.switch_call_layout);
        l_conf_call=(LinearLayout) findViewById(R.id.conference_call_layout);

        ic_back_sublist = findViewById(R.id.icon_back_subuser);


        l_mhf=(LinearLayout) findViewById(R.id.l_mhf);
//        l_mhf.setVisibility(View.GONE);
        l_loud.setVisibility(View.GONE);
        l_dialpad.setVisibility(View.GONE);

        l_loud.setEnabled(false);
        l_mute.setEnabled(false);
        l_dialpad.setEnabled(false);
        l_hold.setEnabled(false);
        l_forward.setEnabled(false);
//        l_calltransfer.setEnabled(false);







        Intent intentcall = getIntent();
        if(intentcall!=null)
        {






            incoming_reminder_number=incomingNumber;
            incoming_reminder_number_display=incomingNumber;
//            try{incoming_reminder_number_display = URLDecoder.decode(incoming_reminder_number_display, "UTF-8");}catch (Exception e){}
            networkStrengthRandomString_reminder=x_ph_network_str;
            Log.e(TAG,"incoming_display "+incoming_reminder_number_display+" incoming_number "+incoming_reminder_number+" networkreminder "+networkStrengthRandomString_reminder);
            get_lastcallnumber=incomingNumber;
        }











        speed_from=incomingNumber;
        speed_to=x_to_tr_number;


        from_acw=incomingNumber;
        try
        {
            if(from_acw.charAt(0)!='+')
            {
                from_acw="+"+from_acw;
            }
        }
        catch (Exception e){}

        to_acw=x_to_tr_number;
        try
        {
            if(to_acw.charAt(0)!='+')
            {
                to_acw="+"+to_acw;
            }
        }
        catch (Exception e){}




        try
        {
            if(x_ph_network_str==null)
            {
                x_ph_network_str="";
            }
        }
        catch (Exception e)
        {
            x_ph_network_str="";
        }

        Log.e(TAG,"x_integration_name "+x_integration_name);


        // for new UI v2
        if(x_ph_tr_id!=null)
        {
            if(x_ph_tr_id.equalsIgnoreCase("0"))
            {
//                btn_calltransfer.setVisibility(View.VISIBLE);
//                l_calltransfer.setVisibility(View.VISIBLE);

//                l_forward.setVisibility(View.VISIBLE); // UI v2
                is_forward=false;
                is_forward_hold=false;
            }
            else
            {
//                btn_calltransfer.setVisibility(View.GONE);
//                l_calltransfer.setVisibility(View.GONE);

//                l_forward.setVisibility(View.GONE);
                is_forward=true;
                is_forward_hold=true;

            }
        }
        else
        {
//            btn_calltransfer.setVisibility(View.GONE);
//            l_calltransfer.setVisibility(View.GONE);

//            l_forward.setVisibility(View.GONE);
            is_forward=true;
            is_forward_hold=true;
        }
















        try
        {
            if(incomingNumber!=null)
            {
                if(!incomingNumber.equalsIgnoreCase("") && !incomingNumber.equalsIgnoreCase(x_incoming_name))
                {
//                    textViewIncomingfromnumber.setText(x_incoming_name);

                    try
                    {
                        if(incomingNumber.charAt(0)!='+')
                        {
                            incomingNumber="+"+incomingNumber;
                        }
                    }
                    catch (Exception e)
                    {

                    }

                }
                else
                {
//                    textViewIncomingfromnumber.setText(incomingNumber);
//                    txt_tonumber_v2.setText(incomingNumber); // UI v2
                }
            }
            else
            {
//                textViewIncomingfromnumber.setText(incomingNumber);
//                txt_tonumber_v2.setText(incomingNumber); // UI v2
            }
        }
        catch (Exception e)
        {
//            textViewIncomingfromnumber.setText(incomingNumber);
//            txt_tonumber_v2.setText(incomingNumber); // UI v2
        }


        try
        {
            if(x_incoming_name!=null)
            {
                if(!x_incoming_name.equalsIgnoreCase(""))
                {
                    afterDecode = URLDecoder.decode(x_incoming_name, "UTF-8");
                    Log.e(TAG, "afterDecode: "+afterDecode);
                }
                else
                {
                    afterDecode="";
                }
            }
            else
            {
                afterDecode="";
            }

        } catch (Exception e) {
            afterDecode="";
//            e.printStackTrace();
        }

        try
        {
            if(afterDecode!=null)
            {
                if (!afterDecode.equalsIgnoreCase(""))
                {
//                    textViewIncomingfromnumber.setText(afterDecode);

                    String numplus=x_incoming_name;
                    if(numplus.charAt(0)!='+')
                    {
                        numplus="+"+numplus;
                    }
                    if(incomingNumber.equalsIgnoreCase(numplus))
                    {
                        Log.e(TAG, "afterDecode00: "+afterDecode);
                        afterDecode="+"+afterDecode;
                    }
                    else
                    {
                        acw_callerName = afterDecode;
//                        Log.e(TAG, "afterDecode33: "+afterDecode);
                        txt_toname_v2.setText(afterDecode);
                        txt_toname_v2.setVisibility(View.VISIBLE);// UI v2
                    }

                }
            }
        }
        catch (Exception e)
        {

        }



        remoteViews = new RemoteViews(getPackageName(),R.layout.call_notification);
//        textviewIncomingtoNumber.setText("To ("+sharedPreferences.getString("fromnumber","")+")");

        // to stop playing ringtone when clicks on "Accept" or "decline" from notification for OS 10
        try
        {
//            Log.e(TAG,"10_audio_"+mRingTone_10.isPlaying());
            if(mRingTone_10.isPlaying())
            {
                mRingTone_10.stop();
            }

        }
        catch (Exception e){}


        am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

        AudioManager.OnAudioFocusChangeListener focusChangeListener = null;
        int result = am.requestAudioFocus(focusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        final MediaPlayer mediaPlayer = new MediaPlayer();
        focusChangeListener = new
                AudioManager.OnAudioFocusChangeListener() {
                    @Override
                    public void onAudioFocusChange(int i) {
                        am = (AudioManager) getSystemService(mcontext.AUDIO_SERVICE);
                        switch (i) {
                            case (AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK):
//                                Log.e(TAG, "qqqqqqqqqqq");
                                mediaPlayer.setVolume(0.2f, 0.2f);
                                break;

                            case (AudioManager.AUDIOFOCUS_LOSS_TRANSIENT):
//                                Log.e(TAG, "hhhhhhhhh");
                                onPause();
                                break;

                            case (AudioManager.AUDIOFOCUS_LOSS):
                                onStop();
//                                    ComponentName componentName=new ComponentName()
                                break;

                            case (AudioManager.AUDIOFOCUS_GAIN):
//                                Log.e(TAG, "kkkkkkkkk");
                                mediaPlayer.setVolume(1f, 1f);
                                mediaPlayer.start();
                                break;
                            default:
                                break;
                        }
                    }
                };

        try
        {
            switch (am.getRingerMode()) {
                case AudioManager.RINGER_MODE_SILENT:
                    Log.e(TAG,"Silent_mode");
                    break;
                case AudioManager.RINGER_MODE_VIBRATE:
                    Log.e(TAG,"Vibrate_mode");

                    try
                    {

                        long[] pattern = {0, 500, 1000,2000}; // default pattern goes here
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            vibrator.vibrate(pattern,0);
                        } else {
                            //deprecated in API 26
                            vibrator.vibrate(pattern,0);
                        }
                    }
                    catch (Exception e)
                    {

                    }

                    break;
                case AudioManager.RINGER_MODE_NORMAL:
                    Log.e(TAG,"Normal_mode");
                    break;

            }
        }
        catch (Exception e)
        {

        }






//        mRingTone_10.stop();



        notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        mRingTone = RingtoneManager.getRingtone(this, notification);
        mRingTone.play();
        isnotification = true;
        editor = sharedPreferences.edit();
        editor.putBoolean("isItreceive", isItreceive);
        editor.commit();




        try
        {
            if(mAudioManager.getMode()==AudioManager.MODE_IN_CALL)
            {
                mRingTone.stop();
                if (vibrator.hasVibrator()){
                    vibrator.cancel();
                }

            }
        }
        catch (Exception e){}


        l_loud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (speakerToggle){
                    mAudioManager.setMode(AudioManager.MODE_NORMAL);
                    mAudioManager.setSpeakerphoneOn(false);
                    img_loud.setImageResource(R.drawable.ic_loud_v2);
                    t_loud.setTextColor(getResources().getColor(R.color.call_mute));
                    speakerToggle = false;
                }else{
                    mAudioManager.setMode(AudioManager.MODE_IN_CALL);
                    mAudioManager.setSpeakerphoneOn(true);
                    img_loud.setImageResource(R.drawable.ic_loud1_v2);
                    t_loud.setTextColor(getResources().getColor(R.color.colorPrimary));
                    speakerToggle = true;
                }
            }
        });




        fab_ans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                call_received=true;




                        LinphoneService.getInstance().pick_call();





            }
        });







        // UI v2
        fab_hangup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    Core core = null;
                    Call call = core.getCurrentCall();
                    call.terminate();
                }
                catch (Exception e){}


            }
        });


        fab_hangup_m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try
                {
                    Core core = null;
                    Call call =core.getCurrentCall();

                    call.terminate();
                }
                catch (Exception e)
                {

                }



            }
        });





        voiceBroadcastReceiver = new VoiceBroadcastReceiver();
        registerReceiver();

    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        try
        {
            if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)){
                //Do something to hide the view
                Log.e(TAG,"KeyPressed");
                mRingTone.stop();
//                mAudioManager.adjustStreamVolume(STREAM_RING,ADJUST_MUTE ,0);
                if (vibrator.hasVibrator()){
                    vibrator.cancel();
                }

                mAudioManager.adjustStreamVolume(
                        AudioManager.STREAM_VOICE_CALL,
                        AudioManager.ADJUST_LOWER,
                        AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
            }
            if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP)){
                //Do something to hide the view
                Log.e(TAG,"Up KeyPressed");
                mAudioManager.adjustStreamVolume(
                        AudioManager.STREAM_VOICE_CALL,
                        AudioManager.ADJUST_RAISE,
                        AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
            }
        }
        catch (Exception e)
        {

        }

        return true;
    }




























    private void registerReceiver() {

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_INCOMING_CALL_FINISHED);
        intentFilter.addAction(tw_NOTIFICATION);
        registerReceiver(voiceBroadcastReceiver, intentFilter);

    }
    public void UnregisterBroadcast(){
        unregisterReceiver(voiceBroadcastReceiver);
    }
    private static ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBoundServiceMessenger = null;
            mServiceConnected = false;
            Log.e(TAG, "onServiceDisconnected: ");
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBoundServiceMessenger = new Messenger(service);
            mServiceConnected = true;
            Log.e(TAG, "onServiceConnected: " );
        }
    };
    static class ActivityHandler extends Handler {
        private final WeakReference<IncomingActivity> mActivity;

        public ActivityHandler(IncomingActivity activity) {
            mActivity = new WeakReference<IncomingActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

            }
        }
    }
    private class VoiceBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.e(TAG_twilio, "onReceive: ccalled" );
            if (action.equals(ACTION_INCOMING_CALL_FINISHED)) {
               /*
                * Handle the incoming call invite
                */
               try
               {
                   Log.e(TAG, "onReceive: Called");
                   String actionis = intent.getStringExtra("actionname");
                   Log.e(TAG, "onReceive: "+actionis );

                   mcontext.finish();

//                   mcontext.unbindService(mServiceConnection);
               }
               catch (Exception e)
               {
                   Log.e(TAG,"Exception_receiver "+e.getMessage());
               }

                //handleIncomingCallIntent(intent);
            }


        }
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
    protected void onStart() {
        super.onStart();
        try
        {
            LocalBroadcastManager.getInstance(this).registerReceiver((broadcastReceiver),
                    new IntentFilter(IncomingActivity.COPA_RESULT)
            );
        }
        catch (Exception e)
        {

        }

        Log.e(TAG,"onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop: Called" );
        is_forward=false;
        is_forward_hold=false;
        try
        {
            if(broadcastReceiver != null) {
                LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
            }
        }
        catch (Exception e)
        {

        }

        try{if(dialog!=null){dialog.cancel();}}catch (Exception e){Log.e(TAG+"_states","excep_dia "+e.getMessage());}


    }




    public static boolean isActivityRunning() {
        try
        {
            return actvitiyRunning;

        }catch (Exception e)
        {
            return true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: Called" );

        try
        {
            editor = sharedPreferences.edit();
            editor.putString("tw_activecall_sid","");
            editor.putString("is_tw_calling","");
            editor.commit();
        }
        catch (Exception e){}



        UnregisterBroadcast();
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(notification_id);
        //if (!mCommManager.isIncomingCall()) {
//            mPulsator.stop();
            mRingTone.stop();
            try
            {
                if(vibrator.hasVibrator())
                {
                    vibrator.cancel();
                }
            }
            catch (Exception e)
            {

            }
//            stopService(new Intent(IncomingActivity.this,IncomingService.class));
            // Begin Stop Proximity sensor
            if(wakeLock.isHeld()) {
                wakeLock.release();
            }
            // End Stop Proximity sensor
            Log.e(TAG, "onDestroy: Finish acitivity and Stop sensor" );
            actvitiyRunning = false;



        try{taglist.clear();}catch (Exception e){}
        finish();

       // }
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG,"onResume");


        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(notification_id);

        try
        {
            NotificationManager manager2 = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager2.cancel(200);

            NotificationManager manager3 = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager3.cancel(210);

            NotificationManager manager4 = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager4.cancel(410);
        }
        catch (Exception e)
        {

        }




        try
        {
            LocalBroadcastManager.getInstance(this).registerReceiver((broadcastReceiver),
                    new IntentFilter(COPA_RESULT1)
            );
        }
        catch (Exception e){}
    }
    @Override
    public void onBackPressed() {
    }
































    private void requestAudioPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            //When permission is not granted by user, show them message why this permission is needed.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {
                Toast.makeText(this, "Please grant permissions to record audio", Toast.LENGTH_LONG).show();

                //Give user option to still opt-in the permissions
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_RECORD_AUDIO);

            } else {
                // Show user dialog to grant permission to record audio
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_RECORD_AUDIO);
            }
        }
        //If permission is granted, then go ahead recording audio
        else if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {

            //Go ahead with recording audio now
//            recordAudio();
        }
    }

    //Handling callback
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_RECORD_AUDIO: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
//                    recordAudio();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Permissions Denied to record audio", Toast.LENGTH_LONG).show();
                }
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                return;
            }
        }
    }




}
