package com.example.administrator.musicguessing;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.musicguessing.bean.User;
import com.example.administrator.musicguessing.constdata.Const;

public class LoginActivity extends Activity implements View.OnClickListener {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private Button login;
    private Button register;
    private EditText et_Username;
    private EditText et_Password;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login= ((Button) findViewById(R.id.bt_login));
        register= ((Button) findViewById(R.id.bt_register));

        et_Username= ((EditText) findViewById(R.id.userName));
        et_Password= ((EditText) findViewById(R.id.passWord));

        login.setOnClickListener(this);
        register.setOnClickListener(this);

        Log.i(TAG, "Song: "+ Const.SONG_INFO.size());
    }

    public void Login(){
        String name=et_Username.getText().toString();
        String pass=et_Password.getText().toString();

        if("".equals(name)||"".equals(pass)){
            Toast.makeText(this, "请输入账号或密码", Toast.LENGTH_SHORT).show();
        }else {
            /*db=new MySQLiteHelper(this,"music.db",null,1).getReadableDatabase();
            Cursor cursor=db.query("users",new String[]{"username","password","money","checkpoint"},null,null,null,null,null);*/
            Uri uri = Uri.parse("content://cn.itcast.db.myProvider/query");
            ContentResolver resolver = getContentResolver();
            Cursor cursor = resolver.query(uri, null, null, null, null);
            //new String[]{"username", "password", "money", "checkpoint"}

            //Log.i(TAG, "Login: "+cursor.moveToFirst());
            if (cursor.getCount()==0)
            {
                Toast.makeText(this, "用户名不存在！", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Login: "+"试运行到这里");
                return;
            }
            boolean match=false;
            boolean isName=false;
            //Log.i(TAG, "Login: "+cursor.moveToNext());
            while (cursor.moveToNext()){

                String getName=cursor.getString(cursor.getColumnIndex("username"));
                String getPass=cursor.getString(cursor.getColumnIndex("password"));
                Log.i(TAG, "Login: "+getName+":"+getPass);
                if (getName.equals(name)&&getPass.equals(pass)){
                    match=true;
                    break;
                }else if (!name.equals(getName)){
                    match=false;
                    isName=true;

                }else {
                    match=false;
                    isName=false;

                }
            }

            if (match){
                Intent intent=new Intent(this, MyDate.class);
                user = new User();
                user.setUsername(name);
                user.setCoins(cursor.getInt(cursor.getColumnIndex("money")));
                user.setIndex(cursor.getInt(cursor.getColumnIndex("checkpoint")));
                intent.putExtra("user", user);
                startActivity(intent);
                Toast.makeText(this, "登录成功！", Toast.LENGTH_SHORT).show();
            }else {
                if(isName){
                    Toast.makeText(this, "用户名不存在！", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, "密码输入错误！", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_login:{
                Login();
            }
            break;
            case R.id.bt_register:{
                Intent intent=new Intent(this,RegisterActivity.class);
                startActivity(intent);
            }
        }
    }
}
