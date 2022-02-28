package com.callhippo.bueno.callhippo.Utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;

import com.callhippo.bueno.callhippo.R;

/**
 * Created by Appitsimple11 on 28-03-2017.
 */

public class Util {
    Context context;
//    private Dialog progressDialog = null;
    private ProgressBar progressBar;
    ProgressDialog progressDialog;

    public Util(Context context) {

        this.context = context;
    }
    public void showPopupProgressSpinner(Boolean isShowing) {
        if (isShowing == true) {
             progressDialog = ProgressDialog.show(context, null, null, true, false);
             progressDialog.setCancelable(false);
             progressDialog.getWindow()
                    .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
             progressDialog.setContentView(R.layout.popup_progressbar);
             progressDialog.show();
        } else if (isShowing == false) {
             progressDialog.dismiss();
        }
    }
}
