package com.nullpointerengineering.android.pomodoro.persistence.data;

/**
 * Created by IntelliJ IDEA.
 * User: Stratos
 * Date: 17/05/11
 * Time: 1:31 PM
 *
 * Constants declaration file.
 *
 */

public class DatabaseConstants {

    /***********************************************
     * Database, table and column name definitions *
     ***********************************************/

    public static final String DATABASE_NAME = "pomodoro_db";

    /***************
     * Tasks table *
     ***************/

    public static final String TABLE_TASKS = "tasks";

    public static final String TASK_KEY_ID = "_id";
    public static final String TASK_TITLE = "title";
    public static final String TASK_PRIORITY = "priority";
    public static final String TASK_ESTIMATE = "estimate";
    public static final String TASK_ACTUAL = "actual";
    public static final String TASK_DONE = "done";

    public static final String TASK_CREATED_DATE = "created_on";
    public static final String TASK_DONE_DATE = "done_on";

    /*******************
     * Pomodoros table *
     *******************/

    public static final String TABLE_POMODOROS  = "pomodoros";

    public static final String POMODORO_KEY_ID = "_id";
    public static final String POMODORO_DURATION = "duration";
    public static final String POMODORO_TIME_FINISHED = "finished_on";
    public static final String POMODORO_COMPLETE = "complete";

    //Database Version
    public static final int  DATABASE_VERSION = 1;
}