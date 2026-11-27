package com.learning.javacore.module4;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * MODULE 4: Enum, Class, Đóng gói (Encapsulation), equals/hashCode & Interface
 * (Đa hình)
 *
 * HƯỚNG DẪN CHẠY NHANH (< 1 giây, không cần mạng):
 * java src/main/java/com/learning/javacore/module4/Module4Practice.java
 *
 * BẢNG ĐỐI CHIẾU REACT NATIVE (TS/JS) -> JAVA (ĐẦY ĐỦ CÚ PHÁP DÙNG TRONG MODULE
 * 4):
 * 1. Type Union / Hằng số -> `enum`:
 * TS: type OrderStatus = 'PENDING' | 'PAID';
 * const LABELS = { PENDING: 'Chờ xác nhận', PAID: 'Đã thanh toán' };
 * Java: enum có thể chứa field + constructor + method ngay bên trong!
 * - `OrderStatus.PENDING.getLabel()` -> "Chờ xác nhận"
 * - `OrderStatus.values()` -> mảng tất cả giá trị enum (giống
 * `Object.values(...)`)
 * - `status.name()` -> tên chuỗi của enum (vd: OrderStatus.PAID.name() ->
 * "PAID")
 * - `switch (this) { case PENDING -> ...; }` -> xét trạng thái hiện tại của
 * enum
 *
 * 2. Làm sạch số `BigDecimal` & `int` trong Constructor (Bài 4, 7, 8, 9):
 * - Kiểm tra âm: `price.compareTo(BigDecimal.ZERO) < 0` (hoặc `price.signum() <
 * 0`)
 * - Trừ tiền: `orderTotal.subtract(discount)`
 * - Lấy nhỏ/lớn: `a.min(b)` (lấy số nhỏ hơn), `a.max(b)` (lấy số lớn hơn)
 * - Chặn số âm: `Math.max(0, quantity)`
 *
 * 3. Interface & Implements (Bài 7, 8, 9, 10, 12 — nền tảng Spring Boot
 * `Service` -> `ServiceImpl`):
 * TS: interface DiscountRule { calculateDiscount(total: BigDecimal):
 * BigDecimal; }
 * class PercentRule implements DiscountRule { ... }
 * Java: Dùng `implements` y chang TypeScript! Gắn `@Override` lên hàm cài đặt
 * lại.
 *
 * 4. So sánh 2 Object tự viết (`equals` & `hashCode`) — Bài 5 🪨 & Bài 6 🪨
 * (Vết sẹo S1 & S2):
 * - Mặc định `new ProductKey("A1").equals(new ProductKey("A1"))` là FALSE (so
 * địa chỉ RAM)!
 * - Kiểm tra cùng kiểu + ép biến (Java 17): `if (!(o instanceof ProductKey
 * other)) return false;`
 * - So sánh an toàn null: `Objects.equals(this.sku, other.sku)`
 * - Tạo mã băm cho HashSet/HashMap: `Objects.hash(this.sku)`
 * - 🪨 Tuyệt đối KHÔNG sửa field tính `hashCode` khi object đang nằm trong
 * `HashSet` (phải `remove` -> sửa -> `add` lại)!
 *
 * 5. Đóng gói & Defensive Copy (Bài 11 🪨 — Chống sửa ngầm List bên trong
 * Object):
 * - Copy khi nhận vào constructor: `this.lines = lines == null ? new
 * ArrayList<>() : new ArrayList<>(lines);`
 * - Trả bản chỉ đọc ở getter: `return
 * Collections.unmodifiableList(this.lines);`
 */
public class Module4Practice {

