package com.nullpointerengineering.android.pomodoro.activities;


import android.os.Bundle;
import com.google.inject.Inject;
import com.nullpointerengineering.android.pomodoro.R;
import com.nullpointerengineering.android.pomodoro.utilities.Eula;
import roboguice.activity.RoboActivity;

public class TaskManager extends RoboActivity {

    @Inject
    Eula eula;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pomodoro);

        eula.show(this);

//        ListView list = getListView();
//        registerForContextMenu(list);
 //       list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
 //           @Override
 //           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
 //               editTaskActivity((Long) view.getTag());
/*
            }
        });

        //On clickers
        ( findViewById(R.id.PlayBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO implement
            }
        });

        ( findViewById(R.id.AddTaskBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTaskActivity(0);
            }
        });

        ( findViewById(R.id.PreferenceBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO implement
            }
        });

        ( findViewById(R.id.StatisticsBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO implement
            }
        });

*/
    }

}
