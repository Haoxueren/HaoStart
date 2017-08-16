package com.haoxueren.start.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.haoxueren.start.MyApplication;

import butterknife.ButterKnife;

public class BaseActivity extends Activity {

    protected MyApplication application;

    @Override
    public void setContentView(int layoutResID) {
        // 使用自定义的标题栏
        this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        // setContentView
        super.setContentView(layoutResID);
        // 在你类中初始化ButterKnife
        ButterKnife.bind(this);
        application = (MyApplication) getApplication();
    }
}
