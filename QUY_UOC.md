# Quy ước code & định dạng file – LecturerPrj (Nhóm 1)

Áp dụng cho cả 6 thành viên. Đọc hết **trước khi viết dòng code đầu tiên**.
Có gì chưa rõ hoặc muốn đổi quy ước → hỏi trên nhóm chat, nhóm trưởng (TV1) chốt rồi sửa file này. Không tự ý làm khác.

---

## 1. Cấu trúc project

```
LecturerPrj/
├── src/
│   └── lecturerprj/
│       ├── LecturerManager.java      ← main (TV1)
│       ├── model/
│       │   ├── Faculty.java          ← TV2
│       │   └── Lecturer.java         ← TV3
│       ├── list/
│       │   ├── FacultyList.java      ← TV2
│       │   ├── LecturerList.java     ← TV3 + TV4 (xem mục 9)
│       │   └── AdvancedFeatures.java ← TV6 (chức năng nâng cao 12, 13)
│       └── ui/
│           ├── Menu.java             ← TV5
│           └── Inputter.java         ← TV4
├── facultys.txt                      ← dữ liệu, đặt ở THƯ MỤC GỐC project
├── lecturers.txt
├── QUY_UOC.md
└── .gitignore
```

- Dòng đầu mỗi file khai báo đúng package, ví dụ `package lecturerprj.model;`
- Mỗi file `.java` chứa đúng **1 class public**, tên file = tên class.
- Mở file dữ liệu bằng **đường dẫn tương đối** `"facultys.txt"`, không dùng đường dẫn tuyệt đối kiểu `C:\Users\...`.
- Chạy từ terminal (đứng ở thư mục gốc `LecturerPrj`):
  ```bash
  javac -encoding UTF-8 -d out $(find src -name "*.java")
  java -cp out lecturerprj.LecturerManager
  ```
- IDE nào cũng được (IntelliJ, NetBeans, VS Code). Không commit file cấu hình riêng của IDE (xem `.gitignore`).
- **Encoding UTF-8** cho mọi file (cài trong IDE: File Encoding = UTF-8).

---

## 2. Đặt tên

| Loại | Quy tắc | Ví dụ |
|---|---|---|
| Class, interface, enum | PascalCase, danh từ tiếng Anh | `FacultyList`, `Inputter` |
| Method | camelCase, bắt đầu bằng động từ | `loadFromFile`, `searchID` |
| Biến, thuộc tính, tham số | camelCase, có nghĩa | `teachingHours`, `keyword` |
| Hằng số | `UPPER_SNAKE_CASE`, `static final` | `FACULTY_FILE = "facultys.txt"` |
| Package | chữ thường | `lecturerprj.model` |

- **Tên class, thuộc tính, method trong UML của đề phải giữ NGUYÊN VĂN**, kể cả tên không theo chuẩn: `int_getChoice`, `ref_getChoice`, `listFacultys`, `searchID`, `screenString`, `printBasedFacultyName`. Giảng viên chấm đối chiếu với UML.
- Không đặt tên `a`, `b`, `temp1`, `x2`. Biến đếm vòng lặp `i`, `j` thì được.

---

## 3. Trình bày code

- Thụt lề **4 dấu cách** (không dùng Tab). Dòng không quá **120 ký tự**.
- Dấu `{` cùng dòng với câu lệnh. `if`/`for`/`while` **luôn có `{}`**, kể cả khi chỉ có 1 lệnh.
- Thứ tự trong class: hằng số → thuộc tính → constructor → getter/setter → các method khác → `toString`/`screenString`/`compareTo`.
- **Mọi thuộc tính đều `private`** (đóng gói). Truy cập qua getter/setter.
- Trước khi commit: format code bằng IDE (IntelliJ: `Ctrl+Alt+L`, NetBeans: `Alt+Shift+F`), xóa import thừa và code bị comment-out.

### Javadoc – bắt buộc cho mọi class và method public

```java
/**
 * Tìm vị trí của khoa theo mã.
 *
 * @param id mã khoa cần tìm (không phân biệt hoa/thường)
 * @return vị trí trong danh sách, hoặc -1 nếu không tìm thấy
 */
public int searchID(String id) { ... }
```

Đầu mỗi file ghi người viết:

```java
/**
 * Quản lý danh sách khoa: đọc/ghi file, thêm, sửa, tìm kiếm.
 *
 * @author TV2 - Ho Ten
 */
public class FacultyList extends ArrayList<Faculty> { ... }
```

Comment trong code: tiếng Việt có dấu được, ngắn gọn, giải thích **tại sao** chứ không lặp lại code.

---

## 4. Quy tắc lập trình bắt buộc

1. **So sánh String bằng `equals` / `equalsIgnoreCase`, KHÔNG dùng `==`** (đề cấm).
   - Mã (ID): so sánh **không phân biệt hoa/thường** và **lưu dạng chữ in hoa** (`id.trim().toUpperCase()`).
   - Tìm theo một phần tên khoa: `name.toLowerCase().contains(keyword.toLowerCase())`.
