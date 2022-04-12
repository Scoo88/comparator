package com.price.comparator.entity.links.controller;

import com.price.comparator.check.store.dto.StoreDto;
import com.price.comparator.check.store.dto.StoreUpdateDto;
import com.price.comparator.check.store.service.StoreService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/store")
@Tag(name = "Store")
public class StoreController {
    @Autowired
    StoreService storeService;

    @PostMapping("/create-store")
    public StoreDto createStore(@RequestParam String storeName, @RequestParam String link){
        return storeService.createStore(storeName, link);
    }

    @GetMapping("/get-all-stores")
    public List<StoreDto> getAllStores(){
        return storeService.getAllStores();
    }

    @GetMapping("/get-store-id")
    public StoreDto getStoreById(@RequestParam Long id){
        return storeService.getStoreById(id);
    }

    @PutMapping("/update-store")
    public StoreDto updateStore(@RequestParam Long id, @RequestBody StoreUpdateDto storeUpdateDto){
        return storeService.updateStore(id, storeUpdateDto);
    }

    @DeleteMapping("/delete-store")
    public String deleteStore(@RequestParam Long id){
        return storeService.softDeleteStore(id);
    }
}

