package hk.ust.cse.hunkim.questionroom.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hunkim on 7/15/15.
 */
public class DBUtil {
    SQLiteOpenHelper helper;

    public DBUtil(SQLiteOpenHelper helper) {
        this.helper = helper;
    }

    // add new entry to te database
    public long put(String key) {

        // Gets the data repository in write mode
        SQLiteDatabase db = helper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DBHelper.KEY_NAME, key);
        values.put(DBHelper.LIKE_NAME, false);
        values.put(DBHelper.DISLIKE_NAME, false);

        return db.insert(
                DBHelper.TABLE_NAME,
                DBHelper.KEY_NAME,
                values);
    }


    public boolean contains(String key) {
        // Gets the data repository in write mode
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT 1 FROM " + DBHelper.TABLE_NAME +
                        " WHERE " + DBHelper.KEY_NAME +
                        " = ?", new String[]{key});

        boolean exists = c.moveToFirst();
        c.close();
        return exists;
    }

    public void delete(String key) {
        // Gets the data repository in write mode
        SQLiteDatabase db = helper.getWritableDatabase();

        // Define 'where' part of query.
        String selection = DBHelper.KEY_NAME + " = ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {key};
        // Issue SQL statement.
        db.delete(DBHelper.TABLE_NAME, selection, selectionArgs);
    }

    public void updateLikeStatus(String key, int toChange){
        // Gets the data repository in write mode
        SQLiteDatabase db = helper.getReadableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DBHelper.LIKE_NAME, (toChange==1)?true:false);

        // Issue SQL statement.
        db.update(DBHelper.TABLE_NAME, values, DBHelper.KEY_NAME + "='" + key + "'", null);
    }

    public void updateDislikeStatus(String key, int toChange){
        // Gets the data repository in write mode
        SQLiteDatabase db = helper.getReadableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DBHelper.DISLIKE_NAME, (toChange==1)?true:false);

        // Issue SQL statement.
        db.update(DBHelper.TABLE_NAME, values, DBHelper.KEY_NAME + "='" + key + "'", null);
    }

    public boolean getLikeStatus(String key){
        // Gets the data repository in write mode
        SQLiteDatabase db = helper.getReadableDatabase();
        boolean status = false;
        Cursor c = db.rawQuery(
                "SELECT " + DBHelper.LIKE_NAME + " FROM " + DBHelper.TABLE_NAME +
                        " WHERE " + DBHelper.KEY_NAME +
                        " = '" + key + "'", null);
        if(c.moveToFirst())
            status = c.getInt(c.getColumnIndex(DBHelper.LIKE_NAME))>0;
        c.close();
        return status;
    }

    public boolean getDislikeStatus(String key){
        // Gets the data repository in write mode
        SQLiteDatabase db = helper.getReadableDatabase();
        boolean status = false;
        Cursor c = db.rawQuery(
                "SELECT " + DBHelper.DISLIKE_NAME + " FROM " + DBHelper.TABLE_NAME +
                        " WHERE " + DBHelper.KEY_NAME +
                        " = '" + key + "'", null);
        if(c.moveToFirst())
            status = c.getInt(c.getColumnIndex(DBHelper.DISLIKE_NAME))>0;
        c.close();
        return status;
    }
}
