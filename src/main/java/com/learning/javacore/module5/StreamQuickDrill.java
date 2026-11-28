package com.learning.javacore.module5;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * ÔN NHANH STREAM API (5 BÀI SIÊU NGẮN — DỄ — LÀM TRONG 5-10 PHÚT)
 * Mục tiêu: Nhớ tay 5 phép Stream dùng nhiều nhất khi code Spring Boot.
 *
 * CHẠY NHANH (< 1 giây):
 * java src/main/java/com/learning/javacore/module5/StreamQuickDrill.java
 */
public class StreamQuickDrill {

    record Item(Long id, String sku, String name, int price, int stock) {
    }

    public static void main(String[] args) {
        System.out.println("=== ÔN NHANH STREAM API (5 BÀI) ===");
        List<Item> items = List.of(
                new Item(1L, "SKU-01", "Sữa tươi", 38000, 10),
                new Item(2L, "SKU-02", "Sữa chua", 28000, 0),
                new Item(3L, "SKU-03", "Phô mai", 55000, 5),
                new Item(4L, "SKU-04", "Bơ lạt", 42000, 3));

        List<Item> inStock = bai1_locConHang(items);
        check("Bài 1 (filter) - Lọc còn hàng -> còn 3 món", inStock != null && inStock.size() == 3);

        List<Long> ids = bai2_layDanhSachId(items);
        check("Bài 2 (map) - Rút ra List<Long> ids [1, 2, 3, 4]", List.of(1L, 2L, 3L, 4L).equals(ids));

        Optional<Item> found = bai3_timTheoSku(items, "SKU-03");
        check("Bài 3 (findFirst) - Tìm SKU-03 -> Phô mai",
                found != null && found.isPresent() && "Phô mai".equals(found.get().name()));

        boolean hasOutOfStock = bai4_coMonHetHangKhong(items);
        check("Bài 4 (anyMatch) - Có món hết hàng (stock == 0) -> true", hasOutOfStock);

        List<Item> top2 = bai5_topNReNhat(items, 2);
        check("Bài 5 (sorted + limit) - Top 2 rẻ nhất [SKU-02 (28k), SKU-01 (38k)]",
                top2 != null && top2.size() == 2
                        && "SKU-02".equals(top2.get(0).sku())
                        && "SKU-01".equals(top2.get(1).sku()));
    }

    /**
     * BÀI 1 (`filter` — giống `arr.filter(...)` của JS):
     * - Ngữ cảnh: Chỉ giữ lại các sản phẩm còn hàng để hiển thị lên trang chủ.
     * - Yêu cầu: Lọc `items` giữ lại những `Item x` có `x.stock() > 0`.
     * - Ví dụ: 4 món (có 1 món stock=0) -> trả về 3 món.
     * - Công cụ: `items.stream().filter(x -> ...).toList()`
     */
    public static List<Item> bai1_locConHang(List<Item> items) {
        if (items == null) {
            return List.of();

        }
        return items.stream().filter(i -> i != null && i.stock() > 0).toList();
    }

    /**
     * BÀI 2 (`map` — giống `arr.map(...)` của JS):
     * - Ngữ cảnh: Rút `List<Long> ids` từ danh sách item để query DB
     * `findAllById(ids)` 1 lần.
     * - Yêu cầu: Chuyển từng `Item` thành `id` (`Item::id`).
     * - Ví dụ: `[Item(1L...), Item(2L...)]` -> `[1L, 2L]`.
     * - Công cụ: `items.stream().map(Item::id).toList()`
     */
    public static List<Long> bai2_layDanhSachId(List<Item> items) {
        if (items == null) {
            return List.of();
        }
        return items.stream().map(Item::id).toList();
    }

    /**
     * BÀI 3 (`filter` + `findFirst` — giống `arr.find(...)` của JS):
     * - Ngữ cảnh: Tìm 1 sản phẩm đầu tiên khớp `sku` trong list.
     * - Yêu cầu: Lọc `x.sku().equalsIgnoreCase(sku)` rồi gọi `.findFirst()`.
     * - Ví dụ: `bai3_timTheoSku(items, "SKU-03")` -> `Optional.of(Item(3L,
     * "SKU-03", ...))`.
     * - Công cụ: `items.stream().filter(x -> ...).findFirst()`
     */
    public static Optional<Item> bai3_timTheoSku(List<Item> items, String sku) {
        if (items == null || sku == null) {
            return Optional.empty();
        }
        return items.stream().filter(i -> i != null && !i.sku().isBlank() && i.sku().equalsIgnoreCase(sku)).findFirst();
    }

    /**
     * BÀI 4 (`anyMatch` — giống `arr.some(...)` của JS):
     * - Ngữ cảnh: Kiểm tra nhanh trong giỏ hàng có món nào hết hàng (`stock == 0`)
     * không.
     * - Yêu cầu: Trả về `true` nếu có ít nhất 1 `Item` có `x.stock() == 0`.
     * - Ví dụ: `items` có `SKU-02 (stock=0)` -> `true`.
     * - Công cụ: `items.stream().anyMatch(x -> ...)`
     */
    public static boolean bai4_coMonHetHangKhong(List<Item> items) {
        if (items == null) {
            return false;
        }
        return items.stream().anyMatch(i -> i.stock == 0);
    }

    /**
     * BÀI 5 (`sorted` + `limit` — giống `[...arr].sort(...).slice(0, n)` của JS):
     * - Ngữ cảnh: Lấy Top `n` sản phẩm giá rẻ nhất.
     * - Yêu cầu: Sắp xếp `items` tăng dần theo `Item::price` và lấy tối đa `n` món.
     * - Ví dụ: `n = 2` -> `[SKU-02 (28000), SKU-01 (38000)]`.
     * - Công cụ:
     * `items.stream().sorted(Comparator.comparing(Item::price)).limit(n).toList()`
     */
    public static List<Item> bai5_topNReNhat(List<Item> items, int n) {
        if (items == null || n <= 0) {
            return List.of();
        }
        return items.stream().sorted(Comparator.comparing(Item::price)).limit(n).toList();
    }

    private static void check(String tenBai, boolean passed) {
        System.out.println((passed ? "✅ PASS: " : "❌ FAIL: ") + tenBai);
    }
}
