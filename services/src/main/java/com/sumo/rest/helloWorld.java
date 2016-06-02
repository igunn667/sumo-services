package com.sumo.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by iangunn on 5/24/16.
 */
@RestController
public class helloWorld {

    @RequestMapping("/")
    public String foo(){
        return "Hello world";
    }
}
