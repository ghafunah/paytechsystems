package com.paytech.paytechsystems.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.paytech.paytechsystems.getset.Branch;
import com.paytech.paytechsystems.getset.User;
import com.paytech.paytechsystems.helper.Config;
import com.paytech.paytechsystems.helper.SQLiteHandler;
import com.paytech.paytechsystems.helper.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class NetworkStateChecker extends BroadcastReceiver {

    //context and database helper object
    private Context context;
    private SQLiteHandler db;


    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;

        db = new SQLiteHandler(context);

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        //if there is a network
        if (activeNetwork != null) {
            //if connected to wifi or mobile data plan
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {

                //getting all the unsynced names
                Cursor user = db.getUnsyncedUsers();
                Cursor branch = db.getUnsyncedBranches();
                if (user.moveToFirst()) {
                    do {
                        //calling the method to save the unsynced name to MySQL
                        db.saveUser(
                                user.getString(user.getColumnIndex(User.USER_IDNO)),
                                user.getString(user.getColumnIndex(User.USER_FNAME)),
                                user.getString(user.getColumnIndex(User.USER_SNAME)),
                                user.getString(user.getColumnIndex(User.USER_BRANCH)),
                                user.getString(user.getColumnIndex(User.USER_EMAIL)),
                                user.getString(user.getColumnIndex(User.USER_UNAME)),
                                user.getString(user.getColumnIndex(User.USER_PHONE)),
                                user.getString(user.getColumnIndex(User.USER_PASSWORD)),
                                user.getString(user.getColumnIndex(User.USER_GENDER)),
                                user.getString(user.getColumnIndex(User.USER_ROLE)),
                                        1
                        );
                    } while (user.moveToNext());
                }

                if (branch.moveToFirst()) {
                    do {
                        //calling the method to save the unsynced name to MySQL
                        db.saveBranch(
                                branch.getString(branch.getColumnIndex(Branch.BRANCH_CODE)),
                                branch.getString(branch.getColumnIndex(Branch.BRANCH_NAME)),
                                branch.getString(branch.getColumnIndex(Branch.BRANCH_DESC)),
                                branch.getString(branch.getColumnIndex(Branch.BRANCH_PHONE)),
                                branch.getString(branch.getColumnIndex(Branch.BRANCH_EMAIL)),
                                1
                        );
                    } while (branch.moveToNext());
                }
            }
        }
    }

    /*
    * method taking two arguments
    * name that is to be saved and id of the name from SQLite
    * if the name is successfully sent
    * we will update the status as synced in SQLite
    * */
    private void saveName(final String idno, final String fname,
                          final String sname, final String uname, final String branch, final String uemail, final String password, final String phone) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.URL +"register.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //updating the status in sqlite
                                //db.updateNameStatus(uname, MainActivity.NAME_SYNCED_WITH_SERVER);

                                //sending the broadcast to refresh the list
                                //context.sendBroadcast(new Intent(SyncActivity.DATA_SAVED_BROADCAST));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("idno", idno);
                params.put("fname", fname);
                params.put("sname", sname);
                params.put("uname", uname);
                params.put("branch", branch);
                params.put("email", uemail);
                params.put("password", password);
                params.put("phone", phone);
                return params;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

}
