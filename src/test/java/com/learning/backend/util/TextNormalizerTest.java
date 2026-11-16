package com.learning.backend.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TextNormalizerTest {

    @Test
    void normalizeForSearch_handlesVietnameseAccentsAndCase() {
        assertEquals("sua tuoi it duong", TextNormalizer.normalizeForSearch("Sữa tươi ít đường"));
        assertEquals("pho mai que", TextNormalizer.normalizeForSearch("PHÔ MAI QUE"));
        assertEquals("duong trang", TextNormalizer.normalizeForSearch("  ĐƯỜNG   trắng  "));
        assertEquals("kem so-co-la", TextNormalizer.normalizeForSearch("Kem sô-cô-la"));
    }

    @Test
    void normalizeForSearch_handlesNullAndBlank() {
        assertNull(TextNormalizer.normalizeForSearch(null));
        assertEquals("", TextNormalizer.normalizeForSearch("   "));
    }
}
