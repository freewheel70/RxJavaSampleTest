package com.hong.app.rxjavatest.PrettyGirls;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.hong.app.rxjavatest.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/9.
 */
public class PrettyViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.image)
    ImageView image;

    public PrettyViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}