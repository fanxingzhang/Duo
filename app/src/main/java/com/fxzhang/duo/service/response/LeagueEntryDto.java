package com.fxzhang.duo.service.response;

import java.io.Serializable;

/**
 * Created by fanny on 24/12/16.
 */

public class LeagueEntryDto implements Serializable{
    public String division;
    public boolean isFreshBlood;
    public boolean isHotStreak;
    public boolean isInactive;
    public boolean isVeteran;
    public int leaguePoints;
    public int losses;
    public int wins;
    public String playerOrTeamId;
    public String playerOrTeamName;
    public String playstyle;
    //public MiniSeriesDto miniSeries;
}
