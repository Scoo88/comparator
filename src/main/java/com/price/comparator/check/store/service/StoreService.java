package com.price.comparator.check.store.service;

import com.price.comparator.check.store.dto.StoreDto;
import com.price.comparator.check.store.dto.StoreUpdateDto;
import com.price.comparator.check.store.entity.Store;
import com.price.comparator.check.store.repository.StoreRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StoreService {
    @Autowired
    StoreRepository storeRepository;

    @Autowired
    ModelMapper modelMapper;

    public StoreDto createStore(String storeName, String link){
        StoreDto response;

        Optional<Store> storeCheckDb = storeRepository.findByStoreName(storeName);
        if (storeCheckDb.isPresent()){
            throw new RuntimeException("Store already exists.");
        }

        Store store = new Store(storeName, link);
        store.setCreatedAt(LocalDateTime.now());
        store.setActive(true);

        store = storeRepository.saveAndFlush(store);

        response = modelMapper.map(store, StoreDto.class);

        return response;
    }

    public List<StoreDto> getAllStores(){
        List<StoreDto> response;

        List<Store> listFromDb;
        listFromDb = storeRepository.findAll();
        response = List.of(modelMapper.map(listFromDb, StoreDto[].class));

        return response;
    }

    public StoreDto getStoreById(Long id){
        StoreDto response;

        Optional<Store> storeFromDb = storeRepository.findById(id);
        if (storeFromDb.isEmpty()){
            throw new RuntimeException("There is no store with id: " + id);
        }

        response = modelMapper.map(storeFromDb, StoreDto.class);

        return response;
    }

    public StoreDto updateStore(Long id, StoreUpdateDto storeUpdateDto){
        StoreDto response;

        Optional<Store> storeFromDb = storeRepository.findById(id);
        if (storeFromDb.isEmpty()){
            throw new RuntimeException("There is no store with id: " + id);
        }

        Store updateStore = storeFromDb.get();

        if (storeUpdateDto.getStoreName() != null){
            updateStore.setStoreName(storeUpdateDto.getStoreName());
        }
        if (storeUpdateDto.getLink() != null){
            updateStore.setLink(storeUpdateDto.getLink());
        }
        if (storeUpdateDto.getActive() != null){
            updateStore.setActive(storeUpdateDto.getActive());
        }

        response = modelMapper.map(updateStore, StoreDto.class);

        return response;
    }

    public String softDeleteStore(Long id){
        String message;

        Optional<Store> storeFromDb = storeRepository.findById(id);
        if (storeFromDb.isEmpty()){
            throw new RuntimeException("There is no store with id: " + id);
        }

        Store deleteStore = storeFromDb.get();
        deleteStore.setActive(false);

        message = "Store " + deleteStore.getStoreName() + " removed.";

        return message;
    }

}