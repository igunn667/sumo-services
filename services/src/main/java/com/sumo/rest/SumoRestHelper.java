package com.sumo.rest;

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

/**
 * Created by iangunn on 5/24/16.
 */
@Service
public class SumoRestHelper {

    private String username;
    private String password;
    private final String CREDETIAL_PATH = "src/main/resources/";
    private final String CREDETIAL_FILE = "credetials.txt";

    @PostConstruct
    public String test() {
        readFromFile();

        SumoLogicClient client = new SumoLogicClient(username, password);
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setQuery("_sourceCategory=Application/trader-services/live | parse \"InventoryPortImpl * - GetPendingItemCount found: * Ads for TransactionId: *\" as uuid, pendingcount, trans | \n" +
                "number(pendingcount)  | max (pendingcount)");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, -1);
        Date date = calendar.getTime();
        searchRequest.setFromTime(date);
        SearchResponse searchResponse= client.search(searchRequest);


        return "";
    }


    private void readFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(CREDETIAL_PATH + CREDETIAL_FILE))) {
            String user = br.readLine();
            String pass = br.readLine();
            this.username = user;
            this.password = pass;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
