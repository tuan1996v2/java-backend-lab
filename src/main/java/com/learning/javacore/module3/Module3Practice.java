package com.learning.javacore.module3;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * MODULE 3: Map<K, V> (HashMap / LinkedHashMap), getOrDefault, entrySet & Gom nhóm dữ liệu
 *
 * HƯỚNG DẪN CHẠY NHANH (không cần mạng):
 * java src/main/java/com/learning/javacore/module3/Module3Practice.java
 * (Hoặc sau khi có cache Maven: ./mvnw -o compile -q && java -cp target/classes com.learning.javacore.module3.Module3Practice)
 */
public class Module3Practice {

    private static final BigDecimal FREESHIP_THRESHOLD = new BigDecimal("500000");
    private static final BigDecimal DEFAULT_SHIP_FEE = new BigDecimal("35000");
    private static final Map<String, BigDecimal> SHIP_FEES = Map.of(
            "HN", new BigDecimal("20000"),
            "HCM", new BigDecimal("25000"),
            "DN", new BigDecimal("25000")
    );

    static class ProductItem {
        String sku;
        String name;
        String category;
        BigDecimal price;
        int stock;

        ProductItem(String sku, String name, String category, String price, int stock) {
            this.sku = sku;
            this.name = name;
            this.category = category;
            this.price = price == null ? null : new BigDecimal(price);
            this.stock = stock;
        }
    }

    static class OrderLine {
        String sku;
        int quantity;

        OrderLine(String sku, int quantity) {
            this.sku = sku;
            this.quantity = quantity;
        }
    }

