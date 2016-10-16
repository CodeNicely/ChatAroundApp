package com.fame.plumbum.chataround.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pankaj on 23/7/16.
 */
public class DBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "oneMile";
    private static final String TABLE_CHAT = "chat";
    private static final String TABLE_NOTIF = "notif";

    private static final String KEY_ID = "id";
    private static final String STATUS = "status";
    private static final String POST_ID = "post_id";
    private static final String REMOTE_ID = "remote_id";
    private static final String REMOTE_NAME = "remote_name";
    private static final String POSTER_NAME = "poster_name";
    private static final String MESSAGE = "message";
    private static final String TIMESTAMP = "timestamp";
    private static final String NLike = "nlike";
    private static final String NComment = "ncomment";

    public DBHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_CHAT = "CREATE TABLE IF NOT EXISTS " + TABLE_CHAT + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + STATUS + " INTEGER NOT NULL," + POST_ID + " TEXT NOT NULL," + REMOTE_ID + " TEXT NOT NULL," + POSTER_NAME + " TEXT NOT NULL,"
                + REMOTE_NAME + " TEXT NOT NULL, " + MESSAGE + " TEXT," + TIMESTAMP + " TEXT NOT NULL" + ")";
        String CREATE_TABLE_NOTIF = "CREATE TABLE IF NOT EXISTS " + TABLE_NOTIF + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + POST_ID + " TEXT NOT NULL,"
                + NLike + " INTEGER NOT NULL," + NComment + " INTEGER NOT NULL" + ")";
        db.execSQL(CREATE_TABLE_CHAT);
        db.execSQL(CREATE_TABLE_NOTIF);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIF);
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

        db.insert(TABLE_CHAT, null, values);
    }

    public void addNotif(NotifTable table){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(POST_ID, table.getPost_Id());
        values.put(NLike, table.getNLike());
        values.put(NComment, table.getNComment());

        db.insert(TABLE_NOTIF, null, values);
    }

    public List<NotifTable> getNotifs(){
        SQLiteDatabase db = this.getReadableDatabase();
        String getQuery = "SELECT * FROM " + TABLE_NOTIF;
        Cursor cursor = db.rawQuery(getQuery, null);
        List<NotifTable> notifTable = new ArrayList<>();
        if (cursor!= null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                notifTable.add(new NotifTable(cursor.getString(1), cursor.getInt(3), cursor.getInt(2)));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return notifTable;
    }


    public List<ChatTable> getChat(String post_id, String person_id){
        SQLiteDatabase db = this.getReadableDatabase();
        String getQuery = "SELECT * FROM " + TABLE_CHAT + " WHERE " + POST_ID + " = ? AND " + REMOTE_ID + " = ?";
        Cursor cursor = db.rawQuery(getQuery, new String[]{post_id, person_id});
        List<ChatTable> chatTables = new ArrayList<>();
        if (cursor!= null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                chatTables.add(new ChatTable(cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7)));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return chatTables;
    }

    public int getChatCount(String post_id, String person_id){
        String countQuery = "SELECT * FROM " + TABLE_CHAT + " WHERE " + POST_ID + " = ? AND " + REMOTE_ID + " = ?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, new String[]{post_id, person_id});
        cursor.close();
        return cursor.getCount();
    }

    public void deleteChat(ChatTable chat){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CHAT, KEY_ID + "=?", new String[]{String.valueOf(chat.getId()) });
    }

    public void deleteNotif(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("DELETE FROM " + TABLE_NOTIF, null);
        cursor.close();
    }

    public void deleteNotif(String post_id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NOTIF + " WHERE " + POST_ID + "='" + post_id + "'");
    }

    public List<ChatTable> getPeronalChats(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        String getQuery = "SELECT * FROM " + TABLE_CHAT + " WHERE " + POST_ID + " = ?";
        Cursor cursor = db.rawQuery(getQuery, new String[]{name});

        List<ChatTable> chatTables = new ArrayList<>();
        List<String> remote = new ArrayList<>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            int i=0;
            for (; i<remote.size(); i++){
                if (remote.get(i).contentEquals(cursor.getString(3)))
                    break;
            }
            if (i==remote.size()) {
                chatTables.add(new ChatTable(cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7)));
                remote.add(cursor.getString(3));
            }
            cursor.moveToNext();
        }
        cursor.close();
        return chatTables;
    }
}