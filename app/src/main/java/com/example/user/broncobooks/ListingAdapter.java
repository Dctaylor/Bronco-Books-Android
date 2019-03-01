package com.example.user.broncobooks;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class ListingAdapter extends RecyclerView.Adapter<ListingAdapter.ViewHolder> {

    private String TAG = "AdapterList";
    private List<Listing> mList;

    private RecyclerItemListener mListen;

    public ListingAdapter(List<Listing> list, RecyclerItemListener l){
        mListen = l;
        mList = list;
    }

    public interface RecyclerItemListener{
        void onClick(View view, int pos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private RecyclerItemListener listen;

        public TextView title;
        public TextView author;
        public TextView price;
        public ViewHolder(@NonNull View itemView, RecyclerItemListener listener) {
            super(itemView);
            listen = listener;
            itemView.setOnClickListener(this);

            title = (TextView)itemView.findViewById(R.id.titleView);
            author = (TextView)itemView.findViewById(R.id.authorView);
            price = (TextView)itemView.findViewById(R.id.priceView);
        }

        @Override
        public void onClick(View v) {
            listen.onClick(v,getAdapterPosition());
        }
    }
    @NonNull
    @Override
    public ListingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflate = LayoutInflater.from(context);

        View listingView = inflate.inflate(R.layout.layout_recycler_buy,viewGroup,false);

        ViewHolder hold = new ViewHolder(listingView,mListen);

        return hold;
    }

    @Override
    public int getItemCount() {
        if(mList==null)
            return -1;
        return mList.size();
    }

    public void onBindViewHolder(@NonNull ListingAdapter.ViewHolder viewHolder, int i) {
        Listing listing = mList.get(i);

        Log.i(TAG,"View made for "+Integer.toString(i));
        viewHolder.title.setText(listing.textbook.title);
        viewHolder.author.setText(listing.textbook.authors.get(0));//TODO, display multiple authors
        viewHolder.price.setText("$" + Double.toString(listing.price));
    }
}
