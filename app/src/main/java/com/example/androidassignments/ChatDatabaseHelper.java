package com.example.androidassignments;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ChatDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Database";
    public static final int VERSION_NUM = 3;

    public static String TABLE_NAME = "KeyTable";
    public static String KEY_ID = "_id";
    public static String KEY_MESSAGE = "Message";


    public ChatDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        android.util.Log.i("ChatDatabaseHelper", "Calling onCreate");
        String createStr = "CREATE TABLE "+TABLE_NAME+" ("+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+KEY_MESSAGE+" TEXT)";
        db.execSQL(createStr);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        android.util.Log.i("ChatDatabaseHelper", "Calling onUpgrade, oldVersion=" + oldVersion + " newVersion=" + newVersion);
        String upgradeStr = "DROP TABLE IF EXISTS "+TABLE_NAME;
        db.execSQL(upgradeStr);
        onCreate(db);
    }

}
