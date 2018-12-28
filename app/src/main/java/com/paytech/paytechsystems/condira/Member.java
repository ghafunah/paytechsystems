package com.paytech.paytechsystems.condira;

import com.google.firebase.database.IgnoreExtraProperties;

// [START comment_class]
@IgnoreExtraProperties
public class Member {

    public String uid;
    public String author;
    public String text;

    public Member() {
        // Default constructor required for calls to DataSnapshot.getValue(Member.class)
    }

    public Member(String uid, String author, String text) {
        this.uid = uid;
        this.author = author;
        this.text = text;
    }

}
// [END comment_class]
