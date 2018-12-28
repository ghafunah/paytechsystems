package com.paytech.paytechsystems;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import timber.log.Timber;

import static com.paytech.paytechsystems.helper.Controller.TAG;

public class MessageBox extends Activity implements OnClickListener {
 
    //  GUI Widget 
    Button btnSent, btnInbox, btnDraft;
    TextView lblMsg, lblNo;
    ListView lvMsg;
 
    // Cursor Adapter
    SimpleCursorAdapter adapter;
 
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mysms);
 
        // Init GUI Widget
        btnInbox = (Button) findViewById(R.id.btnInbox);
        btnInbox.setOnClickListener(this);
 
        btnSent = (Button)findViewById(R.id.btnSentBox);
        btnSent.setOnClickListener(this);
 
        btnDraft = (Button)findViewById(R.id.btnDraft);
        btnDraft.setOnClickListener(this);
 
        lvMsg = (ListView) findViewById(R.id.lvMsg);
 
    }
 
    @Override
    public void onClick(View v) {
 
        if (v == btnInbox) {
 
            // Create Inbox box URI
            Uri inboxURI = Uri.parse("content://sms/inbox");
 
            // List required columns
            String[] reqCols = new String[] { "_id", "address", "body", "person","date" };
            Timber.d("My Messages are : %s", reqCols);
            // Get Content Resolver object, which will deal with Content Provider
            ContentResolver cr = getContentResolver();
 
            // Fetch Inbox SMS Message from Built-in Content Provider
            Cursor c = cr.query(inboxURI, reqCols, null, null, null);
 
            // Attached Cursor with adapter and display in listview
            adapter = new SimpleCursorAdapter(this, R.layout.msg_list_row, c,
                    new String[] { "body", "address", "person" , "date"}, new int[] {
                            R.id.lblMsg, R.id.lblNumber, R.id.person, R.id.date });
            lvMsg.setAdapter(adapter);
 
        }
 
        if(v==btnSent)
        {
 
            // Create Sent box URI
            Uri sentURI = Uri.parse("content://sms/sent");
 
            // List required columns
            String[] reqCols = new String[] { "_id", "address", "body", "person" , "date" };
 
            // Get Content Resolver object, which will deal with Content Provider
            ContentResolver cr = getContentResolver();
 
            // Fetch Sent SMS Message from Built-in Content Provider
            Cursor c = cr.query(sentURI, reqCols, null, null, null);
 
            // Attached Cursor with adapter and display in listview
            adapter = new SimpleCursorAdapter(this, R.layout.msg_list_row, c,
                    new String[] { "body", "address", "person" , "date" }, new int[] {
                            R.id.lblMsg, R.id.lblNumber, R.id.person, R.id.date });
            lvMsg.setAdapter(adapter);
 
        }
 
        if(v==btnDraft)
        {
            // Create Draft box URI
            Uri draftURI = Uri.parse("content://sms/draft");
 
            // List required columns
            String[] reqCols = new String[] { "_id", "address", "body", "person" , "date" };
 
            // Get Content Resolver object, which will deal with Content Provider
            ContentResolver cr = getContentResolver();
 
            // Fetch Sent SMS Message from Built-in Content Provider
            Cursor c = cr.query(draftURI, reqCols, null, null, null);
 
            // Attached Cursor with adapter and display in listview
            adapter = new SimpleCursorAdapter(this, R.layout.msg_list_row, c,
                    new String[] { "body", "address", "person" , "date" }, new int[] {
                            R.id.lblMsg, R.id.lblNumber, R.id.person, R.id.date });
            lvMsg.setAdapter(adapter);
 
        }
 
    }
    
    public static long getTimeMilliSec(String timeStamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(timeStamp);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public CharSequence timeAgo(String time){
        //CharSequence timeAgo = '';
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        SimpleDateFormat std = new SimpleDateFormat("yyyy-MM-dd");
        std.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            long now = std.parse(time).getTime();
            CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(now, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
            return  timeAgo;
        }catch (Exception e){
            Log.d(TAG,"TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT");
        }

        //timestamp.setText(timeAgo);
        return "";
    }
}