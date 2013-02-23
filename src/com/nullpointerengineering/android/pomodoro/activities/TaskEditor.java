package com.nullpointerengineering.android.pomodoro.activities;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import com.nullpointerengineering.android.pomodoro.R;
import com.nullpointerengineering.android.pomodoro.persistence.Task;
import com.nullpointerengineering.android.pomodoro.persistence.TaskRepository;
import com.nullpointerengineering.android.pomodoro.widgets.NumberPicker;

import static com.nullpointerengineering.android.pomodoro.persistence.database.DatabaseConstants.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stratos
 * Date: 03/06/12
 * Time: 7:01 PM
 * An activity to create/edit tasks.
 */
public class TaskEditor extends Activity {

    private EditText titleText;
    private NumberPicker priorityPicker;
    private NumberPicker estimationPicker;
    private TaskRepository repository;
    private long taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_edit);

        repository = new TaskRepository(this);
        titleText = (EditText) findViewById(R.id.title);
        priorityPicker = (NumberPicker) findViewById(R.id.priority);
        estimationPicker = (NumberPicker) findViewById(R.id.work_units);
        priorityPicker.setRange(PRIORITY_MINIMUM, PRIORITY_MAXIMUM);
        estimationPicker.setRange(WORK_UNIT_MINIMUM, WORK_UNIT_MAXIMUM);
        priorityPicker.setCurrent(PRIORITY_MINIMUM);
        estimationPicker.setCurrent(WORK_UNIT_MINIMUM);
        taskId = getIntent().getExtras().getLong(TASK_KEY_ID);

        Button confirmButton = (Button) findViewById(R.id.confirm);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                saveState();
                finish();
            }
        });
        populateFields();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateFields();
    }

    /* This little trick is to make the background show better */
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGB_565);          //Format and Dither flags for the background gradient.
        window.addFlags(WindowManager.LayoutParams.FLAG_DITHER);
    }

    private void populateFields() {
        if (taskId != INVALID_ID) {
            Task task = repository.findTaskById(taskId);
            titleText.setText(task.getTitle());
            priorityPicker.setCurrent(task.getPriority());
            estimationPicker.setCurrent(task.getEstimate());
        }
    }

    private void saveState() {
        Task task;
        String title = titleText.getText().toString();
        if (TextUtils.isEmpty(title))
            title = getResources().getString(R.string.default_task_title);
        int priority = priorityPicker.getCurrent();
        int estimate = estimationPicker.getCurrent();

        if (taskId == INVALID_ID) {
            task = repository.createTask(title,priority,estimate);
            taskId = task.getId();
        } else {
            task = repository.findTaskById(taskId);
            task.setTitle(title);
            task.setPriority(priority);
            task.setEstimate(estimate);
            repository.saveTask(task);
        }

        Bundle data = getIntent().getExtras();
        data.putLong(TASK_KEY_ID, taskId);
        getIntent().putExtras(data);
    }

    protected final static long   INVALID_ID        = -1;

    @SuppressWarnings("FieldCanBeLocal")
    private static int PRIORITY_MINIMUM     = 1;
    @SuppressWarnings("FieldCanBeLocal")
    private static int PRIORITY_MAXIMUM     = 5;
    @SuppressWarnings("FieldCanBeLocal")
    private static int WORK_UNIT_MINIMUM    = 1;
    @SuppressWarnings("FieldCanBeLocal")
    private static int WORK_UNIT_MAXIMUM    = 7;
}