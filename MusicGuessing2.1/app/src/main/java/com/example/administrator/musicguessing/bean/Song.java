package com.example.administrator.musicguessing.bean;

/**
 * Created by Administrator on 2018/11/22.
 */

public class Song {
    //对应关卡
    private int level;
    //歌曲名称
    private String songName;
    //歌曲文件名
    private String songFileName;
    //歌曲名字长度
    private int nameLength;

    public Song() {
    }

    //把String转换为char[]
    public char[] getNameChar(){
        return songName.toCharArray();
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
        this.nameLength = songName.length();
    }

    public String getSongFileName() {
        return songFileName;
    }

    public void setSongFileName(String songFileName) {
        this.songFileName = songFileName;
    }

    public int getNameLength() {
        return nameLength;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
