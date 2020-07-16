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
import com.lmx.library.media.VideoPlayRecyclerView;

public class VideoActivity extends AppCompatActivity {

    private VideoPlayRecyclerView mRvVideo;
    private VideoAdapter adapter;
    private String TAG = "video";

    public static void launch(Activity activity, String videoUrl, String pictureUrl ) {
        Intent intent = new Intent(activity, VideoActivity.class);
        intent.putExtra("video_url", videoUrl);
        intent.putExtra("picture_url", pictureUrl);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        initView();
    }

    private void initView() {
      /*  findViewById(R.id.ibBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
        mRvVideo = findViewById(R.id.rvVideo);
        String videoUrl = getIntent().getStringExtra("video_url");
        String pictureUrl = getIntent().getStringExtra("picture_url");
        Log.i(TAG, "url = "+videoUrl);
        adapter = new VideoAdapter(this,videoUrl, pictureUrl);
        mRvVideo.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.release();
    }
/*    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        String url = getIntent().getStringExtra("url");
        VideoView videoView = findViewById(R.id.video_container);
        final ProgressBar progressBar = findViewById(R.id.progress_bar);
        videoView.setMediaController(new MediaController(this));
        videoView.setVideoURI(Uri.parse(url));
        videoView.requestFocus();
        videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                progressBar.setVisibility(View.GONE);
            }
        });
        progressBar.setVisibility(View.VISIBLE);
    }*/
}
