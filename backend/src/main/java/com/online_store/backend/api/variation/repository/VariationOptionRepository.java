package com.online_store.backend.api.variation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.online_store.backend.api.variation.entity.VariationOption;

public interface VariationOptionRepository extends JpaRepository<VariationOption, Long> {

}
