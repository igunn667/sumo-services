package com.sumo.services;

import com.sumo.model.SumoQueryRequest;
import com.sumo.model.SumoQueryResponse;
import com.sumo.util.AsyncSumoCaller;
import com.sumo.util.SumoCacheUtil;
import com.sumologic.client.SumoLogicClient;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Log
@Getter
@Setter
public class QueryService {

    private SumoLogicClient sumoLogicClient;

    @Autowired
    AsyncSumoCaller sumoCaller;

    @Autowired
    SumoCacheUtil cache;

    public String performSearchAsync(SumoQueryRequest sumoQueryRequest, String hashedUsername, String hashedPassword) {
        SumoLogicClient client = new SumoLogicClient(base64Decode(hashedUsername), base64Decode(hashedPassword));
        sumoQueryRequest.setId(UUID.randomUUID().toString().replace("-", ""));
        sumoCaller.performAndCacheQuery(client, sumoQueryRequest);
        return sumoQueryRequest.getId();
    }

    public SumoQueryResponse returnRequestBasedOnID(String requestId) {
        return cache.getObjectFromCache(requestId);
    }

    String base64Decode(String string) {
        byte[] array = Base64.decodeBase64(string.getBytes());
        return new String(array);
    }
}