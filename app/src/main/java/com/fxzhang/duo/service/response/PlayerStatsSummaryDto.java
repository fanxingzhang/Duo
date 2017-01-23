package com.fxzhang.duo.service.response;

import java.io.Serializable;

/**
 * Created by fanny on 20/12/16.
 */

public class PlayerStatsSummaryDto implements Serializable {
    public int wins;
    public int losses;
    public long modifyDate;
    public String playerStatSummaryType;
    public AggregatedStatsDto aggregatedStats;
}
