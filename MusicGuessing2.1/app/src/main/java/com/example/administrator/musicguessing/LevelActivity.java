package com.example.administrator.musicguessing;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.musicguessing.bean.LevelItem;
import com.example.administrator.musicguessing.bean.User;
import com.example.administrator.musicguessing.constdata.Const;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



public class LevelActivity extends Activity {

    private static final String TAG = LevelActivity.class.getSimpleName();
    private List<List<String>> lists;
    private ListView lv_level_list;
    private MyAdapter myAdapter;
    private TextView textView;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        //MySQLiteHelper helper = new MySQLiteHelper(getApplicationContext(), "music.db", null, 1);
        textView = (TextView) findViewById(R.id.txt_bar_coins1);
        lv_level_list = (ListView) findViewById(R.id.lv_level_list);
        lists = new ArrayList<>();
        int count = 0;
        for(int i=0;i<20;i++){
            List<String> s = new ArrayList<>();
            for(int j=0;j<5;j++){
                count++;
                s.add(count+"");
            }
            lists.add(s);
        }

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        Log.i(TAG, "onCreate: "+user.getCoins()+"");
        textView.setText(String.valueOf(user.getCoins()));

        myAdapter = new MyAdapter();
        lv_level_list.setAdapter(myAdapter);
        Log.i(TAG, "onCreate: ");
    }

    public void returnMydate(View view) {
        Intent intent = new Intent(this, MyDate.class);
        intent.putExtra("user",user);
        startActivity(intent);
    }


    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return lists.size();
        }

        @Override
        public Object getItem(int position) {
            return lists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null){
                convertView = getLayoutInflater().inflate(R.layout.list_button,null);
                LevelItem item = new LevelItem();
                item.button01 = ((Button) convertView.findViewById(R.id.b_level_01));
                item.button02 = ((Button) convertView.findViewById(R.id.b_level_02));
                item.button03 = ((Button) convertView.findViewById(R.id.b_level_03));
                item.button04 = ((Button) convertView.findViewById(R.id.b_level_04));
                item.button05 = ((Button) convertView.findViewById(R.id.b_level_05));
                convertView.setTag(item);
            }
            final  LevelItem level = ((LevelItem) convertView.getTag());
            level.button01.setText(lists.get(position).get(0));
            level.button02.setText(lists.get(position).get(1));
            level.button03.setText(lists.get(position).get(2));
            level.button04.setText(lists.get(position).get(3));
            level.button05.setText(lists.get(position).get(4));
            level.button01.setOnClickListener(new ButtonListener());
            level.button02.setOnClickListener(new ButtonListener());
            level.button03.setOnClickListener(new ButtonListener());
            level.button04.setOnClickListener(new ButtonListener());
            level.button05.setOnClickListener(new ButtonListener());

            int level_index = Integer.parseInt(lists.get(position).get(0));
            if(level_index==1){
                level.button01.setEnabled(true);
            }else if(level_index>user.getIndex()+1 || level_index> Const.SONG_INFO.size()){
                level.button01.setEnabled(false);
            }
            level_index = Integer.parseInt(lists.get(position).get(1));
            if(level_index>user.getIndex()+1 || level_index> Const.SONG_INFO.size()){
                level.button02.setEnabled(false);
            }
            level_index = Integer.parseInt(lists.get(position).get(2));
            if(level_index>user.getIndex()+1 || level_index> Const.SONG_INFO.size()){
                level.button03.setEnabled(false);
            }
            level_index = Integer.parseInt(lists.get(position).get(3));
            if(level_index>user.getIndex()+1 || level_index> Const.SONG_INFO.size()){
                level.button04.setEnabled(false);
            }
            level_index = Integer.parseInt(lists.get(position).get(4));
            if(level_index>user.getIndex()+1 || level_index> Const.SONG_INFO.size()){
                level.button05.setEnabled(false);
            }


            return convertView;
        }
    }

    private class ButtonListener implements View.OnClickListener{

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onClick(View v) {

            Button btn = (Button) v;
            String id = btn.getText().toString();
            //int id = Integer.parseInt(btn.getText().toString());
            Intent intent = new Intent(LevelActivity.this,MainActivity.class);
            intent.putExtra("level_id",id);
            intent.putExtra("user", (Serializable) user);
            startActivity(intent);
            finish();
        }
    }


}
