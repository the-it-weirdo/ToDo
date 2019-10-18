package com.kingominho.todo;

public class CategoryModel {

    private int icon;
    private String categoryTitle;
    private String taskRemaining;

    public CategoryModel(int icon, String categoryTitle, String taskRemaining) {
        this.icon = icon;
        this.categoryTitle = categoryTitle;
        this.taskRemaining = taskRemaining;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public String getTaskRemaining() {
        return taskRemaining;
    }

    public void setTaskRemaining(String taskRemaining) {
        this.taskRemaining = taskRemaining;
    }
}
