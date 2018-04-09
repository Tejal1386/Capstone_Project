package com.example.capstone.furniturestore.Models;

/**
 * Created by mankirankaur on 2018-03-31.
 */

public class Address {

    //private String UserID;
    private String uaddress;
    private String ufullname;
    private String ucity;
    private String ustate;
    private String uphone;
    //private String UserPostalcode;


    public Address(){

    }

    public Address(String user_fullname,String user_address,String user_city,String user_state,String user_phone) {

        //UserID = user_id;
        ufullname = user_fullname;
        uaddress = user_address;
        ucity = user_city;
        ustate = user_state;
        uphone = user_phone;
        //UserPostalcode = user_postalcode;
    }

    public String getUaddress() {
        return uaddress;
    }

    public void setUaddress(String uaddress) {
        this.uaddress = uaddress;
    }

    public String getUfullname() {
        return ufullname;
    }

    public void setUfullname(String ufullname) {
        this.ufullname = ufullname;
    }

    public String getUcity() {
        return ucity;
    }

    public void setUcity(String ucity) {
        this.ucity = ucity;
    }

    public String getUstate() {
        return ustate;
    }

    public void setUstate(String ustate) {
        this.ustate = ustate;
    }

    public String getUphone() {
        return uphone;
    }

    public void setUphone(String uphone) {
        this.uphone = uphone;
    }
}
