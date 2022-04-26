package com.price.comparator.check.store.repository;

import com.price.comparator.check.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {
    Optional<Store> findByStoreName(String storeName);
    List<Store> findByActive(boolean activeStatus);
}
