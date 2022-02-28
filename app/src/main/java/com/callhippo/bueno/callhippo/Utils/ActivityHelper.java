package com.callhippo.bueno.callhippo.Utils;

import android.app.Activity;
import android.content.pm.ActivityInfo;

/**
 * Created by Appitsimple11 on 28-03-2017.
 */

public class ActivityHelper {
    public static void initialize(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
