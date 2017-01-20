package com.fxzhang.duo.views.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fxzhang.duo.R;
import com.fxzhang.duo.utils.SharedPref;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by fanny on 24/12/16.
 */

public class SummonerListAdapter extends ArrayAdapter<SharedPref.SummonerLite> {
    public SummonerListAdapter(Context context, List<SharedPref.SummonerLite> list) {
        super(context, R.layout.summoner_list_row, list);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v = inflater.inflate(R.layout.summoner_list_row, null);
        }
        SharedPref.SummonerLite summonerLite = getItem(position);
        if (summonerLite != null) {
            TextView nameText = (TextView) v.findViewById(R.id.summoner_list_name);
            TextView rankText = (TextView) v.findViewById(R.id.summoner_list_rank);
            ImageView icon = (ImageView) v.findViewById(R.id.summoner_list_icon);
            nameText.setText(summonerLite.name);
            rankText.setText(summonerLite.rank);
            Picasso.with(getContext()).load("http://ddragon.leagueoflegends.com/cdn/6.24.1/img/profileicon/@@@.png".replace("@@@", "" + summonerLite.profileIconId)).into(icon);
        }
        return v;
    }
}
