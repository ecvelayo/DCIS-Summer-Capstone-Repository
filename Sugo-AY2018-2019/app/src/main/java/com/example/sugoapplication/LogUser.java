package com.example.sugoapplication;

public class LogUser {

    private static LogUser instance;


    private LogUser() {
    }

    public static synchronized LogUser getInstance(){
        if(instance == null){
            instance = new LogUser();
        }
        return instance;

    }

    String user_id;
    String fname;
    String lname;
    String course;
    String id_number;
    String phone_number;
    String email;
    String bid_amount;

    public LogUser( String user_id, String email, String fname, String lname, String course, String id_number, String phone_number, String bid_amount) {
        this.bid_amount = bid_amount;
        this.user_id = user_id;
        this.fname = fname;
        this.lname = lname;
        this.course = course;
        this.id_number = id_number;
        this.phone_number = phone_number;
        this.email = email;
    }

    public String getBid_amount() {
        return bid_amount;
    }

    public String getEmail() {
        return email;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getCourse() {
        return course;
    }

    public String getId_number() {
        return id_number;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public static void setInstance(LogUser instance) {
        LogUser.instance = instance;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
