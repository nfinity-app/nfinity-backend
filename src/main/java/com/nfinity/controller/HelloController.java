package com.nfinity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

@RestController
public class HelloController {
    @GetMapping
    public String hello(){
        return "hello: " + new Timestamp(System.currentTimeMillis());
    }
}
