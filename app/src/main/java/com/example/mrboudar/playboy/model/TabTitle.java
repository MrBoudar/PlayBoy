package com.example.mrboudar.playboy.model;

import java.io.Serializable;

/**
 * Created by MrBoudar on 16/8/7.
 */
public class TabTitle implements Serializable{
    private String name;
    private String key;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
