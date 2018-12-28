package com.paytech.paytechsystems.getset;

public class Student {
    private Integer id, status;
    private String firstname, surname, idno, telephone, email, admno, admdate, yob, gender, photo;
    public static final String TABLE_STUDENT = "student";
    //student table fields
    public static final String STD_ID = "std_id";
    public static final String STD_ADMNO = "std_admno";
    public static final String STD_DOB = "std_dob";
    public static final String STD_FNAME = "std_fname";
    public static final String STD_SNAME = "std_sname";
    public static final String STD_GENDER = "std_gender";
    public static final String STD_EMAIL = "std_email";
    public static final String STD_IDNUMBER = "std_idnumber";
    public static final String STD_PHONE = "std_phone";
    public static final String STD_STATUS = "std_status";
    public static final String STD_CREATED_AT = "std_created_at";
    public static final String STD_BRANCH = "std_branch";

    public static final String CREATE_TABLE_STUDENT = "CREATE TABLE "
            + TABLE_STUDENT + "("
            + STD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + STD_ADMNO  + " TEXT UNIQUE,"
            + STD_IDNUMBER + " TEXT UNIQUE,"
            + STD_FNAME + " TEXT,"
            + STD_SNAME + " TEXT,"
            + STD_DOB + " TEXT,"
            + STD_EMAIL +" TEXT,"
            + STD_PHONE + " TEXT,"
            + STD_GENDER + " TEXT,"
            + STD_BRANCH + " TEXT,"
            + STD_STATUS + " TEXT,"
            + STD_CREATED_AT  + " DATETIME  DEFAULT CURRENT_TIMESTAMP" + ")";

    public Student(){
        
    }
 
    public Student(Integer id, String surname,String yob, String gender, String firstname, String email, Integer status, String admno, String telephone, String idno, String admdate, String photo) {
        this.id = id;
        this.firstname = firstname;
        this.surname = surname;
        this.idno = idno;
        this.admdate = admdate;
        this.telephone = telephone;
        this.email = email;
        this.admno  = admno;
        this.yob  = yob;
        this.gender  = gender;
        this.photo  = photo;
        this.status  = status;
    }

    public int getId() {   return id;    }
    public String getFirstname() {  return firstname;    }
    public String getSurname() {  return surname;    }
    public String getIdno() {  return idno;    }
    public String getTelephone() {  return telephone;    }
    public String getAdmdate() {  return admdate;    }
    public String getAdmno() {  return admno;    }
    public String getYob() {  return yob;    }
    public String getGender() {  return gender;    }
    public String getEmail() {  return email;    }
    public String getPhoto() {  return photo;    }
    public Integer getStatus() {  return status;    }

    public void setId(Integer id) {        this.id = id;  }
    public void setFirstname(String firstname) {        this.firstname = firstname;  }
    public void setSurname(String surname) {        this.surname = surname;  }
    public void setAdmdate(String admdate) {        this.admdate = admdate;  }
    public void setTelephone(String telephone) {        this.telephone = telephone;  }
    public void setAdmno(String admno) {        this.admno = admno;  }
    public void setIdno(String idno) {        this.idno = idno;  }
    public void setYob(String yob) {        this.yob = yob;  }
    public void setGender(String gender) {        this.gender = gender;  }
    public void setEmail(String email) {        this.email = email;  }
    public void setPhoto(String photo) {        this.photo = photo;  }
    public void setStatus(Integer status) {        this.status = status;  }

}