    public static void main(String[] args) {
        List<ProductItem> catalog = Arrays.asList(
                new ProductItem("SKU-01", "Sữa tươi 180ml", "Sữa tươi", "38000", 10),
                new ProductItem("SKU-02", "Sữa chua nha đam", "Sữa chua", "28000", 0),
                new ProductItem("SKU-03", "Phô mai que", "Phô mai", "65000", 5),
                new ProductItem("SKU-04", "Sữa tươi 1L", "Sữa tươi", "42000", 2)
        );

        System.out.println("=== KIỂM TRA MODULE 3 ===");

        // Bài 1
        Map<String, BigDecimal> priceMap = bai1_taoBangGiaTheoSku(catalog);
        check("Bài 1 - Bảng giá có 4 SKU", priceMap != null && priceMap.size() == 4);
        check("Bài 1 - Giá SKU-03 đúng 65000",
                priceMap != null && priceMap.get("SKU-03") != null
                        && new BigDecimal("65000").compareTo(priceMap.get("SKU-03")) == 0);
        Map<String, BigDecimal> nullPriceMap = bai1_taoBangGiaTheoSku(Arrays.asList(
                new ProductItem("SKU-X", "Thiếu giá", "Khác", null, 1)
        ));
        check("Bài 1 - Bỏ qua SP có price == null", nullPriceMap.isEmpty());

        // Bài 2
        check("Bài 2 - Tra giá sku viết thường có dấu cách",
                new BigDecimal("38000").compareTo(bai2_traGiaSanPham(priceMap, "  sku-01 ")) == 0);
        check("Bài 2 - SKU không tồn tại trả về 0",
                BigDecimal.ZERO.compareTo(bai2_traGiaSanPham(priceMap, "SKU-999")) == 0);
        check("Bài 2 - SKU null trả về 0",
                BigDecimal.ZERO.compareTo(bai2_traGiaSanPham(priceMap, null)) == 0);

        // Bài 3
        Map<String, Integer> countByCat = bai3_demSoLuongTheoDanhMuc(catalog);
        check("Bài 3 - Đếm SP theo danh mục",
                Map.of("Sữa tươi", 2, "Sữa chua", 1, "Phô mai", 1).equals(countByCat));

        // Bài 4
        Map<String, Integer> stockByCat = bai4_tinhTongTonKhoTheoDanhMuc(catalog);
        check("Bài 4 - Tổng tồn kho theo danh mục",
                Map.of("Sữa tươi", 12, "Sữa chua", 0, "Phô mai", 5).equals(stockByCat));

        // Bài 5
        Map<String, BigDecimal> valueByCat = bai5_tinhGiaTriTonKhoTheoDanhMuc(catalog);
        check("Bài 5 - Giá trị tồn kho Sữa tươi = 464000",
                valueByCat != null && valueByCat.get("Sữa tươi") != null
                        && new BigDecimal("464000").compareTo(valueByCat.get("Sữa tươi")) == 0);
        check("Bài 5 - Giá trị tồn kho Sữa chua = 0",
                valueByCat != null && valueByCat.get("Sữa chua") != null
                        && BigDecimal.ZERO.compareTo(valueByCat.get("Sữa chua")) == 0);

        // Bài 6 🪨
        Map<String, Integer> cart1 = Map.of("SKU-01", 2, "SKU-02", 1);
        Map<String, Integer> cart2 = Map.of(" sku-01 ", 3, "SKU-03", 4);
        Map<String, Integer> mergedCart = bai6_congDonGioHangTheoSku(cart1, cart2);
        check("Bài 6 🪨 - Cộng dồn giỏ hàng (không crash Map.of)",
                Map.of("SKU-01", 5, "SKU-02", 1, "SKU-03", 4).equals(mergedCart));
        check("Bài 6 🪨 - cart1 null không crash",
                Map.of("SKU-01", 3, "SKU-03", 4).equals(bai6_congDonGioHangTheoSku(null, cart2)));

        // Bài 7
        check("Bài 7 - Danh mục nhiều SP nhất",
                "Sữa tươi".equals(bai7_timDanhMucNhieuSanPhamNhat(countByCat)));
        check("Bài 7 - Map rỗng trả về null",
                bai7_timDanhMucNhieuSanPhamNhat(Map.of()) == null);

        // Bài 8
        Map<String, List<String>> grouped = bai8_gomNhomSkuTheoDanhMuc(catalog);
        check("Bài 8 - Gom nhóm SKU theo danh mục",
                grouped != null
                        && List.of("SKU-01", "SKU-04").equals(grouped.get("Sữa tươi"))
                        && List.of("SKU-03").equals(grouped.get("Phô mai")));

        // Bài 9 🪨
        List<OrderLine> orderLines = Arrays.asList(
                new OrderLine("  sku-01 ", 2),
                new OrderLine("SKU-03", 1),
                new OrderLine("SKU-KHONG-CO", 5),
                null
        );
        check("Bài 9 🪨 - Tổng tiền đơn hàng tra từ catalog",
                new BigDecimal("141000").compareTo(bai9_tinhTongTienDonHang(orderLines, catalog)) == 0);

        // Bài 10 🪨
        Map<String, Integer> stockMap = Map.of("SKU-01", 5, "SKU-02", 0);
        Map<String, Integer> orderQty = new LinkedHashMap<>();
        orderQty.put("SKU-01", 3);
        orderQty.put("SKU-02", 1);
        orderQty.put("SKU-99", 2);
        check("Bài 10 🪨 - Tìm các SKU không đủ hàng",
                List.of("SKU-02", "SKU-99").equals(bai10_timSkuKhongDuHang(stockMap, orderQty)));

        // Bài 11 🪨
        List<ProductItem> samePriceVariants = Arrays.asList(
                new ProductItem("A1", "SP 1", "A", "38000", 1),
                new ProductItem("A2", "SP 2", "A", "38000.00", 1),
                new ProductItem("A3", "SP 3", "A", "65000.0", 1)
        );
        Map<BigDecimal, Integer> priceStats = bai11_thongKeSoLuongTheoMucGia(samePriceVariants);
        check("Bài 11 🪨 - Gộp 38000 và 38000.00 thành cùng 1 mức giá",
                priceStats != null && priceStats.size() == 2
                        && Integer.valueOf(2).equals(priceStats.get(new BigDecimal("38000").stripTrailingZeros())));

        // Bài 12
        check("Bài 12 - Phí ship HN đơn < 500k",
                new BigDecimal("20000").compareTo(bai12_tinhPhiShipBangMap(new BigDecimal("200000"), "hn")) == 0);
        check("Bài 12 - Phí ship tỉnh khác (mặc định 35k)",
                new BigDecimal("35000").compareTo(bai12_tinhPhiShipBangMap(new BigDecimal("200000"), "CAN_THO")) == 0);
        check("Bài 12 - Phí ship tỉnh null (mặc định 35k)",
                new BigDecimal("35000").compareTo(bai12_tinhPhiShipBangMap(new BigDecimal("200000"), null)) == 0);
        check("Bài 12 - Freeship đơn >= 500k",
                BigDecimal.ZERO.compareTo(bai12_tinhPhiShipBangMap(new BigDecimal("500000"), "HN")) == 0);
    }

    /** BÀI 1: Tạo bảng tra cứu giá (sku -> price), bỏ qua item/sku/price không hợp lệ. */
    public static Map<String, BigDecimal> bai1_taoBangGiaTheoSku(List<ProductItem> items) {
        if (items == null || items.isEmpty()) {
            return new HashMap<>();
        }
        Map<String, BigDecimal> priceMap = new HashMap<>(items.size());
        for (ProductItem item : items) {
            if (item == null || item.price == null) {
                continue;
            }
            String normalizedSku = normalizeUpper(item.sku);
            if (normalizedSku == null) {
                continue;
            }
            priceMap.put(normalizedSku, item.price);
        }
        return priceMap;
    }

