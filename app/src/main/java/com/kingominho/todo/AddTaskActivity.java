package com.kingominho.todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;

public class AddTaskActivity extends AppCompatActivity {

    private User user;
    private Bundle mBundle;
    private String category;

    private TextView categoryName, userName;
    private EditText newTaskTitle;
    private ShimmerFrameLayout container;
    private ConstraintLayout constraintLayout;
    private ImageButton addTaskButton;
    private DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        dataBaseHelper = new DataBaseHelper(getApplicationContext());
        categoryName = findViewById(R.id.categoryName);
        userName = findViewById(R.id.userName);
        newTaskTitle = findViewById(R.id.newTaskTitle);
        addTaskButton = findViewById(R.id.addTaskButton);
        container = findViewById(R.id.shimmer_container);
        constraintLayout = findViewById(R.id.addTaskParent);

        mBundle = getIntent().getExtras();
        if (mBundle != null) {
            Integer userId = mBundle.getInt(User.USERID_KEY);
            String userName = mBundle.getString(User.USER_NAME_KEY);
            String userEmail = mBundle.getString(User.USER_EMAIL_KEY);
            user = new User(userId, userEmail, userName);
            category = mBundle.getString(SwipeMenuActivity.ITEM_KEY);
            /*TODO: fix taskRemaining pass in Bundle. update task remaining on button click.
            * TODO: fix onBackPresses for various activities ,
            * TODO: fix screenshot issues */
        } else {
            user = new User(0, "Test", "Test");
            category = "Test";
            mBundle = user.toBundle();
            mBundle.putString(SwipeMenuActivity.ITEM_KEY, category);
        }
        categoryName.setText(category);
        userName.setText(user.getUserName());

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
            addTaskButton.setTransitionName(categoryName.getText().toString());
        }

        int color = 0;
        if (category.equals("Work")) {
            color = getResources().getColor(R.color.color5);
        } else if (category.equals("Home")) {
            color = getResources().getColor(R.color.color1);
        }
        addTaskButton.setBackgroundColor(color);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTask = newTaskTitle.getText().toString();
                addNewTask(newTask);
            }
        });
    }

    private void addNewTask(String newTask)
    {
        //constraintLayout.setVisibility(View.GONE);
        container.setAlpha(1);
        container.startShimmer();

        Task mTask = new Task(newTask, false, String.valueOf(user.getUserId()), category);
        dataBaseHelper.insertTask(mTask);

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(getApplicationContext(), ViewCategory.class);
                mBundle.putInt(SwipeMenuActivity.TASK_REMAINING_KEY, mBundle.getInt(SwipeMenuActivity.TASK_REMAINING_KEY)+1);
                i.putExtras(mBundle);
                startActivity(i);
                container.stopShimmer();
                finish();
            }
        }, 1500);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), ViewCategory.class);
        i.putExtras(mBundle);
        startActivity(i);
        container.stopShimmer();
        finish();
    }
}
