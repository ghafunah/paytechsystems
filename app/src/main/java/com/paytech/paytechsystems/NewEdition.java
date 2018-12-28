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

public class NewEdition extends AppCompatActivity  {//implements View.OnClickListener
        private static final String TAG1 = NewEdition.class.getSimpleName();
        private Button btnsave;
        private EditText course, edition, fees;
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
            setContentView(R.layout.activity_edition);
            //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
           // setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //getActionBar().setDisplayHomeAsUpEnabled(true);


            course = (EditText) findViewById(R.id.course);
            edition = (EditText) findViewById(R.id.edition);
            fees = (EditText) findViewById(R.id.fees);

            btnsave = (Button) findViewById(R.id.btnSave);

            // Progress dialog
            pDialog = new ProgressDialog(this);
            pDialog.setCancelable(false);

            // Session manager
            session = new SessionManager(getApplicationContext());

            // SQLite database handler
            db = new SQLiteHandler(getApplicationContext());

            Intent i = getIntent();

            //HashMap<String, String> item = (HashMap<String, String>) i.getSerializableExtra("course");

            String s = i.getExtras().getString("course");
            Toast.makeText(getApplicationContext(), "Course is : " + s, Toast.LENGTH_LONG).show();

            course.setText(s);
            course.setKeyListener(null);
            // Check if user is already logged in or not
//        if (session.isLoggedIn()) {
//            // User is already logged in. Take him to main activity
//            Intent intent = new Intent(Register.this, Start.class);
//            startActivity(intent);
//            finish();
//        }

            // Register Button Click event

            btnsave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    String courseid = course.getText().toString().trim();
                    String edition1 = edition.getText().toString().trim();
                    String fees1 = fees.getText().toString().trim();
                    if (!courseid.isEmpty() && !edition1.isEmpty() && !fees1.isEmpty()) {
                        //Log.d(TAG1, "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ Fname : " +fname1+ " Sname : " +sname1+ " Uname : " +uname1+ " Email :" +email1);
                        saveEdition(courseid, edition1, fees1);
                    } else {
                        Toast.makeText(getApplicationContext(), "Please fill all fields mandatory!", Toast.LENGTH_LONG).show();
                    }
                }

            });
            //getData();
        }

       private void getData(){
           //Creating a string request
           StringRequest stringRequest = new StringRequest(Config.URL + "edition.php",
                   new Response.Listener<String>() {
                       @Override
                       public void onResponse(String response) {
                           Log.d(TAG1, "kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk" +response);
                           JSONObject j = null;
                           try {
                               //Parsing the fetched Json String to JSON Object
                               j = new JSONObject(response);

                               //Storing the Array of JSON String to our JSON Array
                               result = j.getJSONArray(Config.JSON_ARRAY);

                               //Calling method getBranches to get the branches from the JSON Array
                               getBranches(result);
                           } catch (JSONException e) {
                               e.printStackTrace();
                           }
                       }
                   },
                   new Response.ErrorListener() {
                       @Override
                       public void onErrorResponse(VolleyError error) {

                       }
                   });

           //Creating a request queue
           RequestQueue requestQueue = Volley.newRequestQueue(this);

           //Adding request to the queue
           requestQueue.add(stringRequest);
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
        private void saveEdition(final  String courseid, final String edition1, final String fees1){
            // Tag used to cancel the request
            String tag_string_req = "req_register";
            //Log.d(TAG1, "MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
            pDialog.setMessage("Saving to Server ...");
            showDialog();

            StringRequest strReq = new StringRequest(Request.Method.POST,  Config.URL + "edition.php", new Response.Listener<String>() {

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
                            String idno = user.getString("idno");
                            String fname = user.getString("fname");
                            String sname = user.getString("sname");
                            String uname = user.getString("uname");
                            String email = user.getString("email");
                            String password = user.getString("password");
                            String phone = user.getString("phone");
                            String branch = user.getString("branch");
                            //Integer status = 0;//user.getInt("status");

                            //String created_at = user.getString("created_at");
                            db.saveEdition(0,courseid, edition1, fees1, NOT_SYNCED_WITH_SERVER);
                            // Inserting row in users table
                            //db.addUser(idno, fname, sname, uname, email, branch, password, phone, SYNCED_WITH_SERVER);

                            Toast.makeText(getApplicationContext(), "Edition successfully saved!", Toast.LENGTH_LONG).show();

                            // Launch login activity
                            Intent intent = new Intent( NewEdition.this, Login.class);
                            startActivity(intent);
                            finish();
                        } else {

                            // Error occurred in registration. Get the error
                            // message
                           db.saveEdition(0, courseid, edition1, fees1, NOT_SYNCED_WITH_SERVER);
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
                    db.saveEdition(0, courseid, edition1, fees1, NOT_SYNCED_WITH_SERVER);
                    Log.e(TAG1, "Registration Error==========================================: " + error.getMessage());
                    Toast.makeText(getApplicationContext(),  " Hello Paytech Systems " +error.getMessage(), Toast.LENGTH_LONG).show();
                    hideDialog();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting params to register url
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("courseid", courseid);
                    params.put("edition", edition1);
                    params.put("fees", fees1);
                     //params.put("status", status);
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

            Intent add_intent = new Intent(NewEdition.this, Courses.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(add_intent);
        }

    }
