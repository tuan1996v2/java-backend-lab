package com.learning.javacore.module1;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;

/**
 * MODULE 1: String, So sánh (.equals vs ==), BigDecimal & Điều kiện (if/else,
 * switch)
 *
 * HƯỚNG DẪN CHẠY NHANH (không cần mạng):
 * java src/main/java/com/learning/javacore/module1/Module1Practice.java
 * (Hoặc: ./mvnw -o compile -q && java -cp target/classes
 * com.learning.javacore.module1.Module1Practice)
 *
 * BẢNG ĐỐI CHIẾU REACT NATIVE (JS/TS) -> JAVA:
 * - (s ?? "").trim().toUpperCase() -> s == null ? "" :
 * s.trim().toUpperCase(Locale.ROOT)
 * - s1 === s2 (so sánh chuỗi) -> "abc".equals(s1) hoặc
 * "abc".equalsIgnoreCase(s1)
 * (TUYỆT ĐỐI KHÔNG dùng `==` với String!)
 * - s.length -> s.length()
 * - s.startsWith("0") -> s.startsWith("0")
 * - s.indexOf("ORD-") -> s.indexOf("ORD-") (trả về -1 nếu không tìm thấy)
 * - s.slice(0, 3) / s.slice(-3) -> s.substring(0, 3)` / `s.substring(s.length()
 * - 3)
 * - regex.test(s) -> s.matches("0\\d{9}")
 * - a + b / a - b / a * b (tiền) -> a.add(b) / a.subtract(b) / a.multiply(b)
 * - a / b (tiền, làm tròn 2 số lẻ) -> a.divide(b, 2, RoundingMode.HALF_UP)
 * (BẮT BUỘC truyền đủ 3 tham số: số chia, số chữ số lẻ, kiểu làm tròn!)
 * - a >= b (so sánh BigDecimal) -> a.compareTo(b) >= 0 (trả về -1 / 0 / 1)
 * - switch (code) { ... } -> switch (code) { case "HN" -> ...; default -> ...;
 * }
 * (CHÚ Ý: phải chặn `code == null` trước khi vào `switch`!)
 *
 * NHIỆM VỤ:
 * Hoàn thành 12 bài bên dưới (các bài gắn 🪨 có gài bẫy thực tế) cho đến khi
 * toàn bộ in ✅ PASS!
 */
public class Module1Practice {

