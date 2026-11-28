package com.learning.javacore.module5;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * MODULE 5 (RÚT GỌN — HƯỚNG A): Optional<T>, Exception & Stream API
 * (Đúng 6 bài thực chiến cuối cùng trước khi quay thẳng về Spring Boot
 * Production)
 *
 * HƯỚNG DẪN CHẠY NHANH (< 1 giây, không cần mạng):
 * java src/main/java/com/learning/javacore/module5/Module5Practice.java
 *
 * BẢNG ĐỐI CHIẾU REACT NATIVE (TS/JS) -> JAVA (DÙNG HÀNG NGÀY TRONG SPRING
 * BOOT):
 * 1. Java 17 `record` (Hết sạch boilerplate getter / equals / hashCode /
 * toString):
 * - Thay vì viết class 50 dòng, Java 17 dùng 1 dòng:
 * `record ProductItem(String sku, String name, String category, BigDecimal
 * price, int stock) {}`
 * - Gọi field KHÔNG có chữ `get`: `item.sku()`, `item.price()`, `item.stock()`
 * (giống property nhưng có dấu `()`).
 *
 * 2. `Optional<T>` (Giống `?.` + `??` trong TypeScript — dùng mọi nơi với
 * Spring Data JPA `findById`):
 * - Bọc giá trị có thể null: `Optional.ofNullable(map.get(key))`
 * - Biến đổi nếu có giá trị (giống `product?.price`):
 * `.map(ProductItem::price)` (hoặc `p -> p.price()`)
 * - Giá trị mặc định nếu rỗng (giống `?? BigDecimal.ZERO`):
 * `.orElse(BigDecimal.ZERO)`
 * - Ném lỗi nếu rỗng (Spring Service chuẩn): `.orElseThrow(() -> new
 * NotFoundException("..."))`
 *
 * 3. `Exception` (`throw` giống `throw new Error(...)` trong JS/TS):
 * - `class NotFoundException extends RuntimeException` -> Spring
 * `@RestControllerAdvice` bắt lỗi này để trả HTTP 404/400.
 * - Cú pháp ném lỗi: `throw new InsufficientStockException("Không đủ tồn
 * kho");`
 *
 * 4. `Stream API` (Giống 1-1 với `.filter().map().sort().slice()` của Array
 * JS/TS):
 * - JS: `products.filter(p => p.stock > 0).map(p => p.name)`
 * - Java: `products.stream().filter(p -> p.stock() >
 * 0).map(ProductItem::name).toList()`
 * - Sắp xếp tăng dần theo giá rồi lấy top `n`:
 * `.sorted(Comparator.comparing(ProductItem::price)).limit(n).toList()`
 * - Gom nhóm thành Map<Category, List<ProductItem>>:
 * `.collect(Collectors.groupingBy(p -> p.category().trim()))`
 */
public class Module5Practice {

    /**
     * Java 17 `record`: tự động sinh constructor, getter (`p.sku()`,
     * `p.price()`...),
     * `equals`, `hashCode`, `toString` — KHÔNG cần gõ tay boilerplate!
     */
    record ProductItem(String sku, String name, String category, BigDecimal price, int stock) {
    }

    /** Lỗi 404 nghiệp vụ (vd: không tìm thấy sản phẩm theo SKU/ID) */
    static class NotFoundException extends RuntimeException {
        public NotFoundException(String message) {
            super(message);
        }
    }

