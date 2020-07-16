package com.example.mini_tiktok.adapter;

import android.animation.Animator;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.mini_tiktok.R;
import com.example.mini_tiktok.activity.VideoActivity;
import com.example.mini_tiktok.activity.VideoListActivity;
import com.example.mini_tiktok.net.Video;
import com.example.mini_tiktok.utils.AttentionUtils;
import com.example.mini_tiktok.utils.UserAccountUtils;
import com.example.mini_tiktok.view.VideoLoadingProgressbar;
import com.lmx.library.media.VideoPlayAdapter;

import java.util.ArrayList;
import java.util.List;

public class VideoAdapter extends VideoPlayAdapter<VideoAdapter.ViewHolder> {
    private Context mContext;

    private VideoPlayer videoPlayer;
    private TextureView textureView;
    private String videoUrl;
    private String pictureUrl;
    private String NickName;
    private TextView tvNickName;
    private Button buttonAttention;
    private String TAG = "video_activity";
    private int Number;
    private LottieAnimationView animationView;
    private String userId;

    private List<Video> mVideos;

    private int mCurrentPosition;
    private ViewHolder mCurrentHolder;


    public VideoAdapter(VideoActivity mContext, int i, List<Video> Videos) {
        Log.i(TAG, "use adapter");
        this.mContext = mContext;
        this.Number = i-1;
        this.mVideos = Videos;
        Log.i(TAG, "video_url_final =" +videoUrl );
        Log.i(TAG, "p_url_final = "+pictureUrl);
        videoPlayer = new VideoPlayer();
        textureView = new TextureView(mContext);
        videoPlayer.setTextureView(textureView);
        this.userId = UserAccountUtils.userID;
        //mCurrentHolder.setIsRecyclable(false);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_item_video, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Number++;
        pictureUrl = mVideos.get(Number).imageUrl;
        videoUrl = mVideos.get(Number).videoUrl;
        NickName = mVideos.get(Number).userName;
        String attentionId = mVideos.get(Number).studentId;
        tvNickName.setText(NickName);
        animationView.setProgress(0);
        playAnimation();
        //Attention(attentionId);
        Log.i(TAG, "picture_url = "+pictureUrl);
        RequestOptions options = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(mContext).load(pictureUrl).apply(options).into(holder.ivCover);
    }

    @Override
    public int getItemCount() {
        return 200;
    }

    @Override
    public void onPageSelected(int itemPosition, View itemView) {
        mCurrentPosition = itemPosition;
        mCurrentHolder = new ViewHolder(itemView);
        playVideo();
    }


    public void Attention(final String attentionId){
        new Thread(){
            @Override
            public void run(){
                buttonAttention.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        changeButton(attentionId);
                    }
                });

                if(AttentionUtils.checkAttention(mContext,userId, attentionId)){
                    buttonAttention.setText("已关注");
                }
                else{
                    buttonAttention.setText("关注");
                }
            }
        }.start();

    }

    private void changeButton(String attentionId){
        if(buttonAttention.getText() == "关注"){
            AttentionUtils.insertAttention(mContext, userId, attentionId);
        }
        else {
            AttentionUtils.deleteAttention(mContext, userId, attentionId);
        }
    }

    private void playAnimation(){
        animationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animationView.playAnimation();
            }
        });
    }

    private void playVideo() {
        videoPlayer.reset();
        mCurrentHolder.pbLoading.setVisibility(View.VISIBLE);
        videoPlayer.setOnStateChangeListener(new VideoPlayer.OnStateChangeListener() {
            @Override
            public void onReset() {
                mCurrentHolder.ivCover.setVisibility(View.VISIBLE);
                mCurrentHolder.pbLoading.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onRenderingStart() {
                mCurrentHolder.ivCover.setVisibility(View.GONE);
                mCurrentHolder.pbLoading.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onProgressUpdate(float per) {
            }

            @Override
            public void onPause() {
                mCurrentHolder.pbLoading.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onStop() {
                mCurrentHolder.ivCover.setVisibility(View.VISIBLE);
                mCurrentHolder.pbLoading.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onComplete() {
                videoPlayer.start();
            }
        });
        if (textureView.getParent() != mCurrentHolder.flVideo) {
            if (textureView.getParent() != null) {
                ((FrameLayout) textureView.getParent()).removeView(textureView);
            }
            mCurrentHolder.flVideo.addView(textureView);
        }
        videoPlayer.setDataSource(videoUrl);
        videoPlayer.prepare();
    }

    public void release() {
        animationView.setProgress(0);
        videoPlayer.release();
    }
    class ViewHolder extends RecyclerView.ViewHolder{

        private FrameLayout flVideo;
        private ImageView ivCover;
        private VideoLoadingProgressbar pbLoading;


        ViewHolder(@NonNull View itemView) {
            super(itemView);
            flVideo = itemView.findViewById(R.id.flVideo);
            ivCover = itemView.findViewById(R.id.ivCover);
            pbLoading = itemView.findViewById(R.id.pbLoading);
            tvNickName = itemView.findViewById(R.id.tvNickname);
            animationView = itemView.findViewById(R.id.like);
            buttonAttention = itemView.findViewById(R.id.btn_attention);
        }
    }
}
