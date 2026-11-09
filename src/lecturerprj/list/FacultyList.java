package lecturerprj.list;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import lecturerprj.model.Faculty;
import lecturerprj.ui.Inputter;
import lecturerprj.ui.Menu;

/**
 * Danh sách khoa (chức năng 1, 2, 4, 5): đọc/ghi file facultys.txt, tìm theo mã, thêm, sửa, hiển thị.
 * Kế thừa {@code ArrayList<Faculty>} nên dùng trực tiếp được add/get/remove/size/for-each và
 * truyền thẳng vào {@code Menu.ref_getChoice} để chọn khoa.
 *
 * @author TV2 - (điền họ tên)
 */
public class FacultyList extends ArrayList<Faculty> {

    /** Mẫu một dòng bảng khoa (QUY_UOC.md mục 6): mã, tên, phòng, ngân sách có dấu phân cách hàng nghìn. */
    public static final String ROW_FORMAT = "%-6s | %-25s | %-8s | %,18.0f%n";

    /** Mẫu dòng tiêu đề, cùng độ rộng cột với {@link #ROW_FORMAT}. */
    public static final String HEADER_FORMAT = "%-6s | %-25s | %-8s | %18s%n";

    /** Phiên bản tuần tự hóa (bắt buộc khai báo vì ArrayList là Serializable). */
    private static final long serialVersionUID = 1L;

    /** Đường kẻ dưới tiêu đề bảng, dài bằng tổng độ rộng các cột và 3 dấu " | " (JDK 8 chưa có String.repeat). */
    private static final String TABLE_LINE = new String(new char[6 + 25 + 8 + 18 + 3 * 3]).replace('\0', '-');

    /** Ký tự BOM mà Notepad (Windows) hay chèn vào đầu file UTF-8; phải bỏ đi khi đọc dòng đầu. */
    private static final String UTF8_BOM = "\uFEFF";

    /**
     * Tạo danh sách khoa rỗng. Dữ liệu được nạp sau bằng {@link #loadFromFile(String)}.
     */
    public FacultyList() {
        super();
    }

    // ===================== Đọc / ghi file =====================

