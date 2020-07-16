package com.example.mini_tiktok.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mini_tiktok.R;
import com.example.mini_tiktok.utils.UserAccountUtils;

public class UserActivity extends AppCompatActivity {

    private TextView textId;
    private Button btnMyVideo;
    private Button btnLogout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);

        textId = findViewById(R.id.userId);
        btnMyVideo = findViewById(R.id.btnMyVideo);
        btnLogout = findViewById(R.id.btnLogout);

        textId.setText(UserAccountUtils.userID);

        if(UserAccountUtils.userID.equals("Guest")){
            btnLogout.setText("立即登录");
        }else{
            btnLogout.setText("退出登录");
        }

        btnMyVideo.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO : 跳转至'我的视频'界面
            }
        });

        btnLogout.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserAccountUtils.userID = "Guest";
                textId.setText(UserAccountUtils.userID);
                startActivity(new Intent(UserActivity.this, LoginActivity.class));
                textId.setText(UserAccountUtils.userID);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        textId.setText(UserAccountUtils.userID);
    }
}
