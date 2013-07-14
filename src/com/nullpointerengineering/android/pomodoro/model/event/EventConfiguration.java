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

package com.nullpointerengineering.android.pomodoro.model.event;

import java.util.EnumMap;
import android.content.res.Resources;
import android.content.SharedPreferences;
import com.nullpointerengineering.android.pomodoro.R;

import static com.nullpointerengineering.android.pomodoro.model.event.Event.Type.*;

//Loads event configuration
public class EventConfiguration implements SharedPreferences.OnSharedPreferenceChangeListener{

    public static final int DEFAULT_BIG_BREAK_INTERVAL = 4;
    public static final int DEFAULT_POMODORO_LENGTH = 25;
    public static final int DEFAULT_BIG_BREAK = 10;
    public static final int DEFAULT_SMALL_BREAK = 5;

    private final String bigBreakIntervalKey;
    private final String pomodoroLengthKey;
    private final String smallBreakLengthKey;
    private final String bigBreakLengthKey;

    private static final EnumMap<Event.Type, Integer> EVENT_DURATIONS = new EnumMap<Event.Type, Integer>(Event.Type.class);
    private int bigBreakInterval;

    public EventConfiguration(SharedPreferences preferences, Resources resources){
        preferences.registerOnSharedPreferenceChangeListener(this);
        bigBreakIntervalKey = resources.getString(R.string.big_break_interval_prop_key);
        bigBreakInterval = preferences.getInt(bigBreakIntervalKey, DEFAULT_BIG_BREAK_INTERVAL);

        pomodoroLengthKey = resources.getString(R.string.pomodoro_length_prop_key);
        EVENT_DURATIONS.put(POMODORO, preferences.getInt(pomodoroLengthKey, DEFAULT_POMODORO_LENGTH));

        smallBreakLengthKey = resources.getString(R.string.small_break_length_prop_key);
        EVENT_DURATIONS.put(SMALL_BREAK, preferences.getInt(smallBreakLengthKey, DEFAULT_SMALL_BREAK));

        bigBreakLengthKey = resources.getString(R.string.big_break_length_prop_key);
        EVENT_DURATIONS.put(BIG_BREAK, preferences.getInt(bigBreakLengthKey, DEFAULT_BIG_BREAK));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences preferences, String key) {
        if (key.equals(bigBreakIntervalKey))
            bigBreakInterval = preferences.getInt(bigBreakIntervalKey, DEFAULT_BIG_BREAK_INTERVAL);
        synchronized (EVENT_DURATIONS) {
            if (key.equals(pomodoroLengthKey)) {
                EVENT_DURATIONS.remove(POMODORO);
                EVENT_DURATIONS.put(POMODORO, preferences.getInt(pomodoroLengthKey, DEFAULT_POMODORO_LENGTH));
            }
            if (key.equals(smallBreakLengthKey)) {
                EVENT_DURATIONS.remove(SMALL_BREAK);
                EVENT_DURATIONS.put(SMALL_BREAK, preferences.getInt(smallBreakLengthKey, DEFAULT_SMALL_BREAK));
            }
            if (key.equals(bigBreakIntervalKey)) {
                EVENT_DURATIONS.remove(BIG_BREAK);
                EVENT_DURATIONS.put(BIG_BREAK, preferences.getInt(bigBreakLengthKey, DEFAULT_BIG_BREAK));
            }
        }
    }

    public int getDuration(Event.Type eventType) {
        synchronized (EVENT_DURATIONS) {
            return EVENT_DURATIONS.get(eventType);
        }
    }

    public int getBigBreakInterval() {
        return bigBreakInterval;
    }
}