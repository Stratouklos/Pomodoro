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

import android.util.Log;

import static com.google.common.base.Preconditions.*;
import static com.nullpointerengineering.android.pomodoro.model.event.Event.Type.BIG_BREAK;
import static com.nullpointerengineering.android.pomodoro.model.event.Event.Type.POMODORO;
import static com.nullpointerengineering.android.pomodoro.model.event.Event.Type.SMALL_BREAK;

//Provides the next event
public class EventProviderImpl implements EventProvider {

    public static final int MINUTES = 60 * 1000;
    private static final String TAG = EventProviderImpl.class.toString();
    private final EventConfiguration configuration;
    private final EventRepository eventRepository;

    public EventProviderImpl(EventConfiguration configuration, EventRepository eventRepository) {
        this.configuration = checkNotNull(configuration);
        this.eventRepository = checkNotNull(eventRepository);
    }

    @Override
    public synchronized Event getEventNumber(int eventNumber) {
        checkArgument(eventNumber != 0, "For the first event use 1 not 0");
        checkArgument(eventNumber > 0, "Negative event numbers are not acceptable");
        Event.Type typeOfEvent;
        //Even events are always pomodoros
        if (eventNumber % 2 != 0)  typeOfEvent = POMODORO;
        else if ((eventNumber / 2) % configuration.getBigBreakInterval() == 0 ) typeOfEvent = BIG_BREAK;
        else typeOfEvent = SMALL_BREAK;

        Log.d(TAG, "Creating event " + POMODORO);
        return eventRepository.createEvent(typeOfEvent, configuration.getDuration(typeOfEvent) * MINUTES);
    }
}
