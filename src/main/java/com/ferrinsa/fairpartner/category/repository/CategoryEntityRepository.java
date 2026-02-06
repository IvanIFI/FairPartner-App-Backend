package com.ferrinsa.fairpartner.category.repository;

import com.ferrinsa.fairpartner.category.model.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryEntityRepository extends JpaRepository<CategoryEntity,Long> {
}
