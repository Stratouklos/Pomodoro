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