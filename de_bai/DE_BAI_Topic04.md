> Bản Markdown chuyển tự động từ `Topic04_University_Lecturer_Management_System.docx` để đọc nhanh. Khi có khác biệt, file .docx gốc là chuẩn.

# BÀI TẬP LỚN – LẬP TRÌNH HƯỚNG ĐỐI TƯỢNG JAVA

TOPIC 4: University Lecturer Management System

Chủ đề: Quản lý giảng viên đại học. Sinh viên xây dựng ứng dụng Java Console theo các yêu cầu dưới đây.

## 1. Mục tiêu học tập

- Phân tích bài toán thành class, object, attributes, methods và relationships; biểu diễn bằng UML Class Diagram.
- Thực hành encapsulation, abstraction, inheritance và polymorphism khi phù hợp; sử dụng ArrayList, Comparable và Collections.sort.
- Đọc/ghi dữ liệu văn bản bằng Java I/O; kiểm tra dữ liệu đầu vào, xử lý ngoại lệ và thực hiện kiểm thử.
- Trình bày thiết kế, chạy thử và giải thích vai trò của từng class trong chương trình.
## 2. Bối cảnh và dữ liệu

Một đơn vị cần hệ thống quản lý giảng viên đại học chạy trên Java Console. Hệ thống quản lý danh mục Faculty và các bản ghi Lecturer. Mỗi Lecturer thuộc đúng một Faculty; một Faculty có thể liên kết với nhiều Lecturer. Dữ liệu được nạp từ hai tệp văn bản khi khởi động và lưu lại theo lựa chọn của người dùng.

### 2.1. File facultys.txt

Định dạng: facultyID, facultyName, office, researchBudget

| facultyID | facultyName | office | researchBudget |
|---|---|---|---|
| F01 | Computer Science | A301 | 600000000 |
| F02 | Business | B201 | 350000000 |
| F03 | Languages | C102 | 200000000 |

### 2.2. File lecturers.txt

Định dạng: lecturerID, faculty, fullName, academicRank, teachingHours

| lecturerID | faculty | fullName | academicRank | teachingHours |
|---|---|---|---|---|
| L01 | F01 | Nguyen An | Senior Lecturer | 180 |
| L02 | F02 | Tran Binh | Lecturer | 210 |
| L03 | F01 | Le Chi | Associate Professor | 120 |

## 3. Chức năng bắt buộc (Console Menu)

| Option | Menu item (English) | Yêu cầu thực hiện |
|---|---|---|
| 1 | List all Facultys | Hiển thị toàn bộ danh mục Faculty. |
| 2 | Add a new Faculty | Thêm Faculty mới và kiểm tra dữ liệu. |
| 3 | Search Faculty by ID | Tìm Faculty theo mã định danh. |
| 4 | Update Faculty | Cập nhật thông tin Faculty theo mã. |
| 5 | Save Facultys to facultys.txt | Ghi danh sách Faculty ra tệp. |
| 6 | List all Lecturers sorted by facultyName | Hiển thị Lecturer tăng dần theo facultyName, nếu trùng thì theo lecturerID. |
| 7 | Search Lecturers by partial facultyName | Lọc Lecturer khi facultyName chứa chuỗi nhập (không phân biệt hoa/thường). |
| 8 | Add a new Lecturer | Thêm Lecturer; chọn Faculty từ menu. |
| 9 | Remove Lecturer by ID | Xóa Lecturer theo mã. |
| 10 | Update Lecturer by ID | Cập nhật Lecturer theo mã; kiểm tra ràng buộc. |
| 11 | Save Lecturers to lecturers.txt | Ghi danh sách Lecturer ra tệp. |
| 0 | Exit | Thoát chương trình; không tự động ghi đè dữ liệu. |

## 4. Ràng buộc dữ liệu

### 4.1. Faculty

- facultyID (String): không được trùng hoặc rỗng.
- facultyName (String): không được rỗng.
- office (String): không được rỗng.
- researchBudget (double): phải lớn hơn 0.
### 4.2. Lecturer

- lecturerID (String): không được trùng hoặc rỗng.
- faculty (Faculty): phải tồn tại trong FacultyList; lựa chọn qua menu.
- fullName (String): không được rỗng.
- academicRank (String): không được rỗng.
- teachingHours (int): phải không âm.
Khi cập nhật, kiểm tra trùng mã với các bản ghi khác; khi đọc tệp, phát hiện dòng sai định dạng hoặc tham chiếu không tồn tại và thông báo rõ lỗi. Không sử dụng phép so sánh == để đối chiếu nội dung String.

## 5. Phân tích và thiết kế UML

Sinh viên vẽ UML Class Diagram với ký hiệu - (private), + (public), quan hệ association, inheritance và interface implementation. Tên class, attributes và methods giữ tiếng Anh.

### Class Faculty

| Attributes / Relationships | Methods |
|---|---|
| - facultyID: String<br>- facultyName: String<br>- office: String<br>- researchBudget: double | + Faculty() <br>+ Faculty(String, String, String, double)<br>+ getters/setters<br>+ toString(): String |

