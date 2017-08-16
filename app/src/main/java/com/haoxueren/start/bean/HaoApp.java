package com.haoxueren.start.bean;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 * APP信息：名称、包名、图标；
 */
@Entity
public class HaoApp {
    @Id
    private Long id;

    private String appName;

    @Transient
    private Drawable appIcon;

    private String packageName;

    @Generated(hash = 315055191)
    public HaoApp(Long id, String appName, String packageName) {
        this.id = id;
        this.appName = appName;
        this.packageName = packageName;
    }

    @Generated(hash = 1372598548)
    public HaoApp() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppName() {
        return this.appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }
}
