package com.example.user.broncobooks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class ListingDetailActivity extends AppCompatActivity {
    private Listing list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_detail);

        Intent intent = getIntent();
        list = (Listing)intent.getSerializableExtra(BuyFragment.PASS_KEY);

        Log.i("Detail Debug",list.textbook.title);
    }
}