    public static void main(String[] args) {
        System.out.println("=== KIỂM TRA MODULE 1 ===");

        // Bài 1
        check("Bài 1 - Chuẩn hóa SKU", "TH-MILK-001".equals(bai1_chuanHoaSku("  th-milk-001  ")));
        check("Bài 1 - SKU null", "".equals(bai1_chuanHoaSku(null)));

        // Bài 2
        check("Bài 2 - So sánh chuỗi bằng nội dung", bai2_laDanhMucSuaTuoi(new String("Sữa tươi")));
        check("Bài 2 - Không phân biệt hoa thường", bai2_laDanhMucSuaTuoi("  SỮA TƯƠI "));
        check("Bài 2 - Null an toàn", !bai2_laDanhMucSuaTuoi(null));

        // Bài 3
        check("Bài 3 - Validate SĐT hợp lệ", bai3_laSoDienThoaiHopLe("0912345678"));
        check("Bài 3 - SĐT sai độ dài", !bai3_laSoDienThoaiHopLe("09123"));
        check("Bài 3 - SĐT không bắt đầu bằng 0", !bai3_laSoDienThoaiHopLe("8412345678"));
        check("Bài 3 - SĐT chứa chữ", !bai3_laSoDienThoaiHopLe("09123abc78"));

        // Bài 4 🪨
        check("Bài 4 🪨 - Che SĐT bình thường", "091****678".equals(bai4_cheSoDienThoai("0912345678")));
        check("Bài 4 🪨 - Chuỗi ngắn < 6 ký tự giữ nguyên", "123".equals(bai4_cheSoDienThoai("123")));
        check("Bài 4 🪨 - Null trả về chuỗi rỗng", "".equals(bai4_cheSoDienThoai(null)));

        // Bài 5 🪨
        check("Bài 5 🪨 - Tính tiền giảm 20%",
                new BigDecimal("80000.00").compareTo(bai5_tinhGiaSauGiam(new BigDecimal("100000"), 20)) == 0);
        check("Bài 5 🪨 - Giá lẻ chia không hết (99999 giảm 17%)",
                new BigDecimal("82999.17").compareTo(bai5_tinhGiaSauGiam(new BigDecimal("99999"), 17)) == 0);
        check("Bài 5 🪨 - Giảm > 100% hoặc null trả 0",
                BigDecimal.ZERO.compareTo(bai5_tinhGiaSauGiam(null, 20)) == 0);

        // Bài 6
        check("Bài 6 - Hạng KIM CUONG", "KIM CUONG".equals(bai6_xepHangThanhVien(new BigDecimal("10000000"))));
        check("Bài 6 - Hạng VANG", "VANG".equals(bai6_xepHangThanhVien(new BigDecimal("5000000"))));
        check("Bài 6 - Hạng BAC", "BAC".equals(bai6_xepHangThanhVien(new BigDecimal("1500000"))));
        check("Bài 6 - Hạng THUONG (null)", "THUONG".equals(bai6_xepHangThanhVien(null)));

        // Bài 7 🪨
        check("Bài 7 🪨 - Phí ship đơn >= 500k (freeship)",
                BigDecimal.ZERO.compareTo(bai7_tinhPhiShip(new BigDecimal("500000"), "HCM")) == 0);
        check("Bài 7 🪨 - Phí ship HN viết thường có dấu cách",
                new BigDecimal("20000").compareTo(bai7_tinhPhiShip(new BigDecimal("200000"), "  hn ")) == 0);
        check("Bài 7 🪨 - Phí ship tỉnh null mặc định 35k",
                new BigDecimal("35000").compareTo(bai7_tinhPhiShip(new BigDecimal("200000"), null)) == 0);

        // Bài 8
        check("Bài 8 - Format tóm tắt đơn",
                "Đơn #101 - Khách: Nguyễn Văn A - Tổng: 150000 đ".equals(
                        bai8_taoTieuDeDonHang(101L, "Nguyễn Văn A", new BigDecimal("150000"))));

        // Bài 9 🪨
        check("Bài 9 🪨 - Chia đơn giá không hết (100000 / 3)",
                new BigDecimal("33333.33").compareTo(bai9_tinhDonGiaTrungBinh(new BigDecimal("100000"), 3)) == 0);
        check("Bài 9 🪨 - Số lượng <= 0 trả về 0",
                BigDecimal.ZERO.compareTo(bai9_tinhDonGiaTrungBinh(new BigDecimal("100000"), 0)) == 0);

        // Bài 10
        check("Bài 10 - Trích mã đơn từ nội dung CK",
                "ORD-1024".equals(bai10_trichMaDonHang("Thanh toan don ORD-1024 gap")));
        check("Bài 10 - Không có mã đơn trả rỗng",
                "".equals(bai10_trichMaDonHang("Chuyen tien mua sua")));

        // Bài 11
        check("Bài 11 - Voucher hợp lệ", bai11_laVoucherHopLe("  sale-2026 "));
        check("Bài 11 - Voucher chứa khoảng trắng giữa", !bai11_laVoucherHopLe("SALE 2026"));
        check("Bài 11 - Voucher quá ngắn", !bai11_laVoucherHopLe("VIP1"));

        // Bài 12
        check("Bài 12 - Tổng thanh toán sau giảm và VAT 10%",
                new BigDecimal("99000.00").compareTo(
                        bai12_tinhTongThanhToan(new BigDecimal("100000"), new BigDecimal("10000"), 10)) == 0);
        check("Bài 12 - Giảm giá lớn hơn tiền hàng không bị âm",
                BigDecimal.ZERO.compareTo(
                        bai12_tinhTongThanhToan(new BigDecimal("50000"), new BigDecimal("80000"), 10)) == 0);
    }

    /**
     * BÀI 1 (Chuẩn hóa mã SKU): Làm sạch mã sản phẩm trước khi lưu xuống Database.
     * - Ngữ cảnh: Nhân viên nhập mã SKU trên form có thể gõ chữ thường hoặc dính
     * khoảng trắng 2 đầu.
     * - Ví dụ luồng dữ liệu:
     * + Đầu vào: " th-milk-001 " -> Bước 1 (trim): "th-milk-001" -> Bước 2 (IN
     * HOA): "TH-MILK-001"
     * + Đầu vào: null -> Đầu ra: ""
     */
    public static String bai1_chuanHoaSku(String sku) {
        return normalizeText(sku);
    }

