package com.callhippo.bueno.callhippo.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.widget.Toast;

/**
 * Created by appit on 4/5/2017.
 */

public class InternetConnectionManager {
    private Context _context;

    public InternetConnectionManager(Context context) {
        this._context = context;
    }

    public boolean isConnectingToInternet() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ConnectivityManager connectivity = (ConnectivityManager) _context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        // if (isInternetWorking()) {
//                        Toast.makeText(_context, " Your Internet is working, Please try again", Toast.LENGTH_LONG).show();
                        return true;
                        // } else {

                        // toast.setGravity(Gravity.CENTER, 0, 0);
                        // toast.show();
                        // return false;
                        // }
                    }
            } else {
                // Toast toast = Toast.makeText(_context, " Please check your
                // internet connection and try again ", 100);
                // // toast.setGravity(Gravity.CENTER, 0, 0);
                // toast.show();
                Toast.makeText(_context, "Please check your internet connection and try again", Toast.LENGTH_LONG).show();
                return false;
            }
        }

         Toast toast = Toast.makeText(_context, " Please check your\n" + " internet connection and try again ", Toast.LENGTH_SHORT);
         toast.show();
        return false;
    }

//    public static boolean isInternetWorking() {
//        boolean success = false;
//        try {
//            URL url = new URL("https://google.com");
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setConnectTimeout(10000);
//            connection.connect();
//            success = connection.getResponseCode() == 200;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return success;
//    }

}