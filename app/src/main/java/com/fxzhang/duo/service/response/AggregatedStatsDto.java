package com.fxzhang.duo.service.response;

import java.io.Serializable;

/**
 * Created by fanny on 20/12/16.
 */

public class AggregatedStatsDto implements Serializable {

    public int totalChampionKills;
    public int totalMinionKills;
    public int totalNeutralMinionKilled;
    public int totalAssists;
    public int totalTurretsKilled;
}
