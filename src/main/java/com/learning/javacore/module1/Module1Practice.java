package com.learning.javacore.module1;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;

/**
 * MODULE 1: String, So sánh (.equals vs ==), BigDecimal & Điều kiện (if/else,
 * switch)
 *
 * HƯỚNG DẪN CHẠY NHANH (chỉ mất 1 giây):
 * - Cách 1: Bấm nút Run ▶️ ngay phía trên hàm main() bên dưới.
 * - Cách 2: Chạy trong Terminal:
 * ./mvnw -o compile && java -cp target/classes
 * com.learning.javacore.module1.Module1Practice
 *
 * NHIỆM VỤ:
 * Hoàn thành 8 hàm bên dưới (thay phần `// TODO` bằng code của bạn) cho đến khi
 * cả 8 bài đều in ✅ PASS!
 */
public class Module1Practice {

    public static void main(String[] args) {
        System.out.println("=== KIỂM TRA MODULE 1 ===");
        check("Bài 1 - Chuẩn hóa SKU", "TH-MILK-001".equals(bai1_chuanHoaSku("  th-milk-001  ")));
        check("Bài 1 - SKU null", "".equals(bai1_chuanHoaSku(null)));

        check("Bài 2 - So sánh chuỗi bằng nội dung", bai2_laDanhMucSuaTuoi(new String("Sữa tươi")));
        check("Bài 2 - Không phân biệt hoa thường", bai2_laDanhMucSuaTuoi("SỮA TƯƠI"));
        check("Bài 2 - Null an toàn", !bai2_laDanhMucSuaTuoi(null));

        check("Bài 3 - Validate SĐT hợp lệ", bai3_laSoDienThoaiHopLe("0912345678"));
        check("Bài 3 - SĐT sai độ dài", !bai3_laSoDienThoaiHopLe("09123"));
        check("Bài 3 - SĐT không bắt đầu bằng 0", !bai3_laSoDienThoaiHopLe("8412345678"));
        check("Bài 3 - SĐT chứa chữ", !bai3_laSoDienThoaiHopLe("09123abc78"));

        check("Bài 4 - Che SĐT", "091****678".equals(bai4_cheSoDienThoai("0912345678")));

        check("Bài 5 - Tính tiền giảm 20%",
                new BigDecimal("80000.00").compareTo(bai5_tinhGiaSauGiam(new BigDecimal("100000"), 20)) == 0);
        check("Bài 5 - Không giảm giá",
                new BigDecimal("38000.00").compareTo(bai5_tinhGiaSauGiam(new BigDecimal("38000"), 0)) == 0);

        check("Bài 6 - Hạng KIM CUONG", "KIM CUONG".equals(bai6_xepHangThanhVien(new BigDecimal("10000000"))));
        check("Bài 6 - Hạng VANG", "VANG".equals(bai6_xepHangThanhVien(new BigDecimal("5000000"))));
        check("Bài 6 - Hạng BAC", "BAC".equals(bai6_xepHangThanhVien(new BigDecimal("1500000"))));
        check("Bài 6 - Hạng THUONG", "THUONG".equals(bai6_xepHangThanhVien(new BigDecimal("500000"))));

        check("Bài 7 - Phí ship đơn >= 500k (freeship)",
                BigDecimal.ZERO.compareTo(bai7_tinhPhiShip(new BigDecimal("500000"), "HCM")) == 0);
        check("Bài 7 - Phí ship HN",
                new BigDecimal("20000").compareTo(bai7_tinhPhiShip(new BigDecimal("200000"), "HN")) == 0);
        check("Bài 7 - Phí ship HCM",
                new BigDecimal("25000").compareTo(bai7_tinhPhiShip(new BigDecimal("200000"), "HCM")) == 0);
        check("Bài 7 - Phí ship tỉnh khác",
                new BigDecimal("35000").compareTo(bai7_tinhPhiShip(new BigDecimal("200000"), "DA_NANG")) == 0);

        check("Bài 8 - Format tóm tắt đơn",
                "Đơn #101 - Khách: Nguyễn Văn A - Tổng: 150000 đ".equals(
                        bai8_taoTieuDeDonHang(101L, "Nguyễn Văn A", new BigDecimal("150000"))));
    }

    /**
     * BÀI 1: Chuẩn hóa mã SKU trước khi lưu DB.
     * - Nếu sku == null -> trả về "" (chuỗi rỗng).
     * - Ngược lại -> xóa khoảng trắng 2 đầu (.trim()) và chuyển thành CHỮ IN HOA
     * (.toUpperCase()).
     *
     * So sánh React Native/JS: (sku ?? "").trim().toUpperCase()
     */
    public static String bai1_chuanHoaSku(String sku) {
        // TODO: Viết code của bạn ở đây
        return sku == null ? "" : sku.toUpperCase(Locale.ROOT).trim();
    }

    /**
     * BÀI 2: Kiểm tra category có phải "Sữa tươi" không (không phân biệt
     * hoa/thường).
     * - CHÚ Ý BẪY JAVA: Không được dùng `==` để so sánh String!
     * (Trong JS dùng === được, nhưng trong Java `new String("Sữa tươi") == "Sữa
     * tươi"` trả về FALSE!)
     * - Nếu category == null -> trả về false (không để bị NullPointerException).
     *
     * Gợi ý hàm Java: `"Sữa tươi".equalsIgnoreCase(category.trim())` hoặc kiểm tra
     * null trước.
     */
    public static boolean bai2_laDanhMucSuaTuoi(String category) {
        // TODO: Viết code của bạn ở đây
        if (category == null)
            return false;
        return category.trim().equalsIgnoreCase("Sữa tươi");
    }

