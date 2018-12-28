package com.paytech.paytechsystems;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
 
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import com.paytech.paytechsystems.condira.NewRelationActivity;
import com.paytech.paytechsystems.condira.Relation;
import com.paytech.paytechsystems.getset.Barcodes;
import com.paytech.paytechsystems.helper.Controller;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TicketResultActivity extends AppCompatActivity {
    private static final String TAG = TicketResultActivity.class.getSimpleName();

    // url to search barcode
    private static final String URL = "https://api.androidhive.info/barcodes/search.php?code=";

    private TextView txtName, txtDuration, txtDirector, txtGenre, txtRating, txtPrice, txtError;
    private TextView barcodeV, nameV, locationV, siteV;
    private ImageView imgPoster;
    private Button btnBuy;
    private ProgressBar progressBar;
    private ValueEventListener mPostListener;
    private TicketView ticketView;
    private String barcode;
    private String mPostKey;
    private DatabaseReference mDatabase, mPostReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_result);

        // Toolbar toolbar = findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtName = findViewById(R.id.name);
        txtDirector = findViewById(R.id.director);
        txtDuration = findViewById(R.id.duration);
        txtPrice = findViewById(R.id.price);
        txtRating = findViewById(R.id.rating);
        imgPoster = findViewById(R.id.poster);
        txtGenre = findViewById(R.id.genre);
        btnBuy = findViewById(R.id.btn_buy);
        imgPoster = findViewById(R.id.poster);
        txtError = findViewById(R.id.txt_error);
        ticketView = findViewById(R.id.layout_ticket);
        progressBar = findViewById(R.id.progressBar);
        barcode = getIntent().getStringExtra("code");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mPostReference = FirebaseDatabase.getInstance().getReference().child("Barcodes").child(barcode);


        //Toast.makeText(getApplicationContext(), "Barcode is empty!" + barcode, Toast.LENGTH_LONG).show();
        // close the activity in case of empty barcode
        if (TextUtils.isEmpty(barcode)) {
            Toast.makeText(getApplicationContext(), "Barcode is empty!", Toast.LENGTH_LONG).show();
            finish();
        }

        // search the barcode
        //searchBarcode(barcode);
        showDetails(barcode);
    }

    /**
     * Searches the barcode by making http call
     * Request was made using Volley network library but the library is
     * not suggested in production, consider using Retrofit
     */
    private void searchBarcode(String barcode) {
        // making volley's json request
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                URL + barcode, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "Ticket response: " + response.toString());

                        // check for success status
                        if (!response.has("error")) {
                            // received movie response
                            renderMovie(response);
                        } else {
                            // no movie found
                            showNoTicket();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                showNoTicket();
            }
        });

        Controller.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void showNoTicket() {
//        String key = mDatabase.push().getKey();
//        String bname = "Item name is this";
//        Barcodes barcodes = new Barcodes(barcode, bname);
//        Map<String, Object> postValues = barcodes.toMap();
//        Map<String, Object> childUpdates = new HashMap<>();
//
//        childUpdates.put("/Barcodes/"+key+"/",postValues);
//        //mDatabase.child(key).setValue(barcodes);
//        mDatabase.updateChildren(childUpdates);
        newBarcode();
        txtError.setVisibility(View.VISIBLE);
        //txtError.setText("Barcode : " + barcode + " Not found!");
        ticketView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        // Intent intent = new Intent(TicketResultActivity.this, NewTicketActivity.class);
        // intent.putExtra("code", barcode);
        // startActivity(intent);
    }

    /**
     * Rendering movie details on the ticket
     */
    private void renderMovie(JSONObject response) {
        try {

            // converting json to movie object
            Movie movie = new Gson().fromJson(response.toString(), Movie.class);

            if (movie != null) {
                txtName.setText(movie.getName());
                txtDirector.setText(movie.getDirector());
                txtDuration.setText(movie.getDuration());
                txtGenre.setText(movie.getGenre());
                txtRating.setText("" + movie.getRating());
                txtPrice.setText(movie.getPrice());
                Glide.with(this).load(movie.getPoster()).into(imgPoster);

                if (movie.isReleased()) {
                    btnBuy.setText(getString(R.string.btn_buy_now));
                    btnBuy.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                } else {
                    btnBuy.setText(getString(R.string.btn_coming_soon));
                    btnBuy.setTextColor(ContextCompat.getColor(this, R.color.btn_disabled));
                }
                ticketView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            } else {
                // movie not found
                showNoTicket();
            }
        } catch (JsonSyntaxException e) {
            Log.e(TAG, "JSON Exception: " + e.getMessage());
            showNoTicket();
            Toast.makeText(getApplicationContext(), "Error occurred. Check your LogCat for full report", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            // exception
            showNoTicket();
            Toast.makeText(getApplicationContext(), "Error occurred. Check your LogCat for full report", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private void newBarcode() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.new_barcode, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(TicketResultActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText itembarcode = view.findViewById(R.id.itembarcode);
        final TextView itemname = view.findViewById(R.id.itemname);
        final EditText itemlocation = view.findViewById(R.id.itemlocation);
        final EditText itemsite = view.findViewById(R.id.itemsite);

        //final Spinner sex = view.findViewById(R.id.usrsex);
        //sex.setAdapter(dataAdapter);
        itembarcode.setText(barcode);
        //sex.setBackgroundResource(R.color.white);
        //dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_new_note_title) : getString(R.string.lbl_edit_note_title));

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.setTitle("New Barcode");
        //alertDialog.setIcon(R.drawable.ic_action_add_person);
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show toast message when no text is entered
                if (TextUtils.isEmpty(itembarcode.getText().toString()) || TextUtils.isEmpty(itemname.getText().toString())) {
                    Toast.makeText(TicketResultActivity.this, "Barcode and name cannot be empty!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }
                String key = mDatabase.push().getKey();
                String sitemname = itemname.getText().toString();
                String sitemlocation = itemlocation.getText().toString();
                String sitemsite = itemsite.getText().toString();

                Barcodes barcodes = new Barcodes(barcode, sitemname, sitemlocation, sitemsite);
                Map<String, Object> postValues = barcodes.toMap();
                Map<String, Object> childUpdates = new HashMap<>();

                childUpdates.put("/Barcodes/" + barcode + "/", postValues);
                //mDatabase.child(key).setValue(barcodes);
                mDatabase.updateChildren(childUpdates);
                //Double am = admno.getText();
//                db.saveUser(idno.getText().toString(), fname.getText().toString(),
//                        sname.getText().toString(), uname.getText().toString(), email.getText().toString(),
//                        branch.getText().toString(), password.getText().toString(), phone.getText().toString(), sex.getSelectedItem().toString(),"Cashier", 0);

                Intent intent = new Intent(TicketResultActivity.this, ScanActivity.class);
                //intent.putExtra("code", barcode.displayValue);
                startActivity(intent);
            }
        });
    }

    private void showDetails(final String barcode) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.barcode_details, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(TicketResultActivity.this);
        alertDialogBuilderUserInput.setView(view);

        barcodeV = view.findViewById(R.id.barcode);
        nameV = view.findViewById(R.id.name);
        locationV = view.findViewById(R.id.location);
        siteV = view.findViewById(R.id.site);
        //Student v = db.getStudent(position);
        //title.setText("Details For : " +v.getFirstname() +" " +v.getSurname());
        //dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_new_note_title) : getString(R.string.lbl_edit_note_title));

        mPostReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Barcodes post = dataSnapshot.getValue(Barcodes.class);
                // [START_EXCLUDE]

                if (post != null) {
                    Log.d(TAG, " MYPOST " +post.toString());
                    Log.d(TAG, "+++++++++++++++++++++++++++++++++++++"+ post.toString());
                    barcodeV.setTextColor(getResources().getColor(R.color.colorPrimary));
                    barcodeV.setAllCaps(true);
                    barcodeV.setText("Barcode : " + post.getBarcode());
                    nameV.setText("Item Name : " + post.getBname());
                    locationV.setText("Item Location : " + post.getLocation());
                    siteV.setText("Item Site : " + post.getSite());

                }
                if (barcode != null && post == null) {
                    newBarcode();
                    // mSaveb.setVisibility(View.GONE);
                    //authorView.setVisibility(View.GONE);
                    //titleView.setVisibility(View.GONE);
                }
                //mBodyView.setText(post.history);
                // [END_EXCLUDE]
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                Toast.makeText(TicketResultActivity.this, "Failed to load barcode.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        });
        //mPostReference.addValueEventListener(postListener);
        //mPostListener =postListener;

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        dialogBox.dismiss();
                        newBarcode();
                       // Intent intent = new Intent(TicketResultActivity.this, ScanActivity.class);
                        //intent.putExtra("code", barcode.displayValue);
                        //startActivity(intent);
                    }
                })
                .setNegativeButton("Close",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                                Intent intent = new Intent(TicketResultActivity.this, ScanActivity.class);
                                //intent.putExtra("code", barcode.displayValue);
                                startActivity(intent);
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show toast message when no text is entered
                if (TextUtils.isEmpty(barcodeV.getText().toString())) {
                    Toast.makeText(TicketResultActivity.this, "Enter note!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                // check if user updating note
                if (v != null) {
                    // update note by it's id
                    Toast.makeText(TicketResultActivity.this, "Update Note!", Toast.LENGTH_SHORT).show();
                    //updateNote(inputNote.getText().toString(), position);
                    newBarcode();
                } else {
                    // create new note
                    Toast.makeText(TicketResultActivity.this, "Create note!", Toast.LENGTH_SHORT).show();
                    //createNote(inputNote.getText().toString());
                }
            }
        });
    }

    private class Movie {
        String name;
        String director;
        String poster;
        String duration;
        String genre;
        String price;
        float rating;

        @SerializedName("released")
        boolean isReleased;

        public String getName() {
            return name;
        }

        public String getDirector() {
            return director;
        }

        public String getPoster() {
            return poster;
        }

        public String getDuration() {
            return duration;
        }

        public String getGenre() {
            return genre;
        }

        public String getPrice() {
            return price;
        }

        public float getRating() {
            return rating;
        }

        public boolean isReleased() {
            return isReleased;
        }
    }

    @Override
    public void onStart() {
        super.onStart();


}
    @Override
    public void onStop() {
        super.onStop();
//        if (mAdapter != null) {
//            mAdapter.stopListening();
//        }
        if (mPostListener != null) {
            mPostReference.removeEventListener(mPostListener);
        }
    }
}