package com.example.user.broncobooks;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class BuyDetail extends ListingDetailActivity {
    private final String TAG = "DetailedBuyListing";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG,"This actually worked");

        buttonTopView.setText("Buy");
        buttonTopView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BuyDetail.this, "TODO: Implement Buy Button functionality", Toast.LENGTH_LONG).show();
            }
        });

        buttonBottomView.setText("Contact");
        buttonBottomView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Toast.makeText(BuyDetail.this,"TODO: Implement Contact Button functionality", Toast.LENGTH_LONG).show();
            }
        });
    }
}
