package com.example.malang.calorietracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Iterator;

public class DBHelper extends SQLiteOpenHelper {

    static String DATABASE_NAME = "CalorieTracker.db";
    static String FOOD_TABLE = "Food";
    static String CALORIE_TRACK_TABLE = "Calorie_Track";
    static String MEAL_RECORD_TABLE = "Meal_Record";
    static int VERSION = 1;


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE "+ FOOD_TABLE + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT UNIQUE," +
                "QUANTITY INTEGER, CALORIE INTEGER, UNIT TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE " + CALORIE_TRACK_TABLE + "(DATE TEXT UNIQUE, CALORIES_TARGET INTEGER DEFAULT 0, CALORIES_CONSUMED INTEGER DEFAULT 0)");
        sqLiteDatabase.execSQL("CREATE TABLE " + MEAL_RECORD_TABLE + "(DATE TEXT, NAME TEXT, MEAL_TYPE TEXT, UNIT TEXT, QUANTITY INTEGER, CALORIES INTEGER)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1){
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ FOOD_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ CALORIE_TRACK_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ MEAL_RECORD_TABLE);

     /*   sqLiteDatabase.execSQL("CREATE TABLE "+ FOOD_TABLE + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT UNIQUE," +
                "QUANTITY INTEGER, CALORIE INTEGER, UNIT TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE " + CALORIE_TRACK_TABLE + "(DATE TEXT UNIQUE, CALORIES_TARGET INTEGER DEFAULT 0, CALORIES_CONSUMED INTEGER DEFAULT 0)");
        sqLiteDatabase.execSQL("CREATE TABLE " + MEAL_RECORD_TABLE + "(DATE TEXT, NAME TEXT, MEAL_TYPE TEXT, UNIT TEXT, QUANTITY INTEGER, CALORIES INTEGER)");
*/
    }

    public boolean insertFood(Food food){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME", food.name);
        contentValues.put("UNIT", food.unit);
        contentValues.put("QUANTITY", food.quantity);
        contentValues.put("CALORIE", food.calorie);

        long result = db.insert(FOOD_TABLE, null, contentValues);
        db.close();

        if(result == -1)
            return false;
        else
            return true;

    }

    public ArrayList<Food> getAllFood(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "+ FOOD_TABLE, null);
        ArrayList<Food> foodArrayList = new ArrayList<>();

        while(cursor.moveToNext()){
            Food food = new Food();
            food.id = Integer.parseInt(cursor.getString(0));
            food.name = cursor.getString(1);
            food.quantity = Integer.parseInt(cursor.getString(2));
            food.calorie = Integer.parseInt(cursor.getString(3));
            food.unit = cursor.getString(4);
            foodArrayList.add(food);
        }

        return foodArrayList;
    }

    int getCalories(String foodName){
        int calories = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "+ FOOD_TABLE+ " where NAME = "+foodName, null);
        if(cursor != null){
            while(cursor.moveToNext()){
                calories = Integer.parseInt(cursor.getString(3));
            }
        }
        return calories;
    }

    public boolean insertCalorieTrack(String date, int calorieTarget, int calorieConsumed){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("DATE", date);

        long result = db.insert(CALORIE_TRACK_TABLE, null, contentValues);
        db.close();

        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean updateCalorieTrackCalorieTarget(String date, int calorieTarget){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("CALORIES_TARGET", calorieTarget);

        long result = db.update(CALORIE_TRACK_TABLE, contentValues, "DATE = ?" , new String[]{date});
        db.close();

        if(result > 0)
            return true;
        else
            return false;
    }



    public boolean insertMealRecord(MealRecord mealRecord){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("DATE", mealRecord.date);
        contentValues.put("NAME", mealRecord.name);
        contentValues.put("MEAL_TYPE", mealRecord.meal_type);
        contentValues.put("UNIT", mealRecord.unit);
        contentValues.put("QUANTITY", mealRecord.quantity);
        contentValues.put("CALORIES", mealRecord.calories);
        long result = db.insert(MEAL_RECORD_TABLE, null, contentValues);
        db.close();

        if(result == -1)
            return false;
        else
            return true;

    }

    public Cursor getAllMealRecord(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "+ MEAL_RECORD_TABLE, null);
        return cursor;
    }

    public Cursor getCalorieTracker(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "+ CALORIE_TRACK_TABLE, null);
        return cursor;
    }



    public boolean updateCalorieTrackCalorieConsumed(String date, int calorieAdd){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(CALORIE_TRACK_TABLE, new String[]{"CALORIES_CONSUMED"}, "DATE = ?", new String[]{date}, null, null, null);
        int prevCalorieConsumed = 0;
        if(cursor != null){
            while(cursor.moveToNext()){
                prevCalorieConsumed = Integer.parseInt(cursor.getString(0));
            }
        }


        int newCalorieConsumed = prevCalorieConsumed + calorieAdd;




        ContentValues contentValues = new ContentValues();
        contentValues.put("CALORIES_CONSUMED", newCalorieConsumed);

        long result = db.update(CALORIE_TRACK_TABLE, contentValues, "DATE = ?" , new String[]{date});
        db.close();

        if(result > 0)
            return true;
        else
            return false;

    }

    public ArrayList<MealRecord> searchFoodMealRecord(String date, String mealType){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(MEAL_RECORD_TABLE, new String[]{"NAME", "UNIT", "QUANTITY", "CALORIES"},
                "DATE = ? AND MEAL_TYPE = ?" ,new String[]{date, mealType}, null, null, null);
        ArrayList<MealRecord> mealRecordArrayList = new ArrayList<>();
        if(cursor != null){
            while(cursor.moveToNext()){
                MealRecord mealRecord = new MealRecord();
                mealRecord.name= cursor.getString(0);
                mealRecord.unit = cursor.getString(1);
                mealRecord.quantity = Integer.parseInt(cursor.getString(2));
                String s = cursor.getString(3);
                mealRecord.calories = Integer.parseInt(cursor.getString(3));
                mealRecordArrayList.add(mealRecord);
            }
        }

        return mealRecordArrayList;
    }




}
