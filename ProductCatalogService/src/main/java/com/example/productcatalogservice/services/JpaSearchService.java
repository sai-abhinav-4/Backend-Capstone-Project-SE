package com.example.productcatalogservice.services;

import com.example.productcatalogservice.models.Product;
import com.example.productcatalogservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.List;

@Service
public class JpaSearchService implements  ISearchService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Page<Product> searchProducts(String query, Integer pageSize, Integer pageNumber) {
        Sort sort = Sort.by("price").and(Sort.by("id").descending());
        return productRepository.findByName(query, PageRequest.of(pageNumber, pageSize, sort));
    }
}
