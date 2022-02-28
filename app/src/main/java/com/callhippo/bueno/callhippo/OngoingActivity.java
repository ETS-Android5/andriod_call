package com.callhippo.bueno.callhippo;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.os.Vibrator;
import android.provider.ContactsContract;
//import android.support.annotation.Nullable;
//import android.support.design.widget.*;
//import android.support.design.widget.FloatingActionButton;
import com.callhippo.bueno.callhippo.Utils.CustomautoCompleteTextview;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
//import androidx.core.content.LocalBroadcastManager;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.app.NotificationCompat;
//import android.support.v7.view.ContextThemeWrapper;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.callhippo.bueno.callhippo.Utils.ActivityHelper;
//import com.callhippo.bueno.callhippo.Utils.ContactsDatabase;
import com.callhippo.bueno.callhippo.Utils.InternetConnectionManager;
import com.callhippo.bueno.callhippo.Utils.OutgoingAnswered;
import com.callhippo.bueno.callhippo.Utils.SessionManagement_network_strength;
import com.callhippo.bueno.callhippo.service.LinphoneService;
import com.callhippo.bueno.callhippo.service.OngoingService;
import com.example.pulsator4droid.PulsatorLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

//import org.linphone.core.CallStats;
//import org.linphone.core.Core;
//import org.linphone.core.CoreListenerStub;
//import org.linphone.core.StreamType;

import org.linphone.core.Call;
import org.linphone.core.CallStats;
import org.linphone.core.Core;
import org.linphone.core.CoreListenerStub;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by Appitsimple11 on 25-03-2017.
 */

public class OngoingActivity extends AppCompatActivity implements OutgoingAnswered {
    public final static String TAG = "OngoingActivity";
    private NotificationCompat.Builder builder;
    private int notification_id;
    public boolean isnotification = false;
    private RemoteViews remoteViews;
    //    private CountDownTimer countDownTimer; // built in android class CountDownTimer
//    private long totalTimeCountInMilliseconds;
    TextView txtFromNumber, txtTonumber;
    ImageView btnMute, btnSpeaker, btnHangup, btnDialer, btn_hold;
    LinearLayout outgoinghanguplayout;
    Boolean speakerToggle = false, muteToggle = false, holdToggle = false, dialpadToggle = false,forwardToggle=false,recordToggle=false,NoteToggle = false;
    AudioManager mAudioManager;
    SharedPreferences sharedPreferences;
    private final String MY_PREFERANCES = "callhippomaulik";
    SharedPreferences.Editor editor;
    private PulsatorLayout mPulsator;
    Vibrator mVibrator;
    final int DURATION = 50; // Vibrate duration
    EditText phoneNumber;
    //Begin initialize Proximity Sensor Code
    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;
    private int field = 0x00000020;
    //End initialize Proximity Sensor Code
    private static final int NOTIFICATION_ID = 1;
    private static final String NOTIFICATION_CHANNEL_ID = "my_notification_channel";

    public static long totalTimeCountInMilliseconds;

    public static TextView textViewOutgoingtime,t_dash;
    int flag_ans = 0;

    InternetConnectionManager internetConnection;

    String dtmf_digit = "";
    private Dialog rankDialog;
    private RatingBar ratingBar;
    String is_rating_display = "";
    String is_rating_display_api = "";
    private String planName = "";
    SessionManagement_network_strength session_newwork;
    String network_status = "", network_speed = "", network_strength = "";
    String connection_msg = "";
    Snackbar snackbar;
    int ss = 0;

    String tonumber="";
    Boolean isHoldClick = false, isMute = false, isDialpad = false;
    private TextView txt_toname_v2, txt_tonumber_v2;
    private LinearLayout l_mute, l_hold, l_forward, l_loud, l_dialpad, l_hangup,l_record,l_notes,l_takenotes;

    ImageView img_mute, img_hold, img_loud, img_dialpad,img_forward,img_record,img_notes;
    TextView t_mute, t_hold, t_loud, t_dialpad,t_forward,t_notes,t_record;

    LinearLayout mHasButton, mOneButton, mTwoButton, mThreeButton, mFourButton, mFiveButton, mSixButton, mSevenButton, mEightButton, mNineButton, mStarButton, mZeroButton;

    ImageView back_dialpad;
    LinearLayout l_dialpad_top, l_main_calling;
    String call_ans="false";

