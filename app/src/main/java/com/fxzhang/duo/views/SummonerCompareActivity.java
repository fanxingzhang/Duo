package com.fxzhang.duo.views;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fxzhang.duo.R;
import com.fxzhang.duo.SubActivity;
import com.fxzhang.duo.service.response.LeagueDto;
import com.fxzhang.duo.service.response.PlayerStatsSummaryDto;
import com.fxzhang.duo.service.response.SummonerStatsSummary;
import com.fxzhang.duo.utils.Tags;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by fanxing on 1/22/2017.
 */

public class SummonerCompareActivity extends SubActivity {

    private ImageView iconView;
    private TextView nameText;
    private TextView levelText;
    private TextView winRateText;
    private TextView killText;
    private TextView assistText;
    private TextView turretsText;
    private View winRateBar;
    private View killBar;
    private View assistBar;
    private View turretsBar;
    private ImageView iconView2;
    private TextView nameText2;
    private TextView levelText2;
    private TextView winRateText2;
    private TextView killText2;
    private TextView assistText2;
    private TextView turretsText2;
    private View winRateBar2;
    private View killBar2;
    private View assistBar2;
    private View turretsBar2;

    private long mSummonerId;
    private String mSummonerName;
    private int mSummonerIconId;
    private long mSummonerLevel;
    private long mSummonerId2;
    private String mSummonerName2;
    private int mSummonerIconId2;
    private long mSummonerLevel2;

    private PlayerStatsSummaryDto mPlayerStatsSummary;
    private LeagueDto mRankedDetails;
    private PlayerStatsSummaryDto mPlayerStatsSummary2;
    private LeagueDto mRankedDetails2;

    private float screenWidth;
    private float screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentMainView(R.layout.activity_compare);
        Bundle args = getIntent().getExtras();
        mSummonerId = args.getLong(Tags.SUMMONER_ID);
        mSummonerName = args.getString(Tags.SUMMONER_NAME);
        Log.e("TEST", mSummonerName);
        mSummonerIconId = args.getInt(Tags.SUMMONER_PROFILE_ID);
        mSummonerLevel = args.getLong(Tags.SUMMONER_LEVEL);
        mSummonerId2 = args.getLong(Tags.SUMMONER_ID2);
        mSummonerName2 = args.getString(Tags.SUMMONER_NAME2);
        mSummonerIconId2 = args.getInt(Tags.SUMMONER_PROFILE_ID2);
        mSummonerLevel2 = args.getLong(Tags.SUMMONER_LEVEL2);
        mPlayerStatsSummary2 = (PlayerStatsSummaryDto) args.getSerializable(Tags.PLAYER_STATS_SUMMARY);
        mRankedDetails2 = (LeagueDto) args.getSerializable(Tags.RANKED_DETAILS);


        iconView = (ImageView) findViewById(R.id.summoner_icon);
        nameText = (TextView) findViewById(R.id.summoner_name);
        levelText = (TextView) findViewById(R.id.summoner_level);
        winRateText = (TextView) findViewById(R.id.summoner_win_rate);
        killText = (TextView) findViewById(R.id.summoner_champion_kills);
        assistText = (TextView) findViewById(R.id.summoner_assists);
        turretsText = (TextView) findViewById(R.id.summoner_turrets);
        winRateBar = findViewById(R.id.summoner_win_rate_bar);
        killBar = findViewById(R.id.summoner_champion_kills_bar);
        assistBar = findViewById(R.id.summoner_assists_bar);
        turretsBar = findViewById(R.id.summoner_turrets_bar);

        iconView2 = (ImageView) findViewById(R.id.summoner_icon2);
        nameText2 = (TextView) findViewById(R.id.summoner_name2);
        levelText2 = (TextView) findViewById(R.id.summoner_level2);
        winRateText2 = (TextView) findViewById(R.id.summoner_win_rate2);
        killText2 = (TextView) findViewById(R.id.summoner_champion_kills2);
        assistText2 = (TextView) findViewById(R.id.summoner_assists2);
        turretsText2 = (TextView) findViewById(R.id.summoner_turrets2);
        winRateBar2 = findViewById(R.id.summoner_win_rate_bar2);
        killBar2 = findViewById(R.id.summoner_champion_kills_bar2);
        assistBar2 = findViewById(R.id.summoner_assists_bar2);
        turretsBar2 = findViewById(R.id.summoner_turrets_bar2);

        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        screenHeight = displayMetrics.heightPixels / displayMetrics.density;
        screenWidth = displayMetrics.widthPixels;

