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

package com.nullpointerengineering.android.pomodoro.utils;

import android.content.ContentValues;
import com.nullpointerengineering.android.pomodoro.persistence.database.DatabaseConstants;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Stratos
 * Date: 28/02/13
 * Time: 8:07 AM
 * Static util methods to make lists of task content values.
 */

public class BulkTaskMaker {

    public static List<ContentValues> taskValues(int howMany, boolean done) {
        String doneness  = done ? "Done" : "Undone";

        List<ContentValues> valuesList = new LinkedList<ContentValues>();

        for (int i = 0; i < howMany; i++) {
            ContentValues values = new ContentValues();
            values.put(DatabaseConstants.TASK_TITLE,  doneness + " test task " + i );
            values.put(DatabaseConstants.TASK_PRIORITY, i + 1 % 5);
            values.put(DatabaseConstants.TASK_ESTIMATE, i + 1 % 5);
            if (done) {
                values.put(DatabaseConstants.TASK_DONE_DATE, System.currentTimeMillis() - i * (1000 * 60 * 60 ));
                values.put(DatabaseConstants.TASK_ACTUAL, i + 1 % 10);
            }
            valuesList.add(values);
        }
        return valuesList;
    }


}
