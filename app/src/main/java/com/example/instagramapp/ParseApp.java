package com.example.instagramapp;

import android.app.Application;

import com.example.instagramapp.model.Post;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);
        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("joobees-hoobees")
                .clientKey("k3y-of-th3-mast3r")
                .server("http://aublhr99-fbu-instagram.herokuapp.com/parse")
                .build();
        Parse.initialize(configuration);
    }
}
