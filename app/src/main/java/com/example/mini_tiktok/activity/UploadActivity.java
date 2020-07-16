package com.example.mini_tiktok.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.mini_tiktok.R;
import com.example.mini_tiktok.net.GetVideosResponse;
import com.example.mini_tiktok.net.IMiniDouyinService;
import com.example.mini_tiktok.net.ImageHelper;
import com.example.mini_tiktok.net.PostVideoResponse;
import com.example.mini_tiktok.net.Video;
import com.example.mini_tiktok.utils.ImageUtils;
import com.example.mini_tiktok.utils.ResourceUtils;
import com.example.mini_tiktok.utils.UserAccountUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UploadActivity extends Activity {

    private static final int PICK_IMAGE = 1;
    private static final int CAMERA_IMAGE = 2;
    private static final int PICK_VIDEO = 3;
    private static final int CAMERA_VIDEO = 4;
    private static final String TAG = "UploadActivityTAG";
    private static final String myName = "LYC";
    private final static int PHOTO_MODE = 1;
    private final static int VIDEO_MODE = 2;
    public String mSelectedImagePath;
    private String mSelectedVideoPath;
    private Button btnFileImage;
    private Button btnCameraImage;
    private Button btnFileVideo;
    private Button btnCameraVideo;
    private Button btnUpload;
    private TextView text1;
    private TextView text2;
    private ImageView imageView;
    private VideoView videoView;

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(IMiniDouyinService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private IMiniDouyinService miniDouyinService = retrofit.create(IMiniDouyinService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_page);
        initBtns();
    }

    private void initBtns() {
        btnFileImage = findViewById(R.id.btn_choose_file_image);
        btnCameraImage = findViewById(R.id.btn_choose_camera_image);
        btnFileVideo = findViewById(R.id.btn_choose_file_video);
        btnCameraVideo = findViewById(R.id.btn_choose_camera_video);
        btnUpload = findViewById(R.id.upload);
        imageView = findViewById(R.id.imageView);
        videoView = findViewById(R.id.videoView);
        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);

        btnFileImage.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });
        mSelectedImagePath = null;
        mSelectedVideoPath = null;

        btnCameraImage.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UploadActivity.this,CameraActivity.class);
                intent.putExtra("MODE",PHOTO_MODE);
                startActivityForResult(intent,CAMERA_IMAGE);
            }
        });

        btnFileVideo.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseVideo();
            }
        });

        btnCameraVideo.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UploadActivity.this,CameraActivity.class);
                intent.putExtra("MODE",VIDEO_MODE);
                startActivityForResult(intent,CAMERA_VIDEO);
            }
        });

        btnUpload.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSelectedVideoPath != null && mSelectedImagePath != null) {
                    postVideo();
                } else {
                    Toast.makeText(UploadActivity.this,"请选择视频及其封面",Toast.LENGTH_SHORT).show();
                }
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mPlayer) {
                mPlayer.start();
                mPlayer.setLooping(true);
            }
        });
    }
    public void chooseImage() {
        Log.d(TAG,"Begin to choose image");
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    public void chooseVideo() {
        Log.d(TAG,"Begin to choose video");
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Video"), PICK_VIDEO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult() called with: requestCode = ["
                + requestCode
                + "], resultCode = ["
                + resultCode
                + "], data = ["
                + data
                + "]");

        if (resultCode == RESULT_OK && null != data) {
            if (requestCode == PICK_IMAGE) {
                mSelectedImagePath = ResourceUtils.getRealPath(UploadActivity.this,data.getData());
            } else if (requestCode == PICK_VIDEO) {
                mSelectedVideoPath = ResourceUtils.getRealPath(UploadActivity.this,data.getData());
            } else if(requestCode == CAMERA_IMAGE){
                mSelectedImagePath = data.getStringExtra("Path");
            } else if(requestCode == CAMERA_VIDEO){
                mSelectedVideoPath = data.getStringExtra("Path");
            }

            if(requestCode == PICK_IMAGE || requestCode == CAMERA_IMAGE){
                ImageUtils.displayImage(imageView,mSelectedImagePath);
            } else if(requestCode == PICK_VIDEO || requestCode == CAMERA_VIDEO){
                videoView.setVideoPath(mSelectedVideoPath);
                videoView.start();
            }
        }else{
            Log.d(TAG,"Unreadable result");
        }

        if(mSelectedImagePath != null){
            text1.setTextColor(getResources().getColor(R.color.gray));
        }
        if(mSelectedVideoPath != null){
            text2.setTextColor(getResources().getColor(R.color.gray));
        }
    }

    private MultipartBody.Part getMultipartFromPath(String name, String path) {
        File f = new File(path);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), f);
        return MultipartBody.Part.createFormData(name, f.getName(), requestFile);
    }

    private void postVideo() {
        Log.d(TAG,"Begin to post");
        btnUpload.setText("POSTING...");
        btnUpload.setEnabled(false);
        MultipartBody.Part coverImagePart;
        MultipartBody.Part videoPart;


        /*coverImagePart = getMultipartFromUri("cover_image", mSelectedImage);
        videoPart = getMultipartFromUri("video", mSelectedVideo);*/

        coverImagePart = getMultipartFromPath("cover_image",mSelectedImagePath);
        videoPart = getMultipartFromPath("video", mSelectedVideoPath);

        Log.d(TAG,"Get file");
        miniDouyinService.postVideo(UserAccountUtils.userID, myName, coverImagePart, videoPart).enqueue(
                new Callback<PostVideoResponse>() {
                    @Override
                    public void onResponse(Call<PostVideoResponse> call, Response<PostVideoResponse> response) {
                        Log.d(TAG,"Get response");
                        if (response.body() != null) {
                            Toast.makeText(UploadActivity.this, response.body().toString(), Toast.LENGTH_SHORT)
                                    .show();
                        }
                        finish();
                    }

                    @Override
                    public void onFailure(Call<PostVideoResponse> call, Throwable throwable) {
                        Log.d(TAG,"Post Failed : " + throwable.getMessage() + "\n" + throwable.getCause() + "\n" + throwable.getStackTrace());
                        btnUpload.setEnabled(true);
                        btnUpload.setText(getString(R.string.upload));
                        Toast.makeText(UploadActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
