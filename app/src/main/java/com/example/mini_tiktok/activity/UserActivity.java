package com.example.mini_tiktok.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mini_tiktok.R;
import com.example.mini_tiktok.database.AccountEntity;
import com.example.mini_tiktok.utils.AttentionUtils;
import com.example.mini_tiktok.utils.UserAccountUtils;

import java.util.ArrayList;

public class UserActivity extends Activity {

    private TextView userID;
    private TextView userNick;
    private Button btnMyVideo;
    private Button btnLogout;
    private Button btnAttention;
    private Button btnChangeNick;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);

        userID = findViewById(R.id.userID);
        userNick = findViewById(R.id.userNick);
        btnMyVideo = findViewById(R.id.btnMyVideo);
        btnLogout = findViewById(R.id.btnLogout);
        btnAttention = findViewById(R.id.btn_attention);
        btnChangeNick = findViewById(R.id.btnChangeNick);

        updateText();

        if(UserAccountUtils.userID.equals("Guest")){
            btnLogout.setText("立即登录");
        }else{
            btnLogout.setText("退出登录");
        }

        btnChangeNick.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText edit = new EditText(UserActivity.this);

                AlertDialog.Builder editDialog = new AlertDialog.Builder(UserActivity.this);
                editDialog.setTitle(getString(R.string.dialog_edit_text));
                editDialog.setIcon(R.mipmap.ic_launcher_round);

                editDialog.setView(edit);

                editDialog.setPositiveButton(getString(R.string.dialog_btn_confirm_text)
                        , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final String newNick = edit.getText().toString();
                                UserAccountUtils.userNick = newNick;
                                updateText();
                                new Thread(){
                                    @Override
                                    public void run() {
                                        AccountEntity entity = UserAccountUtils.findAccountById(UserActivity.this, UserAccountUtils.userID);
                                        entity.setNick(newNick);
                                        UserAccountUtils.modifyNick(UserActivity.this,entity);
                                    }
                                }.start();
                                dialog.dismiss();
                            }
                        });

                editDialog.create().show();
            }

        });

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
                UserAccountUtils.userNick = "Guest";
                updateText();
                startActivity(new Intent(UserActivity.this, LoginActivity.class));
            }
        });

    }

    void updateText(){
        userID.setText(" ID : " + UserAccountUtils.userID);
        userNick.setText(UserAccountUtils.userNick);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateText();
        if(UserAccountUtils.userID.equals("Guest")){
            btnLogout.setText("立即登录");
        }else{
            btnLogout.setText("退出登录");
        }
    }
}
