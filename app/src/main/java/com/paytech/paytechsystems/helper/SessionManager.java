package com.paytech.paytechsystems.helper;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.paytech.paytechsystems.getset.User;

import org.json.JSONException;
import org.json.JSONObject;

public class SessionManager {
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    public static final String PREF_NAME = "Paytech";

    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    private static final String KEY_NOTIFICATIONS = "notifications";
    private static final String KEY_SESSION_ID = "sessionId", FLAG_MESSAGE = "message";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);

        editor = pref.edit();
    }

    public void storeUserPref(User user) {
        editor.putInt(User.USER_ID, user.getId());
        editor.putString(User.USER_FNAME, user.getFname());
        editor.putString(User.USER_SNAME, user.getSname());
        editor.putString(User.USER_UNAME, user.getUname());
        editor.putString(User.USER_EMAIL, user.getEmail());
        editor.commit();

        Log.e(TAG, "User is stored in shared preferences. " + user.getSname() + ", " + user.getEmail());
    }

    public User getUser() {
        if (pref.getString(User.USER_UNAME, null) != null) {
            String uid, fname, sname, uname, email, phone,idno, password, branch, gender, role;
            Integer id, status;
            id = pref.getInt(User.USER_ID, 0);
            fname = pref.getString(User.USER_FNAME, null);
            sname = pref.getString(User.USER_SNAME, null);
            uname = pref.getString(User.USER_UNAME, null);
            email = pref.getString(User.USER_EMAIL, null);
//            phone = pref.getString(KEY_USER_EMAIL, null);
//            idno = pref.getString(KEY_USER_IDNO, null);
//            phone = pref.getString(KEY_USER_PHONE, null);
//            password = pref.getString(KEY_USER_PWD, null);
//            branch = pref.getString(KEY_USER_BRANCH, null);
//            gender = pref.getString(KEY_USER_GENDER, null);
//            role = pref.getString(KEY_USER_ROLE, null);
//            status = pref.getInt(KEY_USER_STATUS, null);
            //User user= new User(id, idno, fname, sname, uname, email, phone, password, branch, gender, role, status);

            User user = new User(id, fname, sname, uname, email);
            return user;
        }
        return null;
    }

    public void addNotification(String notification) {
 
        // get old notifications
        String oldNotifications = getNotifications();
 
        if (oldNotifications != null) {
            oldNotifications += "|" + notification;
        } else {
            oldNotifications = notification;
        }
 
        editor.putString(KEY_NOTIFICATIONS, oldNotifications);
        editor.commit();
    }

    public String getNotifications() {
        return pref.getString(KEY_NOTIFICATIONS, null);
    }

    public void clear() {
        editor.clear();
        editor.commit();
    }

    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(Config.IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }
 
    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(Config.IS_FIRST_TIME_LAUNCH, true);
    }


    private Context context;
    private SharedPreferences sharedPref;



    public void storeSessionId(String sessionId) {
        Editor editor = pref.edit();
        editor.putString(KEY_SESSION_ID, sessionId);
        editor.commit();
    }

    public String getSessionId() {
        return pref.getString(KEY_SESSION_ID, null);
    }

    public String getSendMessageJSON(String message) {
        String json = null;

        try {
            JSONObject jObj = new JSONObject();
            jObj.put("flag", FLAG_MESSAGE);
            jObj.put("sessionId", getSessionId());
            jObj.put("message", message);

            json = jObj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }

}