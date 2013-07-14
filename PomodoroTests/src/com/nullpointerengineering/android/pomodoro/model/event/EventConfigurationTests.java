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

import android.test.AndroidTestCase;

import static android.preference.PreferenceManager.*;
import static com.nullpointerengineering.android.pomodoro.model.event.Event.Type.*;

public class EventConfigurationTests extends AndroidTestCase{

    private static EventConfiguration eventConfiguration;

    protected void setUp() throws Exception {
        getDefaultSharedPreferences(getContext()).edit().clear().commit();
        eventConfiguration = new EventConfiguration(getDefaultSharedPreferences(getContext()), getContext().getResources());
    }

    public void testGetPomodoroDuration() {
        assertEquals(25 , eventConfiguration.getDuration(POMODORO));
    }

    public void testGetBigBreakDuration() {
        assertEquals(10, eventConfiguration.getDuration(BIG_BREAK));
    }

    public void testGetSmallBreakDuration() {
        assertEquals(5, eventConfiguration.getDuration(SMALL_BREAK));
    }

    public void testGetBigBreakInterval() {
        assertEquals(4, eventConfiguration.getBigBreakInterval());
    }
}
