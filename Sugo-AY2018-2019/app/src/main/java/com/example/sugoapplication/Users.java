package com.example.sugoapplication;

public class Users {
    String user_id;
    String fname;
    String lname;
    String course;
    String id_number;
    String phone_number;
    String email;
    String login_id;
    String skills_id;
    float total_reviews;
    float current_rate;
    float tasks_completed;
    float curr_credits;

    public Users() {

    }

    public Users(String user_id, String email, String fname, String lname, String course, String id_number,
                 String phone_number, String login_id, String skills_id) {
        this.user_id = user_id;
        this.login_id = login_id;
        this.fname = fname;
        this.lname = lname;
        this.course = course;
        this.id_number = id_number;
        this.phone_number = phone_number;
        this.email = email;
        this.skills_id = skills_id;
        this.total_reviews = 0;
        this.current_rate = 0;
        this.tasks_completed = 0;
        this.curr_credits = 0;
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

    public void setLogin_id(String login_id) {
        this.login_id = login_id;
    }

    public void setSkills_id(String skills_id) {
        this.skills_id = skills_id;
    }

    public void setTotal_reviews(float total_reviews) {
        this.total_reviews = total_reviews;
    }

    public void setCurrent_rate(float current_rate) {
        this.current_rate = current_rate;
    }

    public void setTasks_completed(float tasks_completed) {
        this.tasks_completed = tasks_completed;
    }

    public void setCurr_credits(float curr_credits) { this.curr_credits = curr_credits; }

    public String getLogin_id() {
        return login_id;
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

    public String getSkills_id() {
        return skills_id;
    }

    public float getTotal_reviews() {
        return total_reviews;
    }

    public float getCurrent_rate() {
        return current_rate;
    }


    public float getTasks_completed() {
        return tasks_completed;
    }

    public float getCurr_credits() { return curr_credits; }
}