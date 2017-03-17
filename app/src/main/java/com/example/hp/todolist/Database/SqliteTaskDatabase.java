package com.example.hp.todolist.Database;

import android.content.Context;

import com.example.hp.todolist.Model.ToDoTask;
import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class SqliteTaskDatabase implements ITaskDatabase{

    private Dao<ToDoTask, Integer> mDao;

    public SqliteTaskDatabase(Context context) {
        ToDoDbOpenHelper doHelper = new ToDoDbOpenHelper(context);
        ConnectionSource cs = new AndroidConnectionSource(doHelper);
        try {
            mDao = DaoManager.createDao(cs, ToDoTask.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ToDoTask> getTasks() {
        try {
            return mDao.queryBuilder()
                    .orderBy("done", true)
                    .orderBy("dateCreated", false)
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @Override
    public List<ToDoTask> getFutureTasksWithReminder(Date now) {
        try {
            return mDao.queryBuilder()
                    .where().eq("reminder", true)
                    .and().ge("reminderDate", now)
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @Override
    public ToDoTask getTask(int position) {
        try {
            return mDao.queryForId(position);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addTask(ToDoTask task) {
        try {
            mDao.create(task);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateTask(ToDoTask task, int position) {
        try {
            mDao.update(task);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
