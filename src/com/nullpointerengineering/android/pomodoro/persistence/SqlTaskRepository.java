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

import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import com.nullpointerengineering.android.pomodoro.model.task.Task;
import com.nullpointerengineering.android.pomodoro.model.task.TaskRepository;

import static com.nullpointerengineering.android.pomodoro.persistence.database.DatabaseConstants.*;
import static com.nullpointerengineering.android.pomodoro.persistence.database.TaskProvider.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stratos
 * Date: 27/01/13
 * Time: 6:49 PM
 * Task repository backed by a content provider
 */

public class SqlTaskRepository implements TaskRepository {

    private ContentResolver resolver;

    public SqlTaskRepository(Context context) {
        resolver = context.getContentResolver();
    }

    @Override
    public Task createTask(String title, int priority, int estimate) {
        ContentValues taskValues = new ContentValues();
        taskValues.put(TASK_TITLE, title);
        taskValues.put(TASK_PRIORITY, priority);
        taskValues.put(TASK_ESTIMATE, estimate);
        Uri uri = resolver.insert(CONTENT_URI, taskValues);

        long id = Long.parseLong(uri.getPathSegments().get(TASK_ID_PATH_POSITION));

        return findTaskById(id);
    }

    @Override
    public Task findTaskById(long id) {
        Uri taskUri = CONTENT_ID_URI_BASE;
        String[] projection = {
                TASK_KEY_ID,
                TASK_TITLE,
                TASK_PRIORITY,
                TASK_ESTIMATE,
                TASK_ACTUAL,
                TASK_CREATED_DATE,
                TASK_DONE_DATE};
        Cursor cursor = resolver.query( taskUri, projection, TASK_KEY_ID +"="+ id, null, null);
        cursor.moveToFirst();
        return cursorToTask(cursor);
    }

    @Override
    public void saveTask(Task task) {
        ContentValues taskValues = new ContentValues();
        taskValues.put(TASK_KEY_ID, task.getId());
        taskValues.put(TASK_TITLE, task.getTitle());
        taskValues.put(TASK_PRIORITY, task.getPriority());
        taskValues.put(TASK_ESTIMATE, task.getEstimate());
        taskValues.put(TASK_ACTUAL, task.getActual());
        taskValues.put(TASK_CREATED_DATE, task.getTimeCreated().getMillis());
        taskValues.put(TASK_DONE_DATE, task.getTimeDone().getMillis());
        Uri taskUri = ContentUris.withAppendedId(CONTENT_ID_URI_BASE, task.getId());
        resolver.update(taskUri, taskValues, null, null);
    }

    @Override
    public int deleteTask(long id) {
        Uri taskUri = ContentUris.withAppendedId(CONTENT_ID_URI_BASE, id);
        return resolver.delete(taskUri, null, null);
    }

    private Task cursorToTask(Cursor cursor) {
        long id = cursor.getLong(0);
        String title = cursor.getString(1);
        int priority = cursor.getShort(2);
        int estimate = cursor.getShort(3);
        int actual   = cursor.getShort(4);
        long timeCreated = cursor.getLong(5);
        long timeDone = cursor.getLong(6);
        return new Task(id, title, priority, estimate, actual, timeCreated, timeDone);
    }
}
