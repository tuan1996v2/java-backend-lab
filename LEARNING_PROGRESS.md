# Nhật ký học tập & Đồng bộ ngữ cảnh (`java-backend-lab`)

> **Hướng dẫn khôi phục cuộc hội thoại trên máy khác (Mac mini ở nhà):**
> Mở repo này bằng Antigravity, tạo chat mới và nhắn:
> `"Đọc LEARNING_PROGRESS.md, mình tiếp tục học bài đang dở nhé"`
> Agent sẽ tự nạp `AGENTS.md` + skill `.agents/skills/java-mentor/SKILL.md` và tiếp tục đúng mạch từ nơi bạn dừng lại.

---

## 1. Hồ sơ người học & Phương pháp thống nhất
- **Nền tảng:** Dev Mobile **5 năm React Native (JS/TS)**, hiểu chắc API, JSON, REST, state, nhưng mới bắt đầu học Java & Spring Boot (chưa quen cú pháp Java).
- **Phong cách học hiệu quả nhất đã chốt (Hướng A — 80/20 cho Senior Mobile):**
  - Không cày Core Java kiểu sinh viên hay bắt gõ tay boilerplate mà IDE / Lombok / Java `record` tự làm (`equals`/`hashCode`, getter/setter, `Defensive Copy`, lặp lại `Interface`). Những phần này chỉ đọc hiểu cơ chế + vết sẹo rồi đi tiếp.
  - Hoàn tất **1 module Core Java rút gọn cuối cùng (Module 5 — 6 bài)** gồm đúng 2 mảnh ghép dùng hàng ngày ở Spring Boot: **`Optional<T>` + `Exception`** và **`Stream API`**.
  - Xong Module 5 là quay thẳng về **Spring Boot Production (`com.learning.backend/`)** để build API thật: MapStruct, JPA Specification + phân trang, Soft Delete, `Order`/`OrderItem` + `@Transactional` + Lock kho.
  - So sánh trực tiếp **React Native / TypeScript $\leftrightarrow$ Java** và chạy kiểm tra nhanh bằng hàm `main()` (< 1 giây) cho Core Java, Swagger/H2 cho Spring Boot.

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
- **Module 2 ([Module2Practice.java](src/main/java/com/learning/javacore/module2/Module2Practice.java)) — ✅ ĐÃ HOÀN THÀNH (12 bài / 19/19 PASS):**
  - Đã nắm: `List` (`ArrayList`), `Set` (`HashSet` vs `LinkedHashSet` vs `TreeSet`), vòng lặp `for-each`, xóa phần tử an toàn với `removeIf` / `Iterator`, `subList` + `Math.min`, `break;` dừng sớm, tách helper `normalizeText(...)`.
  - **🩹 Vết sẹo đã học ở Module 2:**
    1. **Điểm mù dấu `!` với `isEmpty()`:** Quen JS `if (arr.length)` nên viết nhầm `if (list.isEmpty())` thay vì `!list.isEmpty()`. Với vòng `for`, chỉ cần `if (list != null)` là đủ!
    2. **`ConcurrentModificationException`:** Không bao giờ gọi `list.remove(x)` bên trong `for (T x : list)` — luôn dùng `list.removeIf(...)` hoặc `Iterator`.
    3. **`new ArrayList<>(null)` nổ NPE:** Constructor copy Collection không nhận `null` — guard trả rỗng luôn dùng `new ArrayList<>()`.
    4. **Đường tắt trả dữ liệu thô:** Fallback `return new ArrayList<>(rawList)` bỏ qua bước trim/IN HOA/loại trùng — chỉ return sớm khi trả về rỗng.
    5. **Check cha quên con (`item != null` nhưng `item.category == null`):** Java không có `?.` như JS; gọi `.trim()` lên field có thể `null` sẽ nổ NPE.
    6. **Auto-import nhầm package:** Vd `org.springframework.cglib.core.Local` thay vì `java.util.Locale` — thấy lỗi lạ ở class quen phải kiểm tra lại phần `import` đầu file.
    7. **`List.of(...)` là bất biến & `subList(0, n)` chặt hơn `slice(0, n)`:** `List.of` cấm `null` và cấm xóa/sửa; `subList` nổ lỗi nếu `end > size()`.
