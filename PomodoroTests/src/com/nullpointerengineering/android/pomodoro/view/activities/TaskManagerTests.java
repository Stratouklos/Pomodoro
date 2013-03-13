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

package com.nullpointerengineering.android.pomodoro.view.activities;

import android.content.Context;
import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;
import com.jayway.android.robotium.solo.Solo;
import com.nullpointerengineering.android.pomodoro.persistence.database.DatabaseConstants;
import com.nullpointerengineering.android.pomodoro.utils.BulkTaskMaker;
import com.nullpointerengineering.android.pomodoro.utils.Seed;

import static com.nullpointerengineering.android.pomodoro.utilities.EulaTest.EULA_KEY;

/**
 * Created with IntelliJ IDEA.
 * User: Stratos
 * Date: 28/02/13
 * Time: 7:56 AM
 * Tests for the task manager activity.
  */

public class TaskManagerTests extends ActivityInstrumentationTestCase2<TaskManager> {

    private TaskManager taskManager;
    private Context context;
    private TaskManager activity;
    private Solo solo;

    public TaskManagerTests() {
        super(TaskManager.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setActivityInitialTouchMode(false);
        PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putBoolean(EULA_KEY, true).commit();

        taskManager = getActivity();
        context = taskManager.getApplicationContext();
        activity = getActivity();
        solo = new Solo(getInstrumentation(), activity);

        Seed seed = Seed.data().basedOn(context).table(DatabaseConstants.TABLE_TASKS).clear();
        seed.insert(BulkTaskMaker.taskValues(10, false));
        seed.insert(BulkTaskMaker.taskValues(11, true));
        getInstrumentation().waitForIdleSync();
    }

    public void testListView() {
        solo.clickOnText("Undone test task 1");
        solo.waitForActivity("TaskEditor");
    }




}
