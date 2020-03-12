package com.example.malang.calorietracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.TimeZone;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    TextView caloreigoalTextView;
    TextView usernameTextView;
    TextView actualdateTextView;
    TextView remainingCalorieTextView;
    TextView consumedCalorieTextView;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        caloreigoalTextView = findViewById(R.id.goalcalories_textView);
        actualdateTextView = findViewById(R.id.actualdate_textView);
        remainingCalorieTextView = findViewById(R.id.remainingcalories_textView);
        consumedCalorieTextView = findViewById(R.id.foodcalories_textView);

        final DBHelper database = new DBHelper(HomeActivity.this);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);


        calendar = Calendar.getInstance(TimeZone.getDefault());
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);


        String formatedDate = currentDay+"-"+currentMonth+"-"+currentYear;
        actualdateTextView.setText(currentDay+" "+getMonth(currentMonth)+", "+currentYear);


        try{
            database.insertCalorieTrack(formatedDate, 0, 0);
            boolean success = database.updateCalorieTrackCalorieTarget(formatedDate, Integer.parseInt(sharedPreferences.getString("calories_goal", "0")));
        }
        catch (Exception exception){

        }





        Cursor cursor = database.getCalorieTracker();
        int target = 0;
        int consumed = 0;
        while(cursor.moveToNext()){
            target = Integer.parseInt(cursor.getString(1));
            consumed = Integer.parseInt(cursor.getString(2));
        }

        remainingCalorieTextView.setText((target-consumed)+"");
        consumedCalorieTextView.setText(consumed+"");







        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        View view = navigationView.getHeaderView(0);
        usernameTextView = view.findViewById(R.id.name_editText1);



        caloreigoalTextView.setText(sharedPreferences.getString("calories_goal", "0")+"");
        usernameTextView.setText(sharedPreferences.getString("user_name", "Name"));
       // String s = sharedPreferences.getString("user_name", "Name");
        //Log.v("TAG", s);


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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action

        } else if (id == R.id.nav_diary) {
            startActivity(new Intent(HomeActivity.this, DiaryActivity.class).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));

        } else if (id == R.id.nav_food) {
            startActivity(new Intent(HomeActivity.this, FoodActivity.class).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));

        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    private String getMonth(int month){
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September",
        "October", "November", "December"};
        return monthNames[month-1];
    }






}
