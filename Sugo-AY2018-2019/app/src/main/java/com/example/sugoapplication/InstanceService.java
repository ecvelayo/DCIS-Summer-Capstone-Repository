package com.example.sugoapplication;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;

public class InstanceService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String token) {
        Log.d("MyFirebaseToken", "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
    }

}
