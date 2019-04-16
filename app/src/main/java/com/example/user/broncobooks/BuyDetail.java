package com.example.user.broncobooks;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class BuyDetail extends ListingDetailActivity implements PopupMenu.OnMenuItemClickListener {
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
                showContactMenu(v);
            }
        });
    }

    public void showContactMenu(View v) {
        PopupMenu contactMenu = new PopupMenu(this, v);
        contactMenu.setOnMenuItemClickListener(this);
        contactMenu.inflate(R.menu.contact_menu);
        contactMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, ListingDetailActivity.list.seller.email);

        switch(item.getItemId()) {
            case R.id.contact_price:
                intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
                intent.putExtra(Intent.EXTRA_TEXT, "mail body");
                startActivity(Intent.createChooser(intent, ""));
                return true;
            case R.id.contact_pic:
                intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
                intent.putExtra(Intent.EXTRA_TEXT, "mail body");
                startActivity(Intent.createChooser(intent, ""));
                return true;
            case R.id.contact_custom:
                intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
                intent.putExtra(Intent.EXTRA_TEXT, "mail body");
                startActivity(Intent.createChooser(intent, ""));
                return true;
            default:
                return false;
        }
    }
}
