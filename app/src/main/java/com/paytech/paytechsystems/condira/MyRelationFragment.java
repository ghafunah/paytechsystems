package com.paytech.paytechsystems.condira;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class MyRelationFragment extends RelationListFragment {

    public MyRelationFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // All my posts
        return databaseReference.child("user-member").child(getUid());
    }
}
