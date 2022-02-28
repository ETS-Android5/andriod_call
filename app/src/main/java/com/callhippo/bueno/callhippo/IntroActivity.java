package com.callhippo.bueno.callhippo;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
//import android.support.annotation.CallSuper;
//import androidx.core.app.Fragment;
//import androidx.core.app.FragmentManager;
//import androidx.core.app.FragmentStatePagerAdapter;
import androidx.annotation.CallSuper;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
//import androidx.core.view.ViewPager;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.callhippo.bueno.callhippo.Utils.ActivityHelper;
import com.callhippo.bueno.callhippo.Utils.AppStaticContext;
import com.callhippo.bueno.callhippo.Utils.Constants;

import java.util.ArrayList;

public class IntroActivity extends AppCompatActivity {

    private static final String TAG = "IntroActivity";
    ViewPager pager;
    pagerAdap adap;
    ImageView imgcountry, imgfeatures, imgsupport, imghome;
    private ImageButton nextButton;
    private ImageButton prevButton;
    TextView btngetstatrted;
    private OnIntroductionFinishedListener onIntroductionFinishedListener;
    private OnFragmentChangedListener onFragmentChangedListener;
    private ViewPager.PageTransformer pageTransformer;
    private int lastPosition = 0;
    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor editor;
    private final String  MY_PREFERANCES = "callhippomaulik";

    public ArrayList<String> mNames= new ArrayList<>();
    public ArrayList<String> mNumbers=null ;
    public ArrayList<String> mNumbers_v1=new ArrayList<>() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHelper.initialize(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_intro);
        sharedPreferences = getSharedPreferences(MY_PREFERANCES,MODE_PRIVATE);
        pager = (ViewPager) findViewById(R.id.pager);
        nextButton = (ImageButton) findViewById(R.id.nextButton);
        prevButton = (ImageButton) findViewById(R.id.prevButton);
        btngetstatrted = (TextView) findViewById(R.id.btngetstatrted);

        adap = new pagerAdap(getSupportFragmentManager());
        initialize();
        pager.setAdapter(adap);
        pager.setPageTransformer(false,pageTransformer);

