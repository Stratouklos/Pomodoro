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
import com.nullpointerengineering.android.pomodoro.persistence.EventRepositoryTestImpl;
import org.joda.time.Duration;

import static com.nullpointerengineering.android.pomodoro.model.event.Event.Type.*;
import static org.mockito.Mockito.*;

public class EventProviderTests extends AndroidTestCase {

    private EventProvider providerUnderTest;

    private EventConfiguration mockConfiguration = mock(EventConfiguration.class);
    private EventRepository testRepository = new EventRepositoryTestImpl();

    @Override
    protected void setUp() throws Exception {
        reset(mockConfiguration);
        when(mockConfiguration.getBigBreakInterval()).thenReturn(4);
        when(mockConfiguration.getDuration(POMODORO)).thenReturn(22);
        when(mockConfiguration.getDuration(SMALL_BREAK)).thenReturn(3);
        when(mockConfiguration.getDuration(BIG_BREAK)).thenReturn(9);
        providerUnderTest = new EventProviderImpl(mockConfiguration, testRepository);
    }

    public void testFirstEvent() {
        Event actual = providerUnderTest.getEventNumber(1);
        assertEquals(POMODORO, actual.getType());
    }

    public void testSecondEvent() {
        Event actual = providerUnderTest.getEventNumber(2);
        assertEquals(SMALL_BREAK, actual.getType());
    }

    public void testThirdCall() {
        Event actual = providerUnderTest.getEventNumber(3);
        assertEquals(POMODORO, actual.getType());
    }

    public void testPomodoroDuration() {
        Event actual = providerUnderTest.getEventNumber(1);
        assertEquals(new Duration(22 * 60 * 1000), actual.getTotalDuration());
    }

    public void testSmallBreakDuration() {
        Event actual = providerUnderTest.getEventNumber(2);
        assertEquals(new Duration(3 * 60 * 1000), actual.getTotalDuration());
    }

    public void testBigBreakDuration() {
        Event actual = providerUnderTest.getEventNumber(8);
        assertEquals(new Duration(9 * 60 * 1000), actual.getTotalDuration());
    }

    // 1 P | 2 B | 3 P | 4 B | 5 P | 6 B | 7 P | 8 BB | 9 P | 10 B
    // 11 P | 12 B | 13 P | 14 B | 15 P | 16 BB | 17 P | 18 B | 19 P
    public void testMultipleWithBreakIntervalToFour() {
        assertEquals(POMODORO, providerUnderTest.getEventNumber(5).getType());
        assertEquals(SMALL_BREAK, providerUnderTest.getEventNumber(6).getType());
        assertEquals(BIG_BREAK, providerUnderTest.getEventNumber(8).getType());
        assertEquals(POMODORO, providerUnderTest.getEventNumber(11).getType());
        assertEquals(BIG_BREAK, providerUnderTest.getEventNumber(16).getType());
    }

    // 1 P | 2 B | 3 P | 4 BB | 5 P | 6 B | 7 P | 8 BB | 9 P | 10 B
    public void testMultipleWithBreakIntervalToTwo() {
        reset(mockConfiguration);
        when(mockConfiguration.getBigBreakInterval()).thenReturn(2);

        assertEquals(POMODORO, providerUnderTest.getEventNumber(1).getType());
        assertEquals(SMALL_BREAK, providerUnderTest.getEventNumber(2).getType());
        assertEquals(POMODORO, providerUnderTest.getEventNumber(3).getType());
        assertEquals(BIG_BREAK, providerUnderTest.getEventNumber(4).getType());
        assertEquals(POMODORO, providerUnderTest.getEventNumber(5).getType());
        assertEquals(SMALL_BREAK, providerUnderTest.getEventNumber(6).getType());
        assertEquals(POMODORO, providerUnderTest.getEventNumber(7).getType());
        assertEquals(BIG_BREAK, providerUnderTest.getEventNumber(8).getType());
    }

    // 1 P | 2 B | 3 P | 4 B | 5 P | 6 BB | 7 P | 8 B | 9 P | 10 B
    public void testMultipleWithBreakIntervalToThree() {
        reset(mockConfiguration);
        when(mockConfiguration.getBigBreakInterval()).thenReturn(3);

        assertEquals(POMODORO, providerUnderTest.getEventNumber(1).getType());
        assertEquals(SMALL_BREAK, providerUnderTest.getEventNumber(2).getType());
        assertEquals(POMODORO, providerUnderTest.getEventNumber(3).getType());
        assertEquals(SMALL_BREAK, providerUnderTest.getEventNumber(4).getType());
        assertEquals(POMODORO, providerUnderTest.getEventNumber(5).getType());
        assertEquals(BIG_BREAK, providerUnderTest.getEventNumber(6).getType());
        assertEquals(POMODORO, providerUnderTest.getEventNumber(7).getType());
        assertEquals(SMALL_BREAK, providerUnderTest.getEventNumber(8).getType());
    }
}