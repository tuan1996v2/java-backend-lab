package com.learning.backend.service;

import com.learning.backend.dto.ProductRequest;
import com.learning.backend.dto.ProductResponse;

import java.util.List;

/**
 * Service Interface: Định nghĩa hợp đồng nghiệp vụ cho Product.
 * Tách Interface và Implementation là Best Practice trong kiến trúc Spring.
 */
public interface ProductService {

    List<ProductResponse> getAllProducts(String keyword, String category);

    ProductResponse getProductById(Long id);

    ProductResponse createProduct(ProductRequest request);

    ProductResponse updateProduct(Long id, ProductRequest request);

    void deleteProduct(Long id);
}

