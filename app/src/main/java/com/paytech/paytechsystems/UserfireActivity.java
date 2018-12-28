package com.paytech.paytechsystems;
 
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.paytech.paytechsystems.adapter.FireAdapter;
import com.paytech.paytechsystems.getset.Userfire;
import com.paytech.paytechsystems.helper.Config;
import com.paytech.paytechsystems.helper.NotificationUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class UserfireActivity extends AppCompatActivity  implements FireAdapter.FireAdapterListener, SwipeRefreshLayout.OnRefreshListener {
 
    private static final String TAG = UserfireActivity.class.getSimpleName();
    private static final String REQUIRED = "Required";
    private TextView txtDetails, name, email, date;
    private EditText inputName, inputEmail, messageIn;
    private Button btnSave;
    private RecyclerView rv;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Userfire> list;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private FloatingActionButton send;
    private String userId;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private  FireAdapter fAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_userfire);
        setContentView(R.layout.content_mgs);
 
        // Displaying toolbar icon
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        // txtDetails = (TextView) findViewById(R.id.txt_user);
        // inputName = (EditText) findViewById(R.id.name);
        // inputEmail = (EditText) findViewById(R.id.email);
        //messageIn = (EditText) findViewById(R.id.message);
        // btnSave = (Button) findViewById(R.id.btn_save);
       // fAdapter = new FireAdapter(UserfireActivity.this, list);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        rv = (RecyclerView) findViewById(R.id.recycler_viewfa);
        list = new ArrayList<>();
        fAdapter = new FireAdapter(UserfireActivity.this, list);
        RecyclerView.LayoutManager recycle = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(recycle);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(fAdapter);
        rv.smoothScrollToPosition(rv.getAdapter().getItemCount());
        name = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);
        date = (TextView) findViewById(R.id.mgsdate);
        send = (FloatingActionButton) findViewById(R.id.send);
        swipeRefreshLayout.setOnRefreshListener(this);
        //if (mFirebaseDatabase == null){
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        //mFirebaseInstance.setPersistenceEnabled(true);
        mFirebaseDatabase = mFirebaseInstance.getReference("/user/messages1");
        //}
//        rv.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//            @Override
//            public void onLayoutChange(View view, int l, int t, int r, int b, int l1, int t1, int r1, int b1) {
//                if (b < b1){
//                    rv.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            rv.smoothScrollToPosition(rv.getAdapter().getItemCount() - 1);
//                        }
//                    });
//                }
//            }
//        });

        // get reference to 'users' node

 
        // store app title to 'app_title' node
        mFirebaseInstance.getReference("app_title").setValue(" Chat Messages ");
 
        // app_title change listener
        mFirebaseInstance.getReference("app_title").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "App title updated");
 
                String appTitle = dataSnapshot.getValue(String.class);
 
                // update toolbar title
                getSupportActionBar().setTitle(appTitle);
            }
 
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read app title value.", error.toException());
            }
        });
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
 
                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
 
                    //displayFirebaseRegId();
 
                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
 
                    String message = intent.getStringExtra("message");
 
                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
 
                    //txtMessage.setText(message);
                    addUserChangeListener();
                }
            }
        };
       send.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
              // Intent login = new Intent(getApplicationContext(), NewLesson.class);
              // startActivity(login);
              //finish();
            messageIn = (EditText) findViewById(R.id.message);
            String message = messageIn.getText().toString().trim();
              SharedPreferences pref = getSharedPreferences("Paytech", 0);
              String uname = pref.getString("user_uname", null).trim();
              String name = pref.getString("user_fname", null).trim()+ " " +pref.getString("user_sname", null).trim();
              String email = "ghafred@gmail.com";//inputEmail.getText().toString();
              String date = new SimpleDateFormat("yyyyMMdd HHmmss").format(Calendar.getInstance().getTime());

              if (TextUtils.isEmpty(message)) {
                  messageIn.setError(REQUIRED);
                  //Toast.makeText(getApplicationContext(), "Please enter your message!"+ uname, Toast.LENGTH_SHORT).show();
                  return;
              }
              setEditingEnabled(false);
              Toast.makeText(UserfireActivity.this, "Posting...", Toast.LENGTH_SHORT).show();
            createUser(message, email, date, uname, name);
            messageIn.setText("");
            userId = null;
        }
      });

                swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                addUserChangeListener();
                                        //fAdapter.notifyDataSetChanged();

            }

                    }
                                // }
        );
    }

    private void setEditingEnabled(boolean enabled) {
        messageIn.setEnabled(enabled);
        //mBodyField.setEnabled(enabled);
        if (enabled) {
            send.show();
        } else {
            send.hide();
        }
    }

    @Override
    public void onFireSelected(Userfire i) {
        Toast.makeText(getApplicationContext(), "Selected: " + i.getName() + ", " + i.getEmail(), Toast.LENGTH_SHORT).show();
    }
    // Changing button text
    private void toggleButton() {
        if (TextUtils.isEmpty(userId)) {
            btnSave.setText("Save");
        } else {
            btnSave.setText("Update");
        }
    }
 
     @Override
    public void onRefresh() {
        list.clear();
        addUserChangeListener();
    }
    /**
     * Creating new user node under 'users'
     */
    private void createUser(String message, String email, String date, String uname, String name) {
        // TODO
        // In real apps this userId should be fetched
        // by implementing firebase auth
        if (TextUtils.isEmpty(userId)) {
            userId = mFirebaseDatabase.push().getKey();
        }
 
        Userfire user = new Userfire(message, email, date, uname, name);
 
        mFirebaseDatabase.child(userId).setValue(user);
        //list.add(0, user);
        setEditingEnabled(true);
        list.clear();
        addUserChangeListener();
        //fAdapter.notifyDataSetChanged();
    }
 
    /**
     * User data change listener
     */
    private void addUserChangeListener() {
        // User data change listener
        mFirebaseDatabase = mFirebaseInstance.getReference("/user/messages1");
        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                
                list.clear();
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                Userfire val = dataSnapshot1.getValue(Userfire.class);
                Userfire usr = new Userfire();
                String name = val.getName();
                String email = val.getEmail();
                String date = val.getDate();
                //String uname = val.getUname();
                usr.setName(name);
                usr.setEmail(email);
                usr.setDate(date);
                usr.setUname(val.getUname().toString());
                usr.setMessage(val.getMessage().toString());
                list.add(usr);

            }

                //Collections.reverse(list);
                fAdapter.notifyDataSetChanged();
                rv.smoothScrollToPosition(rv.getAdapter().getItemCount());
                swipeRefreshLayout.setRefreshing(false);
            }
 
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read user", error.toException());
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
 
    private void updateUser(String name, String email) {
        // updating the user via child nodes
        if (!TextUtils.isEmpty(name))
            mFirebaseDatabase.child(userId).child("name").setValue(name);
 
        if (!TextUtils.isEmpty(email))
            mFirebaseDatabase.child(userId).child("email").setValue(email);
    }

     @Override
    protected void onResume() {
        super.onResume();
 
        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));
 
        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));
 
        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }
 
    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }
}




