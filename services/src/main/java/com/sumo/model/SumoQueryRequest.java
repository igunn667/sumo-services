package com.sumo.model;

import lombok.Data;
import java.util.Date;

/**
 * Created by iangunn on 6/1/16.
 */
@Data
public class SumoQueryRequest {

    String queryString;
    Date startTime;
    Date endtime;
}
