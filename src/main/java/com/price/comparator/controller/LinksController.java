package com.price.comparator.controller;

import com.price.comparator.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
public class LinksController {

    @Autowired
    Service service;

    @GetMapping("/test")
    public void objectTest() throws IOException {
        service.response();
    }
}