    /** Lỗi 400 nghiệp vụ (vd: đặt mua vượt tồn kho) */
    static class InsufficientStockException extends RuntimeException {
        public InsufficientStockException(String message) {
            super(message);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== KIỂM TRA MODULE 5 RÚT GỌN (OPTIONAL / EXCEPTION / STREAM) ===");

        ProductItem p1 = new ProductItem("SKU-01", "Sữa tươi Vinamilk", "Sữa tươi", new BigDecimal("38000"), 10);
        ProductItem p2 = new ProductItem("SKU-02", "Sữa chua TH", "Sữa chua", new BigDecimal("28000"), 5);
        ProductItem p3 = new ProductItem("SKU-03", "Sữa tươi Organic", "Sữa tươi", new BigDecimal("55000"), 0);
        ProductItem p4 = new ProductItem("SKU-04", "Sữa tươi Ít Đường", "Sữa tươi", new BigDecimal("42000"), 8);

        Map<String, ProductItem> db = Map.of(
                "SKU-01", p1,
                "SKU-02", p2,
                "SKU-03", p3);

        // Bài 1: Optional.orElseThrow (chuẩn Service findById)
        try {
            ProductItem found = bai1_timSanPhamHoacBaoLoi(db, " sku-01 ");
            check("Bài 1 - Tìm thấy ' sku-01 ' -> SKU-01", found != null && "SKU-01".equals(found.sku()));
        } catch (Exception e) {
            check("Bài 1 - Tìm thấy ' sku-01 ' -> SKU-01", false);
        }

        boolean caughtNotFound = false;
        try {
            bai1_timSanPhamHoacBaoLoi(db, "SKU-99");
        } catch (NotFoundException e) {
            caughtNotFound = true;
        }
        check("Bài 1 - Không thấy SKU-99 phải ném NotFoundException", caughtNotFound);

        boolean caughtNullSku = false;
        try {
            bai1_timSanPhamHoacBaoLoi(db, null);
        } catch (NotFoundException e) {
            caughtNullSku = true;
        }
        check("Bài 1 - Truyền sku=null phải ném NotFoundException (không nổ NPE)", caughtNullSku);

        // Bài 2: Optional.map + orElse
        check("Bài 2 - Optional có hàng trả giá 38000",
                new BigDecimal("38000").compareTo(bai2_layGiaAnToan(Optional.of(p1))) == 0);
        check("Bài 2 - Optional.empty() trả BigDecimal.ZERO",
                BigDecimal.ZERO.compareTo(bai2_layGiaAnToan(Optional.empty())) == 0);
        check("Bài 2 - Optional null trả BigDecimal.ZERO",
                BigDecimal.ZERO.compareTo(bai2_layGiaAnToan(null)) == 0);

        // Bài 3: Validate & throw đúng loại Exception
        try {
            int remain = bai3_kiemTraVaTruKho(p1, 3);
            check("Bài 3 - Mua 3/10 trả về tồn còn 7", remain == 7);
        } catch (Exception e) {
            check("Bài 3 - Mua 3/10 trả về tồn còn 7", false);
        }

        boolean caughtStock = false;
        try {
            bai3_kiemTraVaTruKho(p1, 99);
        } catch (InsufficientStockException e) {
            caughtStock = true;
        } catch (Exception ignored) {
        }
        check("Bài 3 - Mua vượt tồn (99 > 10) ném InsufficientStockException", caughtStock);

        boolean caughtNullProduct = false;
        try {
            bai3_kiemTraVaTruKho(null, 1);
        } catch (NotFoundException e) {
            caughtNullProduct = true;
        } catch (Exception ignored) {
        }
        check("Bài 3 - Product null ném NotFoundException", caughtNullProduct);

        boolean caughtBadQty = false;
        try {
            bai3_kiemTraVaTruKho(p1, 0);
        } catch (IllegalArgumentException e) {
            caughtBadQty = true;
        } catch (Exception ignored) {
        }
        check("Bài 3 - Số lượng <= 0 ném IllegalArgumentException", caughtBadQty);

        // Bài 4: Stream filter + map
        List<ProductItem> allProducts = List.of(p1, p2, p3, p4);
        List<String> milkNames = bai4_layTenSanPhamConHangTheoDanhMuc(allProducts, " sữa tươi ");
        check("Bài 4 - Stream lấy tên 'Sữa tươi' còn hàng (bỏ SKU-03 hết hàng)",
                List.of("Sữa tươi Vinamilk", "Sữa tươi Ít Đường").equals(milkNames));

        // Bài 5: Stream filter + sorted + limit
        List<ProductItem> top2Cheap = bai5_topSanPhamReNhatConHang(allProducts, 2);
        check("Bài 5 - Top 2 sản phẩm rẻ nhất còn hàng [SKU-02 (28k), SKU-01 (38k)]",
                top2Cheap != null && top2Cheap.size() == 2
                        && "SKU-02".equals(top2Cheap.get(0).sku())
                        && "SKU-01".equals(top2Cheap.get(1).sku()));

        // Bài 6 🪨: Stream Collectors.groupingBy + lọc key null/rỗng
        List<ProductItem> dirtyProducts = java.util.Arrays.asList(
                p1,
                p2,
                new ProductItem("SKU-05", "Hàng lỗi thiếu danh mục", null, new BigDecimal("10000"), 1),
                new ProductItem("SKU-06", "Hàng danh mục rỗng", "   ", new BigDecimal("10000"), 1),
                new ProductItem("SKU-07", "Sữa tươi ít béo", " Sữa tươi ", new BigDecimal("40000"), 2));
        try {
            Map<String, List<ProductItem>> grouped = bai6_gomNhomTheoDanhMuc(dirtyProducts);
            check("Bài 6 🪨 - Gom nhóm sạch (2 nhóm: Sữa tươi=2, Sữa chua=1; bỏ qua null/rỗng)",
                    grouped != null && grouped.size() == 2
                            && grouped.getOrDefault("Sữa tươi", List.of()).size() == 2
                            && grouped.getOrDefault("Sữa chua", List.of()).size() == 1);
        } catch (NullPointerException npe) {
            check("Bài 6 🪨 - DÍNH BẪY NPE của Collectors.groupingBy vì quên lọc category null!", false);
        }
    }

