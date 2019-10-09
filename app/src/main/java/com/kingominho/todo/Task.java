package com.kingominho.todo;

import android.os.Bundle;

class Task {

    final String taskID_KEY = "TASK_ID";
    final String taskTITLE_KEY = "TASK_TITLE";
    final String taskCOMPLETED_KEY = "TASK_COMPLETED";
    final String userID_KEY = "USER_ID";


    private String taskTitle, userId;
    private int taskId;
    private boolean taskCompleted;

    Task(String tTITLE, boolean isCompleted, String uID)
    {
        this.taskId = taskTitle.hashCode();
        this.taskTitle = tTITLE;
        this.taskCompleted = isCompleted;
        this.userId = uID;

    }

    public int getTaskId() {
        return taskId;
    }


    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isTaskCompleted() {
        return taskCompleted;
    }

    public void setTaskCompleted(boolean taskCompleted) {
        this.taskCompleted = taskCompleted;
    }

    public Bundle toBundle()
    {
        Bundle bundle = new Bundle();
        bundle.putInt(taskID_KEY, this.taskId);
        bundle.putString(taskTITLE_KEY, this.taskTitle);
        bundle.putBoolean(taskCOMPLETED_KEY, this.taskCompleted);
        bundle.putString(userID_KEY, this.userId);

        return bundle;
    }
}
