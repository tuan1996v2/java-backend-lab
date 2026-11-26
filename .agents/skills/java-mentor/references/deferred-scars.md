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
| S1 | Bỏ object tự viết vào `Set`/`Map` key mà quên override `equals()` + `hashCode()` → `Set` không loại trùng (so sánh theo địa chỉ RAM). | Module 4 (Class / `equals` & `hashCode`) | `new Set([{sku:"A1"},{sku:"A1"}]).size === 2` vì 2 object khác tham chiếu. |
| S2 | Dùng Lombok `@Data` cho JPA `@Entity` rồi bỏ vào `Set` → `hashCode` đổi khi `id` từ `null` thành số sau `save()` → `contains()`/`remove()` trả `false` dù object vẫn nằm trong `Set`. Quy tắc: không sửa field dùng để tính `hashCode` sau khi đã bỏ vào `Set`. | Module 4 (Class) + Spring Boot phần `Order` / `OrderItem` (quan hệ 1-N) | Giống đổi `key` của item trong `FlatList` giữa chừng → React không tìm thấy item cũ. |

---

## Đã tiêu hoá

| # | Vết sẹo | Đã vấp & hiểu ở | Liên hệ React Native / JS |
| :-: | :--- | :--- | :--- |
| S3 | Gọi `List.contains()` hoặc lồng 2 vòng `for` với dữ liệu lớn → O(n²), API timeout. Đổi sang `HashMap` / `HashSet` → O(1). | Module 3 (Bài 9 `🪨`) | `arr.includes()` trong vòng `for` 10.000 phần tử làm lag JS thread; đổi sang `Map`/`Set`. |
| S4 | `BigDecimal("38000")` và `BigDecimal("38000.00")`: `HashMap`/`HashSet` coi là 2 key khác nhau (`equals` so cả scale). Phải gọi `.stripTrailingZeros()` trước khi đưa làm key. | Module 3 (Bài 11 `🪨`) | JS không có chuyện này vì `38000 === 38000.00`, nên dev JS rất dễ dính. |
| S5 | `Set.of(...)` / `List.of(...)` / `Map.of(...)`: truyền phần tử trùng → `IllegalArgumentException`, truyền `null` → `NullPointerException`, gọi `put()`/`add()` → `UnsupportedOperationException`. Chỉ dùng cho hằng số tự gõ, không sửa trực tiếp dữ liệu đầu vào. | Module 3 (Bài 6 `🪨` & Bài 12) | Giống `Object.freeze()` nhưng gắt hơn: nổ lỗi ngay chứ không im lặng bỏ qua. |
