package com.learning.javacore.module2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * MODULE 2: List (ArrayList), Set (HashSet / LinkedHashSet) & Vòng lặp for
 *
 * HƯỚNG DẪN CHẠY NHANH:
 * ./mvnw -o compile -q && java -cp target/classes com.learning.javacore.module2.Module2Practice
 *
 * BẢNG ĐỐI CHIẾU REACT NATIVE (JS/TS) -> JAVA:
 * - const arr: string[] = []       ->  List<String> list = new ArrayList<>();
 * - arr.push("a")                  ->  list.add("a");
 * - arr.length                     ->  list.size();
 * - arr[0]                         ->  list.get(0);
 * - for (const item of arr) { }    ->  for (ProductItem item : items) { }
 * - new Set<string>()              ->  Set<String> set = new HashSet<>(); (không giữ thứ tự)
 *                                      Set<String> set = new LinkedHashSet<>(); (giữ đúng thứ tự thêm vào)
 * - set.has(x) / set.add(x)        ->  set.contains(x) / set.add(x) (add trả về false nếu x đã có!)
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
                new ProductItem("SKU-04", "Sữa tươi 1L", "Sữa tươi", "42000", 2)
        );

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
    }

    /**
     * BÀI 1: Trả về danh sách TÊN các sản phẩm còn hàng (stock > 0).
     * Gợi ý:
     *   List<String> result = new ArrayList<>();
     *   for (ProductItem item : items) {
     *       if (item.stock > 0) { ... }
     *   }
     *   return result;
     */
    public static List<String> bai1_layTenSanPhamConHang(List<ProductItem> items) {
        // TODO: Viết code của bạn ở đây
        return new ArrayList<>();
    }

    /**
     * BÀI 2: Tính tổng giá trị tồn kho của cả danh sách = tổng (item.price * item.stock).
     * Gợi ý:
     * - Khởi tạo `BigDecimal total = BigDecimal.ZERO;`
     * - Với mỗi item: `BigDecimal thanhTien = item.price.multiply(BigDecimal.valueOf(item.stock));`
     * - Cộng dồn: `total = total.add(thanhTien);` (CHÚ Ý: BigDecimal là bất biến, phải gán `total = total.add(...)`!)
     */
    public static BigDecimal bai2_tongGiaTriTonKho(List<ProductItem> items) {
        // TODO: Viết code của bạn ở đây
        return BigDecimal.ZERO;
    }

    /**
     * BÀI 3: Tìm sản phẩm có giá (price) cao nhất trong danh sách.
     * - Nếu items == null hoặc items.isEmpty() -> trả về null.
     * - Gợi ý: gán `ProductItem max = items.get(0);` rồi duyệt vòng lặp so sánh `item.price.compareTo(max.price) > 0`.
     */
    public static ProductItem bai3_timSanPhamDatNhat(List<ProductItem> items) {
        // TODO: Viết code của bạn ở đây
        return null;
    }

    /**
     * BÀI 4: Đếm có bao nhiêu sản phẩm thuộc danh mục `category` (không phân biệt hoa/thường).
     * - An toàn null: dùng `category != null && category.equalsIgnoreCase(item.category)`
     */
    public static int bai4_demSanPhamTheoDanhMuc(List<ProductItem> items, String category) {
        // TODO: Viết code của bạn ở đây
        return 0;
    }

    /**
     * BÀI 5: Lấy tập hợp các tên danh mục (category) KHÔNG trùng lặp.
     * - Bỏ qua nếu item.category == null hoặc rỗng sau khi trim().
     * - Gợi ý: tạo `Set<String> result = new HashSet<>();` rồi `result.add(item.category.trim());`
     */
    public static Set<String> bai5_layDanhSachDanhMucKhongTrung(List<ProductItem> items) {
        // TODO: Viết code của bạn ở đây
        return new HashSet<>();
    }

    /**
     * BÀI 6 (Nghiệp vụ import Excel): Kiểm tra danh sách mã SKU có bị trùng mã nào không.
     * - Trả về true nếu có ít nhất 1 mã bị lặp lại, false nếu tất cả đều duy nhất.
     * - Gợi ý: tạo `Set<String> seen = new HashSet<>();`
     *   Khi gọi `seen.add(sku)`: nếu trả về `false` nghĩa là mã đó ĐÃ CÓ trước đó -> return true ngay!
     */
    public static boolean bai6_kiemTraCoMaSkuTrungKhong(List<String> skuList) {
        // TODO: Viết code của bạn ở đây
        return false;
    }

    /**
     * BÀI 7: Gộp 2 danh sách mã SKU thành 1 List mới KHÔNG chứa mã trùng và GIỮ ĐÚNG THỨ TỰ xuất hiện.
     * - Gợi ý:
     *   Set<String> merged = new LinkedHashSet<>();
     *   merged.addAll(list1);
     *   merged.addAll(list2);
     *   return new ArrayList<>(merged);
     */
    public static List<String> bai7_gopHaiDanhSachKhongTrung(List<String> list1, List<String> list2) {
        // TODO: Viết code của bạn ở đây
        return new ArrayList<>();
    }

    /**
     * BÀI 8 (BẪY JAVA KINH ĐIỂN): Xóa toàn bộ sản phẩm có `stock <= 0` trực tiếp khỏi `items`
     * và trả về SỐ LƯỢNG phần tử đã xóa.
     * - CẤM dùng `for (ProductItem item : items) { items.remove(item); }` -> sẽ nổ lỗi `ConcurrentModificationException`!
     * - Cách chuẩn Java 8+: đếm trước (hoặc lấy `size` trước và sau), dùng `items.removeIf(item -> item.stock <= 0);`
     *   (Hoặc dùng `Iterator<ProductItem> it = items.iterator();` -> `while (it.hasNext()) { ... it.remove(); }`)
     */
    public static int bai8_xoaSanPhamHetHang(List<ProductItem> items) {
        // TODO: Viết code của bạn ở đây
        return 0;
    }

    private static void check(String tenBai, boolean passed) {
        System.out.println((passed ? "✅ PASS: " : "❌ FAIL: ") + tenBai);
    }
}
