package com.example.niger.nigerangyidong_150271176_co3320.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.niger.nigerangyidong_150271176_co3320.data.Contract.Entry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = DatabaseHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "ingredients,db";
    private static final int DATABASE_VERSION = 1;
    SQLiteDatabase db;
    Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        loadCSV();
        String CREATE_TABLE = "CREATE TABLE" + Entry.TABLE_NAME + "("
                + Entry.COLUMN_TYPE + " TEXT, "
                + Entry.COLUMN_PURPOSE + "TEXT, "
                + Entry.COLUMN_NAME + "TEXT);";

        db.execSQL(CREATE_TABLE);
    }

    public void loadCSV(){
        String csvFileName = "IngredientsDB.csv";
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = null;

        try {
           inputStream = assetManager.open(csvFileName);
        } catch (IOException e){
            Log.e(LOG_TAG, "Failed to open file");
        }

        BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(inputStream));
        String line;
        db.beginTransaction();

        try{
            while((line = bufferedReader.readLine()) != null){
                String [] columns = line.split(",");
                if(columns.length != 4){
                    Log.e(LOG_TAG, "Bad Row");
                    continue;
                }
                ContentValues contentValues = new ContentValues();
                contentValues.put(Entry.COLUMN_TYPE, columns[0].trim());
                contentValues.put(Entry.COLUMN_PURPOSE, columns[1].trim());
                contentValues.put(Entry.COLUMN_NAME, columns[2].trim());
                db.insert(Entry.TABLE_NAME, null, contentValues);
            }
        }catch (IOException e){
            Log.e(LOG_TAG, "Failed to read file");
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
