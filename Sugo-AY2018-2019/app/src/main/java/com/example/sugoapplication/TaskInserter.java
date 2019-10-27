package com.example.sugoapplication;

public class TaskInserter{

    String user_id;
    String task_id;
    String task_type;
    String task_name;
    String taskee_name;
    String task_state;
    String date_needed;
    String starting_amount;
    String final_amount;
    String tasker_name;
    String tasker_id;

    public TaskInserter(){}


    public TaskInserter(String user_id, String task_id, String taskee_name , String task_type, String task_name, String task_state, String date_needed,String starting_amount, String tasker_name){
        this.task_type = task_type;
        this.user_id = user_id;
        this.task_id = task_id;
        this.task_name = task_name;
        this.taskee_name = taskee_name;
        this.task_state = task_state;
        this.date_needed = date_needed;
        this.starting_amount = starting_amount;
        this.tasker_name = tasker_name;
        this.final_amount = "null";
    }

    public String getTasker_name() {
        return tasker_name;
    }

    public String getTasker_id(){ return tasker_id; }

    public String getFinal_amount() { return final_amount; }

    public String getUser_id() {
        return user_id;
    }

    public String getTask_id() {
        return task_id;
    }

    public String getTask_type() {
        return task_type;
    }

    public String getTask_name() {
        return task_name;
    }

    public String getTaskee_name() {
        return taskee_name;
    }

    public String getTask_state() {
        return task_state;
    }

    public String getDate_needed() {
        return date_needed;
    }

    public String getStarting_amount() {
        return starting_amount;
    }
}
