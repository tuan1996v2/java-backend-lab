# Vết sẹo để dành (Deferred Senior Scars)

Những bài học người học **đã nghe qua nhưng chưa tiêu hoá được** vì thuộc kiến thức chưa học.
**Quy tắc cho mentor:** Khi bắt đầu module/chủ đề ghi ở cột "Nhắc lại khi học", phải:
1. Nhắc lại vết sẹo đó bằng ngôn ngữ dễ hiểu + liên hệ React Native.
2. Gài ít nhất 1 bài tập "hòn đá tảng" để người học **tự vấp** đúng lỗi đó.
3. Sau khi người học đã vấp và hiểu, chuyển dòng đó sang mục "Đã tiêu hoá" ở cuối file.

---

## Đang chờ (chưa học tới)

| # | Vết sẹo | Nhắc lại khi học | Liên hệ React Native / JS |
| :-: | :--- | :--- | :--- |
| S1 | Bỏ object tự viết vào `Set`/`Map` key mà quên override `equals()` + `hashCode()` → `Set` không loại trùng (so sánh theo địa chỉ RAM). | Module 3 (`Map`) và Module 4 (Class / `record`) | `new Set([{sku:"A1"},{sku:"A1"}]).size === 2` vì 2 object khác tham chiếu. |
| S2 | Dùng Lombok `@Data` cho JPA `@Entity` rồi bỏ vào `Set` → `hashCode` đổi khi `id` từ `null` thành số sau `save()` → `contains()`/`remove()` trả `false` dù object vẫn nằm trong `Set`. Quy tắc: không sửa field dùng để tính `hashCode` sau khi đã bỏ vào `Set`. | Module 4 (Class) + Spring Boot phần `Order` / `OrderItem` (quan hệ 1-N) | Giống đổi `key` của item trong `FlatList` giữa chừng → React không tìm thấy item cũ. |
| S3 | Gọi `List.contains()` trong vòng `for` với dữ liệu lớn → O(n²), API timeout. Đổi sang `HashSet` → O(1). | Module 3 (`Map`) và Module 6 (Stream), bài import Excel nhiều dòng | `arr.includes()` trong vòng `for` 10.000 phần tử làm lag JS thread; đổi sang `Set.has()`. |
| S4 | `BigDecimal("100.0")` và `BigDecimal("100.00")`: `HashSet` coi là 2 phần tử (`equals` so cả scale), `TreeSet` coi là 1 phần tử (`compareTo`). Không dùng `BigDecimal` làm key của `Set`/`Map` (hoặc `stripTrailingZeros()` trước). | Module 3 (`Map` key) | JS không có chuyện này vì `100.0 === 100.00`, nên dev JS rất dễ dính. |
| S5 | `Set.of(...)` / `List.of(...)` / `Map.of(...)`: truyền phần tử trùng → `IllegalArgumentException`, truyền `null` → `NullPointerException`, gọi `add()` → `UnsupportedOperationException`. Chỉ dùng cho hằng số tự gõ, không dùng cho dữ liệu động từ API/DB. | Module 3 (`Map.of`) và Module 5 (Exception) | Giống `Object.freeze()` nhưng gắt hơn: nổ lỗi ngay chứ không im lặng bỏ qua. |

---

## Đã tiêu hoá

*(Chưa có — chuyển dòng từ bảng trên xuống đây sau khi người học đã tự vấp và hiểu.)*
