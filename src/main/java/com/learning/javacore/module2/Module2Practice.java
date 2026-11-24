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
 * HƯỚNG DẪN CHẠY NHANH (không cần mạng):
 * java src/main/java/com/learning/javacore/module2/Module2Practice.java
 * (Hoặc: ./mvnw -o compile -q && java -cp target/classes
 * com.learning.javacore.module2.Module2Practice)
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
 * - Xóa phần tử thỏa điều kiện -> items.removeIf(item -> item.stock <= 0);
 * (CẤM gọi items.remove(item) trong vòng `for-each` vì sẽ nổ
 * ConcurrentModificationException!)
 * - new Set<string>() -> Set<String> set = new HashSet<>(); (không giữ thứ tự)
 * Set<String> set = new LinkedHashSet<>(); (giữ đúng thứ tự thêm vào)
 * - set.has(x) / set.add(x) -> set.contains(x) / set.add(x) (add trả về false
 * nếu x đã có!)
 *
 * NHIỆM VỤ:
 * Hoàn thành 12 bài bên dưới (các bài gắn 🪨 có gài bẫy thực tế) cho đến khi
 * toàn bộ in ✅ PASS!
 */
public class Module2Practice {

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
                check("Bài 1 - List null trả về rỗng",
                                List.of().equals(bai1_layTenSanPhamConHang(null)));

                // 38000*10 + 28000*0 + 65000*5 + 42000*2 = 380000 + 325000 + 84000 = 789000
                check("Bài 2 - Tổng giá trị tồn kho",
                                new BigDecimal("789000").compareTo(bai2_tongGiaTriTonKho(sample)) == 0);

                ProductItem maxProduct = bai3_timSanPhamDatNhat(sample);
                check("Bài 3 - Tìm SP đắt nhất", maxProduct != null && "SKU-03".equals(maxProduct.sku));
                check("Bài 3 - List rỗng trả null", bai3_timSanPhamDatNhat(List.of()) == null);
                List<ProductItem> withNullFirst = Arrays.asList(
                                new ProductItem("SKU-X", "SP lỗi giá", "Khác", null, 1),
                                new ProductItem("SKU-Y", "SP chuẩn", "Khác", "50000", 1));
                ProductItem maxWithNull = bai3_timSanPhamDatNhat(withNullFirst);
                check("Bài 3 - SP đầu bị null price không crash",
                                maxWithNull != null && "SKU-Y".equals(maxWithNull.sku));

                check("Bài 4 - Đếm SP theo danh mục", bai4_demSanPhamTheoDanhMuc(sample, "  sữa tươi ") == 2);

                Set<String> categories = bai5_layDanhSachDanhMucKhongTrung(sample);
                check("Bài 5 - Danh mục không trùng",
                                Set.of("Sữa tươi", "Sữa chua", "Phô mai").equals(categories));

                check("Bài 6 - Phát hiện SKU trùng (kể cả khoảng trắng/hoa thường)",
                                bai6_kiemTraCoMaSkuTrungKhong(List.of("A1", "A2", " a1 ")));
                check("Bài 6 - Không có SKU trùng",
                                !bai6_kiemTraCoMaSkuTrungKhong(List.of("A1", "A2", "A3")));

                check("Bài 7 - Gộp 2 list không trùng, giữ thứ tự",
                                List.of("A1", "A2", "A3", "A4").equals(
                                                bai7_gopHaiDanhSachKhongTrung(List.of("A1", "A2"),
                                                                List.of("A2", "A3", "A4"))));

                List<ProductItem> mutableList = new ArrayList<>(sample);
                mutableList.add(new ProductItem("SKU-05", "Nước ép cam", "Đồ uống", "15000", 0));
                int removed = bai8_xoaSanPhamHetHang(mutableList);
                check("Bài 8 - Xóa đúng 2 SP hết hàng", removed == 2 && mutableList.size() == 3);

                System.out.println("--- NÂNG CAO (BÀI 9 - 12 🪨) ---");

