package org.ecommerce.project.service;

import org.ecommerce.project.model.Category;
import org.ecommerce.project.repository.ControllerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class Categoryserviceimp implements Categoryservice {

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
        List<Category> categoryList= controllerRepo.findAll();
        Category cat =categoryList.stream()
                .filter(c->c.getCategoryId().equals(id))
                .findFirst()
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Resource not found"));

        controllerRepo.delete(cat);
        return "Category with id "+ id +" successfully deleted";
    }

    @Override
    public Category updateCategory(Category category, Long id) {
        List<Category> categoryList= controllerRepo.findAll();
        Optional<Category> cat= categoryList.stream()
                .filter(c->c.getCategoryId().equals(id))
                .findFirst();

        if(cat.isPresent()){
            Category x=cat.get();
            x.setCategoryName(category.getCategoryName());
            controllerRepo.save(x);
            return x;
        }
         throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Category not found");
    }
}
