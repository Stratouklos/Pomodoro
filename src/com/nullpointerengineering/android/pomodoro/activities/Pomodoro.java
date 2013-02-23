package com.nullpointerengineering.android.pomodoro.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.nullpointerengineering.android.pomodoro.R;
import com.nullpointerengineering.android.pomodoro.persistence.Task;
import com.nullpointerengineering.android.pomodoro.persistence.TaskRepository;
import com.nullpointerengineering.android.pomodoro.persistence.database.DatabaseConstants;
import com.nullpointerengineering.android.pomodoro.widgets.TimerFace;

/**
 * Created with IntelliJ IDEA.
 * User: Stratos
 * Date: 22/02/13
 * Time: 3:26 PM
 * The activity that holds a timer and the task being worked at.
 */


public class Pomodoro extends Activity {

    private TimerFace timerFace;
    private TextView  taskLabel;
    private TextView  taskTitle;
    private Task task;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //Timer setup
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pomodoro);

        timerFace = (TimerFace) findViewById(R.id.timer_face);
        timerFace.setOnClickListener( new View.OnClickListener(){
            public void onClick(View v){
                //Clicks on the view prompt the TimerController for action.
            }
        });

        taskLabel = (TextView) findViewById(R.id.task_label);
        taskLabel.setText(getString(R.string.working_on));

        taskTitle = (TextView) findViewById(R.id.task_title);
        long taskId = getIntent().getExtras().getLong(DatabaseConstants.TASK_KEY_ID);
        TaskRepository repository = new TaskRepository(this);
        task = repository.findTaskById(taskId);

        taskTitle.setText(task.getTitle());
    }
}