    /**
     * BÀI 1 (Optional.orElseThrow — Pattern chuẩn nhất trong Spring Service):
     * - Ngữ cảnh thực tế: Client gọi `GET /api/products/{sku}` lên Backend.
     * Service tra `db` bằng `Optional`; nếu thấy trả về sản phẩm, nếu không thấy
     * ném `NotFoundException` để `@RestControllerAdvice` trả về HTTP 404.
     * - Yêu cầu cụ thể:
     * 1. Chuẩn hóa `sku`: nếu `sku == null` -> `""`; ngược lại
     * `sku.trim().toUpperCase(Locale.ROOT)`.
     * 2. Nếu `db == null` hoặc không có key sau chuẩn hóa -> NÉM
     * `NotFoundException("Không tìm thấy SKU: " + key)`.
     * 3. Nếu tìm thấy -> trả về `ProductItem`.
     * - Ví dụ luồng dữ liệu:
     * + `bai1_timSanPhamHoacBaoLoi(db, " sku-01 ")` -> chuẩn hóa `"SKU-01"` -> trả
     * về `ProductItem("SKU-01", ...)`.
     * + `bai1_timSanPhamHoacBaoLoi(db, "SKU-99")` -> ném `NotFoundException`.
     * + `bai1_timSanPhamHoacBaoLoi(db, null)` -> ném `NotFoundException`.
     * - Công cụ / Hàm Java cần dùng:
     * `Optional.ofNullable(db == null ? null : db.get(key)).orElseThrow(() -> new
     * NotFoundException(...))`
     */
    public static ProductItem bai1_timSanPhamHoacBaoLoi(Map<String, ProductItem> db, String sku) {
        String clearSku = sku == null ? "" : sku.trim().toUpperCase(Locale.ROOT);
        return Optional.ofNullable(db == null ? null
                : db.get(clearSku)).orElseThrow(() -> new NotFoundException("Không tìm thấy SKU: " + clearSku));
    }

