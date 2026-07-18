package org.ecommerce.project.service;

import org.ecommerce.project.exception.ResourceNotFoundException;
import org.ecommerce.project.model.Category;
import org.ecommerce.project.model.Product;
import org.ecommerce.project.payload.ProductDTO;
import org.ecommerce.project.payload.ProductResponse;
import org.ecommerce.project.repository.CategoryRepository;
import org.ecommerce.project.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImp implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProductDTO addProduct(Long categoryId, Product product) {
        Category category= categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category", "categoryId","Category"));

        product.setCategory(category);
        double specialPrice= product.getSpecialPrice()-(product.getDiscount()*0.01 * product.getPrice());
        product.setSpecialPrice(specialPrice);

        Product savedProduct= productRepository.save(product);

         return modelMapper.map(savedProduct , ProductDTO.class);

    }

    @Override
    public ProductResponse getAllProducts() {
        List<Product> productList=productRepository.findAll();
        List<ProductDTO> productDTOList= productList.stream()
                .map(item->modelMapper.map(item, ProductDTO.class))
                .toList();

//        ProductResponse productResponse=new ProductResponse(productDTOList);
        return new ProductResponse(productDTOList);
    }

    @Override
    public ProductResponse searchProductByCategory(Long categoryId) {
        Category category= categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category" ,"categoryId","Category"));
        List<Product> productList=productRepository.findByCategoryOrderByPriceAsc(category);
        List<ProductDTO> productDTOList= productList.stream()
                .map(item->modelMapper.map(item, ProductDTO.class))
                .toList();

//        ProductResponse productResponse=new ProductResponse(productDTOList);
        return new ProductResponse(productDTOList);
    }

    @Override
    public ProductResponse searchByKeyword(String keyword) {
        List<Product> productList= productRepository.findByProductNameLikeIgnoreCase('%'+keyword+'%');
        List<ProductDTO> productDTOList= productList.stream()
                .map(item->modelMapper.map(item,ProductDTO.class))
                .toList();

        return new ProductResponse(productDTOList);
    }

    @Override
    public ProductDTO updateProduct(Long productId) {

        Product product= productRepository.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("ProductId","ProductId","ProductId"));
        product.setProductName("Robot");
        productRepository.save(product);

        return modelMapper.map(product,ProductDTO.class);
    }
}
