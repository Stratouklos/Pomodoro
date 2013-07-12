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

    /****************
     * Events table *
     ****************/

    public static final String TABLE_EVENTS = "events";

    public static final String EVENT_KEY_ID = "_id";
    public static final String EVENT_TYPE = "type";
    public static final String EVENT_ACTUAL_DURATION = "actual_duration";
    public static final String EVENT_TOTAL_DURATION = "total_duration";
    public static final String EVENT_TIME_STARTED = "started_on";

    public static final String EVENT_TYPE_POMODORO = "pomodoro";
    public static final String EVENT_TYPE_SMALL_BREAK = "small_break";
    public static final String EVENT_TYPE_BIG_BREAK = "big_break";

    //Database Version
    public static final int  DATABASE_VERSION = 1;
}