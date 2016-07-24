package com.fame.plumbum.chataround.database;

/**
 * Created by pankaj on 24/7/16.
 */
public class UserTable {
    String user_id;
    String user_name;
    String image;
    String phone;

    public UserTable(){

    }

    public UserTable(String user_id, String user_name, String image, String phone){
        this.user_id = user_id;
        this.user_name = user_name;
        this.image = image;
        this.phone = phone;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getImage() {
        return image;
    }

    public String getPhone() {
        return phone;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
    }
}
