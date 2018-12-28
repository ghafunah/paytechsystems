package com.paytech.paytechsystems.getset;

public class Teacher {
    private Integer id, status;
    private String fname, sname, phone, email, idno, mname,special,terms,createdat;
    public static final String TABLE_TEACHER = "teacher";
    public static final String T_ID = "id";
    public static final String T_IDNO = "idno";
    public static final String T_FNAME = "fname";
    public static final String T_SNAME = "sname";
    public static final String T_MNAME = "mname";
    public static final String T_EMAIL = "email";
    public static final String T_PHONE = "phone";
    public static final String T_TERMS = "terms";
    public static final String T_STATUS = "status";
    public static final String T_CREATED_AT = "created_at";
    public static final String T_SPECIAL = "special";

    public static final String CREATE_TABLE_TEACHER = "CREATE TABLE "
            + TABLE_TEACHER + "("
            + T_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + T_IDNO  + " TEXT UNIQUE,"
            + T_FNAME + " TEXT,"
            + T_SNAME + " TEXT,"
            + T_MNAME + " TEXT,"
            + T_EMAIL + " TEXT,"
            + T_PHONE + " TEXT UNIQUE,"
            + T_TERMS + " TEXT,"
            + T_SPECIAL + " TEXT,"
            + T_STATUS + " INTEGER,"
            + T_CREATED_AT  + " DATETIME  DEFAULT CURRENT_TIMESTAMP" + ")";

    public Teacher(){
        
    }
    public Teacher(Integer id, String idno, String fname, String sname, String mname, String phone, String email, String terms, String createdat, String special, Integer status) {
        this.id = id;
        this.idno = idno;
        this.fname = fname;
        this.sname = sname;
        this.mname = mname;
        this.phone = phone;
        this.email = email;
        this.terms = terms;
        this.special = special;
        this.status = status;
        this.createdat = createdat;
    }

    public int getId() {   return id;    }
    public String getFname() {  return fname;    }
    public String getSname() {  return sname;    }
    public String getPhone() {  return phone;    }
    public String getMname() {  return mname;    }
    public String getEmail() {  return email;    }
    public String getSpecial() {  return special;    }
    public String getTerms() {  return terms;    }
    public String getIdno() {  return idno;    }
    public int gettStatus() {  return status;    }
    public String getCreatedat() { return createdat; }

    public void setId(Integer bid) {        this.id = id;  }
    public void setFname(String fname) {        this.fname = fname;  }
    public void setSname(String sname) {        this.sname = sname;  }
    public void setPhone(String phone) {        this.phone = phone;  }
    public void setMname(String mname) {        this.mname = mname;  }
    public void setEmail(String email) {        this.email = email;  }
    public void setSpecial(String special) {        this.special = special;  }
    public void setTerms(String terms) {        this.terms = terms;  }
    public void setIdno(String idno) {        this.idno = idno;  }
    public void settCreatedat(String createdat) {        this.createdat = createdat;  }
    public void setStatus(Integer status) {        this.status = status;  }



}
