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
 * MODULE 4: Enum, Class, Đóng gói (Encapsulation), equals/hashCode & Interface (Đa hình)
 *
 * HƯỚNG DẪN CHẠY NHANH (< 1 giây, không cần mạng):
 * java src/main/java/com/learning/javacore/module4/Module4Practice.java
 *
 * BẢNG ĐỐI CHIẾU REACT NATIVE (TS/JS) -> JAVA:
 * 1. Type Union / Hằng số -> `enum`:
 *    TS:   type OrderStatus = 'PENDING' | 'PAID';
 *          const LABELS = { PENDING: 'Chờ xác nhận', PAID: 'Đã thanh toán' };
 *    Java: enum có thể chứa field + constructor + method ngay bên trong!
 *          `OrderStatus.PENDING.getLabel()` -> "Chờ xác nhận"
 *          `OrderStatus.values()` -> trả về mảng tất cả các giá trị enum (để lặp tìm theo chuỗi!)
 *
 * 2. Interface & Implements (Nền tảng của Spring Boot `ProductService` -> `ProductServiceImpl`):
 *    TS:   interface DiscountRule { calculate(total: number): number; }
 *          class PercentRule implements DiscountRule { ... }
 *    Java: Dùng `implements` y chang TypeScript! Một hàm nhận `DiscountRule` có thể nhận bất kỳ
 *          class nào implements nó (gọi là Đa hình / Polymorphism).
 *
 * 3. So sánh 2 Object tự viết (`equals` & `hashCode`) — 🪨 Vết sẹo S1 & S2:
 *    - Mặc định `new SkuTag("A1").equals(new SkuTag("A1"))` trả về FALSE (vì so sánh địa chỉ RAM)!
 *    - Khi bỏ Object vào `HashSet` hay làm key của `HashMap`, BẮT BUỘC override cả `equals()` lẫn `hashCode()`:
 *      `Objects.equals(a, b)` và `Objects.hash(field)`.
 *    - 🪨 Tuyệt đối KHÔNG đổi field dùng để tính `hashCode` khi object đang nằm trong `HashSet`!
 *
 * 4. Đóng gói & Defensive Copy (Chống sửa ngầm List bên trong Object):
 *    - Không trả thẳng `return this.items;` nếu không muốn bên ngoài `.clear()` phá hỏng dữ liệu bên trong.
 *    - Copy khi nhận: `this.items = new ArrayList<>(items);`
 *    - Trả bản chỉ đọc: `return Collections.unmodifiableList(this.items);` (hoặc `List.copyOf(this.items)`).
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
        DiscountRule fixedRule = new FixedAmountDiscountRule(new BigDecimal("40000"), new BigDecimal("150000")); // đơn >= 150k giảm 40k
        check("Bài 7 - Giảm 20% của 100k = 20k",
                new BigDecimal("20000.00").compareTo(percentRule.calculateDiscount(new BigDecimal("100000"))) == 0);
        check("Bài 7 - Giảm 20% của 500k bị chặn trần = 50k",
                new BigDecimal("50000.00").compareTo(percentRule.calculateDiscount(new BigDecimal("500000"))) == 0);
        check("Bài 8 - Đơn 100k < 150k không được giảm cố định",
                BigDecimal.ZERO.compareTo(fixedRule.calculateDiscount(new BigDecimal("100000"))) == 0);
        check("Bài 8 - Đơn 200k >= 150k được giảm 40k",
                new BigDecimal("40000").compareTo(fixedRule.calculateDiscount(new BigDecimal("200000"))) == 0);

        List<DiscountRule> allRules = List.of(percentRule, fixedRule);
        // Đơn 200k: percent giảm min(40k, 50k)=40k; đơn 300k: percent giảm min(60k, 50k)=50k, fixed giảm 40k -> chọn 50k -> trả 250k
        check("Bài 9 - Chọn giảm giá tốt nhất (300k -> còn 250k)",
                new BigDecimal("250000.00").compareTo(bai9_tinhGiaSauGiamTotNhat(new BigDecimal("300000"), allRules)) == 0);

        // Bài 10: Interface Filter (Tiền đề Spring Specification)
        List<ProductData> products = List.of(
                new ProductData("SKU-01", "Sữa tươi", new BigDecimal("38000")),
                new ProductData("SKU-02", "Sữa chua", new BigDecimal("28000")),
                new ProductData("SKU-03", "Sữa tươi Organic", new BigDecimal("55000"))
        );
        List<ProductSearchFilter> filters = List.of(
                new CategoryFilter("Sữa tươi"),
                new MaxPriceFilter(new BigDecimal("40000"))
        );
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
     * BÀI 1 & 2 & 3 🪨: Enum OrderStatus có label tiếng Việt, luật chuyển trạng thái, và parse an toàn.
     *
     * - BÀI 1: Khai báo 5 trạng thái:
     *   PENDING("Chờ xác nhận"), PAID("Đã thanh toán"), SHIPPED("Đang giao"),
     *   COMPLETED("Hoàn thành"), CANCELLED("Đã hủy").
     *   Hoàn thiện constructor và `getLabel()`.
     *
     * - BÀI 2: Luật chuyển trạng thái `canTransitionTo(OrderStatus next)`:
     *   + PENDING   -> chỉ sang PAID hoặc CANCELLED
     *   + PAID      -> chỉ sang SHIPPED hoặc CANCELLED
     *   + SHIPPED   -> chỉ sang COMPLETED
     *   + COMPLETED, CANCELLED -> không sang được trạng thái nào (false)
     *
     * - BÀI 3 🪨: `fromCode(String code)`:
     *   + Chuẩn hóa `code` (null -> trả null; trim + IN HOA).
     *   + Duyệt `for (OrderStatus status : OrderStatus.values())`: nếu `status.name().equals(normalCode)` -> return status.
     *   + Nếu không khớp cái nào -> trả về `null` (KHÔNG dùng thẳng `OrderStatus.valueOf(...)` mà không kiểm tra, vì truyền "UNKNOWN" sẽ nổ `IllegalArgumentException`!).
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
            // TODO (Bài 1): Trả về label
            return "";
        }

        public boolean canTransitionTo(OrderStatus next) {
            // TODO (Bài 2): Kiểm tra next != null và luật chuyển trạng thái (switch theo `this`)
            return false;
        }

        public static OrderStatus fromCode(String code) {
            // TODO (Bài 3 🪨): Duyệt OrderStatus.values() để parse an toàn không bị crash
            return null;
        }
    }

    /**
     * BÀI 4: Đóng gói (Encapsulation) trong class `CartLine`.
     * - Ngữ cảnh: Khi tạo 1 dòng giỏ hàng, constructor có trách nhiệm "gác cổng" làm sạch dữ liệu ngay từ đầu:
     *   + `sku`: nếu null -> "", ngược lại `trim().toUpperCase(Locale.ROOT)`.
     *   + `unitPrice`: nếu null hoặc `< 0` -> gán `BigDecimal.ZERO`.
     *   + `quantity`: nếu `< 0` -> gán `0`.
     * - Hoàn thiện `getLineTotal()` = `unitPrice * quantity`.
     */
    static class CartLine {
        private final String sku;
        private final BigDecimal unitPrice;
        private final int quantity;

        public CartLine(String sku, BigDecimal unitPrice, int quantity) {
            // TODO (Bài 4): Sửa constructor này để chuẩn hóa & chặn giá/số lượng âm
            this.sku = sku;
            this.unitPrice = unitPrice;
            this.quantity = quantity;
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
            // TODO (Bài 4): Trả về unitPrice * quantity
            return BigDecimal.ZERO;
        }
    }

    /**
     * BÀI 5 🪨 (Vết sẹo S1 — equals & hashCode):
     * - Ngữ cảnh: Hai object `new ProductKey("SKU-01")` và `new ProductKey("  sku-01 ")` đại diện cho
     *   CÙNG 1 sản phẩm khi bỏ vào `HashSet` hoặc dùng làm key của `HashMap`.
     * - Nhiệm vụ:
     *   1. Trong constructor: chuẩn hóa `sku` (null -> "", ngược lại `trim().toUpperCase(Locale.ROOT)`).
     *   2. Override `equals(Object o)` và `hashCode()` theo trường `this.sku`:
     *      @Override
     *      public boolean equals(Object o) {
     *          if (this == o) return true;
     *          if (!(o instanceof ProductKey other)) return false;
     *          return Objects.equals(this.sku, other.sku);
     *      }
     *      @Override
     *      public int hashCode() { return Objects.hash(this.sku); }
     */
    static class ProductKey {
        private final String sku;

        public ProductKey(String sku) {
            // TODO (Bài 5 🪨): Chuẩn hóa sku (trim + IN HOA)
            this.sku = sku;
        }

        public String getSku() {
            return sku;
        }

        // TODO (Bài 5 🪨): Override equals(Object o) và hashCode()
    }

    /**
     * BÀI 6 🪨 (Vết sẹo S2 — Không đổi key hashCode khi object đang nằm trong HashSet):
     * - Ngữ cảnh: Đổi tên của `tag` từ "HOT" -> "BEST_SELLER" khi `tag` đang nằm trong `tagSet`.
     * - 🪨 Bẫy: Nếu gọi thẳng `tag.setName(newName);` khi `tag` vẫn đang ở trong `tagSet`, `hashCode()` của `tag`
     *   thay đổi nhưng vị trí ngăn tủ trong `HashSet` không tự dời theo -> `tagSet.contains(tag)` trả về FALSE!
     * - Cách đúng: `tagSet.remove(tag)` -> đổi tên `tag.setName(newName)` -> `tagSet.add(tag)`.
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
            if (this == o) return true;
            if (!(o instanceof MutableTag other)) return false;
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
     * BÀI 7: Giảm theo % có trần tối đa (`maxDiscount`).
     * - Ví dụ: 20%, trần 50_000:
     *   + orderTotal = 100_000 -> 20% = 20_000 (< 50_000) -> giảm 20_000.
     *   + orderTotal = 500_000 -> 20% = 100_000 (> 50_000) -> bị chặn trần -> giảm 50_000.
     * - Lưu ý: Nếu orderTotal == null hoặc <= 0 -> trả về BigDecimal.ZERO.
     *   Tính phần trăm dùng `.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)`.
     *   Nếu `maxDiscount != null && discount.compareTo(maxDiscount) > 0` -> trả về `maxDiscount`.
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
            // TODO (Bài 7): Tính tiền giảm theo % và chặn trần maxDiscount
            return BigDecimal.ZERO;
        }
    }

    /**
     * BÀI 8: Giảm số tiền cố định nếu đơn đạt ngưỡng tối thiểu (`minOrderTotal`).
     * - Nếu orderTotal == null hoặc `< minOrderTotal` -> trả về `BigDecimal.ZERO`.
     * - Ngược lại trả về `discountAmount` (nhưng không được vượt quá `orderTotal` để tránh tổng tiền âm!).
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
            // TODO (Bài 8): Kiểm tra đủ minOrderTotal và chặn không giảm vượt quá orderTotal
            return BigDecimal.ZERO;
        }
    }

    /**
     * BÀI 9: Duyệt danh sách nhiều `DiscountRule` (đa hình), tìm mức giảm LỚN NHẤT
     * và trả về số tiền cuối khách phải trả = `orderTotal - maxDiscount` (tối thiểu là `BigDecimal.ZERO`).
     */
    public static BigDecimal bai9_tinhGiaSauGiamTotNhat(BigDecimal orderTotal, List<DiscountRule> rules) {
        // TODO (Bài 9): Duyệt qua `rules`, gọi `rule.calculateDiscount(orderTotal)`, lấy maxDiscount rồi trừ vào orderTotal
        return BigDecimal.ZERO;
    }

    /**
     * BÀI 10: Interface `ProductSearchFilter` (tiền đề cho Spring `Specification`).
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
            // TODO (Bài 10): Trả true nếu product != null và category khớp (equalsIgnoreCase)
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
            // TODO (Bài 10): Trả true nếu product != null && product.price != null && price <= maxPrice
            return false;
        }
    }

    public static List<ProductData> bai10_locSanPham(List<ProductData> products, List<ProductSearchFilter> filters) {
        // TODO (Bài 10): Chỉ giữ lại các product thỏa mãn TẤT CẢ filter trong `filters`
        return new ArrayList<>();
    }

    /**
     * BÀI 11 🪨 (Defensive Copy): Bảo vệ dữ liệu nội bộ của `OrderSnapshot` không bị bên ngoài sửa ngầm.
     * - Trong constructor: nếu `lines == null` -> `new ArrayList<>()`, ngược lại tạo bản copy `new ArrayList<>(lines)`.
     * - Trong `getLines()`: trả về `Collections.unmodifiableList(this.lines)` để bên ngoài không `.add()`/`.clear()` được!
     */
    static class OrderSnapshot {
        private final List<CartLine> lines;

        public OrderSnapshot(List<CartLine> lines) {
            // TODO (Bài 11 🪨): Copy ra ArrayList mới (không gán trực tiếp `this.lines = lines;`)
            this.lines = lines;
        }

        public List<CartLine> getLines() {
            // TODO (Bài 11 🪨): Trả về Collections.unmodifiableList(this.lines)
            return lines;
        }
    }

    /**
     * BÀI 12: Interface `StockService` và implementation `InMemoryStockService`
     * (Mô phỏng đúng kiến trúc `ProductService` -> `ProductServiceImpl` trong Spring Boot).
     * - `addStock(sku, qty)`: bỏ qua nếu sku null/rỗng hoặc qty <= 0; chuẩn hóa sku (trim + IN HOA) rồi cộng vào Map.
     * - `deductStock(sku, qty)`: chuẩn hóa sku; nếu qty <= 0 hoặc tồn kho hiện tại < qty -> trả `false` (không trừ);
     *   ngược lại trừ đi `qty` và trả `true`.
     * - `getStock(sku)`: chuẩn hóa sku; trả về tồn kho hiện tại (0 nếu chưa có).
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
