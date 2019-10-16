package com.kingominho.todo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.jetbrains.annotations.NotNull;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String dbName = "ToDoDatabase";

    public static final String taskTableName = "TaskTable";
    public static final String userTableName = "UserTable";

    public static final String taskDbIdColumn = "Id";
    public static final String taskIdColumn = "TaskId";
    public static final String taskTitleColumn = "TaskTitle";
    public static final String taskCompletedColumn = "TaskCompleted";
    public static final String taskUserIdColumn = "UserId";
    public static final String taskCategoryColumn = "TaskCategory";

    public static final String userDbIdColumn = "Id";
    public static final String userIdColumn = "UserId";
    public static final String userNameColumn = "UserName";
    public static final String userEmailColumn = "UserEmail";
    public static final String userUserPasswordColumn = "UserPassword";



    public DataBaseHelper(Context context)
    {
        super(context, dbName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String sqlQueryToCreateTaskTable = "CREATE TABLE " + taskTableName + "(" + taskDbIdColumn + " INTEGER PRIMARY KEY AUTOINCREMENT," + taskIdColumn + " TEXT," +
                taskTitleColumn + " TEXT," + taskCompletedColumn + " INTEGER DEFAULT 0," + taskUserIdColumn + " TEXT," + taskCategoryColumn + " TEXT)";
        sqLiteDatabase.execSQL(sqlQueryToCreateTaskTable);

        String sqlQueryToCreateUserTable = "CREATE TABLE " + userTableName + "(" + userDbIdColumn + " INTEGER PRIMARY KEY AUTOINCREMENT," + userIdColumn + " TEXT," +
                userNameColumn+ " TEXT," + userEmailColumn + " TEXT," + userUserPasswordColumn + " TEXT)";
        sqLiteDatabase.execSQL(sqlQueryToCreateUserTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sqlQueryTaskTable = "DROP TABLE IF EXISTS " + taskTableName;
        String sqlQueryUserTable = "DROP TABLE IF EXISTS " + userTableName;

        sqLiteDatabase.execSQL(sqlQueryTaskTable);
        sqLiteDatabase.execSQL(sqlQueryUserTable);
    }


    /*taskTable start*/

    public boolean insertTask(@NotNull Task task)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        int flag;
        if(task.isTaskCompleted())
            flag = 1;
        else
            flag = 0;

        contentValues.put(taskIdColumn, String.valueOf(task.getTaskId()));
        contentValues.put(taskTitleColumn, task.getTaskTitle());
        contentValues.put(taskCompletedColumn, flag);
        contentValues.put(taskUserIdColumn, task.getUserId());
        contentValues.put(taskCategoryColumn, task.getTaskCategory());

        long res = database.insert(taskTableName, null, contentValues);
        return res > 0;
    }

    public boolean updateTask(Task task)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        int flag;
        if(task.isTaskCompleted())
            flag = 1;
        else
            flag = 0;

        int taskId = task.getTaskId();

        contentValues.put(taskIdColumn, String.valueOf(taskId));
        contentValues.put(taskTitleColumn, task.getTaskTitle());
        contentValues.put(taskCompletedColumn, flag);
        contentValues.put(taskUserIdColumn, task.getUserId());
        contentValues.put(taskCategoryColumn, task.getTaskCategory());

        long res = database.update(taskTableName, contentValues, taskIdColumn+"=?", new String[] {String.valueOf(taskId).trim()});
        return res > 0;
    }

    public Cursor getTask(int taskId)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        final String query = "select * from "+taskTableName+" where "+taskIdColumn+"='"+String.valueOf(taskId).trim()+"'";
        Cursor res=db.rawQuery(query,null);
        return res;
    }

    public Cursor getTask(String taskUserId)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        final String query = "select * from "+taskTableName+" where "+taskUserIdColumn+"='"+taskUserId.trim()+"'";
        Cursor res=db.rawQuery(query,null);
        return res;
    }

    public Cursor getTask(String taskUserId, boolean isCompleted)
    {
        int flag;
        if(isCompleted)
            flag = 1;
        else
            flag = 0;

        SQLiteDatabase db=this.getWritableDatabase();
        final String query = "select * from "+taskTableName+" where "+taskUserIdColumn+"='"+taskUserId.trim()+"' and "
                +taskCompletedColumn+"='"+flag+"'";
        Cursor res=db.rawQuery(query,null);
        return res;
    }

    public boolean deleteTask(int taskId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        long res = db.delete(taskTableName, taskIdColumn + "=?", new String[]{String.valueOf(taskId).trim()});

        return res > 0;
    }

    /*taskTable end*/

    /*userTable start*/
    public boolean insertUser(@NotNull User user)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(userIdColumn, String.valueOf(user.getUserId()));
        contentValues.put(userNameColumn, user.getUserName());
        contentValues.put(userEmailColumn, user.getUserEmail());
        contentValues.put(userUserPasswordColumn, user.getUserPassword());

        long res = database.insert(userTableName, null, contentValues);
        return res > 0;
    }

}
