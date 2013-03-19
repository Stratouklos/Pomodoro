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

import org.joda.time.DateTime;
import org.joda.time.Duration;

/**
 * Created with IntelliJ IDEA.
 * User: Stratos
 * Date: 17/03/13
 * Time: 12:42 PM
 * Event domain object.
 */
public class Event {

    private final long id;
    private final Type type;
    private final Duration totalDuration;
    private final Duration actualDuration;
    private final DateTime timeCreated;

    public enum Type {
        POMODORO,
        BREAK;

        public String toString() {
            switch (this) {
                case POMODORO:
                    return "pomodoro";
                case BREAK:
                    return "break";
                default:
                    return "pomodoro";
            }
        }

        private static Type getType(String typeName){
            if ( typeName.toLowerCase().equals("pomodoro")) {
                return POMODORO;
            } else if (typeName.toLowerCase().equals("break")) {
                return BREAK;
            } else {
                throw new IllegalArgumentException("Illegal typeString " + typeName + " is not valid." );
            }
        }
    }

    Event(long id, long timeCreatedInMillis, String typeString, long totalDurationInMillis, long actualDurationInMillis) {
        if (id < 0) throw new IllegalArgumentException("Illegal id " + id);
        if (timeCreatedInMillis < 0) throw new IllegalArgumentException("Illegal crated time " + timeCreatedInMillis);

        this.id = id;
        timeCreated = new DateTime(timeCreatedInMillis * 1000);
        type = Type.getType(typeString);
        totalDuration = getDuration(totalDurationInMillis);
        actualDuration = getDuration(actualDurationInMillis);
    }

    private Duration getDuration(long duration){
        if (duration > 0) return new Duration(duration);
        else throw new IllegalArgumentException("Illegal duration argument " + duration);
    }

    public long getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public Duration getTotalDuration() {
        return totalDuration;
    }

    public Duration getActualDuration() {
        return actualDuration;
    }

    public DateTime getTimeCreated() {
        return timeCreated;
    }
}
