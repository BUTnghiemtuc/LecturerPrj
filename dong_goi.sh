#!/usr/bin/env bash
# Đóng gói bài nộp vào LTHDT_Nhom_1/ theo cấu trúc mẫu nộp bài của giảng viên (1.Document, 2.Code, 3.Present, 4.Orthers).
# File gốc vẫn nằm ở docs/, Picture/, src/ – sửa ở đó rồi chạy lại: bash dong_goi.sh
set -euo pipefail
cd "$(dirname "$0")"
OUT=LTHDT_Nhom_1
rm -rf "$OUT"
mkdir -p "$OUT/1.Document" "$OUT/2.Code/LecturerPrj" "$OUT/3.Present" "$OUT/4.Orthers/Anh_hop"

# 1.Document: báo cáo, kế hoạch, kiểm thử, UML, ảnh kết quả chạy chương trình
cp docs/Nhom_1_Bao_cao.docx Nhom_1_Plan.xlsx docs/Nhom_1_Kiem_thu.xlsx docs/UML_Class_Diagram.png "$OUT/1.Document/"
cp -r docs/anh_kiem_thu "$OUT/1.Document/"

# 2.Code: mã nguồn + 2 file dữ liệu (chạy từ thư mục LecturerPrj)
cp -r src facultys.txt lecturers.txt "$OUT/2.Code/LecturerPrj/"
cat > "$OUT/2.Code/LecturerPrj/README.txt" <<'EOF'
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
EOF

# 3.Present: slide
cp docs/Nhom_1_Slides.pptx "$OUT/3.Present/"

# 4.Orthers: biên bản họp + ảnh minh chứng
cp docs/Nhom_1_Bien_ban_hop_Lan*.docx "$OUT/4.Orthers/"
cp Picture/Hop1-07-11.png "$OUT/4.Orthers/Anh_hop/"
cp Picture/image.png "$OUT/4.Orthers/Anh_hop/Hop1-07-11_lich_su_Google_Meet.png"
for f in Picture/Hop2-*.png Picture/Hop3-*.png; do [ -e "$f" ] && cp "$f" "$OUT/4.Orthers/Anh_hop/"; done

find "$OUT" -type f | sort
