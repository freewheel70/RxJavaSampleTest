package com.hong.app.rxjavatest;

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

import com.hong.app.rxjavatest.CustomViews.OnRecyclerViewItemClickListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/4/17.
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


    protected RecyclerView.Adapter adapter;
    protected LayoutInflater inflater;

    protected LinearLayoutManager layoutManager;

    protected int SIZE_OF_IMAGES_PER_REQUEST = 10;
    protected int currentPage = 1;


    protected boolean hasInitFirstPage = false;
    protected boolean isRequesting = false;

    protected int position = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view = inflater.inflate(getContainerViewId(), container, false);
        ButterKnife.bind(this, view);
        this.inflater = inflater;
        EventBus.getDefault().register(this);

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

        sendRequest();
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
        sendRequest();
    }

    protected boolean sendRequest() {
        if (isRequesting) {
            Log.d(TAG, "sendRequest: isRequesting true, ignore current request");
            return false;
        }

        isRequesting = true;

        return true;
    }

    @Subscribe
    public void refreshIfNeed(RefreshEvent refreshEvent) {
        Log.d(TAG, "refreshIfNeed() called with: " + "refreshEvent = [" + refreshEvent + "]");
        int diff = refreshEvent.position - this.position;
        if (diff > -2 && diff < 2) {
            sendRequest();
        }
    }


    public static class RefreshEvent {
        public int position;

        public RefreshEvent(int position) {
            this.position = position;
        }

        @Override
        public String toString() {
            return "RefreshEvent{" +
                    "position=" + position +
                    '}';
        }
    }

    protected void refreshRecyclerView() {
        adapter.notifyDataSetChanged();
        currentPage++;
        enableRequest();
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
        EventBus.getDefault().unregister(this);
//        ButterKnife.unbind(this);
    }
}

