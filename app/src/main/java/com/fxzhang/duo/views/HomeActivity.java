package com.fxzhang.duo.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fxzhang.duo.MainActivity;
import com.fxzhang.duo.R;
import com.fxzhang.duo.utils.SharedPref;
import com.fxzhang.duo.utils.Tags;
import com.fxzhang.duo.service.response.Summoner;
import com.fxzhang.duo.views.adapters.SummonerListAdapter;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by fanny on 20/12/16.
 */

public class HomeActivity extends MainActivity implements SearchView.OnQueryTextListener, ListView.OnItemClickListener{

    ListView mSummonerListView;
    List<SharedPref.SummonerLite> summonerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentMainView(R.layout.home_activity);

        mSummonerListView = (ListView) findViewById(R.id.home_list_view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        initList();
    }

    public boolean onQueryTextChange(String text) {
        return true;
    }

    public boolean onQueryTextSubmit(String text) {
        searchSummonerByName(text.toLowerCase().replace(" ", ""));
        return true;
    }

    private void searchSummonerByName(final String name) {
        Call<Map<String, Summoner>> call = riotGamesService.getSummonerByName("na", name);
        call.enqueue(new Callback<Map<String, Summoner>>() {
            @Override
            public void onResponse(Call<Map<String, Summoner>> call, Response<Map<String, Summoner>> response) {
                Summoner mSummoner = response.body().get(name);
                searchSummonerById(mSummoner);
            }

            @Override
            public void onFailure(Call<Map<String, Summoner>> call, Throwable t) {

            }
        });
    }

    private void searchSummonerById(Summoner summoner) {
        Intent intent = new Intent(this, SummonersActivity.class);
        intent.putExtra(Tags.SUMMONER_ID, summoner.id);
        intent.putExtra(Tags.SUMMONER_NAME, summoner.name);
        intent.putExtra(Tags.SUMMONER_PROFILE_ID, summoner.profileIconId);
        intent.putExtra(Tags.SUMMONER_LEVEL, summoner.summonerLevel);
        startActivity(intent);
    }

    private void initList() {
        summonerList = SharedPref.getSavedSummonerList(this);
        Log.d("TEST", "" + summonerList.size());
        SummonerListAdapter summonerListAdapter = new SummonerListAdapter(this, summonerList);
        mSummonerListView.setAdapter(summonerListAdapter);
        mSummonerListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        searchSummonerByName(summonerList.get(position).name.toLowerCase().replace(" ", ""));
    }
}
