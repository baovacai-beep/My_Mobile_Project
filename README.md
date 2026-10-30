# SSharing - Sàn Ký Quỹ & Chia Sẻ Mã Giảm Giá C2C
> **Đồ án môn học: Phát Triển Ứng Dụng Mobile**  
> **Trường Công nghệ và Thiết kế (CTD) – Đại học Kinh tế TP. Hồ Chí Minh (UEH)**  
> **Khóa:** K50 | **Chuyên ngành:** Công nghệ Phần mềm | **Học kỳ:** Cuối 2026  
> **Giảng viên hướng dẫn:** TS. Đặng Ngọc Hoàng Thành  

---

<!-- BADGES -->
<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android%20Native-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Platform" />
  <img src="https://img.shields.io/badge/Language-Java%2017%20LTS-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Language" />
  <img src="https://img.shields.io/badge/Target%20SDK-34%20(Android%2014)-34A853?style=for-the-badge&logo=android" alt="Target SDK" />
  <img src="https://img.shields.io/badge/Database-Room%20SQLite%20%7C%20Firestore-FFA000?style=for-the-badge&logo=firebase&logoColor=white" alt="Database" />
  <img src="https://img.shields.io/badge/Architecture-MVVM%20%2B%20Repository-blueviolet?style=for-the-badge" alt="Architecture" />
  <img src="https://img.shields.io/badge/Build-Gradle%208.7%20SUCCESS-brightgreen?style=for-the-badge&logo=gradle" alt="Gradle Build" />
</p>

---

##  1. Giới thiệu đề tài

Trong bối cảnh bùng nổ thương mại điện tử, phiếu giảm giá điện tử (**E-Voucher**) từ các thương hiệu lớn (*Highlands Coffee, Starbucks, Shopee, Grab, MoMo...*) được phát hành rộng rãi nhưng thường xuyên bị bỏ quên dẫn đến lãng phí, trong khi nhiều người dùng khác lại có nhu cầu mua lại với mức chiết khấu hấp dẫn. 

Giao dịch voucher tự phát qua mạng xã hội hiện nay tiềm ẩn nguy cơ **lừa đảo (scam)** rất cao: người mua chuyển tiền trước thì nhận mã giả/mã đã dùng; người bán giao mã trước thì bị quỵt tiền do thiếu cơ chế trung gian đảm bảo.

**SSharing** ra đời như một giải pháp **Sàn giao dịch mã giảm giá ngang hàng (C2C)** chuyên biệt trên nền tảng Android Native. Trọng tâm của hệ thống là **Động cơ Ký quỹ Trung gian (Escrow Engine)** giúp khóa tiền người mua trong 24 giờ để kiểm tra tính hợp lệ của voucher trước khi giải ngân, kết hợp công nghệ **AI On-Device (Google ML Kit)** bóc tách voucher bằng camera và **Cổng nạp Xu VietQR Napas247** bán tự động an toàn.

---

##  2. Các tính năng cốt lõi (Key Features)

###  2.1. Động cơ Ký quỹ Escrow & Chat P2P Realtime
* **Giao dịch nguyên tử (ACID Transaction):** Khi đặt mua, số Xu thanh toán được trừ khỏi ví khả dụng và đóng băng nguyên tử vào quỹ trung gian `soDuXuDongBangEscrow` trên Firestore. Voucher lập tức chuyển trạng thái `LOCKED` để chống mua trùng lặp (*Concurrency Control*).
* **Mở khóa mã bí mật & Đếm ngược 24h:** Người mua được mở khóa chuỗi mã bí mật kèm đồng hồ đếm lùi `CountDownTimer` 24 giờ (86.400.000 ms) chạy từng giây để trải nghiệm mã ngoài đời thực.
* **Phòng Chat P2P 1-1 đối xứng:** Hệ thống tự động sinh ID phòng chat chuẩn từ điển `CHAT_ROOM_P2P_userA_userB`, lắng nghe tin nhắn tức thời qua Firestore WebSocket (< 100 ms).
* **Giải ngân an toàn:** 
  * *Chủ động:* Người mua kiểm tra mã thành công và nhấn *'Xác nhận mã'* $\rightarrow$ Xu chuyển ngay về ví người bán.
  * *Tự động:* Quá 24 giờ không có khiếu nại $\rightarrow$ Hệ thống tự động giải phóng Xu cho người bán.
  * *Khiếu nại:* Nếu mã lỗi, người mua nhấn *'Báo lỗi / Khiếu nại'* và đính kèm tối đa 5 ảnh bằng chứng thực tế $\rightarrow$ Dòng tiền bị phong tỏa vĩnh viễn chờ Trọng tài phân xử.

