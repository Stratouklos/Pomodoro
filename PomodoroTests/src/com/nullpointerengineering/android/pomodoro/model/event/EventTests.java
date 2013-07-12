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
import org.joda.time.DateTime;
import org.joda.time.Duration;

import static com.nullpointerengineering.android.pomodoro.model.event.Event.Type.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stratos
 * Date: 17/03/13
 * Time: 10:05 PM
 * Event domain object tests
 */

public class EventTests extends AndroidTestCase {

    public void testPomodoroType() {
        Event event = new EventImpl(0, 0, Event.Type.POMODORO, 1000, 1000);
        assertEquals(POMODORO, event.getType());
    }

    public void testBreakType() {
        Event event = new EventImpl(0, 0, SMALL_BREAK, 1000, 1000);
        assertEquals(SMALL_BREAK, event.getType());
    }

    public void testTotalDuration() {
        Event event = new EventImpl(0, 0 ,Event.Type.POMODORO, 1000, 1000);
        Duration expected = new Duration(1000);
        assertEquals(expected, event.getTotalDuration());
    }

    public void testActualDuration() {
        Event event = new EventImpl(0, 0 ,Event.Type.POMODORO, 1000, 1000);
        Duration expected = new Duration(1000);
        assertEquals(expected, event.getActualDuration());
    }

    public void testNegativeDuration() {
        @SuppressWarnings("ThrowableInstanceNeverThrown")
        IllegalArgumentException expected = new IllegalArgumentException("Illegal duration argument -1000");
        IllegalArgumentException caught = null;
        try {
            new EventImpl(0, 0, Event.Type.POMODORO, - 1000, 1000);
        } catch (IllegalArgumentException e) {
            caught = e;
        }
        assertNotNull(caught);
        assertSame(expected.getClass(), caught.getClass());
        assertEquals(expected.getMessage(), caught.getMessage());
    }

    public void testNegativeId() {
        @SuppressWarnings("ThrowableInstanceNeverThrown")
        IllegalArgumentException expected = new IllegalArgumentException("Illegal id -1");
        IllegalArgumentException caught = null;
        try {
            new EventImpl(-1, 0, Event.Type.POMODORO, 1000, 1000);
        } catch (IllegalArgumentException e) {
            caught = e;
        }
        assertNotNull(caught);
        assertSame(expected.getClass(), caught.getClass());
        assertEquals(expected.getMessage(), caught.getMessage());
    }

    public void testCreatedOn() {
        long millis = System.currentTimeMillis();
        DateTime expected = new DateTime(millis * 1000);
        Event event = new EventImpl(0, millis, Event.Type.POMODORO, 1000, 1000);
        assertEquals(expected, event.getTimeCreated());
    }

    public void testNegativeCreatedOn() {
        @SuppressWarnings("ThrowableInstanceNeverThrown")
        IllegalArgumentException expected = new IllegalArgumentException("Illegal crated time -1000");
        IllegalArgumentException caught = null;
        try {
            new EventImpl(0, -1000, Event.Type.POMODORO, 1000, 1000);
        } catch (IllegalArgumentException e) {
            caught = e;
        }
        assertNotNull(caught);
        assertSame(expected.getClass(), caught.getClass());
        assertEquals(expected.getMessage(), caught.getMessage());
    }

}
