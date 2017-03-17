package com.example.hp.todolist.Database;

import com.example.hp.todolist.Model.ToDoTask;

import java.util.Date;
import java.util.List;

public interface ITaskDatabase {
    List<ToDoTask> getTasks();

    List<ToDoTask> getFutureTasksWithReminder(Date now);

    ToDoTask getTask(int position);

    void addTask(ToDoTask task);

    void updateTask(ToDoTask task, int position);
}
