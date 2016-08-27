package com.example.mrboudar.playboy.DataResponse;

import com.example.mrboudar.playboy.model.VideoCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrBoudar on 16/8/17.
 */
public class VideoCategoryResponse {
    private List<VideoCategory> category;

    public List<VideoCategory> getCategory() {
        if(null == category){
            category = new ArrayList<>();
        }
        return category;
    }

    public void setCategory(List<VideoCategory> category) {
        this.category = category;
    }
}
