package com.paytech.paytechsystems.getset;

public class Branch {
    private Integer id, status;
    private String name, desc, location, phone, email, code, till, created_at;

    public static final String TABLE_BRANCH = "branch";
    public static final String BRANCH_ID = "br_id";
    public static final String BRANCH_CODE = "br_code";
    public static final String BRANCH_NAME = "br_name";
    public static final String BRANCH_DESC = "br_desc";
    public static final String BRANCH_EMAIL = "br_email";
    public static final String BRANCH_TILL = "br_till";
    public static final String BRANCH_PHONE = "br_phone";
    public static final String BRANCH_LOCATION = "br_location";
    public static final String BRANCH_STATUS = "br_status";
    public static final String BRANCH_CREATED_AT = "br_created_at";

    public static final String CREATE_TABLE_BRANCH = "CREATE TABLE "
            + TABLE_BRANCH + "("
            + BRANCH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + BRANCH_CODE  + " TEXT UNIQUE,"
            + BRANCH_NAME + " TEXT UNIQUE,"
            + BRANCH_DESC + " TEXT,"
            + BRANCH_PHONE + " TEXT,"
            + BRANCH_EMAIL + " TEXT,"
            + BRANCH_TILL + " TEXT,"
            + BRANCH_LOCATION + " TEXT,"
            + BRANCH_STATUS + " TEXT,"
            + BRANCH_CREATED_AT  + " DATETIME  DEFAULT CURRENT_TIMESTAMP" + ")";
    public Branch(){
        
    }
    public Branch(Integer id, String name, String code, String desc, String location, String till, String phone,  String email, Integer status) {
        this.name = name;
        this.id = id;
        this.desc = desc;
        this.location = location;
        this.phone = phone;
        this.email = email;
        this.code  = code;
        this.status = status;
        this.till  = till;
        //this.created_at = created_at;
    }

    public int getId() {   return id;    }
    public String getName() {  return name;    }
    public String getDesc() {  return desc;    }
    public String getLocation() {  return location;    }
    public String getPhone() {  return phone;    }
    public String getEmail() {  return email;    }
    public String getTill() {  return till;    }
    public String getCode() {  return code;    }

    public void setId(Integer id) {        this.id = id;  }
    public void setName(String name) {        this.name = name;  }
    public void setDesc(String desc) {        this.desc = desc;  }
    public void setLocation(String location) {        this.location = location;  }
    public void setPhone(String phone) {        this.phone = phone;  }
    public void setEmail(String email) {        this.email = email;  }
    public void setTill(String till) {        this.till = till;  }
    public void setCode(String code) {        this.code = code;  }

}
