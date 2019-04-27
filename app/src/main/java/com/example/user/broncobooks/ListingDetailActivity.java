package com.example.user.broncobooks;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class ListingDetailActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG= "DetailerView";
    protected static Listing list;
    protected TextView titleView,subtitleView,authorView,publisherView;
    protected TextView pubDateView,languageView,editionView,paymentMethodView;
    protected TextView priceView,buttonTopView,numPageView,bindingView,postedView,buttonBottomView;
    protected LinearLayout gallery;
    protected FirebaseStorage storage;
    protected StorageReference storageReference;

    protected RecyclerView rView;
    protected ListingPictureAdapter adapter;
    protected ArrayList<byte[]> mBMList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_detail);

        mBMList = new ArrayList<byte[]>();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        Intent intent = getIntent();
        list = (Listing)intent.getSerializableExtra(BuyFragment.PASS_KEY);
        titleView = (TextView)findViewById(R.id.titleView);
        subtitleView = (TextView)findViewById(R.id.subtitleView);
        authorView = (TextView)findViewById(R.id.authorView);
        publisherView = (TextView)findViewById(R.id.publisherView);
        pubDateView = (TextView)findViewById(R.id.pubDateView);
        languageView = (TextView)findViewById(R.id.languageView);
        editionView = (TextView)findViewById(R.id.editionView);
        paymentMethodView = (TextView)findViewById(R.id.paymentMethodView);
        priceView = (TextView)findViewById(R.id.priceView);
        numPageView = (TextView)findViewById(R.id.numPageView);
        bindingView = (TextView)findViewById(R.id.bindingView);
        postedView = (TextView)findViewById(R.id.postedView);

        buttonTopView = (TextView)findViewById(R.id.buttonTopView);
        buttonBottomView = (TextView)findViewById(R.id.buttonBottomView);

        rView = (RecyclerView)findViewById(R.id.pictureView);

        titleView.setText(list.textbook.title);
        subtitleView.setText(list.textbook.subtitle);

        //Log.i(TAG,list.id);

        //Setting up String for authors
        String temp = "";
        if(list.textbook.authors != null) {
            for (String s : list.textbook.authors) {
                temp = temp + s + ", ";
            }
        }
        if(list.textbook.authors.size() == 1) {//if we only added one author, take out comma
            temp = temp.substring(0, temp.length() - 2);
        }

        authorView.setText("Author(s): " + temp);
        publisherView.setText("Publisher: " + list.textbook.publisher);
        pubDateView.setText(list.textbook.publishedDate);
        languageView.setText(list.textbook.language);
        editionView.setText(list.textbook.edition + " Edition");
        editionView.setVisibility(list.textbook.edition.length()!=0 ? View.VISIBLE : View.INVISIBLE);
        paymentMethodView.setText(list.paymentMethod);
        priceView.setText(String.format("$%2.2f",list.price));
        numPageView.setText(Integer.toString(list.textbook.pages) +" pages");
        bindingView.setText(list.textbook.binding);


        DateFormat format = new SimpleDateFormat("MM/dd/yyyy, hh:mm a");
        Date post = new Date((long)list.epochTimePosted * 1000);
        postedView.setText("Posted: " + format.format(post));

        adapter = new ListingPictureAdapter(mBMList);
        boolean keepGoing = true;
        for(int x = 0; x < 4; x++){
            StorageReference imageRef = storageReference.child("images").child(list.id + "_" + Integer.toString(x)+".jpeg");
            imageRef.getBytes(1*1024*1024)
                    .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            mBMList.add(bytes);
                            adapter.notifyDataSetChanged();
                        }
                    });

        }

        rView.setAdapter(adapter);
        rView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));


        Log.i("Detail Debug",list.textbook.title);
    }

}