    public static void main(String[] args) {
        System.out.println("=== KIỂM TRA MODULE 4 ===");

        // Bài 1: Enum có label tiếng Việt
        check("Bài 1 - Enum PENDING label", "Chờ xác nhận".equals(OrderStatus.PENDING.getLabel()));
        check("Bài 1 - Enum PAID label", "Đã thanh toán".equals(OrderStatus.PAID.getLabel()));
        check("Bài 1 - Enum CANCELLED label", "Đã hủy".equals(OrderStatus.CANCELLED.getLabel()));

        // Bài 2: Chuyển trạng thái đơn hàng
        check("Bài 2 - PENDING -> PAID hợp lệ", OrderStatus.PENDING.canTransitionTo(OrderStatus.PAID));
        check("Bài 2 - PENDING -> COMPLETED không hợp lệ", !OrderStatus.PENDING.canTransitionTo(OrderStatus.COMPLETED));
        check("Bài 2 - CANCELLED -> PAID không hợp lệ", !OrderStatus.CANCELLED.canTransitionTo(OrderStatus.PAID));
        check("Bài 2 - PAID -> SHIPPED hợp lệ", OrderStatus.PAID.canTransitionTo(OrderStatus.SHIPPED));

        // Bài 3 🪨: Parse Enum an toàn từ API string
        check("Bài 3 🪨 - Parse ' paid ' ra PAID", OrderStatus.PAID == OrderStatus.fromCode(" paid "));
        check("Bài 3 🪨 - Mã rác 'UNKNOWN' trả null (không crash)", OrderStatus.fromCode("UNKNOWN") == null);
        check("Bài 3 🪨 - Null trả null", OrderStatus.fromCode(null) == null);

        // Bài 4: Đóng gói CartLine
        CartLine line = new CartLine("  sku-01 ", new BigDecimal("38000"), 3);
        check("Bài 4 - Chuẩn hóa SKU trong constructor", "SKU-01".equals(line.getSku()));
        check("Bài 4 - Thành tiền = 114000", new BigDecimal("114000").compareTo(line.getLineTotal()) == 0);
        CartLine badLine = new CartLine("SKU-02", new BigDecimal("-100"), -5);
        check("Bài 4 - Chặn giá/số lượng âm về 0",
                BigDecimal.ZERO.compareTo(badLine.getUnitPrice()) == 0 && badLine.getQuantity() == 0);

        // Bài 5 🪨 (Vết sẹo S1): equals & hashCode trên ProductKey
        Set<ProductKey> keySet = new HashSet<>();
        keySet.add(new ProductKey("SKU-01"));
        keySet.add(new ProductKey("  sku-01 "));
        check("Bài 5 🪨 - Set nhận ra 2 ProductKey cùng SKU là 1", keySet.size() == 1);
        check("Bài 5 🪨 - contains tìm thấy ProductKey mới cùng SKU", keySet.contains(new ProductKey("sku-01")));

        // Bài 6 🪨 (Vết sẹo S2): Đổi tên tag đang nằm trong HashSet
        Set<MutableTag> tagSet = new HashSet<>();
        MutableTag hotTag = new MutableTag("HOT");
        tagSet.add(hotTag);
        bai6_doiTenTagAnToanTrongSet(tagSet, hotTag, "BEST_SELLER");
        check("Bài 6 🪨 - Đổi tên xong Set vẫn tìm thấy hotTag",
                tagSet.size() == 1 && tagSet.contains(hotTag) && "BEST_SELLER".equals(hotTag.getName()));

        // Bài 7, 8, 9: Interface DiscountRule & Đa hình
        DiscountRule percentRule = new PercentDiscountRule(20, new BigDecimal("50000")); // giảm 20%, tối đa 50k
        DiscountRule fixedRule = new FixedAmountDiscountRule(new BigDecimal("40000"), new BigDecimal("150000")); // đơn
                                                                                                                 // >=
                                                                                                                 // 150k
                                                                                                                 // giảm
                                                                                                                 // 40k
        check("Bài 7 - Giảm 20% của 100k = 20k",
                new BigDecimal("20000.00").compareTo(percentRule.calculateDiscount(new BigDecimal("100000"))) == 0);
        check("Bài 7 - Giảm 20% của 500k bị chặn trần = 50k",
                new BigDecimal("50000.00").compareTo(percentRule.calculateDiscount(new BigDecimal("500000"))) == 0);
        check("Bài 8 - Đơn 100k < 150k không được giảm cố định",
                BigDecimal.ZERO.compareTo(fixedRule.calculateDiscount(new BigDecimal("100000"))) == 0);
        check("Bài 8 - Đơn 200k >= 150k được giảm 40k",
                new BigDecimal("40000").compareTo(fixedRule.calculateDiscount(new BigDecimal("200000"))) == 0);

        List<DiscountRule> allRules = List.of(percentRule, fixedRule);
        // Đơn 200k: percent giảm min(40k, 50k)=40k; đơn 300k: percent giảm min(60k,
        // 50k)=50k, fixed giảm 40k -> chọn 50k -> trả 250k
        check("Bài 9 - Chọn giảm giá tốt nhất (300k -> còn 250k)",
                new BigDecimal("250000.00")
                        .compareTo(bai9_tinhGiaSauGiamTotNhat(new BigDecimal("300000"), allRules)) == 0);

        // Bài 10: Interface Filter (Tiền đề Spring Specification)
        List<ProductData> products = List.of(
                new ProductData("SKU-01", "Sữa tươi", new BigDecimal("38000")),
                new ProductData("SKU-02", "Sữa chua", new BigDecimal("28000")),
                new ProductData("SKU-03", "Sữa tươi Organic", new BigDecimal("55000")));
        List<ProductSearchFilter> filters = List.of(
                new CategoryFilter("Sữa tươi"),
                new MaxPriceFilter(new BigDecimal("40000")));
        List<ProductData> filtered = bai10_locSanPham(products, filters);
        check("Bài 10 - Lọc đa hình (Sữa tươi và giá <= 40k)",
                filtered != null && filtered.size() == 1 && "SKU-01".equals(filtered.get(0).sku));

        // Bài 11 🪨: Defensive Copy (chống bên ngoài sửa ngầm đơn hàng)
        List<CartLine> mutableLines = new ArrayList<>();
        mutableLines.add(new CartLine("SKU-01", new BigDecimal("38000"), 2));
        OrderSnapshot snapshot = new OrderSnapshot(mutableLines);
        mutableLines.clear(); // Cố tình xóa list gốc bên ngoài!
        check("Bài 11 🪨 - Xóa list gốc không làm mất dòng hàng trong OrderSnapshot",
                snapshot.getLines().size() == 1);

        // Bài 12: Interface StockService + Implementation
        StockService stockService = new InMemoryStockService();
        stockService.addStock(" sku-01 ", 10);
        boolean okDeduct = stockService.deductStock("SKU-01", 4);
        boolean failDeduct = stockService.deductStock("SKU-01", 99);
        check("Bài 12 - Trừ kho hợp lệ thành công, còn 6",
                okDeduct && stockService.getStock("sku-01") == 6);
        check("Bài 12 - Trừ quá tồn kho trả false, giữ nguyên 6",
                !failDeduct && stockService.getStock("SKU-01") == 6);
    }