###  2.2. Kinh tế Xu SShares & Cổng nạp VietQR Napas247
* **Quy chuẩn tỷ giá:** 1.000 VNĐ = 1 Xu SSharing (SS).
* **Sinh mã VietQR động:** Gọi API `VietQR.io` sinh mã QR thanh toán chuẩn Napas247 nhúng trực tiếp thông tin tài khoản MB Bank (*0913672861 - HO GIA BAO*) và cú pháp chuẩn hóa `SSHARING NAP XU <username>`.
* **Đối soát nạp Xu bán tự động:** Tích hợp Open API Casso đối soát biến động số dư ngân hàng thương mại. Sau khi chuyển khoản, người dùng nhấn *"Tôi đã chuyển khoản / Kiểm tra thanh toán"*, ứng dụng truy vấn API Casso để cộng Xu nguyên tử và lưu mã giao dịch chống nạp lặp (*Idempotency*).
* **Chế độ Sandbox Demo:** Tích hợp gói nạp thử nghiệm 2.000 VNĐ và nút *'Giả Lập Thanh Toán Sandbox'* phục vụ kiểm thử và demo trực quan trước hội đồng chấm thi.

### 📷 2.3. Quét Smart OCR & Đăng bán Voucher
* **Bóc tách văn bản qua Camera/Thư viện:** Khung quét Live Camera `TextureView` tích hợp Google ML Kit Text Recognition bóc tách văn bản thô trực tiếp trên chip thiết bị (On-Device, 0đ chi phí API, bảo mật dữ liệu).
* **Bộ lọc Regex thông minh:** Tự động trích xuất Hạn dùng (`dd/MM/yyyy`), Mã voucher in hoa, Mức giảm giá VNĐ và Tên thương hiệu đối tác.
* **Đa định dạng hiển thị:** Tự động phát sinh 3 định dạng mã chuyên biệt: **Mã vạch 1D Code 128** (quét tại quầy), **Mã QR 2D** (quét app đối tác), hoặc **Chuỗi ký tự Text** (sao chép 1 chạm).
* **Quỹ Cộng Đồng 0đ:** Tùy chọn tặng voucher 0đ cho cộng đồng để tăng điểm uy tín Trust Score.

###  2.4. Bàn Trao đổi Voucher P2P (Atomic Swap)
* Cho phép 2 người dùng trực tuyến hoán đổi trực tiếp mã voucher với nhau mà không qua thanh toán Xu.
* **Nguyên lý Hoán đổi Nguyên tử (Atomic Swap):** Giao diện chia 2 cột đối soát offer. Giao dịch chỉ thành công khi cả hai bên cùng nhấn *'Khóa Offer'* và *'Xác nhận Hoán đổi'*, đảm bảo không bên nào bị mất quyền lợi đơn phương.

###  2.5. Bản đồ Leaflet OpenStreetMap & Điều hướng Google Maps
* **Kiến trúc Crash-Proof:** Thay thế thẻ `<fragment>` cứng bằng khung nạp động `FrameLayout` trong khối try-catch, triệt tiêu 100% nguy cơ văng ứng dụng do thiếu Google API Key.
* **Bản đồ tương tác Leaflet OSM:** Nhúng WebView tải bản đồ giao thông Quận 10 & Quận 3 (TP.HCM), ghim tọa độ GPS thực tế các chi nhánh đối tác (Highlands 279 Nguyễn Tri Phương cạnh UEH Cơ sở B, Highlands Vạn Hạnh Mall, Highlands 59 Nguyễn Đình Chiểu cạnh UEH Cơ sở A).
* **Implicit Intent:** Tích hợp nút *'Mở Google Maps dẫn đường'* kích hoạt ứng dụng Google Maps chính thức của điện thoại để tìm đường đi ngắn nhất.

###  2.6. Đồng bộ Ngoại tuyến Nguồn dữ liệu duy nhất (Offline-First SSOT)
* Kiến trúc Room SQLite đóng vai trò là **Nguồn dữ liệu duy nhất (Single Source of Truth - SSOT)**.
* Giao diện UI chỉ quan sát (`observe`) `LiveData` từ Room DAO, không phụ thuộc trực tiếp vào trạng thái mạng.
* Khi mất sóng 4G/Wifi hoặc bật **Chế độ máy bay (Airplane Mode)**, ứng dụng vẫn đọc mượt mà dữ liệu đệm từ bộ nhớ Flash, đảm bảo cam kết **Không văng app (Zero-Crash Guarantee)**.

