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

package com.nullpointerengineering.android.pomodoro.persistence;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.google.common.collect.ImmutableBiMap;
import com.nullpointerengineering.android.pomodoro.model.event.Event;
import com.nullpointerengineering.android.pomodoro.model.event.EventImpl;
import com.nullpointerengineering.android.pomodoro.model.event.EventRepository;

import static com.nullpointerengineering.android.pomodoro.persistence.database.DatabaseConstants.*;
import static com.nullpointerengineering.android.pomodoro.persistence.database.EventProvider.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stratos
 * Date: 18/03/13
 * Time: 8:43 PM
 * Event Repository backed by a content provider
 */
public class SqlEventRepository implements EventRepository {

    private ContentResolver resolver;
    public static final ImmutableBiMap<String, Event.Type> EVENT_TYPE_MAPPER = ImmutableBiMap.of(
            EVENT_TYPE_BIG_BREAK, Event.Type.BIG_BREAK,
            EVENT_TYPE_SMALL_BREAK, Event.Type.SMALL_BREAK,
            EVENT_TYPE_POMODORO, Event.Type.POMODORO
    );

    public SqlEventRepository(Context context) {
        resolver = context.getContentResolver();
    }

    @Override
    public Event createEvent(String type, long timeStarted, long totalDuration, long actualDuration) {
        ContentValues eventValues = new ContentValues();
        eventValues.put(EVENT_TIME_STARTED, timeStarted);
        eventValues.put(EVENT_TYPE, type);
        eventValues.put(EVENT_TOTAL_DURATION, totalDuration);
        eventValues.put(EVENT_ACTUAL_DURATION, actualDuration);

        Uri uri = resolver.insert(CONTENT_URI, eventValues);
        long id = Long.parseLong(uri.getPathSegments().get(EVENT_ID_PATH_POSITION));

        return findEventById(id);
    }

    @Override
    public Event findEventById(long id) {
        Uri eventUri = CONTENT_ID_URI_BASE;
        String[] projection = {
                EVENT_KEY_ID,
                EVENT_TIME_STARTED,
                EVENT_TYPE,
                EVENT_TOTAL_DURATION,
                EVENT_ACTUAL_DURATION
        };
        Cursor cursor = resolver.query( eventUri, projection, EVENT_KEY_ID + "=" +id, null, null);
        cursor.moveToFirst();
        return cursorToEvent(cursor);
    }

    @Override
    public void saveEvent(Event event) {
        ContentValues eventValues = new ContentValues();
        eventValues.put(EVENT_KEY_ID, event.getId());
        eventValues.put(EVENT_TIME_STARTED, event.getTimeCreated().toInstant().getMillis());
        eventValues.put(EVENT_TYPE, EVENT_TYPE_MAPPER.inverse().get(event.getType()));
        eventValues.put(EVENT_TOTAL_DURATION, event.getTotalDuration().getMillis());
        eventValues.put(EVENT_ACTUAL_DURATION, event.getActualDuration().getMillis());
        Uri eventUri = ContentUris.withAppendedId(CONTENT_ID_URI_BASE, event.getId());
        resolver.update(eventUri, eventValues, null, null);
    }

    @Override
    public int deleteEvent(long id) {
        Uri eventUri = ContentUris.withAppendedId(CONTENT_ID_URI_BASE, id);
        return resolver.delete(eventUri, null, null);
    }

    private Event cursorToEvent(Cursor cursor){
        long id = cursor.getLong(0);
        long timeCreated = cursor.getLong(1);
        String type = cursor.getString(2);
        long totalTime = cursor.getLong(3);
        long actualTime = cursor.getLong(4);
        return new EventImpl(id, timeCreated, EVENT_TYPE_MAPPER.get(type), totalTime, actualTime);
    }
}
