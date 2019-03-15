package com.example.user.broncobooks;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

public class ListingPictureAdapter extends RecyclerView.Adapter<ListingPictureAdapter.ViewHolder>{
    ArrayList<byte[]> bitmapList;

    public ListingPictureAdapter(ArrayList<byte[]> bitmapList){
        this.bitmapList = bitmapList;
    }

    @NonNull
    @Override
    public ListingPictureAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflate = LayoutInflater.from(context);

        View listingView = inflate.inflate(R.layout.layout_recycler_picture,viewGroup,false);

        ListingPictureAdapter.ViewHolder hold = new ListingPictureAdapter.ViewHolder(listingView);

        return hold;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        byte[] bytes = bitmapList.get(i);
        viewHolder.iView.setImageBitmap(BitmapFactory.decodeByteArray(bytes,0,bytes.length));

    }

    @Override
    public int getItemCount(){
        return bitmapList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView iView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iView = itemView.findViewById(R.id.imageView);
        }
    }
}
