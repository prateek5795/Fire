package com.example.prateek.fireapp;


import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class FireApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
