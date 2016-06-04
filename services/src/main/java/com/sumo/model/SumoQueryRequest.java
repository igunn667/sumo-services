package com.sumo.model;

import lombok.Data;
import java.util.Date;

@Data
public class SumoQueryRequest {
    String id;
    String queryString;
    Date startTime;
    Date endTime;
}
