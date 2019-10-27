package com.example.sugoapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SidebarActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    public static final String TASK_TYPE = "task_type";
    public static final String TASK_DESC = "task_name";
    public static final String TASKEE_NAME = "taskee_name";
    public static final String STARTING_AMOUNT = "starting_amount";
    public static final String DATE_NEEDED = "date_needed";
    public static final String TASK_STATE = "task_state";
    public static final String TASK_ID = "task_id";
    public static final String USER_ID = "user_id";

    private Button main_btn;
    List<TaskInserter> insertList;
    private DatabaseReference databaseTask, databaseUser;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    ListView listViewTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sidebar);
        firebaseAuth = FirebaseAuth.getInstance();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        NavigationView navigationView = (NavigationView) findViewById( R.id.nav_view );
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.welcomeUser_id);
        navUsername.setText(firebaseAuth.getCurrentUser().getDisplayName());
        setSupportActionBar(toolbar);
        progressDialog = new ProgressDialog(this);

        databaseTask = FirebaseDatabase.getInstance().getReference("task");

        ArrayList<String> lista = new ArrayList<String>();
        Intent intent = new Intent( this, Tasks.class );
        intent.putStringArrayListExtra( "key", lista);

        main_btn = findViewById( R.id.main_btn );


        listViewTask = (ListView) findViewById( R.id.taskList );
        insertList = new ArrayList<>();


        main_btn.setOnClickListener( this );
        SidebarActivity.super.onResume();
        insertList.clear();



        listViewTask.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent( getApplicationContext(), BiddingActivity.class );
                TaskInserter taskInserter = insertList.get( position );
                SidebarActivity.super.onResume();
                insertList.clear();


                intent.putExtra( TASK_TYPE , taskInserter.getTask_type());
                intent.putExtra( TASK_DESC , taskInserter.getTask_name());
                intent.putExtra( TASKEE_NAME , taskInserter.getTaskee_name());
                intent.putExtra( STARTING_AMOUNT , taskInserter.getStarting_amount());
                intent.putExtra( DATE_NEEDED , taskInserter.getDate_needed());
                intent.putExtra( TASK_STATE , taskInserter.getTask_state());
                intent.putExtra( TASK_ID, taskInserter.getTask_id() );
                intent.putExtra( USER_ID, taskInserter.getUser_id() );
                startActivity( intent );

            }
        } );




       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Message module coming soon", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sidebar, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(this, MainActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SidebarActivity.super.onResume();
        insertList.clear();
        databaseTask.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                for (DataSnapshot taskSnapshot: dataSnapshot.getChildren()) {
                    TaskInserter taskInserter = taskSnapshot.getValue( TaskInserter.class );
                    if (taskSnapshot.child( "task_state" ).getValue().toString().equals( "Pending") ) {
                        insertList.add( taskInserter );
                    }
                }
                TaskView taskAdapter = new TaskView( SidebarActivity.this, insertList );
                listViewTask.setAdapter( taskAdapter );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        } );
    }

    @Override
    public void onClick(View v) {
        if (v == main_btn){
            startActivity( new Intent( this, Post.class ) );
        }


    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        displaySelectedScreen(item.getItemId());
        return true;
    }
    private void displaySelectedScreen(int itemId) {
        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {

            case R.id.nav_task:
                findViewById( R.id.hide_layout ).setVisibility( View.GONE );
                startActivity( new Intent( this, SidebarActivity.class ) );
                break;

            case R.id.nav_profile:
                findViewById( R.id.hide_layout ).setVisibility( View.GONE );
                fragment = new ProfileUpdate();
                break;
            case R.id.nav_bids:
                findViewById( R.id.hide_layout ).setVisibility( View.GONE );
                fragment = new Bids();
                break;
            case R.id.nav_history:
                findViewById( R.id.hide_layout ).setVisibility( View.GONE );
                fragment = new History();

                break;
            case R.id.nav_load_credits:
                findViewById( R.id.hide_layout ).setVisibility( View.GONE );
                startActivity( new Intent( this, LoadCredits.class ) );
                break;


        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
}
