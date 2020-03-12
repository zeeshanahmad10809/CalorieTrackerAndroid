package com.example.malang.calorietracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class FoodActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    EditText foodnameEditText;
    Spinner foodunitSpinner;
    EditText foodquantityEditText;
    EditText caloriesEditText;
    Button addButton;
    TextView usernameTextView;
    String foodUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        foodnameEditText = findViewById(R.id.foodname_editText);
        foodunitSpinner = findViewById(R.id.foodunit_Spinner);
        foodquantityEditText = findViewById(R.id.foodquantity_editText);
        caloriesEditText = findViewById(R.id.calories_editText);
        addButton = findViewById(R.id.addfood_Button);

        final DBHelper database = new DBHelper(FoodActivity.this);


        final String[] foodunitArray = new String[]{"milli litre", "grams"};




        foodunitSpinner.setAdapter(new ArrayAdapter<String>(FoodActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                foodunitArray));



        foodunitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                foodUnit = foodunitArray[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(FoodActivity.this, foodUnit, Toast.LENGTH_SHORT).show();
                //Toast.makeText(FoodActivity.this, foodUnit, Toast.LENGTH_SHORT).show();

                Food food = new Food();


                if(!validateFoodForm()){
                    Toast.makeText(FoodActivity.this, "Fill all fields!", Toast.LENGTH_SHORT).show();
                }
                else {


                    food.name = foodnameEditText.getText().toString();
                    food.unit = foodUnit;
                    food.quantity = Integer.parseInt(foodquantityEditText.getText().toString());
                    food.calorie = Integer.parseInt(caloriesEditText.getText().toString());

                    if(!validateFood(food)){
                        Toast.makeText(FoodActivity.this, "Enter valid food name!", Toast.LENGTH_SHORT).show();
                    }
                    else{


                        boolean success = database.insertFood(food);

                        if(success){
                            Toast.makeText(FoodActivity.this, "Food Added!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(FoodActivity.this, "Food already present!", Toast.LENGTH_SHORT).show();

                        }



                    }

                }




                Log.d("TAG", "onClick: Stop!");


            }
        });







        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_food);

        View view = navigationView.getHeaderView(0);
        usernameTextView = view.findViewById(R.id.foodusername_editText);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(FoodActivity.this);
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
            startActivity(new Intent(FoodActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));


        } else if (id == R.id.nav_diary) {

            startActivity(new Intent(FoodActivity.this, DiaryActivity.class).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));

        } else if (id == R.id.nav_food) {

        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(FoodActivity.this, SettingsActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private boolean validateFood(Food food){
        Pattern foodNamePattern = Pattern.compile("[a-zA-Z]+");
        return foodNamePattern.matcher(food.name).matches();
    }

    private boolean validateFoodForm(){
        if(!foodnameEditText.getText().toString().isEmpty() && !foodquantityEditText.getText().toString().isEmpty() && !caloriesEditText.getText().toString().isEmpty())
            return true;
        else return false;
    }

}
