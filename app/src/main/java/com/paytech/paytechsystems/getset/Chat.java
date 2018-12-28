package com.paytech.paytechsystems.getset;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Ravi Tamada on 07/10/16.
 * www.androidhive.info
 */

@IgnoreExtraProperties
public class Chat {

    public String name;
    public String email;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Chat() {
    }

    public Chat(String name, String email) {
        this.name = name;
        this.email = email;
    }
}