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
 * Date: 13/03/13
 * Time: 10:27 PM
 * Provides access to the event database.
 */
public class EventProvider extends ContentProvider {

    private DatabaseHelper databaseHelper;

    @Override
    public boolean onCreate() {
        databaseHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(TABLE_EVENTS);

        switch (uriMatcher.match(uri)){
            case EVENTS:
                queryBuilder.setProjectionMap(eventsProjectionMap);
                break;
            case EVENT_ID:
                queryBuilder.setProjectionMap(eventsProjectionMap);
                queryBuilder.appendWhere(
                        EVENT_KEY_ID +
                                "=" +
                                uri.getPathSegments().get(EVENT_ID_PATH_POSITION));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        String orderBy;
        if (TextUtils.isEmpty(sortOrder)){
            orderBy = DEFAULT_SORT_ORDER;
        } else orderBy = sortOrder;

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = queryBuilder.query(
                db, projection, selection, selectionArgs, null, null, orderBy );
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            case EVENTS:
                return CONTENT_TYPE;
            case EVENT_ID:
                return CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (uriMatcher.match(uri) != EVENTS) {
            throw new IllegalArgumentException("Unknown URI" + uri);
        }

        if (values == null) {
            values = new ContentValues();
        }

        if (!values.containsKey(EVENT_TYPE)) {
            values.put(EVENT_TYPE, "pomodoro");
        }

        if (!values.containsKey(EVENT_DURATION)) {
            values.put(EVENT_DURATION, 0);
        }

        if (!values.containsKey(EVENT_TIME_FINISHED)) {
            values.put(EVENT_TIME_FINISHED, 0);
        }

        if (!values.containsKey(EVENT_COMPLETE)) {
            values.put(EVENT_COMPLETE, 0);
        }

        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        long rowId = db.insert(TABLE_EVENTS, null, values);

        if (rowId > 0 ) {
            Uri eventUri = ContentUris.withAppendedId(CONTENT_ID_URI_BASE, rowId);
            getContext().getContentResolver().notifyChange(eventUri, null);
            return eventUri;
        }
        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String finalWhere;
        int count;

        switch (uriMatcher.match(uri)) {
            case EVENTS:
                count = db.delete(TABLE_EVENTS, where, whereArgs);
                break;
            case EVENT_ID:
                finalWhere = EVENT_KEY_ID + " = " + uri.getPathSegments().get(EVENT_ID_PATH_POSITION);
                if (where != null) {
                    finalWhere = finalWhere + " AND " + where;
                }

                count = db.delete( TABLE_EVENTS, finalWhere, whereArgs );
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
            case EVENTS:
                count = db.update( TABLE_EVENTS, values, where, whereArgs);
                break;
            case EVENT_ID:
                finalWhere =EVENT_KEY_ID + " = " + uri.getPathSegments().get(EVENT_ID_PATH_POSITION);

                if (where !=null) {
                    finalWhere = finalWhere + " AND " + where;
                }

                count = db.update( TABLE_EVENTS, values, finalWhere, whereArgs );
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    public static final String AUTHORITY = "com.nullpointerengineering.android.pomodoro.provider.event";

    private static final String SCHEME = "content://";

    private static final String PATH_EVENTS = "/events";

    private static final String PATH_EVENT_ID = "/events/";

    public static final Uri CONTENT_URI =  Uri.parse(SCHEME + AUTHORITY + PATH_EVENTS);

    public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + AUTHORITY + PATH_EVENT_ID);

    @SuppressWarnings("UnusedDeclaration")
    public static final Uri CONTENT_ID_URI_PATTERN = Uri.parse(SCHEME + AUTHORITY + PATH_EVENT_ID + "/#");

    public static final int EVENT_ID_PATH_POSITION = 1;

    /*
     * MIME type definitions
     */

    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.nullpointerengineering.event";

    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.nullpointerengineering.event";

    public static final String DEFAULT_SORT_ORDER = EVENT_TIME_FINISHED + " DESC";

    private static HashMap<String, String> eventsProjectionMap;

    private static final int EVENTS= 1;

    private static final int EVENT_ID = 2;

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "events", EVENTS);
        uriMatcher.addURI(AUTHORITY, "events/#", EVENT_ID);

        eventsProjectionMap = new HashMap<String, String>();
        eventsProjectionMap.put(EVENT_KEY_ID, EVENT_KEY_ID);
        eventsProjectionMap.put(EVENT_DURATION, EVENT_DURATION);
        eventsProjectionMap.put(EVENT_TYPE, EVENT_TYPE);
        eventsProjectionMap.put(EVENT_TIME_FINISHED, EVENT_TIME_FINISHED);
        eventsProjectionMap.put(EVENT_COMPLETE, EVENT_COMPLETE);
    }
}
