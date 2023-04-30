package com.example.dbrwmodel.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DatabaseAdapter {
    private DatabaseHelper databaseHelper; //получаем доступ к БД н устройстве
    private SQLiteDatabase database;

    public DatabaseAdapter(Context context) {
        this.databaseHelper = new DatabaseHelper(context.getApplicationContext()); //получаем доступ к БД нашего приложения
    }

    //открываем подключение к БД
    public DatabaseAdapter open(){
        database = databaseHelper.getReadableDatabase();
        return DatabaseAdapter.this;
    }

    public void close(){
        databaseHelper.close();
    }

    private Cursor getProfileEntries(){
            String[] columns = new String[]{DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_SNAME, DatabaseHelper.COLUMN_NAME, DatabaseHelper.COLUMN_AGE, DatabaseHelper.COLUMN_IMAGE};
            return database.query(DatabaseHelper.TABLE_PROFILE, columns, null, null, null, null, null);
    }

    public ArrayList<Profile> profiles(){
        ArrayList<Profile> profiles = new ArrayList<>();
        Cursor cursor = getProfileEntries();
            while (cursor.moveToNext()){
                Profile profile = new Profile(
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SNAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AGE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_IMAGE))
                );
                profiles.add(profile);
            }
        return profiles;
    }

    public Profile getSinleProfile(long id){
        Cursor cursor = database.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_PROFILE + " WHERE " + DatabaseHelper.COLUMN_ID + "=?;", new String[]{String.valueOf(id)});
        cursor.moveToFirst();
        return new Profile(
                cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SNAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME)),
                cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AGE)),
                cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_IMAGE))
        );
    }
    public void insert(Profile profile){
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_SNAME, profile.sName);
        cv.put(DatabaseHelper.COLUMN_NAME, profile.name);
        cv.put(DatabaseHelper.COLUMN_AGE, profile.age);
        cv.put(DatabaseHelper.COLUMN_IMAGE, profile.photo);
        database.insert(DatabaseHelper.TABLE_PROFILE, null, cv);
    }
    public void update(Profile profile){
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_SNAME, profile.sName);
        cv.put(DatabaseHelper.COLUMN_NAME, profile.name);
        cv.put(DatabaseHelper.COLUMN_AGE, profile.age);
        cv.put(DatabaseHelper.COLUMN_IMAGE, profile.photo);
        database.update(DatabaseHelper.TABLE_PROFILE, cv, DatabaseHelper.COLUMN_ID + "=" + profile._id, null);
    }
    public void delete(long id){
        database.delete(DatabaseHelper.TABLE_PROFILE, "_id = ?", new String[]{String.valueOf(id)});
    }
}
