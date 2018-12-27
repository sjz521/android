package com.example.administrator.musicguessing.constdata;

import com.example.administrator.musicguessing.bean.Song;

import java.util.List;

/**
 * Created by Administrator on 2018/11/22.
 */

public class Const {
    public static List<Song> SONG_INFO ;
    public static final int STATE_ANSWER_LACK = 0x11;// 不存在
    public static final int STATE_ANSWER_RIGHT = 0x12;// 正确
    public static final int STATE_ANSWER_WRONG = 0x13;// 错误
}
