package com.price.comparator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Service
public class Service {

    String urlLinks = "https://www.links.hr/hr";

    public void response() throws IOException {
        Document document = Jsoup.connect(urlLinks).get();
        document.outputSettings().charset("UTF-16");

        Elements elementsByClass = document.getElementsByClass("list category-parents");

        List<CategoryList> categoryLists = new ArrayList<>();

        elementsByClass.forEach(element -> {
            CategoryList categoryList = new CategoryList();
            categoryList.setClassName(String.valueOf(element.select("li class")));
//            categoryList.setLink();
//            categoryList.setProductCount();
//            categoryList.setTitle();
            categoryLists.add(categoryList);
        });

        System.out.println("test elemenata: " + elementsByClass);
        System.out.println("test liste: " + categoryLists);
    }
}