- **Module 3 ([Module3Practice.java](src/main/java/com/learning/javacore/module3/Module3Practice.java)) — ✅ ĐÃ HOÀN THÀNH (12 bài / 22/22 PASS sau khi tối ưu Senior):**
  - Đã nắm: `Map<K, V>` (`HashMap`, `LinkedHashMap`), `put`, `getOrDefault`, `entrySet`, `computeIfAbsent`, `Map.of`, tra cứu O(1), `BigDecimal.stripTrailingZeros()`.
  - **🩹 Vết sẹo đã học ở Module 3 (đã tiêu hóa `S3`, `S4`, `S5`):**
    1. **`getOrDefault(k, default)` KHÔNG cứu được value `null`:** Nếu `map.put("A", null)` thì `getOrDefault("A", 0)` vẫn trả `null` (khác JS `??`) $\rightarrow$ luôn chặn `value == null` trước khi `put` và kiểm tra giá trị trả về.
    2. **`BigDecimal` làm key `HashMap` (`S4`):** `38000` != `38000.00` nếu chưa `.stripTrailingZeros()`.
    3. **Không mutate `Map.of(...)` / `List.of(...)` (`S5`):** Gọi `put`/`add` hoặc truyền `null` sẽ nổ `UnsupportedOperationException` / `NullPointerException`; luôn tạo `HashMap` mới khi gộp dữ liệu và đặt bảng hằng số làm `static final`.
    4. **Tra cứu O(1) thay vì lồng 2 vòng `for` (`S3`):** Dựng `Map<String, BigDecimal>` trước khi tính tiền đơn hàng.
- **Module 4 ([Module4Practice.java](src/main/java/com/learning/javacore/module4/Module4Practice.java)) — ✅ XONG PHẦN CỐT LÕI (Bài 1–4 PASS, dừng đúng lúc theo Hướng A):**
  - Đã nắm & tự code sạch: `enum` có `label` + luật chuyển trạng thái `canTransitionTo` + parse an toàn `fromCode` (dùng `OrderStatus.values()` & `.name()`), đóng gói & chuẩn hóa dữ liệu trong constructor (`CartLine`: `trim().toUpperCase(Locale.ROOT)` + chặn giá/số lượng âm).
  - Đã đọc hiểu cơ chế `equals()` / `hashCode()` (`S1`), `HashSet` bucket (`S2`), `Defensive Copy` & `Interface` (không tốn thời gian gõ tay boilerplate vì IDE / Lombok / `record` xử lý trong dự án thật).
- **Module 5 Rút gọn ([Module5Practice.java](src/main/java/com/learning/javacore/module5/Module5Practice.java)) — ▶️ ĐANG HỌC (Next, 6 bài thực chiến):**
  - Tổng hợp đúng phần còn thiếu trước khi vào Spring Boot: Java 17 `record`, `Optional<T>` (`ofNullable`, `map`, `orElse`, `orElseThrow`), Custom `RuntimeException` (`throw` / `try-catch`), và `Stream API` (`filter`, `map`, `sorted`, `limit`, `groupingBy`).

---

### C. Cách học đã thống nhất (cập nhật 27/11 — Hướng A)
- **Đề bài minh bạch 100%:** Mọi bài đều có 4 mục tách bạch (**Ngữ cảnh thực tế** $\rightarrow$ **Yêu cầu cụ thể khớp test `main()`** $\rightarrow$ **Ví dụ luồng dữ liệu** $\rightarrow$ **Công cụ Java**). Bẫy `🪨` chỉ gài bẫy cơ chế Java, tuyệt đối không giấu rule nghiệp vụ.
- **Tối đa 5–6 bài / module Core Java:** Không lan man boilerplate IDE/Lombok; xong Module 5 rút gọn là vào thẳng Spring Boot (`com.learning.backend`).
- **Sau mỗi bài học:** Mentor đưa mục "🩹 Vết sẹo Senior" ngắn gọn, liên hệ React Native/JS.
- **Quản lý vết sẹo:** `S1`–`S5` đã xử lý/tiêu hoá trong [deferred-scars.md](.agents/skills/java-mentor/references/deferred-scars.md) (riêng `S2` sẽ thực hành lại khi tạo `@Entity` `Order`/`OrderItem` ở Spring Boot).

---

## 3. Lộ trình Core Java Rút gọn & Spring Boot Production

