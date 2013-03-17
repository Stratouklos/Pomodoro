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

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;

import static com.nullpointerengineering.android.pomodoro.persistence.database.DatabaseConstants.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stratos
 * Date: 13/03/13
 * Time: 10:38 PM
 * Event provider test cases.
 */
public class EventProviderTests extends AndroidTestCase {

    private final static int KEY_ID_INDEX = 0;
    private final static int TYPE_INDEX = 1;
    private final static int TIME_FINISHED_INDEX = 2;
    private final static int DURATION_INDEX = 3;
    private final static int COMPLETE_INDEX = 4;
    private final static String[] PROJECTION;
    static  {
        PROJECTION = new String[5];
        PROJECTION[KEY_ID_INDEX] = EVENT_KEY_ID;
        PROJECTION[TYPE_INDEX] = EVENT_TYPE;
        PROJECTION[TIME_FINISHED_INDEX] = EVENT_TIME_FINISHED;
        PROJECTION[DURATION_INDEX] = EVENT_DURATION;
        PROJECTION[COMPLETE_INDEX] = EVENT_COMPLETE;
    }

    private ContentResolver resolver;
    private long id;

    @Override
    protected void setUp(){
        resolver = getContext().getContentResolver();
        id = createTestEvent();
    }

    @Override
    protected void tearDown() {
        resolver.delete(EventProvider.CONTENT_URI, null, null);
    }

    private long createTestEvent() {
        ContentValues eventValues = new ContentValues();
        eventValues.put(DatabaseConstants.EVENT_TYPE, "pomodoro");
        eventValues.put(DatabaseConstants.EVENT_TIME_FINISHED, System.currentTimeMillis());
        eventValues.put(DatabaseConstants.EVENT_COMPLETE, 1);
        eventValues.put(DatabaseConstants.EVENT_DURATION, 25 * 60 );
        Uri uri = resolver.insert(EventProvider.CONTENT_URI, eventValues);
        return Long.parseLong(uri.getPathSegments().get(EventProvider.EVENT_ID_PATH_POSITION));
    }

    //POST
    public void testCreateEvent() {
        assertNotSame(0, id);
    }

    //GET
    public void testFindEventById() {
        Uri eventUri = EventProvider.CONTENT_ID_URI_BASE;
        Cursor cursor = resolver.query( eventUri, PROJECTION, EVENT_KEY_ID +"="+ id, null, null);
        assertEquals(1, cursor.getCount());
        cursor.moveToFirst();
        assertEquals(id, cursor.getLong(KEY_ID_INDEX));
    }

    public void testFindAllEvents() {
        createTestEvent();
        Uri eventUri = EventProvider.CONTENT_URI;
        Cursor cursor = resolver.query( eventUri, PROJECTION, null, null, null);
        assertEquals(2, cursor.getCount());
    }

    //PUT
    public void testUpdateEvent() {
        Uri eventUri = ContentUris.withAppendedId(EventProvider.CONTENT_ID_URI_BASE, id);
        ContentValues eventValues = new ContentValues();
        eventValues.put(DatabaseConstants.EVENT_TYPE, "break");
        resolver.update(eventUri,eventValues, null, null );

        Cursor cursor = resolver.query(eventUri, PROJECTION, null, null, null);
        cursor.moveToFirst();
        assertEquals("Value that should be updated didn't","break", cursor.getString(TYPE_INDEX));
        assertEquals("Value that shouldn't be updated did", 25 * 60 , cursor.getInt(DURATION_INDEX));
    }

    //DELETE
    public void testDeleteEvent() {
        Uri eventUri = ContentUris.withAppendedId(EventProvider.CONTENT_ID_URI_BASE, id);
        int count = resolver.delete(eventUri, null, null);
        assertEquals(1 , count);
    }
}
