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

package com.nullpointerengineering.android.pomodoro.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Paint;
import android.view.*;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.nullpointerengineering.android.pomodoro.R;
import org.joda.time.DateTime;

import static com.nullpointerengineering.android.pomodoro.persistence.database.DatabaseConstants.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stratos
 * Date: 06/02/13
 * Time: 3:50 AM
 * Custom cursor adapter to handle displaying the task list
 */
public class TaskCursorAdapter extends SimpleCursorAdapter  {

    private final LayoutInflater layoutInflater;
    private final View.OnClickListener listener;
    private final int layout;
    private final String priorityAmple;
    private final String estimationAmple;
    private final int doneColour;
    private final int notDoneColour;

    public TaskCursorAdapter(Context context, View.OnClickListener clickListener) {
        super( context,
               R.layout.tasks_row,
               null,
               new String[] {TASK_TITLE, TASK_ESTIMATE, TASK_PRIORITY, TASK_DONE_DATE},
               new int[] {R.id.task_title, R.id.task_work_units,R.id.task_priority},
               0
        );

        // Cache the LayoutInflate to avoid asking for a new one each time.
        layoutInflater  = LayoutInflater.from(context);
        layout          = R.layout.tasks_row;
        listener        = clickListener;

        //Eagerly load resources
        Resources resources = context.getResources();
        priorityAmple   = resources.getString(R.string.priority)   + ": ";
        estimationAmple = resources.getString(R.string.work_units) + ": ";
        doneColour = resources.getColor(R.color.MediumSlateBlue);
        notDoneColour = resources.getColor(R.color.Gold);
    }

    @Override
    public View getView(int position, View v, ViewGroup parent){
        if (v ==null){
            v = layoutInflater.inflate(layout, parent, false);
        }

        Cursor cursor = getCursor();
        cursor.moveToPosition(position);

        //Column pointers
        int keyColumn       = cursor.getColumnIndex(TASK_KEY_ID);
        int titleColumn     = cursor.getColumnIndex(TASK_TITLE);
        int priorityColumn  = cursor.getColumnIndex(TASK_PRIORITY) ;
        int workUnitsColumn = cursor.getColumnIndex(TASK_ESTIMATE);
        int timeDoneColumn  = cursor.getColumnIndex(TASK_DONE_DATE);

        //Data
        String title        = cursor.getString(titleColumn);
        String priority     = cursor.getString(priorityColumn);
        String workUnits    = cursor.getString(workUnitsColumn);
        DateTime doneDate   = new DateTime(cursor.getLong(timeDoneColumn));

        //This is an object intentionally
        Long key = cursor.getLong(keyColumn);

        //TextViews & Buttons
        TextView titleView      = (TextView) v.findViewById(R.id.task_title);
        TextView priorityView   = (TextView) v.findViewById(R.id.task_priority);
        TextView estimationView = (TextView) v.findViewById(R.id.task_work_units);

        titleView.setText(title);
        priorityView.setText(priorityAmple + priority);
        estimationView.setText(estimationAmple + workUnits);

        v.setTag(key);

        if (EPOCH.equals(doneDate)){
            //Task is not done yet so set the view accordingly
            titleView.setTextColor(notDoneColour);
            titleView.setPaintFlags(titleView.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            priorityView.setTextColor(notDoneColour);
            priorityView.setPaintFlags(priorityView.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            estimationView.setTextColor(notDoneColour);
            estimationView.setPaintFlags(estimationView.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);

            //Set the whole view clickable to edit the tasks.
            v.setOnClickListener(listener);
        } else {
            //Set to gray and do strike through.
            titleView.setTextColor(doneColour);
            titleView.setPaintFlags(titleView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            priorityView.setTextColor(doneColour);
            priorityView.setPaintFlags(priorityView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            estimationView.setTextColor(doneColour);
            estimationView.setPaintFlags(estimationView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            v.setOnClickListener(null);
        }
        return v;
    }

    private final static DateTime EPOCH = new DateTime(0);
}