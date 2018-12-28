package com.paytech.paytechsystems.getset;

public class Lesson {
    private Integer id, status;
    private String course, lesson, created_at;
    
    public static final String TABLE_LESSON = "lesson";
    public static final String L_ID = "id";
    public static final String L_COURSE = "course";
    public static final String L_DESC = "lesson";
    public static final String L_STATUS = "status";
    public static final String L_CREATED_AT = "created_at";

    public static final String CREATE_TABLE_LESSON = "CREATE TABLE "
            + TABLE_LESSON + "("
            + L_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + L_COURSE  + " TEXT,"
            + L_DESC + " TEXT,"
            + L_STATUS + " INTEGER,"
            + L_CREATED_AT  + " DATETIME  DEFAULT CURRENT_TIMESTAMP" + ")";

    public Lesson(){
        
    }
    public Lesson(Integer id, String course, String lesson, String created_at, Integer status) {
        this.id = id;
        this.course = course;
        this.lesson = lesson;
        this.created_at = created_at;
        this.status = status;
    }

    public int getId() {   return id;    }
    public String getCourse() {  return course;    }
    public String getLesson() {  return lesson;    }
    public String getCreated_at() {  return created_at;    }
    public Integer getStatus() {  return status;    }

    public void setId(Integer id) {        this.id = id;  }
    public void setCourse(String course) {        this.course = course;  }
    public void setLesson(String lesson) {        this.lesson = lesson;  }
    public void setCreated_at(String created_at) {        this.created_at = created_at;  }
    public void setStatus(Integer status) {        this.status = status;  }

}
