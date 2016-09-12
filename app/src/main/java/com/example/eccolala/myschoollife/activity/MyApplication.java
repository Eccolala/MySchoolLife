package com.example.eccolala.myschoollife.activity;

import android.app.Application;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by eccolala on 16-9-11.
 */

public class MyApplication extends Application {
    @Override public void onCreate() {
        super.onCreate();

        // Configure Realm for the application
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this).build();
        Realm.deleteRealm(realmConfiguration); // Clean slate
        Realm.setDefaultConfiguration(realmConfiguration); // Make this Realm the default
    }
}
