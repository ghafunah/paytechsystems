package com.paytech.paytechsystems.condira;

import com.google.firebase.database.IgnoreExtraProperties;

// [START blog_user_class]
@IgnoreExtraProperties
public class NewUser {

    public String username;
    public String email;

    public NewUser() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public NewUser(String username, String email) {
        this.username = username;
        this.email = email;
    }

}
// [END blog_user_class]
