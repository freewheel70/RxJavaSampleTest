package com.hong.app.rxjavatest.profile;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hong.app.rxjavatest.R;
import com.hong.app.rxjavatest.database.User;
import com.hong.app.rxjavatest.network.AccountNetworkManager;
import com.hong.app.rxjavatest.network.NetworkResponseResult;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/4/19.
 */
public class LoginActivity extends AppCompatActivity {

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

    @Bind(R.id.email_login_form)
    LinearLayout emailLoginForm;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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

    }

    @OnClick({R.id.register_button, R.id.login_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_button:
                registerUser();
                break;
            case R.id.login_button:
                login();
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

                if (responseResult.success) {
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(new Throwable(responseResult.message));
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

                if (responseResult.success) {
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(new Throwable(responseResult.message));
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
}
