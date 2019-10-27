package com.example.sugoapplication;

import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class HistoryView extends ArrayAdapter<TaskInserter> {

    private FragmentActivity context;
    private List<HistoryModel> historyList;
    public static final String TASKEE_NAME = "taskee_name";

    DatabaseReference databaseTask;

    public HistoryView(FragmentActivity activity, int list) {
        super( activity, list );
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate( R.layout.history_layout,null);
        databaseTask = FirebaseDatabase.getInstance().getReference("task");

        TextView textViewTitle = (TextView) listViewItem.findViewById( R.id.title );
        TextView textViewTaskee = (TextView) listViewItem.findViewById( R.id.taskee );
        TextView textViewStart = (TextView) listViewItem.findViewById( R.id.start );
        TextView textViewDate = (TextView) listViewItem.findViewById( R.id.date_needed);
        TextView textViewBid = (TextView) listViewItem.findViewById( R.id.latest_bid );

        HistoryModel historyModel= historyList.get( position );

        textViewTaskee.setText( historyModel.getTaskee_name() );
        textViewTitle.setText( historyModel.getTask_name() );
        textViewStart.setText( historyModel.getStart_amount() );
        textViewDate.setText( historyModel.getDate_needed() );
        textViewBid.setText( historyModel.getBid_amount() );

        return listViewItem;

    }

}
