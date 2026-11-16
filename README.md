# ☕ Java Backend Lab — Mini Store REST API

Chào mừng bạn đến với project thực hành Backend đầu tiên bằng **Java 17** và **Spring Boot 3**!

Project này được thiết kế theo chuẩn kiến trúc phân tầng thực tế trong doanh nghiệp, tích hợp sẵn **In-memory Database H2** (chạy ngay trong RAM, không cần cài MySQL/PostgreSQL) và **Swagger UI** (giao diện test API trực quan như Postman ngay trên trình duyệt).

---

## 📂 1. Cấu trúc Project (Kiến trúc phân tầng 3-tier)

```
java-backend-lab/
├── pom.xml                                   # File cấu hình thư viện Maven
├── README.md                                 # Tài liệu hướng dẫn này
└── src/main/java/com/learning/backend/
    ├── JavaBackendLabApplication.java        # Hàm main khởi chạy ứng dụng
    │
    ├── controller/                           # [TẦNG 1: Controller] Tiếp nhận HTTP Request
    │   └── ProductController.java            # Định nghĩa các endpoint GET, POST, PUT, DELETE
    │
    ├── service/                              # [TẦNG 2: Service] Xử lý nghiệp vụ logic
    │   ├── ProductService.java               # Interface nghiệp vụ
    │   └── impl/ProductServiceImpl.java      # Cài đặt nghiệp vụ (validate, lưu, map data)
    │
    ├── repository/                           # [TẦNG 3: Repository] Thao tác với Cơ sở dữ liệu
    │   └── ProductRepository.java            # Giao tiếp DB qua Spring Data JPA (tự sinh SQL)
    │
    ├── entity/                               # Bảng trong Database (ORM)
    │   └── Product.java                      # Ánh xạ bảng "products"
    │
    ├── dto/                                  # Data Transfer Object (Trao đổi dữ liệu qua API)
    │   ├── ApiResponse.java                  # Khung JSON phản hồi chuẩn { success, message, data }
    │   ├── ProductRequest.java               # Payload nhận từ Client gửi lên (có Validation)
    │   └── ProductResponse.java              # Payload trả về cho Client
    │
    ├── exception/                            # Xử lý lỗi tập trung (Global Exception Handling)
    │   ├── GlobalExceptionHandler.java       # Bắt lỗi 400, 404, 500 trả về JSON chuẩn
    │   └── ResourceNotFoundException.java    # Lỗi khi không tìm thấy tài nguyên
    │
    └── config/                               # Cấu hình hệ thống
        ├── OpenApiConfig.java                # Cấu hình Swagger UI
        └── DataInitializer.java              # Tự động nạp sẵn dữ liệu mẫu khi khởi động app
```

---

## 🚀 2. Cách chạy ứng dụng

Mở Terminal tại thư mục `/Users/tuanos/work/java-backend-lab` và chạy lệnh:

```bash
mvn spring-boot:run
```

Hoặc nếu bạn dùng **IntelliJ IDEA**:
- Mở thư mục `/Users/tuanos/work/java-backend-lab`
- Chờ IntelliJ tải dependencies (Maven sync)
- Mở file `JavaBackendLabApplication.java` và bấm nút **Run ▶️** màu xanh.

---

## 🌐 3. Trải nghiệm & Kiểm tra ngay trên trình duyệt

Khi ứng dụng đã chạy (`Started JavaBackendLabApplication in ... seconds`):

### 1. Swagger UI (Test API không cần Postman)
Truy cập: **[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**
- Bạn có thể bấm vào từng API (`GET`, `POST`, `PUT`, `DELETE`).
- Bấm **"Try it out"** $\rightarrow$ **"Execute"** để xem request/response trực tiếp!

### 2. H2 Database Console (Xem bảng dữ liệu trực tiếp)
Truy cập: **[http://localhost:8080/h2-console](http://localhost:8080/h2-console)**
- **JDBC URL**: `jdbc:h2:mem:storedb`
- **User Name**: `sa`
- **Password**: *(để trống)*
- Bấm **Connect** $\rightarrow$ Bạn sẽ thấy bảng `PRODUCTS` và có thể chạy câu lệnh `SELECT * FROM PRODUCTS;` để xem dữ liệu.

---

## 📡 4. Danh sách API có sẵn trong Project

| Method | Endpoint | Mô tả | Tham số / Body |
| :---: | :--- | :--- | :--- |
| `GET` | `/api/v1/products` | Lấy danh sách sản phẩm | Params tùy chọn: `?keyword=sua&category=Sua tuoi` |
| `GET` | `/api/v1/products/{id}` | Xem chi tiết 1 sản phẩm | Path param: `id` |
| `POST` | `/api/v1/products` | Thêm mới 1 sản phẩm | Body: `ProductRequest` (sku, name, price, stock...) |
| `PUT` | `/api/v1/products/{id}` | Cập nhật thông tin sản phẩm | Path param: `id`, Body: `ProductRequest` |
| `DELETE` | `/api/v1/products/{id}` | Xóa sản phẩm theo ID | Path param: `id` |

---

## 🔄 5. So sánh nhanh dành cho Mobile Developer (Flutter vs Spring Boot)

| Khái niệm | Mobile (Flutter) | Backend (Spring Boot) |
| :--- | :--- | :--- |
| **Giao tiếp mạng** | Gọi API bằng `Dio` / `http` | Tiếp nhận qua `@RestController` |
| **Model gửi đi** | Request Body / Query Params | `@RequestBody ProductRequest` / `@RequestParam` |
| **Model nhận về** | Response Model (`fromJson`) | Trả về `ApiResponse<T>` (Jackson tự chuyển sang JSON) |
| **Validation** | Form validation (`validator`) | Bean Validation (`@NotBlank`, `@DecimalMin`, `@Valid`) |
| **Xử lý logic** | Provider / BLoC / Repository | `@Service` / `@Transactional` |
| **Lưu dữ liệu** | SQLite / Hive / SharedPreferences | Spring Data JPA (`ProductRepository` lưu vào Database) |
| **Bắt lỗi** | `try-catch` hoặc Error Interceptor | `@RestControllerAdvice` + `@ExceptionHandler` |

---

## 🎯 6. Bài tập thực hành tự luyện (Roadmap gợi ý)

1. **Bài tập 1 (Dễ)**: Thêm 1 trường `imageUrl` (String) vào Entity `Product`, cập nhật DTO và xem nó hiển thị thế nào trên Swagger.
2. **Bài tập 2 (Trung bình)**: Thêm API tìm kiếm sản phẩm theo khoảng giá:
   - `GET /api/v1/products/price-range?min=20000&max=50000`
   - Gợi ý: Thêm method vào `ProductRepository`: `findByPriceBetween(BigDecimal min, BigDecimal max)`.
3. **Bài tập 3 (Nâng cao)**: Tạo thêm một module mới: **Order** (Đơn hàng):
   - Tạo Entity `Order` và `OrderItem`.
   - Tạo `OrderController` cho phép khách hàng đặt mua các sản phẩm từ bảng `Product` và tự động trừ số lượng tồn kho (`stockQuantity`).

