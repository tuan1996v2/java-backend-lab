package com.learning.backend.common.util;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Tiện ích bỏ dấu tiếng Việt để tìm kiếm món ăn nhanh không phân biệt dấu và hoa/thường.
 * Ví dụ: "Phở Bò Tái Nạm" -> "pho bo tai nam"
 */
public final class TextNormalizer {

    private static final Pattern COMBINING_MARKS = Pattern.compile("\\p{M}+");

    private TextNormalizer() {
    }

    public static String normalizeForSearch(String input) {
        if (input == null) {
            return null;
        }
        String decomposed = Normalizer.normalize(input.trim(), Normalizer.Form.NFD);
        String withoutAccents = COMBINING_MARKS.matcher(decomposed).replaceAll("");
        return withoutAccents
                .replace('đ', 'd')
                .replace('Đ', 'D')
                .toLowerCase(Locale.ROOT)
                .replaceAll("\\s+", " ");
    }
}
