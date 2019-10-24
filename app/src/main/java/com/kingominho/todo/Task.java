package com.kingominho.todo;

import android.os.Bundle;

class Task {

    final String taskID_KEY = "TASK_ID";
    final String taskTITLE_KEY = "TASK_TITLE";
    final String taskCOMPLETED_KEY = "TASK_COMPLETED";
    final String userID_KEY = "USER_ID";
    final String taskCategory_KEY = "TASK_CATEGORY";


    private String taskTitle, userId, taskCategory;
    private int taskId;
    private boolean taskCompleted;


    private int getHashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((taskTitle == null) ? 0: taskTitle.hashCode());
        result = prime * result + ((userId == null) ? 0: userId.hashCode());
        result = prime * result + ((taskCategory == null) ? 0: taskCategory.hashCode());
        return result;
    }

    Task(String tTITLE, boolean isCompleted, String uID, String category)
    {
        this.taskId = 0;
        this.taskTitle = tTITLE;
        this.taskCompleted = isCompleted;
        this.userId = uID;
        this.taskCategory = category;

    }

    Task(Integer taskId, String tTITLE, boolean isCompleted, String uID, String category)
    {
        this.taskId = Integer.valueOf(taskId);
        this.taskTitle = tTITLE;
        this.taskCompleted = isCompleted;
        this.userId = uID;
        this.taskCategory = category;

    }

    public int getTaskId() {
        return taskId;
    }

    public String getTaskCategory() {
        return taskCategory;
    }

    public void setTaskCategory(String taskCategory) {
        this.taskCategory = taskCategory;
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
        bundle.putString(taskCategory_KEY, this.taskCategory);

        return bundle;
    }
}
