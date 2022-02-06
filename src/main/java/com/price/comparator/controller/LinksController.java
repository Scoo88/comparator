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

    @GetMapping("/first-level")
    public List<LinksCategory> getCategoriesFirstLevel() throws IOException {
        return linksService.getCategoriesFirstLevel();
    }

    @GetMapping("/second-level")
    public List<LinksCategory> getCategoriesSecondLevel() throws IOException {
        return linksService.getCategoriesSecondLevel();
    }

    @GetMapping("/third-level")
    public List<LinksCategory> getCategoriesThirdLevel() throws IOException {
        return linksService.getCategoriesThirdLevel();
    }

    @GetMapping("/get-product-list")
    public void getProducts() throws IOException {
        linksService.getProducts();
    }
}
