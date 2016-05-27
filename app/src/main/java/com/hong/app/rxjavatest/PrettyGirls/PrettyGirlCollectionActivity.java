package com.hong.app.rxjavatest.PrettyGirls;

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
import com.hong.app.rxjavatest.R;
import com.hong.app.rxjavatest.Utils.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/4/23.
 */
public class PrettyGirlCollectionActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    List<File> beautyFileList = new ArrayList<>();

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

//        toolbar.setTitle(R.string.my_image_collection);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initFiledata();
        initRecyclerView();
    }

    private void initFiledata() {
        File dir = new File(FileUtil.PRIVATE_IMAGE_STORAGE_DIR);
        File[] files = dir.listFiles();
        beautyFileList = Arrays.asList(files);
    }

    private void initRecyclerView() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
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

            final File file = beautyFileList.get(position);

            Glide.with(PrettyGirlCollectionActivity.this)
                    .load(file)
                    .into(holder.image);

            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PrettyGirlCollectionActivity.this, PrettyGirlDetailActivity.class);
                    intent.putExtra(PrettyGirlDetailActivity.EXTRA_SOURCE, PrettyGirlDetailActivity.EXTRA_SOURCE_COLLECTION);
                    intent.putExtra(PrettyGirlDetailActivity.EXTRA_PRETTY, file.getAbsolutePath());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return beautyFileList.size();
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
}
