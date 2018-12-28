package com.paytech.paytechsystems.getset;

public class BranchCollection {
    private Integer id;
    private String branch_name, Fees, date;
    
    public static final String TABLE_BRANCH_COLLECTION = "branch_collection";
    public static final String BC_ID = "id";
    public static final String BC_BRANCH = "branch";
    public static final String BC_FEES = "fees";
    public static final String BC_DATE = "date";

    public static final String CREATE_TABLE_BRANCH_COLLECTION = "CREATE TABLE "
            + TABLE_BRANCH_COLLECTION + "("
            + BC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + BC_BRANCH  + " TEXT,"
            + BC_FEES  + " TEXT,"
            + BC_DATE  + " DATETIME  DEFAULT CURRENT_TIMESTAMP" + ")";

    public BranchCollection(){
        
    }
    public BranchCollection(Integer id, String branch, String Fees, String date) {
        this.id = id;
        this.branch_name = branch;
        this.Fees = Fees;
        this.date = date;
    }

    public int getId() {   return id;    }
    public String getFees() {  return Fees;    }
    public String getBranch() {  return branch_name;    }
    public String getDate() {  return date;    }


    public void setId(Integer id) {        this.id = id;  }
    public void setFees(String fees) {        this.Fees = fees;  }
    public void setBranch(String branch) {        this.branch_name = branch;  }
    public void setDate(String date) {        this.date = date;  }

}
