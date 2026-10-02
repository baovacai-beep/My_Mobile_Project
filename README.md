# 🎟️ SSharing - Nền Tảng Chia Sẻ Mã Giảm Giá

> **Tiết kiệm thông minh - Chia sẻ tiện ích**
> *SSharing* là ứng dụng di động cho phép cộng đồng người dùng tìm kiếm, chia sẻ và quản lý các mã giảm giá (voucher/coupon) từ các sàn thương mại điện tử, dịch vụ ăn uống, di chuyển và mua sắm phổ biến.

---

## 📌 Mục Lục

* [Giới thiệu dự án]
* [Tính năng nổi bật]
* [Công nghệ sử dụng]
* [Cấu trúc dự án]
* [Cài đặt và Chạy ứng dụng]
* [Thành viên phát triển]
* [Giấy phép]

---

## 📖 Giới thiệu dự án

Trong thời đại mua sắm trực tuyến phát triển, nhu cầu săn mã khuyến mãi và tối ưu chi phí tiêu dùng ngày càng lớn. **SSharing** ra đời nhằm giải quyết vấn đề phân tán thông tin voucher bằng cách xây dựng một cộng đồng nơi mọi người có thể:

* Đóng góp những mã giảm giá mình không dùng đến.
* Dễ dàng tìm được mã ưu đãi còn hiệu lực cho nhu cầu cá nhân.
* Tránh lãng phí thời gian thử các mã đã hết hạn thông qua cơ chế phản hồi cộng đồng.

---

## ✨ Tính năng nổi bật

* 🔍 **Tìm kiếm & Phân loại thông minh:** Lọc mã theo danh mục (Shopee, Lazada, Grab, Đồ ăn, Du lịch, Công nghệ,...).
* 📋 **Sao chép 1 chạm:** Tự động sao chép mã voucher vào bộ nhớ tạm (Clipboard) và chuyển hướng nhanh đến ứng dụng/website mua sắm.
* ✍️ **Chia sẻ mã cộng đồng:** Người dùng có thể đăng tải mã giảm giá kèm hạn sử dụng, điều kiện áp dụng và mô tả chi tiết.
* 👍 **Đánh giá & Báo cáo:** Hệ thống bình chọn mã còn hoạt động hay đã hết hạn, giúp giữ chất lượng dữ liệu luôn chính xác.
* ⭐️ **Lưu trữ mã yêu thích:** Đánh dấu các voucher tiềm năng để sử dụng sau.
* 👤 **Quản lý tài khoản:** Xem lại các mã đã đăng, lịch sử lưu mã và điểm uy tín cộng đồng.

---

## 🛠️ Công nghệ sử dụng

| Thành phần | Công nghệ / Thư viện |
| --- | --- |
| **Nền tảng** | Android (Java / Kotlin) |
| **Kiến trúc** | MVVM (Model - View - ViewModel) |
| **UI Components** | Material Components, RecyclerView, ViewBinding |
| **Backend & Database** | Firebase (Authentication, Firestore Database, Storage) / RESTful API |
| **Xử lý bất đồng bộ** | Coroutines / RxJava / ExecutorService |
| **Tải ảnh** | Glide / Picasso |

---

## 📁 Cấu trúc dự án

```text
SSharing/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/project/ssharing/
│   │   │   │   ├── data/            # Models, Repositories, Data Sources
│   │   │   │   ├── ui/              # Activities, Fragments, Adapters, ViewModels
│   │   │   │   │   ├── auth/        # Đăng ký, Đăng nhập
│   │   │   │   │   ├── home/        # Màn hình chính, danh sách mã
│   │   │   │   │   ├── post/        # Đăng mã giảm giá mới
│   │   │   │   │   ├── saved/       # Danh sách mã đã lưu
│   │   │   │   │   └── profile/     # Trang cá nhân
│   │   │   │   └── utils/           # Helper classes, Constants, Formatters
│   │   │   └── res/                 # Layouts, Drawables, Values, Navigations
│   └── build.gradle
└── README.md

```

---

## 🚀 Cài đặt và Chạy ứng dụng

### Yêu cầu tiên quyết

* **Android Studio** (Phiên bản Jellyfish / Koala hoặc mới hơn).
* **JDK:** Phiên bản 17 hoặc 21.
* Thiết bị thật chạy Android 8.0 (API 26) trở lên hoặc Android Emulator.

### Các bước cài đặt

1. **Clone repository về máy:**
```bash
git clone https://github.com/baovacai-beep/Du_an_di_dong_cua_toi.git

```


2. **Mở dự án:**
* Mở Android Studio $\rightarrow$ Chọn **File** $\rightarrow$ **Open...** $\rightarrow$ Trỏ tới thư mục vừa clone.


3. **Cấu hình dịch vụ (nếu có sử dụng Firebase):**
* Đặt file `google-services.json` vào thư mục `app/`.


4. **Đồng bộ Gradle:**
* Nhấn nút **Sync Project with Gradle Files**.


5. **Chạy ứng dụng:**
* Chọn thiết bị máy ảo/máy thật và nhấn biểu tượng **Run** (phím tắt `Shift + F10`).



---

## 👥 Thành viên phát triển

Dự án được xây dựng và hoàn thiện bởi nhóm sinh viên:

| STT | Họ và Tên | Vai trò chính |
| --- | --- | --- |
| 1 | **Lê Nguyễn Khánh Trình** | Team Leader / Architecture & Backend Integration |
| 2 | **Lê Bảo Ngọc** | UI/UX Designer & Frontend Mobile Developer |
| 3 | **Hồ Gia Bảo** | Mobile Developer & Logic Flow |
| 4 | **Trần Gia Bảo** | Database Management & Quality Assurance (Tester) |

---

## 📄 Giấy phép

Dự án được phát triển phục vụ mục đích học tập và nghiên cứu môn Lập trình ứng dụng di động. Mọi quyền đóng góp và sử dụng mã nguồn tuân thủ theo giấy phép [MIT License](https://www.google.com/search?q=LICENSE&utm_source=gemini).
