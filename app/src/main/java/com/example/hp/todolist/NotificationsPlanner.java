package com.example.hp.todolist;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.hp.todolist.Database.ITaskDatabase;
import com.example.hp.todolist.Model.ToDoTask;
import com.example.hp.todolist.Service.ToDoNotificationService;

import java.util.Date;
import java.util.List;

public class NotificationsPlanner {
    private ITaskDatabase mTaskDatabase;
    private Context mContext;

    public NotificationsPlanner(ITaskDatabase mTaskDatabase, Context mContext) {
        this.mTaskDatabase = mTaskDatabase;
        this.mContext = mContext;
    }

    public void planNotifications() {
        // 1. pobrac powiadomienia, ktore maja wlaczane przypomnienie - ale tylko z czasem pozniejszym niz teraz
        List<ToDoTask> tasks = mTaskDatabase.getFutureTasksWithReminder(new Date());

        // 2. dla tych zadan zaplanowac za pomoca AlarmManagera uruchomienie uslugi ToDoNotificationService
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        for (ToDoTask task : tasks) {
            Intent serviceIntent = new Intent(mContext, ToDoNotificationService.class);
            serviceIntent.putExtra("id", task.getId());
            PendingIntent pendingIntent = PendingIntent.getService(mContext, task.getId(), serviceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                alarmManager.set(AlarmManager.RTC_WAKEUP, task.getReminderDate().getTime(), pendingIntent);
            }
            else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, task.getReminderDate().getTime(), pendingIntent);
            }
        }
    }
}
