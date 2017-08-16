package com.haoxueren.start;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.haoxueren.start.base.BaseActivity;
import com.haoxueren.start.bean.HaoApp;
import com.haoxueren.start.bean.HaoAppDao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class InstalledAppActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_installed_app);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        useRxAndroid();
    }

    /**
     * 在子线程中运行
     */
    private void useRxAndroid() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("正在加载已安装应用...");
        dialog.setCancelable(false);
        dialog.show();
        Observable.create(new ObservableOnSubscribe<List<HaoApp>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<HaoApp>> emitter) throws Exception {
                List<HaoApp> list = getInstalledAppList();
                emitter.onNext(list);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<HaoApp>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull final List<HaoApp> list) {
                        recyclerView.setAdapter(new HaoAppAdapter(list, new HaoAppAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                // 保存APP数据到SQLite
                                HaoAppDao haoAppDao = application.getDaoSession().getHaoAppDao();
                                HaoApp haoApp = list.get(position);
                                long insert = haoAppDao.insert(haoApp);
                                InstalledAppActivity.this.finish();
                            }

                            @Override
                            public void onItemLongClick(int position) {

                            }
                        }));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        dialog.dismiss();
                    }
                });
    }

    // 获取已安装应用的信息；
    public List<HaoApp> getInstalledAppList() {
        PackageManager packageManager = this.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent, 0);
        // 按应用名称排序；
        Collections.sort(resolveInfoList, new ResolveInfo.DisplayNameComparator(packageManager));
        List<HaoApp> list = new ArrayList<>();
        for (ResolveInfo resolveInfo : resolveInfoList) {
            // 应用名称；
            String appName = resolveInfo.loadLabel(packageManager).toString();
            // 应用图标；
            Drawable appIcon = resolveInfo.loadIcon(packageManager);
            // 应用包名；
            String packageName = resolveInfo.activityInfo.packageName;
            // 封装对象；
            HaoApp haoApp = new HaoApp();
            haoApp.setAppName(appName);
            haoApp.setAppIcon(appIcon);
            haoApp.setPackageName(packageName);
            list.add(haoApp);
        }
        return list;
    }

}
