package com.fxzhang.duo.service.response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by fanny on 24/12/16.
 */

public class LeagueDto implements Serializable {
    public String queue;
    public String name;
    public String participantId;
    public String tier;
    public List<LeagueEntryDto> entries;
}
