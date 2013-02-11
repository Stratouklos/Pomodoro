package com.nullpointerengineering.android.pomodoro.persistence.database;

 import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.nullpointerengineering.android.pomodoro.persistence.database.DatabaseConstants.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stratos
 * Date: 17/01/13
 * Time: 10:22 PM
 *
 * Opens a Db connection, builds the db when first run or upgrades when it has to.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v( this.getClass().toString(), "Creating the tables");
        Log.v(this.getClass().toString(), CREATE_TASKS_TABLE);
        db.execSQL(CREATE_TASKS_TABLE);
        db.execSQL(CREATE_POMODOROS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.v(this.getClass().toString(), "Upgrading database from version" + oldVersion +
                "to version" + newVersion + "all data are lost.");
        db.execSQL("DROP TABLE IF EXISTS  " + TABLE_TASKS);
        db.execSQL("DROP TABLE IF EXISTS "  + TABLE_POMODOROS);
        onCreate(db);
    }

    private static final String CREATE_TASKS_TABLE = "create table "+ TABLE_TASKS +" ( "+
            TASK_KEY_ID        + " integer primary key autoincrement, " +
            TASK_TITLE         + " TEXT not null, " +
            TASK_PRIORITY      + " INTEGER not null, " +
            TASK_ESTIMATE      + " INTEGER not null, " +
            TASK_ACTUAL        + " INTEGER, " +
            TASK_CREATED_DATE  + "  INTEGER not null, " +
            TASK_DONE_DATE     + " INTEGER" +
            ");";

    private static final String CREATE_POMODOROS_TABLE = "create table " +  TABLE_POMODOROS +" ("+
            POMODORO_KEY_ID         + " INTEGER primary key autoincrement" + ", " +
            POMODORO_DURATION       + " INTEGER, " +
            POMODORO_TIME_FINISHED  + " INTEGER default 0, " +
            POMODORO_COMPLETE       + " INTEGER default 0" +
            ");";
}
