package com.sumo.model;

import com.sumologic.client.model.LogMessage;
import com.sumologic.client.model.SearchResponse;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by iangunn on 6/3/16.
 */
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

    public HashMap<String, String> transformResponse(SearchResponse searchResponse){
        HashMap<String,String> returnMap = new HashMap<>();
        for(LogMessage message : searchResponse.getMessages()){
            for(String s :message.getProperties().keySet()){
                if(!returnMap.containsKey(s)){
                    returnMap.put(s, message.getProperties().get(s));
                }
            }
        }
        return returnMap;

    }
}
