package com.purplefront.brightly.Modules;

public class ContactShare {

    private String ContactName;
    private String ContactNumber;
    private boolean isSelected = false;

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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
