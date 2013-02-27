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

package com.nullpointerengineering.android.pomodoro.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import com.nullpointerengineering.android.pomodoro.R;

/*
 * Created by IntelliJ IDEA.
 * User: Stratos
 * Date: 10/13/11
 * Time: 5:15 PM
 *
 * Taken from directly from: https://github.com/Agilevent/Eula-Sample
 */


public final class Eula {

    protected  static final String EULA_PREFIX = "eula_";

    public static void show(final Activity activity) {
        PackageInfo versionInfo = getPackageInfo(activity);

        final String eulaKey = EULA_PREFIX + versionInfo.versionCode;
        // the eulaKey changes every time you increment the version number in the AndroidManifest.xml
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        boolean hasBeenShown = prefs.getBoolean(eulaKey, false);
        if(!hasBeenShown){
            // Show the Eula
            String title = activity.getString(R.string.app_name) + " v" + versionInfo.versionName;

            //Includes the updates as well so users know what changed.
            String message = activity.getString(R.string.updates) + "\n\n" + activity.getString(R.string.eula);

            AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok, new Dialog.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Mark this version as read.
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putBoolean(eulaKey, true);
                            editor.commit();
                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new Dialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Close the activity as they have declined the EULA
                            activity.finish();
                        }
                    });
            builder.create().show();
        }
    }

    protected static PackageInfo getPackageInfo(Activity activity) {
        PackageInfo pi = null;
        try {
            pi = activity.getPackageManager().getPackageInfo(activity.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return pi;
    }
}