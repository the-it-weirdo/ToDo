package com.kingominho.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    Button loginButton, signUpButton;
    EditText uName, uPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.loginButton);
        signUpButton = (Button) findViewById(R.id.signUpButton);

        uName = (EditText) findViewById(R.id.uNameInput);
        uPass = (EditText) findViewById(R.id.passInput);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //loginFunction();
                Intent intent = new Intent(getApplicationContext(), SwipeMenuActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loginFunction()
    {
        String mUserName = uName.getText().toString().trim();
        String mUserPass = uPass.getText().toString().trim();

        DataBaseHelper dataBaseHelper = new DataBaseHelper(getApplicationContext());

        Cursor queryResult = dataBaseHelper.getUser(mUserName, mUserPass);

        if(!queryResult.moveToNext())
        {
            Toast.makeText(getApplicationContext(), "Error!! Incorrect username or password!!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            /*
            public static final String userDbIdColumn = "Id"; -> 0
            public static final String userIdColumn = "UserId"; -> 1
            public static final String userNameColumn = "UserName"; -> 2
            public static final String userEmailColumn = "UserEmail"; -> 3
            public static final String userPasswordColumn = "UserPassword"; -> 4
             */
            Integer userId = Integer.parseInt(queryResult.getString(0));
            String userName = queryResult.getString(2).trim();
            String userEmail = queryResult.getString(3).trim();

            User user = new User(userId, userName, userEmail);
            Intent intent = new Intent(getApplicationContext(), SwipeMenuActivity.class);
            intent.putExtras(user.toBundle());
            startActivity(intent);
            finish();
        }
    }
}
