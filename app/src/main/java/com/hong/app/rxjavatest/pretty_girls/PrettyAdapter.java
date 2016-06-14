package com.hong.app.rxjavatest.pretty_girls;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.hong.app.rxjavatest.blogs.BlogBean;
import com.hong.app.rxjavatest.R;

import java.util.List;

/**
 * Created by Freewheel on 2016/5/9.
 */
class PrettyAdapter extends RecyclerView.Adapter<PrettyViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    private List<BlogBean> prettyList;

    public PrettyAdapter(Context context, LayoutInflater inflater, List<BlogBean> prettyList) {
        this.context = context;
        this.inflater = inflater;
        this.prettyList = prettyList;
    }

    @Override
    public PrettyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_beauty, parent, false);
        PrettyViewHolder viewHolder = new PrettyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PrettyViewHolder holder, int position) {

        final BlogBean blogBean = prettyList.get(position);
        final String imageStr = blogBean.getUrl();

        Glide.with(context)
                .load(imageStr)
                .into(holder.image);

//        holder.image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, PrettyGirlDetailActivity.class);
//                intent.putExtra(PrettyGirlDetailActivity.EXTRA_PRETTY, blogBean);
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return prettyList.size();
    }
}
