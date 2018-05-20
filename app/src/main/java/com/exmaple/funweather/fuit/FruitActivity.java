package com.exmaple.funweather.fuit;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.exmaple.funweather.R;
import com.exmaple.funweather.db.User;

import org.litepal.crud.DataSupport;

public class FruitActivity extends AppCompatActivity {

    public static final String FRUIT_NAME = "fruit_name";
    public static final String FRUIT_IMAGE_ID = "fruit_image_id";

    private int currentImgId;
    private User loginedUser;

    FloatingActionButton changeImgButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruit);
        initUser();
        Intent intent = getIntent();
        String fruitName = intent.getStringExtra(FRUIT_NAME);
        int fruitImgId = intent.getIntExtra(FRUIT_IMAGE_ID, 0);
        currentImgId = fruitImgId;
        Toolbar toolbar = (Toolbar) findViewById(R.id.another_toolbar);
        ImageView fruitImg = (ImageView) findViewById(R.id.fruit_image_view);
        TextView fruitContent = (TextView) findViewById(R.id.fruit_content_text);
        changeImgButton = (FloatingActionButton)findViewById(R.id.sure_change_img);

        changeImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder= new AlertDialog.Builder(FruitActivity.this);
                builder.setTitle("提醒");
                builder.setMessage("确定设置为背景图片？");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        loginedUser.setBackImgUrl(String.valueOf(currentImgId));
                        loginedUser.save();
                        Toast.makeText(FruitActivity.this,"设置成功",Toast.LENGTH_SHORT).show();

                    }
                });
                builder.show();

            }
        });
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);   // 设置显示导航按钮,默认为返回箭头，没有默认事件
        }

        collapsingToolbarLayout.setTitle(fruitName);

        Glide.with(this).load(fruitImgId).into(fruitImg);

        String fruitContentText = generateFruitContent(fruitName);
        fruitContent.setText(fruitContentText);

      /*  FloatingActionButton comment = (FloatingActionButton)findViewById(R.id.float_comment);
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FruitActivity.this,"点击评论",Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    private void initUser() {
        SharedPreferences preferences = getSharedPreferences("logined_user",MODE_PRIVATE);
        String username = preferences.getString("username",null);
        if(username!=null){
            loginedUser = DataSupport.where("username=?",username).find(User.class).get(0);
        }
    }

    private String generateFruitContent(String fruitName) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 500; i++) {
            sb.append(fruitName);
        }
        return sb.toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