                List<String> expectedSkus = Arrays.asList("SKU-01", " sku-02 ", "SKU-03", "SKU-01", null);
                List<String> actualSkus = Arrays.asList("SKU-02", null, "  sku-01  ");
                check("Bài 9 🪨 - Tìm SKU thiếu trong kho",
                                List.of("SKU-03").equals(bai9_timMaSkuThieuTrongKho(expectedSkus, actualSkus)));
                check("Bài 9 🪨 - Kho trống thì thiếu hết (đã làm sạch & không trùng)",
                                List.of("SKU-01", "SKU-02", "SKU-03").equals(
                                                bai9_timMaSkuThieuTrongKho(
                                                                List.of("SKU-01", "sku-02", "SKU-01", "SKU-03"),
                                                                null)));

                check("Bài 10 🪨 - Lọc giá [38000.00 .. 65000.0]",
                                List.of("SKU-01", "SKU-03", "SKU-04").equals(
                                                bai10_locSanPhamTheoKhoangGia(sample, new BigDecimal("38000.00"),
                                                                new BigDecimal("65000.0"))));
                check("Bài 10 🪨 - Chỉ có maxPrice (minPrice = null)",
                                List.of("SKU-01", "SKU-02").equals(
                                                bai10_locSanPhamTheoKhoangGia(sample, null, new BigDecimal("38000"))));

                List<String> rawTags = Arrays.asList("  sua ", null, "   ", "SUA", "giam-gia", "moi", "hot");
                check("Bài 11 🪨 - Làm sạch tag & giới hạn 3",
                                List.of("SUA", "GIAM-GIA", "MOI").equals(bai11_lamSachVaGioiHanTags(rawTags, 3)));
                check("Bài 11 🪨 - limit lớn hơn số tag sạch",
                                List.of("SUA").equals(bai11_lamSachVaGioiHanTags(List.of(" sua ", "SUA"), 10)));
                check("Bài 11 🪨 - limit <= 0 trả list rỗng",
                                List.of().equals(bai11_lamSachVaGioiHanTags(rawTags, 0)));

