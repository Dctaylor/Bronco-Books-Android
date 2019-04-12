package com.example.user.broncobooks;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileDetail extends ListingDetailActivity {
    private final String TAG = "DetailedBuyListing";
    private DatabaseReference dbRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG,"This actually worked");
        dbRef = FirebaseDatabase.getInstance().getReference();

        if(list.onSale)
            buttonTopView.setText("Remove");
        else
            buttonTopView.setText("Put on sale");
        buttonTopView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message;

                if(list.onSale)
                    message = "Are you sure you want to remove this listing from sale?";
                else
                    message = "Are you sure you want to put this listing back up for sale?";
                AlertDialog.Builder builder =  new AlertDialog.Builder(ProfileDetail.this);

                builder.setTitle("Confirmation");
                builder.setMessage(message);

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        String listingPath = "listings/" + list.id+"/onSale";
                        dbRef.child(listingPath).setValue(!list.onSale);
                        Toast.makeText(ProfileDetail.this, "Sale Flag updated", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(ProfileDetail.this, TestingActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                builder.create().show();
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
