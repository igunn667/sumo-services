package com.sumo.rest;

import com.sumo.model.SumoQueryRequest;
import com.sumologic.client.model.LogMessage;
import com.sumologic.client.model.SearchResponse;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by iangunn on 6/1/16.
 */
@RestController
@RequestMapping("/sumo/")
@Log
public class ApplicationRestController {


    @Autowired
    SumoRestHelper sumoRestHelper;

    @RequestMapping(value = "/query/", method = RequestMethod.POST)
    public ResponseEntity<HashMap> performQuery(@RequestBody SumoQueryRequest sumoQueryRequest){
        log.info("I got a request for " + sumoQueryRequest.getQueryString() + " and start time of " + sumoQueryRequest.getStartTime());
        SearchResponse searchResponse = sumoRestHelper.performSearch(sumoQueryRequest);
        return new ResponseEntity<HashMap>(transformResponse(searchResponse), HttpStatus.OK);

    }
    @RequestMapping(value = "/query/auth", method = RequestMethod.POST)
    public ResponseEntity<HashMap> performQueryWithHeaders(@RequestHeader("username") String usernanme, @RequestHeader("password")String password, @RequestBody SumoQueryRequest sumoQueryRequest){

        log.info("Base 64 encoded username is : " + usernanme);
        log.info("I got a request for " + sumoQueryRequest.getQueryString() + " and start time of " + sumoQueryRequest.getStartTime());
        SearchResponse searchResponse = sumoRestHelper.performSearch(sumoQueryRequest);
        return new ResponseEntity<HashMap>(transformResponse(searchResponse), HttpStatus.OK);

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

    @RequestMapping(value = "date")
    public Date now(){
        return new Date();
    }
}
