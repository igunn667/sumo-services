package com.sumo.rest;

import com.sumo.model.SumoQueryRequest;
import com.sumo.model.SumoQueryResponse;
import com.sumo.util.AsyncSumoCaller;
import com.sumo.util.SumoCacheUtil;
import com.sumologic.client.SumoLogicClient;
import com.sumologic.client.model.SearchRequest;
import com.sumologic.client.model.SearchResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
@Log
@Getter
@Setter
public class SumoRestHelper {

    private String username;
    private String password;
    private final String CREDENTIAL_PATH = "src/main/resources/";
    private final String CREDENTIAL_FILE = "credentials.txt";
    private SumoLogicClient sumoLogicClient;

    @Autowired
    AsyncSumoCaller sumoCaller;

    @Autowired
    SumoCacheUtil cache;


    @PostConstruct
    public void readFromFileAndSetCredentials() {
        try {
//            readFromFile();
        } catch (Exception e) {
            log.info("There was a problem reading in credentials from file. Perhaps its not there?");
        }
    }

    public SearchResponse performSearch(SumoQueryRequest sumoQueryRequest) {
        if (sumoQueryRequest.getEndtime() == null) {
            if (sumoQueryRequest.getStartTime() == null) {
                sumoQueryRequest.setStartTime(new Date());
            }
            return this.performSearch(sumoQueryRequest.getQueryString(), sumoQueryRequest.getStartTime());
        }
        return this.performSearch(sumoQueryRequest.getQueryString(), sumoQueryRequest.getStartTime(), new Date());
    }

    public String performSearchAsync(SumoQueryRequest sumoQueryRequest, String hashedUsername, String hashedPassword) {
        SumoLogicClient client = new SumoLogicClient(base64Decode(hashedUsername), base64Decode(hashedPassword));
        sumoQueryRequest.setId(UUID.randomUUID().toString().replace("-", ""));
        sumoCaller.performAndCacheQuery(client, sumoQueryRequest);
        return sumoQueryRequest.getId();

    }

    public SearchResponse performSearch(SumoQueryRequest sumoQueryRequest, String hashedUsername, String hashedPassword) {
        this.sumoLogicClient = new SumoLogicClient(base64Decode(hashedUsername), base64Decode(hashedPassword));
        if (sumoQueryRequest.getEndtime() == null) {
            if (sumoQueryRequest.getStartTime() == null) {
                sumoQueryRequest.setStartTime(new Date());
            }
            return this.performSearch(sumoQueryRequest.getQueryString(), sumoQueryRequest.getStartTime());
        }
        return this.performSearch(sumoQueryRequest.getQueryString(), sumoQueryRequest.getStartTime(), new Date());
    }

    public SearchResponse performSearch(String query, Date start) {
        return this.performSearch(query, start, new Date());
    }

    public SearchResponse performSearch(String query, Date start, Date end) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setQuery(query);
        searchRequest.setFromTime(start);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, -4);
        Date date = calendar.getTime();
        searchRequest.setFromTime(date);
        return sumoLogicClient.search(searchRequest);

    }


    public SumoQueryResponse returnRequestBasedOnID(String requestId) {
        return cache.getObjectFromCache(requestId);
    }


    private void readFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(CREDENTIAL_PATH + CREDENTIAL_FILE))) {
            String user = br.readLine();
            String pass = br.readLine();
            log.info("Username is " + base64Encode(user));
            log.info("password is " + base64Encode(pass));
            this.sumoLogicClient = new SumoLogicClient(user, pass);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String base64Encode(String string) {
        byte[] array = Base64.encodeBase64(string.getBytes());
        String encodedString = new String(array);
        return encodedString;
    }

    public String base64Decode(String string) {
        byte[] array = Base64.decodeBase64(string.getBytes());
        String decodedString = new String(array);
        return decodedString;
    }


}
