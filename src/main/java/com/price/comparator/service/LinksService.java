package com.price.comparator.service;

import com.price.comparator.entity.links.LinksCategory;

import java.io.IOException;
import java.util.List;

public interface LinksService {
    List<LinksCategory> getAllCategoriesFromSite();

    void getProductsFromSite() throws IOException;

}
