# Nhật ký học tập & Đồng bộ ngữ cảnh (`java-backend-lab`)

> **Hướng dẫn khôi phục cuộc hội thoại trên máy khác (Mac mini ở nhà):**
> Mở repo này bằng Antigravity, tạo chat mới và nhắn:
> `"Đọc LEARNING_PROGRESS.md, mình tiếp tục học bài đang dở nhé"`
> Agent sẽ tự nạp `AGENTS.md` + skill `.agents/skills/java-mentor/SKILL.md` và tiếp tục đúng mạch từ nơi bạn dừng lại.

---

## 1. Hồ sơ người học & Phương pháp thống nhất
- **Nền tảng:** Dev Mobile **5 năm React Native (JS/TS)**, hiểu chắc API, JSON, REST, state, nhưng mới bắt đầu học Java & Spring Boot (chưa quen cú pháp Java).
- **Phong cách học hiệu quả nhất đã chốt:**
  - Học theo **Cách B: Làm vững móng Core Java thực chiến trước** (trong `src/main/java/com/learning/javacore/`), rồi mới quay lại các phần Spring Boot nâng cao (`com.learning.backend/`).
  - So sánh trực tiếp **React Native / TypeScript $\leftrightarrow$ Java** trước mỗi module.
  - Mỗi module gồm **7–8 bài tập nhỏ, dễ vừa sức, lặp nhiều lần để nhớ cú pháp**, gắn nghiệp vụ bán hàng thực tế (không lý thuyết suông).
  - Chạy kiểm tra nhanh bằng hàm `main()` (tốn < 1 giây), **không ép viết JUnit test** cho các bài CRUD Spring Boot nhỏ.

---

## 2. Tóm tắt toàn bộ kiến thức & lỗi thực tế đã học trong hội thoại

### A. Hạ tầng Spring Boot & Database H2 (`com.learning.backend`)
- **H2 Console (`/h2-console`):** Giao diện quản trị DB trong RAM (`jdbc:h2:mem:storedb`, user `sa`, pass rỗng). Dữ liệu mất khi tắt app; khi bật lại app, `DataInitializer` nạp lại 40 sản phẩm mẫu nếu bảng đang rỗng.
- **SQL trong H2:** Chuỗi giá trị dùng **nháy đơn `'Kem'`** (khác Java dùng `"..."`); nháy kép `"..."` dùng cho tên bảng/cột khi cần phân biệt hoa-thường hoặc trùng từ khóa.
- **Tìm kiếm tiếng Việt không dấu & tương đối (đã code xong cho cả `keyword` & `category`):**
  - Tạo [TextNormalizer.java](src/main/java/com/learning/backend/util/TextNormalizer.java) (dùng `Normalizer.Form.NFD` + xóa `\p{M}+` + thay `đ`/`Đ` $\rightarrow$ `d`/`D`).
  - Lưu 2 cột `name_normalized`, `category_normalized` trên `Product` và tự động cập nhật trong `@PrePersist` / `@PreUpdate`.
  - Hiểu **Spring Data JPA Derived Query Methods**: Spring dịch từng khúc tên hàm trong interface `ProductRepository` (theo tên biến Java như `NameNormalized`, `Containing`, `And`, `CategoryNormalized`) thành SQL `LIKE` — không được đặt tên tùy ý nếu không dùng `@Query`.
- **Thiết kế DTO vs Entity & Tiền tệ:**
  - `finalPrice` không lưu DB (tránh lệch dữ liệu với `price` & `discountPercent`), chỉ tính ở `ProductResponse` (chỉ lưu snapshot giá vào DB khi làm bảng lịch sử đơn hàng `order_items`).
  - Tiền tệ luôn dùng `BigDecimal` (không dùng `double`/`float`/`Integer`).
- **Lombok & Getter/Setter (đã nắm & kiểm chứng bằng `javap -p`):**
  - Đã hiểu `javac` + Lombok sinh bytecode `get...`, `set...`, `builder()`, constructor vào file `.class`.
  - Đã hiểu `int` (primitive, không `null`) vs `Integer` (wrapper object, nhận `null`, dùng `.equals()` khi so sánh, dùng cho Entity/DTO).
  - Đã hiểu khi nào dùng constructor overloading vs `@Builder` + `@Builder.Default` (giữ giá trị `= 0` không bị đè `null`).
  - Đã áp dụng chuẩn công ty: DTO dùng `@Data` (`ProductRequest`, `ProductResponse`), Entity dùng `@Getter @Setter @NoArgsConstructor(access = AccessLevel.PROTECTED) @Builder` + `@Setter(AccessLevel.NONE)` trên `id`/`createdAt`/`updatedAt` + `@ToString(of = ...)` (tránh bẫy `StackOverflowError` và `HashSet` của `@Data` khi có quan hệ bảng), DI dùng `@RequiredArgsConstructor`, log dùng `@Slf4j` với `{}`.
