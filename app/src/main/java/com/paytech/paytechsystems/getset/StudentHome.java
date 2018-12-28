package com.paytech.paytechsystems.getset;

public class StudentHome {
    private Integer id;
    private String admno, student, phone, admdate;
    private String fees, charges, paid, balance;

    public static final String TABLE_STUDENT_HOME = "student_home";

    public static final String SH_ID = "id";
    public static final String SH_ADMNO = "admno";
    public static final String SH_STUDENT = "student";
    public static final String SH_PHONE = "phone";
    public static final String SH_FEES = "fees";
    public static final String SH_CHARGES = "charges";
    public static final String SH_PAID = "paid";
    public static final String SH_BALANCE = "balance";
    public static final String SH_ADMDATE = "admdate";

    public static final String CREATE_TABLE_STUDENT_HOME = "CREATE TABLE "
            + TABLE_STUDENT_HOME + "("
            + SH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + SH_STUDENT  + " TEXT,"
            + SH_ADMNO + " TEXT UNIQUE,"
            + SH_PHONE + " TEXT,"
            + SH_FEES + " DOUBLE,"
            + SH_CHARGES + " DOUBLE,"
            + SH_PAID  + " DOUBLE,"
            + SH_BALANCE + " DOUBLE,"
            + SH_ADMDATE  + " DATETIME  DEFAULT CURRENT_TIMESTAMP" + ")";
    public StudentHome(){
        
    }
    public StudentHome(Integer id, String student, String admno, String phone, String fees, String charges,  String paid,  String balance, String admdate) {
        this.id = id;
        this.student = student;
        this.admno = admno;
        this.phone = phone;
        this.fees = fees;
        this.charges = charges;
        this.paid = paid;
        this.balance  = balance;
        this.admdate = admdate;
    }

    public int getId() {   return id;    }
    public String getStudent() {  return student;    }
    public String getAdmno() {  return admno;    }
    public String getPhone() {  return phone;    }
    public String getFees() { return fees; }
    public String getCharges() {  return charges;    }
    public String getPaid() {  return paid;    }
    public String getBalance() {  return balance;    }
    public String getAdmdate(){ return admdate; }

    public void setId(Integer id) {        this.id = id;  }
    public void setStudent(String student) {        this.student = student;  }
    public void setAdmno(String admno) {        this.admno = admno;  }
    public void setPhone(String phone) {        this.phone = phone;  }
    public void setFees(String fees) {        this.fees = fees;  }
    public void setCharges(String charges) {        this.charges = charges;  }
    public void setPaid(String paid) {        this.paid = paid;  }
    public void setBalance(String balance) {        this.balance  = balance;  }
    public void setAdmdate(String admdate) {        this.admdate  = admdate;  }

}
