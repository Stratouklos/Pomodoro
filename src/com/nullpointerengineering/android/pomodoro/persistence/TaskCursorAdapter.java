package com.nullpointerengineering.android.pomodoro.persistence;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.nullpointerengineering.android.pomodoro.R;

import static com.nullpointerengineering.android.pomodoro.persistence.database.DatabaseConstants.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stratos
 * Date: 06/02/13
 * Time: 3:50 AM
 * Custom cursor adapter to handle displaying the task list
 */
public class TaskCursorAdapter extends SimpleCursorAdapter {

    private final LayoutInflater layoutInflater;
    private final View.OnClickListener listener;
    private final int layout;
    private final int taskColor;
    private final String priorityAmple;
    private final String estimationAmple;

    public TaskCursorAdapter(Context context, View.OnClickListener clickListener) {
        super(
                context,
                R.layout.tasks_row,
                null,
                new String[] {TASK_TITLE, TASK_ESTIMATE, TASK_PRIORITY},
                new int[] {R.id.task_title, R.id.task_work_units,R.id.task_priority},
                0
        );

        // Cache the LayoutInflate to avoid asking for a new one each time.
        layoutInflater  = LayoutInflater.from(context);
        layout          = R.layout.tasks_row;
        listener        = clickListener;
        taskColor       = context.getResources().getColor(R.color.Gold);
        priorityAmple   = context.getResources().getString(R.string.priority)   + ": ";
        estimationAmple = context.getResources().getString(R.string.work_units) + ": ";
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

        //Data
        String title        = cursor.getString(titleColumn);
        String priority     = cursor.getString(priorityColumn);
        String workUnits    = cursor.getString(workUnitsColumn);

        //This is an object intentionally
        Long key = cursor.getLong(keyColumn);

        //TextViews & Buttons
        TextView titleView      = (TextView) v.findViewById(R.id.task_title);
        TextView priorityView   = (TextView) v.findViewById(R.id.task_priority);
        TextView estimationView = (TextView) v.findViewById(R.id.task_work_units);

        v.setOnClickListener(listener);
        v.setClickable(true);
        v.setTag(key);

        titleView.setText(title);
        titleView.setTextColor(taskColor);
        priorityView.setText(priorityAmple + priority);
        priorityView.setTextColor(taskColor);
        estimationView.setText(estimationAmple + workUnits);
        estimationView.setTextColor(taskColor);

        return v;
    }

}
