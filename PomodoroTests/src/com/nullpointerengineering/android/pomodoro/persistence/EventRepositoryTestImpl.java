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

import com.google.common.collect.Maps;
import com.nullpointerengineering.android.pomodoro.model.event.Event;
import com.nullpointerengineering.android.pomodoro.model.event.EventImpl;
import com.nullpointerengineering.android.pomodoro.model.event.EventRepository;

import java.util.Map;

//Implementation to be used in tests due to issues with Mockito.
public class EventRepositoryTestImpl implements EventRepository {

    private final Map<Long, Event> createdEventsMap = Maps.newHashMap();
    private long count;


    @Override
    public synchronized Event createEvent(Event.Type type, long totalDurationInMillis) {
        count++;
        Event newEvent = new EventImpl(count, System.currentTimeMillis(), type, totalDurationInMillis, 0);
        createdEventsMap.put(count, newEvent);
        return newEvent;
    }

    @Override
    public synchronized Event findEventById(long id) {
        return createdEventsMap.get(id);
    }

    @Override
    public synchronized void saveEvent(Event event) {
        createdEventsMap.remove(event.getId());
        createdEventsMap.put(event.getId(), event);
    }

    @Override
    public synchronized int deleteEvent(long id) {
        return createdEventsMap.remove(id) != null ? 1 : 0;
    }
}
