package com.example.sugoapplication;

import java.util.List;

public class ViewTaskModel {

    String task_type;
    String taskee_name;
    String date_needed;
    String starting_amount;



    public ViewTaskModel(String task_type, String taskee_name, String date_needed, String starting_amount){
        this.task_type = task_type;
        this.taskee_name = taskee_name;
        this.date_needed = date_needed;
        this.starting_amount = starting_amount;
    }

    public String getTask_type() {
        return task_type;
    }


    public String getTaskee_name() {
        return taskee_name;
    }


    public String getDate_needed() {
        return date_needed;
    }

    public String getStarting_amount() {
        return starting_amount;
    }
}
