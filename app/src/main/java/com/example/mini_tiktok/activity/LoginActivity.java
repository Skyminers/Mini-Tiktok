package com.example.mini_tiktok.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mini_tiktok.R;
import com.example.mini_tiktok.database.AccountEntity;
import com.example.mini_tiktok.utils.UserAccountUtils;

public class LoginActivity extends Activity {

    private EditText userName;
    private EditText password;
    private ImageView unameClear;
    private ImageView pwdClear;
    private Button btnLogin;
    private Button btnRegister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userName = findViewById(R.id.et_userName);
        password = findViewById(R.id.et_password);
        unameClear = findViewById(R.id.iv_unameClear);
        pwdClear = findViewById(R.id.iv_pwdClear);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);

        addClearListener(userName,unameClear);
        addClearListener(password,pwdClear);

        final Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {// 该方法在主线程中运行
                super.handleMessage(msg);
                switch (msg.what){
                    case 0: {
                        Toast.makeText(LoginActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
                        break;
                    }
                }

            }
        };
        btnLogin.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userName.getText().length() == 0 || password.getText().length() == 0){
                    Toast.makeText(LoginActivity.this,"用户名和密码不能为空",Toast.LENGTH_SHORT).show();
                    return ;
                }
                final AccountEntity entity = new AccountEntity(userName.getText().toString(),
                                                         password.getText().toString(),null);
                new Thread(){
                    @Override
                    public void run() {
                        if(!UserAccountUtils.checkAccount(LoginActivity.this,entity)){
                            Message msg = mHandler.obtainMessage(0,"错误的用户名或密码");
                            mHandler.sendMessage(msg);
                        }else{
                            Message msg = mHandler.obtainMessage(0,"登录成功");
                            mHandler.sendMessage(msg);
                            AccountEntity entityDB = UserAccountUtils.findAccountById(LoginActivity.this,entity.getMId());
                            UserAccountUtils.userID = entityDB.getMId();
                            UserAccountUtils.userNick = entityDB.getNick();
                            finish();
                        }
                    }
                }.start();
            }
        });

        btnRegister.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userName.getText().length() == 0 || password.getText().length() == 0){
                    Toast.makeText(LoginActivity.this,"用户名和密码不能为空",Toast.LENGTH_SHORT).show();
                    return ;
                }
                final AccountEntity entity = new AccountEntity(userName.getText().toString(),
                                                         password.getText().toString(),userName.getText().toString());
                new Thread(){
                    @Override
                    public void run() {
                        if(!UserAccountUtils.registerAccount(LoginActivity.this,entity)){
                            //注册失败
                            Message msg = mHandler.obtainMessage(0,"该用户名已被使用");
                            mHandler.sendMessage(msg);
                        }else{
                            Message msg = mHandler.obtainMessage(0,"注册成功，请重新登录");
                            mHandler.sendMessage(msg);
                        }
                    }
                }.start();
            }
        });
    }

    public static void addClearListener(final EditText et , final ImageView iv){
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //如果有输入内容长度大于0那么显示clear按钮
                String str = s + "" ;
                if (s.length() > 0){
                    iv.setVisibility(View.VISIBLE);
                }else{
                    iv.setVisibility(View.INVISIBLE);
                }
            }
        });

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et.setText("");
            }
        });
    }
}
