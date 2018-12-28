package com.paytech.paytechsystems.getset;
 
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Userfire {
 
    public String name;
    public String email, date, uname, message;
    public long dates;
 
    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Userfire() {
    }
 
    public Userfire(String message, String email, String date, String uname, String name) {
        this.name = name;
        this.email = email;
        this.date = date;
        this.uname = uname;
        this.message  = message;
    }
    // public String getId() {  return id;    }
    public String getName() {  return name;    }    
    public String getEmail() {  return email;    }
    public String getDate() {  return date;    }
    public String getUname() {  return uname;    }
    public String getMessage() {  return message;    }

    // public void setId(Integer id) {        this.id = id;  }
    public void setName(String name) {        this.name = name;  }
    public void setEmail(String email) {        this.email = email;  }
    public void setDate(String date) {        this.date = date;  }
    public void setUname(String uname) {        this.uname = uname;  }
    public void setMessage(String message) {        this.message = message;  }
}