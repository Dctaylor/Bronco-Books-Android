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

    public void showEmailMenu(View v) {
        PopupMenu emailMenu = new PopupMenu(this, v);
        emailMenu.setOnMenuItemClickListener(this);
        emailMenu.inflate(R.menu.contact_emailmenu);
        emailMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{ListingDetailActivity.list.seller.email});
        String subject = ListingDetailActivity.list.textbook.title;
        String body = "Hello " + ListingDetailActivity.list.seller.displayName + ",\n\n";

        switch(item.getItemId()) {
            case R.id.contact_email:
                showEmailMenu(findViewById(R.id.buttonBottomView));
                return true;
            case R.id.contact_text:
                Toast.makeText(BuyDetail.this, "TODO: Implement Text Contact", Toast.LENGTH_LONG).show();
                return true;
            case R.id.contact_price:
                body = body + "Would you be willing to sell this textbook at a price of $ ?\n\nThanks,\n" + FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                subject = subject + " Price";
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT, body);
                startActivity(Intent.createChooser(intent, "Send message"));
                return true;
            case R.id.contact_pic:
                body = body + "Can you please post more images of the book? I would like to get a better idea of its condition.\n\nThanks,\n" + FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                subject = subject + " Pictures";
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT, body);
                startActivity(Intent.createChooser(intent, ""));
                return true;
            case R.id.contact_payment:
                body = body + "Would you be willing to accept the payment for this book through ?\n\nThanks,\n" + FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                subject = subject + " Payment Method";
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT, body);
                startActivity(Intent.createChooser(intent, ""));
                return true;
            case R.id.contact_custom:
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(intent, ""));
                return true;
            default:
                return false;

        }

    }
}
