package lecturerprj;

import java.util.ArrayList;
import java.util.Arrays;

import lecturerprj.list.AdvancedFeatures;
import lecturerprj.list.FacultyList;
import lecturerprj.list.LecturerList;
import lecturerprj.ui.Inputter;
import lecturerprj.ui.Menu;

/**
 * Điểm bắt đầu chương trình: nạp dữ liệu, hiển thị menu chính, gọi chức năng tương ứng.
 * Chức năng 1-11 và 0 theo đề; 12, 13 là chức năng nâng cao (lớp {@link AdvancedFeatures}).
 *
 * @author TV1 - Nguyen Trong Khoi
 */
public class LecturerManager {

    /** File dữ liệu khoa, đặt ở thư mục gốc project. */
    public static final String FACULTY_FILE = "facultys.txt";

    /** File dữ liệu giảng viên, đặt ở thư mục gốc project. */
    public static final String LECTURER_FILE = "lecturers.txt";

    /**
     * Chặn tạo đối tượng: lớp chỉ chứa hàm main và các hàm static hỗ trợ.
     */
    private LecturerManager() {
        // Không tạo đối tượng LecturerManager.
    }

    /**
     * Chạy chương trình.
     * <ol>
     *   <li>Nạp facultys.txt TRƯỚC, rồi mới nạp lecturers.txt (để đổi mã khoa thành đối tượng Faculty).
     *       Thiếu file hoặc có dòng sai chỉ in cảnh báo, chương trình vẫn chạy tiếp.</li>
     *   <li>Tạo danh sách 13 mục menu và đối tượng chức năng nâng cao.</li>
     *   <li>Chạy vòng lặp menu ({@link #runMenu}) đến khi người dùng chọn 0.</li>
     *   <li>Nếu luồng nhập bị đóng giữa chừng (Ctrl+D / Ctrl+Z) thì dừng gọn, không ghi file.</li>
     * </ol>
     *
     * @param args tham số dòng lệnh, không dùng
     */
    public static void main(String[] args) {
        // Phải nạp khoa TRƯỚC giảng viên để nối được Lecturer.faculty
        FacultyList facultyList = new FacultyList();
        facultyList.loadFromFile(FACULTY_FILE);

        LecturerList lecturerList = new LecturerList(facultyList);
        lecturerList.loadFromFile(LECTURER_FILE);

        ArrayList<String> options = new ArrayList<String>(Arrays.asList(
                "List all Facultys",
                "Add a new Faculty",
                "Search Faculty by ID",
                "Update Faculty",
                "Save Facultys to " + FACULTY_FILE,
                "List all Lecturers sorted by facultyName",
                "Search Lecturers by partial facultyName",
                "Add a new Lecturer",
                "Remove Lecturer by ID",
                "Update Lecturer by ID",
                "Save Lecturers to " + LECTURER_FILE,
                "Statistics by Faculty (advanced)",
                "Remove Faculty by ID (advanced)"));

        // Chức năng nâng cao 12, 13 (TV6) – mở rộng theo mục 9 của đề
        AdvancedFeatures advancedFeatures = new AdvancedFeatures(facultyList, lecturerList);

        try {
            runMenu(options, facultyList, lecturerList, advancedFeatures);
        } catch (IllegalStateException e) {
            // Inputter ném lỗi này khi hết luồng nhập (Ctrl+D / Ctrl+Z): không thể hỏi lại nên dừng gọn
            System.out.println();
            System.out.println("[ERROR] " + e.getMessage() + " Program stopped without saving.");
        }
    }

    /**
     * Vòng lặp menu chính: in tiêu đề và menu (qua {@code Menu.int_getChoice}, nhập sai thì tự hỏi lại),
     * gọi chức năng tương ứng với số được chọn, lặp đến khi chọn 0.
     * Chọn 0 chỉ in lời chào, KHÔNG tự ghi file (đề yêu cầu); dữ liệu chưa lưu sẽ mất.
     *
     * @param options          các mục menu 1..13
     * @param facultyList      danh sách khoa
     * @param lecturerList     danh sách giảng viên
     * @param advancedFeatures chức năng nâng cao 12, 13
     */
    private static void runMenu(ArrayList<String> options, FacultyList facultyList, LecturerList lecturerList,
            AdvancedFeatures advancedFeatures) {
        int choice;
        do {
            System.out.println();
            System.out.println("========== UNIVERSITY LECTURER MANAGEMENT ==========");
            choice = Menu.int_getChoice(options);   // in 1..13 và "0. Exit"
            switch (choice) {
                case 1:
                    facultyList.listFacultys();
                    break;
                case 2:
                    facultyList.addFaculty();
                    break;
                case 3:
                    searchFaculty(facultyList);
                    break;
                case 4:
                    facultyList.updateFaculty();
                    break;
                case 5:
                    printSaveResult(facultyList.saveToFile(FACULTY_FILE), FACULTY_FILE);
                    break;
                case 6:
                    lecturerList.listLecturers();
                    break;
                case 7:
                    lecturerList.printBasedFacultyName();
                    break;
                case 8:
                    lecturerList.addLecturer();
                    break;
                case 9:
                    lecturerList.removeLecturer();
                    break;
                case 10:
                    lecturerList.updateLecturer();
                    break;
                case 11:
                    printSaveResult(lecturerList.saveToFile(LECTURER_FILE), LECTURER_FILE);
                    break;
                case 12:
                    advancedFeatures.printStatistics();
                    break;
                case 13:
                    advancedFeatures.removeFaculty();
                    break;
                case 0:
                    // Đề yêu cầu: thoát KHÔNG tự động ghi file
                    System.out.println("Goodbye! (Unsaved changes were NOT written to file.)");
                    break;
                default:
                    // Menu.int_getChoice đã kiểm tra khoảng, nhánh này chỉ để phòng hờ
                    System.out.println("[ERROR] Invalid choice.");
                    break;
            }
        } while (choice != 0);
    }

    /**
     * Chức năng 3: tìm khoa theo mã.
     * Danh sách rỗng thì báo [INFO]. Nhập mã khoa (không phân biệt hoa/thường), dùng
     * {@code facultyList.searchID} để tìm; tìm thấy thì in khoa đó theo mẫu bảng (QUY_UOC.md mục 6),
     * không thấy thì báo {@code [INFO] Faculty <id> not found.}
     *
     * @param facultyList danh sách khoa cần tìm
     */
    private static void searchFaculty(FacultyList facultyList) {
        if (facultyList.isEmpty()) {
            System.out.println("[INFO]  List is empty.");
            return;
        }
        String id = Inputter.inputString("Enter faculty ID: ");
        int index = facultyList.searchID(id);
        if (index < 0) {
            System.out.println("[INFO]  Faculty " + id.toUpperCase() + " not found.");
            return;
        }
        FacultyList.printHeader();
        FacultyList.printRow(facultyList.get(index));
    }

    /**
     * In kết quả lưu file (chức năng 5 và 11) theo mẫu thông báo chung:
     * {@code [OK] Saved to <file>.} hoặc {@code [ERROR] Cannot save <file>.}
     *
     * @param success  kết quả trả về từ {@code saveToFile}
     * @param filename tên file vừa lưu
     */
    private static void printSaveResult(boolean success, String filename) {
        if (success) {
            System.out.println("[OK]    Saved to " + filename + ".");
        } else {
            System.out.println("[ERROR] Cannot save " + filename + ".");
        }
    }
}