2. **Chỉ có 1 `Scanner(System.in)` trong cả chương trình**, đặt `static final` trong `Inputter`. Mọi chỗ cần nhập đều gọi `Inputter`. Không tạo `Scanner` mới và **không `close()`** Scanner này.
3. **Luôn đọc bằng `nextLine()` rồi tự chuyển kiểu** (`Integer.parseInt`, `Double.parseDouble`) trong `try/catch (NumberFormatException)`. Không dùng `nextInt()`/`nextDouble()`, vì sẽ bị trôi dòng.
4. **Nhập sai thì báo lỗi và hỏi lại**, không được thoát chương trình hay ném exception ra ngoài.
5. **Đọc/ghi file bằng try-with-resources**:
   ```java
   try (BufferedReader br = new BufferedReader(new FileReader(filename))) { ... }
   ```
6. Không dùng `System.exit()` ngoài lựa chọn 0 (Exit). Exit **không tự ghi file**.
7. Không bắt `catch (Exception e) {}` rỗng. Bắt đúng loại lỗi và in thông báo rõ.
8. Không dùng số "ma thuật": giới hạn và tên file khai báo thành hằng số.

---

## 5. Định dạng file dữ liệu

Quy tắc chung cho cả 2 file:
- Mỗi dòng 1 bản ghi, **không có dòng tiêu đề**.
- Các trường cách nhau bằng **dấu phẩy + 1 dấu cách** `", "`.
- Khi đọc: `split(",")` rồi `trim()` từng phần, bỏ qua dòng trống.
- Vì dấu phẩy là ký tự phân tách, **mọi trường chữ đều không được chứa dấu `,`**. `Inputter` phải chặn khi nhập.
- Encoding UTF-8, mỗi file có **tối thiểu 3 bản ghi**.

### 5.1. `facultys.txt`

```
facultyID, facultyName, office, researchBudget
```
```
F01, Computer Science, A301, 600000000
F02, Business, B201, 350000000
F03, Languages, C102, 200000000
```

- `researchBudget` là `double` nhưng **ghi ra file dạng số nguyên, không có dấu phân cách hàng nghìn**. Nếu không định dạng, `600000000.0` sẽ thành `6.0E8` và lần đọc sau bị sai:
  ```java
  String.format(Locale.US, "%.0f", researchBudget)
  ```

### 5.2. `lecturers.txt`

```
lecturerID, facultyID, fullName, academicRank, teachingHours
```
```
L01, F01, Nguyen An, Senior Lecturer, 180
L02, F02, Tran Binh, Lecturer, 210
L03, F01, Le Chi, Associate Professor, 120
```

- Trong file, cột 2 là **mã khoa**. Trong chương trình, khi đọc phải đổi thành **đối tượng `Faculty`** bằng `facultyList.searchID(...)`.
- Luôn **nạp `facultys.txt` trước `lecturers.txt`**.

### 5.3. Xử lý lỗi khi đọc file

| Tình huống | Cách xử lý |
|---|---|
| Không tìm thấy file | In cảnh báo, bắt đầu với danh sách rỗng, `loadFromFile` trả về `false`. Chương trình vẫn chạy tiếp. |
| Dòng thiếu/thừa trường | **Bỏ qua dòng đó**, in lỗi kèm số dòng, đọc tiếp các dòng sau. |
| Số sai định dạng hoặc vi phạm ràng buộc | Như trên. |
| Mã trùng với dòng trước | Như trên. |
| `lecturers.txt` tham chiếu mã khoa không tồn tại | Như trên. |

Mẫu thông báo:
```
[lecturers.txt] Line 4: faculty 'F09' does not exist -> skipped.
```

### 5.4. `toString()` và `screenString()`

| Method | Dùng để | Định dạng |
|---|---|---|
| `toString()` | **Ghi file** | Đúng 1 dòng như mục 5.1 / 5.2 |
| `screenString()` | **Hiển thị** lên màn hình | Dạng bảng theo mục 6 |

`Faculty` không có `screenString()` trong UML. Khi hiển thị, `FacultyList.listFacultys()` tự in bằng `printf` theo mẫu ở mục 6.

---

## 6. Giao diện console

- Toàn bộ chữ hiển thị (menu, câu hỏi, thông báo) bằng **tiếng Anh, không dấu**. Menu trong đề là tiếng Anh, và tiếng Việt có dấu dễ bị lỗi font trên CMD Windows.
- Câu hỏi nhập: `Enter <field>: `, ví dụ `Enter faculty ID: `
- Thông báo dùng **đúng 4 tiền tố**:
  ```
  [OK]    Faculty F04 added.
  [ERROR] Faculty ID must not be empty. Please try again.
  [INFO]  No lecturer matches "abc".
  [WARN]  File facultys.txt not found. Starting with empty list.
  ```
