package com.price.comparator.controller;

import com.price.comparator.entity.links.LinksCategory;
import com.price.comparator.service.LinksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class LinksController {

    @Autowired
    LinksService linksService;

    @PostMapping("/create-categories")
    public List<LinksCategory> createAllCategoriesFromSite() throws IOException {
        return linksService.createAllCategoriesFromSite();
    }

    @GetMapping("/get-categories-from-db")
    public List<LinksCategory> getCategoriesFromDb(){
        return linksService.getCategoriesFromDb();
    }

    @GetMapping("/get-categories-like-title")
    public List<LinksCategory> getCategoriesByTitle(@RequestParam String title){
        return linksService.getCategoriesByTitle(title);
    }

    @PostMapping("/create-products-from-site")
    public void createProductsFromSite() throws IOException {
        linksService.createProductsFromSite();
    }

    @PutMapping("/change-product-active-status")
    public void changeProductActiveStatus(@RequestParam List<String> categoryId,
            @RequestParam(required = false) Boolean statusUpdate){
        linksService.changeProductActiveStatus(categoryId, statusUpdate);
    }
}
