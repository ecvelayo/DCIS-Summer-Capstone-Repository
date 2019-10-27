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


public class TaskView extends ArrayAdapter<TaskInserter> {

    private Activity context;
    private List<TaskInserter> taskList;
    public static final String TASK_STATE = "task_state";

    DatabaseReference databaseTask;

    public TaskView(Activity context, List<TaskInserter> taskList){
        super(context, R.layout.task_layout, taskList);
        this.context = context;
        this.taskList = taskList;
    }




    @Override
public View getView(int position, View convertView,ViewGroup parent) {
final LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate( R.layout.task_layout,null,true );
        databaseTask = FirebaseDatabase.getInstance().getReference("task");

        TextView textViewTitle = (TextView) listViewItem.findViewById( R.id.title );
        TextView textViewTaskee = (TextView) listViewItem.findViewById( R.id.taskee );
        TextView textViewStart = (TextView) listViewItem.findViewById( R.id.start );
        TextView textViewDate = (TextView) listViewItem.findViewById( R.id.date_needed);
        TextView textViewState = (TextView) listViewItem.findViewById( R.id.state_id );
        TextView textViewBid = (TextView) listViewItem.findViewById( R.id.latest_bid );

        TaskInserter taskInserter = taskList.get( position );

        textViewTaskee.setText( taskInserter.getTaskee_name() );
        textViewTitle.setText( taskInserter.getTask_name() );
        textViewStart.setText( taskInserter.getStarting_amount() );
        textViewDate.setText( taskInserter.getDate_needed() );
        textViewState.setText( taskInserter.getTask_state() );
        textViewBid.setText( taskInserter.getFinal_amount() );

        return listViewItem;

        }
}
