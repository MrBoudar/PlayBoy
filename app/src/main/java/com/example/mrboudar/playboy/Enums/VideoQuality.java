package com.example.mrboudar.playboy.Enums;

/**
 * Created by MrBoudar on 16/8/25.
 */
public enum  VideoQuality {
    HIGH("high"),
    LOW("low"),
    MIDEIUM("medium");
    final String value;
    VideoQuality(String medium) {
        this.value = medium;
    }
    public String toString(){
        return value;
    }
}