###  2.7. Thiết kế UI/UX Dark Theme & Responsive Đa hướng
* **Tone màu thương hiệu:** Phối màu Dark Fintech hiện đại `#161325` (Surface Purple), `#8B5CF6` (Primary Neon Violet), `#6EE7B7` (Mint Green số dư), `#FCD34D` (Gold Trust Score).
* **Hỗ trợ Xoay ngang (Landscape):** Toàn bộ các màn hình trọng điểm đều có tệp layout tương ứng trong thư mục `res/layout-land/`, tự động tái cấu trúc bố cục 2 cột song song khi xoay thiết bị hoặc chạy trên máy tính bảng mà không giật lag.

---

##  3. Kiến trúc hệ thống (System Architecture)

Dự án áp dụng mô hình chuẩn **MVVM (Model - View - ViewModel)** kết hợp **Repository Pattern** theo khuyến nghị của Google Android Jetpack:

```
┌────────────────────────────────────────────────────────┐
│                   VIEW LAYER (UI)                      │
│   Activities / XML Layouts / Custom Views / Adapters   │
│   (MainActivity, EscrowChatActivity, WalletActivity)   │
└───────────────────────────▲────────────────────────────┘
                            │ Observes LiveData (UI State)
┌───────────────────────────┴────────────────────────────┐
│                    VIEWMODEL LAYER                     │
│         VoucherViewModel  │  UserViewModel             │
│   (Bảo toàn trạng thái khi xoay ngang màn hình)        │
└───────────────────────────▲────────────────────────────┘
                            │ Requests / Exposes Streams
┌───────────────────────────┴────────────────────────────┐
│                   REPOSITORY LAYER                     │
│         VoucherRepository │  UserRepository            │
│   (Điều phối & đồng bộ dữ liệu giữa Cloud và Local)    │
└───────────────▲────────────────────────▲───────────────┘
                │                        │
       Cache & Read (SSOT)        Realtime Sync (WebSocket)
                │                        │
┌───────────────┴────────┐      ┌────────┴───────────────┐
│     LOCAL DATABASE     │      │     CLOUD BACKEND      │
│      (Room SQLite)     │      │   (Firebase Firestore) │
│  - UserDao             │      │  - users/              │
│  - VoucherDao          │      │  - vouchers/           │
│  - AppDatabase         │      │  - escrow_orders/      │
│  - SharedPreferences   │      │  - chat_rooms/         │
└────────────────────────┘      └────────────────────────┘
```

---

##  4. Sơ đồ thiết kế (Diagrams)

Toàn bộ tài liệu mô hình hóa hệ thống được lưu trữ trong thư mục `/Diagram`:

| Loại sơ đồ | Tên tệp / Mô tả |
| :--- | :--- |
| **Activity Diagram (AD 1 - 7)** | `AC 1 SSharing.png` đến `AC 7 SSharing.png` (Định danh eKYC, Nạp VietQR, Đăng bán OCR, Ký quỹ Escrow, Tranh chấp, Thu mua tức thì, Giựt Deal giờ vàng) |
| **Sequence Diagram (SD 1 - 7)** | `SD1 SSharing.jpg` đến `SD7 SSharing.jpg` (Trình tự gọi hàm giữa Client, Google ML Kit, Cổng Ngân hàng và Firestore Transaction) |
| **Entity Relationship (ERD)** | `ERD SSharing.png` (Cơ sở dữ liệu 13 bảng thực thể chuẩn hóa quan hệ 1-N, N-N) |
| **BPMN / Swimlane Workflow** | Quy trình phân làn 5 tác nhân (Người bán - Đối tác AI - Người mua - SSharing Escrow - Trọng tài CSKH) |

---

## 🛠️ 5. Ngăn xếp công nghệ (Tech Stack)

