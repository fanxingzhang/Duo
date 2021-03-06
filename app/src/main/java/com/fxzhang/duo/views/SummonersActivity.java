package com.fxzhang.duo.views;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fxzhang.duo.R;
import com.fxzhang.duo.SubActivity;
import com.fxzhang.duo.service.response.ChampionDto;
import com.fxzhang.duo.service.response.ChampionListDto;
import com.fxzhang.duo.service.response.LeagueDto;
import com.fxzhang.duo.service.response.MatchList;
import com.fxzhang.duo.service.response.MatchReference;
import com.fxzhang.duo.service.response.Summoner;
import com.fxzhang.duo.utils.RGManager;
import com.fxzhang.duo.utils.SharedPref;
import com.fxzhang.duo.utils.Tags;
import com.fxzhang.duo.service.response.PlayerStatsSummaryDto;
import com.fxzhang.duo.service.response.SummonerStatsSummary;
import com.fxzhang.duo.views.fragments.SummonerSelectFragment;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by fanny on 20/12/16.
 */

public class SummonersActivity extends SubActivity {

    private PlayerStatsSummaryDto mPlayerStatsSummary;
    private LeagueDto mRankedDetails;
    private long mSummonerId;
    private String mSummonerName;
    private int mSummonerIconId;
    private long mSummonerLevel;
    private String mRegion = "NA";
    private String mSummonerRankString;

    private TextView mNameText;
    private TextView mWinsText;
    private TextView mLossesText;
    private TextView mWinRateText;
    private TextView mLevelText;
    private TextView mTotalKillsText;
    private TextView mTotalAssistsText;
    //private TextView mTurretKillsText;
    private TextView mRankText;
    private TextView mLeagueText;
    private TextView mLPText;
    private ImageView mIconImage;
    private ImageView mLeagueImage;

    private ImageView champion1;
    private ImageView champion2;
    private ImageView champion3;

    private SummonerSelectFragment summonerSelectFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentMainView(R.layout.summoners_activity);
        Bundle args = getIntent().getExtras();
        mSummonerId = args.getLong(Tags.SUMMONER_ID);
        mSummonerName = args.getString(Tags.SUMMONER_NAME);
        mSummonerIconId = args.getInt(Tags.SUMMONER_PROFILE_ID);
        mSummonerLevel = args.getLong(Tags.SUMMONER_LEVEL);

        mNameText= (TextView) findViewById(R.id.summoner_name);
        mWinsText = (TextView) findViewById(R.id.summoner_wins);
        mLossesText = (TextView) findViewById(R.id.summoner_losses);
        mIconImage = (ImageView) findViewById(R.id.summoner_icon);
        mWinRateText = (TextView) findViewById(R.id.summoner_win_rate);
        mTotalKillsText = (TextView) findViewById(R.id.summoner_champion_kills);
        mTotalAssistsText = (TextView) findViewById(R.id.summoner_assists);
        //mTurretKillsText = (TextView) findViewById(R.id.summoner_turrets);
        mLevelText = (TextView) findViewById(R.id.summoner_level);
        mRankText = (TextView) findViewById(R.id.summoner_rank);
        mLeagueText = (TextView) findViewById(R.id.summoner_league_name);
        mLPText = (TextView) findViewById(R.id.summoner_lp);
        mLeagueImage = (ImageView) findViewById(R.id.summoner_league_image);

        champion1 = (ImageView) findViewById(R.id.champion_icon_1);
        champion2 = (ImageView) findViewById(R.id.champion_icon_2);
        champion3 = (ImageView) findViewById(R.id.champion_icon_3);

