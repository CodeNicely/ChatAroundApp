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
    private static final String TABLE_COMMENT = "comment";
    private static final String TABLE_POST = "post";
    private static final String TABLE_USER = "user";

    private static final String KEY_ID = "id";
    private static final String POST_ID = "post_id";
    private static final String POSTER_NAME = "poster_name";
    private static final String REMOTE_ID = "remote_id";
    private static final String STATUS = "s";
    private static final String MESSAGE = "id";
    private static final String TIMESTAMP = "id";
    private static final String POSTER_ID = "id";
    private static final String NO_LIKES = "id";
    private static final String COMMENT_IDS = "id";
    private static final String LATITUDE = "id";
    private static final String LONGITUDE = "id";
    private static final String COMMENTOR_ID = "id";
    private static final String USER_ID = "user_id";
    private static final String USER_NAME = "user_name";
    private static final String USER_IMAGE = "image";
    private static final String PHONE = "phone";


    public DBHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_CHAT = "CREATE TABLE IF NOT EXISTS " + TABLE_CHAT + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + POST_ID + " TEXT NOT NULL"
                + COMMENTOR_ID + " TEXT NOT NULL" + MESSAGE + " TEXT" +")";
        String CREATE_TABLE_POST = "CREATE TABLE IF NOT EXISTS " + TABLE_POST + "("
                + POST_ID + " TEXT NOT NULL," + POSTER_ID+ " TEXT NOT NULL," + POSTER_NAME + " TEXT NOT NULL,"
                + NO_LIKES + " TEXT NOT NULL," + MESSAGE + " TEXT," + COMMENT_IDS + " TEXT," + TIMESTAMP + " TEXT,"
                + LATITUDE + " TEXT," + LONGITUDE + " TEXT," + ")";
        String CREATE_TABLE_COMMENT = "CREATE TABLE IF NOT EXISTS " + TABLE_COMMENT + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COMMENTOR_ID + " TEXT NOT NULL,"
                + POST_ID + " TEXT," + MESSAGE + " TEXT," + TIMESTAMP + "TEXT NOT NULL" + ")";
        String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + USER_ID + " TEXT NOT NULL"
                + USER_NAME + " TEXT NOT NULL" + PHONE + " TEXT" + USER_IMAGE + " TEXT" +")";
        db.execSQL(CREATE_TABLE_CHAT);
        db.execSQL(CREATE_TABLE_POST);
        db.execSQL(CREATE_TABLE_COMMENT);
        db.execSQL(CREATE_TABLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }

    public void addComment(CommentTable commentTable){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COMMENTOR_ID, commentTable.getCommentor_id());
        values.put(MESSAGE, commentTable.getComment());
        values.put(POST_ID, commentTable.getPost_id());
        values.put(TIMESTAMP, commentTable.getTimestamp());

        db.insert(TABLE_COMMENT, null, values);
        db.close();
    }

    public void addChat(ChatTable chatTable){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(REMOTE_ID, chatTable.getRemote_id());
        values.put(STATUS, chatTable.getStatus());
        values.put(MESSAGE, chatTable.getComment());
        values.put(TIMESTAMP, chatTable.getTimestamp());

        db.insert(TABLE_CHAT, null, values);
        db.close();
    }

    public void addPost(PostTable postTable){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(POST_ID, postTable.getId());
        values.put(POSTER_ID, postTable.getPoster_id());
        values.put(POSTER_NAME, postTable.getPoster_name());
        values.put(MESSAGE, postTable.getMessage());
        values.put(NO_LIKES, postTable.getNo_likes());
        values.put(COMMENT_IDS, postTable.getComment_ids());
        values.put(LATITUDE, postTable.getLat());
        values.put(LONGITUDE, postTable.getLon());
        values.put(TIMESTAMP, postTable.getTimestamp());

        db.insert(TABLE_CHAT, null, values);
        db.close();
    }

    public void addUser(UserTable userTable){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_ID, userTable.getUser_id());
        values.put(USER_IMAGE, userTable.getImage());
        values.put(PHONE, userTable.getPhone());
        values.put(USER_NAME, userTable.getUser_name());

        db.insert(TABLE_USER, null, values);
        db.close();
    }

    public PostTable getPost(String POST_ID){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_POST, new String[]{ this.POST_ID, POSTER_ID, POSTER_NAME,
        MESSAGE, NO_LIKES, COMMENT_IDS, LATITUDE, LONGITUDE, TIMESTAMP}, this.POST_ID + "=?",
                new String[]{POST_ID}, null, null, null, null);
        if (cursor!=null)
            cursor.moveToFirst();
        return new PostTable(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),
                cursor.getInt(4), cursor.getString(5), cursor.getString(6), cursor.getString(7),
                cursor.getString(8));
    }

    public ChatTable getChat(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CHAT, new String[]{KEY_ID, REMOTE_ID,
                STATUS, MESSAGE, TIMESTAMP}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor!=null)
            cursor.moveToFirst();
        return new ChatTable();
    }

    public CommentTable getComment(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_COMMENT, new String[]{KEY_ID, COMMENTOR_ID, MESSAGE, MESSAGE, TIMESTAMP},
                KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor!=null)
            cursor.moveToFirst();
        return new CommentTable();
    }

    public UserTable getUser(String user_id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER, new String[]{USER_ID, USER_NAME, PHONE, USER_IMAGE},
                USER_ID + "=?", new String[]{user_id}, null, null, null, null);
        if (cursor!=null)
            cursor.moveToFirst();
        return new UserTable(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
    }



    public List<CommentTable> getAllComments() {
        List<CommentTable> commentList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_COMMENT;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                CommentTable commentTable = new CommentTable();
                commentTable.setComment_id(Integer.parseInt(cursor.getString(0)));
                commentTable.setCommentor_id(cursor.getString(1));
                commentTable.setPost_id(cursor.getString(2));
                commentTable.setComment(cursor.getString(3));
                commentTable.setTimestamp(cursor.getString(4));
                commentList.add(commentTable);
            } while (cursor.moveToNext());
        }
        return commentList;
    }

    public List<ChatTable> getAllChats(){
        List<ChatTable> chatList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_CHAT;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()){
            do {
                chatList.add(new ChatTable(Integer.valueOf(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
            }while (cursor.moveToNext());
        }
        return chatList;
    }

    public List<PostTable> getAllPosts(){
        List<PostTable> postList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_POST;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        String CREATE_TABLE_POST = "CREATE TABLE IF NOT EXISTS " + TABLE_POST + "("
                + POST_ID + " TEXT NOT NULL," + POSTER_ID+ " TEXT NOT NULL," + POSTER_NAME + " TEXT NOT NULL," + NO_LIKES
                + " INTEGER NOT NULL," + MESSAGE + " TEXT," + COMMENT_IDS + " TEXT," + TIMESTAMP + " TEXT,"
                + LATITUDE + " TEXT," + LONGITUDE + " TEXT," + ")";
        if (cursor.moveToFirst()){
            do {
                postList.add(new PostTable(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), Integer.valueOf(cursor.getString(4)), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8)));
            }while (cursor.moveToNext());
        }
        return postList;
    }

    public List<UserTable> getAllUsers(){
        List<UserTable> userList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_USER;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()){
            do {
                userList.add(new UserTable(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
            }while (cursor.moveToNext());
        }
        return userList;
    }

    public int getPostCount(){
        String countQuery = "SELECT * FROM " + TABLE_POST;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }

    public int getChatCount(){
        String countQuery = "SELECT * FROM " + TABLE_CHAT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }

    public int getCommentCount(){
        String countQuery = "SELECT * FROM " + TABLE_COMMENT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }

    public int getUserCount(){
        String countQuery = "SELECT * FROM " + TABLE_USER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }


    public void deleteChat(ChatTable chat){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CHAT, KEY_ID + "=?", new String[]{String.valueOf(chat.getId()) });
        db.close();
    }

    public void deletePost(PostTable post){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_POST, POST_ID + "=?", new String[]{post.getPost_id()});
        db.close();
    }

    public void deleteComment(CommentTable comment){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_COMMENT, KEY_ID + "=?", new String[]{String.valueOf(comment.getComment_id()) });
        db.close();
    }
}
