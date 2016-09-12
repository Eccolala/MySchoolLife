package com.example.eccolala.myschoollife.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import com.example.eccolala.myschoollife.R;
import com.example.eccolala.myschoollife.model.User;

public class LoginActivity extends AppCompatActivity {
    private EditText userNameTxt;
    private EditText passWordTxt;
    private User user;
    private Bundle bundle;
    private CheckBox isChecked;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setupViews();

        user = new User();
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        //记住密码逻辑
        boolean isRemember = pref.getBoolean("remember_password",false);
        if (isRemember){
            String account = pref.getString("account","");
            String password = pref.getString("password","");


            userNameTxt.setText(account);
            passWordTxt.setText(password);
            isChecked.setChecked(true);
        }
    }


    private void setupViews() {
        userNameTxt = (EditText) findViewById(R.id.username_id);
        userNameTxt.setCursorVisible(false);
        passWordTxt = (EditText) findViewById(R.id.pwd_id);
        passWordTxt.setCursorVisible(false);
        isChecked = (CheckBox) findViewById(R.id.checkbox_id);

    }


    public void check4login(View view) {
        String account = userNameTxt.getText().toString();
        String password = passWordTxt.getText().toString();
        editor = pref.edit();
        if (isChecked.isChecked()){
            editor.putBoolean("remember_password",true);
            editor.putString("account",account);
            editor.putString("password",password);
        }else {
            editor.clear();
        }
        editor.commit();

        user.userName = userNameTxt.getText().toString();
        user.passWord = passWordTxt.getText().toString();


        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, MainActivity.class);
        bundle = new Bundle();
        bundle.putSerializable("user", user);
        intent.putExtras(bundle);
        this.startActivity(intent);
    }
}
