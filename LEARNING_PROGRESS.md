# Nhật ký học tập & Đồng bộ ngữ cảnh (`java-backend-lab`)

> **Hướng dẫn tiếp tục trên máy khác (Mac mini ở nhà):**
> 1. `git pull`
> 2. Mở Antigravity chat mới và nhắn:
>    **"tiếp tục học"** *(hoặc **"bài tiếp theo"**)*
> Mentor sẽ tự đọc file này + `AGENTS.md` + `SKILL.md` và nối đúng ngay bước đang dở!
> *(Muốn ôn lại bài tập Java Core lúc rảnh: `git checkout practice/java-core`)*

---

## 1. Hồ sơ người học & Nguyên tắc dạy đã chốt cứng (cập nhật 28/11)
- **Người học:** Lead Mobile Developer (**5 năm React Native / TS**), **mới bắt đầu Java & Spring Boot** (không phải chuyên gia Java, không chạy đua làm thương mại).
- **Dự án thực hành:** **Restaurant Backend (F&B)** — Modular Monolith (`common/`, `menu/`, `table/`...), bắt đầu từ 1 nhà hàng (`branchId = 1L`), mở rộng dần thành Chuỗi nhà hàng.
- **Phong cách học bắt buộc (trong `AGENTS.md` & `SKILL.md`):**
  1. **Luôn ánh xạ React Native / TypeScript $\leftrightarrow$ Java / Spring / SQL** trước khi đưa cú pháp mới.
  2. **Đi từng nấc một ("nước gạo $\rightarrow$ cháo $\rightarrow$ cơm phở $\rightarrow$ hải sản mỹ vị") — CẤM gộp hay nhảy cóc:**
     `List` thuần + chạy Swagger $\rightarrow$ `GET /{id}` / `1 @RequestParam` $\rightarrow$ `POST @RequestBody` $\rightarrow$ `Pageable` $\rightarrow$ `Specification`.
  3. **Show & Mổ xẻ ví dụ riêng trong chat TRƯỚC, Người học tự viết TODO trong file SAU:** Không viết hộ mất phần thực hành của người học, cũng không ném TODO trống trơn bắt tự bơi. Luôn liệt kê đủ họ hàng cùng nhóm (`@GetMapping` $\leftrightarrow$ `@PostMapping`/`@PutMapping`/`@PatchMapping`/`@DeleteMapping`; `@PathVariable` $\leftrightarrow$ `@RequestParam`/`@RequestBody`).

---

## 2. Những gì đã hoàn thành tới cuối buổi 28/11

### A. Core Java (đã lưu trọn bộ trên nhánh `practice/java-core`)
- Module 1–5 + `StreamQuickDrill`: `String`, `BigDecimal`, `List/Set/Map`, `Enum`, `Optional`, `Exception`, `record`, `Stream API`.

### B. Hạ tầng `common/` trên `main`
- [BaseEntity.java](src/main/java/com/learning/backend/common/entity/BaseEntity.java) (`id`, `branchId = 1L`, `createdAt`, `updatedAt`, `deleted`) + `@EnableJpaAuditing` ở [JavaBackendLabApplication.java](src/main/java/com/learning/backend/JavaBackendLabApplication.java).
- [ApiResponse.java](src/main/java/com/learning/backend/common/dto/ApiResponse.java), [PageResponse.java](src/main/java/com/learning/backend/common/dto/PageResponse.java), [BusinessException.java](src/main/java/com/learning/backend/common/exception/BusinessException.java), [ResourceNotFoundException.java](src/main/java/com/learning/backend/common/exception/ResourceNotFoundException.java), [GlobalExceptionHandler.java](src/main/java/com/learning/backend/common/exception/GlobalExceptionHandler.java), [TextNormalizer.java](src/main/java/com/learning/backend/common/util/TextNormalizer.java), [OpenApiConfig.java](src/main/java/com/learning/backend/common/config/OpenApiConfig.java), [RestaurantDataSeeder.java](src/main/java/com/learning/backend/common/config/RestaurantDataSeeder.java) (tự nạp 4 danh mục + 10 món ăn mẫu vào H2 `restaurantdb`).