    /**
     * BÀI 1 (Enum có thuộc tính & hàm): Tạo trạng thái đơn hàng `OrderStatus` kèm
     * nhãn tiếng Việt.
     * - Ngữ cảnh thực tế: Trong DB/API ta lưu mã tiếng Anh ngắn gọn (`PENDING`,
     * `PAID`...),
     * nhưng khi trả về App Mobile hiển thị cho người dùng cần nhãn tiếng Việt
     * (`"Chờ xác nhận"`, `"Đã thanh toán"`).
     * Thay vì viết `Map` hay `switch` tách rời, Java cho phép gắn luôn trường
     * `label` vào từng giá trị Enum.
     * - Ví dụ luồng dữ liệu:
     * + `OrderStatus.PENDING.getLabel()` -> `"Chờ xác nhận"`
     * + `OrderStatus.CANCELLED.getLabel()` -> `"Đã hủy"`
     */
    enum OrderStatus {
        PENDING("Chờ xác nhận"),
        PAID("Đã thanh toán"),
        SHIPPED("Đang giao"),
        COMPLETED("Hoàn thành"),
        CANCELLED("Đã hủy");

        private final String label;

        OrderStatus(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        /**
         * BÀI 2 (Luật chuyển trạng thái đơn hàng): Kiểm tra đơn ở trạng thái hiện tại
         * (`this`)
         * có được phép chuyển sang trạng thái `next` hay không.
         * - Ngữ cảnh thực tế: Trên màn hình Admin/Shipper bấm đổi trạng thái đơn hàng,
         * Backend phải chặn các thao tác vô lý (ví dụ đơn đã hủy `CANCELLED` thì KHÔNG
         * thể
         * bấm chuyển lại sang `PAID`; đơn mới đặt `PENDING` thì chưa thể nhảy thẳng lên
         * `COMPLETED`).
         * - Quy tắc chuyển hợp lệ:
         * + `PENDING` -> chỉ sang được `PAID` hoặc `CANCELLED`
         * + `PAID` -> chỉ sang được `SHIPPED` hoặc `CANCELLED`
         * + `SHIPPED` -> chỉ sang được `COMPLETED`
         * + `COMPLETED`, `CANCELLED` -> trạng thái cuối, không sang được trạng thái nào
         * (`false`)
         * - Ví dụ luồng dữ liệu:
         * + `OrderStatus.PENDING.canTransitionTo(OrderStatus.PAID)` -> `true`
         * + `OrderStatus.PENDING.canTransitionTo(OrderStatus.COMPLETED)` -> `false`
         * + `OrderStatus.CANCELLED.canTransitionTo(OrderStatus.PAID)` -> `false`
         * + `OrderStatus.PENDING.canTransitionTo(null)` -> `false`
         * - Công cụ: Dùng `switch (this)` (bên trong Enum so sánh trực tiếp `next ==
         * PAID`).
         */
        public boolean canTransitionTo(OrderStatus next) {
            // TODO (Bài 2): Kiểm tra next != null và luật chuyển trạng thái (switch theo
            // `this`)
            if (next == null) {
                return false;
            }
            return switch (this) {
                case PENDING -> next == PAID || next == CANCELLED;
                case PAID -> next == SHIPPED || next == CANCELLED;
                case SHIPPED -> next == COMPLETED;
                case COMPLETED, CANCELLED -> false;
            };

        }

        /**
         * BÀI 3 🪨 (Parse chuỗi từ Client/API sang Enum an toàn): Chuyển chuỗi `code`
         * thành `OrderStatus`.
         * - Ngữ cảnh thực tế: Mobile/Web gửi query param `?status=paid` hoặc `?status=
         * abc ` lên API lọc đơn hàng.
         * Ta cần đổi chuỗi đó về đúng `OrderStatus` để xử lý nghiệp vụ; nếu client gửi
         * mã bậy (`"UNKNOWN"`)
         * hoặc `null` thì trả về `null` (để API báo lỗi 400 đẹp), KHÔNG để sập server!
         * - Ví dụ luồng dữ liệu:
         * + `OrderStatus.fromCode(" paid ")` -> bước 1: trim + IN HOA thành `"PAID"` ->
         * trả về `OrderStatus.PAID`
         * + `OrderStatus.fromCode("UNKNOWN")` -> không khớp cái nào -> trả về `null`
         * + `OrderStatus.fromCode(null)` -> trả về `null`
         * - 🪨 Bẫy: Hàm có sẵn `OrderStatus.valueOf("UNKNOWN")` sẽ NỔ LỖI
         * `IllegalArgumentException` làm crash!
         * Hãy duyệt `for (OrderStatus status : OrderStatus.values())` rồi so sánh
         * `status.name().equals(normalCode)`.
         */
        public static OrderStatus fromCode(String code) {
            if (code == null) {
                return null;
            }
            String clearCode = code.trim().toUpperCase(Locale.ROOT);
            for (OrderStatus status : OrderStatus.values()) {
                if (status.name().equals(clearCode)) {
                    return status;
                }
            }
            return null;
        }
    }

