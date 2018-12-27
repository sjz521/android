package com.example.administrator.musicguessing.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/12/6.
 */

public class User implements Serializable {
    private String username;
    private int coins;
    private int index;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
