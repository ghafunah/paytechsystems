package com.paytech.paytechsystems.getset;

public class Permission {
    private Integer id, status;
    private String page, users, created_at;
    
    public static final String TABLE_PERMISSION = "permission";
    public static final String P_ID = "id";
    public static final String P_PAGE = "page";
    public static final String P_USERS = "users";
    public static final String P_STATUS = "status";
    public static final String P_CREATED_AT = "created_at";

    public static final String CREATE_TABLE_PERMISSION = "CREATE TABLE "
            + TABLE_PERMISSION + "("
            + P_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + P_PAGE  + " TEXT UNIQUE,"
            + P_USERS + " TEXT,"
            + P_STATUS + " INTEGER,"
            + P_CREATED_AT  + " DATETIME  DEFAULT CURRENT_TIMESTAMP" + ")";

    public Permission(){
        
    }
    public Permission(Integer id, String page, String users, String created_at, Integer status) {
        this.id = id;
        this.page = page;
        this.users = users;
        this.created_at = created_at;
        this.status = status;
    }

    public int getId() {   return id;    }
    public String getPage() {  return page;    }
    public String getUsers() {  return users;    }
    public String getCreated_at() {  return created_at;    }
    public Integer getStatus() {  return status;    }

    public void setId(Integer id) {        this.id = id;  }
    public void setPage(String page) {        this.page = page;  }
    public void setUsers(String users) {        this.users = users;  }
    public void setCreated_at(String created_at) {        this.created_at = created_at;  }
    public void setStatus(Integer status) {        this.status = status;  }

}