    /** BÀI 2: Tra giá an toàn theo sku, xử lý cả trường hợp value trong Map bị null. */
    public static BigDecimal bai2_traGiaSanPham(Map<String, BigDecimal> priceMap, String sku) {
        if (priceMap == null || priceMap.isEmpty()) {
            return BigDecimal.ZERO;
        }
        String normalizedSku = normalizeUpper(sku);
        if (normalizedSku == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal price = priceMap.get(normalizedSku);
        return price != null ? price : BigDecimal.ZERO;
    }

    /** BÀI 3: Đếm số sản phẩm theo từng category (giữ nguyên hoa/thường sau trim). */
    public static Map<String, Integer> bai3_demSoLuongTheoDanhMuc(List<ProductItem> items) {
        if (items == null || items.isEmpty()) {
            return new HashMap<>();
        }
        Map<String, Integer> countMap = new HashMap<>();
        for (ProductItem item : items) {
            if (item == null) {
                continue;
            }
            String category = normalizeKeepCase(item.category);
            if (category == null) {
                continue;
            }
            countMap.put(category, countMap.getOrDefault(category, 0) + 1);
        }
        return countMap;
    }

    /** BÀI 4: Tính tổng tồn kho theo từng category (bỏ qua dòng có stock < 0). */
    public static Map<String, Integer> bai4_tinhTongTonKhoTheoDanhMuc(List<ProductItem> items) {
        if (items == null || items.isEmpty()) {
            return new HashMap<>();
        }
        Map<String, Integer> stockByCat = new HashMap<>();
        for (ProductItem item : items) {
            if (item == null || item.stock < 0) {
                continue;
            }
            String category = normalizeKeepCase(item.category);
            if (category == null) {
                continue;
            }
            stockByCat.put(category, stockByCat.getOrDefault(category, 0) + item.stock);
        }
        return stockByCat;
    }

    /** BÀI 5: Tính tổng giá trị tồn kho (price * stock) theo từng category. */
    public static Map<String, BigDecimal> bai5_tinhGiaTriTonKhoTheoDanhMuc(List<ProductItem> items) {
        if (items == null || items.isEmpty()) {
            return new HashMap<>();
        }
        Map<String, BigDecimal> valueByCat = new HashMap<>();
        for (ProductItem item : items) {
            if (item == null || item.price == null || item.stock < 0) {
                continue;
            }
            String category = normalizeKeepCase(item.category);
            if (category == null) {
                continue;
            }
            BigDecimal itemValue = item.stock == 0
                    ? BigDecimal.ZERO
                    : item.price.multiply(BigDecimal.valueOf(item.stock));
            BigDecimal current = valueByCat.getOrDefault(category, BigDecimal.ZERO);
            valueByCat.put(category, current.add(itemValue));
        }
        return valueByCat;
    }

    /** BÀI 6 🪨: Cộng dồn 2 giỏ hàng vào Map mới, an toàn với Map.of và null. */
    public static Map<String, Integer> bai6_congDonGioHangTheoSku(
            Map<String, Integer> cart1, Map<String, Integer> cart2) {
        Map<String, Integer> merged = new HashMap<>();
        mergeCartInto(cart1, merged);
        mergeCartInto(cart2, merged);
        return merged;
    }

    private static void mergeCartInto(Map<String, Integer> source, Map<String, Integer> target) {
        if (source == null || source.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Integer> entry : source.entrySet()) {
            String normalizedSku = normalizeUpper(entry.getKey());
            Integer qty = entry.getValue();
            if (normalizedSku == null || qty == null || qty <= 0) {
                continue;
            }
            target.put(normalizedSku, target.getOrDefault(normalizedSku, 0) + qty);
        }
    }

    /** BÀI 7: Tìm tên danh mục có số lượng sản phẩm nhiều nhất. */
    public static String bai7_timDanhMucNhieuSanPhamNhat(Map<String, Integer> countByCategory) {
        if (countByCategory == null || countByCategory.isEmpty()) {
            return null;
        }
        String bestCategory = null;
        int maxCount = 0;
        for (Map.Entry<String, Integer> entry : countByCategory.entrySet()) {
            String category = normalizeKeepCase(entry.getKey());
            Integer count = entry.getValue();
            if (category == null || count == null || count <= 0) {
                continue;
            }
            if (count > maxCount) {
                bestCategory = category;
                maxCount = count;
            }
        }
        return bestCategory;
    }

    /** BÀI 8: Gom nhóm danh sách SKU theo từng category (giữ thứ tự). */
    public static Map<String, List<String>> bai8_gomNhomSkuTheoDanhMuc(List<ProductItem> items) {
        if (items == null || items.isEmpty()) {
            return new LinkedHashMap<>();
        }
        Map<String, List<String>> groups = new LinkedHashMap<>();
        for (ProductItem item : items) {
            if (item == null) {
                continue;
            }
            String category = normalizeKeepCase(item.category);
            String sku = normalizeKeepCase(item.sku);
            if (category == null || sku == null) {
                continue;
            }
            groups.computeIfAbsent(category, k -> new ArrayList<>()).add(sku);
        }
        return groups;
    }

    /** BÀI 9 🪨: Tính tổng tiền đơn hàng theo giá gốc trong catalog (tra cứu O(1)). */
    public static BigDecimal bai9_tinhTongTienDonHang(List<OrderLine> orderLines, List<ProductItem> catalog) {
        if (orderLines == null || orderLines.isEmpty() || catalog == null || catalog.isEmpty()) {
            return BigDecimal.ZERO;
        }
        Map<String, BigDecimal> priceMap = bai1_taoBangGiaTheoSku(catalog);
        BigDecimal total = BigDecimal.ZERO;
        for (OrderLine line : orderLines) {
            if (line == null || line.quantity <= 0) {
                continue;
            }
            BigDecimal unitPrice = bai2_traGiaSanPham(priceMap, line.sku);
            if (unitPrice.signum() > 0) {
                total = total.add(unitPrice.multiply(BigDecimal.valueOf(line.quantity)));
            }
        }
        return total;
    }

    /** BÀI 10 🪨: Trả về danh sách SKU không đủ hàng theo đúng thứ tự orderQty. */
    public static List<String> bai10_timSkuKhongDuHang(
            Map<String, Integer> stockMap, Map<String, Integer> orderQty) {
        if (orderQty == null || orderQty.isEmpty()) {
            return new ArrayList<>();
        }
        List<String> insufficientSkus = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : orderQty.entrySet()) {
            String normalizedSku = normalizeUpper(entry.getKey());
            Integer qty = entry.getValue();
            if (normalizedSku == null || qty == null || qty <= 0) {
                continue;
            }
            Integer stockObj = stockMap != null ? stockMap.get(normalizedSku) : null;
            int stock = stockObj != null ? stockObj : 0;
            if (stock < qty) {
                insufficientSkus.add(normalizedSku);
            }
        }
        return insufficientSkus;
    }

