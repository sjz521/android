package com.example.administrator.musicguessing;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.administrator.musicguessing.bean.Song;
import com.example.administrator.musicguessing.constdata.Const;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/11/29.
 */

public class MySongProvider extends ContentProvider {
    private static UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
    private MySQLiteHelper helper;
    private static final int INSERT = 1;
    private static final int UPDATE = 2;
    private static final int QUERY = 4;
    static {
        matcher.addURI("cn.itcast.db.myProvider","insert",INSERT);
        matcher.addURI("cn.itcast.db.myProvider","update",UPDATE);
        matcher.addURI("cn.itcast.db.myProvider","query",QUERY);
    }

    @Override
    public boolean onCreate() {
        helper = new MySQLiteHelper(this.getContext(), "music.db", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();
        //add(db);
        List<Song> lists = new ArrayList<>();
        Cursor cursor = db.query("song", new String[]{"id", "song_filename", "song_name"}, null, null, null, null, null);

        while (cursor.moveToNext()){

            int level_id = cursor.getInt(cursor.getColumnIndex("id"));
            String song_filename = cursor.getString(cursor.getColumnIndex("song_filename"));
            String song_name = cursor.getString(cursor.getColumnIndex("song_name"));

            Song song = new Song();
            song.setLevel(level_id);
            song.setSongFileName(song_filename);
            song.setSongName(song_name);
            lists.add(song);
        }

        db.close();
        Const.SONG_INFO = lists;
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        if(matcher.match(uri)==QUERY){
            SQLiteDatabase db = helper.getReadableDatabase();
            Cursor cursor = db.query("users",projection, selection, selectionArgs, null, null, sortOrder);
            return cursor;
        }
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        if(matcher.match(uri)==INSERT){
            SQLiteDatabase db = helper.getWritableDatabase();
            db.insert("users",null,values);
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values,
                      @Nullable String selection, @Nullable String[] selectionArgs) {
        if(matcher.match(uri)==UPDATE){
            SQLiteDatabase db = helper.getReadableDatabase();
            db.update("users",values,selection,selectionArgs);
        }
        return 0;
    }
}
