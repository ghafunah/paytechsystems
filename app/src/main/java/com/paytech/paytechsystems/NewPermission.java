package com.paytech.paytechsystems;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.paytech.paytechsystems.helper.Config;
import com.paytech.paytechsystems.helper.Controller;
import com.paytech.paytechsystems.helper.SQLiteHandler;
import com.paytech.paytechsystems.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewPermission extends AppCompatActivity  {//implements View.OnClickListener
        private static final String TAG1 = NewPermission.class.getSimpleName();
        private Button btnsave;
        private EditText page, users;
        private ProgressDialog pDialog;
        private SessionManager session;
        private SQLiteHandler db;



            //1 means data is synced and 0 means data is not synced
        public static final int SYNCED_WITH_SERVER = 1;
        public static final int NOT_SYNCED_WITH_SERVER = 0;
        //    private Spinner  site;
        private JSONArray result;
        private ArrayList<String> branches;
        private AutoCompleteTextView branch2;
        private MultiAutoCompleteTextView text1;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_permission);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //getActionBar().setDisplayHomeAsUpEnabled(true);


            page = (EditText) findViewById(R.id.page);
            users = (EditText) findViewById(R.id.users);

            btnsave = (Button) findViewById(R.id.btnSave);

            // Progress dialog
            pDialog = new ProgressDialog(this);
            pDialog.setCancelable(false);

            // Session manager
            session = new SessionManager(getApplicationContext());

            // SQLite database handler
            db = new SQLiteHandler(getApplicationContext());

            Intent i = getIntent();

            // Register Button Click event

            btnsave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    String page1 = page.getText().toString().trim();
                    String users1 = users.getText().toString().trim();
                    if (!page1.isEmpty() && !users1.isEmpty()) {
                        //Log.d(TAG1, "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ Fname : " +fname1+ " Sname : " +sname1+ " Uname : " +uname1+ " Email :" +email1);
                        savePermission(page1, users1);
                    } else {
                        Toast.makeText(getApplicationContext(), "Please fill all fields mandatory!", Toast.LENGTH_LONG).show();
                    }
                }

            });
            //getData();
        }

       private void getBranches(JSONArray j){
           //Traversing through all the items in the json array
           for(int i=0;i<j.length();i++){
               try {
                   //Getting json object
                   JSONObject json = j.getJSONObject(i);

                   //Adding the name of the branch to array list
                   branches.add(json.getString(Config.TAG_NAME));
                   //Log.d(TAG1, "uuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu" + branches);
               } catch (JSONException e) {
                   e.printStackTrace();
               }
           }
           //Setting adapter to show the items in the spinner
           ArrayAdapter<String> br = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,branches);
           Log.d(TAG1, "uuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu" + branches);
//        site.setAdapter(new ArrayAdapter<String>(Register.this, android.R.layout.simple_list_item_1, site));
           branch2.setAdapter(br);
       }

        /**
         * Function to store user in MySQL database will post params(tag, name,
         * email, password) to register url
         * */
        private void savePermission(final  String page, final String users){
            // Tag used to cancel the request
            String tag_string_req = "req_register";
            //Log.d(TAG1, "MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
            pDialog.setMessage("Saving to Server ...");
            showDialog();

            StringRequest strReq = new StringRequest(Request.Method.POST,  Config.URL + "permission.php", new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d(TAG1, "Register Response+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++: " + response.toString());
                    hideDialog();
                    Log.d(TAG1, "NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN" + response);
                    try {
                        JSONObject jObj = new JSONObject(response);
                        boolean error = jObj.getBoolean("error");
                        //Log.d(TAG1, "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO" + response);
                        if (!error) {
                            // User successfully stored in MySQL
                            // Now store the user in sqlite
                            String stfid = jObj.getString("stfid");

                            JSONObject user = jObj.getJSONObject("user");
                            String page = user.getString("page");
                            String users = user.getString("users");
                            //String created_at = user.getString("created_at");
                            db.savePermission(page, users,  SYNCED_WITH_SERVER);

                            Toast.makeText(getApplicationContext(), "Permission successfully saved!", Toast.LENGTH_LONG).show();

                            // Launch login activity
                            Intent intent = new Intent( NewPermission.this, Permissions.class);
                            startActivity(intent);
                            finish();
                        } else {

                            // Error occurred in registration. Get the error
                            // message
                           db.savePermission(page, users, NOT_SYNCED_WITH_SERVER);
                            String errorMsg = jObj.getString("error_msg");
                            Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        //progressDialog.dismiss();
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    db.savePermission(page, users, NOT_SYNCED_WITH_SERVER);
                    Log.e(TAG1, "Saving Error==========================================: " + error.getMessage());
                    Toast.makeText(getApplicationContext(),  R.string.greetings , Toast.LENGTH_LONG).show();
                    hideDialog();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting params to register url
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("page", page);
                    params.put("users", users);
                    return params;
                }

            };

            // Adding request to request queue
            Controller.getInstance().addToRequestQueue(strReq, tag_string_req);
        }

        private void showDialog() {
            if (!pDialog.isShowing())
                pDialog.show();
        }

        private void hideDialog() {
            if (pDialog.isShowing())
                pDialog.dismiss();
        }

        public void cancel(View view) {

            Intent add_intent = new Intent(NewPermission.this, Courses.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(add_intent);
        }

    }
