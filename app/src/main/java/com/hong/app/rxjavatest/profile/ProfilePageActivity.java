package com.hong.app.rxjavatest.profile;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hong.app.rxjavatest.Events.ServerSyncBlogEvent;
import com.hong.app.rxjavatest.R;
import com.hong.app.rxjavatest.database.Blog;
import com.hong.app.rxjavatest.database.User;
import com.hong.app.rxjavatest.services.NetworkSyncService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/4/22.
 */
public class ProfilePageActivity extends AppCompatActivity {

    @Bind(R.id.avatar)
    ImageView avatar;

    @Bind(R.id.user_name)
    TextView userName;

    @Bind(R.id.logout_button)
    Button logoutButton;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        EventBus.getDefault().register(this);

        User user = User.getUser();
        userName.setText(user.username);
    }

    @OnClick(R.id.logout_button)
    public void onClick() {
        showProgressDialog();
        startService(new Intent(ProfilePageActivity.this, NetworkSyncService.class));
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
        }

        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handlerServerSyncResult(ServerSyncBlogEvent event) {

        dismissProgressDialog();
        if (event.success) {
            deleteAllDataAndLogout();
        } else {
            showDeleteDataAlert();
        }
    }

    private void dismissProgressDialog() {

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void deleteAllDataAndLogout() {
        User.getUser().delete();
        Blog.deleteAll();
        finish();
    }

    private void showDeleteDataAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.logout)
                .setMessage(R.string.logout_with_delete_all_data)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAllDataAndLogout();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
