package com.example.chattingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class UserProfile extends AppCompatActivity {

    public String username, userid;

    public UserProfile(){

    }

    public UserProfile(String userid,String username) {
        this.userid = userid;
        this.username = username;
    }



    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
    }
}