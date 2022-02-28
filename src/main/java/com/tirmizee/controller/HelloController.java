package com.tirmizee.controller;

import com.tirmizee.dto.HelloRequest;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HelloController {

    @PostMapping(path = "/hello")
    public String helloPost(@RequestBody HelloRequest request) {
        return "hello " + request.getName();
    }

    @GetMapping(path = "/hello")
    public String helloGet() {
        return "hello world";
    }

}
