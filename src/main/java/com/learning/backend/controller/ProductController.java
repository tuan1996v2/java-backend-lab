package com.learning.backend.controller;

import com.learning.backend.dto.ApiResponse;
import com.learning.backend.dto.ProductRequest;
import com.learning.backend.dto.ProductResponse;
import com.learning.backend.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller: Điểm tiếp nhận các HTTP Request từ Client (Web/Mobile/Postman).
 * - @RestController = @Controller + @ResponseBody (tự động chuyển data Java
 * sang JSON).
 * - @RequestMapping("/api/v1/products"): Định nghĩa prefix endpoint.
 */
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "Product API", description = "Các endpoint quản lý sản phẩm (CRUD, tìm kiếm, lọc)")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @Operation(summary = "Lấy danh sách sản phẩm (có hỗ trợ tìm kiếm theo tên và lọc theo danh mục)")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category) {
        List<ProductResponse> products = productService.getAllProducts(keyword, category);
        return ResponseEntity.ok(ApiResponse.ok("Lấy danh sách thành công", products));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy chi tiết một sản phẩm theo ID")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable Long id) {
        ProductResponse product = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponse.ok(product));
    }

    @PostMapping
    @Operation(summary = "Tạo mới một sản phẩm (yêu cầu validate dữ liệu đầu vào)")
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(@Valid @RequestBody ProductRequest request) {
        ProductResponse created = productService.createProduct(request);
        return new ResponseEntity<>(ApiResponse.ok("Tạo sản phẩm mới thành công", created), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật thông tin sản phẩm theo ID")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {
        ProductResponse updated = productService.updateProduct(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Cập nhật sản phẩm thành công", updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa một sản phẩm theo ID")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.ok("Đã xóa sản phẩm thành công", null));
    }
}