    /** BÀI 11 🪨: Thống kê số lượng sản phẩm theo mức giá, chuẩn hóa scale BigDecimal trước khi làm key. */
    public static Map<BigDecimal, Integer> bai11_thongKeSoLuongTheoMucGia(List<ProductItem> items) {
        if (items == null || items.isEmpty()) {
            return new HashMap<>();
        }
        Map<BigDecimal, Integer> priceStats = new HashMap<>();
        for (ProductItem item : items) {
            if (item == null || item.price == null) {
                continue;
            }
            BigDecimal normalizedPrice = item.price.stripTrailingZeros();
            priceStats.put(normalizedPrice, priceStats.getOrDefault(normalizedPrice, 0) + 1);
        }
        return priceStats;
    }

    /** BÀI 12: Tính phí ship bằng bảng hằng số Map.of + getOrDefault. */
    public static BigDecimal bai12_tinhPhiShipBangMap(BigDecimal orderTotal, String provinceCode) {
        if (orderTotal != null && orderTotal.compareTo(FREESHIP_THRESHOLD) >= 0) {
            return BigDecimal.ZERO;
        }
        String normalizedProvince = normalizeUpper(provinceCode);
        if (normalizedProvince == null) {
            return DEFAULT_SHIP_FEE;
        }
        return SHIP_FEES.getOrDefault(normalizedProvince, DEFAULT_SHIP_FEE);
    }

    private static String normalizeUpper(String raw) {
        if (raw == null) {
            return null;
        }
        String trimmed = raw.trim();
        return trimmed.isEmpty() ? null : trimmed.toUpperCase(Locale.ROOT);
    }

    private static String normalizeKeepCase(String raw) {
        if (raw == null) {
            return null;
        }
        String trimmed = raw.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private static void check(String tenBai, boolean passed) {
        System.out.println((passed ? "✅ PASS: " : "❌ FAIL: ") + tenBai);
    }
}
