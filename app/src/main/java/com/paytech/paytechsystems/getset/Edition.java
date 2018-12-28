package com.paytech.paytechsystems.getset;

public class Edition {
    private Integer id, status;
    private String course, edition, fees;
    public static final String TABLE_COURSE_EDITION = "course_edition";

    //Course Lesson table fields
    public static final String E_ID = "e_id";
    public static final String E_NAME = "e_name";
    public static final String E_FEES = "e_fees";
    public static final String E_COURSE = "e_course";
    public static final String E_STATUS = "e_status";
    public static final String COURSE_CREATED_AT = "e_status";

    public static final String CREATE_TABLE_EDITION = "CREATE TABLE "
            + TABLE_COURSE_EDITION + "("
            + E_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + E_NAME  + " TEXT,"
            + E_COURSE  + " TEXT,"
            + E_FEES + " TEXT,"
            + COURSE_CREATED_AT  + " DATETIME  DEFAULT CURRENT_TIMESTAMP" + ")";

    public Edition(){
        
    }
    public Edition(Integer id, String course, String edition, String fees) {
        this.fees = fees;
        this.id = id;
        this.edition = edition;
        this.course = course;
    }

    public int getId() {   return id;    }
    public String getCourse() {  return course;    }
    public String getFees() {  return fees;    }
    public String getEdition() {  return edition;    }


    public void setId(Integer bid) {        this.id = id;  }
    public void setFees(String fees) {        this.fees = fees;  }
    public void setEdition(String edition) {        this.edition = edition;  }
    public void setCourse(String course) {        this.course = course;  }


}
