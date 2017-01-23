package com.fxzhang.duo.service;

import com.fxzhang.duo.service.response.ChampionDto;
import com.fxzhang.duo.service.response.ChampionListDto;
import com.fxzhang.duo.service.response.LeagueDto;
import com.fxzhang.duo.service.response.MatchList;
import com.fxzhang.duo.service.response.Summoner;
import com.fxzhang.duo.service.response.SummonerStatsSummary;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by fanny on 15/12/16.
 */

public interface RiotGamesService {
    @GET("/api/lol/{region}/v1.4/summoner/by-name/{summonerNames}")
    Call<Map<String, Summoner>> getSummonerByName(@Path("region") String region, @Path("summonerNames") String summonerName);

    @GET("/api/lol/{region}/v1.3/stats/by-summoner/{summonerId}/summary")
    Call<SummonerStatsSummary> getSummonerStatsById(@Path("region") String region, @Path("summonerId") long id);

    @GET("/api/lol/{region}/v2.5/league/by-summoner/{summonerIds}/entry")
    Call<Map<String, List<LeagueDto>>> getSummonerRankedStatsById(@Path("region") String region, @Path("summonerIds") long summonerId);

    @GET("/api/lol/{region}/v2.2/matchlist/by-summoner/{summonerId}")
    Call<MatchList> getMatchList(@Path("region") String region, @Path("summonerId") long summonderId);

    @GET("/api/lol/static-data/{region}/v1.2/champion")
    Call<ChampionListDto> getChampionList(@Path("region") String region);
}
