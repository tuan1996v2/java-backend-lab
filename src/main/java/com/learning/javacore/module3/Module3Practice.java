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
 *
 * BẢNG ĐỐI CHIẾU REACT NATIVE (JS/TS) -> JAVA:
 * - const map: Record<string, number> = {}  ->  Map<String, Integer> map = new HashMap<>(); (không giữ thứ tự)
 *   hoặc new Map<string, number>()          ->  Map<String, Integer> map = new LinkedHashMap<>(); (giữ đúng thứ tự put)
 * - map[k] = v / map.set(k, v)              ->  map.put(k, v); (ghi đè nếu key đã tồn tại)
 * - map[k] / map.get(k)                     ->  map.get(k) (trả về null nếu chưa có!)
 * - map[k] ?? 0                             ->  map.getOrDefault(k, 0) (an toàn tránh NullPointerException!)
 * - k in map / map.has(k)                   ->  map.containsKey(k)
 * - for (const [k, v] of Object.entries(m)) ->  for (Map.Entry<String, Integer> e : map.entrySet()) {
 *                                                   String k = e.getKey(); Integer v = e.getValue();
 *                                               }
 * - Object.keys(m) / Object.values(m)       ->  map.keySet() / map.values()
 * - Gom nhóm vào Map<String, List<T>>:
 *   JS: groups[k] ??= []; groups[k].push(x);
 *   Java: groups.computeIfAbsent(k, key -> new ArrayList<>()).add(x);
 * - Map hằng số chỉ đọc (Java 9+):
 *   Map.of("HN", 20000, "HCM", 25000) -> KHÔNG được put thêm và CẤM key/value là null!
 * - Cẩn thận với BigDecimal làm key của HashMap:
 *   new BigDecimal("38000") và new BigDecimal("38000.00") khác nhau với HashMap (vì khác số lẻ)!
 *   Muốn gộp chung giá trị, phải chuẩn hóa key bằng price.stripTrailingZeros() trước khi put/get!
 *
 * NHIỆM VỤ:
 * Hoàn thành 12 bài bên dưới (các bài gắn 🪨 có gài bẫy thực tế) cho đến khi toàn bộ in ✅ PASS!
 */
public class Module3Practice {

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
        // 38000 * 2 + 65000 * 1 = 141000
        check("Bài 9 🪨 - Tổng tiền đơn hàng tra từ catalog",
                new BigDecimal("141000").compareTo(bai9_tinhTongTienDonHang(orderLines, catalog)) == 0);

