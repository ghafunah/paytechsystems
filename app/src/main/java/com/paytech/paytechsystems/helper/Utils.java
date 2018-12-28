 package com.paytech.paytechsystems.helper;
 
 import org.json.JSONException;
 import org.json.JSONObject;
 
 import android.content.Context;
 import android.content.SharedPreferences;
 import android.content.SharedPreferences.Editor;
 import android.graphics.PorterDuff;
 import android.graphics.drawable.Drawable;
 import android.support.v4.content.ContextCompat;
 import android.view.MenuItem;

 public class Utils {
 
     private Context context;
     private SharedPreferences sharedPref;
 
     private static final String KEY_SHARED_PREF = "ANDROID_WEB_CHAT";
     private static final int KEY_MODE_PRIVATE = 0;
     private static final String KEY_SESSION_ID = "sessionId",
             FLAG_MESSAGE = "message";
 
     public Utils(Context context) {
         this.context = context;
         sharedPref = this.context.getSharedPreferences(KEY_SHARED_PREF,
                 KEY_MODE_PRIVATE);
     }
 
     public void storeSessionId(String sessionId) {
         Editor editor = sharedPref.edit();
         editor.putString(KEY_SESSION_ID, sessionId);
         editor.commit();
     }
 
     public String getSessionId() {
         return sharedPref.getString(KEY_SESSION_ID, null);
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
 


// Utils.java
//package info.androidhive.webview;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.graphics.PorterDuff;
//import android.graphics.drawable.Drawable;
//import android.support.v4.content.ContextCompat;
//import android.view.MenuItem;
 
/**
 * Created by Ravi Tamada on 28/05/16.
 * www.androidhive.info
 */
// public class Utils {
 
    public static boolean isSameDomain(String url, String url1) {
        return getRootDomainUrl(url.toLowerCase()).equals(getRootDomainUrl(url1.toLowerCase()));
    }
 
    private static String getRootDomainUrl(String url) {
        String[] domainKeys = url.split("/")[2].split("\\.");
        int length = domainKeys.length;
        int dummy = domainKeys[0].equals("www") ? 1 : 0;
        if (length - dummy == 2)
            return domainKeys[length - 2] + "." + domainKeys[length - 1];
        else {
            if (domainKeys[length - 1].length() == 2) {
                return domainKeys[length - 3] + "." + domainKeys[length - 2] + "." + domainKeys[length - 1];
            } else {
                return domainKeys[length - 2] + "." + domainKeys[length - 1];
            }
        }
    }
 
    public static void tintMenuIcon(Context context, MenuItem item, int color) {
        Drawable drawable = item.getIcon();
        if (drawable != null) {
            // If we don't mutate the drawable, then all drawable's with this id will have a color
            // filter applied to it.
            drawable.mutate();
            drawable.setColorFilter(ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_ATOP);
        }
    }
 
    public static void bookmarkUrl(Context context, String url) {
        SharedPreferences pref = context.getSharedPreferences("androidhive", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
 
        // if url is already bookmarked, unbookmark it
        if (pref.getBoolean(url, false)) {
            editor.putBoolean(url, false);
        } else {
            editor.putBoolean(url, true);
        }
 
        editor.commit();
    }
 
    public static boolean isBookmarked(Context context, String url) {
        SharedPreferences pref = context.getSharedPreferences("androidhive", 0);
        return pref.getBoolean(url, false);
    }
//}
 }