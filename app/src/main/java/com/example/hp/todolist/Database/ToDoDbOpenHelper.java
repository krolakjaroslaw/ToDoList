package com.example.hp.todolist.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.hp.todolist.Model.ToDoTask;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class ToDoDbOpenHelper extends OrmLiteSqliteOpenHelper{

    public static final String DATABASE_NAME = "todo.db";
    public static final int DATABASE_VERSION = 2;

    public ToDoDbOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, ToDoTask.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        if (oldVersion == 1) {
            database.execSQL("ALTER TABLE todo_task ADD COLUMN reminder BOOLEAN NOT NULL DEFAULT 0");
            database.execSQL("ALTER TABLE todo_task ADD COLUMN reminderDate TEXT");
        }
    }
}
