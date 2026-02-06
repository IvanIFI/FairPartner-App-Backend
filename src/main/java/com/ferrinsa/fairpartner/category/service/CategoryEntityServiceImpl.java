package com.ferrinsa.fairpartner.category.service;

import com.ferrinsa.fairpartner.category.repository.CategoryEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryEntityServiceImpl implements CategoryEntityService {

    private final CategoryEntityRepository categoryRepository;

    @Autowired
    public CategoryEntityServiceImpl(CategoryEntityRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
}
