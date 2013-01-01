package com.nullpointerengineering.android.pomodoro.activities;

import android.os.Bundle;
import com.nullpointerengineering.android.pomodoro.R;
import roboguice.activity.RoboPreferenceActivity;

/**
 * Created with IntelliJ IDEA.
 * User: Stratos
 * Date: 01/01/13
 * Time: 1:24 PM
 * Activity to edit the application's preferences.
 */

public class PreferencesEditor  extends RoboPreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
