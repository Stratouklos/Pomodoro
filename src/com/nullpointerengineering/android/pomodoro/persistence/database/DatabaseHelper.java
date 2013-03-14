/*
 * Copyright (c) 2013 Efstratios Xakoustos.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
        db.execSQL("DROP TABLE IF EXISTS "  + TABLE_EVENTS);
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

    private static final String CREATE_POMODOROS_TABLE = "create table " +  TABLE_EVENTS +" ("+
            EVENT_KEY_ID         + " INTEGER primary key autoincrement" + ", " +
            EVENT_DURATION       + " INTEGER, " +
            EVENT_TYPE           + " TEXT not null" +
            EVENT_TIME_FINISHED  + " INTEGER default 0, " +
            EVENT_COMPLETE       + " INTEGER default 0" +
            ");";
}
