package com.ferrinsa.fairpartner.category.controller;

import com.ferrinsa.fairpartner.category.service.CategoryEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/category")
public class CategoryEntityController {

    private final CategoryEntityService categoryService;

    @Autowired
    public CategoryEntityController(CategoryEntityService categoryService) {
        this.categoryService = categoryService;
    }
}
