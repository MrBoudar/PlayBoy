package com.example.mrboudar.playboy.api;

/**
 * Created by MrBoudar on 16/8/17.
 */
public enum APISTATUS {

    SUCESS("0000"),
    ERROR("9999"),
    ERROR_REQUEST("9998");

    private final String code;

    APISTATUS(String s) {
        this.code = s;
    }

    @Override
    public String toString() {
        return code;
    }
}
