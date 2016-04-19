package com.hong.app.rxjavatest.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hong.app.rxjavatest.R;
import com.hong.app.rxjavatest.network.AccountNetworkManager;

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

    @Bind(R.id.login_progress)
    ProgressBar loginProgress;
    @Bind(R.id.avatar)
    ImageView avatar;
    @Bind(R.id.user_name)
    EditText userName;
    @Bind(R.id.email)
    AutoCompleteTextView email;
    @Bind(R.id.password)
    EditText password;
    @Bind(R.id.register_button)
    Button registerButton;
    @Bind(R.id.login_button)
    Button loginButton;
    @Bind(R.id.email_login_form)
    LinearLayout emailLoginForm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.register_button, R.id.login_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_button:
                registerUser();
                break;
            case R.id.login_button:
                break;
        }
    }

    private void registerUser() {

        final String username = userName.getText().toString();
        final String pass = password.getText().toString();

        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {


                String response = AccountNetworkManager.signup(username, pass);
                subscriber.onNext(response);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String str) {
                        Toast.makeText(LoginActivity.this, str, Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
