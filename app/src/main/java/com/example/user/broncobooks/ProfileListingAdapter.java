package com.example.user.broncobooks;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ProfileListingAdapter extends RecyclerView.Adapter<ProfileListingAdapter.ViewHolder>{
    private List<Listing> mListings;
    public ProfileListingAdapter(List<Listing> l){
        mListings = l;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView title, price, sold;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.titleView);
            price = (TextView)itemView.findViewById(R.id.priceView);
            sold = (TextView)itemView.findViewById(R.id.soldView);
        }
    }

    @NonNull
    @Override
    public ProfileListingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflate = LayoutInflater.from(context);

        View listingView = inflate.inflate(R.layout.layout_recycler_profile,viewGroup,false);

        ProfileListingAdapter.ViewHolder hold = new ProfileListingAdapter.ViewHolder(listingView);

        return hold;
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileListingAdapter.ViewHolder viewHolder, int i) {
        Listing list = mListings.get(i);

        viewHolder.title.setText(list.textbook.title);
        viewHolder.price.setText("$"+Double.toString(list.price));
        viewHolder.sold.setText(list.onSale?"On Sale" : "Sold");
    }

    @Override
    public int getItemCount(){
        return mListings == null ? -1 : mListings.size();
    }


}