        getSummonerDetails();
    }

    private void getSummonerDetails() {
        Call<SummonerStatsSummary> call = riotGamesService.getSummonerStatsById("na", mSummonerId);
        call.enqueue(new Callback<SummonerStatsSummary>() {
            @Override
            public void onResponse(Call<SummonerStatsSummary> call, Response<SummonerStatsSummary> response) {
                for (PlayerStatsSummaryDto p : response.body().playerStatSummaries) {
                    if (p.playerStatSummaryType.equals(Tags.RANKED_SOLO)) {
                        mPlayerStatsSummary = p;
                        break;
                    }
                }
                if (mPlayerStatsSummary != null) {
                    getSummonerRankedDetails();
                }
            }

            @Override
            public void onFailure(Call<SummonerStatsSummary> call, Throwable t) {

            }
        });
    }

    private void getSummonerRankedDetails() {
        Call<Map<String, List<LeagueDto>>> call = riotGamesService.getSummonerRankedStatsById("na", mSummonerId);
        call.enqueue(new Callback<Map<String, List<LeagueDto>>>() {
            @Override
            public void onResponse(Call<Map<String, List<LeagueDto>>> call, Response<Map<String, List<LeagueDto>>> response) {
                List<LeagueDto> listLeagues= response.body().get("" + mSummonerId);
                for (LeagueDto league : listLeagues) {
                    if (league.queue.equals(Tags.RANKED_SOLO_2)) {
                        mRankedDetails = league;
                        break;
                    }
                }
                if (mRankedDetails != null) {
                    initUI();
                }
            }

            @Override
            public void onFailure(Call<Map<String, List<LeagueDto>>> call, Throwable t) {

            }
        });
    }

    private void initUI() {
        Picasso.with(this).load(PROFILE_ICON_URL.replace("@@@", "" + mSummonerIconId)).into(iconView);
        nameText.setText(mSummonerName);
        levelText.setText("@ | Level %%".replace("@", "NA").replace("%%", "" + mSummonerLevel));

        Picasso.with(this).load(PROFILE_ICON_URL.replace("@@@", "" + mSummonerIconId2)).into(iconView2);
        nameText2.setText(mSummonerName2);
        levelText2.setText("@ | Level %%".replace("@", "NA").replace("%%", "" + mSummonerLevel2));

        int wins = mPlayerStatsSummary.wins;
        int wins2 = mPlayerStatsSummary2.wins;
        int losses = mPlayerStatsSummary.losses;
        int losses2 = mPlayerStatsSummary2.losses;
        int winRate = (wins * 100 )/ (wins + losses);
        int winRate2 = (wins2 * 100 )/ (wins2 + losses2);
        int total = wins + losses;
        int total2 = wins2 + losses2;

        winRateBar.getLayoutParams().width = (int) (winRate * screenWidth / 2) / (winRate + winRate2);
        winRateBar2.getLayoutParams().width = (int) (winRate2 * screenWidth / 2) / (winRate + winRate2);
        winRateText.setText("" + winRate + "% " + getString(R.string.win_rate));
        winRateText2.setText("" + winRate2 + "% " + getString(R.string.win_rate));

        int kills = mPlayerStatsSummary.aggregatedStats.totalChampionKills;
        int kills2 = mPlayerStatsSummary2.aggregatedStats.totalChampionKills;
        killText.setText("" + kills + " " + getString(R.string.champion_kills2));
        killText2.setText("" + kills2 + " " + getString(R.string.champion_kills2));
        killBar.getLayoutParams().width = (int) (kills * screenWidth / 2) / (kills + kills2);
        killBar2.getLayoutParams().width = (int) (kills2 * screenWidth / 2) / (kills + kills2);

        int assits = mPlayerStatsSummary.aggregatedStats.totalAssists;
        int assits2 = mPlayerStatsSummary2.aggregatedStats.totalAssists;
        assistText.setText("" + assits + " " + getString(R.string.assists2));
        assistText2.setText("" + assits2 + " " + getString(R.string.assists2));
        assistBar.getLayoutParams().width = (int) (assits * screenWidth / 2) / (assits + assits2);
        assistBar2.getLayoutParams().width = (int) (assits2 * screenWidth / 2) / (assits + assits2);

        int turrets = mPlayerStatsSummary.aggregatedStats.totalTurretsKilled;
        int turrets2 = mPlayerStatsSummary2.aggregatedStats.totalTurretsKilled;
        turretsText.setText("" + turrets + " " + getString(R.string.turret_kills2));
        turretsText2.setText("" + turrets2 + " " + getString(R.string.turret_kills2));
        turretsBar.getLayoutParams().width = (int) (turrets * screenWidth / 2) / (turrets + turrets2);
        turretsBar2.getLayoutParams().width = (int) (turrets2 * screenWidth / 2) / (turrets + turrets2);

    }
}