### C. Phân hệ Thực đơn `menu/` (Giai đoạn 1 — đang đi từng nấc)
- **Entity & Enum:** [MenuItemStatus.java](src/main/java/com/learning/backend/menu/entity/MenuItemStatus.java), [Category.java](src/main/java/com/learning/backend/menu/entity/Category.java), [MenuItem.java](src/main/java/com/learning/backend/menu/entity/MenuItem.java) (đã nắm Khóa chính vs Khóa ngoại `@ManyToOne(fetch = FetchType.LAZY)`, `@Enumerated(EnumType.STRING)`, `@Index`, `@PrePersist`/`@PreUpdate`, `protected`, `camelCase` vs `snake_case`).
- **Repository:** [CategoryRepository.java](src/main/java/com/learning/backend/menu/repository/CategoryRepository.java), [MenuItemRepository.java](src/main/java/com/learning/backend/menu/repository/MenuItemRepository.java) (`existsByCodeIgnoreCaseAndDeletedFalse`, `findByIdAndDeletedFalse`, `findByCategoryIdAndDeletedFalse`).
- **DTO & MapStruct Mapper:** [CategoryResponse.java](src/main/java/com/learning/backend/menu/dto/CategoryResponse.java), [MenuItemRequest.java](src/main/java/com/learning/backend/menu/dto/MenuItemRequest.java), [MenuItemResponse.java](src/main/java/com/learning/backend/menu/dto/MenuItemResponse.java), [MenuItemMapper.java](src/main/java/com/learning/backend/menu/mapper/MenuItemMapper.java) (đã hiểu DTO là hộp đóng gói/soát lỗi ở Controller, `@Mapping(source = "category.name", target = "categoryName")`, `defaultValue = "AVAILABLE"`).
- **Service:** [MenuService.java](src/main/java/com/learning/backend/menu/service/MenuService.java), [MenuServiceImpl.java](src/main/java/com/learning/backend/menu/service/impl/MenuServiceImpl.java) — đã tự hoàn thiện `getCategories()`, `getAllMenuItems()`, `getMenuItemDetail(id)`, `getMenuItemsByCategory(categoryId)` (đã hiểu DI `@RequiredArgsConstructor`, `Class::method` vs `bien::method`, quy tắc không `()` khi dùng `::`, và Tính trừu tượng `Service` interface vs `ServiceImpl`).
- **Controller:** [MenuController.java](src/main/java/com/learning/backend/menu/controller/MenuController.java) — đã tự hoàn thiện cả **Nấc 1** (`GET /api/menu/categories`, `GET /api/menu`), **Nấc 2** (`GET /api/menu/{id}` với `@PathVariable`), và **Nấc 2.2** (`GET /api/menu/by-category?categoryId=...` với `@RequestParam`), `./mvnw -o clean compile -q` **PASS 100%**!

---

## 3. ĐÚNG ĐIỂM DỪNG & BƯỚC TIẾP THEO KHI NHẮN `"tiếp tục học"` / `"bài tiếp theo"`
1. **Kiểm chứng nhanh Nấc 1–2.2 trên Swagger UI (2 phút):** Bật app (`./mvnw -o spring-boot:run`), test `GET /api/menu`, `GET /api/menu/3` (200), `GET /api/menu/999` (404), và `GET /api/menu/by-category?categoryId=2` (3 món chính) — đồng thời **soi log SQL Hibernate** (bắt thử N+1 Query `select category`).
2. **Nấc 3 (Cơm): `POST /api/menu` — Tạo món mới** (`@PostMapping`, `@ResponseStatus(HttpStatus.CREATED)`, `@Valid`, `@RequestBody`, `@Transactional`, check trùng `code`, tìm `Category` $\rightarrow$ `save()`) — **Show & Mổ xẻ ví dụ trong chat trước $\rightarrow$ người học tự code TODO sau**, rồi xem dòng mới trong H2 Console!
3. **Nấc 4 (Phở): Phân trang** (`Pageable` + `PageResponse`).
4. **Nấc 5 (Hải sản mỹ vị): `Specification` lọc động nhiều tiêu chí** + sang `table/` (`DiningTable` QR bàn ăn).

---

## 4. Lộ trình tổng 5 Giai đoạn (Restaurant Backend)

| GĐ | Phạm vi & App sử dụng | API cốt lõi | Kỹ thuật Spring Boot & SQL | Trạng thái |
| :---: | :--- | :--- | :--- | :---: |
| **1** | **Thực đơn & Bàn (`menu/`, `table/`)** — *App Khách* | `GET /api/menu`, `GET /api/menu/{id}`, `GET /api/menu/by-category`, `POST /api/menu`, `Pageable`, `Specification`, `GET /api/tables` | `@Entity`, `@ManyToOne`, MapStruct, `@PathVariable`/`@RequestParam`/`@RequestBody`, `PageResponse`, `Specification`, `@Index` | **▶️ ĐANG HỌC (Xong code Nấc 1–2.2, tiếp theo: test Swagger + sang Nấc 3 `POST /api/menu`)** |
| **2** | **Đặt món tại bàn (`order/`)** — *App Khách* | `POST /api/orders`, `POST /api/orders/{id}/items`, `GET /api/orders/{id}` | Quan hệ 1-N (`Order`–`OrderItem`), `@Transactional`, Snapshot giá, `JOIN FETCH` | ⬜ Chưa học |
| **3** | **Bếp & Thu ngân (`kitchen/`, `payment/`)** | `PATCH /api/kitchen/items/{id}/status`, Checkout VAT + Voucher | Enum State Machine, Lock chống thanh toán trùng, SSE / WebSocket | ⬜ Chưa học |
| **4** | **Auth & DB thật (`auth/`)** | Login JWT + RBAC, chuyển H2 $\rightarrow$ PostgreSQL + DBeaver | Spring Security + JWT, Docker/Postgres, Flyway `.sql` | ⬜ Chưa học |
| **5** | **Kho, Báo cáo & Chuỗi (`inventory/`)** | Kho nguyên liệu BOM, doanh thu ngày, multi-branch `branch_id` | SQL `GROUP BY` / `SUM` / `EXPLAIN ANALYZE`, Redis Cache | ⬜ Chưa học |

---

## 5. Lệnh chạy nhanh
- **Chạy Spring Boot server (Swagger `/swagger-ui.html`, H2 `/h2-console`, JDBC URL `jdbc:h2:mem:restaurantdb`):**
  ```bash
  ./mvnw -o spring-boot:run
  ```
- **Kiểm tra build & boot sạch:**
  ```bash
  ./mvnw -o clean test
  ```
- **Ôn Java Core:** `git checkout practice/java-core` (xong quay lại `git checkout main`).
