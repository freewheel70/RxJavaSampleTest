package com.hong.app.freegank.pretty_girls;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hong.app.freegank.R;
import com.hong.app.freegank.utils.FileUtil;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Freewheel on 2016/4/23.
 */
public class PrettyGirlCollectionActivity extends AppCompatActivity {

    private static final String TAG = "PrettyGirlCollectionActivity";

    private static final int REQUEST_CODE = 2;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    File[] beautyFileList;

    private LayoutInflater inflater;

    private BeautyAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pretty_collection);
        ButterKnife.bind(this);

        this.inflater = LayoutInflater.from(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.my_image_collection);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initFiledata();
        adapter.notifyDataSetChanged();
    }

    private void initFiledata() {
        File dir = new File(FileUtil.PRIVATE_IMAGE_STORAGE_DIR);
        beautyFileList = dir.listFiles();
    }

    private void initRecyclerView() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new BeautyAdapter();
        recyclerView.setAdapter(adapter);

    }

    public class BeautyAdapter extends RecyclerView.Adapter<BeautyViewholder> {

        @Override
        public BeautyViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.item_beauty, parent, false);
            BeautyViewholder viewholder = new BeautyViewholder(view);
            return viewholder;
        }

        @Override
        public void onBindViewHolder(BeautyViewholder holder, int position) {

            final File file = beautyFileList[position];

            Glide.with(PrettyGirlCollectionActivity.this)
                    .load(file)
                    .into(holder.image);

            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PrettyGirlCollectionActivity.this, PrettyGirlDetailActivity.class);
                    intent.putExtra(PrettyGirlDetailActivity.EXTRA_SOURCE, PrettyGirlDetailActivity.EXTRA_SOURCE_COLLECTION);
                    intent.putExtra(PrettyGirlDetailActivity.EXTRA_PRETTY, file.getAbsolutePath());
                    startActivityForResult(intent, REQUEST_CODE);
                }
            });
        }

        @Override
        public int getItemCount() {
            return beautyFileList.length;
        }
    }

    public class BeautyViewholder extends RecyclerView.ViewHolder {

        @Bind(R.id.image)
        ImageView image;

        public BeautyViewholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String filePath = data.getStringExtra(PrettyGirlDetailActivity.EXTRA_PRETTY);
            FileUtil.deleteImage(filePath);
        }
    }
}
