package com.example.sugoapplication;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class BidderView extends ArrayAdapter<BidModel> {

    private Activity context;
    private List<BidModel> bidderList;

    DatabaseReference databaseBid;

    public BidderView(Activity context, List<BidModel> bidderList){
        super(context, R.layout.bidder_layout, bidderList);
        this.context = context;
        this.bidderList = bidderList;
    }

    @Override
    public View getView(int position, View convertView,  ViewGroup parent) {
        final LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.bidder_layout,null,true);
        databaseBid = FirebaseDatabase.getInstance().getReference("bids");


        TextView bidder = (TextView) listViewItem.findViewById(R.id.bidder);
        TextView bid_amount = (TextView) listViewItem.findViewById(R.id.bid_amount);

        BidModel bidModel = bidderList.get(position);

        bidder.setText(bidModel.getTasker_name());
        bid_amount.setText(bidModel.getBid_amount());

        return listViewItem;
    }
}
