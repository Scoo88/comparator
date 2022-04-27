package com.price.comparator.check.store.startup;

import com.price.comparator.check.store.entity.Store;
import com.price.comparator.check.store.exception.PriceException;
import com.price.comparator.check.store.repository.StoreRepository;
import com.price.comparator.check.store.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
@PropertySource("classpath:application.properties")
public class RunOnStartup {

    @Autowired
    private StoreService storeService;

    @Autowired
    StoreRepository storeRepository;

    @Value("#{${stores}}")
    private Map<String, String> storeMap;

    @EventListener(ApplicationReadyEvent.class)
    private void runOnStartup(){
        storeMap.forEach((storeName, url) -> {
            Optional<Store> checkDb =  storeRepository.findByStoreName(storeName);
            if (checkDb.isEmpty()){
                try {
                    storeService.createStore(storeName, url);
                } catch (PriceException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
