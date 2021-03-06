package com.price.comparator.check.store.service;

import com.price.comparator.check.store.dto.StoreDto;
import com.price.comparator.check.store.dto.StoreUpdateDto;
import com.price.comparator.check.store.entity.Store;
import com.price.comparator.check.store.exception.Messages;
import com.price.comparator.check.store.exception.PriceException;
import com.price.comparator.check.store.repository.StoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class StoreService {

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    ModelMapper modelMapper;

    public StoreDto createStore(String storeName, String link) throws PriceException {
        StoreDto response;

        Optional<Store> storeCheckDb = storeRepository.findByStoreName(storeName);
        if (storeCheckDb.isPresent()){
            log.error(Messages.STORE_ALREADY_EXISTS.getMessage());
            throw new PriceException(Messages.STORE_ALREADY_EXISTS);
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

    public StoreDto getStoreById(Long id) throws PriceException {
        StoreDto response;

        Optional<Store> storeFromDb = storeRepository.findById(id);
        if (storeFromDb.isEmpty()){
            log.error(Messages.STORE_NOT_FOUND.getMessage());
            throw new PriceException(Messages.STORE_NOT_FOUND);
        }

        response = modelMapper.map(storeFromDb.get(), StoreDto.class);

        return response;
    }

    public StoreDto updateStore(Long id, StoreUpdateDto storeUpdateDto) throws PriceException {
        StoreDto response;

        Optional<Store> storeFromDb = storeRepository.findById(id);
        if (storeFromDb.isEmpty()){
            log.error(Messages.STORE_NOT_FOUND.getMessage());
            throw new PriceException(Messages.STORE_NOT_FOUND);
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
        storeRepository.saveAndFlush(updateStore);

        response = modelMapper.map(updateStore, StoreDto.class);

        return response;
    }

    public String softDeleteStore(Long id) throws PriceException {
        String message;

        Optional<Store> storeFromDb = storeRepository.findById(id);
        if (storeFromDb.isEmpty()){
            log.error(Messages.STORE_NOT_FOUND.getMessage());
            throw new PriceException(Messages.STORE_NOT_FOUND);
        }

        Store deleteStore = storeFromDb.get();
        deleteStore.setActive(false);
        deleteStore = storeRepository.saveAndFlush(deleteStore);

        message = "Store " + deleteStore.getStoreName() + " removed.";

        return message;
    }

}
