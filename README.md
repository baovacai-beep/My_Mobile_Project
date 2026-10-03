Markdown
# 🎟️ SSharing - Nền Tảng Chia Sẻ & Giao Dịch Voucher Ký Quỹ (Escrow)

> **Dự án Lập trình Ứng dụng Di động (Mobile Development Project)**  
> **Nền tảng:** Android Native (Java) | **Kiến trúc:** Gradle Kotlin DSL (`build.gradle.kts`) | **Package:** `vn.edu.ueh.baoho.ssharing`

---

## 📖 1. Giới thiệu Dự án (Overview)

**SSharing** là giải pháp ứng dụng di động cho phép người dùng chia sẻ, mua bán và trao đổi lại các mã giảm giá, voucher dịch vụ (ẩm thực, mua sắm, giải trí, di chuyển...) chưa qua sử dụng. 

Điểm cốt lõi của nền tảng là cơ chế **Giao dịch bảo đảm / Ký quỹ (Escrow Payment)** kết hợp tính năng **Trò chuyện trực tiếp (Escrow Chat)** và **Bản đồ định vị cửa hàng (Store Locator)**, giúp loại bỏ triệt để rủi ro voucher giả, mã hết hạn hoặc gian lận trong giao dịch P2P.

---

## 👥 2. Phân công Thành viên & Giao diện Đảm nhiệm (Work Division)

Dự án hiện tại đã hoàn thành toàn bộ **Giao diện người dùng (UI/UX Mockup)** theo phong cách **Dark Modern Theme** (`#13101E`) và hệ thống điều hướng activity:

| STT | Thành viên | Màn hình đảm nhiệm (Activities & Layouts) | Vai trò & Nghiệp vụ cốt lõi |
| :---: | :--- | :--- | :--- |
| **1** | **Trần Gia Bảo** | • `MainActivity` (`activity_main.xml`)<br>• `LoginActivity` (`activity_login_ui.xml`) | **Trang chủ & Luồng Xác thực:** Dựng layout trang chủ khám phá ưu đãi, phân loại danh mục thương hiệu (Shopee, Grab, Highlands...) và form đăng nhập chuẩn bảo mật. |
| **2** | **Hồ Gia Bảo** | • `SellerDashboardActivity` (`activity_seller_dashboard.xml`)<br>• `EscrowChatActivity` (`activity_escrow_chat.xml`)<br>• `ProfileActivity` (`activity_profile.xml`) | **Kênh Bán, Ký quỹ & Tín nhiệm:** Bảng điều khiển tài chính người bán, luồng chat thương lượng có banner ký quỹ giữ tiền an toàn và trang hồ sơ hiển thị điểm tín nhiệm (Trust Score). |
| **3** | **Lê Bảo Ngọc** | • `WalletActivity` (`activity_wallet.xml`)<br>• `PostVoucherActivity` (`activity_post_voucher.xml`) | **Ví người dùng & Đăng bán Voucher:** Quản lý số dư khả dụng/chờ giải ngân, lịch sử biến động số dư và biểu mẫu đăng bán voucher kèm khu vực tải ảnh mã QR. |
| **4** | **Khánh Trình** | • `VoucherDetailActivity` (`activity_voucher_detail.xml`)<br>• `MapStoreLocatorActivity` (`activity_map_store_locator.xml`) | **Chi tiết Voucher & Định vị chi nhánh:** Hiển thị thông số giảm giá, điều kiện áp dụng, thông tin người bán và bản đồ tích hợp danh sách chi nhánh áp dụng voucher gần nhất. |

---

## 🏗️ 3. Kiến trúc Hệ thống & Tài liệu Phân tích (System Diagrams)

Toàn bộ tài liệu phân tích và thiết kế hệ thống được lưu trữ đầy đủ trong thư mục dự án:

* **Sơ đồ Lớp (Class Diagram):** `Diagram/Class Diagram.png`  
  Mô hình hóa các thực thể cốt lõi trong hệ thống: `User`, `Voucher`, `Order`, `EscrowTransaction`, `Wallet`.
* **Sơ đồ Thực thể - Quan hệ (ERD):** `Diagram/EDR.png`  
  Mô hình cơ sở dữ liệu quan hệ, thể hiện liên kết khóa ngoại và ràng buộc toàn vẹn dữ liệu cho giao dịch escrow.