                List<String> promoCategories = Arrays.asList(" sữa tươi ", "Đồ uống", null);
                check("Bài 12 🪨 - Tổng tiền nhóm KM (không giới hạn)",
                                new BigDecimal("464000")
                                                .compareTo(bai12_tinhTongTienNhomKhuyenMai(sample, promoCategories,
                                                                null)) == 0);
                check("Bài 12 🪨 - Tổng tiền nhóm KM bị chặn trần 300k",
                                new BigDecimal("300000").compareTo(
                                                bai12_tinhTongTienNhomKhuyenMai(sample, promoCategories,
                                                                new BigDecimal("300000.00"))) == 0);
        }

        /**
         * BÀI 1 (Lọc tên sản phẩm còn hàng): Lấy danh sách TÊN các sản phẩm có `stock >
         * 0`.
         * - Ngữ cảnh: Trang chủ chỉ hiển thị tên các sản phẩm đang còn hàng để khách
         * đặt mua.
         * - Ví dụ luồng dữ liệu:
         * + Đầu vào: [ ("Sữa tươi 180ml", stock=10), ("Sữa chua", stock=0), ("Phô mai
         * que", stock=5) ]
         * + Duyệt từng SP: giữ lại những SP có `stock > 0`
         * + Đầu ra: ["Sữa tươi 180ml", "Phô mai que"] (nếu `items == null` trả về list
         * rỗng `[]`).
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
         * BÀI 2 (Tổng giá trị tồn kho): Tính tổng `(item.price * item.stock)` của cả
         * danh sách.
         * - Ngữ cảnh: Báo cáo tài chính cuối ngày xem tổng giá trị hàng hóa đang nằm
         * trong kho.
         * - Ví dụ luồng dữ liệu:
         * + Đầu vào: [ ("SKU-01", 38000, stock=10), ("SKU-02", 28000, stock=0),
         * ("SKU-03", 65000, stock=5) ]
         * + Tính: 38000*10 + 28000*0 + 65000*5 = 705000
         * - Lưu ý: `BigDecimal` là bất biến, phải gán `total = total.add(thanhTien);`.
         */
        public static BigDecimal bai2_tongGiaTriTonKho(List<ProductItem> items) {
                BigDecimal total = BigDecimal.ZERO;
                if (items == null) {
                        return total;
                }
                for (ProductItem item : items) {
                        if (item != null && item.price != null && item.stock > 0
                                        && item.price.compareTo(BigDecimal.ZERO) > 0) {
                                total = total.add(item.price.multiply(BigDecimal.valueOf(item.stock)));
                        }
                }
                return total;
        }

        /**
         * BÀI 3 🪨 (Tìm sản phẩm đắt nhất): Trả về `ProductItem` có `price` cao nhất
         * trong danh sách.
         * - Ngữ cảnh: Hiển thị sản phẩm cao cấp nhất ở banner đầu danh mục.
         * - Ví dụ luồng dữ liệu:
         * + Đầu vào: [ SP1(38000), SP2(28000), SP3(65000) ] -> Đầu ra: SP3
         * + Đầu vào: [] hoặc null -> Đầu ra: null
         * - 🪨 Gợi ý: Nếu phần tử đầu tiên trong danh sách bị `price == null` thì sao?
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
         * BÀI 4 (Đếm sản phẩm theo danh mục): Đếm số SP thuộc `category` (không phân
         * biệt hoa/thường & dấu cách).
         * - Ngữ cảnh: Bộ lọc hiển thị số lượng sản phẩm đang khớp với danh mục khách
         * chọn.
         * - Ví dụ luồng dữ liệu:
         * + Đầu vào: items = [ SP1("Sữa tươi"), SP2("Sữa chua"), SP4("Sữa tươi") ],
         * category = " sữa tươi "
         * + Đầu ra: 2 (nếu `items == null` hoặc `category == null` -> trả về 0).
         */
        public static int bai4_demSanPhamTheoDanhMuc(List<ProductItem> items, String category) {
                String target = normalizeText(category);
                if (items == null || target.isEmpty()) {
                        return 0;
                }
                int count = 0;
                for (ProductItem item : items) {
                        if (item != null && target.equals(normalizeText(item.category))) {
                                count++;
                        }
                }
                return count;
        }

        /**
         * BÀI 5 (Lấy danh sách danh mục không trùng): Trả về `Set<String>` các tên danh
         * mục duy nhất.
         * - Ngữ cảnh: Lấy danh sách tab ngành hàng hiển thị lên thanh cuộn ngang của
         * app Mobile.
         * - Ví dụ luồng dữ liệu:
         * + Đầu vào: [ SP("Sữa tươi"), SP("Sữa chua"), SP("Phô mai"), SP("Sữa tươi"),
         * SP(null) ]
         * + Bước 1: Bỏ qua SP có `category == null` hoặc rỗng sau khi `.trim()`
         * + Bước 2: Đưa `item.category.trim()` vào `HashSet` (tự loại trùng)
         * + Đầu ra: {"Sữa tươi", "Sữa chua", "Phô mai"}
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
                        String trimmed = item.category.trim();
                        if (!trimmed.isEmpty()) {
                                result.add(trimmed);
                        }
                }
                return result;
        }

        /**
         * BÀI 6 (Kiểm tra trùng mã SKU khi import Excel): Trả về `true` nếu có ít nhất
         * 1 mã bị lặp lại.
         * - Ngữ cảnh: Admin upload file Excel nhập hàng; nếu có 2 dòng trùng SKU (vd
         * `"A1"` và `" a1 "`) thì chặn ngay.
         * - Ví dụ luồng dữ liệu:
         * + Đầu vào: ["A1", "A2", " a1 "] -> trim + IN HOA -> "A1" xuất hiện 2 lần ->
         * Đầu ra: true
         * + Đầu vào: ["A1", "A2", "A3"] -> tất cả duy nhất -> Đầu ra: false
         * - Công cụ: `Set<String> seen = new HashSet<>();` -> nếu `!seen.add(normal)`
         * thì `return true;` ngay!
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
         * BÀI 7 (Gộp 2 danh sách không trùng & giữ thứ tự): Trả về `List<String>` mới
         * từ `list1` + `list2`.
         * - Ngữ cảnh: Gộp danh sách "Sản phẩm vừa xem" (`list1`) và "Sản phẩm gợi ý"
         * (`list2`) để render FlatList,
         * không hiện trùng và giữ nguyên thứ tự ưu tiên từ `list1` sang `list2`.
         * - Ví dụ luồng dữ liệu:
         * + Đầu vào: list1 = ["A1", "A2"], list2 = ["A2", "A3", "A4"]
         * + Bước 1: Dùng `LinkedHashSet` để vừa loại trùng, vừa giữ thứ tự thêm vào
         * + Bước 2: Chuyển lại sang `new ArrayList<>(merged)`
         * + Đầu ra: ["A1", "A2", "A3", "A4"]
         */
        public static List<String> bai7_gopHaiDanhSachKhongTrung(List<String> list1, List<String> list2) {
                Set<String> merged = new LinkedHashSet<>();
                for (List<String> source : Arrays.asList(list1, list2)) {
                        if (source == null) {
                                continue;
                        }
                        for (String item : source) {
                                if (item != null && !item.trim().isEmpty()) {
                                        merged.add(item);
                                }
                        }
                }
                return new ArrayList<>(merged);
        }

        /**
         * BÀI 8 🪨 (Xóa sản phẩm hết hàng trực tiếp khỏi danh sách): Xóa SP có `stock
         * <= 0` khỏi `items`
         * và trả về SỐ LƯỢNG phần tử đã xóa.
         * - Ngữ cảnh: Dọn danh sách giỏ hàng tạm trước khi tính tiền, loại bỏ trực tiếp
         * các món đã hết hàng.
         * - Ví dụ luồng dữ liệu:
         * + Đầu vào: `items` gồm 5 SP, trong đó có 2 SP có `stock == 0`
         * + Sau khi chạy: `items` chỉ còn 3 SP còn hàng, và hàm trả về `2`.
         * - Công cụ: `items.removeIf(item -> item == null || item.stock <= 0);`
         * - 🪨 Gợi ý: Đừng xóa bằng `items.remove(item)` trong vòng `for (ProductItem
         * item : items)`!
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
         * BÀI 9 🪨 (Đối soát kiểm kho): Tìm các mã SKU có trong `expectedSkus` nhưng
         * THIẾU trong `actualSkus`.
         * - Ngữ cảnh: Cuối ngày kiểm kho, nhân viên quét mã thực tế trên kệ
         * (`actualSkus`), đối chiếu với
         * danh sách chuẩn hệ thống (`expectedSkus`) để tìm các mã bị thất lạc, giữ đúng
         * thứ tự và không trùng.
         * - Ví dụ luồng dữ liệu:
         * + expectedSkus = ["SKU-01", " sku-02 ", "SKU-03", "SKU-01", null]
         * + actualSkus = ["SKU-02", null, " sku-01 "]
         * + Bước 1: Làm sạch `actualSkus` (bỏ null/rỗng, trim + IN HOA) đưa vào
         * `HashSet` -> {"SKU-01", "SKU-02"}
         * + Bước 2: Duyệt `expectedSkus`, mã sạch nào chưa có trong `actualSet` -> đưa
         * vào `LinkedHashSet`
         * + Đầu ra: ["SKU-03"]
         * - 🪨 Gợi ý: Nếu `actualSkus == null`, đừng `return new
         * ArrayList<>(expectedSkus)` trực tiếp kẻo lọt dữ liệu chưa chuẩn hóa!
         */
        public static List<String> bai9_timMaSkuThieuTrongKho(List<String> expectedSkus, List<String> actualSkus) {
                if (expectedSkus == null || expectedSkus.isEmpty()) {
                        return new ArrayList<>();
                }
                Set<String> missingSkus = new LinkedHashSet<>();
                for (String expected : expectedSkus) {
                        String normal = normalizeText(expected);
                        if (!normal.isEmpty()) {
                                missingSkus.add(normal);
                        }
                }
                if (actualSkus != null) {
                        for (String actual : actualSkus) {
                                String normal = normalizeText(actual);
                                if (!normal.isEmpty()) {
                                        missingSkus.remove(normal);
                                }
                        }
                }
                return new ArrayList<>(missingSkus);
        }

        /**
         * BÀI 10 🪨 (Bộ lọc khoảng giá): Trả về danh sách `sku` có `price` nằm trong
         * `[minPrice, maxPrice]`.
         * - Ngữ cảnh: Người dùng kéo thanh lọc giá trên app Mobile (tính cả 2 đầu mút
         * `>= minPrice` và `<= maxPrice`).
         * - Ví dụ luồng dữ liệu:
         * + Đầu vào: sample, minPrice = 38000.00, maxPrice = 65000.0 -> Đầu ra:
         * ["SKU-01", "SKU-03", "SKU-04"]
         * + Đầu vào: sample, minPrice = null, maxPrice = 38000 -> Đầu ra: ["SKU-01",
         * "SKU-02"]
         * - 🪨 Gợi ý: Khách hàng có thể chỉ nhập 1 đầu giá (đầu kia là `null`), và
         * `38000` vs `38000.00` phải dùng `compareTo`!
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
                        if (item == null || item.price == null) {
                                continue;
                        }
                        String sku = normalizeText(item.sku);
                        if (sku.isEmpty()) {
                                continue;
                        }
                        boolean okMin = minPrice == null || item.price.compareTo(minPrice) >= 0;
                        boolean okMax = maxPrice == null || item.price.compareTo(maxPrice) <= 0;
                        if (okMin && okMax) {
                                result.add(sku);
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
         * - Công cụ: dùng `break;` khi `set.size() == limit`, hoặc `list.subList(0,
         * Math.min(limit, list.size()))`.
         */
        public static List<String> bai11_lamSachVaGioiHanTags(List<String> rawTags, int limit) {
                if (rawTags == null || limit <= 0 || rawTags.isEmpty()) {
                        return new ArrayList<>();
                }
                Set<String> cleanTags = new LinkedHashSet<>();
                for (String raw : rawTags) {
                        String normal = normalizeText(raw);
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
         * + Bước 1: Chuẩn hóa `promoCategories` (bỏ null/rỗng, trim + IN HOA) -> {"SỮA
         * TƯƠI", "ĐỒ UỐNG"}
         * + Bước 2: Duyệt từng SP còn hàng (`stock > 0`, `price != null`, `category !=
         * null`):
         * - SKU-01 ("Sữa tươi", 38000 * 10 = 380000): thuộc nhóm KM -> cộng 380000
         * - SKU-02 ("Sữa chua", stock = 0): bỏ qua
         * - SKU-03 ("Phô mai"): không nằm trong nhóm KM -> bỏ qua
         * - SKU-04 ("Sữa tươi", 42000 * 2 = 84000): thuộc nhóm KM -> cộng 84000
         * + Bước 3: Tổng = 464000. Vì `cap = 300000` (464000 > 300000) -> chặn trần,
         * trả về `300000`!
         * (Nếu `cap == null` thì không chặn trần, trả về nguyên `464000`.)
         * - 🪨 Gợi ý: Coi chừng `item.category == null` và đừng gọi
         * `promoCategories.contains(item.category)` trực tiếp!
         */
        public static BigDecimal bai12_tinhTongTienNhomKhuyenMai(
                        List<ProductItem> items, List<String> promoCategories, BigDecimal cap) {
                if (items == null || items.isEmpty() || promoCategories == null
                                || (cap != null && cap.compareTo(BigDecimal.ZERO) <= 0)) {
                        return BigDecimal.ZERO;
                }
                Set<String> promoSet = new HashSet<>();
                for (String category : promoCategories) {
                        String normal = normalizeText(category);
                        if (!normal.isEmpty()) {
                                promoSet.add(normal);
                        }
                }
                if (promoSet.isEmpty()) {
                        return BigDecimal.ZERO;
                }

                BigDecimal total = BigDecimal.ZERO;
                for (ProductItem item : items) {
                        if (item == null || item.stock <= 0 || item.price == null
                                        || !promoSet.contains(normalizeText(item.category))) {
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
