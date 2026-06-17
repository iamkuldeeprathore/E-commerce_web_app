package org.ecommerce.project.service;

import org.ecommerce.project.exception.APIException;
import org.ecommerce.project.exception.ResourceNotFoundException;
import org.ecommerce.project.model.Category;
import org.ecommerce.project.payload.CategoryDTO;
import org.ecommerce.project.payload.CategoryResponse;
import org.ecommerce.project.repository.ControllerRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImp implements CategoryService {

    @Autowired
    private ControllerRepo controllerRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponse getAllCategories(Integer  pageNumber , Integer pageSize ,String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();

        Pageable pageable= PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page <Category> categoryPage= controllerRepo.findAll(pageable);

        List<Category> list= categoryPage.getContent();
        if(list.isEmpty()){
            throw  new APIException("No Category is Present");
        }
        List<CategoryDTO> categoryDTOS=list.stream()
                .map(category -> modelMapper.map(category,CategoryDTO.class))
                .toList();

        CategoryResponse categoryResponse= new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setLast(categoryPage.isLast());
        return categoryResponse;

    }

    @Override
    public CategoryDTO addCategory(CategoryDTO categoryDTO) {

            Category category=modelMapper.map(categoryDTO,Category.class);

            Optional<Category> x=findByCategoryName(category.getCategoryName());
            if(x.isPresent()){
                throw new APIException("Category "+ category.getCategoryName()+" is already exits");
            }

           Category cat=controllerRepo.save(category);
           return modelMapper.map(cat, CategoryDTO.class);

    }

    @Override
    public String deleteCategory(Long id) {
        Category cat = controllerRepo.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Category","CategoryId",id));

        controllerRepo.delete(cat);
        return "Category with id "+ id +" successfully deleted";
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long id) {
        Category category= modelMapper.map(categoryDTO,Category.class);
        Category cat= controllerRepo.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Category","CategoryId",id));

        category.setCategoryId(id);
        Category savedCategory=controllerRepo.save(category);
        return modelMapper.map(savedCategory,CategoryDTO.class);

    }

    @Override
    public Optional<Category> findByCategoryName(String categoryName) {
        Optional<Category> x =controllerRepo.findByCategoryName(categoryName);
        return x;
    }
}
