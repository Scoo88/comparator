package com.price.comparator.check.store.controller;

import com.price.comparator.check.store.dto.StoreDto;
import com.price.comparator.check.store.dto.StoreUpdateDto;
import com.price.comparator.check.store.exception.PriceException;
import com.price.comparator.check.store.service.StoreService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/store")
@Tag(name = "Store")
public class StoreController {

    @Autowired
    StoreService storeService;

    @PostMapping("/create-store")
    public ResponseEntity<StoreDto> createStore(@RequestParam String storeName, @RequestParam String link)
            throws PriceException {
        return new ResponseEntity<>(storeService.createStore(storeName, link), HttpStatus.OK);
    }

    @GetMapping("/get-all-stores")
    public ResponseEntity<List<StoreDto>> getAllStores(){
        return new ResponseEntity<>(storeService.getAllStores(), HttpStatus.OK);
    }

    @GetMapping("/get-store-id")
    public ResponseEntity<StoreDto> getStoreById(@RequestParam Long id) throws PriceException {
        return new ResponseEntity<>(storeService.getStoreById(id), HttpStatus.OK);
    }

    @PutMapping("/update-store")
    public ResponseEntity<StoreDto> updateStore(@RequestParam Long id, @RequestBody StoreUpdateDto storeUpdateDto)
            throws PriceException {
        return new ResponseEntity<>(storeService.updateStore(id, storeUpdateDto),HttpStatus.OK);
    }

    @DeleteMapping("/delete-store")
    public ResponseEntity<String> deleteStore(@RequestParam Long id) throws PriceException {
        return new ResponseEntity<>(storeService.softDeleteStore(id),HttpStatus.OK);
    }
}

