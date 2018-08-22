package com.purplefront.brightly.Modules;

public class UserModule {

    String userId;
    String set_id;
    String set_name;
    String card_name = "";
    String card_id;
    String card_description = "";
    String encoded_string = "";
    String image_name = "";

    public UserModule() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSet_id() {
        return set_id;
    }

    public void setSet_id(String set_id) {
        this.set_id = set_id;
    }

    public String getSet_name() {
        return set_name;
    }

    public void setSet_name(String set_name) {
        this.set_name = set_name;
    }

    public String getCard_name() {
        return card_name;
    }

    public void setCard_name(String card_name) {
        this.card_name = card_name;
    }

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    public String getCard_description() {
        return card_description;
    }

    public void setCard_description(String card_description) {
        this.card_description = card_description;
    }

    public String getEncoded_string() {
        return encoded_string;
    }

    public void setEncoded_string(String encoded_string) {
        this.encoded_string = encoded_string;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }
}
