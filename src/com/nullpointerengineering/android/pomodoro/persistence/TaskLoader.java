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

package com.nullpointerengineering.android.pomodoro.persistence;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;

import static com.nullpointerengineering.android.pomodoro.persistence.database.DatabaseConstants.*;
import static com.nullpointerengineering.android.pomodoro.persistence.database.DatabaseConstants.TASK_DONE_DATE;
import static com.nullpointerengineering.android.pomodoro.persistence.database.TaskProvider.CONTENT_URI;

/**
 * Created with IntelliJ IDEA.
 * User: Stratos
 * Date: 19/02/13
 * Time: 9:17 PM
 * Task list loader
 */
public class TaskLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    private SimpleCursorAdapter adapter;
    private final Context context;
    private Cursor cursor;
    private String selection;

    public static TaskLoader getLoader(Context context){
        return new TaskLoader(context);
    }

    private TaskLoader(Context context) {
        this.context = context;
        this.adapter = null;
        this.cursor = null;
        this.selection = null;
    }

    public void setAdapter(SimpleCursorAdapter adapter) {
        this.adapter = adapter;
    }

    public void setSelection(String selection){
        this.selection = selection;
    }

    public Cursor getCursor(){
        return cursor;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = { TASK_KEY_ID, TASK_TITLE, TASK_ESTIMATE, TASK_PRIORITY, TASK_DONE_DATE};
        return new CursorLoader(context, CONTENT_URI, projection, selection, null, "done_on ASC, priority ASC");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        this.cursor = null;
        if (adapter != null) adapter.swapCursor(cursor);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        this.cursor = cursor;
        if (adapter != null) adapter.swapCursor(cursor);
    }
}