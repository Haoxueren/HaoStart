package com.haoxueren.start;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.haoxueren.start.bean.DaoMaster;
import com.haoxueren.start.bean.DaoSession;


/**
 * Created by Haoxueren on 2017/8/14.
 * 初始化GreenDao
 */
public class MyApplication extends Application {

    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        // do this once, for example in your Application class
        DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(getApplicationContext(), "HaoApp.db"); // 创建或打开数据库
        SQLiteDatabase database = openHelper.getWritableDatabase();// 获取数据库操作对象
        DaoMaster daoMaster = new DaoMaster(database);// 创建DaoMaster对象
        // 通过DaoMaster获取DaoSession对象
        daoSession = daoMaster.newSession();
    }

    /**
     * 在Activity/Fragment中获取DaoSession对象
     */
    public DaoSession getDaoSession() {
        return daoSession;
    }
}
