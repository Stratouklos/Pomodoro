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

package com.nullpointerengineering.android.pomodoro.persistence;

import android.test.AndroidTestCase;
import com.nullpointerengineering.android.pomodoro.model.event.Event;
import com.nullpointerengineering.android.pomodoro.model.event.EventImpl;
import com.nullpointerengineering.android.pomodoro.model.event.EventRepository;
import org.joda.time.Duration;

import static com.nullpointerengineering.android.pomodoro.model.event.Event.Type.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stratos
 * Date: 18/03/13
 * Time: 9:52 PM
 * Event repository tests
 */
public class EventRepositoryTests extends AndroidTestCase {

    private EventRepository repository;
    private long eventId;

    private static final long TOTAL_TIME = 25 * 60 * 1000;
    private static final long ACTUAL_TIME = 15 * 60 * 1000;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        repository = new SqlEventRepository(getContext());
    }

    @Override
    protected void tearDown(){
        repository.deleteEvent(eventId);
    }

    public void testCreatedEvent() {
        eventId = repository.createEvent(POMODORO, TOTAL_TIME).getId();
        assertTrue("Event id was expected to be above 0", (eventId > 0));
    }

    public void testFindEventById() {
        eventId = repository.createEvent(POMODORO, TOTAL_TIME).getId();
        Event event = repository.findEventById(eventId);
        assertEquals(POMODORO, event.getType());
        assertEquals(new Duration(0), event.getActualDuration());
        assertEquals(new Duration(TOTAL_TIME), event.getTotalDuration());
    }

    public void testSaveEvent() {
        eventId = repository.createEvent(POMODORO, TOTAL_TIME).getId();
        Event event = repository.findEventById(eventId);
        Event savedEvent = new EventImpl(
                event.getId(), event.getTimeCreated().getMillis(), SMALL_BREAK, TOTAL_TIME , ACTUAL_TIME);
        repository.saveEvent(savedEvent);
        event = repository.findEventById(eventId);
        assertEquals(SMALL_BREAK, event.getType());
    }

}