    /**
     * BÀI 3: Validate số điện thoại Việt Nam cơ bản:
     * - Không được null
     * - Độ dài đúng 10 ký tự: phone.length() == 10
     * - Bắt đầu bằng ký tự '0': phone.startsWith("0")
     * - Tất cả ký tự đều là số: gợi ý dùng vòng lặp for từng ký tự +
     * Character.isDigit(c),
     * HOẶC dùng regex `phone.matches("0\\d{9}")`
     */
    public static boolean bai3_laSoDienThoaiHopLe(String phone) {
        // TODO: Viết code của bạn ở đây
        if (phone == null || phone.length() != 10 || !phone.startsWith("0") || !phone.matches("0\\d{9}"))
            return false;
        return true;
    }

    /**
     * BÀI 4: Che giữa số điện thoại để trả ra API cho an toàn (vd: "0912345678" ->
     * "091****678").
     * - Gợi ý: dùng `phone.substring(0, 3)` lấy 3 số đầu,
     * và `phone.substring(phone.length() - 3)` lấy 3 số cuối, ghép `"****"` ở giữa.
     * - Nếu phone == null hoặc độ dài < 6 thì trả về chính nó (hoặc "" nếu null).
     *
     * So sánh JS: phone.slice(0, 3) + "****" + phone.slice(-3)
     */
    public static String bai4_cheSoDienThoai(String phone) {
        // TODO: Viết code của bạn ở đây
        if (phone == null || phone.length() < 6)
            return phone;
        if (bai3_laSoDienThoaiHopLe(phone)) {
            return phone.substring(0, 3) + "****" + phone.substring(phone.length() - 3);
        }
        return phone;
    }

    /**
     * BÀI 5: Tính giá sau giảm bằng BigDecimal (KHÔNG dùng double).
     * - Công thức: price * (100 - discountPercent) / 100, làm tròn 2 chữ số
     * (RoundingMode.HALF_UP).
     * - Gợi ý:
     * BigDecimal tyLe = BigDecimal.valueOf(100 - discountPercent);
     * return price.multiply(tyLe).divide(BigDecimal.valueOf(100), 2,
     * RoundingMode.HALF_UP);
     */
    public static BigDecimal bai5_tinhGiaSauGiam(BigDecimal price, int discountPercent) {
        // TODO: Viết code của bạn ở đây
        if (discountPercent == 0)
            return price;

        BigDecimal tyLe = BigDecimal.valueOf(100 - discountPercent);
        return price.multiply(tyLe).divide(BigDecimal.valueOf((100)), 2);
    }

    /**
     * BÀI 6: Xếp hạng khách hàng theo tổng chi tiêu (dùng BigDecimal.compareTo):
     * - CHÚ Ý BẪY JAVA: Với BigDecimal KHÔNG dùng dấu >=, mà dùng `a.compareTo(b)
     * >= 0`.
     * + >= 10_000_000 -> "KIM CUONG"
     * + >= 5_000_000 -> "VANG"
     * + >= 1_000_000 -> "BAC"
     * + Còn lại -> "THUONG"
     */
    public static String bai6_xepHangThanhVien(BigDecimal totalSpent) {
        // TODO: Viết code của bạn ở đây
        if (totalSpent == null)
            return "THUONG";
        if (totalSpent.compareTo(BigDecimal.valueOf(10_000_000)) >= 0)
            return "KIM CUONG";
        if (totalSpent.compareTo(BigDecimal.valueOf(5_000_000)) >= 0)
            return "VANG";
        if (totalSpent.compareTo(BigDecimal.valueOf(1_000_000)) >= 0)
            return "BAC";
        return "THUONG";
    }

    /**
     * BÀI 7: Tính phí ship theo tổng đơn và mã tỉnh (dùng if + switch):
     * - Nếu orderTotal >= 500_000 -> trả về BigDecimal.ZERO (Miễn phí ship)
     * - Ngược lại, xét provinceCode (switch):
     * + "HN" -> 20000
     * + "HCM" -> 25000
     * + Các tỉnh khác (default) -> 35000
     */
    public static BigDecimal bai7_tinhPhiShip(BigDecimal orderTotal, String provinceCode) {
        // TODO: Viết code của bạn ở đây

        if (orderTotal.compareTo(BigDecimal.valueOf(500_000)) >= 0)
            return BigDecimal.ZERO;
        if (provinceCode.equalsIgnoreCase("HN"))
            return BigDecimal.valueOf(20000);

        if (provinceCode.equalsIgnoreCase("HCM"))
            return BigDecimal.valueOf(25000);
        return BigDecimal.valueOf(35000);

    }

    /**
     * BÀI 8: Ghép chuỗi tạo tiêu đề thông báo đơn hàng (tương đương template
     * literal `${}` bên JS).
     * - Format chính xác: "Đơn #" + orderId + " - Khách: " + customerName + " -
     * Tổng: " + total + " đ"
     * - Gợi ý: dùng `String.format("Đơn #%d - Khách: %s - Tổng: %s đ", orderId,
     * customerName, total)`
     * hoặc nối chuỗi `+`.
     */
    public static String bai8_taoTieuDeDonHang(Long orderId, String customerName, BigDecimal total) {
        // TODO: Viết code của bạn ở đây

        return "Đơn #" + orderId + " - Khách: " + customerName + " - Tổng: " + total + " đ";
    }

    // Hàm hỗ trợ in kết quả test
    private static void check(String tenBai, boolean passed) {
        System.out.println((passed ? "✅ PASS: " : "❌ FAIL: ") + tenBai);
    }
}