* **Sơ đồ Phân rã Chức năng (Function Diagram):** `Diagram/Function Diagram.jpg`  
  Phân tích chi tiết luồng xử lý từ lúc đăng tải voucher, giữ tiền ký quỹ, bàn giao mã, đến khi giải ngân hoàn tất.
* **Nguyên mẫu Thiết kế (UI Mockup Overview):** `figma/`  
  Bao gồm file tổng quan `figma_overview.png` và ảnh export chi tiết của từng màn hình tương ứng.

---

## 📂 4. Cấu trúc Thư mục Mã nguồn (Project Structure)

```text
My_Mobile_Project/
├── Diagram/                        # Bản vẽ phân tích hệ thống (Class, ERD, Functional)
├── figma/                          # Ảnh chụp thiết kế màn hình chuẩn UI/UX
├── app/
│   ├── build.gradle.kts            # Cấu hình module app & dependencies
│   └── src/main/
│       ├── AndroidManifest.xml     # Đăng ký 9 Activities và cấu hình quyền ứng dụng
│       ├── java/vn/edu/ueh/baoho/ssharing/
│       │   ├── MainActivity.java               # Trang chủ + Menu test nhanh các màn hình
│       │   ├── LoginActivity.java              # Đăng nhập & Xác thực
│       │   ├── SellerDashboardActivity.java    # Bảng điều khiển kênh người bán
│       │   ├── EscrowChatActivity.java         # Chat giao dịch giữ tiền ký quỹ
│       │   ├── ProfileActivity.java            # Hồ sơ người dùng & Điểm tín nhiệm
│       │   ├── WalletActivity.java             # Ví điện tử & Quản lý số dư
│       │   ├── PostVoucherActivity.java        # Biểu mẫu đăng bán voucher mới
│       │   ├── VoucherDetailActivity.java      # Chi tiết ưu đãi & Điều kiện áp dụng
│       │   └── MapStoreLocatorActivity.java    # Bản đồ tra cứu địa điểm sử dụng voucher
│       └── res/
│           ├── drawable/           # Bộ tài nguyên bo góc, nút tím, bóng chat, icon vector
│           ├── layout/             # 9 file giao diện XML tương ứng cho các Activity
│           ├── values/             # Bảng màu (colors.xml), chuỗi (strings.xml), theme/style
│           └── values-night/       # Cấu hình tối ưu chế độ nền tối (Dark Mode)
├── build.gradle.kts                # Cấu hình root build Gradle
└── settings.gradle.kts             # Khai báo cấu hình dự án

🚀 5. Hướng dẫn Chạy Thử nghiệm (How to Run & Test)
Mở dự án: Khởi động Android Studio, chọn Open và dẫn tới thư mục My_Mobile_Project.

Đồng bộ mã nguồn: Chờ Android Studio chạy Gradle Sync tự động.

Biên dịch (Build):

Nhấn tổ hợp phím Ctrl + F9 (hoặc biểu tượng cây búa) để Make Project.

Dự án đạt trạng thái BUILD SUCCESSFUL không có lỗi biên dịch.

Kiểm thử trên thiết bị (Run App):

Kết nối thiết bị thật (bật USB Debugging) hoặc khởi chạy máy ảo Android Emulator (Pixel 4 / API 30+).

Nhấn Shift + F10 (hoặc biểu tượng Run tam giác xanh).

Menu Điều Hướng Demo: Tại góc trên bên phải màn hình chính (MainActivity), bấm vào biểu tượng Menu 3 chấm để chuyển trực tiếp đến bất kỳ màn hình nào của các thành viên trong nhóm phục vụ việc chấm điểm và đánh giá giao diện.

📌 6. Kế hoạch Phát triển Tiếp theo (Roadmap)
[x] Thiết kế UI/UX Mockup trên Figma & xây dựng sơ đồ hệ thống (Class, ERD, Functional).

[x] Chuyển đổi toàn bộ layout sang XML Native và khai báo đầy đủ AndroidManifest.

[x] Hoàn thiện hệ thống điều hướng tập trung phục vụ kiểm thử.

[ ] Xây dựng Database Backend (Firebase Firestore / SQLite Room) cho User & Voucher.

[ ] Lập trình logic tính toán số dư ví và trạng thái giao dịch Ký quỹ (Escrow State Machine).

[ ] Tích hợp Google Maps SDK cho tính năng Store Locator.
