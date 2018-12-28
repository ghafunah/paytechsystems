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

public class Teacher extends AppCompatActivity  {//implements View.OnClickListener
        private static final String TAG1 = Teacher.class.getSimpleName();
        private Button btnsave;
        private EditText idno, fname, sname, email, phone;
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
            setContentView(R.layout.activity_teacher);
            //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            //setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //getActionBar().setDisplayHomeAsUpEnabled(true);


            idno = (EditText) findViewById(R.id.idno);
            fname = (EditText) findViewById(R.id.fname);
            sname = (EditText) findViewById(R.id.sname);

            email = (EditText) findViewById(R.id.email);

            phone = (EditText) findViewById(R.id.phone);

            btnsave = (Button) findViewById(R.id.btnSave);

            // Progress dialog
            pDialog = new ProgressDialog(this);
            pDialog.setCancelable(false);

            // Session manager
            session = new SessionManager(getApplicationContext());

            // SQLite database handler
            db = new SQLiteHandler(getApplicationContext());

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
                    String idno1 = idno.getText().toString().trim();
                    String fname1 = fname.getText().toString().trim();
                    String sname1 = sname.getText().toString().trim();
                    String email1 = email.getText().toString().trim();
                    String phone1 = phone.getText().toString().trim();


                    if (!sname1.isEmpty() && !fname1.isEmpty() && !phone1.isEmpty()) {
                        //Log.d(TAG1, "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ Fname : " +fname1+ " Sname : " +sname1+ " Uname : " +uname1+ " Email :" +email1);
                        SaveTeacher(idno1, fname1, sname1, email1, phone1);
                    } else {
                        Toast.makeText(getApplicationContext(), "Please firstname, surname and phone mandatory!", Toast.LENGTH_LONG).show();
                    }
                }

            });
            getData();
        }

       private void getData(){
           //Creating a string request
           StringRequest stringRequest = new StringRequest(Config.URL + "branches.php",
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
        private void SaveTeacher(final  String idno1, final String fname1, final String sname1, final String email1, final String phone1){
            // Tag used to cancel the request
            String tag_string_req = "req_register";
            //Log.d(TAG1, "MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
            pDialog.setMessage("Saving to Server ...");
            showDialog();

            StringRequest strReq = new StringRequest(Request.Method.POST,  Config.URL + "register.php", new Response.Listener<String>() {

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

                            // Inserting row in users table
                            //db.addUser(idno, fname, sname, uname, email, branch, password, phone, SYNCED_WITH_SERVER);

                            Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();

                            // Launch login activity
                            Intent intent = new Intent( Teacher.this, Login.class);
                            startActivity(intent);
                            finish();
                        } else {

                            // Error occurred in registration. Get the error
                            // message
                            //db.SaveTeacher(idno1, fname1, sname1, email1,  phone1, NOT_SYNCED_WITH_SERVER); //Save to local DB
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
                    //db.SaveTeacher(idno1, fname1, sname1, email1, phone1, NOT_SYNCED_WITH_SERVER); //Save to local DB
                    Log.e(TAG1, "Registration Error==========================================: " + error.getMessage());
                    Toast.makeText(getApplicationContext(),  "Hello Paytech Systems! ", Toast.LENGTH_LONG).show();
                    //Toast.makeText(getApplicationContext(),  error.getMessage(), Toast.LENGTH_LONG).show();
                    hideDialog();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting params to register url
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("idno", idno1);
                    params.put("fname", fname1);
                    params.put("sname", sname1);
                    params.put("email", email1);
                    params.put("phone", phone1);
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

            Intent add_intent = new Intent(Teacher.this, MainActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(add_intent);
        }

    }
