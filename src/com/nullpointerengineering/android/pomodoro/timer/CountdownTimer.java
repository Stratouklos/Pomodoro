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

package com.nullpointerengineering.android.pomodoro.timer;

import android.util.Log;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import static com.nullpointerengineering.android.pomodoro.timer.CountdownTimer.State.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stratos
 * Date: 23/02/13
 * Time: 12:28 PM
 *
 */
public class CountdownTimer {

    private static final CountdownTimer INSTANCE = new CountdownTimer();
    private static final String TAG = "CountdownTimer";
    private static final boolean D = true;

    private Timer timer;
    private State state;
    private int count;
    private int start;
    private Set<TimerListener> listeners;

    public enum State {
        STOPPED,
        COUNTING,
        PAUSED,
    }

    private CountdownTimer() {
        state = STOPPED;
        if (D) Log.d(TAG, "state: stopped");
        listeners = new HashSet<TimerListener>();
        start = STARTING_DURATION;
    }

    public static CountdownTimer getInstance(){
        return INSTANCE;
    }

    public synchronized void setStartTime(int seconds) {
        if (D) Log.d(TAG, "setStartTime "+ seconds);
        start = seconds;
    }

    public synchronized void registerListener(TimerListener listener) {
        listeners.add(listener);
        if (D) Log.d(TAG, "Registering listener, active listeners: " + listeners.size());
    }

    public synchronized void deregisterListener(TimerListener listener) {
        listeners.remove(listener);
        if (D) Log.d(TAG, "deRegistering listener, active listeners: " + listeners.size());
    }

    public synchronized void start() {
        if (D) Log.d(TAG, "start");
        switch (state) {
            case STOPPED:
                count = start;
            case PAUSED:
                timer = new Timer(TAG);
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        if (count > 0) {
                            count--;
                            Log.d(TAG, "Tick: " + count);
                            for (TimerListener listener : listeners) {
                                listener.timerUpdate(count);
                            }
                        }
                        else stop();
                    }
                }, 0, DELAY);
                state = COUNTING;
                if (D) Log.d(TAG, "state: counting");
                break;
            case COUNTING:
                break;
            default:
                throw new IllegalStateException(state.toString());
        }
    }

    public synchronized void pause() {
        if (D) Log.d(TAG, "pause");
        state = PAUSED;
        if (D) Log.d(TAG, "state: paused");
        timer.cancel();
    }

    public synchronized void stop() {
        if (D) Log.d(TAG, "stop");
        state = STOPPED;
        if (D) Log.d(TAG, "state: stopped");
        timer.cancel();
    }

    public synchronized State getState(){
        return state;
    }

    private static final int DELAY = 1000;
    private static final int STARTING_DURATION = 25;
}