- **MapStruct (`ProductMapper.java`):** Đã cài sẵn dependency (`mapstruct`, `mapstruct-processor`, `lombok-mapstruct-binding`) vào `pom.xml`, nhưng **tạm giữ `ProductMapper.java` làm khung trống** để học chắc Core Java trước rồi quay lại sau.

### B. Core Java Thực Chiến (`com.learning.javacore`)
- **Module 1 ([Module1Practice.java](src/main/java/com/learning/javacore/module1/Module1Practice.java)) — ✅ ĐÃ HOÀN THÀNH (21/21 PASS):**
  - Đã nắm: `.trim()`, `.toUpperCase()`, `.equalsIgnoreCase()`, `substring(start, end)`, `matches(...)`, `BigDecimal.compareTo(...)`.
  - **2 bẫy quan trọng cần luôn nhớ từ review Module 1:**
    1. `BigDecimal.divide(divisor, 2, RoundingMode.HALF_UP)` — **phải truyền đủ 3 tham số** (`RoundingMode`), tránh gọi hàm 2 tham số cũ `divide(divisor, 2)` vì `2` ở đó bị hiểu là `ROUND_CEILING` ở 0 chữ số lẻ!
    2. **Yoda condition tránh `NullPointerException`:** viết `"HN".equalsIgnoreCase(provinceCode)` thay vì `provinceCode.equalsIgnoreCase("HN")`.

---

## 3. Lộ trình 6 Module Core Java & Tiến độ hiện tại

| Module | Nội dung | File thực hành | Trạng thái |
| :---: | :--- | :--- | :---: |
| **1** | `String`, `.equals()`, `BigDecimal`, `if/else`, `switch` (8 bài) | `src/main/java/com/learning/javacore/module1/Module1Practice.java` | **✅ Xong (21/21 PASS)** |
| **2** | `List` (`ArrayList`), `Set` (`HashSet`/`LinkedHashSet`), vòng lặp `for` (8 bài) | `src/main/java/com/learning/javacore/module2/Module2Practice.java` | **▶️ ĐANG HỌC (Next)** |
| **3** | `Map<K, V>` (`HashMap`), `getOrDefault`, đếm & gom nhóm dữ liệu (7 bài) | *(Chưa tạo — tạo sau khi xong Module 2)* | ⬜ Chưa học |
| **4** | `Class`, `Interface`, `Enum`, tính đóng gói & đa hình (7 bài) | *(Chưa tạo)* | ⬜ Chưa học |
| **5** | `Optional<T>`, Null Safety & `Exception` (`try-catch`, `throw`, Custom Exception) (7 bài) | *(Chưa tạo)* | ⬜ Chưa học |
| **6** | `Stream API` & Lambda (`filter`, `map`, `sorted`, `groupingBy`) (8 bài) | *(Chưa tạo)* | ⬜ Chưa học |

### Sau khi xong 6 Module Core Java $\rightarrow$ Quay lại Spring Boot Production (`com.learning.backend`)
Các file khung đã tạo sẵn chờ hoàn thiện:
1. [ProductMapper.java](src/main/java/com/learning/backend/mapper/ProductMapper.java) — MapStruct + Lombok
2. [PageResponse.java](src/main/java/com/learning/backend/dto/PageResponse.java) & [ProductSpecification.java](src/main/java/com/learning/backend/repository/ProductSpecification.java) — Phân trang + Lọc động 6 tiêu chí
3. [BaseEntity.java](src/main/java/com/learning/backend/entity/BaseEntity.java) — JPA Auditing (`@CreatedDate`, `@LastModifiedDate`) + Soft Delete (`@SQLDelete`, `@SQLRestriction`) + Restore API
4. Module `Order` & `OrderItem` — Quan hệ `1-N`, `@Transactional`, snapshot giá và Lock chống bán âm kho

---

## 4. Lệnh hay dùng trên cả 2 máy
- **Chạy bài tập Core Java (< 1 giây):**
  ```bash
  ./mvnw -o compile -q && java -cp target/classes com.learning.javacore.module2.Module2Practice
  ```
- **Chạy Spring Boot server (test Swagger `/swagger-ui.html` & H2 `/h2-console`):**
  ```bash
  ./mvnw -o spring-boot:run
  ```
- **Kiểm tra app boot sạch:**
  ```bash
  ./mvnw -o clean test
  ```
  *(Lưu ý: Thêm cờ `-o` để chạy offline nhanh 1–3 giây, không bị Maven chờ timeout kết nối snapshot repos).*