    private CoreListenerStub mCoreListener;
    Boolean is_lp=false;
        Core core,core1;
        org.linphone.core.Call call_lp;
    private FirebaseAnalytics mFirebaseAnalytics;
    FloatingActionButton   fab_hangup;

    private int lastState = TelephonyManager.CALL_STATE_IDLE;
    String outgoing_number="",outgoing_number_display="";
    AlertDialog dialog = null;
    String call_duration="",depart_acw="",callername_acw="",to_acw="";

    LinearLayout l_lastcall;
    ImageView img_cross;
    String lastcall_setting="";

    ImageView img_lasttype;
    TextView t_lastname,t_laststatus,t_lastdate;


    LinearLayout l_subuser_list,l_tranfertype,l_switch_call,l_conf_call;
    String calltrasnfer_option="";

    private ArrayList<String> su_username;
    private ArrayList<String> su_userid;
    private ArrayList<String> su_available;

//    ListView lv_subusers;
    RecyclerView lv_subusers;

    LinearLayout dlg_l_blindtransfer,dlg_l_talktouser;
    TextView dlg_tr_uname,dlg_tr_talkto;
    ImageView dlg_back;

    LinearLayout dlg_btn_fromnumber,dlg_btn_tonumber,dlg_btn_transfer,dlg_btn_merge,dlg_btn_hangup;
    TextView txt_from_merge,txt_to_merge;
    ImageView img_from_merge,img_to_merge;

    public static LinearLayout dlg4_l_fromname,dlg4_l_toname;
    String fromCallerNumber="",toCallNumber="";
    public static String fromCallerNumber1="",toCallNumber1="";
    String x_from_tr_number="";
    private BroadcastReceiver broadcastReceiver;
    Boolean switch_layout_en=false;

    Boolean is_forward=false;
    CallStats state1;
    Boolean is_snackbar=false;
    Boolean start_network=false;
    Boolean is_acw_open=false;
    Boolean is_timer_start=false;
    Boolean is_rating_open=false;

    ImageView ic_back_sublist;
    String speed_from="",speed_to="";
    List<String> spd_array = new ArrayList<String>();
    Boolean f_spbar=true;


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

    List<String> low_spd_array = new ArrayList<String>();

    private VoiceBroadcastReceiver voiceBroadcastReceiver;
    public static final String ACTION_tw_outgoing_accept = "tw_call_accept_outgoing";
    private static final String TAG_twilio = "twilio_outgoing";
    String dtmf_api="";

    String x_ph_extensioncall="";
    public String last_call_ext="";
    ArrayList<String> taglist;
    Boolean isblindtranfer = false;
    private Context mcontext;
    CustomautoCompleteTextview autoCompleteTextView;
   String istagginginplan = "";
    public static PhoneNumberUtil phoneNumberUtil;
//    String recordingStatus_B = "";
//    String recordingStatus_C = "";
//    String Compliance_inPlan = "";

