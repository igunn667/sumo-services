package com.sumo.controllers;

import com.sumo.model.SumoQueryRequest;
import com.sumo.model.SumoQueryResponse;
import com.sumo.services.QueryService;
import com.sumologic.client.model.LogMessage;
import com.sumologic.client.model.SearchResponse;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/query")
@Log
public class QueryController {

    @Autowired
    QueryService queryService;

    @RequestMapping("/{requestId}")
    public Map<String, String> getRequestFromCache(@PathVariable String requestId) {
        log.info("I got a request for id " + requestId);
        SumoQueryResponse searchResponse = queryService.returnRequestBasedOnID(requestId);
        return searchResponse.getReturnValues();
    }


    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> performAsyncQuery(@RequestHeader("username") String username, @RequestHeader("password") String password, @RequestBody SumoQueryRequest sumoQueryRequest) {
        log.info("Base 64 encoded username is : " + username);
        log.info("I got a request for " + sumoQueryRequest.getQueryString() + " and start time of " + sumoQueryRequest.getStartTime());
        String id = queryService.performSearchAsync(sumoQueryRequest, username, password);
        return new ResponseEntity<>(buildUrl(id), HttpStatus.OK);
    }

    private String buildUrl(String id) {
        return MvcUriComponentsBuilder.fromMethodName(QueryController.class, "getRequestFromCache", id)
                .buildAndExpand().toUriString();
    }
}