        getSummonerDetails();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_summoners, menu);
        return true;
    }


    public void startCompare(MenuItem item) {
        summonerSelectFragment = new SummonerSelectFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_content_view, summonerSelectFragment)
                .addToBackStack(null)
                .commit();
    }

    public void compare(String name) {
        getSupportFragmentManager().popBackStack();
        searchSummonerByName(name.toLowerCase().replace(" ", ""));
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
        Intent intent = new Intent(this, SummonerCompareActivity.class);
        intent.putExtra(Tags.SUMMONER_ID, summoner.id);
        intent.putExtra(Tags.SUMMONER_NAME, summoner.name);
        intent.putExtra(Tags.SUMMONER_PROFILE_ID, summoner.profileIconId);
        intent.putExtra(Tags.SUMMONER_LEVEL, summoner.summonerLevel);
        intent.putExtra(Tags.SUMMONER_ID2, mSummonerId);
        intent.putExtra(Tags.SUMMONER_NAME2, mSummonerName);
        intent.putExtra(Tags.SUMMONER_PROFILE_ID2, mSummonerIconId);
        intent.putExtra(Tags.SUMMONER_LEVEL2, mSummonerLevel);
        intent.putExtra(Tags.PLAYER_STATS_SUMMARY, mPlayerStatsSummary);
        intent.putExtra(Tags.RANKED_DETAILS, mRankedDetails);

        startActivity(intent);
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
                    getMatchList();
                }
            }

            @Override
            public void onFailure(Call<Map<String, List<LeagueDto>>> call, Throwable t) {

            }
        });
    }

    private void getMatchList() {
        Call<MatchList> call = riotGamesServiceForMatches.getMatchList("na", mSummonerId);
        call.enqueue(new Callback<MatchList>() {
            @Override
            public void onResponse(Call<MatchList> call, Response<MatchList> response) {
                initUI();
                if (response.body() != null) {
                    setChampions(response.body().matches);
                }
            }

            @Override
            public void onFailure(Call<MatchList> call, Throwable t) {

            }
        });
    }

    private void setChampions(List<MatchReference> matches) {
        Map<Long, Integer> championsCount = new HashMap<>();
        for (MatchReference match : matches) {
            if (championsCount.containsKey(match.champion)) {
                championsCount.put(match.champion, championsCount.get(match.champion) + 1);
            }
            else {
                championsCount.put(match.champion, 1);
            }
        }
        final List<Long> maxChampions = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            int max = 0;
            long maxChampion = 0;
            for (Map.Entry<Long, Integer> entry : championsCount.entrySet()) {
                if (entry.getValue() > max) {
                    maxChampion = entry.getKey();
                    max = entry.getValue();
                }
            }
            Log.d("CHAMION", "" + maxChampion);
            maxChampions.add(maxChampion);
            championsCount.remove(maxChampion);
        }

        final RGManager manager = RGManager.newInstance();
        if (manager.getChampionList() != null) {
            setChampionsImg(maxChampions, manager.getChampionList());
        }
        else {
            Call<ChampionListDto> call = riotGamesService.getChampionList("na");
            call.enqueue(new Callback<ChampionListDto>() {
                @Override
                public void onResponse(Call<ChampionListDto> call, Response<ChampionListDto> response) {
                    if (response.body() != null) {
                        List<ChampionDto> championDtoList = new ArrayList<ChampionDto>();
                        championDtoList.addAll(response.body().data.values());
                        manager.setChampionList(championDtoList);
                        setChampionsImg(maxChampions, championDtoList);
                    }
                }

                @Override
                public void onFailure(Call<ChampionListDto> call, Throwable t) {

                }
            });
        }
    }

    private void setChampionsImg(List<Long> listIds, List<ChampionDto> listChampions) {
        List<String> championNames = new ArrayList<>();
        for (long id : listIds) {
            for (ChampionDto champion : listChampions) {
                if (id == champion.id) {
                    Log.d("CHAMPION", champion.name);
                    championNames.add(champion.name);
                }
            }
        }
        Picasso.with(this).load(CHAMPION_ICON_URL.replace("@@@", "" + championNames.get(0))).into(champion1);
        Picasso.with(this).load(CHAMPION_ICON_URL.replace("@@@", "" + championNames.get(1))).into(champion2);
        Picasso.with(this).load(CHAMPION_ICON_URL.replace("@@@", "" + championNames.get(2))).into(champion3);

    }

    private void initUI() {
        int wins = mPlayerStatsSummary.wins;
        int losses = mPlayerStatsSummary.losses;
        int winRate = (wins * 100 )/ (wins + losses);
        mNameText.setText(mSummonerName);
        mWinsText.setText("" + wins);
        mLossesText.setText("" + losses);
        mWinRateText.setText("" + winRate + "%");
        mLevelText.setText("@ | Level %%".replace("@", mRegion).replace("%%", "" + mSummonerLevel));

        Picasso.with(this).load(PROFILE_ICON_URL.replace("@@@", "" + mSummonerIconId)).into(mIconImage);

        mTotalKillsText.setText("" + mPlayerStatsSummary.aggregatedStats.totalChampionKills);
        mTotalAssistsText.setText("" + mPlayerStatsSummary.aggregatedStats.totalAssists);
        //mTurretKillsText.setText("" + mPlayerStatsSummary.aggregatedStats.totalTurretsKilled);

        String mRank = mRankedDetails.tier;
        String mDivision = mRankedDetails.entries.get(0).division;
        mSummonerRankString = mRank + " " + mDivision;
        mRankText.setText(mSummonerRankString);
        mLeagueText.setText(mRankedDetails.name);
        mLPText.setText("" + mRankedDetails.entries.get(0).leaguePoints + " LP");

        String divisionId;
        if (mRank.equals("CHALLENGER") || mRank.equals("MASTER")) {
            divisionId = mRank.toLowerCase();
        }
        else {
            divisionId = mRank.toLowerCase() + "_" + mDivision.toLowerCase();
        }
        int imageId = getResources().getIdentifier(divisionId, "drawable", getPackageName());
        mLeagueImage.setImageResource(imageId);

        saveSummoner();
    }

    private void saveSummoner() {
        SharedPref.SummonerLite summonerLite = new SharedPref.SummonerLite(mSummonerId, mSummonerName, mSummonerIconId, mSummonerRankString);
        SharedPref.saveSummoner(this, summonerLite);
    }
}
