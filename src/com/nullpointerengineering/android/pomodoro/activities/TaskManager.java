package com.nullpointerengineering.android.pomodoro.activities;


import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.nullpointerengineering.android.pomodoro.R;
import com.nullpointerengineering.android.pomodoro.persistence.Task;
import com.nullpointerengineering.android.pomodoro.persistence.TaskCursorAdapter;
import com.nullpointerengineering.android.pomodoro.persistence.TaskRepository;
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

        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                editTaskActivity((Long) view.getTag());
            }
        });
        // Now create a custom cursor adapter and set it to display
        adapter = new TaskCursorAdapter(this,
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        editTaskActivity((Long) view.getTag());
                    }
                });

        setListAdapter(adapter);

        /*list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast hello = Toast.makeText(view.getContext(), "hello", 30);
                hello.show();
            }
        });  */

        getLoaderManager().initLoader(0, null, this);

        //On clickers
        Button playButton = (Button) findViewById(R.id.PlayBtn);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast hello = Toast.makeText(view.getContext(), "Play", 30);
                hello.show();
            }
        });

        Button addTaskButton = (Button) findViewById(R.id.AddTaskBtn);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTaskActivity(TaskEditor.INVALID_ID);
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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DONE_ID, 1, R.string.menu_done);
        menu.add(0, EDIT_ID, 1 , R.string.edit_note);
        menu.add(0, DELETE_ID, 1, R.string.menu_delete);
        menu.add(0, CANCEL_ID, 1, R.string.cancel);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        TaskRepository repository = new TaskRepository(this);
        switch(item.getItemId()) {
            case DONE_ID:
                Task task = repository.findTaskById(info.id);
                task.setDone(!task.isDone());
                repository.saveTask(task);
                return true;
            case EDIT_ID:
                editTaskActivity(info.id);
                return true;
            case DELETE_ID:
                repository.deleteTask(info.id);
                return true;
            case CANCEL_ID:
                return true;
        }
        return super.onContextItemSelected(item);
    }


    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = { TASK_KEY_ID, TASK_TITLE, TASK_ESTIMATE, TASK_PRIORITY, TASK_DONE_DATE};
        return new CursorLoader(TaskManager.this, CONTENT_URI, projection, null, null, "done_on ASC, priority ASC");
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
        bundle.putLong(TaskEditor.TASK_KEY_ID, id);
        i.putExtras(bundle);
        startActivityForResult(i, ACTIVITY_EDIT);
    }

    private static final int    ACTIVITY_EDIT       = 1;
    private static final int    DONE_ID             = Menu.FIRST;
    private static final int    EDIT_ID             = Menu.FIRST + 1;
    private static final int    DELETE_ID           = Menu.FIRST + 2;
    private static final int    CANCEL_ID           = Menu.FIRST + 3;


}