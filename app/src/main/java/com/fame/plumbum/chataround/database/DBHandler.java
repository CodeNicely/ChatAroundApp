package com.fame.plumbum.chataround.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pankaj on 23/7/16.
 */
public class DBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "oneMile";
    private static final String TABLE_CHAT = "chat";

    private static final String KEY_ID = "id";
    private static final String STATUS = "status";
    private static final String POST_ID = "post_id";
    private static final String REMOTE_ID = "remote_id";
    private static final String REMOTE_NAME = "remote_name";
    private static final String POSTER_NAME = "poster_name";
    private static final String MESSAGE = "message";
    private static final String TIMESTAMP = "timestamp";



    public DBHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_CHAT = "CREATE TABLE IF NOT EXISTS " + TABLE_CHAT + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + STATUS + " INTEGER NOT NULL," + POST_ID + " TEXT NOT NULL," + REMOTE_ID + " TEXT NOT NULL," + POSTER_NAME + " TEXT NOT NULL,"
                + REMOTE_NAME + " TEXT NOT NULL, " + MESSAGE + " TEXT," + TIMESTAMP + " TEXT NOT NULL" + ")";
        db.execSQL(CREATE_TABLE_CHAT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAT);
        onCreate(db);
    }

    public void addChat(ChatTable chatTable){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STATUS, chatTable.getStatus());
        values.put(POST_ID, chatTable.getPost_id());
        values.put(REMOTE_ID, chatTable.getRemote_id());
        values.put(POSTER_NAME, chatTable.getPoster_name());
        values.put(REMOTE_NAME, chatTable.getRemote_name());
        values.put(MESSAGE, chatTable.getMessage());
        values.put(TIMESTAMP, chatTable.getTimestamp());

        long a = db.insert(TABLE_CHAT, null, values);
        Log.e("INSERTED", a+"");
    }

    public List<ChatTable> getChat(String post_id, String person_id){
        SQLiteDatabase db = this.getReadableDatabase();
        String getQuery = "SELECT * FROM " + TABLE_CHAT + " WHERE " + POST_ID + " = ? AND " + REMOTE_ID + " = ?";
        Log.e("getChat", getQuery + " " + post_id + " " + person_id);
        Cursor cursor = db.rawQuery(getQuery, new String[]{post_id, person_id});
//        Cursor cursor = db.query(TABLE_CHAT, new String[]{KEY_ID, POST_ID, POSTER_NAME, REMOTE_ID,
//                REMOTE_NAME, STATUS, MESSAGE, TIMESTAMP}, KEY_ID + "=?",
//                new String[]{post_id}, null, null, null, null);
        List<ChatTable> chatTables = new ArrayList<>();
        if (cursor!= null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Log.e("CURSOR", cursor.getInt(1)+" "+ cursor.getString(2) +" "+ cursor.getString(3) +" "+ cursor.getString(4) +" "+ cursor.getString(5) +" "+ cursor.getString(6) +" "+ cursor.getString(7));
                chatTables.add(new ChatTable(cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7)));
                cursor.moveToNext();
            }
            Log.e("ChatTable Length", chatTables.size() + "");
            cursor.close();
        }else{
            Log.e("TAG_CURSOR", "NULL");
        }
        return chatTables;
    }

    public int getChatCount(String post_id, String person_id){
        String countQuery = "SELECT * FROM " + TABLE_CHAT + " WHERE " + POST_ID + " = ? AND " + REMOTE_ID + " = ?";
        Log.e("getChatCount", countQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, new String[]{post_id, person_id});
        cursor.close();
        Log.e("getVhatCount", cursor.getCount()+"");
        return cursor.getCount();
    }

    public void deleteChat(ChatTable chat){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CHAT, KEY_ID + "=?", new String[]{String.valueOf(chat.getId()) });
    }

    public List<ChatTable> getPeronalChats(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        String getQuery = "SELECT * FROM " + TABLE_CHAT + " WHERE " + POST_ID + " = ?";
        Log.e("getPersonalChats", getQuery);
        Cursor cursor = db.rawQuery(getQuery, new String[]{name});

        List<ChatTable> chatTables = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            chatTables.add(new ChatTable(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6)));
            cursor.moveToNext();
        }
        cursor.close();
        return chatTables;
    }
}