package com.nullpointerengineering.android.pomodoro.activities;


import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.*;
import com.nullpointerengineering.android.pomodoro.R;
import com.nullpointerengineering.android.pomodoro.persistence.TaskCursorAdapter;
import com.nullpointerengineering.android.pomodoro.utilities.Eula;

import static com.nullpointerengineering.android.pomodoro.persistence.database.DatabaseConstants.*;
import static com.nullpointerengineering.android.pomodoro.persistence.database.TaskProvider.*;

public class TaskManager extends ListActivity implements   LoaderManager.LoaderCallbacks<Cursor>{

    TaskCursorAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pomodoro);
        Eula eula = new Eula();
        eula.show(this);
        ListView list = getListView();
        registerForContextMenu(list);

        // Now create a custom cursor adapter and set it to display
        adapter = new TaskCursorAdapter(this,
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        editTaskActivity((Long) view.getTag());
                    }
                });


        setListAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast hello = Toast.makeText(view.getContext(), "hello", 30);
                hello.show();
            }
        });

        getLoaderManager().initLoader(0, null, this);

        //On clickers
        Button playButton = (Button) findViewById(R.id.PlayBtn);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast hello = Toast.makeText(view.getContext(), "hello", 30);
                hello.show();
            }
        });

        Button addTaskButton = (Button) findViewById(R.id.AddTaskBtn);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTaskActivity(0);
            }
        });

        Button preferencesButton = (Button) findViewById(R.id.PreferenceBtn);
        preferencesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(),PreferencesEditor.class);
                startActivity(i);
            }
        });

        ( findViewById(R.id.StatisticsBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO implement
            }
        });

    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = { TASK_KEY_ID, TASK_TITLE, TASK_ESTIMATE, TASK_PRIORITY};
        return new CursorLoader(TaskManager.this, CONTENT_URI, projection, null, null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        adapter.swapCursor(null);
    }
    //Activity launchers
    private void editTaskActivity(long id) {
        Intent i = new Intent(this, TaskEditor.class);
        Bundle bundle = new Bundle();
        bundle.putLong(TaskEditor.TASKS_KEY_ID, id);
        i.putExtras(bundle);
        startActivityForResult(i, ACTIVITY_EDIT);
    }

    private static final int    ACTIVITY_CREATE     = 0;
    private static final int    ACTIVITY_EDIT       = 1;
    private static final int    EDIT_ID             = Menu.FIRST;
    private static final int    DELETE_ID           = Menu.FIRST + 1;
    private static final int    CANCEL_ID           = Menu.FIRST + 2;


}