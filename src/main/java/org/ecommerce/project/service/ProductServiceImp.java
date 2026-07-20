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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductServiceImp implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileService fileService;

    @Value("${project.images")
    private String path;

    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO productDto) {
        Category category= categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category", "categoryId","Category"));
        Product product= modelMapper.map(productDto, Product.class);
        product.setCategory(category);
        double specialPrice= product.getSpecialPrice()-(product.getDiscount()*0.01 * product.getPrice());
        product.setSpecialPrice(specialPrice);

        Product savedProduct= productRepository.save(product);

         return modelMapper.map(savedProduct , ProductDTO.class);

    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sortByAndOrder=sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageDetails= PageRequest.of(pageNumber,pageSize,sortByAndOrder);

        Page<Product> productPage= productRepository.findAll(pageDetails);

        List<Product> productList=productPage.getContent();
        List<ProductDTO> productDTOList= productList.stream()
                .map(item->modelMapper.map(item, ProductDTO.class))
                .toList();

        ProductResponse productResponse=new ProductResponse();
        productResponse.setContent(productDTOList);
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setPagesize(productPage.getSize());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setLastPage(productPage.isLast());
        return productResponse;
    }

    @Override
    public ProductResponse searchProductByCategory(Long categoryId) {
        Category category= categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category" ,"categoryId","Category"));
        List<Product> productList=productRepository.findByCategoryOrderByPriceAsc(category);
        List<ProductDTO> productDTOList= productList.stream()
                .map(item->modelMapper.map(item, ProductDTO.class))
                .toList();

        ProductResponse productResponse=new ProductResponse();
        productResponse.setContent(productDTOList);
        return productResponse;
    }

    @Override
    public ProductResponse searchByKeyword(String keyword) {
        List<Product> productList= productRepository.findByProductNameLikeIgnoreCase('%'+keyword+'%');
        List<ProductDTO> productDTOList= productList.stream()
                .map(item->modelMapper.map(item,ProductDTO.class))
                .toList();

        ProductResponse productResponse=new ProductResponse();
        productResponse.setContent(productDTOList);
        return productResponse;
    }

    @Override
    public ProductDTO updateProduct(ProductDTO productDto , Long productId) {

        Product updatedProduct= productRepository.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("ProductId","ProductId",productId));
        Product product= modelMapper.map(productDto,Product.class);
        updatedProduct.setProductName(product.getProductName());
        updatedProduct.setDescription(product.getDescription());
        updatedProduct.setPrice(product.getPrice());
        updatedProduct.setDiscount(product.getDiscount());
        updatedProduct.setSpecialPrice(product.getSpecialPrice());


        productRepository.save(updatedProduct);

        return modelMapper.map(updatedProduct,ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product product= productRepository.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("ProductId" ,"productID",productId));
        productRepository.delete(product);
        return modelMapper.map(product, ProductDTO.class);
    }

    @Override
    public ProductDTO updateImage(Long productId, MultipartFile image) throws IOException {
        //get the image from the db
        Product product= productRepository.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("ProductId","productId",productId));
        //upload image to the server
        //get the file name of uploaded image
//        String path="images/";
        String fileName=fileService.uplaodImage( path , image);

        //updating the new file name of the product
        product.setImage(fileName);
        productRepository.save(product);

        // return the DTO
        return modelMapper.map(product, ProductDTO.class);
    }


}
