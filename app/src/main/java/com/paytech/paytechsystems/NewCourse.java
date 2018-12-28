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
import com.paytech.paytechsystems.getset.Course;
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

public class NewCourse extends AppCompatActivity  {//implements View.OnClickListener
        private static final String TAG1 = NewCourse.class.getSimpleName();
        private Button btnsave;
        private EditText course;
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
            setContentView(R.layout.activity_course);
            //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
           // setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //getActionBar().setDisplayHomeAsUpEnabled(true);


            course = (EditText) findViewById(R.id.course);

            btnsave = (Button) findViewById(R.id.btnSave);

            // Progress dialog
            pDialog = new ProgressDialog(this);
            pDialog.setCancelable(false);

            // Session manager
            session = new SessionManager(getApplicationContext());

            // SQLite database handler
            db = new SQLiteHandler(getApplicationContext());

            btnsave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    String course1 = course.getText().toString().trim();
                    //String branch1 = branch2.getText().toString().trim();

                    if (!course1.isEmpty()) {
                        //Log.d(TAG1, "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ Fname : " +fname1+ " Sname : " +sname1+ " Uname : " +uname1+ " Email :" +email1);
                        saveCourse(course1);
                    } else {
                        Toast.makeText(getApplicationContext(), "Please firstname, surname, username and password mandatory!", Toast.LENGTH_LONG).show();
                    }
                }

            });
            //getData();


        //siteName = item.get(CarsList.SITE).toString();
        }


        /**
         * Function to store user in MySQL database will post params(tag, name,
         * email, password) to register url
         * */
        private void saveCourse(final  String course1){
            // Tag used to cancel the request
            String tag_string_req = "req_register";
            //Log.d(TAG1, "MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
            pDialog.setMessage("Saving to Server ...");
            showDialog();

            StringRequest strReq = new StringRequest(Request.Method.POST,  Config.URL + "course.php", new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d(TAG1, "Save Response+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++: " + response.toString());
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

                            JSONObject c = jObj.getJSONObject("course");
                            String cname = c.getString("cname");
                            Integer id = c.getInt("cid");
                            //String created_at = user.getString("created_at");

                            // Inserting row in users table
                            db.saveCourse(id, cname, SYNCED_WITH_SERVER);

                            Toast.makeText(getApplicationContext(), "Course successfully saved!", Toast.LENGTH_LONG).show();

                            // Launch login activity
                            Intent intent = new Intent( NewCourse.this, Courses.class);
                            startActivity(intent);
                            finish();
                        } else {

                            // Error occurred in registration. Get the error
                            // message
                            db.saveCourse(0, course1, NOT_SYNCED_WITH_SERVER);
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
                    db.saveCourse(0, course1, NOT_SYNCED_WITH_SERVER);
                    Log.e(TAG1, "Registration Error==========================================: " + error.getMessage());
                    Toast.makeText(getApplicationContext(),  "Registration Error! Check url" + error.getMessage(), Toast.LENGTH_LONG).show();
                    hideDialog();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting params to register url
                    Map<String, String> params = new HashMap<String, String>();
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

            Intent add_intent = new Intent(NewCourse.this, Courses.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(add_intent);
        }

    }
