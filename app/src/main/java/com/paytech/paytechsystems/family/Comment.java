package com.paytech.paytechsystems.family;

import com.google.firebase.database.IgnoreExtraProperties;

// [START comment_class]
@IgnoreExtraProperties
public class Comment {

    public String uid;
    public String author;
    public String text, name, history;

    public Comment() {
        // Default constructor required for calls to DataSnapshot.getValue(Comment.class)
    }

    public Comment(String uid, String author, String text, String name, String history) {
        this.uid = uid;
        this.author = author;
        this.text = text;
        this.history = history;
        this.name = name;
    }

}
// [END comment_class]
