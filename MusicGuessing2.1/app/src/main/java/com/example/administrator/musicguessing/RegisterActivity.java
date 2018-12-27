package com.example.administrator.musicguessing;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity implements View.OnClickListener {
    private Button registerGo;
    private Button registerBack;
    private EditText userName;
    private EditText passWord;
    private EditText repeatPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userName = (EditText) findViewById(R.id.userName);
        passWord = (EditText) findViewById(R.id.passWord);
        repeatPassword = (EditText) findViewById(R.id.repeatPassWord);

        registerGo= ((Button) findViewById(R.id.bt_go));
        registerBack= (Button) findViewById(R.id.bt_back);
        registerGo.setOnClickListener(this);
        registerBack.setOnClickListener(this);
    }

    private void RegisterSave() {

        String username=userName.getText().toString();
        String password=passWord.getText().toString();
        String rpassword=repeatPassword.getText().toString();
        boolean creatUser=true;
        if ("".equals(username)||"".equals(password)||"".equals(rpassword)){

            Toast.makeText(this,"输入不能为空",Toast.LENGTH_SHORT).show();

        }else if (!password.equals(rpassword)){

            Toast.makeText(this,"两次输入密码不一致，请重新输入",Toast.LENGTH_SHORT).show();

        }else if (password.length()<6){

            Toast.makeText(this,"密码小于六位数，请重新输入",Toast.LENGTH_SHORT).show();

        }else{
            Uri uri = Uri.parse("content://cn.itcast.db.myProvider/query");
            ContentResolver resolver = getContentResolver();
            Cursor cursor = resolver.query(uri, null, null, null, null);

            while (cursor.moveToNext()){
                if (username.equals(cursor.getString(cursor.getColumnIndex("username")))){
                    Toast.makeText(RegisterActivity.this,"用户名已存在！",Toast.LENGTH_SHORT).show();
                    creatUser=false;
                }
            }
            if (creatUser){
                ContentValues values=new ContentValues();
                values.put("username",username);
                values.put("password",password);
                values.put("money",100);
                values.put("checkpoint",0);
                uri = Uri.parse("content://cn.itcast.db.myProvider/insert");
                resolver.insert(uri,values);

                AlertDialog.Builder dialog=new AlertDialog.Builder(RegisterActivity.this);
                dialog.setTitle("注册成功");
                dialog.setMessage("您已成功注册账户，请返回登录界面！");
                dialog.show();
            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_go:{
                RegisterSave();
            }
            break;
            case R.id.bt_back:{

                finish();
                Intent intent = new Intent(this,LoginActivity.class);
                startActivity(intent);

            }
        }
    }
}
