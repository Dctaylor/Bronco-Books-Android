package com.example.user.broncobooks;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class ProfileDetail extends ListingDetailActivity {
    private final String TAG = "DetailedBuyListing";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG,"This actually worked");

        buttonTopView.setText("Remove");
        buttonTopView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileDetail.this, "TODO: Implement Remove Button functionality", Toast.LENGTH_LONG).show();
            }
        });

        buttonBottomView.setText("Edit Listing");
        buttonBottomView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileDetail.this,"TODO: Implement Edit Listing Button functionality", Toast.LENGTH_LONG).show();
            }
        });
    }
}
