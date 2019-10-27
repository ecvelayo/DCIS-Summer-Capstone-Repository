package com.example.sugoapplication;


public class BidModel {

    private String bid_id;
    private String task_id;
    private String user_id;
    private String bid_amount;
    private String taskee_name;
    private String tasker_name;

    public BidModel(){}


    public BidModel(String bid_id, String task_id, String user_id, String bid_amount, String tasker_name, String taskee_name) {
        this.taskee_name = taskee_name;
        this.tasker_name = tasker_name;
        this.bid_id = bid_id;
        this.task_id = task_id;
        this.user_id = user_id;
        this.bid_amount = bid_amount;
    }

    public String getTaskee_name() {
        return taskee_name;
    }

    public String getBid_id() {
        return bid_id;
    }

    public String getTasker_name() {
        return tasker_name;
    }


    public String getTask_id() {
        return task_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getBid_amount() {
        return bid_amount;
    }
}
