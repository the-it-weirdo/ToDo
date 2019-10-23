package com.kingominho.todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ViewCategory extends AppCompatActivity {

    protected ArrayList<Task> mTaskListCompleted = new ArrayList<>();
    protected ArrayList<Task> mTaskListRemaining = new ArrayList<>();
    private RecyclerView mRecyclerViewRemaining, mRecyclerViewCompleted;
    protected TaskListAdapter mRemainingAdapter, mCompletedAdapter;
    private RecyclerView.LayoutManager mLayoutManagerRemaining, mLayoutManagerCompleted;

    private DataBaseHelper dataBaseHelper;

    private User user;
    private Bundle mBundle;
    private String category, taskRemaining;

    private TextView categoryName, remainingTask;
    private ImageView categoryIcon;
    private RelativeLayout relativeLayout;
    private FloatingActionButton fab;

    ActivityOptions activityOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_category);
        supportPostponeEnterTransition();

        relativeLayout = findViewById(R.id.parent_rl_view_category);
        dataBaseHelper = new DataBaseHelper(getApplicationContext());
        mRecyclerViewRemaining = findViewById(R.id.taskRemainingRecycler);
        mRecyclerViewCompleted = findViewById(R.id.taskCompletedRecycler);
        categoryName = findViewById(R.id.categoryName);
        remainingTask = findViewById(R.id.taskRemainingCount);
        categoryIcon = findViewById(R.id.categoryIcon);
        fab = findViewById(R.id.addTaskFab);

        mBundle = getIntent().getExtras();
        if (mBundle != null) {
            Integer userId = mBundle.getInt(User.USERID_KEY);
            String userName = mBundle.getString(User.USER_NAME_KEY);
            String userEmail = mBundle.getString(User.USER_EMAIL_KEY);
            user = new User(userId, userEmail, userName);
            category = mBundle.getString(SwipeMenuActivity.ITEM_KEY);
            taskRemaining = mBundle.getString(SwipeMenuActivity.TASK_REMAINING_KEY);
        } else {
            user = new User(0, "Test", "Test");
            category = "Test";
            mBundle = user.toBundle();
            mBundle.putString(SwipeMenuActivity.ITEM_KEY, category);
        }
        categoryName.setText(category);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
            relativeLayout.setTransitionName(categoryName.getText().toString());
        }

        supportStartPostponedEnterTransition();

        String str = taskRemaining + " tasks remaining.";
        remainingTask.setText(str);

        if (category.equals("Work")) {
            categoryIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_work_32b1c4_24dp));
        } else if (category.equals("Home")) {
            categoryIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_person_f5a110_24dp));
        }

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
            int color = 0;
            if (category.equals("Work")) {
                color = getResources().getColor(R.color.color5);
            } else if (category.equals("Home")) {
                color = getResources().getColor(R.color.color1);
            }
            fab.setBackgroundTintList(new ColorStateList(new int[][]{new int[]{0}}, new int[]{color}));
        }
        //mTaskListCompleted = makeList(true);
        //mTaskListCompleted = new ArrayList<Task>();
        //mTaskListRemaining = makeList(false);
        //buildRecyclerView(mRecyclerViewRemaining, mRemainingAdapter, mTaskListRemaining);
        //buildRecyclerView(mRecyclerViewCompleted, mCompletedAdapter, mTaskListCompleted);
        mRemainingAdapter = new TaskListAdapter(mTaskListRemaining, getApplicationContext());
        mCompletedAdapter = new TaskListAdapter(mTaskListCompleted, getApplicationContext());

        mLayoutManagerRemaining = new LinearLayoutManager(getApplicationContext());
        mLayoutManagerCompleted = new LinearLayoutManager(getApplicationContext());

        new LoadTaskAsyncTask(this, true, mCompletedAdapter).execute(true);
        new LoadTaskAsyncTask(this, false, mRemainingAdapter).execute(false);

        buildRecyclerViewRemaining();
        buildRecyclerViewCompleted();
        //buildRecyclerView(mRecyclerViewRemaining, mTaskListRemaining, mRemainingAdapter, mLayoutManagerRemaining);
        //buildRecyclerView(mRecyclerViewCompleted, mTaskListCompleted, mCompletedAdapter, mLayoutManagerCompleted);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
            fab.setTransitionName(categoryName.getText().toString());
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTaskActivity();
            }
        });
    }

    private void addTaskActivity()
    {
        Intent intent = new Intent(getApplicationContext(), AddTaskActivity.class);
        intent.putExtras(mBundle);
        if(Build.VERSION.SDK_INT>=21)
        {
            //@TargetApi(LOLLIPOP)
            activityOptions = ActivityOptions.makeSceneTransitionAnimation(
                    this,
                    fab,
                    ViewCompat.getTransitionName(fab));
            startActivity(intent, activityOptions.toBundle());
        }
        else
        {
            startActivity(intent);
        }
    }

    protected ArrayList<Task> makeList(boolean isCompleted) {
        ArrayList<Task> arrayList = new ArrayList<Task>();
        Cursor queryResult = dataBaseHelper.getTask(String.valueOf(user.getUserId()).trim(), category, isCompleted);

        while (queryResult.moveToNext()) {
            /* Reference for column index:

                public static final String taskDbIdColumn = "Id";
                public static final String taskIdColumn = "TaskId";
                public static final String taskTitleColumn = "TaskTitle";
                public static final String taskCompletedColumn = "TaskCompleted";
                public static final String taskUserIdColumn = "UserId";
                public static final String taskCategoryColumn = "TaskCategory";
             */
            String taskId = queryResult.getString(1);
            String taskTitle = queryResult.getString(2);

            //Task(Integer taskId, String tTITLE, boolean isCompleted, String uID, String category)
            arrayList.add(new Task(Integer.valueOf(taskId), taskTitle, isCompleted, String.valueOf(user.getUserId()), category));
        }
        //Task(String tTITLE, boolean isCompleted, String uID, String category)
        if (arrayList.isEmpty()) {
            arrayList.add(new Task("Test " + isCompleted, isCompleted, String.valueOf(user.getUserId()), category));
            arrayList.add(new Task("Test2 " + isCompleted, isCompleted, String.valueOf(user.getUserId()), category));
            arrayList.add(new Task("Test3 " + isCompleted, isCompleted, String.valueOf(user.getUserId()), category));
        }

        return arrayList;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), SwipeMenuActivity.class);
        i.putExtras(user.toBundle());
        startActivity(i);
        finish();
    }

    protected void buildRecyclerViewRemaining() {
        mRecyclerViewRemaining.setHasFixedSize(false);
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
                //Toast.makeText(getApplicationContext(), "position = "+position, Toast.LENGTH_SHORT).show();
                Log.d("position = ", position + ".");
                Task mTask = mTaskListRemaining.get(position);
                mTask.setTaskCompleted(isChecked);
                Toast.makeText(getApplicationContext(), mTask.getTaskTitle() + " is " + mTask.isTaskCompleted(), Toast.LENGTH_SHORT).show();
                if (mTask.isTaskCompleted()) {
                    mTaskListCompleted.add(0, mTask);
                    mCompletedAdapter.notifyItemInserted(0);
                    mCompletedAdapter.notifyItemRangeChanged(0, mTaskListCompleted.size());


                    mTaskListRemaining.remove(position);
                    mRecyclerViewRemaining.removeViewAt(position);
                    mRemainingAdapter.notifyItemRemoved(position);
                    mRemainingAdapter.notifyItemRangeChanged(position, mTaskListRemaining.size());

                    String str = mTaskListRemaining.size()+" tasks remaining";
                    remainingTask.setText(str);
                }
                boolean res = dataBaseHelper.updateTask(mTask);
                if(!res)
                {
                    Toast.makeText(getApplicationContext(), "Task Update failed!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onDeleteButtonPressed(int position) {
                Task mTask = mTaskListRemaining.get(position);
                mTaskListRemaining.remove(position);
                mRecyclerViewRemaining.removeViewAt(position);
                mRemainingAdapter.notifyItemRemoved(position);
                mRemainingAdapter.notifyItemRangeChanged(position, mTaskListRemaining.size());

                String str = mTaskListRemaining.size()+" tasks remaining";
                remainingTask.setText(str);

                boolean res = dataBaseHelper.deleteTask(mTask.getTaskId());
                if(!res)
                {
                    Toast.makeText(getApplicationContext(), "Task Delete failed!!", Toast.LENGTH_SHORT).show();
                }
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

    protected void buildRecyclerViewCompleted() {
        mRecyclerViewCompleted.setHasFixedSize(false);
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
                //Toast.makeText(getApplicationContext(), "position = "+position, Toast.LENGTH_SHORT).show();
                Log.d("position = ", position + ".");
                Task mTask = mTaskListCompleted.get(position);
                mTask.setTaskCompleted(isChecked);
                Toast.makeText(getApplicationContext(), mTask.getTaskTitle() + " is " + mTask.isTaskCompleted(), Toast.LENGTH_SHORT).show();
                if (!mTask.isTaskCompleted()) {
                    mTaskListRemaining.add(0, mTask);
                    mRemainingAdapter.notifyItemInserted(0);
                    mRemainingAdapter.notifyItemRangeChanged(0, mTaskListRemaining.size());

                    mTaskListCompleted.remove(position);
                    mRecyclerViewCompleted.removeViewAt(position);
                    mCompletedAdapter.notifyItemRemoved(position);
                    mCompletedAdapter.notifyItemRangeChanged(position, mTaskListCompleted.size());
                }

                String str = mTaskListRemaining.size()+" tasks remaining";
                remainingTask.setText(str);

                boolean res = dataBaseHelper.updateTask(mTask);
                if(!res)
                {
                    Toast.makeText(getApplicationContext(), "Task Update failed!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onDeleteButtonPressed(int position) {
                Task mTask = mTaskListCompleted.get(position);
                mTaskListCompleted.remove(position);
                mRecyclerViewCompleted.removeViewAt(position);
                mCompletedAdapter.notifyItemRemoved(position);
                mCompletedAdapter.notifyItemRangeChanged(position, mTaskListCompleted.size());

                String str = mTaskListRemaining.size()+" tasks remaining";
                remainingTask.setText(str);

                boolean res = dataBaseHelper.deleteTask(mTask.getTaskId());
                if(!res)
                {
                    Toast.makeText(getApplicationContext(), "Task Delete failed!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