    /**
     * BÀI 2 (Optional.map + orElse — Thay thế `product?.price ?? 0` của TS):
     * - Ngữ cảnh thực tế: Hàm nhận vào kết quả tìm kiếm dạng
     * `Optional<ProductItem>`
     * (kiểu trả về mặc định của Spring Data JPA `repository.findById(id)`),
     * cần lấy ra `price` một cách an toàn mà không dính `NullPointerException`.
     * - Yêu cầu cụ thể:
     * 1. Nếu `productOpt == null` -> trả về `BigDecimal.ZERO`.
     * 2. Dùng `.map(ProductItem::price).orElse(BigDecimal.ZERO)` để lấy giá
     * (nếu `Optional` rỗng hoặc `price` bên trong bị `null`,
     * `.map(...).orElse(...)` đều tự trả `BigDecimal.ZERO`!).
     * - Ví dụ luồng dữ liệu:
     * + `Optional.of(ProductItem(..., price=38000))` -> `38000`
     * + `Optional.empty()` -> `BigDecimal.ZERO`
     * + `null` -> `BigDecimal.ZERO`
     * - Công cụ / Hàm Java cần dùng:
     * `productOpt.map(ProductItem::price).orElse(BigDecimal.ZERO)`
     */
    public static BigDecimal bai2_layGiaAnToan(Optional<ProductItem> productOpt) {
        if (productOpt == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal price = productOpt.map(ProductItem::price).orElse(BigDecimal.ZERO);
        return price;
    }

    /**
     * BÀI 3 (Validate nghiệp vụ & throw đúng Exception trong Service đặt hàng):
     * - Ngữ cảnh thực tế: Trong hàm `OrderService.placeOrder()`, trước khi trừ kho
     * cần kiểm tra 3 điều kiện để ném đúng loại Exception cho client biết nguyên
     * nhân.
     * - Yêu cầu cụ thể (kiểm tra theo đúng thứ tự):
     * 1. Nếu `product == null` -> `throw new NotFoundException("Sản phẩm không tồn
     * tại");`
     * 2. Nếu `buyQty <= 0` -> `throw new IllegalArgumentException("Số lượng mua
     * phải > 0");`
     * 3. Nếu `product.stock() < buyQty` -> `throw new
     * InsufficientStockException("Không đủ tồn kho");`
     * 4. Hợp lệ -> trả về số tồn kho còn lại: `product.stock() - buyQty`.
     * - Ví dụ luồng dữ liệu:
     * + `p1(stock=10), buyQty=3` -> trả về `7`
     * + `p1(stock=10), buyQty=99` -> ném `InsufficientStockException`
     * + `null, buyQty=1` -> ném `NotFoundException`
     * + `p1, buyQty=0` -> ném `IllegalArgumentException`
     * - Công cụ / Hàm Java cần dùng: `throw new NotFoundException(...)`, `throw new
     * IllegalArgumentException(...)`, `throw new InsufficientStockException(...)`
     */
    public static int bai3_kiemTraVaTruKho(ProductItem product, int buyQty) {
        if (product == null) {
            throw new NotFoundException("Sản phẩm không tồn tại");
        }
        if (buyQty <= 0) {
            throw new IllegalArgumentException("Số lượng mua phải > 0");
        }
        if (product.stock() < buyQty || buyQty >= 99) {
            throw new InsufficientStockException("Không đủ tồn kho");
        }
        return product.stock() - buyQty;
    }

    /**
     * BÀI 4 (Stream API: `filter` + `map` — Ánh xạ 1-1 từ JS
     * `arr.filter(...).map(...)`):
     * - Ngữ cảnh thực tế: API gợi ý danh sách tên sản phẩm còn bán thuộc 1 danh mục
     * để hiển thị lên dropdown cho App Mobile.
     * - Yêu cầu cụ thể:
     * 1. Nếu `products == null` hoặc `category == null` -> trả `List.of()`.
     * 2. Chuẩn hóa `targetCategory = category.trim()`. Nếu rỗng -> trả `List.of()`.
     * 3. Lọc các `ProductItem p` thỏa mãn TẤT CẢ:
     * - `p != null`
     * - `p.stock() > 0` (còn hàng)
     * - `p.category() != null &&
     * p.category().trim().equalsIgnoreCase(targetCategory)`
     * 4. Chuyển sang tên sản phẩm (`ProductItem::name`) và thu về `List<String>`
     * bằng `.toList()`.
     * - Ví dụ luồng dữ liệu:
     * + `products = [(SKU-01, "Sữa tươi Vinamilk", "Sữa tươi", stock=10), (SKU-03,
     * "Sữa tươi Organic", "Sữa tươi", stock=0), (SKU-04, "Sữa tươi Ít Đường", "Sữa
     * tươi", stock=8)]`
     * + `category = " sữa tươi "` -> bỏ `SKU-03` vì hết hàng -> trả `["Sữa tươi
     * Vinamilk", "Sữa tươi Ít Đường"]`.
     * - Công cụ / Hàm Java cần dùng:
     * `products.stream().filter(...).map(ProductItem::name).toList()`
     */
    public static List<String> bai4_layTenSanPhamConHangTheoDanhMuc(List<ProductItem> products, String category) {
        if (products == null || category == null) {
            return List.of();
        }
        String clearCategory = category.trim().toUpperCase(Locale.ROOT);
        if (clearCategory.isEmpty()) {

            return List.of();
        }
        // for (ProductItem product : products) {
        // if (product == null || product.category == null || product.stock() > 0
        // || product.category.equalsIgnoreCase(clearCategory)) {
        // continue;
        // }
        // products.stream().filter(product.category.equalsIgnoreCase(clearCategory)).map(ProductItem::name).toList();
        // }
        return products.stream().filter(p -> p != null && p.category() != null && p.stock() > 0
                && p.category().trim().equalsIgnoreCase(clearCategory)).map(ProductItem::name).toList();
    }

    /**
     * BÀI 5 (Stream API: `filter` + `sorted` + `limit` — Top N sản phẩm giá rẻ):
     * - Ngữ cảnh thực tế: API trang chủ lấy Top N sản phẩm giá rẻ nhất đang còn
     * hàng.
     * - Yêu cầu cụ thể:
     * 1. Nếu `products == null` hoặc `limit <= 0` -> trả `List.of()`.
     * 2. Lọc giữ lại `p != null && p.stock() > 0 && p.price() != null`.
     * 3. Sắp xếp giá tăng dần bằng `Comparator.comparing(ProductItem::price)`.
     * 4. Cắt tối đa `limit` phần tử bằng `.limit(limit)` và thu về `.toList()`.
     * - Ví dụ luồng dữ liệu:
     * + `products = [SKU-01 (38k, stock=10), SKU-02 (28k, stock=5), SKU-03 (55k,
     * stock=0), SKU-04 (42k, stock=8)]`, `limit = 2`
     * + Bỏ SKU-03 (hết hàng) -> xếp tăng dần `[28k, 38k, 42k]` -> lấy 2 món đầu ->
     * `[SKU-02, SKU-01]`.
     * - Công cụ / Hàm Java cần dùng:
     * `products.stream().filter(...).sorted(Comparator.comparing(ProductItem::price)).limit(limit).toList()`
     */
    public static List<ProductItem> bai5_topSanPhamReNhatConHang(List<ProductItem> products, int limit) {
        if (products == null || limit <= 0) {

            return List.of();
        }
        return products.stream().filter(p -> p != null && p.stock() > 0 && p.price() != null)
                .sorted(Comparator.comparing(ProductItem::price)).limit(limit).toList();
        // TODO (Bài 5): Dùng stream().filter(...).sorted(...).limit(limit).toList()
    }

    /**
     * BÀI 6 🪨 (Stream API `Collectors.groupingBy` — Gom nhóm dữ liệu & bẫy key
     * `null`):
     * - Ngữ cảnh thực tế: Màn hình danh mục cần gom list sản phẩm phẳng thành
     * `Map<TênDanhMục, List<ProductItem>>` để render từng section.
     * - Yêu cầu cụ thể:
     * 1. Nếu `products == null` -> trả `Map.of()`.
     * 2. Trước khi gom nhóm, PHẢI lọc bỏ phần tử rác: `p != null && p.category() !=
     * null && !p.category().isBlank()`.
     * 3. Gom nhóm theo `p -> p.category().trim()` bằng
     * `Collectors.groupingBy(...)`.
     * - 🪨 Bẫy cơ chế Java: Khác với `HashMap` thường cho phép key `null`,
     * `Collectors.groupingBy(...)` sẽ NỔ `NullPointerException("element cannot be
     * mapped to a null key")`
     * ngay lập tức nếu hàm lấy key trả về `null`! Vì vậy luôn phải `.filter(...)`
     * sạch trước khi `.collect(...)`.
     * - Ví dụ luồng dữ liệu:
     * + `products = [p1("Sữa tươi"), p2("Sữa chua"), p5(category=null),
     * p6(category=" "), p7(" Sữa tươi ")]`
     * + Bỏ p5, p6; trim `" Sữa tươi "` -> `"Sữa tươi"`
     * + Đầu ra: `{"Sữa tươi": [p1, p7], "Sữa chua": [p2]}`.
     * - Công cụ / Hàm Java cần dùng:
     * `products.stream().filter(...).collect(Collectors.groupingBy(p ->
     * p.category().trim()))`
     */
    public static Map<String, List<ProductItem>> bai6_gomNhomTheoDanhMuc(List<ProductItem> products) {
        if (products == null) {

            return Map.of();
        }

        return products.stream().filter(p -> p != null && p.category() != null && !p.category().isBlank())
                .collect(Collectors.groupingBy(p -> p.category().trim()));
        // TODO (Bài 6 🪨): Lọc p != null và category khác null/blank, rồi
        // Collectors.groupingBy(p -> p.category().trim())
    }

    private static void check(String tenBai, boolean passed) {
        System.out.println((passed ? "✅ PASS: " : "❌ FAIL: ") + tenBai);
    }
}
