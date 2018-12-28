package com.paytech.paytechsystems.getset;

public class Mpesa {
    private Integer id;
    private String service, tref, phone, fname, ttime, amount, lname;

    public static final String TABLE_MPESA = "mpesa";

    public static final String M_SERVICE = "service";
    public static final String M_TREF = "tref";
    public static final String M_ID = "id";
    public static final String M_PHONE = "phone";
    public static final String M_FNAME = "fname";
    public static final String M_LMANE = "lname";
    public static final String M_AMOUNT = "amount";
    public static final String M_TTIME = "ttime";
    public static final String M_STATUS = "status";
    public static final String M_CREATED_AT = "created_at";

    public static final String CREATE_TABLE_MPESA = "CREATE TABLE "
            + TABLE_MPESA + "("
            + M_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + M_TREF  + " TEXT UNIQUE,"
            + M_SERVICE + " TEXT,"
            + M_PHONE + " TEXT,"
            + M_FNAME + " TEXT,"
            + M_LMANE + " TEXT,"
            + M_AMOUNT + " TEXT,"
            + M_TTIME  + " DATETIME,"
            + M_STATUS + " INTEGER,"
            + M_CREATED_AT  + " DATETIME  DEFAULT CURRENT_TIMESTAMP" + ")";
    public Mpesa(){
        
    }
    public Mpesa(Integer id, String service, String lname, String tref, String phone, String fname,  String ttime,  String amount) {
        this.id = id;
        this.service = service;
        this.lname = lname;
        this.phone = phone;
        this.fname = fname;
        this.tref = tref;
        this.ttime = ttime;
        this.amount  = amount;
    }

    public int getId() {   return id;    }
    public String getService() {  return service;    }
    public String getLname() {  return lname;    }
    public String getPhone() {  return phone;    }
    public String getTref() { return tref; }
    public String getFname() {  return fname;    }
    public String getTtime() {  return ttime;    }
    public String getAmount() {  return amount;    }

    public void setId(Integer id) {        this.id = id;  }
    public void setService(String service) {        this.service = service;  }
    public void setLname(String lname) {        this.lname = lname;  }
    public void setPhone(String phone) {        this.phone = phone;  }
    public void setTref(String tref) {        this.tref = tref;  }
    public void setFname(String fname) {        this.fname = fname;  }
    public void setTtime(String lname) {        this.ttime = lname;  }
    public void setAmount(String amount) {        this.amount  = amount;  }

}
