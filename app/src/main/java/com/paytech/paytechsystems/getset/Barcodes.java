package com.paytech.paytechsystems.getset;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Barcodes {
    private String barcode, bname, location, site;
    //private boolean isSelf;
 
    public Barcodes() {
    }
 
    public Barcodes(String barcode, String bname, String location, String site) {
        this.barcode = barcode;
        this.bname = bname;
        this.location = location;
        this.site = site;
       // this.isSelf = isSelf;
    }
 
    public String getBarcode() {
        return barcode;
    }
 
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
 
     public String getBname() {
         return bname;
     }
 
     public void setBname(String bname) {
         this.bname = bname;
     }     

     public String getLocation() {
         return location;
     }
 
     public void setLocation(String location) {
         this.location = location;
     }     

     public String getSite() {
         return site;
     }
 
     public void setSite(String site) {
         this.site = site;
     }
 
    // public boolean isSelf() {
    //     return isSelf;
    // }
 
    // public void setSelf(boolean isSelf) {
    //     this.isSelf = isSelf;
    // }
 
     @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("barcode", barcode);
        result.put("bname", bname);
        result.put("location", location);
        result.put("site", site);

        return result;
    }
}