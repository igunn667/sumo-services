package com.sumo.util;

import com.sumo.model.SumoQueryRequest;
import com.sumologic.client.SumoLogicClient;
import com.sumologic.client.model.SearchRequest;
import com.sumologic.client.model.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class AsyncSumoCaller {

    @Autowired
    SumoCacheUtil sumoCacheUtil;

    @Async("threadPoolTaskExecutor")
    public void performAndCacheQuery(SumoLogicClient client, SumoQueryRequest sumoQueryRequest){
        try {
            SearchRequest searchRequest = new SearchRequest();
            searchRequest.setQuery(sumoQueryRequest.getQueryString());
            searchRequest.setFromTime(new Date());
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.HOUR_OF_DAY, -4);
            Date date = calendar.getTime();
            searchRequest.setFromTime(date);
            SearchResponse response = client.search(searchRequest);
            sumoCacheUtil.putObjectIntoCache(response, sumoQueryRequest.getId());
        }
        catch (Exception ignored){

        }
    }
}
