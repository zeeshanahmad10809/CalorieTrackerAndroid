package com.example.malang.calorietracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.TimeZone;

public class DiaryActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    TextView usernameTextView;
    TextView breakfastItemCountTextView;
    TextView lunchItemCountTextView;
    TextView dinnerItemCountTextView;
    Button addBreakfastButton;
    Button addLunchButton;
    Button addDinnerButton;
    LinearLayout breakfastLinearLayout;
    LinearLayout lunchLinearLayout;
    LinearLayout dinnerLinearLayout;
    DBHelper database;

    static int BREAKFAST_CODE = 101;
    static int LUNCH_CODE = 102;
    static int DINNER_CODE = 103;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        breakfastItemCountTextView = findViewById(R.id.breakfastitemcount_textView);
        lunchItemCountTextView = findViewById(R.id.luncitemcount_textView);
        dinnerItemCountTextView = findViewById(R.id.dinneritemcount_textView);
        addBreakfastButton = findViewById(R.id.addbreadkfast_Button);
        addLunchButton = findViewById(R.id.addlunch_Button);
        addDinnerButton = findViewById(R.id.adddinner_Button);
        breakfastLinearLayout = findViewById(R.id.breakfast_linearlayout);
        lunchLinearLayout = findViewById(R.id.lunch_linearlayout);
        dinnerLinearLayout = findViewById(R.id.dinner_linearlayout);

        addBreakfastButton.setOnClickListener(this);
        addLunchButton.setOnClickListener(this);
        addDinnerButton.setOnClickListener(this);








        database = new DBHelper(DiaryActivity.this);
        loadMeals(database);





        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_diary);


        View view = navigationView.getHeaderView(0);
        usernameTextView = view.findViewById(R.id.diaryusername_editText);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(DiaryActivity.this);
        usernameTextView.setText(sharedPreferences.getString("user_name", "Name"));



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
            startActivity(new Intent(DiaryActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));

        } else if (id == R.id.nav_diary) {

        } else if (id == R.id.nav_food) {
            startActivity(new Intent(DiaryActivity.this, FoodActivity.class).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));

        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(DiaryActivity.this, SettingsActivity.class).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.addbreadkfast_Button){
            startActivityForResult(new Intent(DiaryActivity.this, FoodScrollActivity.class)
                    .putExtra("CODE", BREAKFAST_CODE), BREAKFAST_CODE);
        }
        else if(view.getId() == R.id.addlunch_Button){
            startActivityForResult(new Intent(DiaryActivity.this, FoodScrollActivity.class)
                    .putExtra("CODE", LUNCH_CODE), LUNCH_CODE);
        }
        else if(view.getId() == R.id.adddinner_Button){
            startActivityForResult(new Intent(DiaryActivity.this, FoodScrollActivity.class)
                    .putExtra("CODE", DINNER_CODE), DINNER_CODE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        breakfastLinearLayout.removeAllViews();
        lunchLinearLayout.removeAllViews();
        dinnerLinearLayout.removeAllViews();

        if(resultCode == RESULT_OK){

            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
            int currentYear = calendar.get(Calendar.YEAR);
            int currentMonth = calendar.get(Calendar.MONTH) + 1;
            int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
            String formatedDate = currentDay+"-"+currentMonth+"-"+currentYear;

            int code = data.getIntExtra("CODE", 0);
            if(code == BREAKFAST_CODE){

                ArrayList<MealRecord> mealRecordArrayList = database.searchFoodMealRecord(formatedDate, "Breakfast");
                Iterator<MealRecord> mealRecordIterator = mealRecordArrayList.iterator();
                while(mealRecordIterator.hasNext()){
                    MealRecord mealRecord = mealRecordIterator.next();
                    View row = View.inflate(DiaryActivity.this, R.layout.added_food_row, null);
                    TextView foodNameTextView = (TextView) row.findViewById(R.id.rowfoodname_textView);
                    TextView foodCaloriesTextView = (TextView) row.findViewById(R.id.rowfoodcalories_textView);
                    TextView foodQuantityTextView = (TextView) row.findViewById(R.id.rowfoodquantity_textView);

                    foodNameTextView.setText(mealRecord.name);
                    foodCaloriesTextView.setText(mealRecord.calories+"");
                    foodQuantityTextView.setText(mealRecord.quantity+" "+mealRecord.unit);
                    breakfastLinearLayout.addView(row);


                }
                breakfastItemCountTextView.setText(mealRecordArrayList.size()+"");

            }
            else if(code == LUNCH_CODE){

                ArrayList<MealRecord> mealRecordArrayList = database.searchFoodMealRecord(formatedDate, "Lunch");
                Iterator<MealRecord> mealRecordIterator = mealRecordArrayList.iterator();
                while(mealRecordIterator.hasNext()){
                    MealRecord mealRecord = mealRecordIterator.next();
                    View row = View.inflate(DiaryActivity.this, R.layout.added_food_row, null);
                    TextView foodNameTextView = (TextView) row.findViewById(R.id.rowfoodname_textView);
                    TextView foodCaloriesTextView = (TextView) row.findViewById(R.id.rowfoodcalories_textView);
                    TextView foodQuantityTextView = (TextView) row.findViewById(R.id.rowfoodquantity_textView);

                    foodNameTextView.setText(mealRecord.name);
                    foodCaloriesTextView.setText(mealRecord.calories+"");
                    foodQuantityTextView.setText(mealRecord.quantity+" "+mealRecord.unit);
                    lunchLinearLayout.addView(row);


                }
                lunchItemCountTextView.setText(mealRecordArrayList.size()+"");



            }
            else if(code == DINNER_CODE){


                ArrayList<MealRecord> mealRecordArrayList = database.searchFoodMealRecord(formatedDate, "Dinner");
                Iterator<MealRecord> mealRecordIterator = mealRecordArrayList.iterator();
                while(mealRecordIterator.hasNext()){
                    MealRecord mealRecord = mealRecordIterator.next();
                    View row = View.inflate(DiaryActivity.this, R.layout.added_food_row, null);
                    TextView foodNameTextView = (TextView) row.findViewById(R.id.rowfoodname_textView);
                    TextView foodCaloriesTextView = (TextView) row.findViewById(R.id.rowfoodcalories_textView);
                    TextView foodQuantityTextView = (TextView) row.findViewById(R.id.rowfoodquantity_textView);

                    foodNameTextView.setText(mealRecord.name);
                    foodCaloriesTextView.setText(mealRecord.calories+"");
                    foodQuantityTextView.setText(mealRecord.quantity+" "+mealRecord.unit);
                    dinnerLinearLayout.addView(row);


                }
                dinnerItemCountTextView.setText(mealRecordArrayList.size()+"");


            }

        }



    }

    private void loadMeals(DBHelper database){
        breakfastLinearLayout.removeAllViews();
        lunchLinearLayout.removeAllViews();
        dinnerLinearLayout.removeAllViews();

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        String formatedDate = currentDay+"-"+currentMonth+"-"+currentYear;

        ArrayList<MealRecord> mealRecordArrayList = database.searchFoodMealRecord(formatedDate, "Breakfast");
        Iterator<MealRecord> mealRecordIterator = mealRecordArrayList.iterator();
        while(mealRecordIterator.hasNext()){
            MealRecord mealRecord = mealRecordIterator.next();
            View row = View.inflate(DiaryActivity.this, R.layout.added_food_row, null);
            TextView foodNameTextView = (TextView) row.findViewById(R.id.rowfoodname_textView);
            TextView foodCaloriesTextView = (TextView) row.findViewById(R.id.rowfoodcalories_textView);
            TextView foodQuantityTextView = (TextView) row.findViewById(R.id.rowfoodquantity_textView);

            foodNameTextView.setText(mealRecord.name);
            foodCaloriesTextView.setText(mealRecord.calories+"");
            foodQuantityTextView.setText(mealRecord.quantity+" "+mealRecord.unit);
            breakfastLinearLayout.addView(row);



        }
        breakfastItemCountTextView.setText(mealRecordArrayList.size()+"");




        mealRecordArrayList = database.searchFoodMealRecord(formatedDate, "Lunch");
        mealRecordIterator = mealRecordArrayList.iterator();
        while(mealRecordIterator.hasNext()){
            MealRecord mealRecord = mealRecordIterator.next();
            View row = View.inflate(DiaryActivity.this, R.layout.added_food_row, null);
            TextView foodNameTextView = (TextView) row.findViewById(R.id.rowfoodname_textView);
            TextView foodCaloriesTextView = (TextView) row.findViewById(R.id.rowfoodcalories_textView);
            TextView foodQuantityTextView = (TextView) row.findViewById(R.id.rowfoodquantity_textView);

            foodNameTextView.setText(mealRecord.name);
            foodCaloriesTextView.setText(mealRecord.calories+"");
            foodQuantityTextView.setText(mealRecord.quantity+" "+mealRecord.unit);
            lunchLinearLayout.addView(row);


        }
        lunchItemCountTextView.setText(mealRecordArrayList.size()+"");





        mealRecordArrayList = database.searchFoodMealRecord(formatedDate, "Dinner");
        mealRecordIterator = mealRecordArrayList.iterator();
        while(mealRecordIterator.hasNext()){
            MealRecord mealRecord = mealRecordIterator.next();
            View row = View.inflate(DiaryActivity.this, R.layout.added_food_row, null);
            TextView foodNameTextView = (TextView) row.findViewById(R.id.rowfoodname_textView);
            TextView foodCaloriesTextView = (TextView) row.findViewById(R.id.rowfoodcalories_textView);
            TextView foodQuantityTextView = (TextView) row.findViewById(R.id.rowfoodquantity_textView);

            foodNameTextView.setText(mealRecord.name);
            foodCaloriesTextView.setText(mealRecord.calories+"");
            foodQuantityTextView.setText(mealRecord.quantity+" "+mealRecord.unit);
            dinnerLinearLayout.addView(row);


        }
        dinnerItemCountTextView.setText(mealRecordArrayList.size()+"");




    }



}
