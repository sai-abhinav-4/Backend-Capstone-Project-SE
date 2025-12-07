package com.example.productcatalogservice.services;

import com.example.productcatalogservice.models.Product;

import java.util.List;

public interface IProductService {

    Product getProductById(Long id);

    List<Product> getAllProducts();

    Product createProduct(Product product);

    Product replaceProduct(Long id,Product product);
}
