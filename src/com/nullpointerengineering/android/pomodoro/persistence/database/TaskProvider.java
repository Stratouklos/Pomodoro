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

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;

import static com.nullpointerengineering.android.pomodoro.persistence.database.DatabaseConstants.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stratos
 * Date: 07/02/13
 * Time: 7:33 PM
 * Provides access to the database of tasks.
 */
public class TaskProvider extends ContentProvider {

    private DatabaseHelper databaseHelper;

    @Override
    public boolean onCreate() {
        databaseHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(TABLE_TASKS);

        switch (uriMatcher.match(uri)){
            case TASKS:
                queryBuilder.setProjectionMap(tasksProjectionMap);
                break;
            case TASK_ID:
                queryBuilder.setProjectionMap(tasksProjectionMap);
                queryBuilder.appendWhere( TASK_KEY_ID + "=" + uri.getPathSegments().get(TASK_ID_PATH_POSITION));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        String orderBy;
        if (TextUtils.isEmpty(sortOrder)){
            orderBy = DEFAULT_SORT_ORDER;
        } else orderBy = sortOrder;

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = queryBuilder.query( db, projection, selection, selectionArgs, null, null, orderBy );
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri){
        switch (uriMatcher.match(uri)){
            case TASKS:
                return CONTENT_TYPE;
            case TASK_ID:
                return CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (uriMatcher.match(uri) != TASKS) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        //If no values are passed in create some default ones
        if (values == null) {
            values = new ContentValues();
        }

        if (!values.containsKey(TASK_TITLE)) {
            values.put(TASK_TITLE, "Some random task");
        }

        if (!values.containsKey(TASK_PRIORITY)) {
            values.put(TASK_PRIORITY, 5);
        }

        if (!values.containsKey(TASK_ESTIMATE)) {
            values.put(TASK_ESTIMATE, 2);
        }

        if (!values.containsKey(TASK_CREATED_DATE)){
            values.put(TASK_CREATED_DATE, System.currentTimeMillis());
        }

        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        long rowId = db.insert( TABLE_TASKS, null, values );

        if (rowId > 0 ) {
            Uri taskUri = ContentUris.withAppendedId(CONTENT_ID_URI_BASE, rowId);
            getContext().getContentResolver().notifyChange(taskUri, null);
            return taskUri;
        }
        throw new SQLException("Failed to insert row into " + uri);
    }


    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String finalWhere;
        int count;

        switch (uriMatcher.match(uri)) {
            case TASKS:
                count = db.delete( TABLE_TASKS, where, whereArgs );
                break;
            case TASK_ID:
                finalWhere = TASK_KEY_ID + " = " + uri.getPathSegments().get(TASK_ID_PATH_POSITION);

                if (where != null) {
                    finalWhere = finalWhere + " AND " + where;
                }

                count = db.delete( TABLE_TASKS, finalWhere, whereArgs );
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int count;
        String finalWhere;

        switch (uriMatcher.match(uri)) {
            case TASKS:
                count = db.update( TABLE_TASKS, values, where, whereArgs);
                break;
            case TASK_ID:
                finalWhere = TASK_KEY_ID + " = " + uri.getPathSegments().get(TASK_ID_PATH_POSITION);

                if (where !=null) {
                    finalWhere = finalWhere + " AND " + where;
                }

                count = db.update( TABLE_TASKS, values, finalWhere, whereArgs );
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    public static final String AUTHORITY = "com.nullpointerengineering.android.pomodoro.provider.task";

    private static final String SCHEME = "content://";

    private static final String PATH_TASKS = "/tasks";

    private static final String PATH_TASK_ID = "/tasks/";

    public static final Uri CONTENT_URI =  Uri.parse(SCHEME + AUTHORITY + PATH_TASKS);

    public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + AUTHORITY + PATH_TASK_ID);

    @SuppressWarnings("UnusedDeclaration")
    public static final Uri CONTENT_ID_URI_PATTERN = Uri.parse(SCHEME + AUTHORITY + PATH_TASK_ID + "/#");

    public static final int TASK_ID_PATH_POSITION = 1;

    /*
     * MIME type definitions
     */

    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.nullpointerengineering.task";

    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.nullpointerengineering.task";

    public static final String DEFAULT_SORT_ORDER = TASK_PRIORITY + " ASC";

    private static HashMap<String, String> tasksProjectionMap;

    private static final int TASKS = 1;

    private static final int TASK_ID = 2;

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "tasks", TASKS);
        uriMatcher.addURI(AUTHORITY, "tasks/#", TASK_ID);

        tasksProjectionMap = new HashMap<String, String>();
        tasksProjectionMap.put(TASK_KEY_ID, TASK_KEY_ID);
        tasksProjectionMap.put(TASK_TITLE, TASK_TITLE);
        tasksProjectionMap.put(TASK_PRIORITY, TASK_PRIORITY);
        tasksProjectionMap.put(TASK_ESTIMATE, TASK_ESTIMATE);
        tasksProjectionMap.put(TASK_ACTUAL, TASK_ACTUAL);
        tasksProjectionMap.put(TASK_CREATED_DATE, TASK_CREATED_DATE);
        tasksProjectionMap.put(TASK_DONE_DATE, TASK_DONE_DATE);
    }
}
