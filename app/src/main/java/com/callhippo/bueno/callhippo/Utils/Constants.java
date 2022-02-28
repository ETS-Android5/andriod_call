package com.callhippo.bueno.callhippo.Utils;

/**
 * Created by R Ankit on 25-12-2016.
 */

public class Constants {

    public static final String APP_INTRO = "app_intro";
    public static final String THREAD_ID = "thread_id";
    public static final String THREAD_NAME = "thread_name";
    public static final String CALLHIPPO_CONTACT_THREAD_POSITION = "callhippo_contact_position";
    public static final String THREAD_NUMBER = "thread_number";
    public static final String CH_NUMBER = "ch_number";
    public static final String CH_NUMBERID = "ch_numberId";
    public static final String All_MESSAGES = "all_thread_messages";
    public static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    public static String AUTH_TOKEN="";
    public static String USER_CONTACT_ID="";

    public static final String ACCOUNT_TYPE="com.callhippo.bueno.callhippo";
    public static final String ACCOUNT_NAME="Callhippo";
    public static final String USERCONTACT_ID = "usercontacid";
    public static final String USERUSER_ID = "usernumberid";
    public static final String USER_NAME = "username_name";
    public static final String USER_PHONENUMBER= "user_phonenumber";
    public static final String USER_COMPANY = "user_company";
    public static final String USER_EMAIL = "user_email";
    public static final String USER_CALLINGNUMBER = "user_callingnumber";
    public static final String SELECTED_POSITION = "selected_position";
    public static final String TDB_OUTCALL_COUNTRY = "tdb_outcallcountry";
    public static final String SH_IS_CONTACT_PERMISSION = "iscontactpermission";
    public static final String SH_IS_AUDIO_PERMISSION = "isaudiopermission";

    public static final String FRAGMENT_TAG_DIALER = "fragment_tag_dialer";
    public static final String FRAGMENT_TAG_ALLCALLS = "fragment_tag_allcalls";
    public static final String FRAGMENT_TAG_CONTACTS = "fragment_tag_contacts";
    public static final String FRAGMENT_TAG_SMS = "fragment_tag_sms";
    public static final String FRAGMENT_TAG_FEEDBACK = "fragment_tag_feedback";
    public static final String FRAGMENT_TAG_CREDIT = "fragment_tag_credit";

    public static String getAuthToken() {
        return AUTH_TOKEN;
    }

    public static void setAuthToken(String authToken) {
        AUTH_TOKEN = authToken;
    }


    public static String getUser_contact_id() {
        return USER_CONTACT_ID;
    }

    public static void setUser_contact_id(String USER_CONTACT_ID) {
        Constants.USER_CONTACT_ID = USER_CONTACT_ID;
    }
}