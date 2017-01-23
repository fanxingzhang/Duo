package com.fxzhang.duo.utils;

import com.fxzhang.duo.service.response.ChampionDto;

import java.util.List;

/**
 * Created by fanxing on 1/23/2017.
 */

public class RGManager {
    private List<ChampionDto> championList;
    private static RGManager mRGManager;

    private RGManager() {

    }

    public static RGManager newInstance() {
        if (mRGManager == null) {
            mRGManager = new RGManager();
        }
        return mRGManager;
    }

    public List<ChampionDto> getChampionList() {
        return championList;
    }

    public void setChampionList(List<ChampionDto> list) {
        championList = list;
    }
}
