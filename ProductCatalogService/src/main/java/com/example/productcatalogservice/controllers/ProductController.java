package com.example.productcatalogservice.controllers;

import com.example.productcatalogservice.dtos.CategoryDto;
import com.example.productcatalogservice.dtos.ProductDto;
import com.example.productcatalogservice.exceptions.ProductNotFoundException;
import com.example.productcatalogservice.models.Category;
import com.example.productcatalogservice.models.Product;
import com.example.productcatalogservice.services.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ProductController {

    @Autowired
//    @Qualifier("storageProductService")
    private IProductService productService;

    @GetMapping("/products")
    public List<ProductDto> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        List<ProductDto> productDtos = new ArrayList<>();
        for (Product product : products) {
            productDtos.add(from(product));
        }
        return productDtos;
    }

//    @GetMapping("/products/{id}")
//    public ResponseEntity<ProductDto> getProductById( @PathVariable("id") long productId) {
//        try {
//            if(productId <=0){
//                throw new IllegalArgumentException("please pass id > 0");
//            }
//            Product product = productService.getProductById(productId);
//            if (product != null) {
//                ProductDto response = from(product);
//                return new ResponseEntity<>(response, HttpStatus.OK);
//            }
//            throw new NullPointerException("product is null");
//        }catch (Exception e){
//            return new ResponseEntity<>((ProductDto) null, HttpStatus.BAD_REQUEST);
//        }
//    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDto> getProductById( @PathVariable("id") long productId) {
            if(productId <=0){
                throw new IllegalArgumentException("please pass id > 0");
            }
            Product product = productService.getProductById(productId);
            if (product != null) {
                ProductDto response = from(product);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            throw new ProductNotFoundException("product with id >20 not found");
    }

    @PostMapping("/products")
    public ProductDto createProduct( @RequestBody ProductDto productDto) {
        Product product = from(productDto);
        Product response = productService.createProduct(product);
        return from(response);
    }

    @PutMapping("/products/{id}")
    public ProductDto replaceProduct( @PathVariable("id") long productId, @RequestBody ProductDto productDto) {
        Product product = from(productDto);
        Product response = productService.replaceProduct(productId, product);
        if(response == null){
            throw new ProductNotFoundException("product with id " + productId + " not found");
        }
        return from(response);
    }

    @DeleteMapping("/products/{id}")
    public void deleteProduct( @PathVariable("id") long productId) {
        productService.deleteProduct(productId);
    }

    private ProductDto from (Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice());
        productDto.setImageUrl(product.getImageUrl());
        if(product.getCategory() != null) {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setName(product.getCategory().getName());
            categoryDto.setId(product.getCategory().getId());
            categoryDto.setDescription(product.getCategory().getDescription());
            productDto.setCategory(categoryDto);
        }
        return productDto;
    }
    private Product from(ProductDto productDto) {
        Product product = new Product();
        product.setId(productDto.getId());
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setImageUrl(productDto.getImageUrl());
        product.setDescription(productDto.getDescription());
        if(productDto.getCategory() != null) {
            Category category = new Category();
            category.setId(productDto.getCategory().getId());
            category.setName(productDto.getCategory().getName());
            product.setCategory(category);
        }
        return product;
    }

}
