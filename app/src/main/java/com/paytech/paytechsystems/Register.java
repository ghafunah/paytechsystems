package com.paytech.paytechsystems;

//import com.google.firebase.database.IgnoreExtraProperties;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.paytech.paytechsystems.getset.Edition;
import com.paytech.paytechsystems.helper.Config;
import com.paytech.paytechsystems.helper.Controller;
import com.paytech.paytechsystems.helper.SQLiteHandler;
import com.paytech.paytechsystems.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Register extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {//implements View.OnClickListener
        private static final String TAG1 = Register.class.getSimpleName();
        private Button btnsave;
        private EditText idno, fname, sname, email, password, uname, phone;
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
        private Spinner sex;
    @BindView(R.id.btn_bottom_sheet)
    Button btnBottomSheet;
 
    @BindView(R.id.bottom_sheet)
    LinearLayout layoutBottomSheet;
 
    BottomSheetBehavior sheetBehavior;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_register);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //getActionBar().setDisplayHomeAsUpEnabled(true);
            ButterKnife.bind(this);

            idno = (EditText) findViewById(R.id.usridno);
            fname = (EditText) findViewById(R.id.usrfname);
            sname = (EditText) findViewById(R.id.usrsname);

            email = (EditText) findViewById(R.id.usremail);
            password = (EditText) findViewById(R.id.usrpassword);
            phone = (EditText) findViewById(R.id.usrphone);
            uname = (EditText) findViewById(R.id.usruname);

            sex = (Spinner) findViewById(R.id.usrsex);
            sex.setOnItemSelectedListener(this);

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
            sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
            // Check if user is already logged in or not
//        if (session.isLoggedIn()) {
//            // User is already logged in. Take him to main activity
//            Intent intent = new Intent(Register.this, Start.class);
//            startActivity(intent);
//            finish();
//        }


            List<String> categories = new ArrayList<String>();
            categories.add("Male");
            categories.add("Female");
            categories.add("Other");

            // Creating adapter for spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            sex.setAdapter(dataAdapter);


            sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    switch (newState) {
                        case BottomSheetBehavior.STATE_HIDDEN:
                            break;
                        case BottomSheetBehavior.STATE_EXPANDED: {
                            btnBottomSheet.setText("Close Sheet");
                        }
                        break;
                        case BottomSheetBehavior.STATE_COLLAPSED: {
                            btnBottomSheet.setText("Expand Sheet");
                        }
                        break;
                        case BottomSheetBehavior.STATE_DRAGGING:
                            break;
                        case BottomSheetBehavior.STATE_SETTLING:
                            break;
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                    if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                                btnBottomSheet.setText("Close sheet");
                            } else {
                                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                btnBottomSheet.setText("Expand sheet");
                            }
                }
            });

            // Register Button Click event

            btnsave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    String idno1 = idno.getText().toString().trim();
                    String fname1 = fname.getText().toString().trim();
                    String sname1 = sname.getText().toString().trim();
                    //String status = site.getText().toString().trim(); //"Two Rivers";//site.getSelectedItem().toString().trim();
                    String uname1 = uname.getText().toString().trim();
                    String password1 = password.getText().toString().trim();
                    String email1 = email.getText().toString().trim();
                    String phone1 = phone.getText().toString().trim();
                    String branch1 = branch2.getText().toString().trim();
                    String gender1 = "Male";//gender.getText().toString().trim();
                    String role1 = "Cashier";//role.getText().toString().trim();

                    if (!sname1.isEmpty() && !fname1.isEmpty() && !password1.isEmpty()) {
                        //Log.d(TAG1, "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ Fname : " +fname1+ " Sname : " +sname1+ " Uname : " +uname1+ " Email :" +email1);
                        registerUser(idno1, fname1, sname1, uname1,branch1, email1, password1, gender1, role1, phone1);
                    } else {
                        Toast.makeText(getApplicationContext(), "Please firstname, surname, username and password mandatory!", Toast.LENGTH_LONG).show();
                    }
                }

            });
            getData();
        }

    /**
     * manually opening / closing bottom sheet on button click
     */
    @OnClick(R.id.btn_bottom_sheet)
    public void toggleBottomSheet() {
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            btnBottomSheet.setText("Close sheet");
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            btnBottomSheet.setText("Expand sheet");
        }
    }

/**
 * showing bottom sheet dialog
 */
    @OnClick(R.id.btn_bottom_sheet_dialog)
    public void showBottomSheetDialog() {
        View view = getLayoutInflater().inflate(R.layout.fragment_bottom_sheet_dialog, null);

        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        dialog.show();
    }

    private void loadSpinnerData() {
        // database handler
        SQLiteHandler db = new SQLiteHandler(getApplicationContext());

        // Spinner Drop down elements
        //List<Edition> lables = db.getAllEditions();
        List<String> lables = db.getAllLabels();

        // Creating adapter for spinner
      //  ArrayAdapter<Edition> dataAdapter = new ArrayAdapter<Edition>(this, android.R.layout.simple_spinner_item, lables);
        // Drop down layout style - list view with radio button
      //  dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Creating adapter for spinner
         ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lables);
        // Drop down layout style - list view with radio button
          dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        sex.setAdapter(dataAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }
    /**
     * showing bottom sheet dialog fragment
     * same layout is used in both dialog and dialog fragment
     */
    @OnClick(R.id.btn_bottom_sheet_dialog_fragment)
    public void showBottomSheetDialogFragment() {
        BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
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
        private void registerUser(final  String idno1, final String fname1, final String sname1,  final String uname1, final String branch1, final String email1, final String password1, final String gender1, final String role1, final String phone1){
            // Tag used to cancel the request
            String tag_string_req = "req_register";
            //Log.d(TAG1, "MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
            pDialog.setMessage("Saving User to Server ...");
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
                            String gender = user.getString("gender");
                            String role = user.getString("role");
                            //Integer status = 0;//user.getInt("status");

                            //String created_at = user.getString("created_at");

                            // Inserting row in users table
                            //db.addUser(idno, fname, sname, uname, email, branch, password, phone, SYNCED_WITH_SERVER);

                            Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();

                            // Launch login activity
                            Intent intent = new Intent( Register.this, Login.class);
                            startActivity(intent);
                            finish();
                        } else {

                            // Error occurred in registration. Get the error
                            // message
                            db.saveUser(idno1, fname1, sname1, uname1, email1, branch1, password1, phone1, gender1, role1, NOT_SYNCED_WITH_SERVER);
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
                    db.saveUser(idno1, fname1, sname1, uname1, email1, branch1, password1, phone1, gender1, role1, NOT_SYNCED_WITH_SERVER);
                    Log.e(TAG1, "Registration Error==========================================: " + error.getMessage());
                    Toast.makeText(getApplicationContext(),  error.getMessage(), Toast.LENGTH_LONG).show();
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
                    params.put("uname", uname1);
                    params.put("email", email1);
                    params.put("branch", branch1);
                    params.put("password", password1);
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

            Intent add_intent = new Intent(Register.this, MainActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(add_intent);
        }

    }