| Module | Nội dung | File thực hành | Trạng thái |
| :---: | :--- | :--- | :---: |
| **1** | `String`, `.equals()`, `BigDecimal`, `if/else`, `switch` (8 bài) | `src/main/java/com/learning/javacore/module1/Module1Practice.java` | **✅ Xong (21/21 PASS)** |
| **2** | `List` (`ArrayList`), `Set` (`HashSet`/`LinkedHashSet`), vòng lặp `for` (12 bài) | `src/main/java/com/learning/javacore/module2/Module2Practice.java` | **✅ Xong (19/19 PASS)** |
| **3** | `Map<K, V>` (`HashMap`/`LinkedHashMap`), `getOrDefault`, đếm & gom nhóm dữ liệu (12 bài) | `src/main/java/com/learning/javacore/module3/Module3Practice.java` | **✅ Xong (22/22 PASS)** |
| **4** | `Enum` có logic, đóng gói Constructor (`CartLine`); đọc hiểu `equals/hashCode`, `Interface` | `src/main/java/com/learning/javacore/module4/Module4Practice.java` | **✅ Xong phần cốt lõi (Bài 1–4)** |
| **5 (Rút gọn)** | `Optional<T>`, `Exception` (`throw`, `CustomException`) & `Stream API` (`filter`, `map`, `sorted`, `groupingBy`) (6 bài) | `src/main/java/com/learning/javacore/module5/Module5Practice.java` | **▶️ ĐANG HỌC (Next)** |

### Ngay sau khi xong Module 5 Rút gọn $\rightarrow$ Quay thẳng về Spring Boot Production (`com.learning.backend`)
Các tính năng tạo value thật đang chờ hoàn thiện:
1. [ProductMapper.java](src/main/java/com/learning/backend/mapper/ProductMapper.java) — MapStruct + Lombok
2. [PageResponse.java](src/main/java/com/learning/backend/dto/PageResponse.java) & [ProductSpecification.java](src/main/java/com/learning/backend/repository/ProductSpecification.java) — Phân trang + Lọc động 6 tiêu chí
3. [BaseEntity.java](src/main/java/com/learning/backend/entity/BaseEntity.java) — JPA Auditing (`@CreatedDate`, `@LastModifiedDate`) + Soft Delete (`@SQLDelete`, `@SQLRestriction`) + Restore API
4. Module `Order` & `OrderItem` — Quan hệ `1-N`, `@Transactional`, snapshot giá và Lock chống bán âm kho

---

## 4. Lệnh hay dùng trên cả 2 máy
- **Chạy bài tập Core Java (< 1 giây, không cần cache Maven):**
  ```bash
  java src/main/java/com/learning/javacore/module5/Module5Practice.java
  ```
- **Chạy qua Maven offline (sau khi đã chạy `./mvnw compile` 1 lần có mạng):**
  ```bash
  ./mvnw -o compile -q && java -cp target/classes com.learning.javacore.module5.Module5Practice
  ```
- **Chạy Spring Boot server (test Swagger `/swagger-ui.html` & H2 `/h2-console`):**
  ```bash
  ./mvnw -o spring-boot:run
  ```
- **Kiểm tra app boot sạch:**
  ```bash
  ./mvnw -o clean test
  ```

---

## 5. Bộ Extension chuẩn cho máy công ty (Antigravity IDE / VS Code)
Cài nhanh các extension ID sau trên máy mới để có auto-import, báo lỗi đỏ tức thì, gợi ý `application.yml`/`pom.xml` và nút Run trên `main()`:
- **Bắt buộc cho Java & Spring Boot:**
  - `vscjava.vscode-java-pack` (Extension Pack for Java — gồm Red Hat Java, Debugger, Maven)
  - `vmware.vscode-boot-dev-pack` (Spring Boot Extension Pack)
  - `redhat.vscode-yaml` (YAML — cho `application.yml`)
  - `redhat.vscode-xml` (XML — cho `pom.xml`)
- **Tăng tốc code như Senior:**
  - `usernamehw.errorlens` (Error Lens — hiện lỗi đỏ ngay cuối dòng)
  - `streetsidesoftware.code-spell-checker` (Code Spell Checker — bắt gõ nhầm tên biến)
  - `eamodio.gitlens` (GitLens)
  - `k--kato.intellij-idea-keybindings` *(nếu dùng phím tắt IntelliJ như `Option + Enter`)*
  - `humao.rest-client` *(tuỳ chọn — test API bằng file `.http`)*
- *(Lưu ý: Không cài thêm extension Lombok riêng lẻ — `redhat.java` bản mới đã hỗ trợ sẵn.)*

---

## 6. Câu nhắn mở đầu buổi sau trên máy bất kỳ
1. `git pull` để lấy code mới nhất.
2. Mở Antigravity chat mới và nhắn:
   `"Đọc LEARNING_PROGRESS.md, mình tiếp tục học Module 5 rút gọn (hoặc Spring Boot) đang dở nhé"`
