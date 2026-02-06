package com.ferrinsa.fairpartner.exception.category;

import com.ferrinsa.fairpartner.exception.AppException;

public class CategoryNotFoundException extends AppException {
    public CategoryNotFoundException(String value) {
        super("CATEGORY_NOT_FOUND", "No se ha encontrado la categoría: " + value);
    }
}
