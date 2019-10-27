package com.example.sugoapplication;

public class HistoryModel {

    String task_type;
    String task_name;
    String tasker_name;
    String start_amount;
    String taskee_name;
    String bid_amount;
    String date_needed;

    public HistoryModel(){}


    public HistoryModel(String task_type, String task_name, String tasker_name, String start_amount, String taskee_name, String bid_amount, String date_needed){

        this.task_type = task_type;
        this.task_name = task_name;
        this.tasker_name = tasker_name;
        this.start_amount = start_amount;
        this.taskee_name = taskee_name;
        this.bid_amount = bid_amount;
        this.date_needed = date_needed;
    }

    public String getTask_type() {
        return task_type;
    }

    public String getTask_name() {
        return task_name;
    }

    public String getTasker_name() {
        return tasker_name;
    }

    public String getStart_amount() {
        return start_amount;
    }

    public String getTaskee_name() {
        return taskee_name;
    }

    public String getBid_amount() {
        return bid_amount;
    }

    public String getDate_needed() {
        return date_needed;
    }
}
