package com.hong.app.rxjavatest.pretty_girls;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.hong.app.rxjavatest.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Freewheel on 2016/5/9.
 */
class PrettyViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.image)
    ImageView image;

    public PrettyViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}