package com.example.mynotekeeper;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NoteAdapter mNoteAdapter1;
    private AppBarConfiguration mAppBarConfiguration;
    private LinearLayoutManager mNotesLayoutManager;
    private RecyclerView mRecyclerView;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawer;
    private GridLayoutManager mGridLayoutManager;
    private CourseAdapter mCourseAdapter;
    private MyNoteOpenHelper mMyNoteOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(MainActivity.this, NoteActivity.class));
            }
        });
        mDrawer = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);

        mMyNoteOpenHelper = new MyNoteOpenHelper(this);


        mNavigationView.setNavigationItemSelectedListener(this);
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
//                .setDrawerLayout(drawer)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);

        initializeListContent();
    }


    @Override
    protected void onDestroy() {
        mMyNoteOpenHelper.close();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
       mNoteAdapter1.notifyDataSetChanged();
    }

    private void initializeListContent() {

        DataManager.loadFromDatabase(mMyNoteOpenHelper);

        mRecyclerView = findViewById(R.id.list_items);
        mNotesLayoutManager = new LinearLayoutManager(this);
        mGridLayoutManager = new GridLayoutManager(this, 2);

        List<NoteInfo> noteInfo = DataManager.getInstance().getNotes();
        mNoteAdapter1 = new NoteAdapter(this, noteInfo);

        List<CourseInfo> courseInfo = DataManager.getInstance().getCourses();
        mCourseAdapter = new CourseAdapter(this, courseInfo);

        displayNotes();
    }

    private void displayNotes() {
        mRecyclerView.setAdapter(mNoteAdapter1);
        mRecyclerView.setLayoutManager(mNotesLayoutManager);


        selectMenu(R.id.nav_note);

    }

    public void displayCourse(){
        mRecyclerView.setAdapter(mCourseAdapter);
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        selectMenu(R.id.nav_courses);
    }

    private void selectMenu(int id) {
        Menu menu = mNavigationView.getMenu();
        menu.findItem(id).setChecked(true);
    }

    @Override
    public void onBackPressed() {
        if(mDrawer.isDrawerOpen(GravityCompat.START)){
            mDrawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings){
            startActivity(new Intent(this, SettingsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
       int id = item.getItemId();
        if(id == R.id.nav_note){
           displayNotes();
        }
        if(id == R.id.nav_courses){
            displayCourse();
        }
        if(id == R.id.nav_share){
            handleSelection("share");
        }
        return false;
    }

    private void handleSelection(String message) {
        View view = findViewById(R.id.list_items);
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }
}