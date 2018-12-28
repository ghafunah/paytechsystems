package com.paytech.paytechsystems.getset;

import com.google.gson.annotations.SerializedName;

public class Paybill {
    private Integer id, status;//transactionType,
    private String  transID, transTime, transAmount, businessShortCode, billRefNumber, invoiceNumber, thirdPartyTransID,
mSISDN, firstName, middleName, lastName, orgAccountBalance, created_at;
    @SerializedName("transactionType")
    private String transactionType;

    public static final String TABLE_PAYBILL = "paybill_payments";
    //Vehicle table fields

    public static final String PB_ID = "id";
    public static final String TRANSACTIONTYPE = "TransactionType";
    public static final String TRANSID = "TransID";
    public static final String TRANSTIME= "TransTime";
    public static final String TRANSAMOUNT = "TransAmount";
    public static final String BUSINESSSHORTCODE = "BusinessShortCode";
    public static final String BILLREFNUMBER = "BillRefNumber";
    public static final String INVOICENUMBER = "InvoiceNumber";
    public static final String ORGACCOUNTBALANCE = "OrgAccountBalance";
    public static final String THIRDPARTYTRANSID = "ThirdPartyTransID";
    public static final String MSISDN = "MSISDN";
    public static final String FIRSTNAME = "FirstName";
    public static final String MIDDLENAME = "MiddleName";
    public static final String LASTNAME = "LastName";
    public static final String CREATED_AT = "created_at";

    public static final String CREATE_TABLE_VEHICLE = "CREATE TABLE "
            + TABLE_PAYBILL + "("
            + PB_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + TRANSACTIONTYPE  + " TEXT,"
            + TRANSID  + " TEXT UNIQUE,"
            + TRANSTIME + " TEXT,"
            + TRANSAMOUNT + " TEXT,"
            + BUSINESSSHORTCODE + " INTEGER,"
            + BILLREFNUMBER + " TEXT,"
            + INVOICENUMBER + " TEXT,"
            + ORGACCOUNTBALANCE + " TEXT,"
            + THIRDPARTYTRANSID + " TEXT,"
            + MSISDN + " TEXT,"
            + FIRSTNAME + " TEXT,"
            + MIDDLENAME + " TEXT,"
            + LASTNAME + " TEXT,"
            + CREATED_AT  + " DATETIME  DEFAULT CURRENT_TIMESTAMP" + ")";

    public Paybill(){
        
    }
    public Paybill(Integer id, String transID, String transTime, String transAmount, String businessShortCode, String billRefNumber, String invoiceNumber, String thirdPartyTransID,
String mSISDN, String  firstName, String  middleName, String lastName, String orgAccountBalance, String created_at) {
        this.id = id;
        this.transactionType = transactionType;
        this.transID = transID;
        this.transTime = transTime;
        this.transAmount = transAmount;
        this.businessShortCode = businessShortCode;
        this.billRefNumber = billRefNumber;
        this.invoiceNumber = invoiceNumber;
        this.thirdPartyTransID = thirdPartyTransID;
        this.orgAccountBalance = orgAccountBalance;
        this.mSISDN = mSISDN;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.created_at = created_at;

    }

    public int getId() {   return id;    }
    public String getTransactionType() {  return transactionType;    }
    public String getTransID() {  return transID;    }
    public String getTransTime() {  return transTime;    }
    public String getTransAmount() {  return transAmount;    }
    public String getBusinessShortCode() {  return businessShortCode;    }
    public String getBillRefNumber() {  return billRefNumber;    }
    public String getInvoiceNumber() {  return invoiceNumber;    }
    public String getThirdPartyTransID() {  return thirdPartyTransID;    }
    public String getOrgAccountBalance() {  return orgAccountBalance;    }
    public String getmSISDN() {  return mSISDN;    }
    public String getFirstName() {  return firstName;    }
    public String getLastName() {  return lastName;    }
    public String getMiddleName() {  return middleName;    }
    public String getCreated_at() {  return created_at;    }


    public void setId(Integer id) {  this.id = id;  }
    public void setTransactionType(String transactionType) {  this.transactionType = transactionType;  }
    public void setTransID(String transID) {  this.transID = transID;  }
    public void setTransTime(String transTime) {  this.transTime = transTime;  }
    public void setTransAmount(String transAmount) {  this.transAmount = transAmount;  }
    public void setBusinessShortCode(String businessShortCode) {  this.businessShortCode = businessShortCode;  }
    public void setBillRefNumber(String billRefNumber) {  this.billRefNumber = billRefNumber;  }
    public void setInvoiceNumber(String invoiceNumber) {  this.invoiceNumber = invoiceNumber;  }
    public void setThirdPartyTransID(String thirdPartyTransID) {  this.thirdPartyTransID = thirdPartyTransID;  }
    public void setOrgAccountBalance(String orgAccountBalance) {  this.orgAccountBalance = orgAccountBalance;  }
    public void setmSISDN(String mSISDN) {  this.mSISDN = mSISDN;  }
    public void setFirstName(String firstName) {  this.businessShortCode = businessShortCode;  }
    public void setMiddleName(String middleName) {  this.middleName = middleName;  }
    public void setLastName(String lastName) {  this.lastName = lastName;  }
    public void setCreated_at(String created_at) {  this.created_at = created_at;  }

}
