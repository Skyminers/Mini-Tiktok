package com.example.mini_tiktok.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.mini_tiktok.R;
import com.example.mini_tiktok.utils.ImageUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class CameraActivity extends Activity implements SurfaceHolder.Callback{

    private Button btn;
    private SurfaceView surfaceView;
    private SurfaceHolder holder;
    private Camera mCamera;
    private MediaRecorder mMediaRecorder;
    private boolean isRecording;
    private final static String[] permissions = new String[]{Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO};
    private final static int PERMISSION_CODE = 1;
    private final static int SET_CLICKABLE = 1;
    private final static int PHOTO_MODE = 1;
    private final static int VIDEO_MODE = 2;
    private final static String TAG = "CameraActivity";
    private String mp4Path;

    private void requestForPermission(){
        if(PackageManager.PERMISSION_GRANTED != getPackageManager().checkPermission(permissions[0], getPackageName())
        || PackageManager.PERMISSION_GRANTED != getPackageManager().checkPermission(permissions[1], getPackageName()))
            ActivityCompat.requestPermissions(this,permissions,PERMISSION_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        final int cameraMode = getIntent().getIntExtra("MODE",0);
        if(cameraMode != 1 && cameraMode != 2) finish();

        requestForPermission();
        initCamera();

        btn = findViewById(R.id.btn);
        surfaceView = findViewById(R.id.surfaceView);
        holder = surfaceView.getHolder();

        if(cameraMode == PHOTO_MODE) {
            btn.setText(getString(R.string.take_photo));
        }else if(cameraMode == VIDEO_MODE){
            btn.setText(getString(R.string.take_video));
        }


        final Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {// 该方法在主线程中运行
                super.handleMessage(msg);
                switch (msg.what) {
                    case SET_CLICKABLE:
                        if(btn != null) {
                            btn.setClickable(true);
                        }
                        break;
                }
            }
        };

        holder.addCallback(this);

        final Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {
                FileOutputStream fos = null;
                String filePath = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + File.separator + "1.jpg";
                File file = new File(filePath);
                try {
                    fos = new FileOutputStream(file);
                    fos.write(bytes);
                    fos.close();
                    Intent intent = new Intent();
                    intent.putExtra("Path",filePath);
                    setResult(RESULT_OK,intent);
                    finish();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    mCamera.startPreview();
                    if(fos != null){
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };

        btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn.setClickable(false);
                new Timer("setAble").schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Message msg = mHandler.obtainMessage(SET_CLICKABLE);
                        mHandler.sendMessage(msg);
                    }}, 1000);

                switch(cameraMode){
                    case PHOTO_MODE:{
                        mCamera.takePicture(null,null,mPictureCallback);
                        break;
                    }
                    case VIDEO_MODE:{
                        record();
                        break;
                    }

                }

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mCamera == null){
            initCamera();
        }
        mCamera.startPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCamera.stopPreview();
    }

    private void initCamera(){
        // 0 后置摄像头
        // 1 前置摄像头
        mCamera = Camera.open(0);
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPictureFormat(ImageFormat.JPEG);
        if(parameters.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }
        parameters.set("orientation","portrait");
        parameters.set("rotation",90);
        mCamera.setParameters(parameters);
        mCamera.setDisplayOrientation(90);
    }

    private boolean prepareVideoRecorder(){
        mMediaRecorder = new MediaRecorder();
        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
        mp4Path = getOutputMediaPath();
        mMediaRecorder.setOutputFile(mp4Path);
        mMediaRecorder.setOrientationHint(90);
        try {
            mMediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            mMediaRecorder.release();
            return false;
        }
        return true;
    }

    public void record(){
        if(isRecording){

            btn.setText("录制");
            mMediaRecorder.setOnErrorListener(null);
            mMediaRecorder.setOnInfoListener(null);
            mMediaRecorder.setPreviewDisplay(null);
            try {
                mMediaRecorder.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
            mCamera.lock();

            Intent intent = new Intent();
            intent.putExtra("Path",mp4Path);
            setResult(RESULT_OK,intent);
            finish();

        }else{
            if(prepareVideoRecorder()){
                btn.setText("停止");
                mMediaRecorder.start();
            }
        }
        isRecording = !isRecording;
    }


    public String getOutputMediaPath(){
        File mediaDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaDir,"IMG_" + timeStamp + ".mp4");
        if(!mediaFile.exists()){
            mediaFile.getParentFile().mkdirs();
        }
        return mediaFile.getAbsolutePath();
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        if(holder.getSurface() == null) return;
        try {
            mCamera.stopPreview();
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }
}