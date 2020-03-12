package com.example.malang.calorietracker;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.TimeZone;

public class FoodScrollActivity extends AppCompatActivity {

    SearchView foodSearchView;
    TextView selectedFoodTextView;
    ListView foodListView;
    ArrayAdapter<String> foodAdapter;
    DBHelper database;
    String selectedFood;
    EditText selectedFoodQuantityEditText;
    Button selectedFoodAddButton;
    int requestCode;
    //boolean ischanged = true;

    static int BREAKFAST_CODE = 101;
    static int LUNCH_CODE = 102;
    static int DINNER_CODE = 103;

    String[] mealType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_scroll);
        requestCode = getIntent().getIntExtra("CODE", 0);
        mealType = new String[]{"Breakfast", "Lunch", "Dinner"};
        selectedFood = "";

        foodSearchView = findViewById(R.id.foodsearch_searchView);
        selectedFoodTextView = findViewById(R.id.selectedfooname_textview);
        foodListView = findViewById(R.id.selectfood_listView);
        selectedFoodQuantityEditText = findViewById(R.id.selectedquantity_textView);
        selectedFoodAddButton = findViewById(R.id.addselectedfood_Button);






        database = new DBHelper(FoodScrollActivity.this);
        final ArrayList<Food> foodArrayList = database.getAllFood();
        final ArrayList<String> foodNameArrayList = new ArrayList<>();
        Iterator<Food> foodIterator = foodArrayList.iterator();
        while(foodIterator.hasNext()){
            foodNameArrayList.add(foodIterator.next().name);
        }
        foodAdapter = new ArrayAdapter<>(FoodScrollActivity.this, android.R.layout.simple_list_item_1, foodNameArrayList);
        foodListView.setAdapter(foodAdapter);


        foodListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(FoodScrollActivity.this, foodAdapter.getItem(i)+"", Toast.LENGTH_SHORT).show();
                selectedFood = foodAdapter.getItem(i);
                selectedFoodTextView.setText(selectedFood);

            }
        });





        selectedFoodAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(selectedFoodQuantityEditText.getText().toString().isEmpty() || selectedFood.isEmpty()){
                    Toast.makeText(FoodScrollActivity.this, "Fill out fields completely!", Toast.LENGTH_SHORT).show();
                    //ischanged = false;
                    return;
                }


                Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                int currentYear = calendar.get(Calendar.YEAR);
                int currentMonth = calendar.get(Calendar.MONTH) + 1;
                int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
                String formatedDate = currentDay+"-"+currentMonth+"-"+currentYear;


                int index = foodNameArrayList.indexOf(selectedFood);
                float quantity = Float.parseFloat(selectedFoodQuantityEditText.getText().toString());
                Food food = foodArrayList.get(index);
                float caloriesPerGram = ((float) food.calorie/(float) food.quantity)*quantity;
                int calories = (int) caloriesPerGram;
                MealRecord mealRecord = new MealRecord();




                mealRecord.date = formatedDate;
                mealRecord.name = food.name;
                mealRecord.unit = food.unit;
                if(requestCode == BREAKFAST_CODE){
                    mealRecord.meal_type = mealType[0];
                }
                else if(requestCode == LUNCH_CODE){
                    mealRecord.meal_type = mealType[1];
                }
                else if(requestCode == DINNER_CODE){
                    mealRecord.meal_type = mealType[2];
                }
                mealRecord.quantity = (int)quantity;
                mealRecord.calories = calories;




                boolean status =  database.insertMealRecord(mealRecord);
                boolean status1 = database.updateCalorieTrackCalorieConsumed(formatedDate, mealRecord.calories);

                if(status && status1){
                   Toast.makeText(FoodScrollActivity.this, "Meal Added!", Toast.LENGTH_SHORT).show();
               }
               else{
                   Toast.makeText(FoodScrollActivity.this, "Meal not Added!", Toast.LENGTH_SHORT).show();
               }



                //Log.d("", "onClick: ");

            }
        });


















        foodSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                foodAdapter.getFilter().filter(s);
                return false;
            }
        });


    }


    @Override
    public void onBackPressed() {
        foodSearchView.clearFocus();

        Intent returnIntent = new Intent();
        returnIntent.putExtra("CODE", requestCode);
        //returnIntent.putExtra("ischanged", ischanged);
        setResult(RESULT_OK, returnIntent);

        super.onBackPressed();



    }
}
