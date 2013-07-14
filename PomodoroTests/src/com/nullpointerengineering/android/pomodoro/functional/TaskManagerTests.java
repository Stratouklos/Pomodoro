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

package com.nullpointerengineering.android.pomodoro.functional;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;
import com.jayway.android.robotium.solo.Solo;
import com.nullpointerengineering.android.pomodoro.R;
import com.nullpointerengineering.android.pomodoro.eula.Eula;
import com.nullpointerengineering.android.pomodoro.utils.BulkTaskMaker;
import com.nullpointerengineering.android.pomodoro.view.activities.TaskEditor;
import com.nullpointerengineering.android.pomodoro.view.activities.TaskManager;


/**
 * Created with IntelliJ IDEA.
 * User: Stratos
 * Date: 28/02/13
 * Time: 7:56 AM
 * Tests for the task manager activity.
  */

public class TaskManagerTests extends ActivityInstrumentationTestCase2<TaskManager> {
    private static final int VERSION = 1;
    public static final String EULA_KEY = Eula.EULA_PREFIX + VERSION;

    private String  eulaText;
    private BulkTaskMaker taskMaker;
    private Activity activity;
    private Solo solo;


    public TaskManagerTests() {
        super(TaskManager.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setActivityInitialTouchMode(false);
        PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putBoolean(EULA_KEY, false).commit();
        TaskManager taskManager = getActivity();
        Context context = taskManager.getApplicationContext();
        activity = getActivity();
        solo = new Solo(getInstrumentation(), activity);
        taskMaker = new BulkTaskMaker(context);
        getInstrumentation().waitForIdleSync();
        activity = getActivity();
        eulaText = activity.getResources().getString(R.string.eula).substring(0, 200);
    }

    private void restart(boolean showEULA) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        preferences.edit().putBoolean(EULA_KEY, !showEULA).commit();
        activity.finish();
        setActivity(null);
        activity = getActivity();
        solo = new Solo(getInstrumentation(), getActivity());
        setActivityInitialTouchMode(false);
        getInstrumentation().waitForIdleSync();
    }

    public void testEulaShown(){
        restart(true);
        assertTrue(solo.searchText(eulaText));
    }

    public void testEulaAcceptOK(){
        restart(true);
        solo.clickOnButton(activity.getResources().getString(android.R.string.ok));
        solo.assertCurrentActivity("Task Manager should be running", TaskManager.class);
    }

    public void testEulaReject(){
        restart(true);
        solo.clickOnButton(activity.getResources().getString(android.R.string.cancel));
        getInstrumentation().waitForIdleSync();
        assertTrue(activity.isFinishing());
    }


    public void testEulaNotShown(){
        restart(false);
        assertFalse(solo.searchText(eulaText));
    }

    public void testPackageInfo() {
        PackageInfo info = Eula.getPackageInfo(activity);
        assertEquals(VERSION, info.versionCode);
    }

    public void testListView() {
        taskMaker.taskValues(5, false);
        restart(false);
        solo.clickOnText("Undone test task 1");
        solo.waitForActivity("TaskEditor");
        solo.assertCurrentActivity("Didn't find Expected activity TaskEditor", TaskEditor.class);
    }
}