### Class Lecturer

| Attributes / Relationships | Methods |
|---|---|
| - lecturerID: String<br>- faculty: Faculty<br>- fullName: String<br>- academicRank: String<br>- teachingHours: int | + Lecturer() <br>+ Lecturer(String, Faculty, String, String, int)<br>+ getters/setters<br>+ toString(): String<br>+ screenString(): String<br>+ compareTo(other: Lecturer): int |

### Class FacultyList

| Attributes / Relationships | Methods |
|---|---|
| (none) | + FacultyList()<br>+ loadFromFile(filename: String): boolean<br>+ saveToFile(filename: String): boolean<br>+ searchID(id: String): int<br>+ getUserChoice(): Faculty<br>+ addFaculty(): void<br>+ updateFaculty(): void<br>+ listFacultys(): void |

### Class LecturerList

| Attributes / Relationships | Methods |
|---|---|
| - facultyList: FacultyList | + LecturerList(FacultyList)<br>+ loadFromFile(filename: String): boolean<br>+ saveToFile(filename: String): boolean<br>+ searchID(id: String): int<br>+ addLecturer(): void<br>+ removeLecturer(): boolean<br>+ updateLecturer(): boolean<br>+ listLecturers(): void<br>+ printBasedFacultyName(): void |

### Class Menu

| Attributes / Relationships | Methods |
|---|---|
| (none) | + int_getChoice(options: ArrayList&lt;E&gt;): int<br>+ ref_getChoice(options: ArrayList&lt;E&gt;): E |

### Class LecturerManager

| Attributes / Relationships | Methods |
|---|---|
| (none) | + main(args: String[]): void |

Quan hệ UML: Lecturer → Faculty (association); FacultyList extends ArrayList&lt;Faculty&gt;; LecturerList extends ArrayList&lt;Lecturer&gt;; Lecturer implements Comparable&lt;Lecturer&gt;; LecturerManager sử dụng Menu và hai danh sách.

## 6. Yêu cầu cài đặt và xử lý

- Tên project: LecturerPrj. Tổ chức các class theo thiết kế, phân chia trách nhiệm rõ ràng.
- Nạp facultys.txt trước lecturers.txt để giải quyết liên kết Lecturer.faculty.
- Sắp xếp Lecturer theo facultyName bằng compareTo và Collections.sort; nếu trùng, so sánh ID.
- Hàm searchID trả về vị trí tìm thấy hoặc -1; dùng equals/equalsIgnoreCase phù hợp.
- toString() tạo dữ liệu ghi tệp; screenString() tạo chuỗi hiển thị; dùng try-with-resources cho file I/O.
- Dữ liệu đầu vào không hợp lệ phải được yêu cầu nhập lại; lựa chọn menu không hợp lệ không làm dừng chương trình.
- Không yêu cầu giao diện đồ họa hoặc cơ sở dữ liệu; không nộp lời giải bằng ngôn ngữ khác Java.
## 7. Kịch bản kiểm thử bắt buộc

| Test | Test case / Expected result |
|---|---|
| 01 | Nạp và hiển thị đủ 3 bản ghi Faculty ban đầu. |
| 02 | Thêm Faculty mới; thử mã trùng, tên rỗng, giá trị số sai. |
| 03 | Tìm Faculty với ID tồn tại và không tồn tại. |
| 04 | Sửa một Faculty đã có; xác nhận dữ liệu sau sửa. |
| 05 | Lưu facultys.txt; mở lại và đối chiếu. |
| 06 | Sắp xếp Lecturer theo facultyName rồi ID; thử trường hợp trùng facultyName. |
| 07 | Tìm Lecturer theo một phần facultyName; thử chuỗi có và không có kết quả. |
| 08 | Thêm Lecturer mới; chọn Faculty từ menu, thử mã trùng và tham chiếu sai. |
| 09 | Xóa Lecturer với ID không có và ID có trong danh sách. |
| 10 | Cập nhật Lecturer; thử giá trị hợp lệ và không hợp lệ. |
| 11 | Lưu lecturers.txt, chạy lại chương trình và kiểm tra dữ liệu. |
| 12 | Kiểm tra tệp thiếu, dòng sai định dạng, menu nhập chữ thay vì số. |

## 8. Sản phẩm sinh viên phải nộp

- Source code Java chạy được và có main(String[] args).
- Hai file dữ liệu facultys.txt và lecturers.txt đúng định dạng, tối thiểu 3 bản ghi mỗi file.
- UML Class Diagram với attributes, methods, visibility và relationships.
- Báo cáo ngắn: phân tích yêu cầu, mô tả thuật toán, hướng dẫn chạy, ảnh chụp kiểm thử 11 chức năng.
- Giải thích được encapsulation, inheritance, polymorphism, abstraction, Collections và Java I/O trong thiết kế.
## 9. Lưu ý và mở rộng

Dữ liệu mẫu chỉ dùng để khởi tạo và kiểm thử, không giới hạn dữ liệu người dùng được phép nhập. Sinh viên có thể đề xuất chức năng mở rộng để nâng cấp triển khai.
