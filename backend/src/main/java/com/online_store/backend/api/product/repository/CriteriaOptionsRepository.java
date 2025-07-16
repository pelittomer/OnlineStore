package com.online_store.backend.api.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.online_store.backend.api.product.entity.CriteriaOptions;

public interface CriteriaOptionsRepository extends JpaRepository<CriteriaOptions, Long> {

}
