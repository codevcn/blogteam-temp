# Website viết blog

> Blog Web App.

## Mục lục (Table Content):

-   [Các đối tượng chính](#objects)
-   [Các tính năng chính](#features)
-   [Công nghệ sử dụng](#used-techs)
-   [Mẫu giao diện người dùng](#ui-template)
-   [Cấu trúc thư mục trong dự án](#folder-structure)
    -   [Các thư mục](#folder-structure-folders)
        -   [Thư mục src](#src-folder)
        -   [Thư mục resources](#resources-folder)
    -   [Các file](#folder-structure-files)
-   [Quy trình chạy dự án](#app-running-procedure)

## Giới thiệu ứng dụng:

Một dự án SSR (server side rendering) dựa trên Spring Boot và Java.

## Các đối tượng chính (Objects): <a name="objects"></a>

-   Admin: Quản trị viên
-   User: Người dùng
-   Post: Bài đăng

## Các tính năng chính (Features): <a name="features"></a>

1. Xác thực và Ủy quyền:

-   Đăng ký: User và Admin có quyền đăng ký tài khoản trên website.
-   Đăng nhập: User và Admin có quyền đăng nhập vào website.
-   Đăng xuất: User và Admin có quyền đăng xuất tài khoản khỏi website.

2. Quản lý blog:

-   Người dùng có quyền đăng / xem / sửa / xóa Post trên website.

3. Tương tác:

-   User có quyền bình luận và thả like vào các Post của User khác.

4. Tìm kiếm:

-   Tìm theo Post: User có quyền tìm kiếm các Post có trong hệ thống thông qua thanh tìm kiếm.
-   Tìm theo User: Admin có quyền tìm kiếm các Post và các User có trong hệ thống thông qua thanh tìm kiếm.

## Công nghệ sử dụng (Stack): <a name="used-techs"></a>

-   Ngôn ngữ lập trình:
    -   Java
-   Trình quản lí thư viện bên thứ 3:
    -   Maven (mvn)
-   Server:
    -   Spring Boot
-   Client:
    -   HTML, CSS, JavaScript
-   Hệ quản trị cơ sở dữ liệu:
    -   SQL Server

## Mẫu giao diện người dùng (UI Templates): <a name="ui-template"></a>

_Tự thiết kế_

## Cấu trúc thư mục trong dự án (Folder Structure): <a name="folder-structure"></a>

### Các thư mục: <a name="folder-structure-folders"></a>

1. Thư mục `java\com\example\demo`: <a name="src-folder"></a>

-   `configs`:
    -   Đây là thư mục chứa các file dùng để cấu hình cho mọi thứ trong dự án, ví dụ: cấu hình CORS, cấu hình Env, vân vân...
-   `controllers`:
    -   Đây là thư mục chứa các file controller (các trình điều khiển để xử lí các cuộc gọi api).
-   `models`:
    -   Đây là thư mục chứa các file model (các thực thể cho các đối tượng trong cơ sở dữ liệu).
-   `DAOs`:
    -   Đây là thư mục chứa các file DAO.
-   `services`:
    -   Đây là thư mục chứa các class và method phục vụ cho từng nghiệp vụ cụ thể của ứng dụng.
-   `utils`:
    -   Đây là thư mục chứa các file dùng chung, tức là các file này khai báo các hàm hoặc class để tái sử dụng ở nhiều nơi khác nhau trong dự án.

2. Thư mục `resources`: <a name="resources-folder"></a>

-   `static`:
    -   Nơi để các file css, javascript và các file ảnh.
-   `templates`:
    -   Nơi để các file html.
    -   Thư mục `fragments` dùng để chứa các file html ở dạng component (dùng cho mục đích tái sử dụng, tách mã html, ...).

### Các file: <a name="folder-structure-files"></a>

-   file `App.java`:
    -   Đây là file gốc của toàn bộ dự án, file này chịu trách nhiệm chạy dự án, tất cả các file ở các thư mục trên sẽ được import vào file này để chạy.
    -   File này có thể được xem là file gốc rễ của dự án, là file cao nhất của dự án.
-   file `application.properties`:
    -   Đây là file chứa các dữ liệu nhạy cảm hoặc cấu hình của dự án, ví dụ như thông tin đăng nhập vào cơ sở dữ liệu, các khóa bí mật của thư viện bên thứ 3, vân vân...
-   file `pom.xml`:
    -   Đây là hồ sơ của dự án (bộ mặt của dự án), tức là file khai báo các thư viện được sử dụng trong dự án, mô tả cho dự án, tên dự án, phiên bản dự án, các cấu hình để chạy dự án trên môi trường development hoặc production, vân vân...
-   file `run.cmd`:
    -   Đây là file chứa original command để chạy Spring Boot.

## Quy trình chạy dự án (Install & Run): <a name="app-running-procedure"></a>

Sau khi clone repo từ github về máy, chạy các lệnh sau trong CLI (cmd hoặc terminal):

-   cmd:

```
cd ./blogteam
run.cmd
```

-   terminal:

```
cd ./blogteam
./run
```
