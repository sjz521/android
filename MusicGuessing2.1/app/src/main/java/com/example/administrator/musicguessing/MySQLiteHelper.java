package com.example.administrator.musicguessing;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Administrator on 2018/11/29.
 */

public  class MySQLiteHelper extends SQLiteOpenHelper {


    private static final String TAG = MySQLiteHelper.class.getSimpleName();

    public MySQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "onCreate: ");
        db.execSQL("create table song(id integer primary key autoincrement,song_filename text,song_name text)");
        Log.i(TAG, "onCreate: "+"song");
        add(db);
        //read(db);

        db.execSQL("create table users(id integer primary key autoincrement," +
                "username text,password text,money integer,checkpoint integer)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    private void add(SQLiteDatabase db){
        String str = "insert into song(song_filename,song_name) values('__00000.m4a','征服')";
        db.execSQL(str);
        str = "insert into song(song_filename,song_name) values('__00001.m4a','童话')";
        db.execSQL(str);
        str = "insert into song(song_filename,song_name) values('__00002.m4a','同桌的你')";
        db.execSQL(str);
        str = "insert into song(song_filename,song_name) values('__00003.m4a','七里香')";
        db.execSQL(str);
        str = "insert into song(song_filename,song_name) values('__00004.m4a','传奇')";
        db.execSQL(str);
        str = "insert into song(song_filename,song_name) values('__00005.m4a','大海')";
        db.execSQL(str);
        str = "insert into song(song_filename,song_name) values('__00006.m4a','后来')";
        db.execSQL(str);
        str = "insert into song(song_filename,song_name) values('__00007.m4a','你的背包')";
        db.execSQL(str);
        str = "insert into song(song_filename,song_name) values('__00008.m4a','再见')";
        db.execSQL(str);
        str = "insert into song(song_filename,song_name) values('__00009.m4a','老男孩')";
        db.execSQL(str);
        str = "insert into song(song_filename,song_name) values('__00010.m4a','龙的传人')";
        db.execSQL(str);
        str = "insert into song(song_filename,song_name) values('__00011.mp3','凉凉')";
        db.execSQL(str);
        str = "insert into song(song_filename,song_name) values('__00012.mp3','隐形的翅膀')";
        db.execSQL(str);
        str = "insert into song(song_filename,song_name) values('__00013.mp3','逆战')";
        db.execSQL(str);
        str = "insert into song(song_filename,song_name) values('__00014.mp3','山水之间')";
        db.execSQL(str);
        str = "insert into song(song_filename,song_name) values('__00015.mp3','燕归巢')";
        db.execSQL(str);
        str = "insert into song(song_filename,song_name) values('__00016.mp3','淋雨一直走')";
        db.execSQL(str);
        str = "insert into song(song_filename,song_name) values('__00017.mp3','十年')";
        db.execSQL(str);
        str = "insert into song(song_filename,song_name) values('__00018.mp3','月光')";
        db.execSQL(str);
    }

    /*public static List<Song> read(SQLiteDatabase db){
        List<Song> lists = new ArrayList<>();
        Cursor cursor = db.query("song", new String[]{"id", "song_filename", "song_name"}, null, null, null, null, null);

        while (cursor.moveToNext()){

            int level_id = cursor.getInt(cursor.getColumnIndex("level_id"));
            String song_filename = cursor.getString(cursor.getColumnIndex("song_filename"));
            String song_name = cursor.getString(cursor.getColumnIndex("song_name"));

            Song song = new Song();
            song.setLevel(level_id);
            song.setSongFileName(song_filename);
            song.setSongName(song_name);
            lists.add(song);
        }

        db.close();
        return lists;
    }*/

}
