package com.hong.app.freegank.profile;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hong.app.freegank.events.ServerSyncBlogEvent;
import com.hong.app.freegank.R;
import com.hong.app.freegank.utils.Constants;
import com.hong.app.freegank.database.Blog;
import com.hong.app.freegank.database.User;
import com.hong.app.freegank.network.AccountNetworkManager;
import com.hong.app.freegank.network.NetworkResponseResult;
import com.hong.app.freegank.services.NetworkSyncService;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Freewheel on 2016/4/19.
 */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private static final int REQUEST_SELECT_PICTURE = 20;
    private static final int REQUEST_SAVE_AVATAR = 22;
    public static final String EXTRA_CONFIRM_SAVE = "extra_confirm_save";
    public static final String EXTRA_FILE_PATH = "extra_filr_path";
    public static final int MY_PERMISSIONS_REQUEST_STORAGE = 11;
    public static final int MY_LOCATION_REQUEST_STORAGE = 12;


    @Bind(R.id.avatar)
    ImageView avatar;

    @Bind(R.id.user_name)
    EditText userName;

    @Bind(R.id.password)
    EditText password;

    @Bind(R.id.register_button)
    Button registerButton;

    @Bind(R.id.login_button)
    Button loginButton;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.login_box)
    LinearLayout loginBox;

    @Bind(R.id.profile_user_name)
    TextView profileUserName;

    @Bind(R.id.logout_button)
    Button logoutButton;

    @Bind(R.id.profile_box)
    RelativeLayout profileBox;

    private Uri tempUri;

    private ProgressDialog progressDialog;
    private boolean isLoggingOut = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        EventBus.getDefault().register(this);

        initToolbar();

        tempUri = Uri.fromFile(new File(getCacheDir(), "myavatar.jpeg"));


        User user = User.getUser();
        if (user.isAnonymous()) {
            Log.d(TAG, "onCreate: isAnonymous");
            profileBox.setVisibility(View.GONE);
            loginBox.setVisibility(View.VISIBLE);
        } else {
            Log.d(TAG, "onCreate: is NOT Anonymous");
            profileBox.setVisibility(View.VISIBLE);
            loginBox.setVisibility(View.GONE);

            profileUserName.setText(user.username);
        }

        refreshAvatarIcon();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("个人信息");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick({R.id.register_button, R.id.login_button, R.id.avatar, R.id.logout_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_button:
                registerUser();
                break;
            case R.id.login_button:
                login();
                break;
            case R.id.avatar:
                showChangeAvatarDialog();
                break;
            case R.id.logout_button:
                isLoggingOut = true;
                showProgressDialog();
                startService(new Intent(LoginActivity.this, NetworkSyncService.class));
                break;
        }
    }

    private void registerUser() {

        showProgressDialog();

        final String username = userName.getText().toString();
        final String pass = password.getText().toString();

        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                NetworkResponseResult responseResult = AccountNetworkManager.signup(username, pass);

                if (responseResult.isSuccess()) {
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(new Throwable(responseResult.getMessage()));
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        dismissProgressDialog();
                        Toast.makeText(LoginActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        User.saveUser(username, pass);
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissProgressDialog();
                        Toast.makeText(LoginActivity.this, "注册失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(String str) {

                    }
                });

    }

    private void login() {
        showProgressDialog();

        final String username = userName.getText().toString();
        final String pass = password.getText().toString();

        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                NetworkResponseResult responseResult = AccountNetworkManager.login(username, pass);

                if (responseResult.isSuccess()) {
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(new Throwable(responseResult.getMessage()));
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        dismissProgressDialog();
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        User.saveUser(username, pass);
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissProgressDialog();
                        Toast.makeText(LoginActivity.this, "登录失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Object o) {

                    }
                });

    }

    private void showChangeAvatarDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.change_avatar)
                .setItems(new CharSequence[]{"从图库选取图片"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pickImageFromGallery();
                    }
                })
                .show();
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "选取图片"), REQUEST_SELECT_PICTURE);
    }


    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
        }

        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_SELECT_PICTURE:
                    final Uri selectedUri = data.getData();
                    if (selectedUri != null) {
                        startCropActivity(data.getData());
                    } else {
                        Toast.makeText(LoginActivity.this, "无法获取图片", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case UCrop.REQUEST_CROP:
                    handleCropResult(data);
                    break;
                case REQUEST_SAVE_AVATAR:
                    boolean confirmSave = data.getBooleanExtra(EXTRA_CONFIRM_SAVE, false);
                    if (confirmSave) {
                        String avatarPath = data.getStringExtra(EXTRA_FILE_PATH);
                        refreshAvatarIcon();
                    }
                    break;

            }
        }
    }


    private void refreshAvatarIcon() {
        refreshAvatarIcon(Constants.AVATAR_PATH);
    }

    private void refreshAvatarIcon(String avatarPath) {
        Log.d(TAG, "refreshAvatarIcon() called with: " + "avatarPath = [" + avatarPath + "]");
        Glide.with(this).load(new File(avatarPath))
                .error(R.mipmap.ic_launcher)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(avatar);
    }

    private void handleCropResult(Intent result) {
        final Uri resultUri = UCrop.getOutput(result);

        if (resultUri != null) {
            Intent intent = new Intent(LoginActivity.this, AvatarResultActivity.class);
            intent.setData(resultUri);
            startActivityForResult(intent, REQUEST_SAVE_AVATAR);
        } else {
            Toast.makeText(LoginActivity.this, "无法获取裁剪结果", Toast.LENGTH_SHORT).show();
        }
    }

    private void startCropActivity(Uri dataUri) {
        UCrop uCrop = UCrop.of(dataUri, tempUri);

        uCrop = uCrop.withAspectRatio(1, 1);

        UCrop.Options options = new UCrop.Options();
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.NONE, UCropActivity.SCALE);
        uCrop = uCrop.withOptions(options);

        uCrop.start(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handlerServerSyncResult(ServerSyncBlogEvent event) {

        if (isLoggingOut) {
            dismissProgressDialog();
            if (event.isSuccess()) {
                deleteAllDataAndLogout();
            } else {
                showDeleteDataAlert();
            }
        }
    }

    private void deleteAllDataAndLogout() {
        Log.d(TAG, "deleteAllDataAndLogout() called with: " + "");
        User.deleteUser();
        Blog.deleteAll();
        finish();
    }

    private void showDeleteDataAlert() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
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
        isLoggingOut = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
