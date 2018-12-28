package com.paytech.paytechsystems.condira;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.paytech.paytechsystems.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class NewRelationActivity extends BaseActivity {

    private static final String TAG = "NewRelationActivity";
    private static final String REQUIRED = "Required";
    private FirebaseRecyclerAdapter<Relation, RelationViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    private ValueEventListener mPostListener;
    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    private DatabaseReference mPostReference;
    private String sNameStr, sAgeStr, sRelation;
    // [END declare_database_ref]

    private EditText mRelation, mAge, mSex;
    private Spinner mSpinnersex, mSpinnerrelation;
    private EditText mName, mNaration;
    private Button mSaveb, mDeleteb;
    private FloatingActionButton mSave;
    public static final String EXTRA_POST_KEY = "post_key";
    public static final String EXTRA_ACTION_KEY = "Action";
    private String mPostKey, mActionKey, sParent, sUid, sNaration;
    public TextView titleView;
    public TextView authorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.factivity_new_post);

        mPostKey = getIntent().getStringExtra(EXTRA_POST_KEY);
        mActionKey = getIntent().getStringExtra(EXTRA_ACTION_KEY);
        if (mActionKey.equals("Edit")) {

        }
        if (mPostKey == null) {
            Toast.makeText(this, "The POST KEY is NULL!", Toast.LENGTH_SHORT).show();
            //throw new IllegalArgumentException("Must pass EXTRA_POST_KEY");
        }else{
            //Toast.makeText(this, "The POST KEY is !" + mPostKey, Toast.LENGTH_SHORT).show();
        }
        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mPostReference = FirebaseDatabase.getInstance().getReference().child("Member").child(mPostKey);
        // [END initialize_database_ref]
        mRecycler = findViewById(R.id.messagesList);
        mRecycler.setHasFixedSize(true);

        mManager = new LinearLayoutManager(getApplicationContext());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        //mRelation = findViewById(R.id.fieldBody);
        mName = findViewById(R.id.fieldTitle);
        mAge = findViewById(R.id.fieldAge);
        mNaration = findViewById(R.id.fieldNaration);
        mSpinnersex = (Spinner) findViewById(R.id.spinnersex);
        mSpinnerrelation = (Spinner) findViewById(R.id.spinnerrelation);
        titleView = findViewById(R.id.postTitle);
        authorView = findViewById(R.id.postBody);
        //mSave = findViewById(R.id.fabSubmitPost);
        mSaveb = findViewById(R.id.buttonSave);
        mDeleteb = findViewById(R.id.buttonDelete);

        List<String> sex = new ArrayList<String>();
        sex.add("Select Sex");
        sex.add("Male");
        sex.add("Female");

        List<String> relation = new ArrayList<String>();
        relation.add("Select Relation");
        relation.add("Son");
        relation.add("Daughter");
        relation.add("Father");
        relation.add("Wife");


        ArrayAdapter<String> sexAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sex);
        ArrayAdapter<String> relationAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, relation);
        // Drop down layout style - list view with radio button
        sexAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        relationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        mSpinnersex.setAdapter(sexAdapter);
        mSpinnerrelation.setAdapter(relationAdapter);


        mSaveb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPost();
            }
        });
        mDeleteb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText((this, "Delete action here!", Toast.LENGTH_SHORT).show();
                deleteRelation(mPostKey);
            }
        });
    


            // Set up FirebaseRecyclerAdapter with the Query
        Query postsQuery  = getQuery(mDatabase);

        Log.d(TAG, "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"+ postsQuery.toString());

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Relation>()
                .setQuery(postsQuery, Relation.class)
                .build();

        mAdapter = new FirebaseRecyclerAdapter<Relation, RelationViewHolder>(options) {

            @Override
            public RelationViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new RelationViewHolder(inflater.inflate(R.layout.fitem_post, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(RelationViewHolder viewHolder, int position, final Relation model) {
                final DatabaseReference postRef = getRef(position);

                // Set click listener for the whole post view
                final String postKey = postRef.getKey();
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Launch PostDetailActivity
                        Intent intent = new Intent(getApplicationContext(), NewRelationActivity.class);
                        intent.putExtra(RelationDetailActivity.EXTRA_POST_KEY, postKey);
                        intent.putExtra("Action", "New Child");
                        startActivity(intent);
                        finish();
                    }
                });

                // Determine if the current user has liked this post and set UI accordingly
                if (model.stars.containsKey(getUid())) {
                    viewHolder.starView.setImageResource(R.drawable.ic_toggle_star_24);
                } else {
                    viewHolder.starView.setImageResource(R.drawable.ic_toggle_star_outline_24);
                }

                // Bind Post to ViewHolder, setting OnClickListener for the star button
                viewHolder.bindToPost(model, new View.OnClickListener() {
                    @Override
                    public void onClick(View starView) {
                        // Need to write to both places the post is stored
                        DatabaseReference globalPostRef = mDatabase.child("Member").child(postRef.getKey());
                        DatabaseReference userPostRef = mDatabase.child("user-member").child(model.uid).child(postRef.getKey());

                        // Run two transactions
                        onStarClicked(globalPostRef);
                        onStarClicked(userPostRef);
                    }
                });
            }
        };
        mRecycler.setAdapter(mAdapter);
    }

    private void deleteRelation(String delkey){
        Toast.makeText(this, "Ready to delete " + delkey, Toast.LENGTH_SHORT).show();
        String relation = mDatabase.child("/Member/").child(delkey).toString();
        Timber.d("-------------------------------------------------- Found "+ relation);
        mDatabase.child("/Member/").child(delkey).removeValue();
        mDatabase.child("/Relation/").child(delkey).removeValue();
        mDatabase.child("/user-member/").child(getUid()).child(delkey).removeValue();

        Intent intent = new Intent(getApplicationContext(), SignInActivity2.class);
        //intent.putExtra(RelationDetailActivity.EXTRA_POST_KEY, postKey);
        //intent.putExtra("Action", "New Child");
        startActivity(intent);
        finish();

    }


    private void submitPost() {
        final String name = mName.getText().toString();
        //final String relation = mRelation.getText().toString().toLowerCase().trim();
        final String age = mAge.getText().toString();
       // final String sex = mSex.getText().toString().toUpperCase().trim();
        final String sRelation = mSpinnerrelation.getSelectedItem().toString().toUpperCase().trim();
        final String sSex = mSpinnersex.getSelectedItem().toString().toUpperCase().trim();
        final  String sNaration = mNaration.getText().toString();

        // Title is required
        if (TextUtils.isEmpty(name)) {
            mName.setError(REQUIRED);
            return;
        }

        // Body is required
//        if (TextUtils.isEmpty(relation)) {
//            mRelation.setError(REQUIRED);
//            return;
//        }
//        if (TextUtils.isEmpty(sex)) {
//            mSex.setError(REQUIRED);
//            return;
//        }
        if (TextUtils.isEmpty(sSex) || sSex.equals("SELECT SEX")) {
            mSpinnerrelation.setPrompt(REQUIRED);//.setError(REQUIRED);
            Toast.makeText(this, "Please select sex", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(sRelation) || sRelation.equals("SELECT RELATION")) {
            mSpinnerrelation.setPrompt(REQUIRED);//.setError(REQUIRED);
            Toast.makeText(this, "Please select relationship", Toast.LENGTH_SHORT).show();
            return;
        }

        // Disable button so there are no multi-posts
        setEditingEnabled(false);
        Toast.makeText(this, "Saving ...", Toast.LENGTH_SHORT).show();

        // [START single_value_read]
        final String userId = getUid();
        mDatabase.child("users").child(userId).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        NewUser user = dataSnapshot.getValue(NewUser.class);

                        // [START_EXCLUDE]
                        if (user == null) {
                            // User is null, error out
                            Log.e(TAG, "User " + userId + " is unexpectedly null");
                            Toast.makeText(NewRelationActivity.this, "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Write new post
                            if (mActionKey.equals("Edit")) {
                              editMember(userId, user.username, name, sRelation, age, sSex, sParent, sNaration);
                            }else{
                            writeNewPost(userId, user.username, name, sRelation, mPostKey, age, sSex, sNaration);}
                        }

                        // Finish this Activity, back to the stream
                        setEditingEnabled(true);
                        finish();
                        // [END_EXCLUDE]
                    }
//                    @Override
//                    public void onChildRemoved(DataSnapshot dataSnapshot){
//
//                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        // [START_EXCLUDE]
                        setEditingEnabled(true);
                        // [END_EXCLUDE]
                    }
                });
        // [END single_value_read]
    }

    private void setEditingEnabled(boolean enabled) {
        mName.setEnabled(enabled);
        //mRelation.setEnabled(enabled);
        if (enabled) {
            mSaveb.setVisibility(View.VISIBLE);
        } else {
            mSaveb.setVisibility(View.GONE);
        }
    }

    // [START write_fan_out]
    private void writeNewPost(String userId, String username, String name, String relation, String mPostKey, String age, String sex, String sNaration) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = mDatabase.child("Member").push().getKey();
        if (sex.equals("MALE")){
            sRelation = "SON";
        }else{
            sRelation = "DAUGHTER";
        }
        Relation post = new Relation(userId, username, name.trim(), relation.toLowerCase().trim(), age, sex, mPostKey, sNaration);
        Relation postOlder = new Relation(userId, username, sNameStr, sRelation, sAgeStr,sex, sParent, null);
        Map<String, Object> postValues = post.toMap();
        Map<String, Object> postValuesOlder = postOlder.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        if (relation.equals("FATHER")) {
            //childUpdates.put("/Member/" + key, postValuesOlder);
        }else if (relation.equals("SON")){
            //childUpdates.put("/Member/" + key, postValues);
        }
        //childUpdates.put("/Member/"+mPostKey+"/"+relation+"", true);

        childUpdates.put("/user-member/" + userId + "/" + key, postValues);
        if (relation.equals("FATHER")) {
            childUpdates.put("/Member/" + key, postValues);
            childUpdates.put("/Relation/" + key + "/" + mPostKey, postValuesOlder);
            mDatabase.child("/Member/" + key + "/" + mRelation + "/" + key).setValue(true);
            mDatabase.child("/Member/"+mPostKey).child("parent").setValue(key);
            mDatabase.child("/Member/"+mPostKey).child("relation").setValue("son");
            //mDatabase.child("/Member/" + key + "/").child("SON").child(mPostKey).setValue(true);
            mDatabase.child("/Relation/"+ key +"/" +mPostKey).child("parent").setValue(key);
        }else if (relation.equals("SON")){
            childUpdates.put("/Member/" + key, postValues);
            childUpdates.put("/Relation/" + mPostKey + "/" + key, postValues);
            mDatabase.child("/Relation/"+mPostKey).child("parent").setValue(key);//set parent value
            mDatabase.child("/Member/" + mPostKey + "/" + relation + "/" + key).setValue(true);
        }else{
            childUpdates.put("/Relation/" + mPostKey + "/" + key, postValues);
            if (relation.toUpperCase().equals("DAUGHTER")) {
                //mDatabase.child("/Relation/" + mPostKey+"/"+key).child("parent").setValue(key);//set parent value
            }
            mDatabase.child("/Member/" + mPostKey + "/" + relation + "/" + key).setValue(true);
        }

        if (mPostKey != null) {
            if (sex == "MALE"){
            //mDatabase.child("/Member/" + mPostKey + "/" + relation + "/" + key).setValue(true);
            }

        }

        mDatabase.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });

        if (relation.equals("FATHER")) {
            mDatabase.child("/Member/" + key + "/").child("SON").child(mPostKey).setValue(true);
            mDatabase.child("/Member/" + key).child("parent").removeValue();
        }
        //mDatabase.child("/Member/" +key+ "/" +mPostKey).setValue(true);
        mPostKey = null;
        key = null;
    }
    // [END write_fan_out]
    private void editMember(String userId, String username, String name, String relation, String age, String sex, String sParent, String sNaration) {
        Relation post = new Relation(userId, username, name.trim(), relation.toLowerCase().trim(), age, sex, null, sNaration);
        Map<String, Object> postValues = post.toMap();
        Map<String, Object> childUpdates = new HashMap<>();


        childUpdates.put("/Member/" + mPostKey + "/", postValues);
        childUpdates.put("/user-member/"+sUid+"/"+mPostKey, postValues);
        mDatabase.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });
        if (sParent != null) {

            childUpdates.put("/Relation/"+sParent+"/"+mPostKey, postValues);

        }

        mPostKey = null;
        sParent = null;

    }
    // [START post_stars_transaction]
    private void onStarClicked(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Relation p = mutableData.getValue(Relation.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }

                if (p.stars.containsKey(getUid())) {
                    // Unstar the post and remove self from stars
                    p.starCount = p.starCount - 1;
                    p.stars.remove(getUid());
                } else {
                    // Star the post and add self to stars
                    p.starCount = p.starCount + 1;
                    p.stars.put(getUid(), true);
                }

                // Set value and report transaction success
                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d(TAG, "postTransaction:onComplete:" + databaseError);
            }
        });
    }
    

    public Query getQuery(DatabaseReference databaseReference) {
        // [START my_top_posts_query]
        // My top posts by number of stars
        String myUserId = mPostKey;//getUid();
        Query myTopPostsQuery = databaseReference.child("Relation/"+myUserId);
        //.child(myUserId).orderByChild("starCount");
        // [END my_top_posts_query]

        return myTopPostsQuery;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAdapter != null) {
            mAdapter.startListening();
        }

        // Add value event listener to the post
        // [START post_value_event_listener]

            ChildEventListener childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Toast.makeText(getApplicationContext(), "Child added : " + dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Toast.makeText(getApplicationContext(), "Child Changed : " + dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    Toast.makeText(getApplicationContext(), "Child Removed : " + dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Toast.makeText(getApplicationContext(), "Child Moved : " + dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Relation post = dataSnapshot.getValue(Relation.class);
                // [START_EXCLUDE]
                titleView.setText("Girls do not add to Family Line!");
                titleView.setTextColor(getResources().getColor(R.color.colorPrimary));
                titleView.setAllCaps(true);
                if (post != null){
                    //Log.d(TAG, "+++++++++++++++++++++++++++++++++++++"+ post.toString());
                        sAgeStr = post.age;
                        sNameStr = post.name;
                    if (mActionKey.equals("Edit")) {
                        titleView.setText("Name : "+ post.name);
                        authorView.setText("Age : "+ post.age);
                        mAge.setText(sAgeStr);
                        mName.setText(sNameStr);
                        mNaration.setText(post.naration);
                        sParent = post.parent;
                        mSpinnersex.setPrompt(post.sex);
                        //mDeleteb.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                        //mRelation.setText(post.relation);
                        sUid = post.uid;
                        mSaveb.setText("Modify");
                    }else{
                        titleView.setText("Name : "+ post.name + " ("+ post.age+")");
                        authorView.setText("Relation : "+ post.relation);
                        mDeleteb.setVisibility(View.GONE);
                    }


                }
                if (mPostKey!=null && post==null){
                    mSaveb.setVisibility(View.GONE);
                    authorView.setVisibility(View.GONE);
                    //titleView.setVisibility(View.GONE);
                }
                //mBodyView.setText(post.relation);
                // [END_EXCLUDE]
            }

//            @Override
//            public  void onChildChanged(DataSnapshot dataSnapshot, String previousChildName){
//
//            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                Toast.makeText(NewRelationActivity.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };
        mPostReference.addValueEventListener(postListener);
        mPostListener = postListener;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
        if (mPostListener != null) {
            mPostReference.removeEventListener(mPostListener);
        }
    }
}
