package com.example.productcatalogservice.controllers;

import com.example.productcatalogservice.dtos.ProductDto;
import com.example.productcatalogservice.models.Product;
import com.example.productcatalogservice.services.IProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ProductControllerTest {

    @Autowired
    private ProductController productController;

    @MockBean
    private IProductService productService;

    @Test
    public void TestGetProductById_WithValidId_ReturnProductSuccessfully(){
        //Arrange

        Long id =4L;
        Product mockProduct = new Product();
        mockProduct.setId(id);
        mockProduct.setName("Test Product");
        mockProduct.setPrice(44.44);

        when(productService.getProductById(id)).thenReturn(mockProduct);

        //Act
        ResponseEntity<ProductDto> productDtoResponseEntity = productController.getProductById(id);

        //Assert

        assertNotNull(productDtoResponseEntity);
        assertNotNull(productDtoResponseEntity.getBody());
        assertEquals(id, productDtoResponseEntity.getBody().getId());
        assertEquals("Test Product", productDtoResponseEntity.getBody().getName());
        assertEquals(44.44, productDtoResponseEntity.getBody().getPrice());
        assertEquals(HttpStatus.OK, productDtoResponseEntity.getStatusCode());
        assertNull(productDtoResponseEntity.getBody().getCategory());
        verify(productService,times(1)).getProductById(id);
    }

    @Test
    public void TestGetProductById_WithInvalidId_ReturnIllegalArgumentException(){
        Long id =-11L;
        Exception exception = assertThrows(IllegalArgumentException.class, () -> productController.getProductById(id));
        assertEquals("please pass id > 0", exception.getMessage());
    }


    @Test
    public void TestGetProductById_WhereProductServiceThrowsRuntimeException_ResultInRuntimeException(){
        Long id =1L;
        when(productService.getProductById(id)).thenThrow(new RuntimeException("Product not found"));
        assertThrows(RuntimeException.class, () -> productController.getProductById(id));
    }



}