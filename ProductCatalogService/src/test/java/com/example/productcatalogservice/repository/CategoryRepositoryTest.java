package com.example.productcatalogservice.repository;

import com.example.productcatalogservice.models.Category;
import com.example.productcatalogservice.models.Product;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @Transactional
    public void testFetchTypes(){
        Category category = categoryRepository.findById(10L).get();
        for(Product product:category.getProducts()){
            System.out.println(product.getName());
        }
    }

    @Test
    @Transactional
    public void testNPlusOne(){
        List<Category> categories = categoryRepository.findAll();
        for(Category category:categories){
            System.out.println(category.getName());
            for(Product product:category.getProducts()){
                System.out.println(product.getName());
            }
        }
    }
}