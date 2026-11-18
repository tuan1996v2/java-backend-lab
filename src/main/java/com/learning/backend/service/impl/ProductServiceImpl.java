package com.learning.backend.service.impl;

import com.learning.backend.dto.ProductRequest;
import com.learning.backend.dto.ProductResponse;
import com.learning.backend.entity.Product;
import com.learning.backend.exception.ResourceNotFoundException;
import com.learning.backend.repository.ProductRepository;
import com.learning.backend.service.ProductService;
import com.learning.backend.util.TextNormalizer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation: Triển khai toàn bộ logic nghiệp vụ (Business Logic).
 * Sử dụng Constructor Injection (khuyến nghị thay thế @Autowired trên field).
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts(String keyword, String category) {
        List<Product> products;

        boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();
        boolean hasCategory = category != null && !category.trim().isEmpty();

        if (hasKeyword && hasCategory) {
            String normalizedKeyword = TextNormalizer.normalizeForSearch(keyword);
            String normalizedCategory = TextNormalizer.normalizeForSearch(category);
            products = productRepository.findByNameNormalizedContainingAndCategoryNormalizedContaining(
                    normalizedKeyword, normalizedCategory);
        } else if (hasKeyword) {
            String normalizedKeyword = TextNormalizer.normalizeForSearch(keyword);
            products = productRepository.findByNameNormalizedContaining(normalizedKeyword);
        } else if (hasCategory) {
            String normalizedCategory = TextNormalizer.normalizeForSearch(category);
            products = productRepository.findByCategoryNormalizedContaining(normalizedCategory);
        } else {
            products = productRepository.findAll();
        }

        return products.stream()
                .map(ProductResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm với ID: " + id));
        return ProductResponse.fromEntity(product);
    }

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        // Kiểm tra mã SKU đã tồn tại chưa
        if (productRepository.existsBySku(request.getSku().trim())) {
            throw new IllegalArgumentException("Mã SKU '" + request.getSku() + "' đã tồn tại!");
        }

        // Kiểm tra discount
        if (request.getDiscountPercent() != null) {
            if (request.getDiscountPercent() < 0 || request.getDiscountPercent() > 100) {
                throw new IllegalArgumentException("Phần trăm giảm giá phải từ 0 đến 100!");
            }
        } else {
            request.setDiscountPercent(0);
        }

        Product product = Product.builder()
                .sku(request.getSku().trim())
                .name(request.getName().trim())
                .price(request.getPrice())
                .stockQuantity(request.getStockQuantity())
                .category(request.getCategory() != null ? request.getCategory().trim() : null)
                .description(request.getDescription() != null ? request.getDescription().trim() : null)
                .discountPercent(request.getDiscountPercent())
                .build();

        Product saved = productRepository.save(product);
        log.info("Đã tạo sản phẩm mới: {}", saved);
        return ProductResponse.fromEntity(saved);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm với ID: " + id));

        // Nếu đổi SKU thì phải kiểm tra xem SKU mới có trùng với sản phẩm khác không
        if (!product.getSku().equalsIgnoreCase(request.getSku().trim()) &&
                productRepository.existsBySku(request.getSku().trim())) {
            throw new IllegalArgumentException("Mã SKU '" + request.getSku() + "' đã được sử dụng bởi sản phẩm khác!");
        }

        if (request.getDiscountPercent() != null) {
            if (request.getDiscountPercent() < 0 || request.getDiscountPercent() > 100) {
                throw new IllegalArgumentException("Phần trăm giảm giá phải từ 0 đến 100!");
            }
        } else {
            request.setDiscountPercent(0);
        }
        product.setSku(request.getSku().trim());
        product.setName(request.getName().trim());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setCategory(request.getCategory() != null ? request.getCategory().trim() : null);
        product.setDescription(request.getDescription());
        product.setDiscountPercent(request.getDiscountPercent());

        Product updated = productRepository.save(product);
        return ProductResponse.fromEntity(updated);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm với ID: " + id));
        productRepository.delete(product);
    }
}
