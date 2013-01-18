package com.nullpointerengineering.android.pomodoro.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import com.nullpointerengineering.android.pomodoro.R;

/**
 * Created with IntelliJ IDEA.
 * User: Stratos
 * Date: 01/01/13
 * Time: 1:24 PM
 * Activity to edit the application's preferences.
 */

public class PreferencesEditor  extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //noinspection deprecation
        addPreferencesFromResource(R.xml.preferences);
    }
}