| Thành phần | Công nghệ / Thư viện | Phiên bản | Vai trò kỹ thuật |
| :--- | :--- | :--- | :--- |
| **Ngôn ngữ** | Java | 17 LTS | Ngôn ngữ phát triển logic, xử lý bất đồng bộ, hiện thực 4 tính chất OOP |
| **Hệ điều hành** | Android SDK | API 34 (Android 14) | Biên dịch ứng dụng, tương thích tối thiểu từ API 26 (Android 8.0+) |
| **Cơ sở dữ liệu cục bộ** | Android Jetpack Room | 2.6.1 | Cơ sở dữ liệu SQLite đệm, nguồn dữ liệu duy nhất (SSOT) |
| **Quản lý trạng thái** | ViewModel & LiveData | Lifecycle 2.7 | Quản lý vòng đời màn hình, chống tràn RAM và giữ UI State khi xoay |
| **Cơ sở dữ liệu đám mây** | Cloud Firestore | Firebase BOM 33.x | Lưu trữ phân tán NoSQL, lắng nghe biến động thời gian thực (`addSnapshotListener`) |
| **Xác thực & Mã hóa** | Java Cryptography | MD5 / SHA-256 | Băm bảo vệ mật khẩu người dùng theo tiêu chuẩn OWASP Mobile |
| **Trí tuệ nhân tạo** | Google ML Kit | Text Recognition v2 | Bóc tách ký tự phiếu voucher on-device không phụ thuộc cloud API |
| **Bản đồ tương tác** | Leaflet OpenStreetMap | JS Tile Server | Hiển thị bản đồ đường xá Quận 10 & 3 qua WebView không crash |
| **Cổng thanh toán** | VietQR.io & Casso API | Napas247 Standard | Tạo mã QR động MBBank và đối soát biến động số dư chuyển khoản |
| **Xử lý ảnh & UI** | Glide / ViewPager2 | Latest | Tải ảnh mượt mà, bộ banner quảng cáo trượt tự động mỗi 5 giây |

---

##  6. Đánh giá kiểm thử thực nghiệm (Testing Summary)

*Cam kết chất lượng thực nghiệm được kiểm chứng trên cả 2 môi trường: **Máy ảo Pixel 4 API 34** và **Thiết bị Android thật (Physical Device)**:*

| Tính năng kiểm thử | Kết quả Máy ảo (Emulator Pixel 4) | Kết quả Thiết bị thật (Physical Device) | Mức độ hoàn thiện |
| :--- | :--- | :--- | :---: |
| **Định danh eKYC & 2FA** | OTP 6 số tự động nhảy focus, mô phỏng quét mặt | Mở camera trước chụp chân dung; luồng OTP & Face chạy theo kịch bản mẫu (*Template Mock*) | **Đạt** *(Logic & UI hoàn chỉnh)* |
| **Quét Smart OCR Voucher** | Nạp ảnh mẫu từ Gallery bóc tách dữ liệu chuẩn | Mở camera chụp voucher thật; bóc tách trường thông tin theo kịch bản dữ liệu kiểm thử | **Đạt** *(Chụp & Trích xuất mẫu)* |
| **Ký quỹ Escrow & Chat P2P** | Khóa Xu nguyên tử, Timer 24h chạy từng giây | Nhắn tin thời gian thực 2 chiều, người mua duyệt giải ngân Xu thành công về ví người bán | **Đạt 90%** *(Khóa quỹ & Giải ngân chuẩn)* |
| **Đồng bộ Ngoại tuyến (SSOT)** | Bật *Airplane Mode*, đọc đệm Room SQLite | Mất sóng 4G/Wifi trong thang máy không bị văng app (*Zero-Crash Guarantee*) | **Đạt 100%** *(Chuẩn Offline-First)* |
| **Bản đồ & Điều hướng** | WebView Leaflet nạp an toàn, triệt tiêu crash | Nút *Implicit Intent* mở ứng dụng Google Maps chính thức và ghim chuẩn chi nhánh F&B | **Đạt** *(Fallback Native Maps OK)* |
| **Nạp Xu qua VietQR** | Chạy thành công nút *Sandbox Test* cộng Xu ngay | Sinh mã VietQR động MBBank; đối soát bán tự động qua nút kiểm tra gọi API Casso | **Đạt 85%** *(Quy trình bán tự động chuẩn)* |

---

##  7. Cấu trúc thư mục dự án

