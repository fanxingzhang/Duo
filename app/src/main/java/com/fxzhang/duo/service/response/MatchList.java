package com.fxzhang.duo.service.response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by fanxing on 1/23/2017.
 */

public class MatchList implements Serializable {
    public int endIndex;
    public int startIndex;
    public int totalGames;
    public List<MatchReference> matches;
}