        try {
            editor = sharedPreferences.edit();
            editor.putString("appOnboard","true");
            editor.commit();
        } catch (Exception e) { }

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    getNames();
//                } catch (Exception e) {
//                    Log.e(TAG,"getnamecalled_num_exp "+e.getMessage());
//                }
//                try {
//                    String name, number;
//                    for (int i = 0; i < mNames.size(); i++) {
//                        //   Log.e(TAG,"getnamecalled2222");
//                        name = mNames.get(i);
//                        number=mNumbers_v1.get(i);
//                        String tm = number.replace("[","").replace("]","");
//                        String first = tm.split(",")[0];
//                        // Log.e(TAG,"getnamecalled_num "+first +" name "+name);
//                        ContactManager.addContact(IntroActivity.this, new MyContact(name,first));
//                    }
//                } catch (Exception e) {
//                    Log.e(TAG,"exp_33 " +e.getMessage());
//                }
//            }
//        }).start();


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNextButtonClick();
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPrevButtonClick();
            }
        });
        btngetstatrted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editor = sharedPreferences.edit();
                editor.putString(Constants.APP_INTRO,"NotShown");
                editor.commit();
                Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                startActivity(intent);
                finish();
                //when intro finish
            }
        });
        final int[] colors = {R.drawable.gradient_one, R.drawable.gradient_one, R.drawable.gradient_one,R.drawable.gradient_one};
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position < (adap.getCount() - 1) && position < (colors.length - 1)) {
                    pager.setBackgroundResource(colors[position]);
                } else {
                    pager.setBackgroundResource(colors[colors.length - 1]);
                }
            }
            @Override
            public void onPageSelected(int position) {
                updateProgressBar();

                if (position == 0) {
                    toggleBackButtonVisibility(false);
                } else if (position == 1 && lastPosition == 0) {
                    // we dont want to update it every time, for future animation.
                    toggleBackButtonVisibility(true);
                }
                if (position == (4 - 1)) {
                    toggleFinishIcon(true);
                } else {
                    toggleFinishIcon(false);
                }
                if (position != lastPosition) {
                    if (position < lastPosition) {
                        performOnSlideListener(getApplicationContext(), position, position - 1);
                    } else {
                        performOnSlideListener(getApplicationContext(), position, position + 1);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        pager.setPageTransformer(true, new ViewPager.PageTransformer() {
                    @Override
                    public void transformPage(View view, float position) {

                        imghome = (ImageView) view.findViewById(R.id.imghome);
                        imgcountry = (ImageView) view.findViewById(R.id.imgcountry);
                        imgfeatures = (ImageView) view.findViewById(R.id.imgfeatures);
                        imgsupport = (ImageView) view.findViewById(R.id.imgsupport);
//                        img5 = (ImageView) view.findViewById(R.id.img5);
//                        img6 = (ImageView) view.findViewById(R.id.img6);
                       // img7 = (ImageView) view.findViewById(R.id.img7);
                       // img8 = (ImageView) view.findViewById(R.id.img8);



                        int pageWidth = view.getWidth();
                        int pageHeight = view.getHeight();

                        float ratio = (float) pageWidth / pageHeight;

                        Log.d(TAG, "val " + ratio);
                        if (position < -1) { // [-Infinity,-1)
                            // This page is way off-screen to the left.
                        } else if (position <= 1) { // [-1,1]

                            if (position > 0 && position <= 1) {
                                if (imgcountry != null) {
                                    imgcountry.setTranslationX(pageWidth * (position) / 8);
                                    imgcountry.setTranslationY(pageWidth * (position) / 8);
                                    Log.e(TAG, "Country: "+position );
                                }
                                if (imgfeatures != null) {
//                                    img5.setTranslationX(pageWidth * (1 - position) / 40);
//                                    img5.setTranslationY(pageWidth * (1 - position) / 40);
                                    imgfeatures.setTranslationX(pageWidth *  (position) / 12);
                                    imgfeatures.setTranslationY(pageWidth *  (position) / 12);
                                    Log.e(TAG, "Features: "+position );
                                    //imgfeatures.setRotation(-6 * (1 - position));
//                                    img5.setRotation(-2 * (1 - position));
                                }
                                if (imgsupport != null) {
//                                    img5.setTranslationX(pageWidth * (1 - position) / 40);
//                                    img5.setTranslationY(pageWidth * (1 - position) / 40);
                                    imgsupport.setTranslationX(pageWidth *  (position) / 12);
                                    imgsupport.setTranslationY(pageWidth *  (position) / 12);
                                    Log.e(TAG, "Features: "+position );
                                    //imgfeatures.setRotation(-6 * (1 - position));
//                                    img5.setRotation(-2 * (1 - position));
                                }
                            } else if (position <= 0 && position > -1) {

                                if (imgcountry != null) {
//                                    position = -position;
                                    Log.e(TAG, "Country2: "+position );
                                    imgcountry.setTranslationX(pageWidth * (position) / 4);
                                    imgcountry.setTranslationY(pageWidth * (position) / 4);
                                    imgcountry.setRotation(2 * (position * 10));
//                                    img2.setTranslationX(pageWidth * (1 - position) / 8 * ratio);
//                                    img3.setTranslationY(pageWidth * (1 - position) / 3 * ratio);
                                }
                                if (imgfeatures != null) {
//                                    position = -position;
                                    Log.e(TAG, "features: "+position );
                                    imgfeatures.setTranslationX(pageWidth * (position) / 4);
                                    imgfeatures.setTranslationY(pageWidth * (position) / 4);
                                    imgfeatures.setRotation(2 * (position * 10));
//                                    img2.setTranslationX(pageWidth * (1 - position) / 8 * ratio);
//                                    img3.setTranslationY(pageWidth * (1 - position) / 3 * ratio);
                                }
                                if (imgsupport != null) {
//                                    position = -position;
                                    Log.e(TAG, "support: "+position );
                                    imgsupport.setTranslationX(pageWidth * (position) / 4);
                                    imgsupport.setTranslationY(pageWidth * (position) / 4);
                                    imgsupport.setRotation(2 * (position * 10));
//                                    img2.setTranslationX(pageWidth * (1 - position) / 8 * ratio);
//                                    img3.setTranslationY(pageWidth * (1 - position) / 3 * ratio);
                                }
                                if ( imghome != null) {
                                    imghome.setTranslationY(pageWidth * (position) / 4);
//                                    img8.setTranslationY(pageWidth * (position) / 3);
//                                    img7.setTranslationY(pageWidth * (position) / 4);
                                    imghome.setTranslationX(pageWidth * (position) / 4);
//                                    img8.setTranslationX(pageWidth * (position) / 3);
//                                    img7.setTranslationX(pageWidth * (position) / 4);
                                    imghome.setRotation(2 * (position * 10));
                                    Log.e(TAG, "home: "+position );
//                                    img8.setRotation(2 * (position * 10));
                                }
                            }
                        } else {
                            // (1,+Infinity]
                            // This page is way off-screen to the right.
                        }
                    }
                }
        );

        updateProgressBar();
    }
    @CallSuper
    public void initialize() {
        if (AppStaticContext.onFragmentChangedListener != null) {
            onFragmentChangedListener = AppStaticContext.onFragmentChangedListener;
        }
        if (AppStaticContext.onIntroductionFinishedListener != null) {
            onIntroductionFinishedListener = AppStaticContext.onIntroductionFinishedListener;
        }
        if (AppStaticContext.pageTransformer != null) {
            pageTransformer = AppStaticContext.pageTransformer;
        }
    }
    public void onNextButtonClick() {
        if (pager.getCurrentItem() == pager.getAdapter().getCount() - 1) {
            // finish
            performOnIntroductionFinished();
        } else {
            pager.setCurrentItem(pager.getCurrentItem() + 1, true);
            performOnSlideListener(getApplicationContext(), pager.getCurrentItem(), pager.getCurrentItem() + 1);
        }
    }
    public void onPrevButtonClick() {
        if (pager.getCurrentItem() == 0) {
            // no back
        } else {
            pager.setCurrentItem(pager.getCurrentItem() - 1, true);
            performOnSlideListener(getApplicationContext(), pager.getCurrentItem(), pager.getCurrentItem() - 1);
        }
    }
    private void toggleBackButtonVisibility(boolean visible) {
        ObjectAnimator objectAnimator = new ObjectAnimator().ofFloat(prevButton, View.ALPHA, visible ? 1f : 0f);
        objectAnimator.setDuration(500);
        objectAnimator.start();
    }
    private void toggleFinishIcon(final boolean isFinish) {
        final ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(nextButton, View.ROTATION, isFinish ? 360 : 0);
        objectAnimator.setDuration(500);
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        nextButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), isFinish ? R.drawable.ic_done_small_select : R.drawable.ic_keyboard_arrow_right_white_24dp));
                    }
                }, objectAnimator.getDuration() / 2);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });


        objectAnimator.start();
    }
    public void performOnIntroductionFinished() {
        if (onIntroductionFinishedListener == null) {
            editor = sharedPreferences.edit();
            editor.putString(Constants.APP_INTRO,"NotShown");
            editor.commit();
            Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
            startActivity(intent);
            finish();

            // when intro finish
        } else {
            onIntroductionFinishedListener.OnIntroductionFinished(this);
        }
    }

    private void performOnSlideListener(Context context, int from, int to) {
        if (onFragmentChangedListener != null) {
            onFragmentChangedListener.onFragmentChangedListener(context, from, to);
        }
    }
    public void updateProgressBar() {
        createProgressViews(pager.getCurrentItem(), 4);
    }
    private void createProgressViews(int pos, int max) {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        linearLayout.removeAllViewsInLayout();
        for (int i = 0; i < max; i++) {
            ImageView dotImageView = new ImageView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.CENTER;
            layoutParams.setMargins(5, 0, 5, 0);
            dotImageView.setLayoutParams(layoutParams);

            if (i == pos) {
                dotImageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.progress_dot_selected));
            } else {
                dotImageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.progress_dot_unselected));
            }

            linearLayout.addView(dotImageView, i);
        }
    }
    public class pagerAdap extends FragmentStatePagerAdapter {

        ArrayList<Fragment> fragments = new ArrayList<>();

        public pagerAdap(FragmentManager fm) {
            super(fm);
        }

        void addFrag(Fragment fragment) {
            fragments.add(fragment);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
    public interface OnIntroductionFinishedListener {
        /**
         * On introduction finished method.
         * Default:
         * <code>activity.finish();</code>
         *
         * @param activity the activity
         */
        void OnIntroductionFinished(Activity activity);
    }
    public interface OnFragmentChangedListener {
        /**
         * This event called when the fragment change in the XIntro activity.
         *
         * @param context the context of the activity.
         * @param from    the from position
         * @param to      the to position.
         */
        void onFragmentChangedListener(Context context, int from, int to);
    }
//    private void getNames(){
//        Log.e(TAG,"getnamecalled222");
//        int hasPhone;
//        Cursor c = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
//                null,null,null,null);
//        try {
//            if((c != null) && c.moveToFirst())
//            {
//                while(c.moveToNext())
//                {
//                    mNumbers = new ArrayList<String>();
//                    String id = c.getString(
//                            c.getColumnIndex(ContactsContract.Contacts._ID));
//                    //   Log.e(TAG,"getnamecalled_id "+id);
//                    mNames.add(c.getString(
//                            c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME)));
//                    hasPhone = Integer.parseInt(c.getString(
//                            c.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
//                    String ph="";
//                    if(hasPhone > 0)
//                    {
//                        //Log.e(TAG,"getnamecalled_has "+hasPhone);
//
//                        Cursor pCur = getContentResolver().query(
//                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                                null,
//                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
//                                new String[]{id}, null);
//                        //   Log.e(TAG,"getnamecalled_cr "+pCur);
//                        while (pCur.moveToNext())
//                        {
//                            //                    mNames.add(c.getString(
//                            //                            c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME)));
//                            ph=pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                            //  Log.e(TAG,"getnamecalled_ph "+ph);
//                            mNumbers.add(ph);
//                        }
//                        mNumbers_v1.add(String.valueOf(mNumbers));
//                        pCur.close();
//                    }
////                c.close();
//                }
//            }
//        } catch (Exception e) {
//            Log.e(TAG,"getnamecalled_exp "+e.getMessage());
//        }
//    }
}
