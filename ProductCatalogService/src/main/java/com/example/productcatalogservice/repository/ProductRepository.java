package com.example.productcatalogservice.repository;

import com.example.productcatalogservice.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    Optional<Product> findById(Long id);

    List<Product> findAll();

    Product save(Product product);

    void deleteById(Long id);

    List<Product> findProductByPriceBetween(Double priceFrom, Double priceTo);

    List<Product> findAllByOrderByPrice();

    @Query("SELECT p.name FROM products p WHERE p.price = :price")
    String getNameOfProductWhosePriceIs(double price);

    Page<Product> findByName(String name, Pageable pageable);

}