- Bảng Faculty:
  ```java
  "%-6s | %-25s | %-8s | %,18.0f"
  ```
  ```
  ID     | Name                      | Office   |    Research budget
  F01    | Computer Science          | A301     |        600,000,000
  ```
- Bảng Lecturer (`screenString()`):
  ```java
  "%-6s | %-25s | %-22s | %-20s | %5d"
  ```
  ```
  ID     | Faculty                   | Full name              | Academic rank        | Hours
  L01    | Computer Science          | Nguyen An              | Senior Lecturer      |   180
  ```
- Danh sách rỗng thì in `[INFO] List is empty.` thay vì in bảng trống.

---

## 7. Ràng buộc dữ liệu (kiểm tra cả khi nhập lẫn khi đọc file)

| Trường | Ràng buộc |
|---|---|
| `facultyID` | Không rỗng, không trùng, không chứa `,`. Lưu chữ in hoa. |
| `facultyName`, `office` | Không rỗng, không chứa `,` |
| `researchBudget` | Số thực **> 0** |
| `lecturerID` | Không rỗng, không trùng, không chứa `,`. Lưu chữ in hoa. |
| `faculty` | Phải tồn tại trong `FacultyList`. **Chọn qua menu**, không gõ tay. |
| `fullName`, `academicRank` | Không rỗng, không chứa `,` |
| `teachingHours` | Số nguyên **≥ 0** |

- Khi **sửa**: nếu đổi mã thì kiểm tra trùng với **các bản ghi khác** (không tính chính nó).
- Khi sửa, nhấn Enter bỏ trống nghĩa là **giữ giá trị cũ**. In giá trị cũ trong ngoặc: `Enter office [A301]: `

---

## 8. Quy trình Git

1. **Không push thẳng lên `main`** (trừ TV1 lúc dựng khung). Mỗi người làm trên nhánh riêng:
   ```
   feature/tv2-faculty
   feature/tv3-lecturer
   feature/tv4-inputter
   feature/tv5-menu
   feature/tv6-advanced
   ```
2. Mỗi sáng trước khi code, cập nhật từ `main`:
   ```bash
   git checkout main && git pull
   git checkout feature/tv2-faculty && git merge main
   ```
3. Commit nhỏ, thường xuyên. Message theo mẫu `[TVx] <làm gì>`:
   ```
   [TV2] Add FacultyList.loadFromFile with line error report
   [TV4] Fix updateLecturer duplicate ID check
   ```
4. Xong việc → tạo **Pull Request** vào `main`, gán **người review theo cột "Người Review" trong Nhom_1_Plan.xlsx**. Người review duyệt thì TV1 merge.
5. **Không commit**: file `.class`, thư mục `out/`, `bin/`, `.idea/`, `nbproject/private/`, `.vscode/`, file `.zip`/`.rar`. Đã có `.gitignore` lo việc này, **không xóa `.gitignore`**.
6. Bị conflict mà không tự xử lý được → nhắn TV1. **Không dùng `git push --force`.**

---

## 9. Phân chia file (ai sửa file nào)

| File | Người sở hữu | Ghi chú |
|---|---|---|
| `LecturerManager.java` | TV1 | |
| `Faculty.java`, `FacultyList.java`, `facultys.txt` | TV2 | |
| `Lecturer.java`, `lecturers.txt` | TV3 | |
| `LecturerList.java` | TV3 + TV4 | TV3 viết: constructor, `loadFromFile`, `saveToFile`, `searchID`, `listLecturers`, `printBasedFacultyName`.<br>TV4 viết: `addLecturer`, `removeLecturer`, `updateLecturer`.<br>**Mỗi người chỉ sửa method của mình.** TV3 commit khung trước 12h 10/11. |
| `Inputter.java` | TV4 | Xong 09/11, cả nhóm dùng |
| `Menu.java` | TV5 | Xong trước 12h 10/11 |
| `AdvancedFeatures.java` | TV6 | Chức năng nâng cao 12, 13 (mở rộng mục 9 của đề). Viết trong file riêng; TV1 thêm 2 mục menu trong `LecturerManager`. |

Cần sửa file của người khác → nhắn chủ file sửa, hoặc ghi rõ trong Pull Request và tag chủ file vào review.

---

## 10. Thế nào là "xong việc"

Một việc chỉ được đánh **Hoàn thành** trong `Nhom_1_Plan.xlsx` khi đủ cả 6 điều:

- [ ] Code biên dịch không lỗi và chạy được cùng `main` mới nhất
- [ ] Tự test đủ ca đúng và ca sai (rỗng, trùng mã, chữ thay vì số, số âm)
- [ ] Có Javadoc cho class và mọi method public
- [ ] Đúng các quy ước trong file này
- [ ] Pull Request đã được người review duyệt và merge vào `main`
- [ ] Đã nhắn tiến độ trên nhóm chat lúc 21h
