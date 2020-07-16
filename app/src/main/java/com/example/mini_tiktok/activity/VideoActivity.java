package com.example.mini_tiktok.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mini_tiktok.R;
import com.example.mini_tiktok.adapter.VideoAdapter;
import com.example.mini_tiktok.net.Video;
import com.example.mini_tiktok.utils.AttentionUtils;
import com.lmx.library.media.VideoPlayRecyclerView;

import java.io.Serializable;
import java.util.List;

public class VideoActivity extends Activity {

    private static String TAG= "video_activity";
    private VideoPlayRecyclerView mRvVideo;
    private VideoAdapter adapter;
    private static List<Video> mVideos;

    public static void launch(Activity activity, int i, List<Video> Videos) {
        Log.i(TAG, "init video activity");
        Intent intent = new Intent(activity, VideoActivity.class);
        intent.putExtra("number", i);
        Log.i(TAG, "序列化");
        mVideos = Videos;
        Log.i(TAG, "序列化成功");
        activity.startActivity(intent);
        Log.i(TAG, "start");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i(TAG, "create video activity begin");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        Log.i(TAG, "create video activity");
        initView();
    }

    private void initView() {

        mRvVideo = findViewById(R.id.rvVideo);
        int i = getIntent().getIntExtra("number", 0);
        adapter = new VideoAdapter(this, i, mVideos);
        mRvVideo.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.release();
    }

    public void Attention(int Number, String userId, List<Video> videos){
        if(AttentionUtils.checkAttention(VideoActivity.this,userId, videos.get(Number).studentId)){

        }
    }
}
