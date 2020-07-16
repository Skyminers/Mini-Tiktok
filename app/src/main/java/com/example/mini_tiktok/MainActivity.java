package com.example.mini_tiktok;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mini_tiktok.activity.LoginActivity;
import com.example.mini_tiktok.activity.UploadActivity;
import com.example.mini_tiktok.activity.UserActivity;
import com.example.mini_tiktok.activity.VideoListActivity;

public class MainActivity extends Activity {

    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);

        bind(btn1, UploadActivity.class);
        bind(btn2, LoginActivity.class);
        bind(btn3, VideoListActivity.class);
        bind(btn4, UserActivity.class);
    }

    void bind(Button btn, final Class<?> nxt){
        btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,nxt));
            }
        });
    }
}