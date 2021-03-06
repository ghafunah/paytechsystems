package com.paytech.paytechsystems.condira;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class MyTopRelationFragment extends RelationListFragment {

    public MyTopRelationFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // [START my_top_posts_query]
        // My top posts by number of stars
        String myUserId = getUid();
        Query myTopPostsQuery = databaseReference.child("user-member").child(myUserId)
                .orderByChild("starCount");
        // [END my_top_posts_query]

        return myTopPostsQuery;
    }
}
