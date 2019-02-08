package com.example.user.broncobooks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class LoginActivity extends AppCompatActivity {
    public static final String DEBUG_TAG = "LOGIN_DEBUG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onLogin(View view){
        Intent intent = new Intent(this,ListingActivity.class);
        Log.i(DEBUG_TAG,"Login button pressed");
        startActivity(intent);

    }

    public void onTabbed(View view){
        Intent intent = new Intent(this,TestingActivity.class);
        Log.i(DEBUG_TAG,"Tabbedd button pressed");
        startActivity(intent);
    }
}
