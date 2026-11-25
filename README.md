# LecturerPrj – University Lecturer Management System

Bài tập lớn Lập trình hướng đối tượng (Java Console) – Nhóm 1 – Topic 04.

**Trước khi code, đọc [QUY_UOC.md](QUY_UOC.md).** Kế hoạch và phân công: `Nhom_1_Plan.xlsx`.

## Tài liệu

| Thư mục / file                                              | Nội dung                                                                                                                        |
| ------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------------------------------- |
| [de_bai/DE_BAI_Topic04.md](de_bai/DE_BAI_Topic04.md)           | Đề bài Topic 04 dạng Markdown, đọc nhanh trên GitHub/IDE                                                                  |
| `de_bai/Topic04_University_Lecturer_Management_System.docx` | Đề bài gốc (chuẩn khi có khác biệt)                                                                                      |
| `docs/`                                                     | Báo cáo `Nhom_1_Bao_cao.docx`, biên bản họp lần 1–3, tài liệu kiểm thử `Nhom_1_Kiem_thu.xlsx`, slide `Nhom_1_Slides.pptx`, ảnh UML |
| `docs/anh_kiem_thu/`                                        | Ảnh chụp màn hình kết quả chạy từng chức năng (dùng trong báo cáo mục 8.2 và slide)                                       |
| `Picture/`                                                  | Ảnh minh chứng các buổi họp                                                                                                 |
| `LTHDT_Nhom_1/`                                             | Bài nộp theo cấu trúc mẫu nộp bài của giảng viên (1.Document, 2.Code, 3.Present, 4.Orthers) – tạo lại bằng `bash dong_goi.sh`, không sửa trực tiếp |

Không để file `.zip`/`.rar` trong dự án: giải nén vào thư mục tương ứng ở trên rồi mới commit.

## Chạy chương trình

Yêu cầu JDK 8 trở lên. Đứng ở thư mục gốc `LecturerPrj` (nơi có `facultys.txt`):

```bash
javac -encoding UTF-8 -d out $(find src -name "*.java")
java -cp out lecturerprj.LecturerManager
```

Windows (PowerShell):

```powershell
javac -encoding UTF-8 -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
java -cp out lecturerprj.LecturerManager
```

Chạy bằng IDE: mở thư mục `LecturerPrj`, đánh dấu `src` là thư mục source, chạy `LecturerManager`.
**Working directory phải là thư mục gốc project**, nếu không chương trình sẽ không tìm thấy file `.txt`.

## Cấu trúc & người phụ trách

| File                                                            | Người phụ trách | Chức năng menu   |
| --------------------------------------------------------------- | ------------------- | ------------------ |
| `LecturerManager.java`                                        | TV1                 | main, 3, 0         |
| `model/Faculty.java`, `list/FacultyList.java`               | TV2                 | 1, 2, 4, 5         |
| `model/Lecturer.java`, `list/LecturerList.java` (phần TV3) | TV3                 | 6, 7, 11           |
| `list/LecturerList.java` (phần TV4), `ui/Inputter.java`    | TV4                 | 8, 9, 10           |
| `ui/Menu.java`                                                | TV5                 | menu chọn         |
| `list/AdvancedFeatures.java`                                  | TV6                 | 12, 13, 14 (nâng cao) |

Tìm việc của mình: tìm chuỗi `TODO(TV2)` (thay bằng mã của bạn) trong IDE.