    /**
     * BÀI 2 (So sánh nội dung chuỗi): Kiểm tra `category` có phải "Sữa tươi" hay
     * không.
     * - Ngữ cảnh: Lọc riêng nhóm "Sữa tươi" (không phân biệt hoa/thường, bỏ khoảng
     * trắng 2 đầu).
     * - Ví dụ luồng dữ liệu:
     * + Đầu vào: " SỮA TƯƠI " -> Đầu ra: true
     * + Đầu vào: new String("Sữa tươi") -> Đầu ra: true (không dùng `==`!)
     * + Đầu vào: null -> Đầu ra: false
     */
    public static boolean bai2_laDanhMucSuaTuoi(String category) {
        return category != null && "Sữa tươi".equalsIgnoreCase(category.trim());
    }

    /**
     * BÀI 3 (Validate SĐT Việt Nam): Kiểm tra số điện thoại nhập từ form đăng ký.
     * - Ngữ cảnh: SĐT hợp lệ phải khác null, dài đúng 10 ký tự, bắt đầu bằng '0' và
     * toàn bộ là chữ số.
     * - Ví dụ luồng dữ liệu:
     * + "0912345678" -> true
     * + "09123" -> false (thiếu số)
     * + "8412345678" -> false (không bắt đầu bằng 0)
     * + "09123abc78" -> false (chứa chữ)
     * - Công cụ: `phone.matches("0\\d{9}")` (hoặc kiểm tra độ dài +
     * `startsWith("0")` + vòng lặp `Character.isDigit`).
     */
    public static boolean bai3_laSoDienThoaiHopLe(String phone) {
        return phone != null && phone.matches("0\\d{9}");
    }

    /**
     * BÀI 4 🪨 (Che giữa SĐT trả ra API): Giữ 3 số đầu + "****" + 3 số cuối.
     * - Ngữ cảnh: Khi trả thông tin người mua về app Mobile, cần che bớt 4 số giữa
     * để bảo mật.
     * - Ví dụ luồng dữ liệu:
     * + "0912345678" -> "091****678"
     * + "123" (< 6 ký tự) -> trả nguyên "123"
     * + null -> trả ""
     * - Công cụ: `phone.substring(0, 3)` và `phone.substring(phone.length() - 3)`
     * - 🪨 Gợi ý: Coi chừng bị lỗi cắt chuỗi nếu SĐT ngắn hơn 6 ký tự hoặc bị
     * `null`!
     */
    public static String bai4_cheSoDienThoai(String phone) {
        if (phone == null) {
            return "";
        }
        if (phone.length() < 6) {
            return phone;
        }
        if (!bai3_laSoDienThoaiHopLe(phone)) {
            return "";
        }
        return phone.substring(0, 3) + "****" + phone.substring(phone.length() - 3);
    }

