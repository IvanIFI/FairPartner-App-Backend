package com.ferrinsa.fairpartner.category.repository;

import com.ferrinsa.fairpartner.category.model.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryEntityRepository extends JpaRepository<CategoryEntity,Long> {

    Optional<CategoryEntity> findByName(String name);

}
