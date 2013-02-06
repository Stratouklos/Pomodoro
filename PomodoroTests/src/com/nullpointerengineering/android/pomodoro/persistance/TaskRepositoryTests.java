package com.nullpointerengineering.android.pomodoro.persistance;

import android.database.CursorIndexOutOfBoundsException;
import android.test.AndroidTestCase;
import com.nullpointerengineering.android.pomodoro.persistance.data.Task;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.List;

import static com.nullpointerengineering.android.pomodoro.persistance.DatabaseConstants.DATABASE_NAME;

/**
 * Created with IntelliJ IDEA.
 * User: Stratos
 * Date: 17/01/13
 * Time: 10:49 PM
 *
 * Task DAO tests
 */

public class TaskRepositoryTests extends AndroidTestCase {

    private SQLTaskRepository repository;
    private long taskId;


    @Override
    protected void setUp(){
        repository = new SQLTaskRepository(getContext());
        repository.open();
        taskId = repository.createTask("Test task",1,5).getId();
    }

    @Override
    protected void tearDown(){
        repository.deleteTask(taskId);
        repository.close();
        getContext().deleteDatabase(DATABASE_NAME);
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

        assertTrue("" +task.getTimeCreated().getMillis() ,new Duration(created, now).isShorterThan(new Duration(10000)));
        assertFalse(task.isDone());
    }

    public void testDeleteTask(){
        boolean thrown = false;
        repository.deleteTask(taskId);

        try {
            repository.findTaskById(taskId);
        } catch (CursorIndexOutOfBoundsException e){
            assertEquals("Index 0 requested, with a size of 0", e.getMessage());
            thrown = true;
        }
        assertTrue(thrown);
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
        Duration actual = new Duration(task.getTimeDone(), new DateTime());
        assertTrue(actual.isLongerThan(new Duration(1000)));
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

    public void testGetAll() {
        repository.createTask("Test 2", 2, 2);
        repository.createTask("Test 3", 3, 3);
        repository.createTask("Test 4", 4, 4);
        repository.createTask("Test 5", 5, 5);
        List<Task> tasks = repository.getAllTasks();

        assertEquals(5,  tasks.size());
    }

}