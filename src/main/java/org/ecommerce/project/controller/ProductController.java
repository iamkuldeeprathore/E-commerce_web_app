package org.ecommerce.project.controller;


import org.ecommerce.project.model.Product;
import org.ecommerce.project.payload.ProductDTO;
import org.ecommerce.project.payload.ProductResponse;
import org.ecommerce.project.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductController {
    @Autowired
    ProductService productService;

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@RequestBody  ProductDTO productDto,
                                                 @PathVariable  Long categoryId){
       ProductDTO productDTO= productService.addProduct(categoryId,productDto);
       return new ResponseEntity<>(productDTO, HttpStatus.CREATED);
    }

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts(){
        ProductResponse productResponse=productService.getAllProducts();
        return new ResponseEntity<>(productResponse,HttpStatus.OK);
    }

    @GetMapping("/public/{categoryId}/products")
    public ResponseEntity<ProductResponse> getAllProductsByCategory(@PathVariable Long categoryId){
        ProductResponse productResponse= productService.searchProductByCategory(categoryId);
        return new ResponseEntity<>(productResponse,HttpStatus.OK);
    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> searchByKeyword(@PathVariable String keyword){
        ProductResponse productResponse= productService.searchByKeyword(keyword);
        return new ResponseEntity<>(productResponse,HttpStatus.OK);
    }

    @PutMapping("/admin/categories/{productId}/product")
    public ResponseEntity<ProductDTO> updateProduct(@RequestBody ProductDTO productDto ,
                                                    @PathVariable Long productId){
        ProductDTO productDTO= productService.updateProduct(productDto ,productId);
        return new ResponseEntity<>(productDTO,HttpStatus.OK);
    }

    @DeleteMapping("/admin/categories/{productId}/product")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId){
        ProductDTO deletedProduct= productService.deleteProduct(productId);

        return new ResponseEntity<>(deletedProduct,HttpStatus.OK);
    }

    @PutMapping("/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateImage(@PathVariable Long productId,
                                                  @RequestParam("image") MultipartFile image) throws IOException {
        ProductDTO updatedImage= productService.updateImage(productId,image);
        return  new ResponseEntity<>(updatedImage, HttpStatus.OK);
    }
}
