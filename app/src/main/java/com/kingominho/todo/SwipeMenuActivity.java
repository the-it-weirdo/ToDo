package com.kingominho.todo;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SwipeMenuActivity extends AppCompatActivity implements Adapter.AdapterOnCardClickListener{
    ViewPager viewPager;
    RelativeLayout relativeLayout;
    Adapter adapter;
    List<CategoryModel> models;
    Integer[] colors = null;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();


    private String greetings;
    private int noOfCategories;
    private User user;
    private DataBaseHelper dataBaseHelper;
    private TextView mGreetings, mUserNameView;

    private boolean mBackPressedOnce;
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mBackPressedOnce = false;
        }
    };
    private int delay = 2000;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swipe_menu);
        mGreetings = (TextView) findViewById(R.id.greetings);
        mUserNameView = (TextView) findViewById(R.id.userName);

        dataBaseHelper = new DataBaseHelper(getApplicationContext());

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null)
        {
            Integer userId = bundle.getInt(User.USERID_KEY);
            String userName = bundle.getString(User.USER_NAME_KEY);
            String userEmail = bundle.getString(User.USER_EMAIL_KEY);
            user = new User(userId, userEmail, userName);
        }
        else
        {
            user = new User(0, "Test", "Test");
        }

        //Loading greetings
        greetings = "Hello, ";
        mGreetings.setText(greetings);

        //Setting username
        mUserNameView.setText(user.getUserName());


        //Loading the category Names
        String[] categoryNames = getResources().getStringArray(R.array.categoryNames);
        noOfCategories = categoryNames.length;

        //Loading the remaining tasks counts
        Map<String, Integer> map = new HashMap<String, Integer>();
        for(String categoryName : categoryNames)
        {
            map.put(categoryName, 0);
            Cursor queryResult = dataBaseHelper.getTask(String.valueOf(user.getUserId()).trim(), categoryName.trim(), false);
            while(queryResult.moveToNext())
            {
                map.put(categoryName, map.get(categoryName) + 1);
            }
        }


        //loading the category icons
        TypedArray imgTypedArray = getResources().obtainTypedArray(R.array.categoryIcons);
        int[] iconImageIDs = new int[imgTypedArray.length()];
        for (int i = 0; i < imgTypedArray.length(); i++) {
            iconImageIDs[i] = imgTypedArray.getResourceId(i, -1);
        }
        imgTypedArray.recycle();

        //CategoryModel(int icon, String categoryTitle, String taskRemaining)
        String appendString = " tasks remaining.";
        models = new ArrayList<>();
        for (int i = 0; i < noOfCategories ; i++) {
            models.add(new CategoryModel(iconImageIDs[i], categoryNames[i],
                    String.valueOf(map.get(categoryNames[i]))+appendString));
        }

        adapter = new Adapter(models, this);

        viewPager = findViewById(R.id.viewPager);
        relativeLayout = findViewById(R.id.relativeLayout);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(100, 0, 100, 0);

        Integer[] colors_temp = {
                getResources().getColor(R.color.color1),
                /*getResources().getColor(R.color.color2),
                getResources().getColor(R.color.color3),
                getResources().getColor(R.color.color4),*/
                getResources().getColor(R.color.color5)
        };

        colors = colors_temp;

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position < (adapter.getCount() - 1) && position < (colors.length - 1)) {
                    relativeLayout.setBackgroundColor(

                            (Integer) argbEvaluator.evaluate(
                                    positionOffset,
                                    colors[position],
                                    colors[position + 1]
                            )
                    );
                } else {
                    relativeLayout.setBackgroundColor(colors[colors.length - 1]);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        if (mBackPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.mBackPressedOnce = true;
        String mMessage = getResources().getString(R.string.pressBackTwice);
        mToast = Toast.makeText(getApplicationContext(), mMessage, Toast.LENGTH_SHORT);
        mToast.show();

        mHandler.postDelayed(mRunnable, delay);
        //overridePendingTransition(R.anim.go_up, R.anim.go_down);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mToast != null) {
            mToast.cancel();
        }
        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable);
        }
    }

    @Override
    public void onCardClick(String param) {
        /*Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("param", param);
        startActivity(intent);*/
        //overridePendingTransition(R.anim.go_up, R.anim.go_down);

        Toast.makeText(getApplicationContext(), param+" clicked!", Toast.LENGTH_SHORT).show();
    }

}
