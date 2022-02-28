package com.callhippo.bueno.callhippo.Utils;

//import android.support.annotation.Nullable;
//import androidx.core.view.ViewPager;

import com.callhippo.bueno.callhippo.IntroActivity;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by appit on 9/4/2017.
 */

public class AppStaticContext {
    @Nullable
    public static ViewPager.PageTransformer pageTransformer;
    @Nullable
    public static IntroActivity.OnFragmentChangedListener onFragmentChangedListener;
    @Nullable
    public static IntroActivity.OnIntroductionFinishedListener onIntroductionFinishedListener;
}
