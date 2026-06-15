package org.ecommerce.project.service;

import org.ecommerce.project.exception.ResourceNotFoundException;
import org.ecommerce.project.model.Category;
import org.ecommerce.project.repository.ControllerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;

@Service
public class CategoryServiceImp implements CategoryService {

    @Autowired
    private ControllerRepo controllerRepo;

    @Override
    public List<Category> getAllCategories() {
        return controllerRepo.findAll();
    }

    @Override
    public String addCategory(Category category) {
           Category cat=controllerRepo.save(category);
           return " Category added succesfully  ";

    }

    @Override
    public String deleteCategory(Long id) {
        Category cat= controllerRepo.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Category","CategoryId",id));

        controllerRepo.delete(cat);
        return "Category with id "+ id +" successfully deleted";
    }

    @Override
    public Category updateCategory(Category category, Long id) {
        Category cat= controllerRepo.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Category","CategoryId",id));

        category.setCategoryId(id);
        controllerRepo.save(category);
        return category;

    }
}
