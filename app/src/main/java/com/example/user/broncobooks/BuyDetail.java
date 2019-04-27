package com.example.user.broncobooks;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class BuyDetail extends ListingDetailActivity implements PopupMenu.OnMenuItemClickListener{
    private final String TAG = "DetailedBuyListing";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG,"This actually worked");


        buttonTopView.setText("Buy");
        buttonTopView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPurchaseType();
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

    //Purchase Functions
    public void checkPurchaseType() {
        if(list.paymentMethod.equals("Cash") || list.paymentMethod.equals("Check")) {
            cashCheckBuy();
        } else if(list.paymentMethod.equals("Google Pay")) {
            googlePayBuy();
        } else
            Toast.makeText(BuyDetail.this, "Android does not support Apple Pay. Please contact the seller about whether or not they will accept another payment method.", Toast.LENGTH_LONG).show();
    }

    public void cashCheckBuy() {
        String message = "Do you want to purchase this listing with " + list.paymentMethod + "?  The purchase will be pending until the seller (" + list.seller.displayName + ") confirms the sale.";
        AlertDialog.Builder builder =  new AlertDialog.Builder(this);

        builder.setTitle("Confirmation");
        builder.setMessage(message);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseUser tempUser = FirebaseAuth.getInstance().getCurrentUser();
                User buyer = new User(tempUser.getEmail(), tempUser.getDisplayName(), LoginActivity.userPhoneNumber);
                list.setBuyer(buyer);
                list.onSale = false;
                list.purchaseConfirmed = false;
                String listingpath = "/listings/" + list.id;
                FirebaseDatabase.getInstance().getReference().child(listingpath).setValue(list);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.create().show();
    }

    public void googlePayBuy() {
        String message = "Do you want to purchase this listing with Google Pay?  You will be redirected to your Messaging App to send the payment to " + list.seller.displayName + " using Google Pay Send.  " +
                "Please do not send the payment until you and the seller have discussed/met regarding the purchase";
        AlertDialog.Builder builder =  new AlertDialog.Builder(this);

        builder.setTitle("Confirm Purchase");
        builder.setMessage(message);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseUser tempUser = FirebaseAuth.getInstance().getCurrentUser();
                User buyer = new User(tempUser.getEmail(), tempUser.getDisplayName(), LoginActivity.userPhoneNumber);
                list.setBuyer(buyer);
                list.onSale = false;
                list.purchaseConfirmed = false;

                Intent sms = new Intent(Intent.ACTION_SENDTO);
                sms.setType("vnd.android-dir/mms-sms");
                sms.setData(Uri.parse("sms:" + ListingDetailActivity.list.seller.phoneNumber));
                sms.putExtra("exit_on_sent", true);
                try{
                    startActivityForResult(sms, 1);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(BuyDetail.this, "No Text Message Client available to use", Toast.LENGTH_LONG).show();
                }

                String listingpath = "/listings/" + list.id;
                FirebaseDatabase.getInstance().getReference().child(listingpath).setValue(list);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.create().show();
    }

    //Contact Functions
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
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{ListingDetailActivity.list.seller.email});
        String subject = ListingDetailActivity.list.textbook.title;
        String body = "Hello " + ListingDetailActivity.list.seller.displayName + ",\n\n";

        switch(item.getItemId()) {
            case R.id.contact_email:
                showEmailMenu(findViewById(R.id.buttonBottomView));
                return true;
            case R.id.contact_text:
                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.setData(Uri.parse("sms:" + ListingDetailActivity.list.seller.phoneNumber));
                try{
                    startActivity(smsIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(BuyDetail.this, "No Text Message Client available to use", Toast.LENGTH_LONG).show();
                }
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

    @Override
    public void onClick(View v) {

    }
}
