package com.learning.javacore.module2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * MODULE 2: List (ArrayList), Set (HashSet / LinkedHashSet) & Vòng lặp for
 *
 * HƯỚNG DẪN CHẠY NHANH:
 * ./mvnw -o compile -q && java -cp target/classes
 * com.learning.javacore.module2.Module2Practice
 *
 * BẢNG ĐỐI CHIẾU REACT NATIVE (JS/TS) -> JAVA:
 * - const arr: string[] = [] -> List<String> list = new ArrayList<>();
 * - arr.push("a") -> list.add("a");
 * - arr.length -> list.size();
 * - arr[0] -> list.get(0);
 * - arr.slice(0, n) -> list.subList(0, Math.min(n, list.size()));
 * (KHÁC JS: JS slice(0, 10) trên mảng 2 phần tử vẫn OK; Java subList(0, 10) sẽ
 * báo lỗi vượt quá độ dài!)
 * - for (const item of arr) { } -> for (ProductItem item : items) { }
 * - new Set<string>() -> Set<String> set = new HashSet<>(); (không giữ thứ tự)
 * Set<String> set = new LinkedHashSet<>(); (giữ đúng thứ tự thêm vào)
 * - set.has(x) / set.add(x) -> set.contains(x) / set.add(x) (add trả về false
 * nếu x đã có!)
 */
public class Module2Practice {

