package com.hong.app.rxjavatest.Blogs.BlogFragments;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hong.app.rxjavatest.BasePageFragment;
import com.hong.app.rxjavatest.Blogs.BlogBean;
import com.hong.app.rxjavatest.Blogs.BlogBrowserActivity;
import com.hong.app.rxjavatest.R;
import com.hong.app.rxjavatest.Utils.DateUtil;

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
public class BaseBlogFragment extends BasePageFragment {

    private static final String TAG = "BaseBlogFragment";

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
    public void onHiddenChanged(boolean hidden) {
        Log.d(TAG, "onHiddenChanged() called with: " + "hidden = [" + hidden + "]");
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
    }

    @Override
    protected void sendRequest() {

        Observable.create(new rx.Observable.OnSubscribe<List<BlogBean>>() {
            @Override
            public void call(Subscriber<? super List<BlogBean>> subscriber) {
                List<BlogBean> blogList = requestBlogList();
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
                        enableRequest();
                    }

                    @Override
                    public void onNext(List<BlogBean> beanList) {
                        refreshDataList(beanList);
                        refreshRecyclerView();
                    }
                });
    }

    protected void refreshDataList(List<BlogBean> beanList) {
        blogBeanList.addAll(beanList);
    }

    protected List<BlogBean> requestBlogList() {
        throw new UnsupportedOperationException("Must implement");
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
            holder.publishDate.setText(DateUtil.getDisplayDateString(blogBean.getPublishedAt()));
            initTagLabel(holder.tag, blogBean.getUrl());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), BlogBrowserActivity.class);
                    intent.putExtra(BlogBrowserActivity.EXTRA_BLOG, blogBean);
                    startActivity(intent);
                }
            });
        }

        private void initTagLabel(TextView tag, String urlStr) {
            if (urlStr.contains("github")) {
                tag.setText("Github");
                tag.setBackgroundResource(R.drawable.bg_github_tag);
            } else if (urlStr.contains("jianshu")) {
                tag.setText("简书");
                tag.setBackgroundResource(R.drawable.bg_jianshu_tag);
            } else if (urlStr.contains("weixin")) {
                tag.setText("微信");
                tag.setBackgroundResource(R.drawable.bg_weixin_tag);
            } else {
                tag.setText("博客");
                tag.setBackgroundResource(R.drawable.bg_blog_tag);
            }
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
