package com.digital_easy.info_share.Modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactHelper {

    public ContactHelperModule contact;

    public ContactHelper(ContactHelperModule contactHelperModule){
        contact=contactHelperModule;
    }

    /*Map<String, List<ContactHelperModule>> contactMap = new HashMap<>();
    public boolean isContactAdded;

    public Map<String, List<ContactHelperModule>> getContactMap() {
        return contactMap;
    }

    public void setContactMap(Map<String, List<ContactHelperModule>> contactMap) {
        this.contactMap = contactMap;
    }

    public void add_contact_all_field(int Position, List<ContactHelperModule> contactHelperModuleList) {
        contactMap.put(String.valueOf(Position), contactHelperModuleList);
    }*/

    /**
     * MultiViewAdapterPosition is the Key
     *
     * @param Key
     */
    /*public void add_contact(int Key, ContactHelperModule ModelObj) {
       *//* if(Key==1){
            Key=0;
        }
        else {
            Key=Key-2;
        }*//*
        if (isIndexAvail(String.valueOf(Key))) {
            List<ContactHelperModule> contactHelperModuleListObj = contactMap.get(String.valueOf(Key));
            boolean isModelPresent = false;
            int Pos = 0;
            for (ContactHelperModule contactHelperModuleObj : contactHelperModuleListObj) {
                if (contactHelperModuleObj.type.equals(ModelObj.type)) {
                    isModelPresent = true;
                    break;
                }
                Pos++;
            }
            if (isModelPresent) {
                contactHelperModuleListObj.set(Pos, ModelObj);
            } else {
                contactHelperModuleListObj.add(ModelObj);
            }
        } else {
            List<ContactHelperModule> contactHelperModuleListObj = new ArrayList<>();
            contactHelperModuleListObj.add(ModelObj);

            contactMap.put(String.valueOf(Key), contactHelperModuleListObj);
        }
    }

    private boolean isIndexAvail(String Key) {
        if (contactMap.containsKey(Key)) {
            return true;
        }
        return false;
    }*/
}