    /**
     * BÀI 4 (Đóng gói & làm sạch dữ liệu ngay trong Constructor): Hoàn thiện class
     * `CartLine`.
     * - Ngữ cảnh thực tế: Mỗi dòng trong giỏ hàng gồm `(sku, unitPrice, quantity)`.
     * Constructor chịu trách nhiệm làm sạch & chuẩn hóa toàn bộ input ngay lúc
     * `new CartLine(...)` để các hàm phía sau không phải kiểm tra lại.
     * - Yêu cầu cụ thể:
     * 1. `sku`: nếu `null` -> gán `""`; ngược lại cắt khoảng trắng 2 đầu VÀ chuyển
     * IN HOA (`sku.trim().toUpperCase(Locale.ROOT)`).
     * 2. `unitPrice`: nếu `null` hoặc `<= 0` (`unitPrice.compareTo(BigDecimal.ZERO)
     * <= 0`)
     * -> gán `BigDecimal.ZERO`.
     * 3. `quantity`: nếu `< 0` -> gán `0`.
     * 4. `getLineTotal()`: trả về `unitPrice * quantity`
     * (`this.unitPrice.multiply(BigDecimal.valueOf(this.quantity))`).
     * - Ví dụ luồng dữ liệu:
     * + `new CartLine(" sku-01 ", 38000, 3)` -> `sku = "SKU-01"`, `unitPrice =
     * 38000`, `quantity = 3`, `getLineTotal() = 114000`
     * + `new CartLine("SKU-02", -100, -5)` -> `unitPrice = 0`, `quantity = 0`,
     * `getLineTotal() = 0`
     * + `new CartLine(null, null, 2)` -> `sku = ""`, `unitPrice = 0`, `quantity =
     * 2`
     */
    static class CartLine {
        private final String sku;
        private final BigDecimal unitPrice;
        private final int quantity;

        public CartLine(String sku, BigDecimal unitPrice, int quantity) {
            String clearSku = sku == null ? "" : sku.trim().toUpperCase(Locale.ROOT);
            BigDecimal price = (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) <= 0) ? BigDecimal.ZERO
                    : unitPrice;
            int qty = quantity <= 0 ? 0 : quantity;
            this.sku = clearSku;
            this.unitPrice = price;
            this.quantity = qty;
        }

