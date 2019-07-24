package com.example.ainurbayanova.kolesa.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ainurbayanova.kolesa.mvp.modules.AlarmModule;

public class Alarm extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "alarm.db";
    public static final String TABLE_ALARM = "alarm_store";
    public static final String COL_ID = "ID";
    public static final String COL_USERNAME = "USERNAME";
    public static final String COL_MONTH = "MONTH";
    public static final String COL_DAY = "DAY";
    public static final String COL_HOUR = "HOUR";
    public static final String COL_MINUTE = "MINUTE";
    public static final String COL_CHALLENGE = "CHALLENGE";

    public Alarm(Context context) {
        super(context, TABLE_ALARM, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_ALARM + "(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "USERNAME TEXT," +
                "MONTH INTEGER," +
                "DAY INTEGER, " +
                "HOUR INTEGER," +
                "MINUTE INTEGER ," +
                "CHALLENGE TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALARM);
        onCreate(db);
    }

    public boolean addAlarm(AlarmModule alarm){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_USERNAME ,alarm.getUsername());
        contentValues.put(COL_MONTH ,alarm.getMonth());
        contentValues.put(COL_DAY ,alarm.getDay());
        contentValues.put(COL_HOUR ,alarm.getHour());
        contentValues.put(COL_MINUTE ,alarm.getMinute());
        contentValues.put(COL_CHALLENGE ,alarm.getfKeyOfChallenge());

        long result = db.insert(TABLE_ALARM,null,contentValues);
        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_ALARM;
        Cursor data = db.rawQuery(query,null);
        return data;
    }

    public int deleteData(String fFirebaseKey,String user){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_ALARM,"CHALLENGE = ? AND USERNAME = ?",new String[]{fFirebaseKey,user});
    }
}
