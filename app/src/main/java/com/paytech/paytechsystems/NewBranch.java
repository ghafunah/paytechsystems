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

public class NewBranch extends AppCompatActivity  {//implements View.OnClickListener
        private static final String TAG1 = NewBranch.class.getSimpleName();
        private Button btnsave;
        private EditText code, name, location, email, phone;
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
            setContentView(R.layout.activity_branch);
            //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            //setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //getActionBar().setDisplayHomeAsUpEnabled(true);


            code = (EditText) findViewById(R.id.code);
            name = (EditText) findViewById(R.id.name);
            location = (EditText) findViewById(R.id.location);

            email = (EditText) findViewById(R.id.email);
            phone = (EditText) findViewById(R.id.phone);

            btnsave = (Button) findViewById(R.id.btnSave);

            branches = new ArrayList<String>();

            branch2 = (AutoCompleteTextView) findViewById(R.id.usrbranch);

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
                    String code1 = code.getText().toString().trim();
                    String name1 = name.getText().toString().trim();
                    String location1 = location.getText().toString().trim();
                    String email1 = email.getText().toString().trim();
                    String phone1 = phone.getText().toString().trim();

                    if (!code1.isEmpty() && !name1.isEmpty() && !phone1.isEmpty()) {
                        //Log.d(TAG1, "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ Fname : " +fname1+ " Sname : " +sname1+ " Uname : " +uname1+ " Email :" +email1);
                        saveBranch(code1, name1, phone1, location1, email1);
                    } else {
                        Toast.makeText(getApplicationContext(), "Please fill all mandatory fields!", Toast.LENGTH_LONG).show();
                    }
                }

            });
        }



        /**
         * Function to store user in MySQL database will post params(tag, name,
         * email, password) to register url
         * */
        private void saveBranch(final  String code, final String name, final String location,  final String phone, final String email){
            // Tag used to cancel the request
            String tag_string_req = "req_register";
            //Log.d(TAG1, "MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
            pDialog.setMessage("Saving to Server ...");
            showDialog();

            StringRequest strReq = new StringRequest(Request.Method.POST,  Config.URL + "branch.php", new Response.Listener<String>() {

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
                            String code2 = user.getString("code");
                            String name2 = user.getString("name");
                            String location2 = user.getString("location");
                            String phone2 = user.getString("phone");
                            String email2 = user.getString("email");

                            //String created_at = user.getString("created_at");

                            // Inserting row in users table
                            db.saveBranch(code2, name2, phone2, location2, email2, SYNCED_WITH_SERVER);

                            Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();

                            // Launch login activity
                            Intent intent = new Intent( NewBranch.this, Branches.class);
                            startActivity(intent);
                            finish();
                        } else {

                            // Error occurred in registration. Get the error
                            // message
                            db.saveBranch(code, name, phone, location, email, NOT_SYNCED_WITH_SERVER);
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
                    db.saveBranch(code, name, phone, location, email, NOT_SYNCED_WITH_SERVER);
                    Log.e(TAG1, "Registration Error==========================================: " + error.getMessage());
                    Toast.makeText(getApplicationContext(),  error.getMessage(), Toast.LENGTH_LONG).show();
                    hideDialog();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting params to register url
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("code", code);
                    params.put("name", name);
                    params.put("location", location);
                    params.put("phone", phone);
                    params.put("email", email);
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

            Intent add_intent = new Intent(NewBranch.this, Branches.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(add_intent);
        }

    }
