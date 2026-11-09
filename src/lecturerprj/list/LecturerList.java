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
import java.util.Collections;

import lecturerprj.model.Faculty;
import lecturerprj.model.Lecturer;
import lecturerprj.ui.Inputter;

/**
 * Danh sách giảng viên (chức năng 6-11): đọc/ghi file lecturers.txt, tìm theo mã, sắp xếp,
 * lọc theo tên khoa, thêm, xóa, sửa. Kế thừa {@code ArrayList<Lecturer>} nên dùng trực tiếp được
 * add/get/remove/size/for-each và {@code Collections.sort(this)}.
 * Giữ tham chiếu tới {@link FacultyList} để đổi mã khoa trong file thành đối tượng Faculty
 * và để người dùng chọn khoa qua menu.
 *
 * <p>File này có 2 người cùng viết, MỖI NGƯỜI CHỈ SỬA PHẦN CỦA MÌNH:
 * <ul>
 *   <li>TV3: constructor, loadFromFile, saveToFile, searchID, listLecturers, printBasedFacultyName</li>
 *   <li>TV4: addLecturer, removeLecturer, updateLecturer</li>
 * </ul>
 *
 * @author TV3 - (điền họ tên), TV4 - (điền họ tên)
 */
public class LecturerList extends ArrayList<Lecturer> {

    /** Phiên bản tuần tự hóa (bắt buộc khai báo vì ArrayList là Serializable). */
    private static final long serialVersionUID = 1L;

    /** Đường kẻ dưới tiêu đề bảng, dài bằng tổng độ rộng các cột và 4 dấu " | ". */
    private static final String TABLE_LINE = new String(new char[6 + 25 + 22 + 20 + 5 + 4 * 3]).replace('\0', '-');

    /** Ký tự BOM mà Notepad (Windows) hay chèn vào đầu file UTF-8; phải bỏ đi khi đọc dòng đầu. */
    private static final String UTF8_BOM = "\uFEFF";

    /** Danh sách khoa: dùng để đổi mã khoa trong file sang đối tượng Faculty và để chọn khoa qua menu. */
    private FacultyList facultyList;

    /**
     * Tạo danh sách giảng viên rỗng gắn với danh sách khoa.
     * Phải nạp facultyList TRƯỚC rồi mới gọi {@link #loadFromFile(String)}, để mã khoa trong
     * lecturers.txt tìm được đối tượng Faculty tương ứng.
     *
     * @param facultyList danh sách khoa (dùng chung với LecturerManager, không sao chép)
     */
    public LecturerList(FacultyList facultyList) {
        super();
        this.facultyList = facultyList;
    }

    // ============================================================
    // ===================== PHẦN CỦA TV3 ========================
    // ============================================================

