package com.example.hp.todolist.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.hp.todolist.Database.ITaskDatabase;
import com.example.hp.todolist.Database.SqliteTaskDatabase;
import com.example.hp.todolist.NotificationsPlanner;

public class BootCompleteReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        ITaskDatabase taskDatabase = new SqliteTaskDatabase(context);

        new NotificationsPlanner(taskDatabase, context).planNotifications();
    }
}
