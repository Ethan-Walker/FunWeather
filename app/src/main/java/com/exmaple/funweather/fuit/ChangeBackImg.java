package com.exmaple.funweather.fuit;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.exmaple.funweather.R;
import com.exmaple.funweather.fuit.Fruit;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChangeBackImg extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private SwipeRefreshLayout refreshLayout;
    List<Fruit> fruits = new ArrayList<>();
    FruitAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_img);
        Log.e(TAG, "onCreate: ");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);   // 设置显示导航按钮，默认是返回键，没有响应事件
            actionBar.setTitle("更换背景");
        }


   /*     FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Data deleted", Snackbar.LENGTH_LONG)
                        .setAction("撤销", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(ChangeBackImg.this, "Data Restored", Toast.LENGTH_SHORT).show();

                            }
                        }).show();
            }
        });*/

        init();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(manager);
        adapter = new FruitAdapter(fruits);
        recyclerView.setAdapter(adapter);

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);    //  设置刷新时进度条的颜色
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshImg();
            }
        });
    }

    void refreshImg() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);      // 表示刷新事件需要3秒
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        init();                           // 重新随机显示图片
                        adapter.notifyDataSetChanged();   // 通知适配器数据发生改变，重新显示
                        refreshLayout.setRefreshing(false); // 刷新事件结束，并隐藏进度条
                    }
                });
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();

            default:
                break;
        }
        return true;
    }

    void init() {
        fruits.clear();
        for (int i = 0; i < 50; i++) {
            Random random = new Random();
            int index = random.nextInt(fs.length);
            fruits.add(fs[index]);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    Fruit fruit1 = new Fruit("Apple", R.drawable.apple);
    Fruit fruit2 = new Fruit("Banana", R.drawable.banana);
    Fruit fruit3 = new Fruit("Orange", R.drawable.orange);
    Fruit fruit4 = new Fruit("Watermelon", R.drawable.watermelon);
    Fruit fruit5 = new Fruit("Pear", R.drawable.pear);
    Fruit fruit6 = new Fruit("Cheery", R.drawable.cherry);
    Fruit fruit7 = new Fruit("Grape", R.drawable.grape);
    Fruit fruit8 = new Fruit("Mango", R.drawable.mango);
    Fruit[] fs = new Fruit[]{
            fruit1, fruit2, fruit3, fruit4, fruit5, fruit6, fruit7, fruit8
    };


    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "onStart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: ");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.e(TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "onRestart: ");
    }
}


