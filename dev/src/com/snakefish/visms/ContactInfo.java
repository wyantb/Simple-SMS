package com.snakefish.visms;

/**
 * A model object containing contact data.
 */
public class ContactInfo {

    private String mDisplayName;
    private String mPhoneNumber;
    private long id;
    
    public void setDisplayName(String displayName) {
        this.mDisplayName = displayName;
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.mPhoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public long getId() {
        return id;
    }
}
