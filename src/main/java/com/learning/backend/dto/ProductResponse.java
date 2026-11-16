package com.learning.backend.dto;

import com.learning.backend.entity.Product;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO trả dữ liệu về cho Client.
 * Giúp ẩn các thông tin nhạy cảm của Entity (nếu có) hoặc format dữ liệu phù hợp trước khi xuất ra JSON.
 */
public class ProductResponse {

    private Long id;
    private String sku;
    private String name;
    private BigDecimal price;
    private Integer stockQuantity;
    private String category;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ProductResponse() {
    }

    public static ProductResponse fromEntity(Product entity) {
        ProductResponse res = new ProductResponse();
        res.setId(entity.getId());
        res.setSku(entity.getSku());
        res.setName(entity.getName());
        res.setPrice(entity.getPrice());
        res.setStockQuantity(entity.getStockQuantity());
        res.setCategory(entity.getCategory());
        res.setDescription(entity.getDescription());
        res.setCreatedAt(entity.getCreatedAt());
        res.setUpdatedAt(entity.getUpdatedAt());
        return res;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

