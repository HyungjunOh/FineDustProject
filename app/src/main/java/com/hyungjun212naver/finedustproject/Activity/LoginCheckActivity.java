package com.hyungjun212naver.finedustproject.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hyungjun212naver.finedustproject.Helper.SQLiteHandler;
import com.hyungjun212naver.finedustproject.Helper.SessionManager;
import com.hyungjun212naver.finedustproject.R;

import java.util.HashMap;

/**
 * Created by hyung on 2017-08-15.
 */

public class LoginCheckActivity extends Activity {
    private TextView txtName;
    private TextView txtEmail;
    private Button btnLogout;
    private Button btngoHome;
    private SQLiteHandler db;
    private SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_check);

        txtName = (TextView)findViewById(R.id.name);
        txtEmail = (TextView)findViewById(R.id.email);
        btngoHome = (Button)findViewById(R.id.btnGohome);
        btnLogout = (Button)findViewById(R.id.btnLogout);
        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        if(!session.isLoggedIn()){
            logoutUser();
        }
        HashMap<String,String> user = db.getUserDetails();
        String name = user.get("name");
        String email = user.get("email");
        txtName.setText(name);
        txtEmail.setText(email);
        btngoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginCheckActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                logoutUser();
            }
        });
    }
    public void logoutUser(){
        session.setLogin(false);
        db.deleteUsers();
        Intent intent = new Intent(LoginCheckActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
