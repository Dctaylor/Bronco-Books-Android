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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileDetail extends ListingDetailActivity implements PopupMenu.OnMenuItemClickListener {
    private final String TAG = "DetailedBuyListing";
    public static final String LIST_TAG = "ProfileList";
    private DatabaseReference dbRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG,"This actually worked");
        dbRef = FirebaseDatabase.getInstance().getReference();

        if(list.onSale && list.buyer == null)
            buttonTopView.setText("Remove");
        else if(!list.onSale && list.buyer != null)
            buttonTopView.setText("Confirm");
        else
            buttonTopView.setText("Put on sale");
        buttonTopView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message;

                if(list.onSale && list.buyer == null) {
                    message = "Are you sure you want to remove this listing from sale?";
                    standardOnSaleConfirm(message);
                } else if(!list.onSale && list.buyer != null) {
                    message = "Do you want to confirm this purchase?  This will confirm that " + list.buyer.displayName + " has already paid you $" + list.price + ".";
                    approveSaleConfirm(message);
                }
                else {
                    message = "Are you sure you want to put this listing back up for sale?";
                    standardOnSaleConfirm(message);
                }
            }
        });
        if(!list.onSale && list.buyer != null)
            buttonBottomView.setText("Contact");
        else
            buttonBottomView.setText("Edit Listing");
        buttonBottomView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(list.onSale && list.buyer != null) {
                    showContactMenu(v);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProfileDetail.this);

                    builder.setTitle("Confirmation");
                    builder.setMessage("You want to edit your listing? (You will be placed at the main page after editing.)");

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(ProfileDetail.this, ManualSellActivity.class);
                            intent.putExtra(LIST_TAG, list);
                            startActivityForResult(intent, 2);//use startActivityFromResult so ManualSellForm can check if it was called by ProfileDetail
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
            }
        });
    }

    public void standardOnSaleConfirm(String message) {
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

    public void approveSaleConfirm(String message) {
        AlertDialog.Builder builder =  new AlertDialog.Builder(ProfileDetail.this);

        builder.setTitle("Confirm Purchase");
        builder.setMessage(message);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                String listingPath = "listings/" + list.id+"/purchaseConfirmed";
                dbRef.child(listingPath).setValue(true);
                Toast.makeText(ProfileDetail.this, "Purchase Confirmed", Toast.LENGTH_LONG).show();
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


    //Contact Functions
    public void showContactMenu(View v) {
        PopupMenu contactMenu = new PopupMenu(this, v);
        contactMenu.setOnMenuItemClickListener(this);
        contactMenu.inflate(R.menu.contact_menu);
        contactMenu.show();
    }

    public void showSellerEmailMenu(View v) {
        PopupMenu emailMenu = new PopupMenu(this, v);
        emailMenu.setOnMenuItemClickListener(this);
        emailMenu.inflate(R.menu.seller_contact_emailmenu);
        emailMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{ListingDetailActivity.list.buyer.email});
        String subject;
        String body = "Hello " + ListingDetailActivity.list.buyer.displayName + ",\n\n";

        switch (item.getItemId()) {
            case R.id.contact_email:
                showSellerEmailMenu(findViewById(R.id.buttonBottomView));
                return true;
            case R.id.contact_text:
                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.setData(Uri.parse("sms:" + ListingDetailActivity.list.buyer.phoneNumber));
                try {
                    startActivity(smsIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(ProfileDetail.this, "No Text Message Client available to use", Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.seller_contact_details:
                body = body + "When and Where do you want to meet to finish the purchase and exchange the book? ?\n\nThanks,\n" + FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                subject = "Question About Meeting Up to Exchange Book from Bronco Books";
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT, body);
                startActivity(Intent.createChooser(intent, "Send message"));
                return true;
            case R.id.seller_contact_custom:
                intent.putExtra(Intent.EXTRA_SUBJECT, "");
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
