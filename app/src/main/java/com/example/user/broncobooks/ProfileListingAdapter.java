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
    private ListingAdapter.RecyclerItemListener mListen;

    public ProfileListingAdapter(List<Listing> l, ListingAdapter.RecyclerItemListener listen){
        mListings = l;
        mListen = listen;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView title, price, sold;
        private ListingAdapter.RecyclerItemListener listen;

        public ViewHolder(@NonNull View item, ListingAdapter.RecyclerItemListener listener) {
            super(item);
            listen = listener;
            item.setOnClickListener(this);

            title = (TextView)itemView.findViewById(R.id.titleView);
            price = (TextView)itemView.findViewById(R.id.priceView);
            sold = (TextView)itemView.findViewById(R.id.soldView);


        }

        @Override
        public void onClick(View v) {
            listen.onClick(v,getAdapterPosition());
        }
    }


    @NonNull
    @Override
    public ProfileListingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflate = LayoutInflater.from(context);

        View listingView = inflate.inflate(R.layout.layout_recycler_profile,viewGroup,false);

        ProfileListingAdapter.ViewHolder hold = new ProfileListingAdapter.ViewHolder(listingView,mListen);

        return hold;
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileListingAdapter.ViewHolder viewHolder, int i) {
        Listing list = mListings.get(i);

        viewHolder.title.setText(list.textbook.title);
        viewHolder.price.setText(String.format("$%2.2f",list.price));
        viewHolder.sold.setText(list.onSale?"On Sale" : "Sold");
    }

    @Override
    public int getItemCount(){
        return mListings == null ? -1 : mListings.size();
    }


}
