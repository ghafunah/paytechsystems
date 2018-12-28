package com.paytech.paytechsystems.getset;

public class LessonDone {
    private Integer id;
    private String admno, student, lesson, course, teacher, vehicle, branch, date;
    
    public static final String TABLE_LESSON_DONE = "lesson_done";
    public static final String LD_ID = "id";
    public static final String LD_ADMNO = "admno";
    public static final String LD_STUDENT = "student";
    public static final String LD_LESSON = "lesson";
    public static final String LD_COURSE = "course";
    public static final String LD_TEACHER = "teacher";
    public static final String LD_VEHICLE = "vehicle";
    public static final String LD_BRANCH = "branch";
    public static final String LD_DATE = "date";

    public static final String CREATE_TABLE_LESSON_DONE = "CREATE TABLE "
            + TABLE_LESSON_DONE + "("
            + LD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + LD_ADMNO  + " TEXT,"
            + LD_STUDENT  + " TEXT,"
            + LD_LESSON  + " TEXT,"
            + LD_COURSE + " TEXT,"
            + LD_TEACHER + " TEXT,"
            + LD_VEHICLE + " TEXT,"
            + LD_BRANCH + " TEXT,"
            + LD_DATE  + " DATETIME  DEFAULT CURRENT_TIMESTAMP" + ")";

    public LessonDone(){
        
    }
    public LessonDone(Integer id, String admno, String student, String lesson, String course, String teacher, String vehicle,String branch, String date) {
        this.id = id;
        this.admno = admno;
        this.student = student;
        this.lesson = lesson;
        this.course = course;
        this.teacher = teacher;
        this.vehicle = vehicle;
        this.branch = branch;
        this.date = date;
    }

    public int getId() {   return id;    }
    public String getAdmno() {  return admno;    }
    public String getStudent() {  return student;    }
    public String getLesson() {  return lesson;    }
    public String getCourse() {  return course;    }
    public String getTeacher() {  return teacher;    }
    public String getVehicle() {  return vehicle;    }
    public String getBranch() {  return branch;    }
    public String getDate() {  return date;    }


    public void setId(Integer id) {        this.id = id;  }
    public void setAdmno(String admno) {        this.admno = admno;  }
    public void setStudent(String student) {        this.student = student;  }
    public void setLesson(String lesson) {        this.lesson = lesson;  }
    public void setCourse(String course) {        this.course = course;  }
    public void setTeacher(String teacher) {        this.teacher = teacher;  }
    public void setVehicle(String vehicle) {        this.vehicle = vehicle;  }
    public void setBranch(String branch) {        this.branch = branch;  }
    public void setDate(String date) {        this.date = date;  }

}
