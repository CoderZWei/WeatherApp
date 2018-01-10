package com.example.zw.weatherapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zw on 2016/9/24.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_Citys="create table city_selected("
            +"id integer primary key autoincrement,"
            +"cityName text unique"
            +")";
    private Context mContext;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    db.execSQL(CREATE_Citys);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
