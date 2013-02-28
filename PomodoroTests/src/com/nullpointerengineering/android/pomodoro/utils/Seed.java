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

package com.nullpointerengineering.android.pomodoro.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.nullpointerengineering.android.pomodoro.persistence.database.DatabaseHelper;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Stratos
 * Date: 27/02/13
 * Time: 8:44 PM
 * Seeding utility class for testing
 */
public class Seed {

    private Context context;
    private String table;
    private String[] projection;

    private static final Seed INSTANCE = new Seed();

    private Seed() {
    }

    public static Seed data() {
        return INSTANCE;
    }

    public Seed basedOn(Context context) {
        INSTANCE.context = context;
        return INSTANCE;
    }


    public Seed table(String table) {
        INSTANCE.table = table;
        return INSTANCE;
    }

    public Seed withColumns(String[] projection) {
        INSTANCE.projection = projection;
        return INSTANCE;
    }

    public Seed insert(List<ContentValues> values) {
        SQLiteDatabase db = new DatabaseHelper(context).getWritableDatabase();
        for (ContentValues value : values) {
            db.insert(table, null, value);
        }
        db.close();
        return INSTANCE;
    }

    public Seed clear() {
        SQLiteDatabase db = new DatabaseHelper(context).getWritableDatabase();
        db.execSQL("DELETE from " + table);
        db.close();
        return INSTANCE;
    }
}
