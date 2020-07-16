package com.example.mini_tiktok.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.mini_tiktok.MainActivity;
import com.example.mini_tiktok.R;
import com.example.mini_tiktok.activity.VideoActivity;
import com.example.mini_tiktok.net.GetVideosResponse;
import com.example.mini_tiktok.net.IMiniDouyinService;
import com.example.mini_tiktok.net.ImageHelper;
import com.example.mini_tiktok.net.Video;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VideoListActivity extends Activity {
    private RecyclerView mRv;
    private Button mBtnRefresh;
    private Button mBtnLogin;
    private Button mBtnUpload;
    private Button mBtnMine;
    private List<Video> mVideos = new ArrayList<>();
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(IMiniDouyinService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private IMiniDouyinService miniDouyinService = retrofit.create(IMiniDouyinService.class);
    private List<String> mIdList;
    private static final String TAG = "VideoListActivityTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        mBtnRefresh = findViewById(R.id.btn_refresh);
        mBtnLogin = findViewById(R.id.btn_main_login);
        mBtnUpload = findViewById(R.id.btn_main_upload);
        mBtnMine = findViewById(R.id.btn_main_mine);

        Intent intent = getIntent();
        mIdList = (List<String>) intent.getStringArrayListExtra("IDs");
        if(mIdList != null) {
            Log.d(TAG, "List size : " + mIdList.size());
            for(int i=0;i<mIdList.size();++i) Log.d(TAG, "List item : " + mIdList.get(i));
        } else
            Log.d(TAG,"List size : " + "null");
        bind(mBtnLogin, LoginActivity.class);
        bind(mBtnUpload, UploadActivity.class);
        bind(mBtnMine, UserActivity.class);

        initRecyclerView();
        fetchFeed(mBtnRefresh);

    }

    void bind(Button btn, final Class<?> nxt){
        btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VideoListActivity.this,nxt));
            }
        });
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
        }

        public void bind(final Activity activity, final Video video, final int i, final List<Video> mVideos) {
            ImageHelper.displayWebImage(video.imageUrl, img);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VideoActivity.launch(activity, i, mVideos);
                }
            });
        }
    }

    private void initRecyclerView() {
        mRv = findViewById(R.id.video_list);
        StaggeredGridLayoutManager layout = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRv.setLayoutManager(layout);
       // mRv.setLayoutManager(new LinearLayoutManager(this));
        mRv.setAdapter(new RecyclerView.Adapter<MyViewHolder>() {
            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                return new MyViewHolder(
                        LayoutInflater.from(VideoListActivity.this)
                                .inflate(R.layout.video_item_view, viewGroup, false));
            }

            @Override
            public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {
                final Video video = mVideos.get(i);
                viewHolder.bind(VideoListActivity.this, video, i, mVideos);
            }

            @Override
            public int getItemCount() {
                return mVideos.size();
            }
        });
    }

    public void fetchFeed(View view) {
        mBtnRefresh.setText("requesting...");
        mBtnRefresh.setEnabled(false);
        miniDouyinService.getVideos().enqueue(new Callback<GetVideosResponse>() {
            @Override
            public void onResponse(Call<GetVideosResponse> call, Response<GetVideosResponse> response) {
                if (response.body() != null && response.body().videos != null) {
                    mVideos = response.body().videos;
                    if (mIdList != null && !mIdList.isEmpty()) {
                        Iterator<Video> item = mVideos.iterator();
                        while (item.hasNext()) {
                            boolean result = mIdList.contains(item.next().studentId);
                            if (!result) {
                                item.remove();
                            }
                        }
                    }
                    mRv.getAdapter().notifyDataSetChanged();
                }
                mBtnRefresh.setText("刷新");
                mBtnRefresh.setEnabled(true);
            }

            @Override
            public void onFailure(Call<GetVideosResponse> call, Throwable throwable) {
                mBtnRefresh.setText("刷新");
                mBtnRefresh.setEnabled(true);
                Toast.makeText(VideoListActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
