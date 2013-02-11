package com.nullpointerengineering.android.pomodoro.persistence;

import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import com.nullpointerengineering.android.pomodoro.persistence.database.TaskProvider;

import static com.nullpointerengineering.android.pomodoro.persistence.database.DatabaseConstants.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stratos
 * Date: 27/01/13
 * Time: 6:49 PM
 * Data access abstraction that queries the correct content resolvers for data and provides a persistence API to
 */

public class TaskRepository {

    ContentResolver resolver;

    public TaskRepository(Context context) {
        resolver = context.getContentResolver();
    }

    public Task createTask(String title, int priority, int estimate ) {
        ContentValues taskValues = new ContentValues();
        taskValues.put(TASK_TITLE, title);
        taskValues.put(TASK_PRIORITY, priority);
        taskValues.put(TASK_ESTIMATE, estimate);
        Uri uri = resolver.insert(TaskProvider.CONTENT_URI, taskValues);

        long id = Long.parseLong(uri.getPathSegments().get(TaskProvider.TASK_ID_PATH_POSITION));

        return findTaskById(id);
    }

    public Task findTaskById(long id) {
        Uri taskUri = TaskProvider.CONTENT_ID_URI_BASE;
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

    public void saveTask(Task task) {
        ContentValues taskValues = new ContentValues();
        taskValues.put(TASK_KEY_ID, task.getId());
        taskValues.put(TASK_TITLE, task.getTitle());
        taskValues.put(TASK_PRIORITY, task.getPriority());
        taskValues.put(TASK_ESTIMATE, task.getEstimate());
        taskValues.put(TASK_ACTUAL, task.getActual());
        taskValues.put(TASK_CREATED_DATE, task.getTimeCreated().getMillis());
        taskValues.put(TASK_DONE_DATE, task.getTimeDone().getMillis());
        Uri taskUri = ContentUris.withAppendedId(TaskProvider.CONTENT_ID_URI_BASE, task.getId());
        resolver.update(taskUri, taskValues, null, null);
    }

    public int deleteTask(long id) {
        Uri taskUri = ContentUris.withAppendedId(TaskProvider.CONTENT_ID_URI_BASE, id);
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
