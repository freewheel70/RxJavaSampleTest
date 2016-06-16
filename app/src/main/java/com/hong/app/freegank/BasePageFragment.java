package com.hong.app.freegank;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hong.app.freegank.blogs.BlogBean;
import com.hong.app.freegank.custom_views.OnRecyclerViewItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Freewheel on 2016/4/17.
 */
public abstract class BasePageFragment extends Fragment {

    private static final String TAG = "BasePageFragment";

    @Bind(R.id.recycler_view)
    protected RecyclerView recyclerView;

    @Bind(R.id.progress_bar)
    protected ProgressBar progressBar;

    @Bind(R.id.center_progress_bar)
    protected ProgressBar centerProgressBar;

    @Bind(R.id.no_content_warning)
    protected TextView noContentWarning;

    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;


    private RecyclerView.Adapter adapter;
    private LayoutInflater inflater;

    private LinearLayoutManager layoutManager;

    private int SIZE_OF_IMAGES_PER_REQUEST = 10;
    private int currentPage = 1;


    private boolean hasInitFirstPage = false;
    private boolean isRequesting = false;

    private int position = 0;

    private List<BlogBean> blogBeanList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view = inflater.inflate(getContainerViewId(), container, false);
        ButterKnife.bind(this, view);
        this.inflater = inflater;

        Bundle bundle = getArguments();
        position = bundle.getInt("pos", 0);

        initRecyclerView();
        initSwipeRefreshLayout();
        initViews();

        if (!hasInitFirstPage) {
            Log.d(TAG, "onCreateView: has not yet InitFirstPage");
            centerProgressBar.setVisibility(View.VISIBLE);
            requestOneMorePage();
        } else {
            Log.d(TAG, "onCreateView: hasInitFirstPage");
        }
        return view;
    }

    protected abstract int getContainerViewId();

    private void initRecyclerView() {

        initLayoutManager();

        recyclerView.setHasFixedSize(true);

        recyclerView.setItemAnimator(new DefaultItemAnimator());


        addItemDecoration();

        initAdapter();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!hasInitFirstPage) {
                    return;
                }

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (layoutManager.findLastVisibleItemPosition() == getDataListSize() - 1) {
                        Log.d(TAG, "onScrollStateChanged: lastvisibleitem " + layoutManager.findLastVisibleItemPosition());
                        if (!isRequesting) {
                            showProgressbar();
                            requestOneMorePage();
                        }
                    }
                }

            }
        });

        recyclerView.addOnItemTouchListener(
                new OnRecyclerViewItemClickListener(recyclerView) {
                    @Override
                    public void onItemClick(RecyclerView.ViewHolder vh) {
                        Log.d(TAG, "recyclerView onItemClick() called with: " + "vh = [" + vh + "]");
                        onRecyclerViewItemClick(vh.getLayoutPosition());
                    }

                    @Override
                    public void onItemLongClick(RecyclerView.ViewHolder vh) {
                        Log.d(TAG, "recyclerView onItemLongClick() called with: " + "vh = [" + vh + "]");
                    }
                }
        );


    }

    protected abstract void onRecyclerViewItemClick(int pos);


    private void initSwipeRefreshLayout() {
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
            }
        });

    }

    void refreshItems() {

        sendRequest(true);
    }

    protected void onItemsLoadComplete() {

        swipeRefreshLayout.setRefreshing(false);
    }

    protected abstract void addItemDecoration();

    protected abstract int getDataListSize();

    protected abstract void initAdapter();

    protected abstract void initLayoutManager();

    protected abstract void initViews();

    protected void showProgressbar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    protected void hideProgressbar() {
        centerProgressBar.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    protected void requestOneMorePage() {
        sendRequest(false);
    }

    protected boolean sendRequest(boolean isRefreshing) {
        if (isRequesting) {
            Log.d(TAG, "sendRequest: isRequesting true, ignore current request");
            return false;
        }

        isRequesting = true;

        if (isRefreshing) {
            resetPageNum();
        }

        return true;
    }


    protected void refreshDataList(List<BlogBean> beanList, boolean isRefreshing) {
        if (isRefreshing) {
            blogBeanList.clear();
        }

        blogBeanList.addAll(beanList);
    }


    protected void refreshRecyclerView() {
        adapter.notifyDataSetChanged();
    }

    protected void increasePageNum() {
        currentPage++;
    }

    protected void resetPageNum() {
        currentPage = 1;
    }

    protected void showErrorMsg(String message) {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.network_reuqest_title)
                .setMessage(getString(R.string.network_reuqest_error_warning) + message)
                .show();
    }

    protected void enableRequest() {
        isRequesting = false;
        hideProgressbar();
        hasInitFirstPage = true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView");
//        ButterKnife.unbind(this);
    }

    public RecyclerView.Adapter getAdapter() {
        return adapter;
    }

    public List<BlogBean> getBlogBeanList() {
        return blogBeanList;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public boolean isHasInitFirstPage() {
        return hasInitFirstPage;
    }

    public LayoutInflater getInflater() {
        return inflater;
    }

    public boolean isRequesting() {
        return isRequesting;
    }

    public LinearLayoutManager getLayoutManager() {
        return layoutManager;
    }

    public int getSIZE_OF_IMAGES_PER_REQUEST() {
        return SIZE_OF_IMAGES_PER_REQUEST;
    }

    public int getPosition() {
        return position;
    }

    protected void setAdapter(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }

    protected void setLayoutManager(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }
}

