package com.ferrinsa.fairpartner.category.service;

import com.ferrinsa.fairpartner.category.model.CategoryEntity;

public interface CategoryEntityService {

    CategoryEntity findCategoryById(Long id);

    CategoryEntity findCategoryByName(String name);

    CategoryEntity getDefaultCategory();

}
