package com.example.hp.todolist.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.hp.todolist.R;
import com.example.hp.todolist.Model.ToDoTask;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class ToDoTaskAdapter extends RecyclerView.Adapter<ToDoTaskAdapter.ToDoViewHolder> {
    private List<ToDoTask> mTasks;
    private OnClickListener mClickListener;

    public ToDoTaskAdapter(List<ToDoTask> tasks, OnClickListener clickListener) {
        mTasks = tasks;
        mClickListener = clickListener;
    }

    public void setTasks(List<ToDoTask> tasks) {
        mTasks = tasks;
        notifyDataSetChanged();
    }

    @Override
    public ToDoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {  //ViewGroup klasa nadrzedna dla layoutow
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rowView = inflater.inflate(R.layout.list_item_to_do, parent, false);
        return new ToDoViewHolder(rowView);
    }   // tworzy widoki wierszy dla listy

    @Override
    public void onBindViewHolder(ToDoViewHolder holder, int position) {
        //pobranie elementu danych na pozycji position
        ToDoTask task = mTasks.get(position);
        //uzupelnienie widoku (holder) na podstawie pobranego obiektu
        holder.mBlockListeners = true;  // blokujemy wysylanie powiadomien o zmianie checkboxa i kliknieciu
        holder.mCurrentPosition = task.getId();
        holder.mCurrentTask = task;
        holder.mTitle.setText(task.getName());
        holder.mDone.setChecked(task.isDone());
        holder.mBlockListeners = false; // odblokowujemy powiadomienia, zeby poprawnie reagowac na zdarzenia uzytkownikow
    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }   // ile jest elementow do wyswietlenia na liscie

    public class ToDoViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.task_done)
        CheckBox mDone;
        @BindView(R.id.task_title)
        TextView mTitle;

        ToDoTask mCurrentTask;
        int mCurrentPosition;
        boolean mBlockListeners = true; // true - poniewaz na poczatku kiedy wiersz i jest przed przypisaniem pierwszego zadania
        // nie chcemy zeby uruchamialy sie jakiekolwiek funkcje dotyczace zdarzen (np.OnClick, OnChecked)


        public ToDoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick
        void onItemClick() {
            if (mClickListener != null && !mBlockListeners) {
                mClickListener.onClick(mCurrentTask, mCurrentPosition);
            }
        }

        @OnCheckedChanged(R.id.task_done)
        void onCheckedChange(boolean checked) {
            if (mClickListener != null && !mBlockListeners) {
                mClickListener.onTaskDoneChanged(mCurrentTask, mCurrentPosition, checked);
            }
        }
    }

    public interface OnClickListener {
        void onClick(ToDoTask task, int position);

        void onTaskDoneChanged(ToDoTask task, int position, boolean isDone);
    }
}

