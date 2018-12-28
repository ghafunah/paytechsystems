package com.paytech.paytechsystems.helper;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.style.TtsSpan;
import android.util.Log;

import com.paytech.paytechsystems.getset.Branch;
import com.paytech.paytechsystems.getset.BranchCollection;
import com.paytech.paytechsystems.getset.Course;
import com.paytech.paytechsystems.getset.Edition;
import com.paytech.paytechsystems.getset.Lesson;
import com.paytech.paytechsystems.getset.LessonDone;
import com.paytech.paytechsystems.getset.Mpesa;
import com.paytech.paytechsystems.getset.MyLocation;
import com.paytech.paytechsystems.getset.Note;
import com.paytech.paytechsystems.getset.Payment;
import com.paytech.paytechsystems.getset.Permission;
import com.paytech.paytechsystems.getset.Student;
import com.paytech.paytechsystems.getset.StudentHome;
import com.paytech.paytechsystems.getset.Teacher;
import com.paytech.paytechsystems.getset.User;
import com.paytech.paytechsystems.getset.Vehicle;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "seniors_db";

    public static final String TABLE_LOCATION = "location";
    // Login Table Columns names
    public static final String LOC_ID= "loc_id";
    public static final String LOC_LAT = "loc_lat";
    public static final String LOC_LONG = "loc_long";
    public static final String LOC_CREATED_AT = "loc_created_at";



    public static final String CREATE_TABLE_LOCATION = "CREATE TABLE " + TABLE_LOCATION + "("
            + LOC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + LOC_LAT + " TEXT,"
            + LOC_LONG + " TEXT,"
            + LOC_CREATED_AT  + " DATETIME  DEFAULT CURRENT_TIMESTAMP" + ")";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(User.CREATE_TABLE_LOGIN);
        db.execSQL(Branch.CREATE_TABLE_BRANCH);
        db.execSQL(Student.CREATE_TABLE_STUDENT);
        db.execSQL(Course.CREATE_TABLE_COURSE);
        db.execSQL(Vehicle.CREATE_TABLE_VEHICLE);
        db.execSQL(Mpesa.CREATE_TABLE_MPESA);
        db.execSQL(Edition.CREATE_TABLE_EDITION);
        db.execSQL(Teacher.CREATE_TABLE_TEACHER);
        db.execSQL(Note.CREATE_TABLE_NOTE);
        db.execSQL(Lesson.CREATE_TABLE_LESSON);
        db.execSQL(Payment.CREATE_TABLE_PAYMENT);
        db.execSQL(StudentHome.CREATE_TABLE_STUDENT_HOME);
        db.execSQL(Permission.CREATE_TABLE_PERMISSION);
        db.execSQL(LessonDone.CREATE_TABLE_LESSON_DONE);
        db.execSQL(BranchCollection.CREATE_TABLE_BRANCH_COLLECTION);
        db.execSQL(CREATE_TABLE_LOCATION);
        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + User.TABLE_LOGIN);
        db.execSQL("DROP TABLE IF EXISTS " + Branch.TABLE_BRANCH);
        db.execSQL("DROP TABLE IF EXISTS " + Student.TABLE_STUDENT);
        db.execSQL("DROP TABLE IF EXISTS " + Course.TABLE_COURSE);
        db.execSQL("DROP TABLE IF EXISTS " + Vehicle.TABLE_VEHICLE);
        db.execSQL("DROP TABLE IF EXISTS " + Note.TABLE_NOTE);
        db.execSQL("DROP TABLE IF EXISTS " + Mpesa.TABLE_MPESA);
        db.execSQL("DROP TABLE IF EXISTS " + Edition.TABLE_COURSE_EDITION);
        db.execSQL("DROP TABLE IF EXISTS " + Teacher.TABLE_TEACHER);
        db.execSQL("DROP TABLE IF EXISTS " + Lesson.TABLE_LESSON);
        db.execSQL("DROP TABLE IF EXISTS " + Payment.TABLE_PAYMENT);
        db.execSQL("DROP TABLE IF EXISTS " + LessonDone.TABLE_LESSON_DONE);
        db.execSQL("DROP TABLE IF EXISTS " + StudentHome.TABLE_STUDENT_HOME);
        db.execSQL("DROP TABLE IF EXISTS " + Permission.TABLE_PERMISSION);
        db.execSQL("DROP TABLE IF EXISTS " + BranchCollection.TABLE_BRANCH_COLLECTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION);
        // Create tables again
        Log.d(TAG, "Database tables destroyed!");
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public long saveLocation(Double lat, Double longt, String date) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LOC_LAT, lat); //
        values.put(LOC_LONG, longt); //
        values.put(LOC_CREATED_AT, date); //

        // Inserting Row
        int count = getLocationCount();
        long lid = db.insert(TABLE_LOCATION, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New mylocation inserted into sqlite: " + count + " " +longt);
        return  lid;
    }

    public int getLocationCount() {
        String countQuery = "SELECT  * FROM " + TABLE_LOCATION;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
 
        int count = cursor.getCount();
        cursor.close();
 
 
        // return count
        return count;
    }

    public int getStudentCount() {
        String countQuery = "SELECT  * FROM " + Student.TABLE_STUDENT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public void saveLessondone(Integer id, String admno, String student, String lesson, String course, String teacher, String vehicle,String branch, String date) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        if (id != 0) { values.put(LessonDone.LD_ID, id); }//
        values.put(LessonDone.LD_ADMNO, admno); //
        values.put(LessonDone.LD_STUDENT, student); //
        values.put(LessonDone.LD_LESSON, lesson); //
        values.put(LessonDone.LD_COURSE, course); //
        values.put(LessonDone.LD_TEACHER, teacher); //
        values.put(LessonDone.LD_VEHICLE, vehicle); //
        values.put(LessonDone.LD_BRANCH, branch); //
        values.put(LessonDone.LD_DATE, date); //

        // Inserting Row
        long lid = db.insert(LessonDone.TABLE_LESSON_DONE, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + lid);
    }


    public void saveBranchCollection(Integer id, String branch, String fees, String date) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(BranchCollection.BC_ID, id); //
        values.put(BranchCollection.BC_BRANCH, branch); //
        values.put(BranchCollection.BC_FEES, fees); //
        //values.put(BranchCollection.BC_DATE, date); //

        // Inserting Row
        long lid = db.insert(BranchCollection.TABLE_BRANCH_COLLECTION, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + lid);
    }
    public void saveUser(String idno, String fname, String sname, String uname, String email, String branch, String password, String phone, String gender, String role, int status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(User.USER_IDNO, idno); // idno
        values.put(User.USER_FNAME, fname); // fname
        values.put(User.USER_SNAME, sname); // sname
        values.put(User.USER_UNAME, uname); // uname
        values.put(User.USER_EMAIL, email); // email
        values.put(User.USER_BRANCH, branch); // branch
        values.put(User.USER_PASSWORD, password); // password
        values.put(User.USER_PHONE, phone); // phone
        values.put(User.USER_GENDER, gender); // phone
        values.put(User.USER_ROLE, role); // phone
        values.put(User.USER_STATUS, status); // status
        // Inserting Row
        long id = db.insert(User.TABLE_LOGIN, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }
    public void saveStudentHome(String admno, String student, String phone, Double fees, Double charges,  Double paid,  Double balance, String admdate) {
        SQLiteDatabase db = this.getWritableDatabase();
        StudentHome p = new StudentHome();
        ContentValues values = new ContentValues();
        values.put(p.SH_ADMNO, admno);
        values.put(p.SH_STUDENT, student);
        values.put(p.SH_PHONE, phone);
        values.put(p.SH_FEES, fees);
        values.put(p.SH_CHARGES, charges);
        values.put(p.SH_PAID, paid);
        values.put(p.SH_BALANCE, balance);
        values.put(p.SH_ADMDATE, admdate);

        // Inserting Row
        long id = db.insert(p.TABLE_STUDENT_HOME, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "+++++++++++++++++++++++++++++++++++++++++++++++New Student Home inserted into sqlite: " + id);
    }

    public void savePermission(String page, String users, Integer status) {
        SQLiteDatabase db = this.getWritableDatabase();
        Permission p = new Permission();
        ContentValues values = new ContentValues();
        values.put(p.P_PAGE, page);
        values.put(p.P_USERS, users);
         values.put(p.P_STATUS, status);

        // Inserting Row
        long id = db.insert(p.TABLE_PERMISSION, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "+++++++++++++++++++++++++++++++++++++++++++++++New permission inserted into sqlite: " + id);
    }

    public void savePayment(Integer id, String admno, String student, String courseid, String course, String amount, String mode, String pfor,  String reference, String date, Integer status) {
        SQLiteDatabase db = this.getWritableDatabase();
        Payment p = new Payment();
        ContentValues values = new ContentValues();
        if (id != 0 ) {  values.put(p.P_ID, id); }
        values.put(p.P_ADMNO, admno);
        values.put(p.P_COURSEID, courseid);
        values.put(p.P_AMOUNT, amount);
        values.put(p.P_COURSE, course);
        values.put(p.P_MODE, mode);
        values.put(p.P_STUDENT, student);
        values.put(p.P_FOR, pfor);
        values.put(p.P_REF, reference);
        values.put(p.P_STATUS, status);
        values.put(p.P_CREATED_AT, date);

        // Inserting Row
        long newid = db.insert(p.TABLE_PAYMENT, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "+++++++++++++++++++++++++++++++++++++++++++++++New payment inserted into sqlite: " + newid);
    }

    public void saveBranch(String code, String name, String location, String till, String email, Integer status) {
        SQLiteDatabase db = this.getWritableDatabase();
        Branch branch = new Branch();
        ContentValues values = new ContentValues();
        values.put(branch.BRANCH_CODE, code);
        values.put(branch.BRANCH_NAME, name);
        values.put(branch.BRANCH_DESC, location);
        values.put(branch.BRANCH_TILL, till);
        values.put(branch.BRANCH_EMAIL, email);
        values.put(branch.BRANCH_STATUS, status);

        // Inserting Row
        long id = db.insert(branch.TABLE_BRANCH, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "+++++++++++++++++++++++++++++++++++++++++++++++New branch inserted into sqlite: " + id);
    }

    public void saveStudent(String idno, String fname, String sname, String yob, String email, String branch, String gender, String phone, String admno,String admdate, Integer status) {
        SQLiteDatabase db = this.getWritableDatabase();
        Student s = new Student();
        ContentValues values = new ContentValues();
        values.put(s.STD_IDNUMBER, idno);
        values.put(s.STD_FNAME, fname);
        values.put(s.STD_SNAME, sname);
        values.put(s.STD_PHONE, phone);
        values.put(s.STD_EMAIL, email);
        values.put(s.STD_BRANCH, 8);
        values.put(s.STD_GENDER, gender);
        values.put(s.STD_DOB, yob);
        values.put(s.STD_ADMNO, idno);
        //values.put(s.STD_CREATED_AT, admdate);
        values.put(s.STD_STATUS, status);

        // Inserting Row
        long id = db.insert(s.TABLE_STUDENT, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "+++++++++++++++++++++++++++++++++++++++++++++++New student inserted into sqlite: " + id);
    }

    public void savePayment(String admno, String amount, String mode, Integer status) {
        SQLiteDatabase db = this.getWritableDatabase();
        Payment s = new Payment();
        ContentValues values = new ContentValues();
        values.put(s.P_ADMNO, admno);
        values.put(s.P_MODE, mode);
        values.put(s.P_AMOUNT, amount);
        values.put(s.P_STATUS, status);

        // Inserting Row
        long id = db.insert(s.TABLE_PAYMENT, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "+++++++++++++++++++++++++++++++++++++++++++++++New payment inserted into sqlite: " + id);
    }

    public void saveMpesa( String service,String fname, String lname, String ttime, String phone, String tref, String amount, Integer status) {
        SQLiteDatabase db = this.getWritableDatabase();
        Mpesa m = new Mpesa();
        ContentValues values = new ContentValues();
        values.put(m.M_FNAME, fname);
        values.put(m.M_SERVICE, service);
        values.put(m.M_LMANE, lname);
        values.put(m.M_TTIME, ttime);
        values.put(m.M_PHONE, phone);
        values.put(m.M_TREF, tref);
        values.put(m.M_AMOUNT, amount);
        values.put(m.M_STATUS, status);

        // Inserting Row
        long id = db.insert(Mpesa.TABLE_MPESA, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "+++++++++++++++++++++++++++++++++++++++++++++++New mpesa inserted into sqlite: " + id);
    }

    public void saveCourse(Integer cid, String course, Integer status) {
        SQLiteDatabase db = this.getWritableDatabase();
        Course m = new Course();
        ContentValues values = new ContentValues();
        if(cid == 0){ values.put(m.C_ID, cid); }
        values.put(m.C_NAME, course);
        values.put(m.C_STATUS, status);


        // Inserting Row
        long id = db.insert(Course.TABLE_COURSE, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "+++++++++++++++++++++++++++++++++++++++++++++++New Course inserted into sqlite: " + id);
    }


    public void saveLesson(Integer lid, String course, String lesson, Integer status) {
        SQLiteDatabase db = this.getWritableDatabase();
        Lesson m = new Lesson();
        ContentValues values = new ContentValues();
        if(lid == 0){ values.put(m.L_ID, lid); }
        values.put(m.L_COURSE, course);
        values.put(m.L_DESC, lesson);
        values.put(m.L_STATUS, status);

        // Inserting Row
        long id = db.insert(Lesson.TABLE_LESSON, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "+++++++++++++++++++++++++++++++++++++++++++++++New Course inserted into sqlite: " + id);
    }
    public void saveEdition(Integer eid, String course, String edition, String fees, Integer status) {
        SQLiteDatabase db = this.getWritableDatabase();
        Edition m = new Edition();
        ContentValues values = new ContentValues();
        values.put(m.E_NAME, edition);
        values.put(m.E_FEES, fees);
        values.put(m.E_COURSE, course);
        values.put(m.E_STATUS, status);


        // Inserting Row
        long id = db.insert(Edition.TABLE_COURSE_EDITION, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "+++++++++++++++++++++++++++++++++++++++++++++++New Edition inserted into sqlite: " + id);
    }


    public void saveTeacher(String idno, String fname, String sname, String mname, String phone, String email, String terms, String special, Integer status) {
        SQLiteDatabase db = this.getWritableDatabase();
        Teacher m = new Teacher();
        ContentValues values = new ContentValues();
        values.put(m.T_IDNO, idno);
        values.put(m.T_FNAME, fname);
        values.put(m.T_SNAME, sname);
        values.put(m.T_MNAME, mname);
        values.put(m.T_PHONE, phone);
        values.put(m.T_EMAIL, email);
        values.put(m.T_TERMS, terms);
        values.put(m.T_SPECIAL, special);
        values.put(m.T_STATUS, status);


        // Inserting Row
        long id = db.insert(Teacher.TABLE_TEACHER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "+++++++++++++++++++++++++++++++++++++++++++++++New Teacher inserted into sqlite: " + id);
    }

    public void saveVehicle(String regno, String make, String type, String dateadd, Integer status) {
        SQLiteDatabase db = this.getWritableDatabase();
        Vehicle m = new Vehicle();
        ContentValues values = new ContentValues();
        values.put(m.V_REGNO, regno);
        values.put(m.V_MAKE, make);
        values.put(m.V_TYPE, type);
        values.put(m.V_CREATED_AT, dateadd);
        values.put(m.V_STATUS, status);

        //db.delete(Vehicle.TABLE_VEHICLE, null, null);
        //Log.d(TAG, "=====================================================================================Table emptied ");
        // Inserting Row
        long id = db.insert(Vehicle.TABLE_VEHICLE, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "+++++++++++++++++++++++++++++++++++++++++++++++New Vehicle inserted into sqlite: " + id);
    }

    public void addUsertosql(String idno, String fname, String sname, String uname) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(USER_ID, idno); // idno
        values.put(User.USER_FNAME, fname); // fname
        values.put(User.USER_SNAME, sname); // sname
        values.put(User.USER_UNAME, uname); // uname
        // Inserting Row
        long id = db.insert(User.TABLE_LOGIN, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "+++++++++++++++++++++++++++++++++++++++++++++++New user inserted into sqlite: " + id);
    }
    //Update status

        public boolean updateNameStatus(String uname, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(User.USER_STATUS, status);
        db.update(User.TABLE_LOGIN, contentValues, User.USER_UNAME + "=" + uname, null);
        db.close();
        return true;
    }


    /**
     * Getting user data from database
     * */

    public Cursor getUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + User.TABLE_LOGIN + " ORDER BY " + User.USER_ID + " ASC;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }


    public List<MyLocation> getAllLocations() {
        List<MyLocation> sh = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_LOCATION + " ORDER BY " + LOC_ID + " DESC;";;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MyLocation s = new MyLocation();
                s.setId(cursor.getInt(cursor.getColumnIndex(LOC_ID)));
                s.setLatitude(cursor.getDouble(cursor.getColumnIndex(LOC_LAT)));
                s.setLongitude(cursor.getDouble(cursor.getColumnIndex(LOC_LONG)));
                s.setUpdatetime(cursor.getString(cursor.getColumnIndex(LOC_CREATED_AT)));

                sh.add(s);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return sh;
    }

    public List<StudentHome> getAllStudentHome(String per_page) {
        List<StudentHome> sh = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + StudentHome.TABLE_STUDENT_HOME + " ORDER BY " + StudentHome.SH_ADMNO + " DESC LIMIT " +per_page +";";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                StudentHome s = new StudentHome();
                s.setAdmno(cursor.getString(cursor.getColumnIndex(StudentHome.SH_ADMNO)));
                s.setStudent(cursor.getString(cursor.getColumnIndex(StudentHome.SH_STUDENT)));
                s.setPhone(cursor.getString(cursor.getColumnIndex(StudentHome.SH_PHONE)));
                s.setBalance(cursor.getString(cursor.getColumnIndex(StudentHome.SH_BALANCE)));
                s.setPaid(cursor.getString(cursor.getColumnIndex(StudentHome.SH_PAID)));
                s.setCharges(cursor.getString(cursor.getColumnIndex(StudentHome.SH_CHARGES)));
                s.setFees(cursor.getString(cursor.getColumnIndex(StudentHome.SH_FEES)));
                s.setAdmdate(cursor.getString(cursor.getColumnIndex(StudentHome.SH_ADMDATE)));

                sh.add(s);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return sh;
    }

    public List<User> getAllUsers(String per_page) {
        List<User> users = new ArrayList<>();
 
        // Select All Query
        String selectQuery = "SELECT * FROM " + User.TABLE_LOGIN + " ORDER BY " + User.USER_ID + " DESC LIMIT "+ per_page+";";
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setFname(cursor.getString(cursor.getColumnIndex(User.USER_FNAME)));
                user.setSname(cursor.getString(cursor.getColumnIndex(User.USER_SNAME)));
                user.setUname(cursor.getString(cursor.getColumnIndex(User.USER_UNAME)));
                user.setIdno(cursor.getString(cursor.getColumnIndex(User.USER_IDNO)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(User.USER_EMAIL)));
                user.setRole(cursor.getString(cursor.getColumnIndex(User.USER_ROLE)));
                users.add(user);
            } while (cursor.moveToNext());
        }
 
        // close db connection
        db.close();
 
        // return notes list
        return users;
    }


    public List<Branch> getAllBranches() {
        List<Branch> branches = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + Branch.TABLE_BRANCH + " ORDER BY " + Branch.BRANCH_ID + " DESC;";;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Branch b = new Branch();
                b.setCode(cursor.getString(cursor.getColumnIndex(Branch.BRANCH_CODE)));
                b.setName(cursor.getString(cursor.getColumnIndex(Branch.BRANCH_NAME)));
                b.setPhone(cursor.getString(cursor.getColumnIndex(Branch.BRANCH_PHONE)));
                b.setTill(cursor.getString(cursor.getColumnIndex(Branch.BRANCH_TILL)));
                b.setEmail(cursor.getString(cursor.getColumnIndex(Branch.BRANCH_EMAIL)));
                b.setDesc(cursor.getString(cursor.getColumnIndex(Branch.BRANCH_DESC)));
                b.setId(cursor.getInt(cursor.getColumnIndex(Branch.BRANCH_ID)));
                b.setLocation(cursor.getString(cursor.getColumnIndex(Branch.BRANCH_LOCATION)));
               // b.setCreatedat(cursor.getString(cursor.getColumnIndex(Branch.BRANCH_CREATED_AT)));

                branches.add(b);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return branches;
    }
    
    public List<Course> getAllCourses() {
        List<Course> i = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + Course.TABLE_COURSE + " ORDER BY " + Course.C_ID + " DESC;";;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Course b = new Course();
                b.setCid(cursor.getInt(cursor.getColumnIndex(Course.C_ID)));
                b.setCname(cursor.getString(cursor.getColumnIndex(Course.C_NAME)));
                b.setStatus(cursor.getInt(cursor.getColumnIndex(Course.C_STATUS)));

                i.add(b);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return i;
    }


    public List<Lesson> getAllLessons() {
        List<Lesson> i = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + Lesson.TABLE_LESSON + " ORDER BY " + Lesson.L_ID + " DESC;";;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Lesson b = new Lesson();
                b.setId(cursor.getInt(cursor.getColumnIndex(Lesson.L_ID)));
                b.setCourse(cursor.getString(cursor.getColumnIndex(Lesson.L_COURSE)));
                b.setLesson(cursor.getString(cursor.getColumnIndex(Lesson.L_DESC)));
                b.setCreated_at(cursor.getString(cursor.getColumnIndex(Lesson.L_CREATED_AT)));
                b.setStatus(cursor.getInt(cursor.getColumnIndex(Lesson.L_STATUS)));

                i.add(b);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return i;
    }


    public List<Vehicle> getAllVehicles() {
        List<Vehicle> i = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + Vehicle.TABLE_VEHICLE + " ORDER BY " + Vehicle.V_ID + " DESC;";;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Vehicle b = new Vehicle();
                b.setId(cursor.getInt(cursor.getColumnIndex(Vehicle.V_ID)));
                b.setRegno(cursor.getString(cursor.getColumnIndex(Vehicle.V_REGNO)));
                b.setMake(cursor.getString(cursor.getColumnIndex(Vehicle.V_MAKE)));
                b.setType(cursor.getString(cursor.getColumnIndex(Vehicle.V_TYPE)));
                b.setStatus(cursor.getInt(cursor.getColumnIndex(Vehicle.V_STATUS)));

                i.add(b);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return i;
    }

    public Vehicle getVehicle(String regno) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Vehicle.TABLE_VEHICLE,
                new String[]{Vehicle.V_ID, Vehicle.V_REGNO, Vehicle.V_MAKE, Vehicle.V_TYPE, Vehicle.V_CREATED_AT, Vehicle.V_STATUS},
                Vehicle.V_REGNO + "=?",
                new String[]{String.valueOf(regno)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        Vehicle v = new Vehicle(
                cursor.getInt(cursor.getColumnIndex(Vehicle.V_ID)),
                cursor.getString(cursor.getColumnIndex(Vehicle.V_REGNO)),
                cursor.getString(cursor.getColumnIndex(Vehicle.V_MAKE)),
                cursor.getString(cursor.getColumnIndex(Vehicle.V_TYPE)),
                cursor.getString(cursor.getColumnIndex(Vehicle.V_CREATED_AT)),
                cursor.getInt(cursor.getColumnIndex(Vehicle.V_STATUS)));

        // close the db connection
        cursor.close();

        return v;
    }

    public Student getStudent(String admno) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();