    public static CountDownTimer countDownTimer = new CountDownTimer(totalTimeCountInMilliseconds, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outgoing_new_calltag_v99);
        ActivityHelper.initialize(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                Window window = getWindow();
                int flag = window.getDecorView().getSystemUiVisibility();
                flag |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                window.setStatusBarColor(getResources().getColor(R.color.white));
                window.getDecorView().setSystemUiVisibility(flag);
            }
        } catch (Exception e) {

        }

        voiceBroadcastReceiver = new VoiceBroadcastReceiver();
        registerReceiver();
        try {
            core = LinphoneService.getCore();
        } catch (Exception e) {
        }

        mCoreListener = new CoreListenerStub() {
            @Override
            public void onCallStateChanged(Core lc, org.linphone.core.Call call, org.linphone.core.Call.State state, String message) {

                if (state == org.linphone.core.Call.State.Connected) {
                    call_ans = "true";
                    core1 = lc;
                    call_lp = call;
                    isHoldClick = true;
                    isMute = true;
                    isDialpad = true;

                    Log.e(TAG, "timer_start_isDialpad" + isDialpad);

                    try {
                        mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
                    } catch (Exception e) {
                    }
                    if (!is_timer_start) {
                        startTimer();
                    }
                    try {
                        if (sharedPreferences.getString("callnotesinplan", "").equalsIgnoreCase("true"))
                        {
                            Log.e(TAG,"callnotes_plivo");
                            l_takenotes.setVisibility(View.VISIBLE);
                            img_notes.setImageResource(R.drawable.notes_org);
                            t_notes.setTextColor(getResources().getColor(R.color.colorPrimary));
                            NoteToggle = true;
                        }
                    } catch (Resources.NotFoundException e) {
                        e.printStackTrace();
                    }





//                    startTimer();
//                                    Toast.makeText(OngoingActivity.this, "timer start", Toast.LENGTH_SHORT).show();
//                                    Thread.interrupted();
                    flag_ans = 1;
                }



                if (state == org.linphone.core.Call.State.End) {
                    // Once call is finished (end state), terminate the activity
                    // We also check for released state (called a few seconds later) just in case
                    // we missed the first one

                    Log.e(TAG, "call_end " + outgoing_number);

                    editor = sharedPreferences.edit();
                    editor.putString("outgoing_number_reminder", outgoing_number);
                    editor.putString("outgoing_number_reminder_display", outgoing_number_display);
                    editor.putString("networkStrengthRandomString_reminder", "");
                    editor.commit();

                }

                if (state == org.linphone.core.Call.State.Released) {
                    // Once call is finished (end state), terminate the activity
                    // We also check for released state (called a few seconds later) just in case
                    // we missed the first one

                    Log.e(TAG, "call_release " + outgoing_number);

                    editor = sharedPreferences.edit();
                    editor.putString("outgoing_number_reminder", outgoing_number);
                    editor.putString("outgoing_number_reminder_display", outgoing_number_display);
                    editor.putString("networkStrengthRandomString_reminder", "");
                    editor.commit();

                    String discription = String.valueOf(autoCompleteTextView.getText());

                    try {
                        if (!txt_toname_v2.getText().toString().equalsIgnoreCase(""))
                        {
                            callername_acw = txt_toname_v2.getText().toString();
                        }

                    } catch (Exception e) {

                    }


                    finish();
                }
            }
        };

        mcontext = getApplicationContext();
        sharedPreferences = getSharedPreferences(MY_PREFERANCES, MODE_PRIVATE);
        internetConnection = new InternetConnectionManager(this);
        taglist =new ArrayList<String>();


        txt_typed_number = findViewById(R.id.txt_typed_numbers);
        l_newcontact = findViewById(R.id.l_new_contact);
        tt_contact = findViewById(R.id.txt_contact);
        tt_subuser = findViewById(R.id.txt_highlight_detail);
        tt_subuser.setTextColor(getResources().getColor(R.color.colorPrimary));

        edt_searchtext = findViewById(R.id.edt_srch);
        l_history_tab = findViewById(R.id.l_history_tab);
        l_subuser_tab = findViewById(R.id.l_detail_tab);

        lv_subusers = findViewById(R.id.list_subusers);
        list_tr_contacts = findViewById(R.id.list_subusers_contacts);
//        db = new ContactsDatabase(OngoingActivity.this);
//        contacts = db.getAllContacts();
        Gson gson = new Gson();
        String json = sharedPreferences.getString("tagsmodellist", "");
        autoCompleteTextView = findViewById(R.id.edt_feedback);





        if (lv_subusers.getVisibility() == View.VISIBLE) {
            edt_searchtext.setHint("Search Sub-user");
        } else if (lv_subusers.getVisibility() == View.VISIBLE) {
            edt_searchtext.setHint("Search Contact or add number");
        }






        try {
            registerReceiver1();
        } catch (Exception e) {
            Log.e(TAG, "registerReceiver1 " + e.getMessage());
        }




        //Begin initialize Proximity Sensor Code
        try {
            // Yeah, this is hidden field.
            field = PowerManager.class.getClass().getField("PROXIMITY_SCREEN_OFF_WAKE_LOCK").getInt(null);
        } catch (Throwable ignored) {
        }
