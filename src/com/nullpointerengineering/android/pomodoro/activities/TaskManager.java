package com.nullpointerengineering.android.pomodoro.activities;

import android.app.*;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.nullpointerengineering.android.pomodoro.persistence.TaskLoader;
import com.nullpointerengineering.android.pomodoro.persistence.TaskRepository;
import com.nullpointerengineering.android.pomodoro.persistence.database.DatabaseConstants;
import com.nullpointerengineering.android.pomodoro.utilities.Eula;

import static com.nullpointerengineering.android.pomodoro.persistence.database.DatabaseConstants.TASK_KEY_ID;

public class TaskManager extends ListActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private TaskLoader undoneTaskLoader;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_manager);

        Eula.show(this);

        ListView list = getListView();
        registerForContextMenu(list);
        list.setOnItemClickListener(this);

        // Now create a custom cursor adapter and set it to display
        TaskCursorAdapter adapter = new TaskCursorAdapter(this, new TaskListener());
        setListAdapter(adapter);
        TaskLoader visibleTaskLoader = TaskLoader.getLoader(this);
        visibleTaskLoader.setAdapter(adapter);
        getLoaderManager().initLoader(VISIBLE_TASK_LOADER, null, visibleTaskLoader);

        undoneTaskLoader = TaskLoader.getLoader(this);
        undoneTaskLoader.setSelection(DatabaseConstants.TASK_DONE_DATE + " == 0");
        getLoaderManager().initLoader(UNDONE_TASK_LOADER, null, undoneTaskLoader);

        //On clickers
        Button playButton = (Button) findViewById(R.id.PlayBtn);
        playButton.setOnClickListener(this);
        Button addTaskButton = (Button) findViewById(R.id.AddTaskBtn);
        addTaskButton.setOnClickListener(this);
        Button preferencesButton = (Button) findViewById(R.id.PreferenceBtn);
        preferencesButton.setOnClickListener(this);
        Button statisticsButton = (Button) findViewById(R.id.PreferenceBtn);
        statisticsButton.setOnClickListener(this);
    }

    private class TaskListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            editTaskActivity((Long) view.getTag());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.PlayBtn:
                AlertDialog.Builder dialog= new AlertDialog.Builder(this);
                dialog.setTitle(getString(R.string.select_task));
                final Cursor cursor = undoneTaskLoader.getCursor();
                dialog.setCursor(cursor ,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        cursor.moveToPosition(i);
                        long id = cursor.getLong(cursor.getColumnIndex(TASK_KEY_ID));
                        pomodoroActivity(id);
                        dialogInterface.dismiss();
                    }
                }, DatabaseConstants.TASK_TITLE);
                dialog.show();
                break;
            case R.id.AddTaskBtn:
                editTaskActivity(TaskEditor.INVALID_ID);
                break;
            case R.id.PreferenceBtn:
                Intent i = new Intent(view.getContext(),PreferencesEditor.class);
                startActivity(i);
                break;
            case R.id.StatisticsBtn:
                //TODO implement
                break;
            default:
                throw new IllegalArgumentException("Unknown view!" + view.getTag());
        }
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
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        editTaskActivity((Long) view.getTag());
    }

    //Activity launchers
    private void editTaskActivity(long id) {
        Intent i = new Intent(this, TaskEditor.class);
        Bundle bundle = new Bundle();
        bundle.putLong(TASK_KEY_ID, id);
        i.putExtras(bundle);
        startActivityForResult(i, ACTIVITY_EDIT);
    }

    private void pomodoroActivity(long id){
        Intent i = new Intent(this, Pomodoro.class);
        Bundle bundle = new Bundle();
        bundle.putLong(TASK_KEY_ID, id);
        i.putExtras(bundle);
        startActivity(i);
    }


    private static final int    ACTIVITY_EDIT       = 1;
    private static final int    DONE_ID             = Menu.FIRST;
    private static final int    EDIT_ID             = Menu.FIRST + 1;
    private static final int    DELETE_ID           = Menu.FIRST + 2;
    private static final int    CANCEL_ID           = Menu.FIRST + 3;
    private static final int    VISIBLE_TASK_LOADER = 0;
    private static final int    UNDONE_TASK_LOADER  = 1;
}