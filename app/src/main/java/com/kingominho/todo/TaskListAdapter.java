package com.kingominho.todo;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskListViewHolder>{


    private ArrayList<Task> mTaskList;
    private Context context;
    private OnItemCheckedChange mListener;

    public interface OnItemCheckedChange
    {
        void onItemChecked(int position, boolean isChecked);
        void onDeleteButtonPressed(int position);
    }



    public void setOnItemCheckedChangeListener(OnItemCheckedChange listener)
    {
        mListener = listener;
    }

    public class TaskListViewHolder extends RecyclerView.ViewHolder{

        public TextView taskTitle;
        public CheckBox taskCompletedCheckBox;
        public ImageButton deleteButton;

        public TaskListViewHolder(View itemView)//, final OnItemClickListener listener)
        {
            super(itemView);
            taskTitle = itemView.findViewById(R.id.taskTitle);
            taskCompletedCheckBox = itemView.findViewById(R.id.taskCompletedCheckBox);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            /*taskCompletedCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null)
                    {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                        {
                            listener.onItemChecked(position);
                        }
                    }
                }
            });*/
        }

    }

    public TaskListAdapter(ArrayList<Task> mTaskList, Context context) {
        this.mTaskList = mTaskList;
        this.context = context;
    }

    @NonNull
    @Override
    public TaskListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_list_item, parent, false);
        //TaskListViewHolder evh = new TaskListViewHolder(v, mListener);
        TaskListViewHolder evh = new TaskListViewHolder(v);//, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskListViewHolder holder, final int position) {

        //new LoadTaskAsyncTask((ViewCategory)context, true).execute(true);
        //new LoadTaskAsyncTask((ViewCategory)context, false).execute(false);

        Task currentItem = mTaskList.get(position);

        boolean isCompleted = currentItem.isTaskCompleted();
        holder.taskTitle.setText(currentItem.getTaskTitle());
        holder.taskTitle.setActivated(!isCompleted);
        if(isCompleted)
        {
            holder.taskTitle.setPaintFlags(holder.taskTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else
        {
            holder.taskTitle.setPaintFlags(holder.taskTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
        holder.taskCompletedCheckBox.setChecked(isCompleted);
        holder.taskCompletedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                mListener.onItemChecked(position, isChecked);
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onDeleteButtonPressed(position);
            }
        });
        /*holder.taskCompletedCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemChecked(position);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        if(mTaskList == null)
        {
            return 0;
        }
        return mTaskList.size();
    }

    public void setmTaskList(ArrayList<Task> mTaskList)
    {
        this.mTaskList = mTaskList;
        notifyDataSetChanged();
    }
}
