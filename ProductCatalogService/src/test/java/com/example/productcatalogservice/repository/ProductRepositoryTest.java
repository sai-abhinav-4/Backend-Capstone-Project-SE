package com.example.productcatalogservice.repository;

import com.example.productcatalogservice.models.Product;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @Transactional
    public void testJpaMethods(){
        List<Product> products = productRepository.findProductByPriceBetween(60000d,100000d);
        System.out.println(products.size());
    }

}
