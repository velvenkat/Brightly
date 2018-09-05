package com.purplefront.brightly.Modules;

import java.io.Serializable;

public class ContactShare implements Serializable {

    private String ContactName;
    private String ContactNumber;

    public ContactShare(String contactName, String contactNumber) {
        this.ContactName = contactName;
        this.ContactNumber = contactNumber;
    }

    public ContactShare() {

    }


    public String getContactName() {
        return ContactName;
    }

    public void setContactName(String contactName) {
        ContactName = contactName;
    }

    public String getContactNumber() {
        return ContactNumber;
    }

    public void setContactNumber(String contactNumber) {
        ContactNumber = contactNumber;
    }
}
