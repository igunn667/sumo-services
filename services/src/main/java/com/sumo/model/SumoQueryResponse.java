package com.sumo.model;

import com.sumologic.client.model.LogMessage;
import com.sumologic.client.model.SearchResponse;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
public class SumoQueryResponse {

    private String id;
    private Date created;
    private Map<String, String> returnValues;

    public SumoQueryResponse(String id, Date created, SearchResponse response){
        this.id = id;
        this.created = created;
        this.returnValues = transformResponse(response);
    }

    private HashMap<String, String> transformResponse(SearchResponse searchResponse){
        HashMap<String,String> returnMap = new HashMap<>();
        for(LogMessage message : searchResponse.getMessages()){
            message.getProperties().keySet().parallelStream()
                    .filter(s -> !returnMap.containsKey(s))
                    .forEach(s -> returnMap.put(s, message.getProperties().get(s)));
        }
        return returnMap;

    }
}
