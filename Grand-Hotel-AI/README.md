# Grand Hotel AI Fusion — Frontend + Backend Java

Dự án lấy giao diện từ file HTML bạn gửi và bổ sung backend Java Spring Boot. Trang web được Spring Boot phục vụ ngay tại cùng địa chỉ với API; dữ liệu nghiệp vụ được lưu vào cơ sở dữ liệu H2 dạng file trong thư mục `data/`.

## Cấu trúc

- `backend/src/main/resources/static/index.html`: frontend HTML/CSS/JavaScript của file gốc, đã kết nối API lưu/đọc dữ liệu.
- `backend/src/main/java/vn/grandhotel/fusion/`: backend Spring Boot và REST API.
- `backend/src/main/resources/application.properties`: cổng chạy và cấu hình H2.

## Chạy trên Windows bằng VS Code

1. Cài **JDK 17** và **Apache Maven 3.6.3 trở lên**. Trong Terminal của VS Code, kiểm tra:

   ```powershell
   java -version
   mvn -version
   ```

2. Mở thư mục `backend` trong VS Code.
3. Mở Terminal → New Terminal, sau đó chạy:

   ```powershell
   mvn spring-boot:run
   ```

   Lần chạy đầu cần Internet để Maven tải các thư viện.

4. Chờ dòng `Started GrandHotelApplication`, rồi mở trình duyệt tại:

   **http://localhost:8080**

5. Tài khoản demo có sẵn trong file giao diện:
   - Quản trị: `admin@gmail.com` / `admin123`
   - Lễ tân: `letan@gmail.com` / `letan123`

   Đây là tài khoản minh họa, không dùng cho website thật.

Dừng máy chủ bằng `Ctrl + C` trong Terminal. Dữ liệu backend được lưu dưới `backend/data/`; không xóa thư mục này nếu muốn giữ dữ liệu.

## API backend

- `GET /api/health`: kiểm tra backend.
- `GET /api/hotel-data`: đọc dữ liệu nghiệp vụ.
- `PUT /api/hotel-data`: lưu dữ liệu nghiệp vụ.

Ví dụ kiểm tra nhanh khi ứng dụng đang chạy:

```powershell
Invoke-RestMethod http://localhost:8080/api/health
```

## Tạo địa chỉ riêng để mở trên thiết bị khác

### Trong cùng Wi-Fi/LAN (riêng tư trong mạng nội bộ)

1. Máy chạy Spring Boot và điện thoại/máy khác phải cùng Wi-Fi.
2. Trên máy chủ, mở PowerShell và chạy `ipconfig`; tìm **IPv4 Address** của Wi-Fi, ví dụ `192.168.1.25`.
3. Cho phép Java/Maven qua Windows Firewall khi Windows hỏi. Nếu không hiện hộp thoại, tạo quy tắc inbound TCP cho cổng `8080` trong Windows Defender Firewall.
4. Từ thiết bị kia, mở `http://192.168.1.25:8080` (thay bằng IPv4 thực tế).

Đây là địa chỉ nội bộ: người ngoài Wi-Fi không truy cập được. IP có thể đổi khi router cấp lại địa chỉ. Muốn giữ nguyên, đặt DHCP reservation cho máy trong cài đặt router.

### Địa chỉ riêng có tên miền trên Internet

Cần có máy chủ Internet để chạy **Java backend**, tên miền bạn sở hữu, DNS trỏ tới máy chủ, HTTPS và đăng nhập được bảo vệ. Chỉ tải HTML lên hosting tĩnh sẽ không chạy backend Java. Không nên mở trực tiếp cổng `8080` của máy cá nhân ra Internet.

Trước khi đưa bản này lên Internet, cần thay tài khoản demo, chuyển xác thực sang backend (mật khẩu băm, phân quyền và phiên đăng nhập an toàn), cấu hình HTTPS, sao lưu cơ sở dữ liệu và giới hạn quyền truy cập. Bản hiện tại là bản học tập/demo; tài khoản nhân viên vẫn được khai báo ở frontend. Địa chỉ `localhost` chỉ dùng trên chính máy đang chạy ứng dụng; địa chỉ LAN chỉ dùng trong mạng nội bộ.

## Giới hạn của bản hiện tại

- Giao diện đã đồng bộ dữ liệu nghiệp vụ lên backend để các thiết bị dùng chung dữ liệu phòng/đặt phòng/dịch vụ/báo cáo.
- Tài khoản khách hàng và phiên đăng nhập của file gốc vẫn lưu trên trình duyệt; chúng chưa được chuyển thành xác thực backend.
- Tư vấn AI đang giữ cơ chế mô phỏng/endpoint cấu hình trong giao diện. Không đưa API key vào file HTML; muốn gọi AI thật cần thêm proxy bảo vệ key ở backend.
