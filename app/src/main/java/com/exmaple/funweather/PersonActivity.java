package com.exmaple.funweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.exmaple.funweather.db.User;
import com.exmaple.funweather.util.ActivityCollector;
import com.exmaple.funweather.util.Utility;

import org.litepal.crud.DataSupport;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by EthanWalker on 2017/6/15.
 */

public class PersonActivity extends AppCompatActivity {

    private static int count = 0;
    private CircleImageView personalImg;
    private TextView username;
    private TextView email;
    private TextView phone;
    private TextView personalSign;
    private Button changePersonal;
    private Button personalBack;
    private User loginedUser;

    public static final int CHANGE_DATA = 1;

    private static final String TAG = "PersonActivity";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_activity);
        ActivityCollector.addActivity(this);
        Log.e(TAG, "onCreate: " + ++count);
        personalImg = (CircleImageView) findViewById(R.id.person_img);
        username = (TextView) findViewById(R.id.username);
        email = (TextView) findViewById(R.id.email);
        personalSign = (TextView) findViewById(R.id.personal_sign);
        changePersonal = (Button) findViewById(R.id.change_personal);
        personalBack = (Button) findViewById(R.id.personal_back);

        phone = (TextView) findViewById(R.id.phone);
        init();
        changePersonal.setOnClickListener(v -> {
            Intent intent = new Intent(PersonActivity.this, ChangePersonActivity.class);
            intent.putExtra("username",username.getText());
            intent.putExtra("email",email.getText());
            intent.putExtra("phone",phone.getText());
            intent.putExtra("personalSign",personalSign.getText());

            startActivityForResult(intent, CHANGE_DATA);
        });
        personalBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void init() {

        SharedPreferences preferences = getSharedPreferences("logined_user", MODE_PRIVATE);
        String usernameData = preferences.getString("username", null);
        List<User> users = DataSupport.where("username=?", usernameData).find(User.class);
        loginedUser = users.get(0);
        String imgUrlData = loginedUser.getHeadImgUrl();
        String emailData = loginedUser.getEmail();
        String personalSignData = loginedUser.getPersonalSign();
        String phoneData = loginedUser.getPhone();

        if (imgUrlData != null) {
            if (Utility.isDigit(imgUrlData)) {
                Glide.with(getApplicationContext()).load(Integer.valueOf(imgUrlData)).into(personalImg);
            } else {
                Glide.with(getApplicationContext()).load(imgUrlData).into(personalImg);
            }
        }
        if (username != null) {
            username.setText(usernameData);
        }
        if (emailData != null) {
            email.setText(emailData);
        }
        if (personalSignData != null) {
            personalSign.setText(personalSignData);
        }
        if (phoneData != null) {
            phone.setText(phoneData);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CHANGE_DATA:
                if (resultCode == RESULT_OK) {
                    init();
                } else if (requestCode == RESULT_CANCELED) {
                    Toast.makeText(getApplicationContext(), "取消更新", Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(getApplicationContext(), "未知错误!!!!!", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "onStart: " + count);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Log.e(TAG, "onResume: " + count);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        Log.e(TAG, "onPause: " + count);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop: " + count);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
