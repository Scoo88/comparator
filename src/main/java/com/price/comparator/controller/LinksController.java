package com.price.comparator.controller;

import com.price.comparator.entity.links.LinksCategory;
import com.price.comparator.service.LinksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class LinksController {

    @Autowired
    LinksService linksService;

    @GetMapping("/get-categories")
    public List<LinksCategory> g() throws IOException {
        return linksService.getAllCategoriesFromSite();
    }

    @GetMapping("/get-products-from-site")
    public void getProductsFromSite() throws IOException {
        linksService.getProductsFromSite();
    }
}
