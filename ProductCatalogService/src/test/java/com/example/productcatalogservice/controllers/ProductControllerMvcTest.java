package com.example.productcatalogservice.controllers;


import com.example.productcatalogservice.dtos.ProductDto;
import com.example.productcatalogservice.models.Product;
import com.example.productcatalogservice.services.IProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void TestGetAllProducts_RunSuccessfully() throws Exception {

        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Product 1");

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Product 2");

        List<Product> products = List.of(product1, product2);
        when(productService.getAllProducts()).thenReturn(products);

        ProductDto productDto1 = new ProductDto();
        productDto1.setId(1L);
        productDto1.setName("Product 1");

        ProductDto productDto2 = new ProductDto();
        productDto2.setId(2L);
        productDto2.setName("Product 2");

        List<ProductDto> productDtos = List.of(productDto1, productDto2);


        String expectedString = objectMapper.writeValueAsString(productDtos);

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedString))
                .andExpect(jsonPath("$[1].name").value("Product 2"));



    }


    @Test
    public void Test_CreateProduct_WithValidBody_RunSuccessfully() throws Exception {
        ProductDto productDto = new ProductDto();
        productDto.setName("New Product");
        productDto.setId(2L);
        productDto.setPrice(444.44);

        Product product = new Product();
        product.setName("New Product");
        product.setId(2L);
        product.setPrice(444.44);

        when(productService.createProduct(any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(productDto)))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("New Product"));



    }

    @Test
    public void getProductById_whenFound_returnsProductDto() throws Exception {
        Product p = new Product(); p.setId(5L); p.setName("Found Product");
        when(productService.getProductById(5L)).thenReturn(p);

        ProductDto expectedDto = new ProductDto(); expectedDto.setId(5L); expectedDto.setName("Found Product");

        mockMvc.perform(get("/products/5"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(expectedDto)))
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.name").value("Found Product"));
    }


    @Test
    public void replaceProduct_whenExists_returnsReplacedDto() throws Exception {
        ProductDto requestDto = new ProductDto();
        requestDto.setId(3L);
        requestDto.setName("Replaced Product");
        requestDto.setPrice(99.99);

        Product returned = new Product();
        returned.setId(3L);
        returned.setName("Replaced Product");
        returned.setPrice(99.99);

        when(productService.replaceProduct(3L, any(Product.class))).thenReturn(returned);

        mockMvc.perform(put("/products/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(requestDto)))
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("Replaced Product"));
    }

    @Test
    public void deleteProduct_invokesService() throws Exception {
        mockMvc.perform(delete("/products/7"))
                .andExpect(status().isOk());

        verify(productService).deleteProduct(7L);
    }
}