//        sharedPreferences = getSharedPreferences(MY_PREFERANCES, MODE_PRIVATE);
        dtmf_api = sharedPreferences.getString("DTMFByApi", "");



        l_lastcall = findViewById(R.id.l_lastcall);
        img_cross = findViewById(R.id.img_close);

        img_lasttype = findViewById(R.id.img_lasttype);
        t_lastname = findViewById(R.id.txt_lastname);
        t_lastdate = findViewById(R.id.txt_lasttime);
        t_laststatus = findViewById(R.id.txt_laststatus);

        lastcall_setting = sharedPreferences.getString("last_calldetail_setting", "");



        powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(field, getLocalClassName());
        //End initialize Proximity Sensor Code
        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        remoteViews = new RemoteViews(getPackageName(), R.layout.call_notification);
        totalTimeCountInMilliseconds = 60 * 500 * 1000;
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.setMode(AudioManager.MODE_NORMAL);
        mAudioManager.setSpeakerphoneOn(false);
        isnotification = true;
//        mPulsator = (PulsatorLayout) findViewById(R.id.pulsatoroutgoing);

//        txtTonumber = (TextView) findViewById(R.id.textViewOutgoingtonumber);
        txtFromNumber = (TextView) findViewById(R.id.textviewoutgoingfromNumber);
        textViewOutgoingtime = (TextView) findViewById(R.id.textViewOutgoingtime);
        t_dash = findViewById(R.id.dash);
//        outgoinghanguplayout = (LinearLayout) findViewById(R.id.outgoinghanguplayout);
//        btnHangup = (ImageView) findViewById(R.id.btnOutgoingHangup);
//        btnMute = (ImageView) findViewById(R.id.outgoingMute);
//        btn_hold = (ImageView) findViewById(R.id.outgoingHold);
//        btnSpeaker = (ImageView) findViewById(R.id.outgoingSpeaker);
//        btnDialer = (ImageView) findViewById(R.id.outgoingdialer);
        try {
            startService(new Intent(OngoingActivity.this, OngoingService.class));
        } catch (Exception e) {
        }
        tonumber = sharedPreferences.getString("tonumber", "");
        is_rating_display = sharedPreferences.getString("Rating_checkbox", "");
        is_rating_display_api = sharedPreferences.getString("set_call_rating", "");
        planName = sharedPreferences.getString("displayname", "");
        calltrasnfer_option = sharedPreferences.getString("callTransfer", "");



        if (tonumber.charAt(0) != '+') {
            tonumber = "+" + tonumber;
        }
        outgoing_number = tonumber;
        outgoing_number_display = tonumber;
        Log.e(TAG, "tonumber_ou:" + outgoing_number);

        to_acw = tonumber;

        x_from_tr_number = tonumber;


        try {
            String is_lp1 = sharedPreferences.getString("is_lp", "");

            if (is_lp1.equalsIgnoreCase("true")) {
                is_lp = true;
            } else if (is_lp1.equalsIgnoreCase("false")) {
                is_lp = false;
            }
            Log.e(TAG, "is_lp_flag " + is_lp);


        } catch (Exception e) {
        }


        txt_toname_v2 = (TextView) findViewById(R.id.txt_toname);
        txt_tonumber_v2 = (TextView) findViewById(R.id.txt_tonumber);



        l_mute = (LinearLayout) findViewById(R.id.l_mute);
        l_hold = (LinearLayout) findViewById(R.id.l_hold);
        l_forward = (LinearLayout) findViewById(R.id.l_forward);
        l_loud = (LinearLayout) findViewById(R.id.l_loud);
        l_dialpad = (LinearLayout) findViewById(R.id.l_dialpad);
        l_hangup = (LinearLayout) findViewById(R.id.l_hangup);
        l_record = (LinearLayout) findViewById(R.id.l_record);
        l_notes = findViewById(R.id.l_notes_v1);
        l_takenotes =findViewById(R.id.takenotes);


        img_mute = (ImageView) findViewById(R.id.ic_mute);
        img_hold = (ImageView) findViewById(R.id.ic_hold);
        img_loud = (ImageView) findViewById(R.id.ic_loud);
        img_dialpad = (ImageView) findViewById(R.id.ic_dialer);
        img_forward = (ImageView) findViewById(R.id.ic_forward);
        img_record = findViewById(R.id.ic_record);
        img_notes = findViewById(R.id.ic_notes);

        t_mute = (TextView) findViewById(R.id.tx_mute);
        t_hold = (TextView) findViewById(R.id.tx_hold);
        t_loud = (TextView) findViewById(R.id.tx_loud);
        t_dialpad = (TextView) findViewById(R.id.tx_dialer);
        t_forward = (TextView) findViewById(R.id.tx_forward);
        t_record = findViewById(R.id.tx_record);
        t_notes = findViewById(R.id.tx_notes);

        phoneNumber = (EditText) findViewById(R.id.phoneNumberbm);
        back_dialpad = (ImageView) findViewById(R.id.img_back_dialpad);

        l_main_calling = (LinearLayout) findViewById(R.id.top_layout);
        l_dialpad_top = (LinearLayout) findViewById(R.id.l_dialpad_top);

        l_subuser_list = (LinearLayout) findViewById(R.id.l_subuser_listing);
        l_tranfertype = (LinearLayout) findViewById(R.id.transfer_call_layout);
        l_switch_call = (LinearLayout) findViewById(R.id.switch_call_layout);
        l_conf_call = findViewById(R.id.conference_call_layout);

        lv_subusers = findViewById(R.id.list_subusers);
        ic_back_sublist = findViewById(R.id.icon_back_subuser);

