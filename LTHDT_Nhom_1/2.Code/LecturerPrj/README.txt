LecturerPrj - University Lecturer Management System (Nhom 1 - Topic 04)

Yeu cau: JDK 8 tro len. Dung o thu muc LecturerPrj (noi co facultys.txt, lecturers.txt).

Linux / macOS:
  javac -encoding UTF-8 -d out $(find src -name "*.java")
  java -cp out lecturerprj.LecturerManager

Windows (PowerShell):
  javac -encoding UTF-8 -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
  java -cp out lecturerprj.LecturerManager

IDE: mo thu muc LecturerPrj, danh dau src la source folder, chay LecturerManager.
Working directory phai la thu muc LecturerPrj, neu khong chuong trinh khong tim thay file .txt.