    /**
     * Đọc danh sách giảng viên từ file (gọi khi khởi động, SAU khi đã nạp facultys.txt).
     * <ol>
     *   <li>Xóa dữ liệu cũ; mở file bằng try-with-resources (UTF-8); bỏ BOM ở dòng đầu nếu có.</li>
     *   <li>Bỏ qua dòng trống; mỗi dòng còn lại được {@link #parseLine(String)} tách, kiểm tra và đổi
     *       mã khoa thành đối tượng Faculty.</li>
     *   <li>Dòng sai (thiếu/thừa trường, trường rỗng, số giờ sai hoặc âm, mã khoa không tồn tại,
     *       trùng mã giảng viên): KHÔNG dừng đọc, chỉ in
     *       {@code [lecturers.txt] Line n: <lý do> -> skipped.} (QUY_UOC.md mục 5.3) rồi đọc tiếp.</li>
     *   <li>Cuối cùng in số giảng viên đã nạp và số dòng bị bỏ qua.</li>
     * </ol>
     * Không tìm thấy file: in [WARN] và chạy tiếp với danh sách rỗng.
     *
     * @param filename tên file, ví dụ "lecturers.txt" (đường dẫn tương đối tới thư mục chạy chương trình)
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
            System.out.println("[INFO]  Loaded " + size() + " lecturer(s) from " + filename
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
     * Chức năng 11: ghi toàn bộ danh sách ra file (ghi đè), mỗi giảng viên 1 dòng bằng
     * {@code Lecturer.toString()} (cột 2 là MÃ khoa), mã hóa UTF-8, dùng try-with-resources.
     * Hàm không in thông báo thành công; {@code LecturerManager.printSaveResult} in dựa trên giá trị trả về.
     *
     * @param filename tên file, ví dụ "lecturers.txt"
     * @return true nếu ghi thành công; false nếu không mở được file (bị khóa, không có quyền ghi...)
     *         hoặc có lỗi trong lúc ghi
     */
    public boolean saveToFile(String filename) {
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8)))) {
            for (Lecturer lecturer : this) {
                pw.println(lecturer.toString());
            }
            // PrintWriter không ném IOException, phải tự hỏi checkError() (đồng thời flush)
            return !pw.checkError();
        } catch (FileNotFoundException e) {
            // Gặp khi file đang bị khóa / không có quyền ghi / đường dẫn là thư mục
            System.out.println("[ERROR] " + e.getMessage());
            return false;
        }
    }

    /**
     * Tìm vị trí giảng viên theo mã. Duyệt tuần tự, so sánh bằng {@code equalsIgnoreCase}
     * (không dùng ==) nên "l01", " L01 " và "L01" đều tìm thấy cùng một giảng viên.
     * Dùng cho chức năng 9, 10 và để kiểm tra trùng mã khi thêm/sửa/đọc file.
     *
     * @param id mã giảng viên cần tìm (có thể chứa khoảng trắng thừa, chữ thường)
     * @return vị trí (chỉ số) của giảng viên trong danh sách, hoặc -1 nếu không có hoặc {@code id} là null
     */
    public int searchID(String id) {
        if (id == null) {
            return -1;
        }
        String key = id.trim();
        for (int i = 0; i < size(); i++) {
            if (get(i).getLecturerID().equalsIgnoreCase(key)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Chức năng 6: sắp xếp danh sách bằng {@code Collections.sort} (dùng {@code Lecturer.compareTo}:
     * tên khoa tăng dần, cùng tên khoa thì mã giảng viên tăng dần), rồi in dạng bảng bằng
     * {@code screenString()} và tổng số giảng viên.
     * Lưu ý: thứ tự trong danh sách gốc bị thay đổi theo kết quả sắp xếp.
     * Danh sách rỗng thì in {@code [INFO] List is empty.}
     */
    public void listLecturers() {
        if (isEmpty()) {
            System.out.println("[INFO]  List is empty.");
            return;
        }
        Collections.sort(this);
        printHeader();
        for (Lecturer lecturer : this) {
            System.out.println(lecturer.screenString());
        }
        System.out.println("Total: " + size() + " lecturer(s).");
    }

    /**
     * Chức năng 7: tìm giảng viên theo một phần tên khoa.
     * <ol>
     *   <li>Nhập từ khóa (không rỗng), ví dụ "sci".</li>
     *   <li>Chọn các giảng viên có tên khoa CHỨA từ khóa, không phân biệt hoa/thường
     *       (đưa cả hai về chữ thường rồi dùng {@code contains}).</li>
     *   <li>Sắp xếp bản sao kết quả (không đổi thứ tự danh sách gốc), in dạng bảng và số kết quả.</li>
     * </ol>
     * Không có kết quả thì in {@code [INFO] No lecturer matches "<từ khóa>".}
     */
    public void printBasedFacultyName() {
        if (isEmpty()) {
            System.out.println("[INFO]  List is empty.");
            return;
        }
        String keyword = Inputter.inputString("Enter part of faculty name: ");
        String lowerKeyword = keyword.toLowerCase();

        ArrayList<Lecturer> result = new ArrayList<Lecturer>();
        for (Lecturer lecturer : this) {
            Faculty faculty = lecturer.getFaculty();
            if (faculty != null && faculty.getFacultyName().toLowerCase().contains(lowerKeyword)) {
                result.add(lecturer);
            }
        }
        if (result.isEmpty()) {
            System.out.println("[INFO]  No lecturer matches \"" + keyword + "\".");
            return;
        }
        // Sắp xếp bản sao để kết quả dễ đọc mà không đổi thứ tự danh sách gốc
        Collections.sort(result);
        printHeader();
        for (Lecturer lecturer : result) {
            System.out.println(lecturer.screenString());
        }
        System.out.println("Found " + result.size() + " lecturer(s) matching \"" + keyword + "\".");
    }

    // ============================================================
    // ===================== PHẦN CỦA TV4 ========================
    // ============================================================

    /**
     * Chức năng 8: nhập và thêm giảng viên mới.
     * <ol>
     *   <li>Chưa có khoa nào: báo [ERROR] hướng dẫn thêm khoa trước (chức năng 2) và thoát.</li>
     *   <li>Nhập mã giảng viên (đổi chữ in hoa); trùng mã đã có thì báo [ERROR] và hỏi lại.</li>
     *   <li>Chọn khoa qua menu ({@code facultyList.getUserChoice()}), không gõ tay mã khoa;
     *       chọn 0 thì hủy toàn bộ việc thêm.</li>
     *   <li>Nhập họ tên, học hàm (không rỗng, không có dấu phẩy) và số giờ giảng (số nguyên không âm).</li>
     *   <li>Thêm vào cuối danh sách và in [OK]. Dữ liệu chỉ được ghi ra file khi chọn chức năng 11.</li>
     * </ol>
     */
    public void addLecturer() {
        if (facultyList.isEmpty()) {
            System.out.println("[ERROR] No faculty available. Please add a faculty first (option 2).");
            return;
        }
        String id;
        while (true) {
            id = Inputter.inputString("Enter lecturer ID: ").toUpperCase();
            if (searchID(id) < 0) {
                break;
            }
            System.out.println("[ERROR] Lecturer ID " + id + " already exists. Please try again.");
        }
        Faculty faculty = facultyList.getUserChoice();
        if (faculty == null) {
            System.out.println("[INFO]  Add lecturer cancelled.");
            return;
        }
        String fullName = Inputter.inputString("Enter full name: ");
        String academicRank = Inputter.inputString("Enter academic rank: ");
        int teachingHours = Inputter.inputInt("Enter teaching hours: ", 0, Integer.MAX_VALUE);

        add(new Lecturer(id, faculty, fullName, academicRank, teachingHours));
        System.out.println("[OK]    Lecturer " + id + " added.");
    }

    /**
     * Chức năng 9: xóa giảng viên theo mã.
     * <ol>
     *   <li>Danh sách rỗng: báo [INFO]. Nhập mã; không tìm thấy thì báo [INFO].</li>
     *   <li>In thông tin giảng viên sẽ bị xóa và hỏi xác nhận "Delete &lt;id&gt;? (Y/N)".</li>
     *   <li>Chọn Y: xóa khỏi danh sách và in [OK]; chọn N: hủy, không thay đổi gì.</li>
     * </ol>
     * Chỉ xóa trong bộ nhớ; muốn lưu thay đổi phải chọn chức năng 11.
     *
     * @return true nếu đã xóa; false nếu danh sách rỗng, không tìm thấy mã hoặc người dùng hủy
     */
    public boolean removeLecturer() {
        if (isEmpty()) {
            System.out.println("[INFO]  List is empty.");
            return false;
        }
        String id = Inputter.inputString("Enter lecturer ID to remove: ").toUpperCase();
        int index = searchID(id);
        if (index < 0) {
            System.out.println("[INFO]  Lecturer " + id + " not found.");
            return false;
        }
        printHeader();
        System.out.println(get(index).screenString());
        if (!Inputter.confirm("Delete " + id + "? (Y/N): ")) {
            System.out.println("[INFO]  Delete cancelled.");
            return false;
        }
        remove(index);
        System.out.println("[OK]    Lecturer " + id + " removed.");
        return true;
    }

    /**
     * Chức năng 10: cập nhật giảng viên theo mã.
     * <ol>
     *   <li>Danh sách rỗng: báo [INFO]. Nhập mã; không tìm thấy thì báo [INFO].</li>
     *   <li>In thông tin hiện tại; mọi câu hỏi sau đây nhấn Enter để giữ giá trị cũ.</li>
     *   <li>Mã mới: chỉ báo trùng khi trùng với giảng viên KHÁC (giữ nguyên mã cũ là hợp lệ).</li>
     *   <li>Hỏi có đổi khoa không; nếu có thì chọn khoa qua menu (chọn 0 = giữ khoa cũ).</li>
     *   <li>Họ tên, học hàm (không có dấu phẩy) và số giờ giảng (số nguyên không âm).</li>
     *   <li>Chỉ gán vào đối tượng sau khi đã nhập xong tất cả, rồi in kết quả.</li>
     * </ol>
     *
     * @return true nếu đã cập nhật; false nếu danh sách rỗng hoặc không tìm thấy mã
     */
    public boolean updateLecturer() {
        if (isEmpty()) {
            System.out.println("[INFO]  List is empty.");
            return false;
        }
        String id = Inputter.inputString("Enter lecturer ID to update: ").toUpperCase();
        int index = searchID(id);
        if (index < 0) {
            System.out.println("[INFO]  Lecturer " + id + " not found.");
            return false;
        }
        Lecturer lecturer = get(index);
        System.out.println("Current information (press Enter to keep old value):");
        printHeader();
        System.out.println(lecturer.screenString());

        String newID;
        while (true) {
            newID = Inputter.inputString("Enter lecturer ID", lecturer.getLecturerID()).toUpperCase();
            int found = searchID(newID);
            // Trùng với CHÍNH NÓ thì hợp lệ (giữ mã cũ); chỉ báo lỗi khi trùng giảng viên KHÁC
            if (found < 0 || found == index) {
                break;
            }
            System.out.println("[ERROR] Lecturer ID " + newID + " already exists. Please try again.");
        }

        Faculty faculty = lecturer.getFaculty();
        String currentFaculty = (faculty == null) ? "none" : faculty.getFacultyName();
        if (Inputter.confirm("Change faculty [" + currentFaculty + "]? (Y/N): ")) {
            Faculty chosen = facultyList.getUserChoice();
            if (chosen == null) {
                System.out.println("[INFO]  Faculty not changed.");
            } else {
                faculty = chosen;
            }
        }
        String fullName = Inputter.inputString("Enter full name", lecturer.getFullName());
        String academicRank = Inputter.inputString("Enter academic rank", lecturer.getAcademicRank());
        int teachingHours = Inputter.inputInt("Enter teaching hours", 0, Integer.MAX_VALUE,
                lecturer.getTeachingHours());

        // Gán sau khi đã nhập hết để giữ dữ liệu nhất quán trong lúc nhập
        lecturer.setLecturerID(newID);
        lecturer.setFaculty(faculty);
        lecturer.setFullName(fullName);
        lecturer.setAcademicRank(academicRank);
        lecturer.setTeachingHours(teachingHours);

        System.out.println("[OK]    Lecturer " + newID + " updated.");
        printHeader();
        System.out.println(lecturer.screenString());
        return true;
    }

    // ============================================================
    // ===================== Hàm hỗ trợ (private) =================
    // ============================================================

    /**
     * In dòng tiêu đề bảng giảng viên ("ID | Faculty | Full name | Academic rank | Hours")
     * kèm đường kẻ bên dưới; cùng độ rộng cột với {@code Lecturer.screenString()}.
     */
    private static void printHeader() {
        System.out.println(String.format(Lecturer.HEADER_FORMAT, "ID", "Faculty", "Full name", "Academic rank",
                "Hours"));
        System.out.println(TABLE_LINE);
    }

    /**
     * Tách và kiểm tra một dòng của lecturers.txt, theo thứ tự:
     * đúng 5 trường → không trường nào rỗng → số giờ là số nguyên → số giờ không âm →
     * mã khoa có trong facultyList → mã giảng viên chưa có trong danh sách.
     * Mã khoa được đổi thành ĐỐI TƯỢNG Faculty lấy từ facultyList (giữ đúng liên kết).
     * Vi phạm điều nào thì ném lỗi kèm lý do ngắn gọn (tiếng Anh) để {@link #loadFromFile(String)}
     * in ra cùng số dòng.
     *
     * @param line một dòng (không rỗng) đọc từ file, ví dụ "L01, F01, Nguyen An, Senior Lecturer, 180"
     * @return đối tượng Lecturer hợp lệ, đã gắn với khoa tương ứng
     * @throws IllegalArgumentException kèm lý do nếu dòng không hợp lệ
     */
    private Lecturer parseLine(String line) {
        // limit -1 để giữ cả trường rỗng ở cuối dòng -> báo đúng là thiếu dữ liệu
        String[] parts = line.split(",", -1);
        if (parts.length != Lecturer.FIELD_COUNT) {
            throw new IllegalArgumentException("expected " + Lecturer.FIELD_COUNT + " fields but found "
                    + parts.length);
        }
        String id = requireNotEmpty(parts[0], "lecturerID").toUpperCase();
        String facultyID = requireNotEmpty(parts[1], "facultyID").toUpperCase();
        String fullName = requireNotEmpty(parts[2], "fullName");
        String academicRank = requireNotEmpty(parts[3], "academicRank");
        String hoursText = requireNotEmpty(parts[4], "teachingHours");

        int teachingHours;
        try {
            teachingHours = Integer.parseInt(hoursText);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("teachingHours '" + hoursText + "' is not an integer");
        }
        if (teachingHours < 0) {
            throw new IllegalArgumentException("teachingHours must not be negative");
        }
        int facultyIndex = facultyList.searchID(facultyID);
        if (facultyIndex < 0) {
            throw new IllegalArgumentException("faculty '" + facultyID + "' does not exist");
        }
        if (searchID(id) >= 0) {
            throw new IllegalArgumentException("lecturer ID '" + id + "' is duplicated");
        }
        return new Lecturer(id, facultyList.get(facultyIndex), fullName, academicRank, teachingHours);
    }

    /**
     * Trim một trường đọc từ file và kiểm tra không rỗng.
     *
     * @param value     giá trị thô của trường (có thể có khoảng trắng thừa)
     * @param fieldName tên trường, dùng trong thông báo lỗi, ví dụ "fullName"
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
