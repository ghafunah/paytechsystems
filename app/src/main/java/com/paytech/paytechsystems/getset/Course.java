package com.paytech.paytechsystems.getset;

public class Course {
    private Integer id, status;
    private String cname, createdat;

    public static final String TABLE_COURSE = "course";

    //Course Table Columns names
    public static final String C_ID = "course_id";
    public static final String C_NAME = "course_name";
    public static final String C_STATUS = "course_status";
    public static final String C_CREATED_AT = "course_created_at";

    public static final String CREATE_TABLE_COURSE = "CREATE TABLE "
            + TABLE_COURSE + "("
            + C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + C_NAME  + " TEXT UNIQUE,"
            + C_STATUS + " TEXT,"
            + C_CREATED_AT  + " DATETIME  DEFAULT CURRENT_TIMESTAMP" + ")";

    public Course(){
        
    }
    public Course(Integer id, String cname, String createdat, Integer status) {
        this.cname = cname;
        this.id = id;
        this.createdat  = createdat;
        this.status  = status;
    }

    public int getId() {   return id;    }
    public String getCname() {  return cname;    }
    public Integer getStatus() {  return status;    }
    public String getCreatedat() {  return createdat;    }

    public void setCid(Integer bid) {        this.id = id;  }
    public void setCname(String cname) {        this.cname = cname;  }
    public void setStatus(Integer status) {        this.status = status;  }
    public void setCreatedat(Integer createdat) {        this.status = createdat;  }

}
