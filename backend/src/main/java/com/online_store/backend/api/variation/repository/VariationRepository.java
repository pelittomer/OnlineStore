package com.online_store.backend.api.variation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.online_store.backend.api.category.entity.Category;
import com.online_store.backend.api.variation.entity.Variation;
import java.util.List;

public interface VariationRepository extends JpaRepository<Variation, Long> {
    List<Variation> findByCategory(Category category);

    List<Variation> findByCategoryIsNull();
}
