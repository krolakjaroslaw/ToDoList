package com.example.hp.todolist.Service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import com.example.hp.todolist.Activity.TaskPreviewActivity;
import com.example.hp.todolist.Database.ITaskDatabase;
import com.example.hp.todolist.Database.SqliteTaskDatabase;
import com.example.hp.todolist.Model.ToDoTask;
import com.example.hp.todolist.R;

public class ToDoNotificationService extends IntentService {
    private ITaskDatabase mTaskDatabase;

    public ToDoNotificationService() {
        super("ToDoNotificationService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mTaskDatabase = new SqliteTaskDatabase(this);   // dostep do bazy danych
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int taskId = intent.getIntExtra("id", -1);
        ToDoTask task = mTaskDatabase.getTask(taskId);

        if (task == null) {
            return;
        }

        Intent previewIntent = new Intent(this, TaskPreviewActivity.class);
        previewIntent.putExtra("pos", taskId);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, taskId, previewIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(task.getName())
                .setContentText("Przypominacz")
                .setDefaults(android.support.v4.app.NotificationCompat.DEFAULT_ALL)
                .setTicker(task.getName())
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(taskId, notification);
    }
}
