package com.hyungjun212naver.finedustproject.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hyungjun212naver.finedustproject.App.AppConfig;
import com.hyungjun212naver.finedustproject.App.AppController;
import com.hyungjun212naver.finedustproject.Helper.SQLiteHandler;
import com.hyungjun212naver.finedustproject.Helper.SessionManager;
import com.hyungjun212naver.finedustproject.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hyung on 2017-08-15.
 */

public class RegisterActivity extends Activity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputFullName;
    private EditText inputEmail;
    private EditText inputPassword;
    private RadioGroup radioGroup;
    private RadioButton radioButton_yes;
    private RadioButton radioButton_no;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputFullName = (EditText)findViewById(R.id.name);
        inputEmail = (EditText)findViewById(R.id.email);
        inputPassword = (EditText)findViewById(R.id.password);
        btnRegister = (Button)findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button)findViewById(R.id.btnLinkToLoginScreen);
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        radioButton_yes = (RadioButton)findViewById(R.id.radioButton_yes);
        radioButton_no = (RadioButton)findViewById(R.id.radioButton_no);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        session = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());

        if(session.isLoggedIn()){
            Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }

        btnRegister.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                String name = inputFullName.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                String yn = "null";
                if(radioButton_yes.isChecked())
                    yn="yes";
                else if(radioButton_no.isChecked())
                    yn="no";

                if(!name.isEmpty() && !email.isEmpty() && !password.isEmpty()){
                    registerUser(name, email, password);
                } else{
                    Toast.makeText(getApplicationContext(),"Please enter your details!",Toast.LENGTH_LONG).show();
                }
            }
        });
        btnLinkToLogin.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
    private void registerUser(final String name,final String email, final String password){
//        Log.i("register`````````````", yn);

        String tag_string_req = "req_register";
        pDialog.setMessage("Registering ... ");
        showDialog();
        StringRequest strReq = new StringRequest(Method.POST, AppConfig.URL_REGISTER, new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                Log.d(TAG,"Register Response : " + response.toString());
                hideDialog();
                try{
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if(!error){
                        String uid = jObj.getString("uid");
                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
//                        String yn = user.getString("yn");
                        String created_at = user.getString("created_at");
//                        Log.i("register_add```````````", yn);
                        db.addUser(name, email, uid, created_at);
                        Toast.makeText(getApplicationContext(), "User successfully registered. Try Login now!",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else{
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),errorMsg,Toast.LENGTH_LONG).show();
                    }
                } catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Log.e(TAG,"Registration Error : " + error.getMessage());
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("name",name);
                params.put("email",email);
                params.put("password",password);
//                params.put("yn",yn);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    private void showDialog(){
        if(!pDialog.isShowing())
            pDialog.show();
    }
    private void hideDialog(){
        if(pDialog.isShowing())
            pDialog.dismiss();
    }
}