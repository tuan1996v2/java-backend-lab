package com.learning.backend.util;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Tiện ích chuẩn hóa chuỗi phục vụ tìm kiếm tiếng Việt không phân biệt dấu và hoa/thường.
 */
public final class TextNormalizer {

    private static final Pattern COMBINING_MARKS = Pattern.compile("\\p{M}+");

    private TextNormalizer() {
    }

    /**
     * Chuyển chuỗi về chữ thường, bỏ dấu tiếng Việt (kể cả đ/Đ) và gộp khoảng trắng thừa.
     * Ví dụ: "Sữa tươi ít đường" -> "sua tuoi it duong"
     */
    public static String normalizeForSearch(String input) {
        if (input == null) {
            return null;
        }

        // Tách ký tự gốc và dấu (NFD) rồi xóa toàn bộ dấu thanh/mũ
        String decomposed = Normalizer.normalize(input.trim(), Normalizer.Form.NFD);
        String withoutAccents = COMBINING_MARKS.matcher(decomposed).replaceAll("");

        // Chữ đ/Đ không thuộc nhóm dấu tổ hợp NFD nên cần thay thế riêng
        return withoutAccents
                .replace('đ', 'd')
                .replace('Đ', 'D')
                .toLowerCase(Locale.ROOT)
                .replaceAll("\\s+", " ");
    }
}
