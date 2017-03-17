package com.example.hp.todolist.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.example.hp.todolist.Database.ITaskDatabase;
import com.example.hp.todolist.NotificationsPlanner;
import com.example.hp.todolist.R;
import com.example.hp.todolist.Database.SqliteTaskDatabase;
import com.example.hp.todolist.Model.ToDoTask;
import com.example.hp.todolist.Adapter.ToDoTaskAdapter;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ToDoListActivity extends AppCompatActivity implements ToDoTaskAdapter.OnClickListener {
    @BindView(R.id.task_list)
    RecyclerView mToDoList;

    private ITaskDatabase mTaskDatabase;
    private ToDoTaskAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);
        ButterKnife.bind(this);
        mTaskDatabase = new SqliteTaskDatabase(this);

        //1. w jakim ukladzie maja wyswietlac sie elementy listy
        //uklad pionowy
        mToDoList.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new ToDoTaskAdapter(mTaskDatabase.getTasks(), this);
        mToDoList.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshListData();        // odswiezanie listy po spauzowaniu aplikacji lub po przejsciu do innego ekranu

        new NotificationsPlanner(mTaskDatabase, this).planNotifications();
    }

    private void refreshListData() {
        mAdapter.setTasks(mTaskDatabase.getTasks());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_todolist, menu);
        return super.onCreateOptionsMenu(menu);
    }   // pozwala na ustawienie pozycji w menu gornym

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_item_create) {
            Intent createTaskIntent = new Intent(this, TaskCreateActivity.class);
            startActivity(createTaskIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }   // reaguje na elementy w menu

    @Override
    public void onClick(ToDoTask task, int position) {
        Intent createTaskIntent = new Intent(this, TaskCreateActivity.class);
        createTaskIntent.putExtra("pos", position);
        startActivity(createTaskIntent);
    }

    @Override
    public void onTaskDoneChanged(ToDoTask task, int position, boolean isDone) {
        task.setDone(isDone);
        task.setDateCreated(new Date());
        mTaskDatabase.updateTask(task, position);
        refreshListData();
    }
}
