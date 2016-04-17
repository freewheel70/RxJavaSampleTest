package com.hong.app.rxjavatest.Blogs;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hong.app.rxjavatest.BasePageFragment;
import com.hong.app.rxjavatest.NetworkHelper;
import com.hong.app.rxjavatest.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/4/17.
 */
public class AndroidBlogFragment extends BasePageFragment {

    private static final String TAG = "AndroidBlogFragment";

    List<BlogBean> blogBeanList = new ArrayList<>();

    @Override
    protected int getContainerViewId() {
        return R.layout.fragment_base_list;
    }

    @Override
    protected int getDataListSize() {
        return blogBeanList.size();
    }

    @Override
    protected void initLayoutManager() {
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void initAdapter() {

        adapter = new BlogAdapter();
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void sendRequest() {

        Observable.create(new rx.Observable.OnSubscribe<List<BlogBean>>() {
            @Override
            public void call(Subscriber<? super List<BlogBean>> subscriber) {
                List<BlogBean> blogList = NetworkHelper.getBlogList(SIZE_OF_IMAGES_PER_REQUEST, currentPage);
                subscriber.onNext(blogList);
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<BlogBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<BlogBean> beanList) {
                        blogBeanList.addAll(beanList);
                        refreshRecyclerView();
                    }
                });
    }

    public class BlogAdapter extends RecyclerView.Adapter<BlogViewHolder> {


        @Override
        public BlogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.item_blog, parent, false);
            BlogViewHolder viewHolder = new BlogViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(BlogViewHolder holder, int position) {

            final BlogBean blogBean = blogBeanList.get(position);
            holder.description.setText(blogBean.getDescription());
            holder.author.setText(blogBean.getWho());
            holder.tag.setText("Android");

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), BlogBrowserActivity.class);
                    intent.putExtra(BlogBrowserActivity.EXTRA_URL, blogBean.getUrl());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return blogBeanList.size();
        }

    }

    public class BlogViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.description)
        TextView description;

        @Bind(R.id.data_via_tv)
        TextView author;

        @Bind(R.id.data_tag_tv)
        TextView tag;

        @Bind(R.id.data_tag_ll)
        LinearLayout dataTagLl;

        @Bind(R.id.data_date_tv)
        TextView publishDate;

        View itemView;

        public BlogViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.itemView = itemView;
        }
    }
}
