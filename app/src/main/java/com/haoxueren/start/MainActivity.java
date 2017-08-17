package com.haoxueren.start;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.haoxueren.start.base.BaseActivity;
import com.haoxueren.start.bean.HaoApp;
import com.haoxueren.start.bean.HaoAppDao;

import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public class MainActivity extends BaseActivity {

    private HaoAppDao dao;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.textInputLayout)
    TextInputLayout textInputLayout;
    @BindView(R.id.appEditText)
    EditText appEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        appEditText.setHintTextColor(Color.WHITE);
        dao = super.application.getDaoSession().getHaoAppDao();
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadAppFromSQLite();
            }
        });
        appEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 查询符合条件的APP
                    String appName = v.getText().toString().trim();
                    if (appName.isEmpty()) {
                        textInputLayout.setError("请输入应用名称");
                        return true;
                    }
                    List<HaoApp> list = dao.queryBuilder()
                            .where(HaoAppDao.Properties.AppName.like(String.format("%%%s%%", appName)))
                            .orderDesc(HaoAppDao.Properties.AppName).list();
                    if (list.isEmpty()) {
                        textInputLayout.setError("未查询到符合条件的应用");
                    } else {
                        v.setText(null);
                        launchApp(list.get(0).getPackageName());
                        // 隐藏软件键盘
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    return true;
                }
                return false;
            }
        });
        loadAppFromSQLite();
    }


    @Override
    public void onBackPressed() {
        this.moveTaskToBack(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        appEditText.clearFocus();
    }

    public void onAddClick(View view) {
        Intent intent = new Intent(this, InstalledAppActivity.class);
        this.startActivity(intent);
    }

    private void loadAppFromSQLite() {
        Observable.create(new ObservableOnSubscribe<List<HaoApp>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<HaoApp>> emitter) throws Exception {
                List<HaoApp> list = dao.queryBuilder().
                        orderAsc(HaoAppDao.Properties.AppName).list();
                emitter.onNext(list);
                emitter.onComplete();
            }
        }).subscribe(new Observer<List<HaoApp>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull final List<HaoApp> haoApps) {
                recyclerView.setAdapter(new HaoAppAdapter(haoApps, new HaoAppAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        // 启动APP
                        launchApp(haoApps.get(position).getPackageName());
                    }

                    @Override
                    public void onItemLongClick(int position) {
                        dao.delete(haoApps.get(position));
                        loadAppFromSQLite();
                        Toast.makeText(getApplicationContext(), "删除成功", Toast.LENGTH_SHORT).show();
                    }
                }));
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {
                refreshLayout.setRefreshing(false);
            }
        });
    }


    /**
     * 启动APP，返回是否打开成功；
     */
    public boolean launchApp(String packageName) {
        Context context = getApplicationContext();
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent != null) {
            context.startActivity(intent);
        }
        return intent != null;
    }
}
