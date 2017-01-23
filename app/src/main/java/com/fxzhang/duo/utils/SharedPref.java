package com.fxzhang.duo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fanny on 24/12/16.
 */

public class SharedPref {

    private static final String SUMMONER_LIST = "summoner_list";

    public static class SummonerLite {
        public String name;
        public String rank;
        public int profileIconId;
        public long id;
        public SummonerLite(long id, String name, int profileId, String rank) {
            this.id = id;
            this.name = name;
            this.profileIconId = profileId;
            this.rank = rank;
        }
    }
    public static void saveSummoner(Context context, SummonerLite summoner) {
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        List<SummonerLite> listSummoners = getSavedSummonerList(context);
        boolean alreadySaved = false;
        for (SummonerLite s : listSummoners) {
            if (s.id == summoner.id) {
                alreadySaved = true;
                break;
            }
        }
        if (!alreadySaved) {
            listSummoners.add(summoner);
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SUMMONER_LIST, gson.toJson(listSummoners));
        editor.commit();
    }

    public static List<SummonerLite> getSavedSummonerList(Context context) {
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String listSummonerString = sharedPreferences.getString(SUMMONER_LIST, null);
        List<SummonerLite> listSummoners;
        if (listSummonerString == null) {
            listSummoners = new ArrayList<>();
        }
        else {
            Type type = new TypeToken<ArrayList<SummonerLite>>() {}.getType();
            listSummoners = gson.fromJson(listSummonerString, type);
        }
        return listSummoners;
    }
}
