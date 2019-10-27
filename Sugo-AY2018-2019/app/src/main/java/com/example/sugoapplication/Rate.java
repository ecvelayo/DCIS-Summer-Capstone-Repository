package com.example.sugoapplication;

public class Rate {
    String rate_id;
    String bid_id;
    String review;
    float rate_scale;
    String user_id;
    public Rate(){}
    public Rate(String rate_id, String review, float rate_scale, String bid_id, String user_id){
        this.review = review;
        this.rate_scale = rate_scale;
        this.rate_id = rate_id;
        this.bid_id = bid_id;
        this.user_id = user_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getBid_id() {
        return bid_id;
    }

    public String getRate_id() {
        return rate_id;
    }


    public String getReview(){ return  review; }

    public float getRate_scale() { return rate_scale; }

}
