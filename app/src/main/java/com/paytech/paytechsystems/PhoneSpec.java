package com.paytech.paytechsystems;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonParser;
import com.paytech.paytechsystems.helper.SQLiteHandler;
import com.paytech.paytechsystems.helper.SessionManager;

import java.util.jar.Manifest;

public class PhoneSpec extends AppCompatActivity {
    private static final String TAG = PhoneSpec.class.getSimpleName();
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    private int batLevel;
    private  String imei;
    ActivityManager am;
    long rm, instorage, exstorage, total;
    //private TextView pversion, pname, pbrand,pos, pdevice, ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_specs);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        SharedPreferences  sf = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String syn = sf.getString("sync_frequency", "20");
        String folder = sf.getString("key_gallery_name", "Home");
        String url = sf.getString("key_url", "http://cybersofttechnologies.net/seniors_db/");


        BatteryManager bm = (BatteryManager) getSystemService(BATTERY_SERVICE);
        batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {
            imei = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        }

        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo ram = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(ram);
        rm = ram.totalMem/(1048576L);

        StatFs internal = new StatFs(Environment.getRootDirectory().getAbsolutePath());
        StatFs external = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
        long is = internal.getBlockSizeLong()*internal.getBlockCountLong();
        long es = external.getBlockSizeLong()*external.getBlockCountLong();
        instorage = is/1048576L;
        exstorage = es/1048576L;
        total = (external.getTotalBytes() + internal.getTotalBytes())/(1024*1024);
         // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        TextView pversion = (TextView) findViewById(R.id.phone_version);
        TextView pname = (TextView) findViewById(R.id.phone_name);
        TextView pbrand = (TextView) findViewById(R.id.phone_brand);
        TextView pos = (TextView) findViewById(R.id.phone_os);
        TextView pdevice = (TextView) findViewById(R.id.phone_device);
        TextView pvcodes = (TextView) findViewById(R.id.phone_vcodes);
        TextView pdisplay = (TextView) findViewById(R.id.phone_display);
        TextView phardware = (TextView) findViewById(R.id.phone_hardware);
        TextView ptime = (TextView) findViewById(R.id.phone_time);
        TextView pproduct = (TextView) findViewById(R.id.phone_product);
        TextView pboard = (TextView) findViewById(R.id.phone_board);
        TextView pbattery = (TextView) findViewById(R.id.phone_battery);
        TextView pimei = (TextView) findViewById(R.id.phone_imei);
        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());
        JsonParser jsonParser = new JsonParser();
        JSONObject obj =  getPhoneSpec();

        try {
            //JSONObject object = jsonParser.parse(obj.getString("kk"));
          //final  JSONArray jsonArray  = new  JSONArray(getPhoneSpec().getJSONObject("specs"));
            pversion.setText("\tManufacturer : " +obj.getString("Manufacturer"));
            pname.setText("\tModel : " + obj.getString("Model"));
            pbrand.setText("\tBrand : " + obj.getString("Brand"));
            pdevice.setText("\tDevice : " + obj.getString("Device"));
            pvcodes.setText("\tVersion Codes : " + obj.getString("Version Codes"));
            pdisplay.setText("\tDisplay : " + obj.getString("Display"));
            phardware.setText("\tHardware : " + obj.getString("Hardware"));
            ptime.setText("\tTime : " + obj.getString("Time"));
            pproduct.setText("\tProduct : " + obj.getString("Product"));
            pboard.setText("\tBoard : " + obj.getString("Board"));
            //pboard.setText("Model : " + Build.MODEL);
            pos.setText("\tOS : " + obj.getString("OS"));
            pbattery.setText("\tBattery Level : " + obj.getString("Battery Level")+"%");
            pimei.setText("\tRAM : " + obj.getString("RAM")+
                    " GB " + obj.getString("storage") +
                    "\n\tFrequency = " +syn +
                    "\n\tFolder = "+ folder +
                    "\n\tPer Page  = " +sf.getString("key_per_page", "10") +
                    "\n\tUrl = "+ url +
                    "\n\tUse My URL = "+ sf.getBoolean("use_my_url", false)

             );
        }catch (JSONException e){
            Log.d(TAG, "++++++++++++++++++++++++++"+ e);
        }

        //pversion.setText("Manufacturer : " +Build.MANUFACTURER);
        //pname.setText("Model : " + Build.MODEL);
//        pbrand.setText("Brand : " + Build.BRAND);
//        pdevice.setText("Device : " + Build.DEVICE);
//        pvcodes.setText("Version Codes : " + Build.VERSION_CODES.LOLLIPOP_MR1);
//        pdisplay.setText("Display : " + Build.DISPLAY);
//        phardware.setText("Hardware : " + Build.HARDWARE + ", Serial : " + Build.SERIAL);
//        ptime.setText("Time : " + Build.TIME);
//        pproduct.setText("Product : " + Build.PRODUCT);
//        pboard.setText("Board : " + Build.BOARD);
//        //pboard.setText("Model : " + Build.MODEL);
//        pos.setText("OS : " + Build.VERSION.RELEASE +" , "+ Build.VERSION.CODENAME);
//        pbattery.setText("Battery Level : " + getPhoneSpec());

        //pimei.setText("IMEI : " + rm);

   }

   public  JSONObject getPhoneSpec(){

       //am = new ActivityManager();// context.getSystemService(Context.ACTIVITY_SERVICE);
       //var activityManager
       JSONObject specs = new JSONObject();
       try {
           //JSONObject specs = new JSONObject();
            specs.put("Manufacturer", Build.MANUFACTURER);
            specs.put("Brand",Build.BRAND);
            specs.put("Device", Build.DEVICE);
            specs.put("Version Codes", Build.VERSION_CODES.LOLLIPOP_MR1);
            specs.put("Display", Build.DISPLAY);
            specs.put("Hardware",  Build.HARDWARE);
            specs.put("Time", Build.TIME);
            specs.put("Product", Build.PRODUCT);
            specs.put("Board", Build.BOARD);
            specs.put("Model", Build.MODEL);
            specs.put("OS", Build.VERSION.RELEASE +" , "+ Build.VERSION.CODENAME);
            specs.put("Battery Level", batLevel);
           specs.put("IMEI", imei);
           specs.put("RAM", rm);
           specs.put("storage", total);
            //spec.put("specs", specs);
        }catch (JSONException e){
            Log.e(TAG, "Error" + e);
        }
        return  specs;
   }

}

