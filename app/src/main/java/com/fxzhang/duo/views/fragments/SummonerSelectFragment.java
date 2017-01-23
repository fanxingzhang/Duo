package com.fxzhang.duo.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fxzhang.duo.R;
import com.fxzhang.duo.SubActivity;
import com.fxzhang.duo.utils.SharedPref;
import com.fxzhang.duo.views.SummonersActivity;
import com.fxzhang.duo.views.adapters.SummonerListAdapter;

import java.util.List;

/**
 * Created by fanxing on 1/22/2017.
 */

public class SummonerSelectFragment extends ListFragment implements ListView.OnItemClickListener{

    List<SharedPref.SummonerLite> summonerList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.summoner_list_view, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("TEST", "LIST FRAGMENT!!!");
        summonerList = SharedPref.getSavedSummonerList(getActivity());
        SummonerListAdapter summonerListAdapter = new SummonerListAdapter(getActivity(), summonerList);
        setListAdapter(summonerListAdapter);
        getListView().setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.e("TEST", summonerList.get(position).name);
        ((SummonersActivity)getActivity()).compare(summonerList.get(position).name);
    }
}
