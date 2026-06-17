package org.ecommerce.project.service;

import jakarta.validation.constraints.NotBlank;
import org.ecommerce.project.model.Category;
import org.ecommerce.project.payload.CategoryDTO;
import org.ecommerce.project.payload.CategoryResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface CategoryService {
    CategoryResponse getAllCategories(Integer pageNumber,Integer pageSize, String sortBy, String SortDir);
    CategoryDTO addCategory(CategoryDTO categoryDTO);

    String deleteCategory(Long id);

    CategoryDTO updateCategory(CategoryDTO categoryDTO, Long id);

    Optional<Category> findByCategoryName(@NotBlank(message = "Category should not be blank") String categoryName);
}
