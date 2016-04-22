package com.hong.app.rxjavatest.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by Administrator on 2016/4/20.
 */
@ModelContainer
@Table(database = FreeGankDatabase.class)
public class User extends BaseModel {

    private static final String DEFAULT_NAME = "游客";

    @PrimaryKey(autoincrement = true)
    public long userid;

    @Column
    public String username;

    @Column
    public String password;

    public static User getUser() {
        User user = SQLite.select()
                .from(User.class)
                .querySingle();
        if (user == null) {
            user = new User();
            user.username = DEFAULT_NAME;
        }

        return user;
    }

    public static void saveUser(String username, String password) {
        User user = getUser();
        user.password = password;
        user.username = username;
        user.save();
    }

    public boolean isAnonymous() {
        return username.equals(DEFAULT_NAME);
    }
}