//        l_forward.setEnabled(false);

        fab_hangup = (FloatingActionButton) findViewById(R.id.fabHangup);
        su_username = new ArrayList<>();
        su_userid = new ArrayList<>();
        su_available = new ArrayList<>();




        try {
            // to clear number on dilaer input edittext
            editor = sharedPreferences.edit();
            editor.putString("typed_number_dialer", "");
            editor.commit();
        } catch (Exception e) {

        }

        if (sharedPreferences.getString("last_call_ext", "").equalsIgnoreCase("true")) {
            l_forward.setEnabled(false);
        } else {
            l_forward.setEnabled(true);
        }


        Log.e(TAG, "is_rating_display_api: " + is_rating_display_api);
        Log.e(TAG, "To Number: " + tonumber + ":From_number " + sharedPreferences.getString("fromnumber", ""));

        speed_from = sharedPreferences.getString("fromnumber", "");
        speed_to = tonumber;





        tonumber = tonumber.replaceAll("[\\D]", "");








        // start pulsator
//        mPulsator.start();
        //Begin When call receive Proximity sensor on
        if (!wakeLock.isHeld()) {
            wakeLock.acquire();
        }
        fab_hangup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Call call = core.getCurrentCall();
                call.terminate();



            }
        });









        l_loud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (speakerToggle) {
                    mAudioManager.setMode(AudioManager.MODE_NORMAL);
                    mAudioManager.setSpeakerphoneOn(false);
                    img_loud.setImageResource(R.drawable.ic_loud_v2);
                    t_loud.setTextColor(getResources().getColor(R.color.call_mute));
                    speakerToggle = false;
                } else {
                    mAudioManager.setMode(AudioManager.MODE_IN_CALL);
                    mAudioManager.setSpeakerphoneOn(true);
                    img_loud.setImageResource(R.drawable.ic_loud1_v2);
                    t_loud.setTextColor(getResources().getColor(R.color.colorPrimary));
                    speakerToggle = true;
                }
            }
        });