        public String getSku() {
            return sku;
        }

        public BigDecimal getUnitPrice() {
            return unitPrice;
        }

        public int getQuantity() {
            return quantity;
        }

        public BigDecimal getLineTotal() {
            return this.unitPrice.multiply(BigDecimal.valueOf(this.quantity));
        }
    }

    /**
     * BÀI 5 🪨 (Vết sẹo S1 — equals & hashCode):
     * - Ngữ cảnh thực tế: Hai object `new ProductKey("SKU-01")` và
     * `new ProductKey(" sku-01 ")` đại diện cho CÙNG 1 sản phẩm khi bỏ vào
     * `HashSet` hoặc làm key của `HashMap`.
     * - Yêu cầu cụ thể:
     * 1. Constructor `ProductKey(String sku)`: nếu `sku == null` -> `""`; ngược lại
     * `sku.trim().toUpperCase(Locale.ROOT)`.
     * 2. Override `equals(Object o)`: trả `true` nếu 2 `ProductKey` có cùng
     * `this.sku`
     * (`Objects.equals(this.sku, other.sku)`).
     * 3. Override `hashCode()`: trả `Objects.hash(this.sku)` (bắt buộc luôn đi kèm
     * khi override `equals`, nếu không `HashSet`/`HashMap` vẫn coi là 2 object khác
     * nhau!).
     * - Ví dụ luồng dữ liệu:
     * + Thêm `new ProductKey("SKU-01")` rồi `new ProductKey(" sku-01 ")` vào
     * `HashSet` -> `set.size() == 1`.
     * + `set.contains(new ProductKey("sku-01"))` -> `true`.
     */
    static class ProductKey {
        private final String sku;

        public ProductKey(String sku) {
            String clearSku = sku == null ? "" : sku.trim().toUpperCase(Locale.ROOT);
            this.sku = clearSku;
        }

        public String getSku() {
            return sku;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (!(o instanceof ProductKey other))
                return false; // Không phải ProductKey -> false (Java 17 tự ép biến `other`)
            return Objects.equals(this.sku, other.sku);
        }
    }

    /**
     * BÀI 6 🪨 (Vết sẹo S2 — Không đổi key hashCode khi object đang nằm trong
     * HashSet):
     * - Ngữ cảnh thực tế: Cần đổi tên của `tag` từ `"HOT"` sang `newName` (vd
     * `"BEST_SELLER"`)
     * khi `tag` đang nằm bên trong `tagSet`.
     * - Yêu cầu cụ thể:
     * 1. Kiểm tra null an toàn (`if (tagSet == null || tag == null) return;`).
     * 2. Xóa `tag` khỏi `tagSet` trước -> gọi `tag.setName(newName)` -> thêm `tag`
     * lại vào `tagSet`.
     * - 🪨 Lưu ý cơ chế Java: Nếu gọi thẳng `tag.setName(newName)` khi `tag` vẫn
     * nằm trong `HashSet`,
     * `hashCode()` thay đổi nhưng vị trí bucket cũ trong `HashSet` không tự dời
     * theo -> `tagSet.contains(tag)` trả `false`!
     * - Ví dụ luồng dữ liệu:
     * + `tagSet = [MutableTag("HOT")]`, gọi `bai6_doiTenTagAnToanTrongSet(tagSet,
     * hotTag, "BEST_SELLER")`
     * + Kết quả: `tagSet.size() == 1`, `hotTag.getName()` là `"BEST_SELLER"`,
     * `tagSet.contains(hotTag) == true`.
     */
    static class MutableTag {
        private String name;

