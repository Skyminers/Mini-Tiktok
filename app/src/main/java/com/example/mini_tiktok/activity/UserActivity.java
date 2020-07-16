package com.example.mini_tiktok.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mini_tiktok.R;
import com.example.mini_tiktok.utils.AttentionUtils;
import com.example.mini_tiktok.utils.UserAccountUtils;

import java.util.ArrayList;

public class UserActivity extends Activity {

    private TextView textId;
    private Button btnMyVideo;
    private Button btnLogout;
    private Button btnAttention;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);

        textId = findViewById(R.id.userId);
        btnMyVideo = findViewById(R.id.btnMyVideo);
        btnLogout = findViewById(R.id.btnLogout);
        btnAttention = findViewById(R.id.btn_attention);

        textId.setText(UserAccountUtils.userID);

        if(UserAccountUtils.userID.equals("Guest")){
            btnLogout.setText("立即登录");
        }else{
            btnLogout.setText("退出登录");
        }

        btnMyVideo.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserActivity.this,VideoListActivity.class);
                ArrayList<String> list = new ArrayList<String>();
                list.add(UserAccountUtils.userID);
                intent.putExtra("IDs",list);
                startActivity(intent);
            }
        });

        btnAttention.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(UserActivity.this,VideoListActivity.class);
                new Thread(){
                    @Override
                    public void run() {
                        ArrayList<String> list = (ArrayList<String>)
                                AttentionUtils.getAttentionList(UserActivity.this, UserAccountUtils.userID);
                        list.add(UserAccountUtils.userID);
                        intent.putExtra("IDs",list);
                        startActivity(intent);
                    }
                }.start();
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
        if(UserAccountUtils.userID.equals("Guest")){
            btnLogout.setText("立即登录");
        }else{
            btnLogout.setText("退出登录");
        }
    }
}
