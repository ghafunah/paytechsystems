package com.paytech.paytechsystems.helper;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.content.Context;

public class Config {

	// Server url
	// public static String URL = "http://192.168.64.1/senior/";
//		public static String URL = "http://169.254.141.142/paytech/";
//	public static String URL = "http://cybersofttechnologies.net/mpark/";

//	 public static String URL = "http://169.254.141.142/android_login_api/";
//	 public static String URL = "http://169.254.141.142/mpark/";
	public static String URL1 = "http://cybersofttechnologies.net/seniors_db/";
    public static String URL2 = "http://seniorsdrsch.co.ke/s/";
    public static String URL = "http://mercalhealthcare.co.ke/s/";
	//Tags used in the JSON String
	public static final String TAG_USERNAME = "username";
	public static final String TAG_NAME = "name";
	public static final String TAG_COURSE = "course";
	public static final String TAG_SESSION = "session";
    public static final Integer LIMIT = 10;
    public static final String LIMIT1 = "10";
	//JSON array name
	public static final String JSON_ARRAY = "branches";
	public static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

	// global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";
 
    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";
 
    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;
 
    public static final String SHARED_PREF = "ah_firebase";

     // File upload url (replace the ip with your server address)
    public static final String FILE_UPLOAD_URL = URL + "fileUpload.php";
     
    // Directory name to store captured images and videos
    public static final String IMAGE_DIRECTORY_NAME = "Images";

    public static final String URL_WEBSOCKET = "ws://192.168.0.102:8080/WebMobileGroupChatServer/chat?name=";


}