    /**
     * Đọc danh sách khoa từ file (gọi khi khởi động chương trình, TRƯỚC khi đọc giảng viên).
     * <ol>
     *   <li>Xóa dữ liệu cũ trong danh sách.</li>
     *   <li>Mở file bằng try-with-resources (UTF-8, tự đóng file kể cả khi lỗi); bỏ BOM ở dòng đầu nếu có.</li>
     *   <li>Bỏ qua dòng trống; mỗi dòng còn lại được {@link #parseLine(String)} tách và kiểm tra.</li>
     *   <li>Dòng sai định dạng / vi phạm ràng buộc / trùng mã: KHÔNG dừng đọc, chỉ in
     *       {@code [facultys.txt] Line n: <lý do> -> skipped.} (QUY_UOC.md mục 5.3) rồi đọc tiếp.</li>
     *   <li>Cuối cùng in số khoa đã nạp và số dòng bị bỏ qua.</li>
     * </ol>
     * Không tìm thấy file: in [WARN] và chạy tiếp với danh sách rỗng.
     *
     * @param filename tên file, ví dụ "facultys.txt" (đường dẫn tương đối tới thư mục chạy chương trình)
     * @return true nếu mở và đọc được file (kể cả khi có dòng bị bỏ qua), false nếu không tìm thấy hoặc lỗi đọc
     */
    public boolean loadFromFile(String filename) {
        clear();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(filename), StandardCharsets.UTF_8))) {
            String line;
            int lineNumber = 0;
            int skipped = 0;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                if (lineNumber == 1 && line.startsWith(UTF8_BOM)) {
                    line = line.substring(1);
                }
                if (line.trim().isEmpty()) {
                    continue;
                }
                try {
                    add(parseLine(line));
                } catch (IllegalArgumentException e) {
                    skipped++;
                    System.out.println("[" + filename + "] Line " + lineNumber + ": " + e.getMessage()
                            + " -> skipped.");
                }
            }
            System.out.println("[INFO]  Loaded " + size() + " faculty(s) from " + filename
                    + (skipped > 0 ? " (" + skipped + " line(s) skipped)." : "."));
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("[WARN]  File " + filename + " not found. Starting with empty list.");
            return false;
        } catch (IOException e) {
            System.out.println("[ERROR] Cannot read " + filename + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Chức năng 5: ghi toàn bộ danh sách ra file (ghi đè), mỗi khoa 1 dòng bằng {@code Faculty.toString()},
     * mã hóa UTF-8, dùng try-with-resources để luôn đóng file.
     * Hàm không in thông báo thành công; {@code LecturerManager.printSaveResult} in dựa trên giá trị trả về.
     *
     * @param filename tên file, ví dụ "facultys.txt"
     * @return true nếu ghi thành công; false nếu không mở được file (bị khóa, không có quyền ghi...)
     *         hoặc có lỗi trong lúc ghi
     */
    public boolean saveToFile(String filename) {
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8)))) {
            for (Faculty faculty : this) {
                pw.println(faculty.toString());
            }
            // PrintWriter không ném IOException, phải tự hỏi checkError() (đồng thời flush)
            return !pw.checkError();
        } catch (FileNotFoundException e) {
            // Gặp khi file đang bị khóa / không có quyền ghi / đường dẫn là thư mục
            System.out.println("[ERROR] " + e.getMessage());
            return false;
        }
    }

    // ===================== Tìm kiếm & chọn =====================

    /**
     * Tìm vị trí khoa theo mã. Duyệt tuần tự, so sánh bằng {@code equalsIgnoreCase} (không dùng ==)
     * nên "f01", " F01 " và "F01" đều tìm thấy cùng một khoa.
     * Dùng cho chức năng 3, 4, kiểm tra trùng mã khi thêm/sửa/đọc file, và để đổi mã khoa trong
     * lecturers.txt thành đối tượng Faculty.
     *
     * @param id mã khoa cần tìm (có thể chứa khoảng trắng thừa, chữ thường)
     * @return vị trí (chỉ số) của khoa trong danh sách, hoặc -1 nếu không có hoặc {@code id} là null
     */
    public int searchID(String id) {
        if (id == null) {
            return -1;
        }
        String key = id.trim();
        for (int i = 0; i < size(); i++) {
            if (get(i).getFacultyID().equalsIgnoreCase(key)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Hiện danh sách khoa đánh số để người dùng chọn (dùng {@code Menu.ref_getChoice}).
     * Dùng khi thêm/sửa giảng viên (chức năng 8, 10) để khoa luôn là khoa có thật trong danh sách,
     * không phải gõ tay mã khoa.
     *
     * @return đối tượng khoa được chọn, hoặc null nếu danh sách rỗng hoặc người dùng chọn 0 (hủy)
     */
    public Faculty getUserChoice() {
        if (isEmpty()) {
            System.out.println("[INFO]  List is empty.");
            return null;
        }
        System.out.println("Choose a faculty:");
        return Menu.ref_getChoice(this);
    }

    // ===================== Chức năng menu =====================

    /**
     * Chức năng 2: nhập và thêm khoa mới.
     * <ol>
     *   <li>Nhập mã khoa (đổi chữ in hoa); trùng mã khoa đã có thì báo [ERROR] và hỏi lại.</li>
     *   <li>Nhập tên khoa, phòng (không rỗng, không có dấu phẩy) và ngân sách (số thực lớn hơn 0);
     *       nhập sai thì {@code Inputter} tự hỏi lại.</li>
     *   <li>Thêm khoa vào cuối danh sách và in [OK]. Dữ liệu chỉ được ghi ra file khi chọn chức năng 5.</li>
     * </ol>
     */
    public void addFaculty() {
        String id;
        while (true) {
            id = Inputter.inputString("Enter faculty ID: ").toUpperCase();
            if (searchID(id) < 0) {
                break;
            }
            System.out.println("[ERROR] Faculty ID " + id + " already exists. Please try again.");
        }
        String name = Inputter.inputString("Enter faculty name: ");
        String office = Inputter.inputString("Enter office: ");
        double budget = Inputter.inputPositiveDouble("Enter research budget: ");

        add(new Faculty(id, name, office, budget));
        System.out.println("[OK]    Faculty " + id + " added.");
    }

    /**
     * Chức năng 4: cập nhật khoa theo mã.
     * <ol>
     *   <li>Danh sách rỗng: báo [INFO] và thoát. Nhập mã khoa cần sửa; không có thì báo [INFO] và thoát.</li>
     *   <li>In thông tin hiện tại, rồi lần lượt hỏi mã, tên, phòng, ngân sách mới;
     *       nhấn Enter để giữ giá trị cũ, giá trị mới vẫn được kiểm tra ràng buộc.</li>
     *   <li>Mã mới chỉ bị coi là trùng nếu trùng với khoa KHÁC (giữ nguyên mã cũ thì hợp lệ).</li>
     *   <li>In kết quả sau khi sửa. Nếu đổi mã thì nhắc lưu cả 2 file: giảng viên tham chiếu cùng
     *       đối tượng khoa nên tự nhận mã mới khi lưu lecturers.txt.</li>
     * </ol>
     */
    public void updateFaculty() {
        if (isEmpty()) {
            System.out.println("[INFO]  List is empty.");
            return;
        }
        String id = Inputter.inputString("Enter faculty ID to update: ");
        int index = searchID(id);
        if (index < 0) {
            System.out.println("[INFO]  Faculty " + id.toUpperCase() + " not found.");
            return;
        }
        Faculty faculty = get(index);
        System.out.println("Current information (press Enter to keep old value):");
        printHeader();
        printRow(faculty);

        String oldID = faculty.getFacultyID();
        String newID;
        while (true) {
            newID = Inputter.inputString("Enter faculty ID", oldID).toUpperCase();
            int found = searchID(newID);
            // Trùng với CHÍNH NÓ thì hợp lệ (người dùng giữ mã cũ hoặc chỉ đổi hoa/thường)
            if (found < 0 || found == index) {
                break;
            }
            System.out.println("[ERROR] Faculty ID " + newID + " already exists. Please try again.");
        }
        faculty.setFacultyID(newID);
        faculty.setFacultyName(Inputter.inputString("Enter faculty name", faculty.getFacultyName()));
        faculty.setOffice(Inputter.inputString("Enter office", faculty.getOffice()));
        faculty.setResearchBudget(Inputter.inputPositiveDouble("Enter research budget", faculty.getResearchBudget()));

        System.out.println("[OK]    Faculty " + newID + " updated.");
        printHeader();
        printRow(faculty);
        if (!newID.equals(oldID)) {
            // Giảng viên giữ tham chiếu tới đối tượng này nên tự nhận mã mới; cần lưu cả 2 file
            System.out.println("[INFO]  Faculty ID changed. Save both files (options 5 and 11) to keep data "
                    + "consistent.");
        }
    }

    /**
     * Chức năng 1: in toàn bộ khoa dạng bảng theo thứ tự trong danh sách (QUY_UOC.md mục 6),
     * cuối bảng in tổng số khoa. Danh sách rỗng thì in {@code [INFO] List is empty.} thay vì bảng trống.
     */
    public void listFacultys() {
        if (isEmpty()) {
            System.out.println("[INFO]  List is empty.");
            return;
        }
        printHeader();
        for (Faculty faculty : this) {
            printRow(faculty);
        }
        System.out.println("Total: " + size() + " faculty(s).");
    }

    // ===================== In bảng (dùng chung với chức năng 3) =====================

    /**
     * In dòng tiêu đề bảng khoa ("ID | Name | Office | Research budget") kèm đường kẻ bên dưới.
     * Static để chức năng 3 ({@code LecturerManager.searchFaculty}) và chức năng 13 dùng lại.
     */
    public static void printHeader() {
        System.out.printf(HEADER_FORMAT, "ID", "Name", "Office", "Research budget");
        System.out.println(TABLE_LINE);
    }

    /**
     * In một khoa thành một dòng bảng theo {@link #ROW_FORMAT}; ngân sách có dấu phân cách hàng nghìn,
     * ví dụ {@code F01    | Computer Science          | A301     |        600,000,000}.
     * Lớp Faculty không có screenString() trong UML của đề nên việc in bảng đặt ở đây.
     *
     * @param faculty khoa cần in
     */
    public static void printRow(Faculty faculty) {
        System.out.printf(ROW_FORMAT, faculty.getFacultyID(), faculty.getFacultyName(), faculty.getOffice(),
                faculty.getResearchBudget());
    }

    // ===================== Hàm hỗ trợ (private) =====================

    /**
     * Tách và kiểm tra một dòng của facultys.txt, theo thứ tự:
     * đúng 4 trường → không trường nào rỗng → ngân sách là số hữu hạn → ngân sách lớn hơn 0 →
     * mã chưa có trong danh sách. Vi phạm điều nào thì ném lỗi kèm lý do ngắn gọn (tiếng Anh) để
     * {@link #loadFromFile(String)} in ra cùng số dòng.
     *
     * @param line một dòng (không rỗng) đọc từ file, ví dụ "F01, Computer Science, A301, 600000000"
     * @return đối tượng Faculty hợp lệ (mã đã đổi chữ in hoa)
     * @throws IllegalArgumentException kèm lý do nếu dòng không hợp lệ
     */
    private Faculty parseLine(String line) {
        // limit -1 để giữ cả trường rỗng ở cuối dòng ("F01, A, B, ") -> báo đúng là thiếu dữ liệu
        String[] parts = line.split(",", -1);
        if (parts.length != Faculty.FIELD_COUNT) {
            throw new IllegalArgumentException("expected " + Faculty.FIELD_COUNT + " fields but found "
                    + parts.length);
        }
        String id = requireNotEmpty(parts[0], "facultyID").toUpperCase();
        String name = requireNotEmpty(parts[1], "facultyName");
        String office = requireNotEmpty(parts[2], "office");
        String budgetText = requireNotEmpty(parts[3], "researchBudget");

        double budget;
        try {
            budget = Double.parseDouble(budgetText);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("researchBudget '" + budgetText + "' is not a number");
        }
        if (Double.isNaN(budget) || Double.isInfinite(budget)) {
            throw new IllegalArgumentException("researchBudget '" + budgetText + "' is not a number");
        }
        if (budget <= 0) {
            throw new IllegalArgumentException("researchBudget must be greater than 0");
        }
        if (searchID(id) >= 0) {
            throw new IllegalArgumentException("faculty ID '" + id + "' is duplicated");
        }
        return new Faculty(id, name, office, budget);
    }

    /**
     * Trim một trường đọc từ file và kiểm tra không rỗng.
     *
     * @param value     giá trị thô của trường (có thể có khoảng trắng thừa)
     * @param fieldName tên trường, dùng trong thông báo lỗi, ví dụ "facultyName"
     * @return giá trị đã trim
     * @throws IllegalArgumentException với thông báo "{@code <fieldName> is empty}" nếu trường rỗng
     */
    private static String requireNotEmpty(String value, String fieldName) {
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " is empty");
        }
        return trimmed;
    }
}
