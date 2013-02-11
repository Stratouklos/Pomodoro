package com.nullpointerengineering.android.pomodoro.persistence;

import org.joda.time.DateTime;


/**
 * Created with IntelliJ IDEA.
 * User: Stratos
 * Date: 28/01/13
 * Time: 11:19 PM
 * Task container object
 */
public class Task {

    private long     id;
    private String   title;
    private int      priority;
    private int      estimate;
    private int      actual = 0;
    private DateTime timeCreated;
    private DateTime timeDone;

    protected Task(long id, String title, int priority, int estimate, int actual, long timeCreated, long timeDone) {
        this.id = id;
        this.title = title;
        this.priority = priority;
        this.estimate = estimate;
        this.actual = actual;
        this.timeCreated = new DateTime(timeCreated * 1000);
        this.timeDone = new DateTime(timeDone * 1000);
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getPriority() {
        return priority;
    }

    public int getEstimate() {
        return estimate;
    }

    public int getActual() {
        return actual;
    }

    public boolean isDone() {
        return !timeDone.equals(new DateTime(0));
    }

    public DateTime getTimeCreated() {
        return timeCreated;
    }

    public DateTime getTimeDone(){
        return timeDone;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setEstimate(int estimate) {
        this.estimate = estimate;
    }

    public void setActual(int actual) {
        this.actual = actual;
    }

    public void setDone(boolean done) {
        this.timeDone = (done) ? new DateTime() : new DateTime(0);
    }

    public String toString() {
        StringBuilder result = new StringBuilder("Task: ");
        result.append("id=").append(id).
                append("| title=").append(title).
                append("| priority=").append(priority).
                append("| estimate=").append(estimate).
                append("| actual=").append(actual).
                append("| done=").append(timeDone.toString()).
                append("| created=").append(timeCreated.toString());

        return result.toString();
    }
}
