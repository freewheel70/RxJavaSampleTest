package com.hong.app.rxjavatest.Blogs;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hong.app.rxjavatest.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/9.
 */
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
