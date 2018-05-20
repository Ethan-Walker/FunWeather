package com.exmaple.funweather;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.FileLoader;
import com.exmaple.funweather.db.User;
import com.exmaple.funweather.util.ActivityCollector;
import com.exmaple.funweather.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChangePersonActivity extends AppCompatActivity {

    private CircleImageView changeImg;
    private TextView editUsername;
    private EditText editPassword;
    private EditText editPersonalSignal;
    private EditText editPhone;
    private EditText editEmail;
    private final int TAKE_PHOTO = 0x111;
    private final int OPEN_ALBUM = 0x10;
    private Button saving;
    private Button changePersonalBack;
    private Uri uri;
    private String imgUrl;
    private User loginedUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_person);
        ActivityCollector.addActivity(this);

        changeImg = (CircleImageView) findViewById(R.id.edit_personal_img);
        editUsername = (TextView) findViewById(R.id.edit_username);
        editPassword = (EditText) findViewById(R.id.edit_password);
        editEmail = (EditText) findViewById(R.id.edit_email);
        editPhone = (EditText) findViewById(R.id.edit_phone);
        editPersonalSignal = (EditText) findViewById(R.id.edit_personal_sign);
        saving = (Button) findViewById(R.id.saving_personal);
        changePersonalBack = (Button)findViewById(R.id.change_personal_back);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String email = intent.getStringExtra("email");
        String phone = intent.getStringExtra("phone");
        String personalSign = intent.getStringExtra("personalSign");

        editUsername.setText(username);
        editEmail.setText(email);
        editPersonalSignal.setText(personalSign);
        editPhone.setText(phone);

        init();

        saving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("logined_user", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("username", editUsername.getText().toString());
                editor.apply();
                loginedUser.setUsername(editUsername.getText().toString());
                loginedUser.setHeadImgUrl(imgUrl);
                loginedUser.setPhone(editPhone.getText().toString());
                loginedUser.setEmail(editEmail.getText().toString());
                loginedUser.setPersonalSign(editPersonalSignal.getText().toString());
                loginedUser.setPassword(editPassword.getText().toString());
                loginedUser.save();
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        changeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });
        changePersonalBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePersonBack();
            }
        });
    }

    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(getApplicationContext(), v);
        popupMenu.getMenuInflater().inflate(R.menu.choose_photo, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.open_camera:
                        openCamera();
                        return true;
                    case R.id.open_album:
                        openAlbum();
                        return true;
                    default:
                        break;
                }
                return false;
            }

        });
        popupMenu.show();

    }

    public void openCamera() {
        File outputImg = new File(getExternalCacheDir(), "output.jpg");
        imgUrl = getExternalCacheDir() + "/output.jpg";

        if (outputImg.exists()) {
            outputImg.delete();
        }
        try {
            outputImg.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        uri = null;

        if (Build.VERSION.SDK_INT < 24) {
            uri = Uri.fromFile(outputImg);
        } else {
            uri = FileProvider.getUriForFile(getApplicationContext(), "com.example.funweather.provider", outputImg);
        }
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, TAKE_PHOTO);

    }

    public void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, OPEN_ALBUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {

/*                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                        changeImg.setImageBitmap(bitmap);*/
                    Glide.with(getApplicationContext()).load(imgUrl).into(changeImg);

                }
                break;
            case OPEN_ALBUM:
                if (resultCode == RESULT_OK) {
                    // 判断Android系统版本，在4.4(KitKat)之前/之后 有不同的处理方式
                    if (Build.VERSION.SDK_INT >= 19) {
                        handleImageOnKitKat(data);
                    } else {
                        handleImageBeforeKitKat(data);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "选择图片失败", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;

        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        Log.d("TAG", "handleImageOnKitKat: uri is " + uri);
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else {
                if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                    imagePath = getImagePath(contentUri, null);
                }
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath); // 根据图片路径显示图片
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    // 根据uri对象获取图片的真实路径
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    // 显示图片
    private void displayImage(String imagePath) {
        if (imagePath != null) {
/*
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            changeImg.setImageBitmap(bitmap);
*/
            imgUrl = imagePath;
            Glide.with(getApplicationContext()).load(imagePath).into(changeImg);

        } else {
            Toast.makeText(getApplicationContext(), "加载图片失败", Toast.LENGTH_SHORT).show();
        }
    }

    public void init() {
        SharedPreferences preferences = getSharedPreferences("logined_user", MODE_PRIVATE);
        String usernameData = preferences.getString("username", null);
        loginedUser = DataSupport.where("username=?",usernameData).find(User.class).get(0);

        String imgUrl = loginedUser.getHeadImgUrl();

        if (imgUrl != null) {
            if(Utility.isDigit(imgUrl)){
                Glide.with(getApplicationContext()).load(Integer.valueOf(imgUrl)).into(changeImg);
            }else{
                Glide.with(getApplicationContext()).load(imgUrl).into(changeImg);
            }
        }
    }


    public void changePersonBack() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("警告");
        dialog.setMessage("直接退出会导致填写的信息丢失,确定退出？");
        dialog.setCancelable(false);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent();
                setResult(RESULT_CANCELED,intent);
                finish();
            }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
