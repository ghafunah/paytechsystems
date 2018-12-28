package com.paytech.paytechsystems.getset;

public class Vehicle {
    private Integer id, status;
    private String regno, make, type, dateadd;

    public static final String TABLE_VEHICLE = "vehicle";
    //Vehicle table fields
    public static final String V_ID = "v_id";
    public static final String V_MAKE = "v_make";
    public static final String V_TYPE = "v_type";
    public static final String V_REGNO = "v_regno";
    public static final String V_STATUS = "v_status";
    public static final String V_CREATED_AT = "v_created_at";

    public static final String CREATE_TABLE_VEHICLE = "CREATE TABLE "
            + TABLE_VEHICLE + "("
            + V_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + V_REGNO  + " TEXT UNIQUE,"
            + V_MAKE + " TEXT,"
            + V_TYPE + " TEXT,"
            + V_STATUS + " INTEGER,"
            + V_CREATED_AT  + " DATETIME  DEFAULT CURRENT_TIMESTAMP" + ")";

    public Vehicle(){
        
    }
    public Vehicle(Integer id, String regno, String make, String type, String dateadd, Integer status) {
        this.regno = regno;
        this.make = make;
        this.type = type;
        this.dateadd = dateadd;
        this.status = status;
        this.id = id;
    }

    public int getId() {   return id;    }
    public String getRegno() {  return regno;    }
    public String getMake() {  return make;    }
    public String getType() {  return type;    }
    public String getDateadd() {  return dateadd;    }
    public Integer getStatus() {  return status;    }


    public void setId(Integer id) {        this.id = id;  }
    public void setRegno(String regno) {        this.regno = regno;  }
    public void setMake(String make) {        this.make = make;  }
    public void setType(String type) {        this.type = type;  }
    public void setDateadd(String dateadd) {        this.dateadd = dateadd;  }
    public void setStatus(Integer status) {        this.status = status;  }

}
