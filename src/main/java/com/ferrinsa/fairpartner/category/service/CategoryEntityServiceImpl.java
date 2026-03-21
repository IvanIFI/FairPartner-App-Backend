package com.ferrinsa.fairpartner.category.service;

import com.ferrinsa.fairpartner.category.model.CategoryEntity;
import com.ferrinsa.fairpartner.category.repository.CategoryEntityRepository;
import com.ferrinsa.fairpartner.exception.category.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryEntityServiceImpl implements CategoryEntityService {

    private final CategoryEntityRepository categoryRepository;

    @Autowired
    public CategoryEntityServiceImpl(CategoryEntityRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryEntity findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow( () -> new CategoryNotFoundException(String.valueOf(id)));
    }

    @Override
    public CategoryEntity findCategoryByName(String name) {
        return categoryRepository.findByName(name)
                .orElseThrow( () -> new CategoryNotFoundException(name));
    }

    @Override
    public CategoryEntity getDefaultCategory() {
        return this.findCategoryByName(CategoryEntity.DEFAULT_CATEGORY_NAME);
    }

}
