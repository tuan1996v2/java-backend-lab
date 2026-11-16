package com.learning.backend.service;

import com.learning.backend.dto.ProductRequest;
import com.learning.backend.dto.ProductResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class ProductSearchTest {

    @Autowired
    private ProductService productService;

    @Test
    void searchByKeyword_matchesWithoutVietnameseAccentsAndCase() {
        List<ProductResponse> byNoAccentLower = productService.getAllProducts("sua", null);
        List<ProductResponse> byAccentUpper = productService.getAllProducts("SỮA", null);

        assertEquals(3, byNoAccentLower.size());
        assertEquals(3, byAccentUpper.size());
    }

    @Test
    void searchByKeyword_matchesVietnameseDStrokeAndPartialPhrase() {
        List<ProductResponse> results = productService.getAllProducts("IT DUONG", null);

        assertEquals(1, results.size());
        assertEquals("TH-MILK-001", results.get(0).getSku());
    }

    @Test
    void searchByKeyword_updatesNormalizedColumnOnCreateAndUpdate() {
        ProductResponse created = productService.createProduct(new ProductRequest(
                "TEST-001",
                "Đậu nành hữu cơ",
                new BigDecimal("25000"),
                10,
                "Đồ uống",
                "Mô tả"
        ));

        List<ProductResponse> afterCreate = productService.getAllProducts("dau nanh", null);
        assertTrue(afterCreate.stream().anyMatch(p -> p.getId().equals(created.getId())));

        productService.updateProduct(created.getId(), new ProductRequest(
                "TEST-001",
                "Trà xanh mật ong",
                new BigDecimal("25000"),
                10,
                "Đồ uống",
                "Mô tả"
        ));

        assertTrue(productService.getAllProducts("dau nanh", null).isEmpty());
        List<ProductResponse> afterUpdate = productService.getAllProducts("mat ong", null);
        assertEquals(1, afterUpdate.size());
        assertEquals(created.getId(), afterUpdate.get(0).getId());
    }
}
