package com.groceryshop.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HeartBeatController {
    @GetMapping("HbtChk")
    public String serviceHealth(){
        return "Heart of service is beating !!";
    }
}
