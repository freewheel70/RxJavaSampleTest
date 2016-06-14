package com.hong.app.rxjavatest.database;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by Freewheel on 2016/4/18.
 */
@Database(name = FreeGankDatabase.NAME, version = FreeGankDatabase.VERSION)
public class FreeGankDatabase {

    public static final String NAME = "Freegank";

    public static final int VERSION = 1;
}
