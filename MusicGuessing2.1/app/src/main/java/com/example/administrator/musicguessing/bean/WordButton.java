package com.example.administrator.musicguessing.bean;

import android.widget.Button;

/**
 * 文字按钮
 * Created by Administrator on 2018/11/22.
 */

public class WordButton {
    //索引
    private int index;
    //显示或隐藏
    private boolean isVisiable;
    //当前显示文字
    private String wordString;

    //布局
    public Button viewButton;

    public WordButton() {
        this.isVisiable = true;
        this.wordString = "";
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isVisiable() {
        return isVisiable;
    }

    public void setVisiable(boolean visiable) {
        isVisiable = visiable;
    }

    public String getWordString() {
        return wordString;
    }

    public void setWordString(String wordString) {
        this.wordString = wordString;
    }

    public Button getViewButton() {
        return viewButton;
    }

    public void setViewButton(Button viewButton) {
        this.viewButton = viewButton;
    }
}
