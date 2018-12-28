package com.paytech.paytechsystems.getset;

public class Payment {
    private Integer pid, status;
    private String pidss, courseid, course, amount, admno, pmode, pfor, refno, pdate, student;
    public static final String TABLE_PAYMENT = "payment";
    public static final String P_ID = "id";
    public static final String P_ADMNO = "admno";
    public static final String P_STUDENT = "student";
    public static final String P_COURSEID = "courseid";
    public static final String P_AMOUNT = "amount";
    public static final String P_COURSE = "course";
    public static final String P_MODE = "mode";
    public static final String P_FOR = "pfor";
    public static final String P_REF = "reference";
    public static final String P_STATUS = "status";
    public static final String P_CREATED_AT = "created_at";

    public static final String CREATE_TABLE_PAYMENT = "CREATE TABLE "
            + TABLE_PAYMENT + "("
            + P_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + P_ADMNO  + " TEXT,"
            + P_STUDENT  + " TEXT,"
            + P_COURSEID + " TEXT,"
            + P_AMOUNT + " TEXT,"
            + P_MODE + " TEXT,"
            + P_COURSE + " TEXT,"
            + P_FOR + " TEXT,"
            + P_REF + " TEXT UNIQUE,"
            + P_STATUS + " INTEGER,"
            + P_CREATED_AT  + " DATETIME  DEFAULT CURRENT_TIMESTAMP" + ")";

    public Payment(){
        
    }

    public Payment(Integer pid, String admno, String student, String courseid, String amount, String mode, String pfor,  String reference, Integer status, String pdate) {
        this.pid = pid;
        this.admno = admno;
        this.courseid = courseid;
        this.course = course;
        this.amount = amount;
        this.pmode = mode;
        this.pfor = pfor;
        this.refno  = reference;
        this.pdate  = pdate;
        this.status  = status;
        this.student = student;
    }

    public int getId() {   return pid;    }
    public String getAdmno() {  return admno;    }
    public String getCourseid() {  return courseid;    }
    public String getCourse() {  return course;    }
    public String getAmount() {  return amount;    }
    public String getStudent() {  return student;    }
    public String getMode() {  return pmode;    }
    public String getPfor() {  return pfor;    }
    public String getReference() {  return refno;    }
    public String getCreated_at() {  return pdate;    }
    public Integer getStatus() {  return status;    }

    public void setId(Integer id) {        this.pid = id;  }
    public void setAdmno(String admno) {        this.admno = admno;  }
    public void setCourseid(String courseid) {        this.courseid = courseid;  }
    public void setCourse(String course) {        this.course = course;  }
    public void setAmount(String amount) {        this.amount = amount;  }
    public void setMode(String mode) {        this.pmode = mode;  }
    public void setPfor(String pfor) {        this.pfor = pfor;  }
    public void setStudent(String student) {        this.student = student;  }
    public void setReference(String reference) {        this.refno = reference;  }
    public void setCreated_at(String pdate) {        this.pdate = pdate;  }
    public void setStatus(Integer status) {        this.status = status;  }

}
