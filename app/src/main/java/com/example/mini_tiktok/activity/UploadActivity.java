package com.example.mini_tiktok.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mini_tiktok.R;
import com.example.mini_tiktok.net.GetVideosResponse;
import com.example.mini_tiktok.net.IMiniDouyinService;
import com.example.mini_tiktok.net.ImageHelper;
import com.example.mini_tiktok.net.PostVideoResponse;
import com.example.mini_tiktok.net.Video;
import com.example.mini_tiktok.utils.ResourceUtils;

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

public class UploadActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private static final int CAMERA_IMAGE = 2;
    private static final int PICK_VIDEO = 3;
    private static final int CAMERA_VIDEO = 4;
    private static final String TAG = "UploadActivity";
    private static final String myID = "15097722150";
    private static final String myName = "LYC";
    public Uri mSelectedImage;
    private Uri mSelectedVideo;
    private Button btnFileImage;
    private Button btnCameraImage;
    private Button btnFileVideo;
    private Button btnCameraVideo;
    private Button btnUpload;

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

        btnFileImage.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        btnCameraImage.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

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

            }
        });
        /*
        mBtn = findViewById(R.id.btn);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (mSelectedVideo != null && mSelectedImage != null) {
                        //@TODO 3调用上传功能
                        postVideo();
                    } else {
                        throw new IllegalArgumentException("error data uri, mSelectedVideo = "
                                + mSelectedVideo
                                + ", mSelectedImage = "
                                + mSelectedImage);
                    }
                } else if ((getString(R.string.success_try_refresh).equals(s))) {
                    mBtn.setText(R.string.select_an_image);
                }
            }
        });

        mBtnRefresh = findViewById(R.id.btn_refresh);
        */
    }
    public void chooseImage() {
        Log.d(TAG,"Begin to choose image");
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                PICK_IMAGE);
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
                mSelectedImage = data.getData();
                Log.d(TAG, "selectedImage = " + mSelectedImage);
                //mBtn.setText(R.string.select_a_video);
            } else if (requestCode == PICK_VIDEO) {
                mSelectedVideo = data.getData();
                Log.d(TAG, "mSelectedVideo = " + mSelectedVideo);
                //mBtn.setText(R.string.post_it);
            }
        }
    }

    private MultipartBody.Part getMultipartFromUri(String name, Uri uri) {
        File f = new File(ResourceUtils.getRealPath(UploadActivity.this, uri));
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), f);
        return MultipartBody.Part.createFormData(name, f.getName(), requestFile);
    }

    private void postVideo() {
        Log.d(TAG,"Begin to post");
        //mBtn.setText("POSTING...");
        //mBtn.setEnabled(false);
        MultipartBody.Part coverImagePart;
        MultipartBody.Part videoPart;
        try {
            coverImagePart = getMultipartFromUri("cover_image", mSelectedImage);
            videoPart = getMultipartFromUri("video", mSelectedVideo);
        }catch(Exception e){
            Log.e(TAG,e.getMessage());
            return ;
        }
        Log.d(TAG,"Get file");

        miniDouyinService.postVideo(myID, myName, coverImagePart, videoPart).enqueue(
                new Callback<PostVideoResponse>() {
                    @Override
                    public void onResponse(Call<PostVideoResponse> call, Response<PostVideoResponse> response) {
                        if (response.body() != null) {
                            Toast.makeText(UploadActivity.this, response.body().toString(), Toast.LENGTH_SHORT)
                                    .show();
                        }
                        //mBtn.setText(R.string.select_an_image);
                        //mBtn.setEnabled(true);
                    }

                    @Override
                    public void onFailure(Call<PostVideoResponse> call, Throwable throwable) {
                        //mBtn.setText(R.string.select_an_image);
                        //mBtn.setEnabled(true);
                        Toast.makeText(UploadActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
