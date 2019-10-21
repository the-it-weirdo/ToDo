package com.kingominho.todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ViewCategory extends AppCompatActivity {

    private ArrayList<Task> mTaskListCompleted;
    private ArrayList<Task> mTaskListRemaining;
    private RecyclerView mRecyclerViewRemaining, mRecyclerViewCompleted;
    private TaskListAdapter mAdapter, mRemainingAdapter, mCompletedAdapter;
    private RecyclerView.LayoutManager mLayoutManagerRemaining, mLayoutManagerCompleted;

    private DataBaseHelper dataBaseHelper;

    private User user;
    private Bundle mBundle;
    private String category, taskRemaining;

    private TextView categoryName, remainingTask;
    private ImageView categoryIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_category);

        dataBaseHelper = new DataBaseHelper(getApplicationContext());
        mRecyclerViewRemaining = findViewById(R.id.taskRemainingRecycler);
        mRecyclerViewCompleted = findViewById(R.id.taskCompletedRecycler);
        categoryName = findViewById(R.id.categoryName);
        remainingTask = findViewById(R.id.taskRemainingCount);
        categoryIcon = findViewById(R.id.categoryIcon);

        mBundle = getIntent().getExtras();
        if(mBundle!=null)
        {
            Integer userId = mBundle.getInt(User.USERID_KEY);
            String userName = mBundle.getString(User.USER_NAME_KEY);
            String userEmail = mBundle.getString(User.USER_EMAIL_KEY);
            user = new User(userId, userEmail, userName);
            category = mBundle.getString(SwipeMenuActivity.ITEM_KEY);
            taskRemaining = mBundle.getString(SwipeMenuActivity.TASK_REMAINING_KEY);
        }
        else
        {
            user = new User(0, "Test", "Test");
            category = "Test";
            mBundle = user.toBundle();
            mBundle.putString(SwipeMenuActivity.ITEM_KEY, category);
        }

        categoryName.setText(category);
        String str = taskRemaining+ " tasks remaining.";
        remainingTask.setText(str);

        if(category.equals("Work"))
        {
            categoryIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_work_32b1c4_24dp));
        }
        else if(category.equals("Home"))
        {
            categoryIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_person_f5a110_24dp));
        }

        mTaskListCompleted = makeList(true);
        mTaskListRemaining = makeList(false);
        mRemainingAdapter = new TaskListAdapter(mTaskListRemaining, getApplicationContext());
        mCompletedAdapter = new TaskListAdapter(mTaskListCompleted, getApplicationContext());
        //buildRecyclerView(mRecyclerViewRemaining, mRemainingAdapter, mTaskListRemaining);
        //buildRecyclerView(mRecyclerViewCompleted, mCompletedAdapter, mTaskListCompleted);
        buildRecyclerViewRemaining();
        buildRecyclerViewCompleted();
    }

    private ArrayList<Task> makeList(boolean isCompleted)
    {
        ArrayList<Task> arrayList = new ArrayList<Task>();
        Cursor queryResult = dataBaseHelper.getTask(String.valueOf(user.getUserId()).trim(), category, isCompleted);

        while(queryResult.moveToNext())
        {
            /* Reference for column index:

                public static final String taskDbIdColumn = "Id";
                public static final String taskIdColumn = "TaskId";
                public static final String taskTitleColumn = "TaskTitle";
                public static final String taskCompletedColumn = "TaskCompleted";
                public static final String taskUserIdColumn = "UserId";
                public static final String taskCategoryColumn = "TaskCategory";
             */
            String taskId = queryResult.getString(2);
            String taskTitle = queryResult.getString(3);

            //Task(Integer taskId, String tTITLE, boolean isCompleted, String uID, String category)
            arrayList.add(new Task(Integer.valueOf(taskId), taskTitle, isCompleted, String.valueOf(user.getUserId()), category));
        }
        //Task(String tTITLE, boolean isCompleted, String uID, String category)
        if(arrayList.isEmpty())
        {
            arrayList.add(new Task("Test "+isCompleted, isCompleted, String.valueOf(user.getUserId()), category));
            arrayList.add(new Task("Test2 "+isCompleted, isCompleted, String.valueOf(user.getUserId()), category));
            arrayList.add(new Task("Test3 "+isCompleted, isCompleted, String.valueOf(user.getUserId()), category));
        }

        return arrayList;
    }

    private void buildRecyclerViewRemaining()
    {
        mRecyclerViewRemaining.setHasFixedSize(true);
        mLayoutManagerRemaining = new LinearLayoutManager(getApplicationContext());

        mRecyclerViewRemaining.setLayoutManager(mLayoutManagerRemaining);
        mRecyclerViewRemaining.setAdapter(mRemainingAdapter);

        /*mRemainingAdapter.setOnItemCLickListener(new TaskListAdapter.OnItemClickListener() {
            @Override
            public void onItemChecked(int position) {
                Toast.makeText(getApplicationContext(), "position = "+position, Toast.LENGTH_SHORT).show();
                Log.d("position = ", position+".");
                Task mTask = mTaskListRemaining.get(position);
                mTask.setTaskCompleted(!mTask.isTaskCompleted());
                //Toast.makeText(getApplicationContext(), mTaskList.get(position).isTaskCompleted()+" clicked.", Toast.LENGTH_SHORT).show();
                if(mTask.isTaskCompleted())
                {
                    //mTaskListCompleted.add(0, mTask);
                    mTaskListRemaining.remove(position);
                    mRemainingAdapter.notifyItemRemoved(position);
                    //mCompletedAdapter.notifyItemInserted(0);
                }
                else
                {
                    //mTaskListRemaining.add(0, mTask);
                    mTaskListCompleted.remove(position);
                    //mRemainingAdapter.notifyItemInserted(0);
                    mCompletedAdapter.notifyItemRemoved(position);
                }
                //mAdapter.notifyItemChanged(position);
                /*TODO:update database: uncomment following line
                //dataBaseHelper.updateTask(mTask);
            }
        });*/

        mRemainingAdapter.setOnItemCheckedChangeListener(new TaskListAdapter.OnItemCheckedChange() {
            @Override
            public void onItemChecked(int position, boolean isChecked) {
                Toast.makeText(getApplicationContext(), "position = "+position, Toast.LENGTH_SHORT).show();
                Log.d("position = ", position+".");
                Task mTask = mTaskListCompleted.get(position);
                mTask.setTaskCompleted(isChecked);
                if(isChecked)
                {
                    mTaskListCompleted.add(0, mTask);
                    mTaskListRemaining.remove(position);
                    mRemainingAdapter.notifyItemRemoved(position);
                    mCompletedAdapter.notifyItemInserted(0);
                }
                //dataBaseHelper.updateTask(mTask);
            }
        });

    }

    /*private void buildRecyclerView(RecyclerView mRecyclerView, final TaskListAdapter mAdapter, final ArrayList<Task> mTaskList)
    {
        mRecyclerView.setHasFixedSize(false);

        mLayoutManager = new LinearLayoutManager(getApplicationContext());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemCLickListener(new TaskListAdapter.OnItemClickListener() {
            @Override
            public void onItemChecked(int position) {
                if(position == mTaskList.size())
                {
                    position = 0;
                }

            }
        });
    }*/

    private void buildRecyclerViewCompleted()
    {
        mRecyclerViewCompleted.setHasFixedSize(true);
        mLayoutManagerCompleted = new LinearLayoutManager(getApplicationContext());

        mRecyclerViewCompleted.setLayoutManager(mLayoutManagerCompleted);
        mRecyclerViewCompleted.setAdapter(mCompletedAdapter);


        /*mCompletedAdapter.setOnItemCheckedChangeListener(new TaskListAdapter.OnItemCheckedChange() {
            @Override
            public void onItemCheckedhange(int position, boolean) {
                Toast.makeText(getApplicationContext(), "position = "+position, Toast.LENGTH_SHORT).show();
                Log.d("position = ", position+".");
                Task mTask = mTaskListCompleted.get(position);
                mTask.setTaskCompleted(!mTask.isTaskCompleted());
                //Toast.makeText(getApplicationContext(), mTaskList.get(position).isTaskCompleted()+" clicked.", Toast.LENGTH_SHORT).show();
                if(mTask.isTaskCompleted())
                {
                    //mTaskListCompleted.add(0, mTask);
                    mTaskListRemaining.remove(position);
                    mRemainingAdapter.notifyItemRemoved(position);
                    //mCompletedAdapter.notifyItemInserted(0);
                }
                else
                {
                    //mTaskListRemaining.add(0, mTask);
                    mTaskListCompleted.remove(position);
                    //mRemainingAdapter.notifyItemInserted(0);
                    mCompletedAdapter.notifyItemRemoved(position);
                }
                //mAdapter.notifyItemChanged(position);
                dataBaseHelper.updateTask(mTask);
            }
        });*/

        mCompletedAdapter.setOnItemCheckedChangeListener(new TaskListAdapter.OnItemCheckedChange() {
            @Override
            public void onItemChecked(int position, boolean isChecked) {
                Toast.makeText(getApplicationContext(), "position = "+position, Toast.LENGTH_SHORT).show();
                Log.d("position = ", position+".");
                Task mTask = mTaskListCompleted.get(position);
                mTask.setTaskCompleted(isChecked);
                if(!isChecked)
                {
                    mTaskListRemaining.add(0, mTask);
                    mTaskListCompleted.remove(position);
                    mRemainingAdapter.notifyItemInserted(0);
                    mCompletedAdapter.notifyItemRemoved(position);
                }
                //dataBaseHelper.updateTask(mTask);
            }
        });

    }
}
