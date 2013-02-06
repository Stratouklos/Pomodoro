package com.nullpointerengineering.android.pomodoro.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.List;

import static com.nullpointerengineering.android.pomodoro.data.DatabaseConstants.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stratos
 * Date: 27/01/13
 * Time: 6:49 PM
 * Task DAO class
 */

public class SQLTaskRepository implements TaskRepository {
    private SQLiteDatabase database;
    private DatabaseHelper helper;

    public SQLTaskRepository(Context context) {
        helper = new DatabaseHelper(context);
    }

    @Override
    public void open() throws SQLiteException {
        database = helper.getWritableDatabase();
    }

    @Override
    public void close() {
        helper.close();
    }

    @Override
    public Task createTask(String title, int priority, int estimate ) {
        ContentValues taskValues = new ContentValues();
        taskValues.put(TASK_TITLE, title);
        taskValues.put(TASK_PRIORITY, priority);
        taskValues.put(TASK_ESTIMATE, estimate);
        long id = database.insert(TABLE_TASKS, null, taskValues);

        return findTaskById(id);
    }

    @Override
    public Task findTaskById(long id) {
        Cursor cursor = database.query(
                TABLE_TASKS,                //table
                null,                       //columns = all
                TASK_KEY_ID + "=" + id,     //selection
                null,null,null,null,        //selection args, groupBy, having, orderBy
                "0, 1");
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
        taskValues.put(TASK_DONE, task.isDone());
        database.update(TABLE_TASKS, taskValues, TASK_KEY_ID + "=" + task.getId(), null);
    }

    @Override
    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<Task>();
        Cursor cursor = database.query(TABLE_TASKS, null, null,null,null,null,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Task task = cursorToTask(cursor);
            tasks.add(task);
            cursor.moveToNext();
        }
        cursor.close();
        return tasks;
    }

    @Override
    public void deleteTask(long id) {
        database.delete(TABLE_TASKS, TASK_KEY_ID + "=" + id, null);
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
