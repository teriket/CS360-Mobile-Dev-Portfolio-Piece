package com.hunt.cs360finalproject.datamanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.Nullable;

public class WeightDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "weights.db";
    private static final int VERSION = 2;
    private static final class UsersTable {
        private static final String TABLE = "users";
        private static final String COL_ID = "_id";
        private static final String COL_EMAIL = "email";
        private static final String COL_PASSWORD = "password";
    }

    private static final class WeightsTable{
        private static final String TABLE = "weights";
        private static final String COL_ID = "_id";
        private static final String COL_USER = "user";
        private static final String COL_DATE = "date";
        private static final String COL_WEIGHT = "weight";
        private static final String COL_IS_GOAL = "isGoal";
    }
    public WeightDatabase(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    public WeightDatabase(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("PRAGMA foreign_keys=ON");

        sqLiteDatabase.execSQL(
                "create table " + UsersTable.TABLE + " (" +
                UsersTable.COL_ID + " integer primary key autoincrement, " +
                UsersTable.COL_EMAIL + " text, " +
                UsersTable.COL_PASSWORD + " text)"
        );

        sqLiteDatabase.execSQL(
                "create table " + WeightsTable.TABLE + " (" +
                WeightsTable.COL_ID + " integer primary key autoincrement, " +
                WeightsTable.COL_DATE + " date, " +
                WeightsTable.COL_WEIGHT + " integer, " +
                WeightsTable.COL_USER + " integer, " +
                "foreign key (" + WeightsTable.COL_USER + ")" +
                "references " + UsersTable.TABLE + " (" + UsersTable.COL_ID + "))"
        );

        Log.d("tag", "database created");


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("alter table " + WeightsTable.TABLE
                + " add column " + WeightsTable.COL_IS_GOAL + " integer default 0");
    }

    public long addUser(String email, String password){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UsersTable.COL_EMAIL, email);
        values.put(UsersTable.COL_PASSWORD, password);

        return db.insert(UsersTable.TABLE, null, values);
    }

    public long getUserID(String email, String password){
        SQLiteDatabase db = getReadableDatabase();
        String sql = "select * from " + UsersTable.TABLE + " where email = ? and password = ?";
        long id = -1;

        Cursor cursor = db.rawQuery(sql, new String[] {email, password});
        if(cursor.moveToFirst()){
            id = cursor.getLong(0);
        }

        cursor.close();
        return id;
    }

    public boolean usernameExists(String username){
        SQLiteDatabase db = getReadableDatabase();
        String sql = "select * from " + UsersTable.TABLE + " where email = ?";
        Cursor cursor = db.rawQuery(sql, new String[] {username});
        return cursor.moveToFirst();
    }

    public long addWeight(long userID, long weight, boolean isGoalWeight){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(WeightsTable.COL_USER, userID);
        values.put(WeightsTable.COL_WEIGHT, weight);
        values.put(WeightsTable.COL_DATE, todayAsString());
        values.put(WeightsTable.COL_IS_GOAL, isGoalWeight);

        return db.insert(WeightsTable.TABLE, null, values);
    }

    public int getNumUserWeights(int userID){
        int numberWeights = 0;
        Cursor cursor = getCursorForWeights(userID);

        if(cursor != null && cursor.moveToFirst()){
            do {
                numberWeights += 1;
            }
            while(cursor.moveToNext());
            }

        return numberWeights;
    }

    public String getWeightDate(int userID, int index){
        Cursor cursor = getCursorForWeights(userID);
        if(cursor != null){
            cursor.moveToPosition(index);
            return cursor.getString(1);
        }
        return "";
    }

    public int getWeight(int userID, int index){
        Cursor cursor = getCursorForWeights(userID);
        if(cursor != null){
            cursor.moveToPosition(index);
            int val = cursor.getInt(2);
            cursor.close();
            return val;
        }
        return 0;
    }

    public int getLatestGoalWeight(int userID){
        SQLiteDatabase db = getReadableDatabase();
        String sql = "select * from " + WeightsTable.TABLE +
                " where " + WeightsTable.COL_USER + " = ?" +
                " and " + WeightsTable.COL_IS_GOAL + " = 1";
        Cursor cursor = db.rawQuery(sql, new String[] {"" + userID});

        if(cursor.moveToFirst()){
            cursor.moveToLast();
            return cursor.getInt(2);
        }
        return 0;


    }

    public Cursor getCursorForWeights(int userID){
        SQLiteDatabase db = getReadableDatabase();
        String sql = "select * from " + WeightsTable.TABLE +
                " where " + WeightsTable.COL_USER + " = ? " +
                "and " + WeightsTable.COL_IS_GOAL + " = 0";
        Cursor cursor = db.rawQuery(sql, new String[] {"" + userID});
        if(cursor.moveToFirst()){
            return cursor;
        }
        return null;
    }

    private String todayAsString(){
        String pattern = "MM/dd/yy";
        DateFormat dateFormat = new SimpleDateFormat(pattern);

        Date today = Calendar.getInstance().getTime();
        return dateFormat.format(today);
    }

}