// Student.STD_ID, Student.STD_ADMNO ,Student.STD_IDNUMBER , Student.STD_FNAME , Student.STD_SNAME , STD_DOB ,STD_EMAIL ,Student.STD_PHONE , Student.STD_GENDER ,Student.STD_BRANCH , Student.STD_STATUS ,Student.STD_CREATED_AT  
                
                Cursor cursor = db.query(Student.TABLE_STUDENT,
                new String[]{Student.STD_ID, Student.STD_ADMNO ,Student.STD_IDNUMBER , Student.STD_FNAME , Student.STD_SNAME , Student.STD_DOB ,Student.STD_EMAIL ,Student.STD_PHONE , Student.STD_GENDER ,Student.STD_BRANCH , Student.STD_STATUS ,Student.STD_CREATED_AT },
                Student.STD_ADMNO + "=?",
                new String[]{String.valueOf(admno)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        Student v = new Student(
                cursor.getInt(cursor.getColumnIndex(Student.STD_ID)),
                cursor.getString(cursor.getColumnIndex(Student.STD_SNAME)),
                cursor.getString(cursor.getColumnIndex(Student.STD_DOB)),
                cursor.getString(cursor.getColumnIndex(Student.STD_GENDER)),
                cursor.getString(cursor.getColumnIndex(Student.STD_FNAME)),
                cursor.getString(cursor.getColumnIndex(Student.STD_EMAIL)),
                cursor.getInt(cursor.getColumnIndex(Student.STD_STATUS)),
                cursor.getString(cursor.getColumnIndex(Student.STD_ADMNO)),
                cursor.getString(cursor.getColumnIndex(Student.STD_PHONE)),
                cursor.getString(cursor.getColumnIndex(Student.STD_IDNUMBER)),
                cursor.getString(cursor.getColumnIndex(Student.STD_CREATED_AT)),
                cursor.getString(cursor.getColumnIndex(Student.STD_PHONE)));

        // close the db connection
        cursor.close();

        return v;
    }

    public Course getCourse(String course) {
        // get readable database as we are not inserting anything

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Course.TABLE_COURSE,
                new String[]{Course.C_ID, Course.C_NAME, Course.C_CREATED_AT, Course.C_STATUS},
                Course.C_NAME + "=?",
                new String[]{String.valueOf(course)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        Course v = new Course(
                cursor.getInt(cursor.getColumnIndex(Course.C_ID)),
                cursor.getString(cursor.getColumnIndex(Course.C_NAME)),
                cursor.getString(cursor.getColumnIndex(Course.C_CREATED_AT)),
                cursor.getInt(cursor.getColumnIndex(Course.C_STATUS)));

        // close the db connection
        cursor.close();

        return v;
    }




    public List<Payment> getAllPayments() {
        List<Payment> i = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + Payment.TABLE_PAYMENT + " ORDER BY " + Payment.P_ID + " DESC;";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Payment b = new Payment();
                b.setId(cursor.getInt(cursor.getColumnIndex(Payment.P_ID)));
                b.setCourseid(cursor.getString(cursor.getColumnIndex(Payment.P_COURSEID)));
                b.setAdmno(cursor.getString(cursor.getColumnIndex(Payment.P_ADMNO)));
                b.setStudent(cursor.getString(cursor.getColumnIndex(Payment.P_STUDENT)));
                b.setAmount(cursor.getString(cursor.getColumnIndex(Payment.P_AMOUNT)));
                b.setPfor(cursor.getString(cursor.getColumnIndex(Payment.P_FOR)));
                b.setMode(cursor.getString(cursor.getColumnIndex(Payment.P_MODE)));
                b.setCreated_at(cursor.getString(cursor.getColumnIndex(Payment.P_CREATED_AT)));
                b.setReference(cursor.getString(cursor.getColumnIndex(Payment.P_REF)));

                i.add(b);
            } while (cursor.moveToNext());
        }
        cursor.close();
        // close db connection
        db.close();

        // return notes list
        return i;
    }




    public List<Edition> getAllEditions() {
        List<Edition> i = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + Edition.TABLE_COURSE_EDITION + " ORDER BY " + Edition.E_ID + " DESC;";;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Edition b = new Edition();
                b.setCourse(cursor.getString(cursor.getColumnIndex(Edition.E_COURSE)));
                b.setEdition(cursor.getString(cursor.getColumnIndex(Edition.E_NAME)));
                b.setFees(cursor.getString(cursor.getColumnIndex(Edition.E_FEES)));

                i.add(b);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return i;
    }


    public List<Permission> getAllPermissions() {
        List<Permission> p = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + Permission.TABLE_PERMISSION + " ORDER BY " + Permission.P_ID + " DESC;";;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Permission i = new Permission();
                i.setId(cursor.getInt(cursor.getColumnIndex(Permission.P_ID)));
                i.setPage(cursor.getString(cursor.getColumnIndex(Permission.P_PAGE)));
                i.setUsers(cursor.getString(cursor.getColumnIndex(Permission.P_USERS)));
                i.setCreated_at(cursor.getString(cursor.getColumnIndex(Permission.P_CREATED_AT)));
                p.add(i);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return p;
    }

    public List<Student> getAllStudents() {
        List<Student> stds = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + Student.TABLE_STUDENT + " ORDER BY " + Student.STD_ADMNO + " DESC;";;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Student i = new Student();
                i.setId(cursor.getInt(cursor.getColumnIndex(Student.STD_ID)));
                i.setIdno(cursor.getString(cursor.getColumnIndex(Student.STD_IDNUMBER)));
                i.setFirstname(cursor.getString(cursor.getColumnIndex(Student.STD_FNAME)));
                i.setSurname(cursor.getString(cursor.getColumnIndex(Student.STD_SNAME)));
                i.setTelephone(cursor.getString(cursor.getColumnIndex(Student.STD_PHONE)));
                i.setYob(cursor.getString(cursor.getColumnIndex(Student.STD_DOB)));
                i.setEmail(cursor.getString(cursor.getColumnIndex(Student.STD_EMAIL)));
                i.setAdmno(cursor.getString(cursor.getColumnIndex(Student.STD_ADMNO)));
                i.setAdmdate(cursor.getString(cursor.getColumnIndex(Student.STD_CREATED_AT)));
                stds.add(i);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return stds;
    }


    public List<Teacher> getAllTeachers() {
        List<Teacher> t = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + Teacher.TABLE_TEACHER + " ORDER BY " + Teacher.T_ID + " DESC;";;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Teacher m = new Teacher();
                m.setId(cursor.getInt(cursor.getColumnIndex(Teacher.T_ID)));
                m.setFname(cursor.getString(cursor.getColumnIndex(Teacher.T_FNAME)));
                m.setSname(cursor.getString(cursor.getColumnIndex(Teacher.T_SNAME)));
                m.setIdno(cursor.getString(cursor.getColumnIndex(Teacher.T_IDNO)));
                m.setMname(cursor.getString(cursor.getColumnIndex(Teacher.T_MNAME)));
                m.setPhone(cursor.getString(cursor.getColumnIndex(Teacher.T_PHONE)));
                m.setEmail(cursor.getString(cursor.getColumnIndex(Teacher.T_EMAIL)));
                m.setSpecial(cursor.getString(cursor.getColumnIndex(Teacher.T_SPECIAL)));
                m.settCreatedat(cursor.getString(cursor.getColumnIndex(Teacher.T_CREATED_AT)));
                t.add(m);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return mpesa list
        return t;
    }

    public List<Mpesa> getAllMpesa() {
        List<Mpesa> mpesas = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + Mpesa.TABLE_MPESA + " ORDER BY " + Mpesa.M_CREATED_AT + " DESC;";;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Mpesa m = new Mpesa();
                m.setService(cursor.getString(cursor.getColumnIndex(Mpesa.M_SERVICE)));
                m.setFname(cursor.getString(cursor.getColumnIndex(Mpesa.M_FNAME)));
                m.setLname(cursor.getString(cursor.getColumnIndex(Mpesa.M_LMANE)));
                m.setTref(cursor.getString(cursor.getColumnIndex(Mpesa.M_TREF)));
                m.setTtime(cursor.getString(cursor.getColumnIndex(Mpesa.M_TTIME)));
                m.setPhone(cursor.getString(cursor.getColumnIndex(Mpesa.M_PHONE)));
                m.setAmount(cursor.getString(cursor.getColumnIndex(Mpesa.M_AMOUNT)));
                m.setId(cursor.getInt(cursor.getColumnIndex(Mpesa.M_ID)));
                mpesas.add(m);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return mpesa list
        return mpesas;
    }


    public List<LessonDone> getAllLessonsdone() {
        List<LessonDone> ld = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + LessonDone.TABLE_LESSON_DONE + " ORDER BY " + LessonDone.LD_DATE + " DESC;";;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                LessonDone m = new LessonDone();
                m.setId(cursor.getInt(cursor.getColumnIndex(LessonDone.LD_ID)));
                m.setAdmno(cursor.getString(cursor.getColumnIndex(LessonDone.LD_ADMNO)));
                m.setStudent(cursor.getString(cursor.getColumnIndex(LessonDone.LD_STUDENT)));
                m.setLesson(cursor.getString(cursor.getColumnIndex(LessonDone.LD_LESSON)));
                m.setCourse(cursor.getString(cursor.getColumnIndex(LessonDone.LD_COURSE)));
                m.setTeacher(cursor.getString(cursor.getColumnIndex(LessonDone.LD_TEACHER)));
                m.setVehicle(cursor.getString(cursor.getColumnIndex(LessonDone.LD_VEHICLE)));
                m.setBranch(cursor.getString(cursor.getColumnIndex(LessonDone.LD_BRANCH)));
                m.setDate(cursor.getString(cursor.getColumnIndex(LessonDone.LD_DATE)));
                ld.add(m);
            } while (cursor.moveToNext());
        }
        cursor.close();
        // close db connection
        db.close();

        // return mpesa list
        return ld;
    }


    public List<BranchCollection> getAllBranchCollection() {
        List<BranchCollection> ld = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + BranchCollection.TABLE_BRANCH_COLLECTION + " ORDER BY " + BranchCollection.BC_DATE + " DESC;";;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                BranchCollection m = new BranchCollection();
                m.setId(cursor.getInt(cursor.getColumnIndex(BranchCollection.BC_ID)));
                m.setBranch(cursor.getString(cursor.getColumnIndex(BranchCollection.BC_BRANCH)));
                m.setFees(cursor.getString(cursor.getColumnIndex(BranchCollection.BC_FEES)));
                m.setDate(cursor.getString(cursor.getColumnIndex(BranchCollection.BC_DATE)));
                ld.add(m);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return mpesa list
        return ld;
    }



    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + User.TABLE_LOGIN + " WHERE " + User.USER_ID +" = 1;" ;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            //user.put("stfid", cursor.getString(1));
            user.put("fname", cursor.getString(1));
            user.put("sname", cursor.getString(2));
            user.put("uname", cursor.getString(3));
            //user.put("email", cursor.getString(3));
            //user.put("branch", cursor.getString(6));
            //user.put("status", cursor.getString(7));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteStd() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(Student.TABLE_STUDENT, null, null);
        db.close();

        Log.d(TAG, "Deleted all students from db!");
    }
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(User.TABLE_LOGIN, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }
    public void deleteCollection() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(BranchCollection.TABLE_BRANCH_COLLECTION, null, null);
        db.close();

        Log.d(TAG, "Deleted all collections");
    }
    public void deleteMpesa() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(Mpesa.TABLE_MPESA, null, null);
        db.close();

        Log.d(TAG, "Deleted all mpesa from db");
    }
    public void deleteStdhome() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(StudentHome.TABLE_STUDENT_HOME, null, null);
        db.close();

        Log.d(TAG, "Deleted all students home from db");
    }
    public void deletePay() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(Payment.TABLE_PAYMENT, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }
    public void deleteLd() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(LessonDone.TABLE_LESSON_DONE, null, null);
        db.close();

        Log.d(TAG, "Deleted all lessons done");
    }
    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }

        /*
    * this method is for getting all the unsynced name
    * so that we can sync it with database
    * */
    public Cursor getUnsyncedUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + User.TABLE_LOGIN + " WHERE " + User.USER_STATUS + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public Cursor getUnsyncedBranches() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + Branch.TABLE_BRANCH + " WHERE " + Branch.BRANCH_STATUS + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }


    public List<Edition> getEdition(String course) {
        // get readable database as we are not inserting anything
        List<Edition> i = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

//        Cursor cursor = db.query(Edition.TABLE_COURSE_EDITION,
//                new String[]{Edition.E_ID, Edition.E_COURSE, Edition.E_NAME, Edition.E_FEES},
//                Edition.E_COURSE + "=?",
//                new String[]{String.valueOf(course)}, null, null, null, null);

        String selectQuery = "SELECT * FROM " + Edition.TABLE_COURSE_EDITION + " WHERE " + Edition.E_COURSE + " = '" +course+"';";

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Edition m = new Edition();
                m.setId(cursor.getInt(cursor.getColumnIndex(Edition.E_ID)));
                m.setCourse(cursor.getString(cursor.getColumnIndex(Edition.E_COURSE)));
                m.setEdition(cursor.getString(cursor.getColumnIndex(Edition.E_NAME)));
                m.setFees(cursor.getString(cursor.getColumnIndex(Edition.E_FEES)));
                i.add(m);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        return i;


    }

    public List<String> getAllLabels(){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Course.TABLE_COURSE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }

    public MyLocation getLocation(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_LOCATION,
                new String[]{LOC_ID, LOC_LAT, LOC_LONG, LOC_CREATED_AT},
                LOC_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        MyLocation loc = new MyLocation(
                cursor.getInt(cursor.getColumnIndex(LOC_ID)),
                cursor.getDouble(cursor.getColumnIndex(LOC_LAT)),
                cursor.getDouble(cursor.getColumnIndex(LOC_LONG)),
                cursor.getString(cursor.getColumnIndex(LOC_CREATED_AT)));

        // close the db connection
        cursor.close();

        return loc;
    }


    public Integer getUserLast() {
        Integer lastid = 9000;
        String selectQuery = "SELECT * FROM " + User.TABLE_LOGIN + " ORDER BY " + User.USER_ID + " DESC LIMIT 1;";;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()){
            lastid = cursor.getInt(0);
        }
        db.close();

        return lastid;
    }


    public String getStudentLast() {
        String lastid = "";
        String selectQuery = "SELECT * FROM " + Student.TABLE_STUDENT + " ORDER BY " + Student.STD_ADMNO + " DESC LIMIT 1;";;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()){
            lastid = cursor.getString(1);
        }
        db.close();

        return lastid;
    }
}
