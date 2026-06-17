package org.ecommerce.project.controller;

import jakarta.validation.Valid;
import org.ecommerce.project.config.AppConstant;
import org.ecommerce.project.exception.APIException;
import org.ecommerce.project.model.Category;
import org.ecommerce.project.payload.APIResponse;
import org.ecommerce.project.payload.CategoryDTO;
import org.ecommerce.project.payload.CategoryResponse;
import org.ecommerce.project.service.CategoryService;
import org.ecommerce.project.service.CategoryServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class CategoryController {

    private CategoryService categoryservice;

    public CategoryController(CategoryService categoryserviceimp){
        this.categoryservice=categoryserviceimp;

    }

    @GetMapping("public/categories")
    public ResponseEntity<CategoryResponse>  getAllCategories(@RequestParam(name="pageNumber",defaultValue = AppConstant.PAGE_NUMBER, required = false)
                                                              Integer pageNumber,
                                                              @RequestParam(name="pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false)
                                                              Integer pageSize,
                                                              @RequestParam(name = "sortBy",defaultValue = AppConstant.SORT_CATEGORY_BY, required = false)
                                                              String sortBy,
                                                              @RequestParam(name = "sortOrder",defaultValue = AppConstant.SORT_DIR, required = false)
                                                              String sortOrder
                                                              )
    {

        CategoryResponse output= categoryservice.getAllCategories(pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(output,HttpStatus.OK);
    }

    @PostMapping("public/categories")
    public ResponseEntity<CategoryDTO> addCategory(@Valid @RequestBody CategoryDTO categoryDTO){

//            Optional<Category> x= categoryservice.findByCategoryName(category.getCategoryName());
//            if(x.isPresent()){
//                throw new APIException(category.getCategoryName()+"Already Exit");
//            }
//          String status = categoryservice.addCategory(category);
//
//            return new ResponseEntity<>( status ,HttpStatus.OK);
             CategoryDTO savedCategoryDTO =categoryservice.addCategory(categoryDTO);
             return new ResponseEntity<>(savedCategoryDTO,HttpStatus.CREATED);


    }
    @DeleteMapping("public/categories/{id}")
    public ResponseEntity<APIResponse> deleteCategory(@PathVariable Long id){

           String status = categoryservice.deleteCategory(id);
           APIResponse resp= new APIResponse();
           resp.setMessage(status);
           return new ResponseEntity<>(resp, HttpStatus.OK);


    }

    @PutMapping("admin/categories/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@RequestBody CategoryDTO categoryDTO,
                                   @PathVariable Long id){

           CategoryDTO cat = categoryservice.updateCategory(categoryDTO, id);
           return  new ResponseEntity<>(cat, HttpStatus.OK);


    }
}
