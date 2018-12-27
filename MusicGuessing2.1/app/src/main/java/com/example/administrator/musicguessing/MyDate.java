package com.example.administrator.musicguessing;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.musicguessing.bean.User;

import org.w3c.dom.Text;

import java.io.Serializable;

public class MyDate extends AppCompatActivity {

    private static final String TAG = MyDate.class.getSimpleName();
    private static final int IMAGE = 1;
    private SQLiteDatabase db;
    private  int money;
    private  int checkpoint;
    private  String username;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_date);

//        //获取权限
//        ActivityCompat.requestPermissions(this,new String[]{"android.permission.READ_EXTERNAL_STORAGE"},1);

        Intent intent=getIntent();
        user = (User) intent.getSerializableExtra("user");
        if(user != null){
            ((TextView) findViewById(R.id.userMoney)).setText(""+user.getCoins());
            ((TextView) findViewById(R.id.userIndex)).setText(""+user.getIndex());
            ((TextView) findViewById(R.id.userName)).setText(user.getUsername());
        }
    }

    public void playGame(View view) {
        Intent intent=new Intent(this, LevelActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
        finish();
    }

    public void returnLogin(View view) {
        Intent intent=new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();

    }

//    public void addImage(View view) {
//
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(intent,IMAGE);
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        //获取图片路径
//        Log.i(TAG, "onActivityResult: "+requestCode);
//        Log.i(TAG, "onActivityResult: "+data);
//        Log.i(TAG, "onActivityResult: "+ Activity.RESULT_OK);
//        if(requestCode == IMAGE && data != null){
//            Uri selectedImage = data.getData();
//            Log.i(TAG, "onActivityResult: "+selectedImage);
//            String[] filePathColumns = {MediaStore.Images.Media.DATA};
//            Log.i(TAG, "onActivityResult: "+filePathColumns);
//            Cursor cursor = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
//            Log.i(TAG, "onActivityResult: "+cursor.toString());
//            cursor.moveToFirst();
//            String imagePath = cursor.getString(cursor.getColumnIndex(filePathColumns[0]));
//            showImage(imagePath);
//            cursor.close();
//        }
//    }
//
//    private void showImage(String imagePath){
//        Log.i(TAG, "showImage: ");
//        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
//        Bitmap newBmp = Bitmap.createBitmap(bitmap);
//        ImageView imageView = ((ImageView) findViewById(R.id.imageShow));
//        imageView.setImageBitmap(bitmap);
//    }
}
