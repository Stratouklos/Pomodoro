package com.nullpointerengineering.android.pomodoro.utilities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;
import com.jayway.android.robotium.solo.Solo;
import com.nullpointerengineering.android.pomodoro.R;
import com.nullpointerengineering.android.pomodoro.activities.TaskManager;

/**
 * Created with IntelliJ IDEA.
 * User: Stratos
 * Date: 24/02/13
 * Time: 3:10 PM
 * Eula related tests
 */
public class EulaTest extends ActivityInstrumentationTestCase2<TaskManager> {

    private static final int VERSION = 1;
    private static final String EULA_KEY = Eula.EULA_PREFIX + VERSION;

    private String  eulaText;

    private Activity activity;
    private Solo solo;

    public EulaTest() {
        super(TaskManager.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        activity = getActivity();
        eulaText = activity.getResources().getString(R.string.eula).substring(0, 200);
    }

    private void restart(boolean shown) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        preferences.edit().putBoolean(EULA_KEY, shown).commit();
        activity.finish();
        setActivity(null);
        activity = getActivity();
        solo = new Solo(getInstrumentation(), getActivity());
        setActivityInitialTouchMode(false);
        getInstrumentation().waitForIdleSync();
    }

    public void testEulaShown(){
        restart(false);
        assertTrue(solo.searchText(eulaText));
    }

    public void testEulaAcceptOK(){
        restart(false);
        solo.clickOnButton(activity.getResources().getString(android.R.string.ok));
        solo.assertCurrentActivity("Task Manager should be running", TaskManager.class);
    }

    public void testEulaReject(){
        restart(false);
        solo.clickOnButton(activity.getResources().getString(android.R.string.cancel));
        getInstrumentation().waitForIdleSync();
        assertTrue(activity.isFinishing());
    }

    public void testEulaNotShown(){
        restart(true);
        assertFalse(solo.searchText(eulaText));
    }

    public void testPackageInfo() {
        PackageInfo info = Eula.getPackageInfo(activity);
        assertEquals(VERSION, info.versionCode);
    }
}