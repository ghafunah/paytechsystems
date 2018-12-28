package com.paytech.paytechsystems.getset;

/**
 * Created by Belal on 1/27/2017.
 */
import com.google.firebase.database.IgnoreExtraProperties;


@IgnoreExtraProperties
public class User {
    private Integer stfid, status, id;
    private String idno, fname, sname, uname, branch, password, phone, email, gender, role;

    public static final String TABLE_LOGIN = "user";
    // Login Table Columns names
    public static final String USER_ID= "user_id";
    public static final String USER_FNAME = "user_fname";
    public static final String USER_IDNO = "user_idno";
    public static final String USER_SNAME = "user_sname";
    public static final String USER_EMAIL = "user_email";
    public static final String USER_BRANCH = "user_branch";
    public static final String USER_UNAME = "user_uname";
    public static final String USER_PHONE = "user_phone";
    public static final String USER_PASSWORD = "user_password";
    public static final String USER_GENDER = "user_gender";
    public static final String USER_ROLE = "user_role";
    public static final String USER_STATUS = "user_status";
    public static final String USER_CREATED_AT = "user_created_at";



    public static final String CREATE_TABLE_LOGIN = "CREATE TABLE " + TABLE_LOGIN + "("
            + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + USER_IDNO + " TEXT,"
            + USER_FNAME + " TEXT,"
            + USER_SNAME + " TEXT,"
            + USER_UNAME + " TEXT UNIQUE, "
            + USER_BRANCH + " INTEGER, "
            + USER_EMAIL + " TEXT, "
            + USER_PASSWORD + " TEXT, "
            + USER_GENDER + " TEXT, "
            + USER_ROLE + " TEXT, "
            + USER_PHONE + " TEXT, "
            + USER_STATUS + " TINYINT,"
            + USER_CREATED_AT  + " DATETIME  DEFAULT CURRENT_TIMESTAMP" + ")";

    public User(){
        
    }

    public User(Integer id, String idno, String fname, String sname, String uname, String email, String phone, String password, String branch, String gender, String role, Integer status) {
        this.id = id;
        this.idno = idno;
        this.fname = fname;
        this.sname = sname;
        this.uname = uname;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.branch = branch;
        this.gender = gender;
        this.role = role;
        this.status = status;
    }

    public User(Integer id,String fname, String sname, String uname, String email) {
        this.id = id;
        this.fname = fname;
        this.sname = sname;
        this.uname = uname;
        this.email = email;
    }

    public Integer getId() {   return id;    }
    public String getIdno() {   return idno;    }
    public String getFname() {  return fname;    }   
    public String getSname() {  return sname;    }
    public String getUname() {  return uname;    }
    public String getBranch() {  return branch;    }
    public String getEmail() {  return email;    }
    public String getPassword() {  return password;    }
    public String getRole() {  return role;    }
    public String getPhone() {  return phone;    }    
    public Integer getStatus() {  return status;    }

    public void setId(Integer id) {        this.id = id;  }
    public void setIdno(String idno) {        this.idno = idno;  }
    public void setFname(String fname) {        this.fname = fname;  }
    public void setSname(String sname) {        this.sname = sname;  }
    public void setUname(String uname) {        this.uname = uname;  }
    public void setBranch(String branch) {        this.branch = branch;  }
    public void setEmail(String email) {        this.email = email;  }
    public void setPhone(String phone) {        this.phone = phone;  }
    public void setPassword(String password) {        this.password = password;  }
    public void setGender(String gender) {        this.gender = gender;  }
    public void setRole(String role) {        this.role = role;  }
    public void setStatus(Integer status) {        this.status = status;  }


}
