package com.example.productcatalogservice.services;

import com.example.productcatalogservice.dtos.FakeStoreProductDto;
import com.example.productcatalogservice.models.Category;
import com.example.productcatalogservice.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class FakeStoreProductService implements IProductService{

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Product getProductById(Long id) {
//      ResponseEntity<FakeStoreProductDto>  fakeStoreProductDtoResponseEntity =  restTemplate.getForEntity("https://fakestoreapi.com/products/{id}", FakeStoreProductDto.class, id);

        ResponseEntity<FakeStoreProductDto>  fakeStoreProductDtoResponseEntity =
                requestForEntity("https://fakestoreapi.com/products/{id}", null, HttpMethod.GET, FakeStoreProductDto.class, id);

        if(fakeStoreProductDtoResponseEntity.getStatusCode().equals(HttpStatusCode.valueOf(200)) && fakeStoreProductDtoResponseEntity.getBody() != null){
          return from(fakeStoreProductDtoResponseEntity.getBody());
      }

      return null;
    }

    @Override
    public List<Product> getAllProducts() {
        ResponseEntity<FakeStoreProductDto[]> response =
                requestForEntity(
                        "https://fakestoreapi.com/products",
                        null,
                        HttpMethod.GET,
                        FakeStoreProductDto[].class
                );

        return List.of(response.getBody())
                .stream()
                .map(this::from)
                .toList();
    }

    @Override
    public Product createProduct(Product product) {
//       FakeStoreProductDto fakeStoreProductDto = restTemplate.postForObject("https://fakestoreapi.com/products", from(product), FakeStoreProductDto.class);
        ResponseEntity<FakeStoreProductDto> fakeStoreProductDtoResponseEntity = requestForEntity("https://fakestoreapi.com/products", from(product), HttpMethod.POST, FakeStoreProductDto.class);
        return from(fakeStoreProductDtoResponseEntity.getBody());
    }

    @Override
    public Product replaceProduct(Long id, Product product) {
        FakeStoreProductDto fakeStoreProductDto = from(product);
        ResponseEntity<FakeStoreProductDto> responseEntity = requestForEntity("https://fakestoreapi.com/products/{id}", fakeStoreProductDto, HttpMethod.PUT ,FakeStoreProductDto.class, id);
        return from(responseEntity.getBody());
    }

    @Override
    public void deleteProduct(Long id) {
        //TODO
    }

    public <T> ResponseEntity<T> requestForEntity(String url, @Nullable Object request, HttpMethod httpMethod, Class<T> responseType, Object... uriVariables) throws RestClientException {
        RequestCallback requestCallback = restTemplate.httpEntityCallback(request, responseType);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = restTemplate.responseEntityExtractor(responseType);
        return restTemplate.execute(url, httpMethod, requestCallback, responseExtractor, uriVariables);
    }

    private Product from(FakeStoreProductDto fakeStoreProductDto) {
        Product product = new Product();
        product.setId(fakeStoreProductDto.getId());
        product.setName(fakeStoreProductDto.getTitle());
        product.setDescription(fakeStoreProductDto.getDescription());
        product.setPrice(fakeStoreProductDto.getPrice());
        product.setImageUrl(fakeStoreProductDto.getImage());
        Category category = new Category();
        category.setName(fakeStoreProductDto.getCategory());
        product.setCategory(category);
        return product;
    }

    private FakeStoreProductDto from(Product product) {
        FakeStoreProductDto fakeStoreProductDto = new FakeStoreProductDto();
        fakeStoreProductDto.setId(product.getId());
        fakeStoreProductDto.setTitle(product.getName());
        fakeStoreProductDto.setPrice(product.getPrice());
        fakeStoreProductDto.setDescription(product.getDescription());
        fakeStoreProductDto.setImage(product.getImageUrl());
        if(product.getCategory() != null) {
            fakeStoreProductDto.setCategory(product.getCategory().getName());
        }
        return fakeStoreProductDto;
    }
}
