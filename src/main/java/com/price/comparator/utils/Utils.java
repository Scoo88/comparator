package com.price.comparator.utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public class Utils {
    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor)
    {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    public List<LocalDate> listOfDays(LocalDate dateFrom, LocalDate dateTo) {
        List<LocalDate> listOfDays = new ArrayList<>();
        LocalDate dateToAdd = dateFrom;

        listOfDays.add(dateToAdd);

        do {
            dateToAdd = dateToAdd.plusDays(1L);
            listOfDays.add(dateToAdd);
        } while (dateToAdd.compareTo(dateTo) < 0);

        return listOfDays;
    }
}