    /**
     * BÀI 5 🪨 (Tính giá sau giảm bằng BigDecimal): Tính `price * (100 -
     * discountPercent) / 100`.
     * - Ngữ cảnh: Sản phẩm đang giảm giá `discountPercent`% (0..100), cần tính giá
     * bán cuối làm tròn 2 chữ số.
     * - Ví dụ luồng dữ liệu:
     * + price = 100000, discountPercent = 20 -> 100000 * 80 / 100 = 80000.00
     * + price = 99999, discountPercent = 17 -> 99999 * 83 / 100 = 82999.17
     * + Nếu price == null hoặc discountPercent >= 100 -> trả về BigDecimal.ZERO
     * (nếu <= 0 giữ nguyên price).
     * - 🪨 Gợi ý: Hàm `divide(divisor, scale)` chỉ có 2 tham số trong Java rất dễ
     * làm tròn sai;
     * hãy truyền đủ 3 tham số với `RoundingMode.HALF_UP`!
     */
    public static BigDecimal bai5_tinhGiaSauGiam(BigDecimal price, int discountPercent) {
        if (price == null || discountPercent >= 100) {
            return BigDecimal.ZERO;
        }
        if (discountPercent <= 0) {
            return price;
        }
        return price.multiply(BigDecimal.valueOf(100 - discountPercent))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    /**
     * BÀI 6 (Xếp hạng thành viên): Phân loại hạng khách hàng theo tổng tiền đã chi
     * tiêu `totalSpent`.
     * - Ngữ cảnh: Tính badge hạng thành viên hiển thị trên màn hình Profile của
     * khách hàng.
     * - Quy tắc:
     * + >= 10_000_000 -> "KIM CUONG"
     * + >= 5_000_000 -> "VANG"
     * + >= 1_000_000 -> "BAC"
     * + Còn lại (hoặc null) -> "THUONG"
     * - Ví dụ: 10000000 -> "KIM CUONG"; 500000 -> "THUONG"; null -> "THUONG".
     */
    public static String bai6_xepHangThanhVien(BigDecimal totalSpent) {
        if (totalSpent == null) {
            return "THUONG";
        }
        if (totalSpent.compareTo(BigDecimal.valueOf(10_000_000)) >= 0) {
            return "KIM CUONG";
        }
        if (totalSpent.compareTo(BigDecimal.valueOf(5_000_000)) >= 0) {
            return "VANG";
        }
        if (totalSpent.compareTo(BigDecimal.valueOf(1_000_000)) >= 0) {
            return "BAC";
        }
        return "THUONG";
    }

    /**
     * BÀI 7 🪨 (Tính phí ship theo đơn & mã tỉnh): Áp dụng bảng phí giao hàng.
     * - Ngữ cảnh: Màn hình Checkout tính phí ship dựa trên tổng tiền hàng và tỉnh
     * thành khách chọn.
     * - Quy tắc:
     * + Nếu `orderTotal != null && orderTotal >= 500_000` -> `BigDecimal.ZERO`
     * (Freeship).
     * + Ngược lại xét `provinceCode`: "HN" -> 20000; "HCM" -> 25000; các tỉnh khác
     * -> 35000.
     * - Ví dụ: (200000, " hn ") -> 20000; (200000, null) -> 35000; (500000, "HCM")
     * -> 0.
     * - 🪨 Gợi ý: `switch (provinceCode)` sẽ nổ lỗi ngay nếu `provinceCode` bị
     * `null` hoặc viết thường có dấu cách!
     */
    public static BigDecimal bai7_tinhPhiShip(BigDecimal orderTotal, String provinceCode) {
        if (orderTotal != null && orderTotal.compareTo(BigDecimal.valueOf(500_000)) >= 0) {
            return BigDecimal.ZERO;
        }
        return switch (normalizeText(provinceCode)) {
            case "HN" -> BigDecimal.valueOf(20000);
            case "HCM" -> BigDecimal.valueOf(25000);
            default -> BigDecimal.valueOf(35000);
        };
    }

    /**
     * BÀI 8 (Format tiêu đề thông báo): Ghép thông tin tạo chuỗi tiêu đề Push
     * Notification.
     * - Ngữ cảnh: Khi khách đặt hàng xong, backend bắn thông báo về app Mobile theo
     * đúng format.
     * - Ví dụ luồng dữ liệu:
     * + Đầu vào: orderId = 101, customerName = "Nguyễn Văn A", total = 150000
     * + Đầu ra: "Đơn #101 - Khách: Nguyễn Văn A - Tổng: 150000 đ"
     */
    public static String bai8_taoTieuDeDonHang(Long orderId, String customerName, BigDecimal total) {
        return "Đơn #" + orderId + " - Khách: " + customerName + " - Tổng: " + total + " đ";
    }

    /**
     * BÀI 9 🪨 (Chia đơn giá trung bình mỗi sản phẩm): Tính `totalAmount /
     * quantity` làm tròn 2 chữ số lẻ.
     * - Ngữ cảnh: Kế toán muốn biết đơn giá trung bình mỗi món trong một combo
     * (`totalAmount` chia cho `quantity`).
     * - Ví dụ luồng dữ liệu:
     * + totalAmount = 100000, quantity = 3 -> 33333.33
     * + totalAmount = 100000, quantity = 0 (hoặc < 0, hoặc totalAmount == null) ->
     * trả về `BigDecimal.ZERO`
     * - 🪨 Gợi ý: Với `BigDecimal`, phép chia số không hết (100000 / 3 =
     * 33333.3333...) sẽ nổ lỗi ngay
     * nếu bạn quên chỉ định `scale` và `RoundingMode.HALF_UP`!
     */
    public static BigDecimal bai9_tinhDonGiaTrungBinh(BigDecimal totalAmount, int quantity) {
        if (quantity <= 0 || totalAmount == null || totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        return totalAmount.divide(BigDecimal.valueOf(quantity), 2, RoundingMode.HALF_UP);
    }

    /**
     * BÀI 10 (Trích mã đơn hàng từ nội dung chuyển khoản): Lấy mã dạng `ORD-XXXX`
     * (dài 8 ký tự).
     * - Ngữ cảnh: Khách chuyển khoản kèm ghi chú dài, hệ thống cần cắt đúng mã
     * `ORD-` + 4 ký tự liền sau để tự gạch nợ.
     * - Ví dụ luồng dữ liệu:
     * + Đầu vào: "Thanh toan don ORD-1024 gap"
     * + Bước 1: Tìm vị trí `"ORD-"` bằng `note.indexOf("ORD-")` -> thấy ở vị trí 15
     * + Bước 2: Kiểm tra còn đủ 8 ký tự từ vị trí đó không (`idx + 8 <=
     * note.length()`), rồi `substring(idx, idx + 8)`
     * + Đầu ra: "ORD-1024" (Nếu `note == null`, không thấy `"ORD-"`, hoặc không đủ
     * 8 ký tự -> trả về `""`)
     */
    public static String bai10_trichMaDonHang(String note) {
        String normal = normalizeText(note);
        int idx = normal.indexOf("ORD-");
        if (idx == -1 || idx + 8 > normal.length()) {
            return "";
        }
        return normal.substring(idx, idx + 8);
    }

    /**
     * BÀI 11 (Validate mã Voucher): Kiểm tra mã khuyến mãi người dùng nhập trên giỏ
     * hàng.
     * - Ngữ cảnh: Mã voucher hợp lệ sau khi `.trim()` và chuyển chữ IN HOA phải:
     * 1) Độ dài từ 5 đến 12 ký tự
     * 2) Chỉ gồm chữ cái A-Z, số 0-9 hoặc dấu gạch ngang `-` (không có khoảng trắng
     * ở giữa, không ký tự đặc biệt)
     * - Ví dụ luồng dữ liệu:
     * + " sale-2026 " -> trim + IN HOA -> "SALE-2026" (dài 9, hợp lệ) -> true
     * + "SALE 2026" -> có khoảng trắng ở giữa -> false
     * + "VIP1" -> dài 4 (< 5) -> false
     * - Công cụ: `normalized.matches("[A-Z0-9-]{5,12}")`
     */
    public static boolean bai11_laVoucherHopLe(String code) {
        return normalizeText(code).matches("[A-Z0-9-]{5,12}");
    }

    /**
     * BÀI 12 (Tính tổng tiền thanh toán cuối cùng gồm giảm giá + VAT):
     * - Ngữ cảnh: Màn hình Checkout tính số tiền khách thực trả:
     * `tongSauGiam = subtotal - discount` (nếu bị âm thì chặn về
     * `BigDecimal.ZERO`),
     * sau đó cộng thêm thuế VAT `vatPercent`% (`tongSauGiam * (100 + vatPercent) /
     * 100`), làm tròn 2 chữ số `HALF_UP`.
     * - Ví dụ luồng dữ liệu:
     * + subtotal = 100000, discount = 10000, vatPercent = 10
     * + Bước 1: 100000 - 10000 = 90000
     * + Bước 2: 90000 * 110 / 100 = 99000.00
     * + Nếu subtotal = 50000, discount = 80000 -> Bước 1 chặn về 0 -> trả về 0.
     * + Nếu subtotal == null -> trả về BigDecimal.ZERO (nếu discount == null coi
     * như 0).
     */
    public static BigDecimal bai12_tinhTongThanhToan(BigDecimal subtotal, BigDecimal discount, int vatPercent) {
        if (subtotal == null || subtotal.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal safeDiscount = (discount == null || discount.compareTo(BigDecimal.ZERO) < 0)
                ? BigDecimal.ZERO
                : discount;
        BigDecimal afterDiscount = subtotal.subtract(safeDiscount);
        if (afterDiscount.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        int safeVat = Math.max(vatPercent, 0);
        return afterDiscount.multiply(BigDecimal.valueOf(100 + safeVat))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    private static String normalizeText(String value) {
        return value == null ? "" : value.trim().toUpperCase(Locale.ROOT);
    }

    private static void check(String tenBai, boolean passed) {
        System.out.println((passed ? "✅ PASS: " : "❌ FAIL: ") + tenBai);
    }
}