    // Class mẫu giả lập 1 sản phẩm (giống Entity Product rút gọn)
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
            this.price = new BigDecimal(price);
            this.stock = stock;
        }
    }

    public static void main(String[] args) {
        List<ProductItem> sample = List.of(
                new ProductItem("SKU-01", "Sữa tươi 180ml", "Sữa tươi", "38000", 10),
                new ProductItem("SKU-02", "Sữa chua nha đam", "Sữa chua", "28000", 0),
                new ProductItem("SKU-03", "Phô mai que", "Phô mai", "65000", 5),
                new ProductItem("SKU-04", "Sữa tươi 1L", "Sữa tươi", "42000", 2));

        System.out.println("=== KIỂM TRA MODULE 2 ===");

        List<String> inStockNames = bai1_layTenSanPhamConHang(sample);
        check("Bài 1 - Lọc tên SP còn hàng",
                List.of("Sữa tươi 180ml", "Phô mai que", "Sữa tươi 1L").equals(inStockNames));

        // 38000*10 + 28000*0 + 65000*5 + 42000*2 = 380000 + 325000 + 84000 = 789000
        check("Bài 2 - Tổng giá trị tồn kho",
                new BigDecimal("789000").compareTo(bai2_tongGiaTriTonKho(sample)) == 0);

        ProductItem maxProduct = bai3_timSanPhamDatNhat(sample);
        check("Bài 3 - Tìm SP đắt nhất", maxProduct != null && "SKU-03".equals(maxProduct.sku));
        check("Bài 3 - List rỗng trả null", bai3_timSanPhamDatNhat(List.of()) == null);

        check("Bài 4 - Đếm SP theo danh mục", bai4_demSanPhamTheoDanhMuc(sample, "sữa tươi") == 2);

        Set<String> categories = bai5_layDanhSachDanhMucKhongTrung(sample);
        check("Bài 5 - Danh mục không trùng",
                Set.of("Sữa tươi", "Sữa chua", "Phô mai").equals(categories));

        check("Bài 6 - Phát hiện SKU trùng",
                bai6_kiemTraCoMaSkuTrungKhong(List.of("A1", "A2", "A1")));
        check("Bài 6 - Không có SKU trùng",
                !bai6_kiemTraCoMaSkuTrungKhong(List.of("A1", "A2", "A3")));

        check("Bài 7 - Gộp 2 list không trùng, giữ thứ tự",
                List.of("A1", "A2", "A3", "A4").equals(
                        bai7_gopHaiDanhSachKhongTrung(List.of("A1", "A2"), List.of("A2", "A3", "A4"))));

        List<ProductItem> mutableList = new ArrayList<>(sample);
        mutableList.add(new ProductItem("SKU-05", "Nước ép cam", "Đồ uống", "15000", 0));
        int removed = bai8_xoaSanPhamHetHang(mutableList);
        check("Bài 8 - Xóa đúng 2 SP hết hàng", removed == 2 && mutableList.size() == 3);

        System.out.println("--- BONUS (BÀI 9 - 12 🪨) ---");

        List<String> expectedSkus = Arrays.asList("SKU-01", " sku-02 ", "SKU-03", "SKU-01", null);
        List<String> actualSkus = Arrays.asList("SKU-02", null, "  sku-01  ");
        check("Bài 9 - Tìm SKU thiếu trong kho",
                List.of("SKU-03").equals(bai9_timMaSkuThieuTrongKho(expectedSkus, actualSkus)));
        check("Bài 9 - Kho trống thì thiếu hết (không trùng)",
                List.of("SKU-01", "SKU-02", "SKU-03").equals(
                        bai9_timMaSkuThieuTrongKho(List.of("SKU-01", "sku-02", "SKU-01", "SKU-03"), null)));

        check("Bài 10 - Lọc giá [38000.00 .. 65000.0]",
                List.of("SKU-01", "SKU-03", "SKU-04").equals(
                        bai10_locSanPhamTheoKhoangGia(sample, new BigDecimal("38000.00"), new BigDecimal("65000.0"))));
        check("Bài 10 - Chỉ có maxPrice (minPrice = null)",
                List.of("SKU-01", "SKU-02").equals(
                        bai10_locSanPhamTheoKhoangGia(sample, null, new BigDecimal("38000"))));

        List<String> rawTags = Arrays.asList("  sua ", null, "   ", "SUA", "giam-gia", "moi", "hot");
        check("Bài 11 - Làm sạch tag & giới hạn 3",
                List.of("SUA", "GIAM-GIA", "MOI").equals(bai11_lamSachVaGioiHanTags(rawTags, 3)));
        check("Bài 11 - limit lớn hơn số tag sạch",
                List.of("SUA").equals(bai11_lamSachVaGioiHanTags(List.of(" sua ", "SUA"), 10)));
        check("Bài 11 - limit <= 0 trả list rỗng",
                List.of().equals(bai11_lamSachVaGioiHanTags(rawTags, 0)));

        List<String> promoCategories = Arrays.asList(" sữa tươi ", "Đồ uống", null);
        check("Bài 12 - Tổng tiền nhóm KM (không giới hạn)",
                new BigDecimal("464000")
                        .compareTo(bai12_tinhTongTienNhomKhuyenMai(sample, promoCategories, null)) == 0);
        check("Bài 12 - Tổng tiền nhóm KM bị chặn trần 300k",
                new BigDecimal("300000").compareTo(
                        bai12_tinhTongTienNhomKhuyenMai(sample, promoCategories, new BigDecimal("300000.00"))) == 0);
    }

    /**
     * BÀI 1: Trả về danh sách TÊN các sản phẩm còn hàng (stock > 0).
     * Gợi ý:
     * List<String> result = new ArrayList<>();
     * for (ProductItem item : items) {
     * if (item.stock > 0) { ... }
     * }
     * return result;
     */
    public static List<String> bai1_layTenSanPhamConHang(List<ProductItem> items) {
        List<String> result = new ArrayList<>();
        if (items == null) {
            return result;
        }
        for (ProductItem item : items) {
            if (item != null && item.stock > 0 && item.name != null) {
                result.add(item.name);
            }
        }
        return result;
    }

    /**
     * BÀI 2: Tính tổng giá trị tồn kho của cả danh sách = tổng (item.price *
     * item.stock).
     * Gợi ý:
     * - Khởi tạo `BigDecimal total = BigDecimal.ZERO;`
     * - Với mỗi item: `BigDecimal thanhTien =
     * item.price.multiply(BigDecimal.valueOf(item.stock));`
     * - Cộng dồn: `total = total.add(thanhTien);` (CHÚ Ý: BigDecimal là bất biến,
     * phải gán `total = total.add(...)`!)
     */
    public static BigDecimal bai2_tongGiaTriTonKho(List<ProductItem> items) {
        BigDecimal total = BigDecimal.ZERO;
        if (items == null) {
            return total;
        }
        for (ProductItem item : items) {
            if (item != null && item.price != null && item.stock > 0
                    && item.price.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal thanhTien = item.price.multiply(BigDecimal.valueOf(item.stock));
                total = total.add(thanhTien);
            }
        }
        return total;
    }

    /**
     * BÀI 3: Tìm sản phẩm có giá (price) cao nhất trong danh sách.
     * - Nếu items == null hoặc items.isEmpty() -> trả về null.
     * - Gợi ý: gán `ProductItem max = items.get(0);` rồi duyệt vòng lặp so sánh
     * `item.price.compareTo(max.price) > 0`.
     */
    public static ProductItem bai3_timSanPhamDatNhat(List<ProductItem> items) {
        if (items == null || items.isEmpty()) {
            return null;
        }
        ProductItem maxItem = null;
        for (ProductItem item : items) {
            if (item == null || item.price == null) {
                continue;
            }
            if (maxItem == null || item.price.compareTo(maxItem.price) > 0) {
                maxItem = item;
            }
        }
        return maxItem;
    }

    /**
     * BÀI 4: Đếm có bao nhiêu sản phẩm thuộc danh mục `category` (không phân biệt
     * hoa/thường).
     * - An toàn null: dùng `category != null &&
     * category.equalsIgnoreCase(item.category)`
     */
    public static int bai4_demSanPhamTheoDanhMuc(List<ProductItem> items, String category) {
        if (items == null || items.isEmpty() || category == null) {
            return 0;
        }
        String target = category.trim();
        int total = 0;
        for (ProductItem item : items) {
            if (item != null && item.category != null
                    && target.equalsIgnoreCase(item.category.trim())) {
                total++;
            }
        }
        return total;
    }

    /**
     * BÀI 5: Lấy tập hợp các tên danh mục (category) KHÔNG trùng lặp.
     * - Bỏ qua nếu item.category == null hoặc rỗng sau khi trim().
     * - Gợi ý: tạo `Set<String> result = new HashSet<>();` rồi
     * `result.add(item.category.trim());`
     */
    public static Set<String> bai5_layDanhSachDanhMucKhongTrung(List<ProductItem> items) {
        Set<String> result = new HashSet<>();
        if (items == null) {
            return result;
        }
        for (ProductItem item : items) {
            if (item == null || item.category == null) {
                continue;
            }
            String category = item.category.trim();
            if (!category.isEmpty()) {
                result.add(category);
            }
        }
        return result;
    }

    /**
     * BÀI 6 (Nghiệp vụ import Excel): Kiểm tra danh sách mã SKU có bị trùng mã nào
     * không.
     * - Trả về true nếu có ít nhất 1 mã bị lặp lại, false nếu tất cả đều duy nhất.
     * - Gợi ý: tạo `Set<String> seen = new HashSet<>();`
     * Khi gọi `seen.add(sku)`: nếu trả về `false` nghĩa là mã đó ĐÃ CÓ trước đó ->
     * return true ngay!
     */
    public static boolean bai6_kiemTraCoMaSkuTrungKhong(List<String> skuList) {
        if (skuList == null || skuList.size() < 2) {
            return false;
        }
        Set<String> seen = new HashSet<>();
        for (String sku : skuList) {
            String normal = normalizeText(sku);
            if (!normal.isEmpty() && !seen.add(normal)) {
                return true;
            }
        }
        return false;
    }

    /**
     * BÀI 7: Gộp 2 danh sách mã SKU thành 1 List mới KHÔNG chứa mã trùng và GIỮ
     * ĐÚNG THỨ TỰ xuất hiện.
     * - Gợi ý:
     * Set<String> merged = new LinkedHashSet<>();
     * merged.addAll(list1);
     * merged.addAll(list2);
     * return new ArrayList<>(merged);
     */
    public static List<String> bai7_gopHaiDanhSachKhongTrung(List<String> list1, List<String> list2) {
        Set<String> merged = new LinkedHashSet<>();
        if (list1 != null) {
            for (String item : list1) {
                if (item != null) {
                    merged.add(item);
                }
            }
        }
        if (list2 != null) {
            for (String item : list2) {
                if (item != null) {
                    merged.add(item);
                }
            }
        }
        return new ArrayList<>(merged);
    }

    /**
     * BÀI 8 (BẪY JAVA KINH ĐIỂN): Xóa toàn bộ sản phẩm có `stock <= 0` trực tiếp
     * khỏi `items`
     * và trả về SỐ LƯỢNG phần tử đã xóa.
     * - CẤM dùng `for (ProductItem item : items) { items.remove(item); }` -> sẽ nổ
     * lỗi `ConcurrentModificationException`!
     * - Cách chuẩn Java 8+: đếm trước (hoặc lấy `size` trước và sau), dùng
     * `items.removeIf(item -> item.stock <= 0);`
     * (Hoặc dùng `Iterator<ProductItem> it = items.iterator();` -> `while
     * (it.hasNext()) { ... it.remove(); }`)
     */
    public static int bai8_xoaSanPhamHetHang(List<ProductItem> items) {
        if (items == null || items.isEmpty()) {
            return 0;
        }
        int beforeSize = items.size();
        items.removeIf(item -> item == null || item.stock <= 0);
        return beforeSize - items.size();
    }

    /**
     * BÀI 9 🪨 (Kiểm kho): Đối soát 2 danh sách mã SKU.
     * - `expectedSkus`: danh sách mã chuẩn hệ thống kỳ vọng có trên kệ.
     * - `actualSkus`: danh sách mã nhân viên vừa quét thực tế ngoài kho.
     * - Trả về danh sách các mã có trong `expectedSkus` nhưng KHÔNG có trong
     * `actualSkus`,
     * không chứa mã trùng lặp và giữ đúng thứ tự xuất hiện trong `expectedSkus`.
     * - 🪨 Gợi ý: Đừng quên chuẩn hóa mã SKU (trim + viết hoa) trước khi đối chiếu,
     * và dữ liệu ngoài kho có thể chứa `null` hoặc chuỗi toàn khoảng trắng!
     */
    public static List<String> bai9_timMaSkuThieuTrongKho(List<String> expectedSkus, List<String> actualSkus) {
        if (expectedSkus == null || expectedSkus.isEmpty()) {
            return new ArrayList<>();
        }
        Set<String> actualSkuSet = new HashSet<>();
        if (actualSkus != null) {
            for (String actualSku : actualSkus) {
                String normal = normalizeText(actualSku);
                if (!normal.isEmpty()) {
                    actualSkuSet.add(normal);
                }
            }
        }

        Set<String> missingSkus = new LinkedHashSet<>();
        for (String expectedSku : expectedSkus) {
            String normal = normalizeText(expectedSku);
            if (!normal.isEmpty() && !actualSkuSet.contains(normal)) {
                missingSkus.add(normal);
            }
        }
        return new ArrayList<>(missingSkus);
    }

    /**
     * BÀI 10 🪨 (Bộ lọc giá): Trả về danh sách `sku` của các sản phẩm còn trong
     * khoảng giá
     * [minPrice, maxPrice] (tính cả 2 đầu mút `>= minPrice` và `<= maxPrice`), giữ
     * nguyên thứ tự.
     * - 🪨 Gợi ý: Khách hàng có thể chỉ lọc giá từ X trở lên (maxPrice == null)
     * hoặc chỉ lọc
     * dưới Y (minPrice == null). Và cẩn thận khi so sánh 2 BigDecimal khác số chữ
     * số lẻ!
     */
    public static List<String> bai10_locSanPhamTheoKhoangGia(
            List<ProductItem> items, BigDecimal minPrice, BigDecimal maxPrice) {
        if (items == null || items.isEmpty()) {
            return new ArrayList<>();
        }
        if (minPrice != null && maxPrice != null && minPrice.compareTo(maxPrice) > 0) {
            return new ArrayList<>();
        }
        List<String> result = new ArrayList<>();
        for (ProductItem item : items) {
            if (item == null || item.price == null || item.sku == null) {
                continue;
            }
            boolean okMin = minPrice == null || item.price.compareTo(minPrice) >= 0;
            boolean okMax = maxPrice == null || item.price.compareTo(maxPrice) <= 0;
            if (okMin && okMax) {
                result.add(item.sku);
            }
        }
        return result;
    }

    /**
     * BÀI 11 🪨 (Làm sạch ô nhập Tags/Hashtag sản phẩm trên App):
     * - Ngữ cảnh: Người dùng nhập các từ khóa/tag bừa bãi trên app trước khi lưu
     * DB;
     * hệ thống chỉ giữ tối đa `limit` tag sạch để không vỡ giao diện.
     * - Ví dụ luồng dữ liệu (với limit = 3):
     * + Đầu vào: [" iphone ", null, " ", "IPHONE", "pin trau", "gia re", "bao
     * hanh"]
     * + Bước 1 (bỏ null & rỗng): [" iphone ", "IPHONE", "pin trau", "gia re", "bao
     * hanh"]
     * + Bước 2 (trim + IN HOA): ["IPHONE", "IPHONE", "PIN TRAU", "GIA RE", "BAO
     * HANH"]
     * + Bước 3 (xóa trùng, giữ thứ tự): ["IPHONE", "PIN TRAU", "GIA RE", "BAO
     * HANH"]
     * + Bước 4 (cắt tối đa limit = 3): ["IPHONE", "PIN TRAU", "GIA RE"]
     * (Nếu limit <= 0 hoặc rawTags == null -> trả về list rỗng [])
     * - Công cụ dùng được (tương đương `arr.slice(0, limit)` bên JS):
     * + Cách 1 (dùng thứ đã học): trong vòng `for`, khi `set.size() == limit` thì
     * `break;`
     * + Cách 2 (hàm cắt list của Java): `list.subList(0, end)` với `int end =
     * Math.min(limit, list.size());`
     * - 🪨 Gợi ý: Khác với `arr.slice(0, 10)` bên JS tự dừng ở cuối mảng,
     * `subList(0, 10)` của Java
     * sẽ báo lỗi ngay nếu mảng chỉ có 2 phần tử!
     */
    public static List<String> bai11_lamSachVaGioiHanTags(List<String> rawTags, int limit) {
        if (rawTags == null || limit <= 0 || rawTags.isEmpty()) {
            return new ArrayList<>();
        }
        Set<String> cleanTags = new LinkedHashSet<>();
        for (String rawTag : rawTags) {
            String normal = normalizeText(rawTag);
            if (!normal.isEmpty()) {
                cleanTags.add(normal);
                if (cleanTags.size() == limit) {
                    break;
                }
            }
        }
        return new ArrayList<>(cleanTags);
    }

    /**
     * BÀI 12 🪨 (Thống kê giá trị hàng thuộc nhóm Khuyến mãi trên Dashboard):
     * - Ngữ cảnh: Admin chọn vài danh mục chạy chương trình khuyến mãi
     * (`promoCategories`)
     * và muốn biết tổng giá trị tồn kho (`price * stock`) của các sản phẩm thuộc
     * nhóm đó,
     * nhưng có mức trần tối đa (`cap`) để báo cáo/hạn mức ngân sách không vượt
     * ngưỡng.
     * - Ví dụ luồng dữ liệu (với `sample`, `promoCategories = [" sữa tươi ", "Đồ
     * uống", null]`, `cap = 300000`):
     * + Bước 1: Chuẩn hóa `promoCategories` (bỏ null/rỗng, trim + đưa về cùng kiểu
     * chữ) -> {"sữa tươi", "đồ uống"}
     * + Bước 2: Duyệt từng SP trong `items`:
     * - SKU-01 ("Sữa tươi", 38000 * 10 = 380000): thuộc nhóm KM & còn hàng -> cộng
     * 380000
     * - SKU-02 ("Sữa chua", stock = 0): bỏ qua
     * - SKU-03 ("Phô mai"): không nằm trong nhóm KM -> bỏ qua
     * - SKU-04 ("Sữa tươi", 42000 * 2 = 84000): thuộc nhóm KM & còn hàng -> cộng
     * 84000
     * + Bước 3: Tổng = 464000. Vì `cap = 300000` (464000 > 300000) -> chặn trần,
     * trả về `300000`!
     * (Nếu `cap == null` thì không chặn trần, trả về nguyên `464000`.)
     * - 🪨 Gợi ý: Đừng gọi `promoCategories.contains(item.category)` thẳng — `" sữa
     * tươi "` có dấu cách
     * và viết thường sẽ không khớp với `"Sữa tươi"` của sản phẩm!
     */
    public static BigDecimal bai12_tinhTongTienNhomKhuyenMai(
            List<ProductItem> items, List<String> promoCategories, BigDecimal cap) {
        if (items == null || items.isEmpty() || promoCategories == null
                || (cap != null && cap.compareTo(BigDecimal.ZERO) <= 0)) {
            return BigDecimal.ZERO;
        }

        Set<String> normalizedPromoSet = new HashSet<>();
        for (String category : promoCategories) {
            String normal = normalizeText(category);
            if (!normal.isEmpty()) {
                normalizedPromoSet.add(normal);
            }
        }
        if (normalizedPromoSet.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal total = BigDecimal.ZERO;
        for (ProductItem item : items) {
            if (item == null || item.stock <= 0 || item.price == null || item.category == null) {
                continue;
            }
            if (!normalizedPromoSet.contains(normalizeText(item.category))) {
                continue;
            }
            BigDecimal itemValue = item.price.multiply(BigDecimal.valueOf(item.stock));
            if (itemValue.compareTo(BigDecimal.ZERO) > 0) {
                total = total.add(itemValue);
            }
            if (cap != null && total.compareTo(cap) >= 0) {
                return cap;
            }
        }
        return total;
    }

    private static String normalizeText(String value) {
        return value == null ? "" : value.trim().toUpperCase(Locale.ROOT);
    }

    private static void check(String tenBai, boolean passed) {
        System.out.println((passed ? "✅ PASS: " : "❌ FAIL: ") + tenBai);
    }
}
