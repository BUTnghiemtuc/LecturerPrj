# LecturerPrj – University Lecturer Management System

Bài tập lớn Lập trình hướng đối tượng (Java Console) – Nhóm 1 – Topic 04.

**Trước khi code, đọc [QUY_UOC.md](QUY_UOC.md).** Kế hoạch và phân công: `Nhom_1_Plan.xlsx`.

## Tài liệu

| Thư mục / file                                              | Nội dung                                                                                                                        |
| ------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------------------------------- |
| [de_bai/DE_BAI_Topic04.md](de_bai/DE_BAI_Topic04.md)           | Đề bài Topic 04 dạng Markdown, đọc nhanh trên GitHub/IDE                                                                  |
| `de_bai/Topic04_University_Lecturer_Management_System.docx` | Đề bài gốc (chuẩn khi có khác biệt)                                                                                      |
| `de_bai/cac_topic_khac/`                                    | 11 topic còn lại của môn (tham khảo)                                                                                        |
| `mau_nop_bai/`                                              | Mẫu nộp bài của giảng viên: `1.Document` (báo cáo, kế hoạch), `3.Present` (slide), `4.Orthers` (biên bản họp) |
| `docs/`                                                     | Báo cáo `Nhom_1_Bao_cao.docx`, biên bản họp, ảnh UML                                                                     |
| `Picture/`                                                  | Ảnh minh chứng các buổi họp                                                                                                 |

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
| `list/AdvancedFeatures.java`                                  | TV6                 | 12, 13 (nâng cao) |

Tìm việc của mình: tìm chuỗi `TODO(TV2)` (thay bằng mã của bạn) trong IDE.
