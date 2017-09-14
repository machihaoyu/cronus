package com.fjs.cronus.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {


    @RequestMapping(value = "/api/v1/hello", method = RequestMethod.GET)
    public String index() {

        String str = "Hello World";
        return str;
    }

}