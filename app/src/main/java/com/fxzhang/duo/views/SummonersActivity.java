package com.fxzhang.duo.views;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.fxzhang.duo.R;
import com.fxzhang.duo.SubActivity;
import com.fxzhang.duo.service.response.LeagueDto;
import com.fxzhang.duo.utils.SharedPref;
import com.fxzhang.duo.utils.Tags;
import com.fxzhang.duo.service.response.PlayerStatsSummaryDto;
import com.fxzhang.duo.service.response.SummonerStatsSummary;
import com.squareup.picasso.Picasso;

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
    private TextView mTurretKillsText;
    private TextView mRankText;
    private TextView mLeagueText;
    private TextView mLPText;
    private ImageView mIconImage;
    private ImageView mLeagueImage;

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
        mTurretKillsText = (TextView) findViewById(R.id.summoner_turrets);
        mLevelText = (TextView) findViewById(R.id.summoner_level);
        mRankText = (TextView) findViewById(R.id.summoner_rank);
        mLeagueText = (TextView) findViewById(R.id.summoner_league_name);
        mLPText = (TextView) findViewById(R.id.summoner_lp);
        mLeagueImage = (ImageView) findViewById(R.id.summoner_league_image);

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
        mTurretKillsText.setText("" + mPlayerStatsSummary.aggregatedStats.totalTurretsKilled);

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
