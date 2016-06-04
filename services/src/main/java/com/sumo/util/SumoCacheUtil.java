package com.sumo.util;

import com.sumo.model.SumoQueryResponse;
import com.sumologic.client.model.SearchResponse;
import lombok.extern.java.Log;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;


@Service
@Log
public class SumoCacheUtil {

    private Map<String, SumoQueryResponse> cache = new HashMap<>();
    private static long CACHE_TIME_IN_HOURS = 3;

    public SumoQueryResponse getObjectFromCache(String id) {
        if (cache.containsKey(id)) {
            return cache.get(id);
        }
        return null;
    }

    public boolean putObjectIntoCache(SumoQueryResponse sumoQueryResponse) {
        if (cache.containsKey(sumoQueryResponse.getId())) {
            return false;
        }
        cache.put(sumoQueryResponse.getId(), sumoQueryResponse);
        return true;
    }

    public boolean putObjectIntoCache(SearchResponse searchResponse, String id) {
        if (cache.containsKey(id)) {
            return false;
        }
        SumoQueryResponse sumoQueryResponse = new SumoQueryResponse(id, new Date(), searchResponse);
        cache.put(id, sumoQueryResponse);
        return true;
    }

    @Scheduled(fixedDelay = 60000)
    private void cleanUpCache() {
        log.info("Clearing cache");
        Date now = new Date();
        Set<String> keys = cache.keySet();
        keys.parallelStream().forEach(key -> {
            SumoQueryResponse response = cache.get(key);
            if (response != null && TimeUnit.MILLISECONDS.toHours(now.getTime() - response.getCreated().getTime()) > CACHE_TIME_IN_HOURS) {
                cache.remove(key);
            }
        });
        log.info("Done clearing cache");
    }
}