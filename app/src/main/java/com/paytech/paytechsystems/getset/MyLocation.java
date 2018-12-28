package com.paytech.paytechsystems.getset;

public class MyLocation {
    private Integer id;
    private Double latitude, longitude;
    String updatetime;
    
    // public static final String TABLE_LESSON = "lesson";
    // public static final String L_ID = "id";
    // public static final String L_COURSE = "course";
    // public static final String L_DESC = "lesson";
    // public static final String L_STATUS = "status";
    // public static final String L_CREATED_AT = "created_at";

    // public static final String CREATE_TABLE_LESSON = "CREATE TABLE "
    //         + TABLE_LESSON + "("
    //         + L_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
    //         + L_COURSE  + " TEXT,"
    //         + L_DESC + " TEXT,"
    //         + L_STATUS + " INTEGER,"
    //         + L_CREATED_AT  + " DATETIME  DEFAULT CURRENT_TIMESTAMP" + ")";

    public MyLocation(){
        
    }
    public MyLocation(Integer id, Double latitude, Double longitude, String updatetime) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.updatetime = updatetime;
    }

    public int getId() {   return id;    }
    public Double getLatitude() {  return latitude;    }
    public Double getLongitude() {  return longitude;    }
    public String getUpdatetime() {  return updatetime;    }

    public void setId(Integer id) {        this.id = id;  }
    public void setLatitude(Double latitude) {        this.latitude = latitude;  }
    public void setLongitude(Double longitude) {        this.longitude = longitude;  }
    public void setUpdatetime(String updatetime) {        this.updatetime = updatetime;  }

}
