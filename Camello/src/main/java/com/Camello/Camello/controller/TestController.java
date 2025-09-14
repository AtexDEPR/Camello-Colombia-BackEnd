package com.Camello.Camello.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "Test endpoint working";
    }

    @GetMapping("/api/test")
    public String apiTest() {
        return "API Test endpoint working";
    }
}