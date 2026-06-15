package org.ecommerce.project.service;

import org.ecommerce.project.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    String addCategory(Category category);

    String deleteCategory(Long id);

    Category updateCategory(Category category, Long id);
}
