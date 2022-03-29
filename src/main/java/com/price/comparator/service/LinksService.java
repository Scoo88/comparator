package com.price.comparator.service;

import com.price.comparator.entity.links.LinksCategory;

import java.io.IOException;
import java.util.List;

public interface LinksService {
    List<LinksCategory> createAllCategoriesFromSite();

    List<LinksCategory> getCategoriesFromDb();

    List<LinksCategory> getCategoriesByTitle(String title);

    void createProductsFromSite() throws IOException;

    void changeCategoryActiveStatus(List<String> productId, Boolean statusUpdate);

}
