package com.sumo.rest;

import com.sumo.model.SumoQueryRequest;
import com.sumologic.client.SumoLogicClient;
import com.sumologic.client.collectors.model.GetCollectorsResponse;
import com.sumologic.client.model.SearchRequest;
import com.sumologic.client.model.SearchResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.naming.directory.SearchResult;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by iangunn on 5/24/16.
 */
@Service
public class SumoRestHelper {

    private String username;
    private String password;
    private final String CREDETIAL_PATH = "src/main/resources/";
    private final String CREDETIAL_FILE = "credetials.txt";
    private SumoLogicClient sumoLogicClient;

    @PostConstruct
    public void readFromFileAndSetCredentials() {
        readFromFile();
    }

    public SearchResponse performSearch(SumoQueryRequest sumoQueryRequest){
        if(sumoQueryRequest.getEndtime() == null){
            if(sumoQueryRequest.getStartTime() == null){
                sumoQueryRequest.setStartTime(new Date());
            }
            return this.performSearch(sumoQueryRequest.getQueryString(), sumoQueryRequest.getStartTime());
        }
//        Optional<Date> endDate = Optional.of(sumoQueryRequest.getEndtime());
        return this.performSearch(sumoQueryRequest.getQueryString(), sumoQueryRequest.getStartTime(),new Date());
    }

    public SearchResponse performSearch(String query, Date start){
        return this.performSearch(query, start, new Date());
    }

    public SearchResponse performSearch(String query, Date start, Date end){
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setQuery(query);
        searchRequest.setFromTime(start);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, -4);
        Date date = calendar.getTime();
        searchRequest.setFromTime(date);
//        if(end.isPresent()) {
//            searchRequest.setToTime(end.get());
//        }
        return sumoLogicClient.search(searchRequest);


    }




    private void readFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(CREDETIAL_PATH + CREDETIAL_FILE))) {
            String user = br.readLine();
            String pass = br.readLine();
            this.sumoLogicClient = new SumoLogicClient(user, pass);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
