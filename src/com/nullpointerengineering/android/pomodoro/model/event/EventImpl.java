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

import org.joda.time.DateTime;
import org.joda.time.Duration;

import static com.google.common.base.Preconditions.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stratos
 * Date: 17/03/13
 * Time: 12:42 PM
 * Event domain object.
 */
public class EventImpl implements Event {

    private final long id;
    private final Event.Type type;
    private final Duration totalDuration;
    private final Duration actualDuration;
    private final DateTime timeCreated;

    public static final String BAD_TIME_ARGUMENT = "Time cannot be negative";

    public EventImpl(long id, long timeCreatedInMillis, Event.Type type, long totalDurationInMillis, long actualDurationInMillis) {
        checkArgument(id >= 0, "Illegal id " + id);
        checkArgument(timeCreatedInMillis >= 0, BAD_TIME_ARGUMENT);
        checkArgument(totalDurationInMillis >= 0, BAD_TIME_ARGUMENT);
        checkArgument(actualDurationInMillis >= 0,  BAD_TIME_ARGUMENT);

        this.id = id;
        timeCreated = new DateTime(timeCreatedInMillis * 1000);
        this.type = type;
        totalDuration = new Duration(totalDurationInMillis);
        actualDuration = new Duration(actualDurationInMillis);
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public Duration getTotalDuration() {
        return totalDuration;
    }

    @Override
    public Duration getActualDuration() {
        return actualDuration;
    }

    @Override
    public DateTime getTimeCreated() {
        return timeCreated;
    }
}