```text
My_Mobile_Project/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/vn/edu/ueh/baoho/ssharing/
│   │   │   │   ├── adapter/         # Adapter ViewPager2, RecyclerView danh sách voucher, banner
│   │   │   │   ├── data/
│   │   │   │   │   ├── local/       # Room Database, Entity (NguoiDung, ViTien, Voucher), DAO
│   │   │   │   │   └── remote/      # EscrowFirestoreService (Giao dịch nguyên tử Firestore)
│   │   │   │   ├── model/           # Mô hình hướng đối tượng OOP (Kế thừa, Đa hình, Interfaces)
│   │   │   │   ├── repository/      # Repository điều phối SSOT giữa SQLite và Cloud
│   │   │   │   ├── ui/              # 18 Activity nghiệp vụ (MainActivity, EscrowChat, Wallet...)
│   │   │   │   ├── utils/           # Tiện ích băm MD5, xử lý VietQR, Casso, Location, ML Kit
│   │   │   │   └── viewmodel/       # ViewModel lưu trữ trạng thái giao diện LiveData
│   │   │   ├── res/
│   │   │   │   ├── drawable/        # Bộ nhận diện nút bấm, card view, bubble chat Dark Theme
│   │   │   │   ├── layout/          # Giao diện dọc chuẩn (Portrait XML)
│   │   │   │   ├── layout-land/     # Giao diện xoay ngang 2 cột đáp ứng đa hướng (Landscape XML)
│   │   │   │   └── values/          # Bảng màu thương hiệu (#161325, #8B5CF6), chuỗi Strings, Styles
│   │   │   └── AndroidManifest.xml  # Đăng ký quyền Camera, Internet, Vị trí và các Activity
│   │   └── test/                    # Unit Tests
│   ├── build.gradle.kts             # Cấu hình dependencies, Jetpack, Room, ML Kit, Firebase
│   └── google-services.json         # Tệp kết nối Firebase Project
├── Diagram/                         # Sơ đồ Activity (AD), Sequence (SD), ERD, BPMN
├── figma/                           # Ảnh thiết kế màn hình UI & link mẫu prototype Figma
├── build.gradle.kts                 # Cấu hình Gradle Project root
├── README.md                        # Tài liệu hướng dẫn dự án
└── settings.gradle.kts              # Khai báo cấu trúc mô-đun
```

---

##  8. Hướng dẫn cài đặt & Chạy ứng dụng

### 8.1. Yêu cầu môi trường
* **Android Studio:** Phiên bản Flamingo / Koala / Ladybug trở lên.
* **JDK:** Java Development Kit 17 LTS.
* **Android SDK:** SDK 34 (Hỗ trợ thiết bị Android 8.0+ trở lên).
* **Thiết bị:** Máy ảo Android Emulator (Pixel 4 API 34 khuyến nghị) hoặc điện thoại Android thật có bật chế độ *USB Debugging*.

### 8.2. Các bước khởi chạy
1. **Clone repository về máy:**
   ```bash
   git clone https://github.com/baovacai-beep/My_Mobile_Project.git
   cd My_Mobile_Project
   ```
2. **Mở dự án trên Android Studio:**
   * Chọn `File` $\rightarrow$ `Open` $\rightarrow$ Chọn thư mục dự án vừa clone.
3. **Đồng bộ hóa Gradle dependencies:**
   * Chờ Android Studio tải các gói thư viện Jetpack, Room Compiler, Google ML Kit và Firebase SDK.
   * Hoặc chạy lệnh trong Terminal:
     ```bash
     ./gradlew clean build
     ```
4. **Cấu hình Firebase (Nếu sử dụng Firebase cá nhân):**
   * Đặt tệp `google-services.json` của Firebase Project vào thư mục `/app`.
5. **Biên dịch và cài đặt:**
   * Chọn thiết bị mục tiêu (Pixel 4 API 34 hoặc Thiết bị thật).
   * Nhấn nút **Run** (phím tắt `Shift + F10`) để bắt đầu trải nghiệm ứng dụng.

---

## 👥 9. Thành viên nhóm & Phân công nhiệm vụ (Nhóm 12)

