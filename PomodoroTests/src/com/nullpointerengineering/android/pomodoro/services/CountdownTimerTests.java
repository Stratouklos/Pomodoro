package com.nullpointerengineering.android.pomodoro.services;

import android.test.AndroidTestCase;
import com.nullpointerengineering.android.pomodoro.timer.CountdownTimer;

import static com.nullpointerengineering.android.pomodoro.timer.CountdownTimer.State.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stratos
 * Date: 01/03/13
 * Time: 10:57 PM
 * Testing the timer.
 */
public class CountdownTimerTests extends AndroidTestCase {

    private CountdownTimer timer;

    @Override
    protected void setUp(){
        timer = CountdownTimer.getInstance();
    }

    public void testNewTimerState(){
        assertEquals(STOPPED, timer.getState());
    }

    public void testStartingState(){
        timer.setStartTime(10);
        timer.start();
        assertEquals(COUNTING, timer.getState());
    }

    public void testPausedState() {
        timer.setStartTime(10);
        timer.start();
        timer.pause();
        assertEquals(PAUSED, timer.getState());
    }

    public void testStoppingState() {
        timer.setStartTime(10);
        timer.start();
        timer.stop();
        assertEquals(STOPPED, timer.getState());
    }
}
