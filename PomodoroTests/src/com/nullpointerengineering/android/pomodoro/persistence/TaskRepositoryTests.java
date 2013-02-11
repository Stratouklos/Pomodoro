package com.nullpointerengineering.android.pomodoro.persistence;

import android.database.CursorIndexOutOfBoundsException;
import android.test.AndroidTestCase;
import org.joda.time.DateTime;
import org.joda.time.Duration;

/**
 * Created with IntelliJ IDEA.
 * User: Stratos
 * Date: 17/01/13
 * Time: 10:49 PM
 *
 * Task DAO tests
 */

public class TaskRepositoryTests extends AndroidTestCase {

    private TaskRepository repository;
    private long taskId;

    private static final Duration ONE_SECOND = new Duration(1000);

    @Override
    protected void setUp(){
        repository = new TaskRepository(getContext());
        taskId = repository.createTask("Test task",1,5).getId();
    }

    @Override
    protected void tearDown(){
        repository.deleteTask(taskId);
    }

    public void testCreatedTask(){
        Task task = repository.findTaskById(taskId);

        assertEquals("Test task", task.getTitle());
        assertEquals(1, task.getPriority());
        assertEquals(5, task.getEstimate());
        assertEquals(0, task.getActual());
        assertEquals(new DateTime(0), task.getTimeDone());

        DateTime now = new DateTime();
        DateTime created = task.getTimeCreated();
        Duration timePassed = new Duration(created, now);

        String msg = "Time created was: " +task.getTimeCreated().getMillis()
                + "Now it's:" + System.currentTimeMillis();

        assertTrue(msg, timePassed.isShorterThan(ONE_SECOND));
        assertFalse(task.isDone());
    }

    public void testDeleteTask(){
        boolean thrown = false;
        assertEquals(1, repository.deleteTask(taskId));
    }

    public void testEditTask(){
        Task task = repository.findTaskById(taskId);

        task.setTitle("New test task");
        task.setPriority(2);
        task.setEstimate(3);

        repository.saveTask(task);

        task = repository.findTaskById(taskId);

        assertEquals("New test task", task.getTitle());
        assertEquals(2, task.getPriority());
        assertEquals(3, task.getEstimate());
    }

    public void testMarkDone(){
        Task task = repository.findTaskById(taskId);

        task.setDone(true);
        repository.saveTask(task);

        task = repository.findTaskById(taskId);
        assertTrue(task.isDone());
        DateTime now = new DateTime();
        DateTime timeDone = task.getTimeDone();
        Duration timePassed= new Duration(timeDone, now);
        assertTrue(timePassed.isShorterThan(ONE_SECOND));
    }

    public void testMarkDoneAndThenUndone(){
        Task task = repository.findTaskById(taskId);

        task.setDone(true);
        repository.saveTask(task);
        task.setDone(false);
        repository.saveTask(task);

        task = repository.findTaskById(taskId);
        assertFalse(task.isDone());
        assertEquals(new DateTime(0), task.getTimeDone());
    }

    public void testIncrementActual(){
        Task task = repository.findTaskById(taskId);

        task.setActual(task.getActual() + 1);
        repository.saveTask(task);

        task = repository.findTaskById(taskId);
        assertEquals(1, task.getActual());
    }
}