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

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.nullpointerengineering.android.pomodoro.R;
import com.nullpointerengineering.android.pomodoro.timer.TimerListener;
import com.nullpointerengineering.android.pomodoro.persistence.SqlTaskRepository;
import com.nullpointerengineering.android.pomodoro.model.task.Task;
import com.nullpointerengineering.android.pomodoro.model.task.TaskRepository;
import com.nullpointerengineering.android.pomodoro.persistence.database.DatabaseConstants;
import com.nullpointerengineering.android.pomodoro.timer.CountdownTimer;
import com.nullpointerengineering.android.pomodoro.view.widgets.TimerFace;

/**
 * Created with IntelliJ IDEA.
 * User: Stratos
 * Date: 22/02/13
 * Time: 3:26 PM
 * The activity that holds a timer and the task being worked at.
 */


public class Pomodoro extends Activity implements TimerListener {

    private static boolean D = true;
    private static String  TAG = "Pomodoro_activity";

    private TimerFace timerFace;
    private CountdownTimer timer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //timer setup
        super.onCreate(savedInstanceState);
        if (D) Log.d(TAG, "onCreate");
        setContentView(R.layout.pomodoro);

        timerFace = (TimerFace) findViewById(R.id.timer_face);
        timerFace.setOnClickListener( new View.OnClickListener(){
            public void onClick(View v){
                if (D) Log.d(TAG, "face clicked");
                //Clicks on the view prompt the TimerController for action.
            }
        });
        TextView taskLabel = (TextView) findViewById(R.id.task_label);
        taskLabel.setText(getString(R.string.working_on));

        TextView taskTitle = (TextView) findViewById(R.id.task_title);
        long taskId = getIntent().getExtras().getLong(DatabaseConstants.TASK_KEY_ID);
        TaskRepository repository = new SqlTaskRepository(this);
        Task task = repository.findTaskById(taskId);
        taskTitle.setText(task.getTitle());

        timer = CountdownTimer.getInstance();
        timer.registerListener(this);
        timer.start();
    }

    @Override
    public void timerUpdate(final int time){
        if (time == 0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (D) Log.d(TAG, "Timer is finished");
                    timerFace.setTime(time);
                }
            });
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (D) Log.d(TAG, "Updating dial to " + time);
                timerFace.setTime(time);
            }
        });
    }

    @Override
    public void onPause(){
        super.onPause();
        if (D) Log.d(TAG, "onPause");
        timer.stop();
        timer.deregisterListener(this);
    }
}
