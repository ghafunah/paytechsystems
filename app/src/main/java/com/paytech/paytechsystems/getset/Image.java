package com.paytech.paytechsystems.getset;

public class Image {
    private String small, medium, large, name, timestamp;

    public Image(){

    }
    public Image(String small, String medium, String large) {
        this.small = small;
        this.medium = medium;
        this.large = large;
    }

    public String getSmall() {  return small;    }
    public String getMedium() {  return medium;    }
    public String getLarge() {  return large;    }
    public String getName() {  return name;    }
    public String getTimestamp() {  return timestamp;    }

    public void setSmall(String small) {        this.small = small;  }
    public void setMedium(String medium) {        this.medium = medium;  }
    public void setLarge(String large) {        this.large = large;  }
    public void setName(String name) {        this.name = name;  }
    public void setTimestamp(String timestamp) {        this.timestamp = timestamp;  }

}
