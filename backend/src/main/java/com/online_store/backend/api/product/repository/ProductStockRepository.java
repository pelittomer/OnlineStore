package com.online_store.backend.api.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.online_store.backend.api.product.entity.ProductStock;

public interface ProductStockRepository extends JpaRepository<ProductStock, Long> {

}
