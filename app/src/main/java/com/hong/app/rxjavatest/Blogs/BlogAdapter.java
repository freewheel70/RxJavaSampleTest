package com.hong.app.rxjavatest.Blogs;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hong.app.rxjavatest.R;
import com.hong.app.rxjavatest.Utils.DateUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/5/9.
 */
class BlogAdapter extends RecyclerView.Adapter<BlogViewHolder> {

    private LayoutInflater inflater;
    private List<BlogBean> blogBeanList;
    private Context context;

    public BlogAdapter(Context context, LayoutInflater inflater, List<BlogBean> blogBeanList) {
        this.context = context;
        this.inflater = inflater;
        this.blogBeanList = blogBeanList;
    }

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
        String authorName = blogBean.getWho();
        if (authorName == null || authorName.equals("null")) {
            authorName = context.getResources().getString(R.string.anonymous_author);
        }
        holder.author.setText(authorName);
        holder.publishDate.setText(DateUtil.getDisplayDateString(blogBean.getPublishedAt()));
        initTagLabel(holder.tag, blogBean.getUrl());

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, BlogBrowserActivity.class);
//                intent.putExtra(BlogBrowserActivity.EXTRA_BLOG, blogBean);
//                context.startActivity(intent);
//            }
//        });
    }

    private void initTagLabel(TextView tag, String urlStr) {
        if (urlStr.contains("github")) {
            tag.setText(R.string.tag_github);
            tag.setBackgroundResource(R.drawable.bg_github_tag);
        } else if (urlStr.contains("jianshu")) {
            tag.setText(R.string.tag_jianshu);
            tag.setBackgroundResource(R.drawable.bg_jianshu_tag);
        } else if (urlStr.contains("weixin")) {
            tag.setText(R.string.tag_wechat);
            tag.setBackgroundResource(R.drawable.bg_weixin_tag);
        } else {
            tag.setText(R.string.tag_blog);
            tag.setBackgroundResource(R.drawable.bg_blog_tag);
        }
    }

    @Override
    public int getItemCount() {
        return blogBeanList.size();
    }

}