        public MutableTag(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (!(o instanceof MutableTag other))
                return false;
            return Objects.equals(name, other.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }

    public static void bai6_doiTenTagAnToanTrongSet(Set<MutableTag> tagSet, MutableTag tag, String newName) {
        // TODO (Bài 6 🪨): Bỏ ra khỏi Set -> đổi tên -> thêm lại vào Set
    }

    /**
     * BÀI 7, 8, 9: Interface `DiscountRule` & Đa hình (Polymorphism)
     */
    interface DiscountRule {
        BigDecimal calculateDiscount(BigDecimal orderTotal);
    }

    /**
     * BÀI 7: Giảm theo % có trần tối đa (`maxDiscount`) — implement `DiscountRule`.
     * - Ngữ cảnh thực tế: Mã voucher `"GIẢM 20%, TỐI ĐA 50K"` khi thanh toán đơn
     * hàng.
     * - Yêu cầu cụ thể:
     * 1. Nếu `orderTotal == null` hoặc `orderTotal.compareTo(BigDecimal.ZERO) <= 0`
     * hoặc `percent <= 0` -> trả `BigDecimal.ZERO`.
     * 2. Tính tiền giảm =
     * `orderTotal.multiply(BigDecimal.valueOf(percent)).divide(BigDecimal.valueOf(100),
     * 2, RoundingMode.HALF_UP)`.
     * 3. Nếu `maxDiscount != null` và `maxDiscount.compareTo(BigDecimal.ZERO) >= 0`
     * và `discount.compareTo(maxDiscount) > 0` -> chặn trần, trả về `maxDiscount`.
     * 4. Lưu ý: Tiền giảm không được vượt quá `orderTotal` (nếu `> orderTotal` thì
     * trả `orderTotal`).
     * - Ví dụ luồng dữ liệu (với `percent = 20`, `maxDiscount = 50000`):
     * + `orderTotal = 100000` -> 20% = `20000.00` (nhỏ hơn trần 50k) -> trả
     * `20000.00`.
     * + `orderTotal = 500000` -> 20% = `100000.00` (vượt trần 50k) -> trả `50000`.
     */
    static class PercentDiscountRule implements DiscountRule {
        private final int percent;
        private final BigDecimal maxDiscount;

        public PercentDiscountRule(int percent, BigDecimal maxDiscount) {
            this.percent = percent;
            this.maxDiscount = maxDiscount;
        }

        @Override
        public BigDecimal calculateDiscount(BigDecimal orderTotal) {
            // TODO (Bài 7): Nếu orderTotal == null hoặc <= 0 hoặc percent <= 0 trả
            // BigDecimal.ZERO.
            // Tính: orderTotal * percent / 100, lấy tối đa maxDiscount (nếu maxDiscount !=
            // null).
            return BigDecimal.ZERO;
        }
    }

    /**
     * BÀI 8: Giảm số tiền cố định nếu đơn đủ giá trị tối thiểu, cài đặt
     * `DiscountRule`.
     * - Ngữ cảnh thực tế: Mã voucher kiểu `"Giảm 40k cho đơn từ 150k"`.
     * - Yêu cầu cụ thể:
     * 1. Nếu `orderTotal == null || discountAmount == null` hoặc một trong hai `<=
     * 0` -> trả `BigDecimal.ZERO`.
     * 2. Nếu `minOrderTotal != null && orderTotal.compareTo(minOrderTotal) < 0` ->
     * chưa đủ ngưỡng -> trả `BigDecimal.ZERO`.
     * 3. Nếu đủ điều kiện (`orderTotal >= minOrderTotal`): trả về `discountAmount`,
     * nhưng nếu `discountAmount > orderTotal` thì chỉ giảm tối đa bằng
     * `orderTotal`.
     * 4. Dùng `compareTo(...)` để so sánh `BigDecimal`, KHÔNG dùng `<` hay `==`.
     * - Ví dụ luồng dữ liệu (`discountAmount = 40000`, `minOrderTotal = 150000`):
     * + `orderTotal = 100000` (< 150000) -> trả về `BigDecimal.ZERO`.
     * + `orderTotal = 200000` (>= 150000) -> trả về `40000`.
     */
    static class FixedAmountDiscountRule implements DiscountRule {
        private final BigDecimal discountAmount;
        private final BigDecimal minOrderTotal;

        public FixedAmountDiscountRule(BigDecimal discountAmount, BigDecimal minOrderTotal) {
            this.discountAmount = discountAmount;
            this.minOrderTotal = minOrderTotal;
        }

        @Override
        public BigDecimal calculateDiscount(BigDecimal orderTotal) {
            // TODO (Bài 8): So sánh BigDecimal bằng compareTo (không dùng ==), kiểm tra
            // null an toàn.
            return BigDecimal.ZERO;
        }
    }

    /**
     * BÀI 9: Áp dụng đa hình `DiscountRule` — tính giá cuối cùng sau khi chọn 1
     * rule
     * giảm nhiều tiền nhất từ danh sách `rules`.
     * - Ngữ cảnh thực tế: Giỏ hàng có nhiều mã khuyến mãi (giảm %, giảm cố
     * định...),
     * đều cùng cài `interface DiscountRule`. Hệ thống chỉ lặp qua `rules`, gọi
     * `rule.calculateDiscount(orderTotal)` để tìm mức giảm lớn nhất mà không cần
     * `if/else`.
     * - Yêu cầu cụ thể:
     * 1. Nếu `orderTotal == null || orderTotal.compareTo(BigDecimal.ZERO) <= 0` ->
     * trả `BigDecimal.ZERO`.
     * 2. Nếu `rules == null || rules.isEmpty()` -> không có mã giảm -> trả nguyên
     * `orderTotal`.
     * 3. Duyệt từng `rule` trong `rules` (bỏ qua nếu `rule == null`), tìm
     * `maxDiscount` lớn nhất (so sánh bằng `compareTo`).
     * 4. Trả về `orderTotal.subtract(maxDiscount)`; nếu kết quả `< 0` thì chặn về
     * `BigDecimal.ZERO`.
     * - Ví dụ luồng dữ liệu:
     * + `orderTotal = 300000`, `rules = [Percent(20%, max 50k), Fixed(40k, min
     * 150k)]`
     * + Percent giảm `50000`, Fixed giảm `40000` -> chọn giảm lớn nhất là `50000`
     * + Đầu ra: `300000 - 50000 = 250000`.
     */
    public static BigDecimal bai9_tinhGiaSauGiamTotNhat(BigDecimal orderTotal, List<DiscountRule> rules) {
        // TODO (Bài 9): Duyệt rules tìm maxDiscount (chặt null cho rules và từng rule),
        // lấy orderTotal trừ maxDiscount (không để âm).
        return BigDecimal.ZERO;
    }

    /**
     * BÀI 10: Interface Filter — tiền đề cho Spring Data JPA Specification.
     * - Ngữ cảnh thực tế: Màn hình tìm kiếm sản phẩm cho phép lọc linh hoạt theo
     * nhiều tiêu chí (danh mục, giá tối đa...). Mỗi bộ lọc đóng gói thành 1 class
     * cài `ProductSearchFilter`.
     * - Yêu cầu cụ thể:
     * 1. `CategoryFilter.matches(product)`: trả `true` nếu `product != null`,
     * `product.category != null`,
     * `expectedCategory != null` và
     * `product.category.trim().equalsIgnoreCase(expectedCategory.trim())`.
     * 2. `MaxPriceFilter.matches(product)`: trả `true` nếu `product != null`,
     * `product.price != null`,
     * `maxPrice != null` và `product.price.compareTo(maxPrice) <= 0`.
     * 3. `bai10_locSanPham(products, filters)`: nếu `products == null` trả
     * `List.of()`;
     * giữ lại từng `product != null` thỏa mãn TẤT CẢ filter không null trong
     * `filters` (AND logic).
     * - Ví dụ luồng dữ liệu:
     * + `products = [ ("SKU-01", "Sữa tươi", 38000), ("SKU-02", "Sữa chua", 28000),
     * ("SKU-03", "Sữa tươi Organic", 55000) ]`
     * + `filters = [ CategoryFilter("Sữa tươi"), MaxPriceFilter(40000) ]`
     * + `"SKU-01"` thỏa cả 2 filter -> GIỮ; `"SKU-02"` sai category -> BỎ;
     * `"SKU-03"` sai category & vượt giá -> BỎ.
     * + Đầu ra: `[ ("SKU-01", "Sữa tươi", 38000) ]`.
     */
    static class ProductData {
        final String sku;
        final String category;
        final BigDecimal price;

        ProductData(String sku, String category, BigDecimal price) {
            this.sku = sku;
            this.category = category;
            this.price = price;
        }
    }

    interface ProductSearchFilter {
        boolean matches(ProductData product);
    }

    static class CategoryFilter implements ProductSearchFilter {
        private final String expectedCategory;

        CategoryFilter(String expectedCategory) {
            this.expectedCategory = expectedCategory;
        }

        @Override
        public boolean matches(ProductData product) {
            // TODO (Bài 10): Trả true nếu product != null và category khớp
            // (equalsIgnoreCase / equals an toàn null).
            return false;
        }
    }

    static class MaxPriceFilter implements ProductSearchFilter {
        private final BigDecimal maxPrice;

        MaxPriceFilter(BigDecimal maxPrice) {
            this.maxPrice = maxPrice;
        }

        @Override
        public boolean matches(ProductData product) {
            // TODO (Bài 10): Trả true nếu product != null, price != null, maxPrice != null
            // và price <= maxPrice.
            return false;
        }
    }

    public static List<ProductData> bai10_locSanPham(List<ProductData> products, List<ProductSearchFilter> filters) {
        // TODO (Bài 10): Trả về danh sách sản phẩm thỏa mãn TẤT CẢ filter trong
        // `filters`.
        return List.of();
    }

    /**
     * BÀI 11 🪨 (Defensive Copy — Bảo vệ List bên trong Object):
     * - Ngữ cảnh thực tế: Sau khi chốt đơn tạo `OrderSnapshot`, không muốn đoạn
     * code
     * bên ngoài lỡ gọi `mutableList.clear()` hay `snapshot.getLines().add(...)` làm
     * biến dạng dữ liệu đơn hàng đã lưu (giống clone array `[...lines]` trong
     * React/JS).
     * - Yêu cầu cụ thể:
     * 1. Constructor `OrderSnapshot(List<CartLine> lines)`: copy sang `ArrayList`
     * mới
     * (`this.lines = (lines == null) ? new ArrayList<>() : new
     * ArrayList<>(lines);`),
     * KHÔNG gán trực tiếp `this.lines = lines;`.
     * 2. `getLines()`: trả về list chỉ đọc `Collections.unmodifiableList(lines)`
     * (hoặc `List.copyOf(lines)`)
     * để code bên ngoài không thể `.add()`/`.remove()`/`clear()`.
     * - Ví dụ luồng dữ liệu:
     * + Tạo `mutableLines` có 1 món -> `snapshot = new OrderSnapshot(mutableLines)`
     * + Bên ngoài gọi `mutableLines.clear()`
     * + `snapshot.getLines().size()` VẪN PHẢI LÀ `1`.
     */
    static class OrderSnapshot {
        private final List<CartLine> lines;

        public OrderSnapshot(List<CartLine> lines) {
            // TODO (Bài 11 🪨): Copy ra ArrayList mới (không gán trực tiếp `this.lines =
            // lines;`)
            this.lines = lines;
        }

        public List<CartLine> getLines() {
            // TODO (Bài 11 🪨): Trả về list chỉ đọc (unmodifiableList) để bên ngoài không
            // add/remove được
            return lines;
        }
    }

    /**
     * BÀI 12: Interface StockService & InMemoryStockService — Quản lý tồn kho chuẩn
     * hóa SKU.
     * - Ngữ cảnh thực tế: Kho hàng cần 3 thao tác: nhập kho (`addStock`),
     * xuất/trừ kho (`deductStock`), và xem tồn (`getStock`).
     * - Yêu cầu cụ thể:
     * 1. Chuẩn hóa SKU ở CẢ 3 hàm (`addStock`, `deductStock`, `getStock`):
     * `sku == null ? "" : sku.trim().toUpperCase(Locale.ROOT)`.
     * Nếu SKU sau chuẩn hóa bị rỗng (`isEmpty()`): `addStock` bỏ qua, `deductStock`
     * trả `false`, `getStock` trả `0`.
     * 2. `addStock(sku, qty)`: nếu `qty <= 0` thì bỏ qua; ngược lại cộng `qty` vào
     * tồn hiện tại (`stockMap.getOrDefault(key, 0) + qty`).
     * 3. `deductStock(sku, qty)`: nếu `qty <= 0` hoặc tồn hiện tại `< qty` -> KHÔNG
     * trừ và trả `false`;
     * ngược lại trừ `qty` vào `stockMap` và trả `true`.
     * 4. `getStock(sku)`: trả tồn kho hiện tại, nếu chưa có trả `0`.
     * - Ví dụ luồng dữ liệu:
     * + `addStock(" sku-01 ", 10)` -> chuẩn hóa `"SKU-01"`, tồn = `10`.
     * + `deductStock("SKU-01", 4)` -> đủ hàng -> tồn còn `6`, trả `true`.
     * + `deductStock("SKU-01", 99)` -> không đủ hàng (`6 < 99`) -> giữ nguyên tồn
     * `6`, trả `false`.
     * + `getStock("sku-01")` -> trả `6`.
     */

    interface StockService {
        void addStock(String sku, int qty);

        boolean deductStock(String sku, int qty);

        int getStock(String sku);
    }

    static class InMemoryStockService implements StockService {
        private final Map<String, Integer> stockMap = new HashMap<>();

        @Override
        public void addStock(String sku, int qty) {
            // TODO (Bài 12)
        }

        @Override
        public boolean deductStock(String sku, int qty) {
            // TODO (Bài 12)
            return false;
        }

        @Override
        public int getStock(String sku) {
            // TODO (Bài 12)
            return 0;
        }
    }

    private static void check(String tenBai, boolean passed) {
        System.out.println((passed ? "✅ PASS: " : "❌ FAIL: ") + tenBai);
    }
}
