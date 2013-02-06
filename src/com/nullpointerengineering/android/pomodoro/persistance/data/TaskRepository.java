package com.nullpointerengineering.android.pomodoro.persistance.data;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Stratos
 * Date: 29/01/13
 * Time: 12:11 AM
 * Interface to perform CRUD operations for tasks
 */
public interface TaskRepository {

    public void open();

    public void close();

    public Task createTask(String title, int priority, int estimate);

    public Task findTaskById(long id);

    public void saveTask(Task task);

    public void deleteTask(long id);

    public List<Task> getAllTasks();
}