//    }
//        startTimer();


        //to fix timer issue(timer should start when call is picked up)
        Thread t = new Thread() {
            @Override
            public void run() {
                while (!isInterrupted()) {
                    try {
                        Thread.sleep(500);  //1000ms = 1 sec
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {




                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (flag_ans == 1) {
                        Thread.interrupted();
                    }

                }
            }
        };

        t.start();


    }







    private void keyPressed(int keyCode) {
        mVibrator.vibrate(DURATION);
        KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
        phoneNumber.onKeyDown(keyCode, event);
//        Button b = (Button)v;
//        b.setBackground(getResources().getDrawable(R.drawable.circle_outline_press));
//        b.setTextColor(getResources().getColor(R.color.colorwhite));

    }

    private void startTimer() {

        Log.e(TAG,"timer_start");
        is_timer_start=true;

//        isHoldClick = true;
//        isMute = true;
//        isDialpad = true;

//        Log.e(TAG,String.valueOf(isHoldClick));
        textViewOutgoingtime.setVisibility(View.VISIBLE);
        t_dash.setVisibility(View.VISIBLE);
        countDownTimer = new CountDownTimer(totalTimeCountInMilliseconds, 1000) {
            // 500 means, onTick function will be called at every 500
            // milliseconds
            long mincount = 0;

            @Override
            public void onTick(long leftTimeInMilliseconds) {
                long seconds = leftTimeInMilliseconds / 1000;

                long sec = 60 - (seconds % 60);
                if (sec == 60) {
                    mincount++;
                    sec = 00;
                    textViewOutgoingtime.setText(String.format("%02d", mincount)
                            + ":" + String.format("%02d", sec));
                    call_duration=textViewOutgoingtime.getText().toString();
                } else {
                    textViewOutgoingtime.setText(String.format("%02d", mincount)
                            + ":" + String.format("%02d", sec));
                    call_duration=textViewOutgoingtime.getText().toString();
                }
                // format the textview to show the easily readable format





            }

            @Override
            public void onFinish() {
                // this function will be called when the timecount is finished
                textViewOutgoingtime.setText("Call Finished");
                textViewOutgoingtime.setVisibility(View.VISIBLE);
                t_dash.setVisibility(View.VISIBLE);
            }

        }.start();

    }

    @Override
    protected void onStop() {
        super.onStop();
        is_forward=false;
        if (isnotification) {
        }

        try
        {
            if(dialog!=null)
            {
                dialog.cancel();
            }
        }
        catch (Exception e)
        {

        }


        try
        {
            if(broadcastReceiver != null) {
                LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
            }
        }
        catch (Exception e)
        {

        }
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try{mAudioManager.setMode(AudioManager.MODE_NORMAL);}catch (Exception e){}

        try{LinphoneService.getCore().removeListener(mCoreListener);}catch (Exception e){}

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(notification_id);
        //if (!mCommManager.isOutgoingCall()) {

//            Toast.makeText(OngoingActivity.this, "timer stop", Toast.LENGTH_SHORT).show();
        countDownTimer.cancel();
        finish();
        // Begin Stop Proximity sensor
        if (wakeLock.isHeld()) {
            wakeLock.release();
        }
        // End Stop Proximity sensor
        stopService(new Intent(OngoingActivity.this, OngoingService.class));
        // }
        editor = sharedPreferences.edit();
        editor.putString("logfromnumber", "");
        editor.putString("logfromname", "");
        editor.putString("is_tw_calling","");
        editor.commit();

//        try
//        {
//            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
//            telephonyManager.listen(myPhoneStateChangeListener1, PhoneStateListener.LISTEN_NONE);
//        }
//        catch (Exception e)
//        {
//
//        }

        try{UnregisterBroadcast();}catch (Exception e){}
        taglist.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(notification_id);

        try{LinphoneService.getCore().addListener(mCoreListener);}catch (Exception e){}

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



    @Override
    public void onAnswered() {

    }






































    private void registerReceiver1() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String transfer_button_msg = intent.getStringExtra(IncomingActivity.COPA_MESSAGE);
                contact_search = intent.getStringExtra(IncomingActivity.COPA_MESSAGE1);
                Log.e(TAG,"contact_search_result:"+contact_search);

                if(contact_search!=null)
                {
                    if(contact_search.equalsIgnoreCase("no_contact_found"))
                    {
                        String digit=intent.getStringExtra(COPA_MESSAGE2);

                        l_newcontact.setVisibility(View.VISIBLE);
                        list_tr_contacts.setVisibility(View.GONE);

                        if(digit.matches("[0-9]+") || digit.charAt(0)=='+')
                        {
                            txt_typed_number.setText(digit);
                            new_number=digit;

                        }
                        else
                        {
                            l_newcontact.setVisibility(View.GONE);
                        }

                    }
                    else
                    {
                        l_newcontact.setVisibility(View.GONE);
                        list_tr_contacts.setVisibility(View.VISIBLE);
                        txt_typed_number.setText("");
                    }
                }



                /*
                 * Step 3: We can update the UI of the activity here
                 * */

                // this method is use to hide transfer button when we get notification {"transferButtonNotify"="false"}
                Log.e(TAG,"broadcastReceiver_msg "+transfer_button_msg);



            }
        };
    }










    private class VoiceBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_tw_outgoing_accept))
            {
            }

        }
    }

    private void registerReceiver() {

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_tw_outgoing_accept);
        registerReceiver(voiceBroadcastReceiver, intentFilter);

    }
    public void UnregisterBroadcast(){
        unregisterReceiver(voiceBroadcastReceiver);
    }









}

