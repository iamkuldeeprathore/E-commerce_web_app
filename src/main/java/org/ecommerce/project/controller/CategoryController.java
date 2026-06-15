package org.ecommerce.project.controller;

import jakarta.validation.Valid;
import org.ecommerce.project.model.Category;
import org.ecommerce.project.service.CategoryService;
import org.ecommerce.project.service.CategoryServiceImp;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {
    private CategoryService categoryservice;

    public CategoryController(CategoryServiceImp categoryserviceimp){
        this.categoryservice=categoryserviceimp;

    }

    @GetMapping("public/categories")
    public ResponseEntity<List<Category>>  getAllCategories(){

        List<Category> output= categoryservice.getAllCategories();
        return new ResponseEntity<List<Category>>(output,HttpStatus.OK);
    }

    @PostMapping("public/categories")
    public ResponseEntity<String> addCategory( @Valid @RequestBody Category category){
        try {

            String status = categoryservice.addCategory(category);
            return new ResponseEntity( status ,HttpStatus.OK);
        }
        catch(ResponseStatusException e){
            return new ResponseEntity(e.getReason(),HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("public/categories/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id){
       try{

           String status = categoryservice.deleteCategory(id);
           return new ResponseEntity<String>(status, HttpStatus.OK);

       }
       catch (ResponseStatusException e){
           return new ResponseEntity<>(e.getReason(), e.getStatusCode());
       }
    }

    @PutMapping("admin/categories/{id}")
    public ResponseEntity<Category> updateCategory(@RequestBody Category category,
                                   @PathVariable Long id){
        try {
           Category cat = categoryservice.updateCategory(category, id);
           return  new ResponseEntity<Category>(cat, HttpStatus.OK);
        }
        catch (ResponseStatusException e){
            return new ResponseEntity<Category>(e.getStatusCode());
        }
    }
}
