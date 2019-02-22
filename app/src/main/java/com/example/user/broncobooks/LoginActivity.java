package com.example.user.broncobooks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    SignInButton googleButton;
    FirebaseAuth mAuth;


    public static final String DEBUG_TAG = "LOGIN_DEBUG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        googleButton = (SignInButton) findViewById(R.id.googleBtn);
    }

    /*GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build();

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }*/

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