        // Bài 10 🪨
        Map<String, Integer> stockMap = Map.of("SKU-01", 5, "SKU-02", 0);
        Map<String, Integer> orderQty = new LinkedHashMap<>();
        orderQty.put("SKU-01", 3);   // Đủ (3 <= 5)
        orderQty.put("SKU-02", 1);   // Thiếu (1 > 0)
        orderQty.put("SKU-99", 2);   // Không có trong stockMap -> tồn = 0 -> Thiếu!
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
        check("Bài 12 - Freeship đơn >= 500k",
                BigDecimal.ZERO.compareTo(bai12_tinhPhiShipBangMap(new BigDecimal("500000"), "HN")) == 0);
    }

    /**
     * BÀI 1 (Tạo bảng tra cứu giá): Chuyển danh sách `ProductItem` thành `Map<String, BigDecimal>` (sku -> price).
     * - Ngữ cảnh: Khi khởi động app hoặc tính giỏ hàng, thay vì mỗi lần tìm giá phải quét cả list,
     *   ta dựng sẵn 1 bảng tra cứu `sku -> price` để tra trong O(1).
     * - Ví dụ luồng dữ liệu:
     *   + Đầu vào: [ (" sku-01 ", price=38000), ("SKU-02", price=28000), (null, price=10000) ]
     *   + Bước 1: Bỏ qua item == null, sku null/rỗng sau trim, hoặc price == null.
     *   + Bước 2: Chuẩn hóa sku (trim + IN HOA) -> đưa vào map: map.put("SKU-01", 38000)
     *   + Đầu ra: { "SKU-01": 38000, "SKU-02": 28000 }
     */
    public static Map<String, BigDecimal> bai1_taoBangGiaTheoSku(List<ProductItem> items) {
        // TODO: Viết code của bạn ở đây
        return new HashMap<>();
    }

    /**
     * BÀI 2 (Tra cứu an toàn với getOrDefault): Lấy giá theo `sku` từ `priceMap`.
     * - Ngữ cảnh: Frontend gửi lên mã `sku` cần báo giá (có thể gõ chữ thường, dính dấu cách hoặc sai mã).
     * - Ví dụ luồng dữ liệu:
     *   + Đầu vào: priceMap = {"SKU-01": 38000}, sku = "  sku-01 " -> Đầu ra: 38000
     *   + Đầu vào: priceMap = {"SKU-01": 38000}, sku = "SKU-999"   -> Đầu ra: 0 (BigDecimal.ZERO)
     * - Công cụ: `priceMap.getOrDefault(normalizedSku, BigDecimal.ZERO)`
     */
    public static BigDecimal bai2_traGiaSanPham(Map<String, BigDecimal> priceMap, String sku) {
        // TODO: Viết code của bạn ở đây
        return BigDecimal.ZERO;
    }

    /**
     * BÀI 3 (Đếm tần suất — bài kinh điển nhất của HashMap): Đếm số sản phẩm thuộc mỗi `category`.
     * - Ngữ cảnh: Trên màn hình bộ lọc danh mục của App, bên cạnh tên danh mục cần hiện số lượng
     *   (vd: "Sữa tươi (2)", "Sữa chua (1)").
     * - Ví dụ luồng dữ liệu:
     *   + Đầu vào: [ SP1("Sữa tươi"), SP2("Sữa chua"), SP3("Phô mai"), SP4("Sữa tươi") ]
     *   + Duyệt từng SP: `countMap.put(cat, countMap.getOrDefault(cat, 0) + 1);`
     *   + Đầu ra: { "Sữa tươi": 2, "Sữa chua": 1, "Phô mai": 1 }
     * - Lưu ý: Bỏ qua item == null hoặc category null/rỗng sau trim; giữ nguyên chữ hoa/thường sau trim.
     */
    public static Map<String, Integer> bai3_demSoLuongTheoDanhMuc(List<ProductItem> items) {
        // TODO: Viết code của bạn ở đây
        return new HashMap<>();
    }

    /**
     * BÀI 4 (Cộng dồn tồn kho theo danh mục): Tính TỔNG `stock` của từng `category`.
     * - Ngữ cảnh: Báo cáo tồn kho cho biết mỗi ngành hàng còn tổng cộng bao nhiêu sản phẩm trong kho.
     * - Ví dụ luồng dữ liệu:
     *   + Đầu vào: [ SP1("Sữa tươi", stock=10), SP2("Sữa chua", stock=0), SP4("Sữa tươi", stock=2) ]
     *   + Bước 1: Gặp SP1 -> "Sữa tươi" = 0 + 10 = 10
     *   + Bước 2: Gặp SP2 -> "Sữa chua" = 0 + 0 = 0
     *   + Bước 3: Gặp SP4 -> "Sữa tươi" = 10 + 2 = 12
     *   + Đầu ra: { "Sữa tươi": 12, "Sữa chua": 0 } (nếu item.stock < 0 thì coi như 0).
     */
    public static Map<String, Integer> bai4_tinhTongTonKhoTheoDanhMuc(List<ProductItem> items) {
        // TODO: Viết code của bạn ở đây
        return new HashMap<>();
    }

    /**
     * BÀI 5 (Cộng dồn tiền theo danh mục): Tính tổng giá trị tồn kho (`price * stock`) cho từng `category`.
     * - Ngữ cảnh: Kế toán muốn xem tổng tiền vốn đang nằm ở từng ngành hàng trên Dashboard.
     * - Ví dụ luồng dữ liệu:
     *   + Đầu vào: [ SP1("Sữa tươi", 38000 * 10), SP2("Sữa chua", 28000 * 0), SP4("Sữa tươi", 42000 * 2) ]
     *   + Đầu ra: { "Sữa tươi": 464000, "Sữa chua": 0 }
     * - Công cụ: `BigDecimal current = valueMap.getOrDefault(cat, BigDecimal.ZERO);`
     *            `valueMap.put(cat, current.add(itemValue));`
     */
    public static Map<String, BigDecimal> bai5_tinhGiaTriTonKhoTheoDanhMuc(List<ProductItem> items) {
        // TODO: Viết code của bạn ở đây
        return new HashMap<>();
    }

    /**
     * BÀI 6 🪨 (Gộp 2 giỏ hàng): Cộng dồn số lượng từ `cart1` và `cart2` (sku -> quantity) thành 1 Map mới.
     * - Ngữ cảnh: Khách chưa đăng nhập thêm hàng vào giỏ tạm (`cart2`), sau đó đăng nhập và gộp vào
     *   giỏ cũ trên tài khoản (`cart1`).
     * - Ví dụ luồng dữ liệu:
     *   + Đầu vào: cart1 = {"SKU-01": 2, "SKU-02": 1}, cart2 = {" sku-01 ": 3, "SKU-03": 4}
     *   + Bước 1: Chuẩn hóa sku (trim + IN HOA) và cộng dồn số lượng của cả 2 cart (chỉ cộng qty > 0).
     *   + Đầu ra: {"SKU-01": 5, "SKU-02": 1, "SKU-03": 4}
     * - 🪨 Gợi ý: ĐỪNG sửa trực tiếp lên `cart1` (như `cart1.put(...)`) — nếu bên gọi truyền `Map.of(...)`
     *   (chỉ đọc) vào thì chương trình sẽ nổ lỗi `UnsupportedOperationException` ngay!
     */
    public static Map<String, Integer> bai6_congDonGioHangTheoSku(
            Map<String, Integer> cart1, Map<String, Integer> cart2) {
        // TODO: Viết code của bạn ở đây
        return new HashMap<>();
    }

    /**
     * BÀI 7 (Duyệt Map bằng `entrySet()`): Tìm tên danh mục có số lượng sản phẩm nhiều nhất.
     * - Ngữ cảnh: Hiển thị badge "Ngành hàng nổi bật nhất" từ bảng thống kê `countByCategory`.
     * - Ví dụ luồng dữ liệu:
     *   + Đầu vào: {"Sữa tươi": 2, "Sữa chua": 1, "Phô mai": 1} -> Đầu ra: "Sữa tươi"
     *   + Đầu vào: null hoặc {} -> Đầu ra: null
     * - Công cụ: Duyệt `for (Map.Entry<String, Integer> entry : countByCategory.entrySet())`
     *            Lấy key = `entry.getKey()`, value = `entry.getValue()`.
     */
    public static String bai7_timDanhMucNhieuSanPhamNhat(Map<String, Integer> countByCategory) {
        // TODO: Viết code của bạn ở đây
        return null;
    }

    /**
     * BÀI 8 (Gom nhóm dữ liệu — Group By): Gom danh sách `sku` theo từng `category`.
     * - Ngữ cảnh: Trang chủ App hiển thị từng block theo ngành hàng:
     *   "Sữa tươi" -> ["SKU-01", "SKU-04"], "Phô mai" -> ["SKU-03"].
     * - Ví dụ luồng dữ liệu:
     *   + Đầu vào: [ SP("SKU-01", "Sữa tươi"), SP("SKU-03", "Phô mai"), SP("SKU-04", "Sữa tươi") ]
     *   + Đầu ra: { "Sữa tươi": ["SKU-01", "SKU-04"], "Phô mai": ["SKU-03"] }
     * - Công cụ (tương đương `groups[cat] ??= []; groups[cat].push(sku);` bên JS):
     *   `groups.computeIfAbsent(cat, k -> new ArrayList<>()).add(item.sku);`
     */
    public static Map<String, List<String>> bai8_gomNhomSkuTheoDanhMuc(List<ProductItem> items) {
        // TODO: Viết code của bạn ở đây
        return new LinkedHashMap<>();
    }

    /**
     * BÀI 9 🪨 (Tính tiền đơn hàng chuẩn O(1)): Tính tổng tiền của `orderLines` dựa trên bảng `catalog`.
     * - Ngữ cảnh: Khách bấm đặt hàng gồm nhiều dòng `(sku, quantity)`. Backend phải tra giá gốc từ
     *   `catalog` (KHÔNG tin giá từ client gửi lên!).
     * - Ví dụ luồng dữ liệu:
     *   + Đầu vào: orderLines = [("  sku-01 ", qty=2), ("SKU-03", qty=1), ("SKU-KHONG-CO", qty=5)]
     *   + Bước 1: Dựng `Map<String, BigDecimal>` (hoặc tái sử dụng `bai1_taoBangGiaTheoSku(catalog)`) để tra giá O(1).
     *   + Bước 2: Duyệt `orderLines` (bỏ qua dòng null hoặc `qty <= 0`), tìm giá theo `sku` đã chuẩn hóa;
     *             nếu không thấy trong catalog thì bỏ qua (giá = 0).
     *   + Đầu ra: 38000*2 + 65000*1 = 141000
     * - 🪨 Gợi ý: Đừng lồng 2 vòng `for (orderLines) { for (catalog) }` — nếu có 10.000 đơn x 10.000 SP sẽ treo API!
     */
    public static BigDecimal bai9_tinhTongTienDonHang(List<OrderLine> orderLines, List<ProductItem> catalog) {
        // TODO: Viết code của bạn ở đây
        return BigDecimal.ZERO;
    }

    /**
     * BÀI 10 🪨 (Kiểm tra tồn kho trước khi chốt đơn): Trả về danh sách `sku` KHÔNG ĐỦ HÀNG theo đúng thứ tự `orderQty`.
     * - Ngữ cảnh: Khách đặt mua các mã trong `orderQty` (`sku -> số lượng mua`). Cần đối chiếu với
     *   `stockMap` (`sku -> tồn kho hiện tại`) để báo lỗi những mã khách đặt vượt quá tồn kho.
     * - Ví dụ luồng dữ liệu:
     *   + Đầu vào: stockMap = {"SKU-01": 5, "SKU-02": 0}, orderQty = {"SKU-01": 3, "SKU-02": 1, "SKU-99": 2}
     *   + "SKU-01": đặt 3 <= tồn 5 (Đủ)
     *   + "SKU-02": đặt 1 > tồn 0 (Thiếu!)
     *   + "SKU-99": không có trong stockMap -> coi như tồn = 0 -> đặt 2 > 0 (Thiếu!)
     *   + Đầu ra: ["SKU-02", "SKU-99"]
     * - 🪨 Gợi ý: Nếu bạn viết `int tonKho = stockMap.get(sku);` khi `sku` không có trong `stockMap`,
     *   `stockMap.get("SKU-99")` trả về `null` và Java ép `null` sang `int` sẽ nổ `NullPointerException` ngay!
     */
    public static List<String> bai10_timSkuKhongDuHang(
            Map<String, Integer> stockMap, Map<String, Integer> orderQty) {
        // TODO: Viết code của bạn ở đây
        return new ArrayList<>();
    }

    /**
     * BÀI 11 🪨 (Thống kê sản phẩm theo mức giá — Bẫy BigDecimal làm Key):
     * - Ngữ cảnh: Thống kê xem mỗi mức giá đang có bao nhiêu sản phẩm (`price -> số lượng SP`).
     * - Ví dụ luồng dữ liệu:
     *   + Đầu vào: [ SP("38000"), SP("38000.00"), SP("65000.0") ]
     *   + Bước 1: Chuẩn hóa `item.price.stripTrailingZeros()` trước khi cho vào key của `HashMap`!
     *   + Đầu ra: { 38000: 2, 65000: 1 } (chỉ có 2 mức giá chứ KHÔNG phải 3!)
     * - 🪨 Gợi ý: `new BigDecimal("38000")` và `new BigDecimal("38000.00")` có `.equals()` là `FALSE`!
     *   Nên nếu bỏ thẳng `item.price` làm key của `HashMap`, chúng sẽ bị tách làm 2 dòng khác nhau!
     */
    public static Map<BigDecimal, Integer> bai11_thongKeSoLuongTheoMucGia(List<ProductItem> items) {
        // TODO: Viết code của bạn ở đây
        return new HashMap<>();
    }

    /**
     * BÀI 12 (Bảng cấu hình `Map.of` thay cho `switch-case` dài dòng): Tính phí ship theo mã tỉnh.
     * - Ngữ cảnh: Thay vì viết chuỗi `if/else` hoặc `switch` dài hàng chục tỉnh thành, backend lưu
     *   bảng phí ship dạng `Map<String, BigDecimal>` để tra cứu trong 1 dòng.
     * - Ví dụ luồng dữ liệu:
     *   + Bảng cấu hình: `Map.of("HN", new BigDecimal("20000"), "HCM", new BigDecimal("25000"), "DN", new BigDecimal("25000"))`
     *   + Đầu vào: (orderTotal = 200000, provinceCode = "  hn ") -> Đầu ra: 20000
     *   + Đầu vào: (orderTotal = 200000, provinceCode = "CAN_THO") -> không có trong bảng -> Đầu ra: 35000
     *   + Đầu vào: (orderTotal = 500000, provinceCode = "HN") -> >= 500_000 -> Đầu ra: 0 (Freeship)
     * - Công cụ: `shippingMap.getOrDefault(normalizedProvince, new BigDecimal("35000"))`
     */
    public static BigDecimal bai12_tinhPhiShipBangMap(BigDecimal orderTotal, String provinceCode) {
        // TODO: Viết code của bạn ở đây
        return BigDecimal.ZERO;
    }

    private static void check(String tenBai, boolean passed) {
        System.out.println((passed ? "✅ PASS: " : "❌ FAIL: ") + tenBai);
    }
}
