package com.kingominho.todo;

import android.os.AsyncTask;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class LoadTaskAsyncTask extends AsyncTask<Boolean, Integer, ArrayList<Task>> {

    WeakReference<ViewCategory> viewCategoryWeakReference;
    boolean bool;
    TaskListAdapter mAdapter;

    public LoadTaskAsyncTask(ViewCategory viewCategory, boolean bool, TaskListAdapter mAdapter) {
        viewCategoryWeakReference = new WeakReference<>(viewCategory);
        this.bool = bool;
        this.mAdapter = mAdapter;
    }

    @Override
    protected void onPreExecute() {

        ViewCategory viewCategory = viewCategoryWeakReference.get();

        if(viewCategory == null || viewCategory.isFinishing())
        {
            return;
        }
        Toast.makeText(viewCategory.getApplicationContext(), "Loading...", Toast.LENGTH_LONG).show();
    }

    @Override
    protected ArrayList<Task> doInBackground(Boolean... booleans) {
        ViewCategory viewCategory = viewCategoryWeakReference.get();
        if(viewCategory == null || viewCategory.isFinishing())
        {
            return null;
        }
        ArrayList<Task> arrayList = null;
        if(booleans[0].equals(true))
        {
            arrayList = viewCategory.makeList(true);
        }
        else if(booleans[0].equals(false))
        {
            arrayList = viewCategory.makeList(false);
        }
        return arrayList;
    }

    @Override
    protected void onPostExecute(ArrayList<Task> tasks) {

        ViewCategory viewCategory = viewCategoryWeakReference.get();

        if(viewCategory == null || viewCategory.isFinishing())
        {
            return;
        }
        Toast.makeText(viewCategory.getApplicationContext(), "Load Complete", Toast.LENGTH_LONG).show();
        if(bool)
        {
            viewCategory.mTaskListCompleted = tasks;
            //Toast.makeText(viewCategory.getApplicationContext(), viewCategory.mTaskListCompleted.get(0).getTaskTitle(), Toast.LENGTH_LONG).show();
        }
        else
        {
            viewCategory.mTaskListRemaining = tasks;
            //Toast.makeText(viewCategory.getApplicationContext(), viewCategory.mTaskListRemaining.get(0).getTaskTitle(), Toast.LENGTH_LONG).show();
        }

        mAdapter.setmTaskList(tasks);
    }
}