| STT | Họ và tên | MSSV | Vai trò & Nhiệm vụ kỹ thuật chính | Đóng góp |
| :---: | :--- | :---: | :--- | :---: |
| **1** | **Hồ Gia Bảo** *(Nhóm trưởng)* | **31241023360** | • Kiến trúc Động cơ Escrow Transaction (`EscrowFirestoreService`).<br>• Cài đặt Google ML Kit OCR, eKYC Face Liveness & 2FA OTP.<br>• Thiết kế cơ sở dữ liệu Room SQLite & kiến trúc SSOT Offline Sync.<br>• Tích hợp cổng VietQR, xác thực mật khẩu băm MD5.<br>• Viết báo cáo Chương 4, 5; quản lý Sprint & Điều phối dự án. | **100%** |
| **2** | **Lê Nguyễn Khánh Trình** | **31241026087** | • Phát triển Bản đồ tương tác Leaflet OpenStreetMap trên WebView.<br>• Xử lý định vị GPS và Intent mở Google Maps điều hướng.<br>• Xây dựng Kênh Người Bán (`SellerDashboardActivity`).<br>• Phát triển Bàn Trade Voucher P2P (`P2PVoucherTradeActivity`).<br>• Xây dựng Mạng xã hội bạn bè (`SocialFriendsActivity`); Viết Chương 3, 7. | **100%** |
| **3** | **Lê Bảo Ngọc** | **31241025411** | • Thiết kế Hệ thống UI/UX Design System Dark Theme (`#161325`, `#8B5CF6`).<br>• Xây dựng màn hình Ví (`WalletActivity`), hiển thị 3 dạng mã Barcode/QR.<br>• Xây dựng màn hình Hồ sơ cá nhân & đổi Avatar (`ProfileActivity`).<br>• Cài đặt phân hệ Giựt deal Giờ vàng (`FlashGrabActivity`).<br>• Viết báo cáo Chương 2, 6; Rà soát và hoàn thiện tài liệu. | **100%** |
| **4** | **Trần Gia Bảo** | **31241026999** | • Thiết kế luồng Đăng nhập, Đăng ký (`LoginActivity`).<br>• Xây dựng Trang chủ (`MainActivity`) & Banner trượt `ViewPager2`.<br>• Bộ lọc danh mục và Tìm kiếm thời gian thực (`SearchActivity`).<br>• Xây dựng Trung tâm thông báo (`NotificationCenterActivity`).<br>• Viết báo cáo Chương 1; Kiểm thử giao diện và luồng chat. | **100%** |

> 📌 **Ghi chú về lịch sử Commit trên GitHub:** Do sự cố cấu hình sai email tài khoản Git trong một số phiên làm việc cục bộ, số lượng commit của thành viên *Trần Gia Bảo* bị phân tách thành 2 tài khoản con trên đồ thị thống kê của repository. Nhóm xác nhận tất cả 4 thành viên đều tham gia tích cực và đóng góp 100% khối lượng công việc.

---

## 🔗 10. Liên kết tài nguyên dự án (Project Deliverables)

* 💻 **Mã nguồn GitHub:** [github.com/baovacai-beep/My_Mobile_Project](https://github.com/baovacai-beep/My_Mobile_Project)
* 📁 **Tài liệu Google Drive:** [Tệp tin & Báo cáo đồ án nhóm 12](https://drive.google.com/drive/folders/1i0ph8vJlcnwAbBN4HGw40ABfG_pWjjCm?usp=sharing)
* 🎥 **Video Demo ứng dụng:** [Xem video trên YouTube](https://youtu.be/lgF6j9HjMbY?si=TnBXE5LdYgQl1Kro)
* 🎨 **Slide thuyết trình Canva:** [Trình chiếu Canva Slide SSharing](https://www.canva.com/design/DAHZbdl-_aw/78LfMrjwS-x_D5PBdG2cFw/edit)
* 📐 **Bản vẽ thiết kế Figma:**
  * [Giao diện mẫu tổng thể SSharing](https://www.figma.com/design/sPeTVcmbdCHIZHt0aXeYkn/Giao-di%E1%BB%87n-mobiles-nh%C3%B3m-12?node-id=0-1&t=wxDH8fFDbRRpCyZz-0)
  * [Prototype Bản đồ & Chi tiết Voucher](https://www.figma.com/proto/S5qlqJuZ3EmHMrN9pS7eCl/Mobile_UI_Map_And_VoucherDetail?node-id=3-5&t=AFHR7Sg0byPbrfnh-1)

---

##  11. Lời cảm ơn (Acknowledgments)

Nhóm 12 xin bày tỏ lòng biết ơn sâu sắc đến:
* **Ban Giám hiệu Trường Công nghệ và Thiết kế (CTD) – Đại học Kinh tế TP. Hồ Chí Minh (UEH)** đã tạo điều kiện học tập và nghiên cứu tốt nhất.
* **TS. Đặng Ngọc Hoàng Thành**, giảng viên trực tiếp giảng dạy học phần *Phát Triển Ứng Dụng Mobile*, người thầy đã tận tình định hướng kỹ thuật, phản biện và đồng hành cùng nhóm trong suốt quá trình hoàn thiện đồ án này.

---
<p align="center">
  <i>Đồ án môn học Phát Triển Ứng Dụng Mobile - K50 UEH CTD © 2026 SSharing Team. All rights reserved.</i>
</p>
