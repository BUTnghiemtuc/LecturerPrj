package lecturerprj.list;

import lecturerprj.model.Faculty;
import lecturerprj.model.Lecturer;
import lecturerprj.ui.Inputter;

/**
 * Chức năng nâng cao (mở rộng theo mục 9 của đề), menu 12 và 13:
 * <ul>
 *   <li>12. Thống kê theo khoa: số giảng viên, tổng và trung bình giờ giảng.</li>
 *   <li>13. Xóa khoa theo mã: chỉ cho xóa khi khoa không còn giảng viên.</li>
 * </ul>
 * Viết trong file riêng để không phải sửa file của thành viên khác; làm việc trực tiếp trên
 * 2 danh sách mà LecturerManager đang dùng (không sao chép dữ liệu).
 *
 * @author TV6 - (điền họ tên)
 */
public class AdvancedFeatures {

    /** Mẫu dòng bảng thống kê: mã khoa, tên khoa, số giảng viên, tổng giờ, giờ trung bình. */
    private static final String STAT_FORMAT = "%-6s | %-25s | %9d | %11d | %9s";

    /** Mẫu dòng tiêu đề, cùng độ rộng cột với {@link #STAT_FORMAT}. */
    private static final String STAT_HEADER_FORMAT = "%-6s | %-25s | %9s | %11s | %9s";

    /** Đường kẻ dưới tiêu đề bảng, dài bằng tổng độ rộng các cột và 4 dấu " | ". */
    private static final String TABLE_LINE = new String(new char[6 + 25 + 9 + 11 + 9 + 4 * 3]).replace('\0', '-');

    /** Danh sách khoa đang dùng trong chương trình (để thống kê và xóa khoa). */
    private FacultyList facultyList;

    /** Danh sách giảng viên đang dùng trong chương trình (để đếm giảng viên, cộng giờ giảng). */
    private LecturerList lecturerList;

    /**
     * Tạo đối tượng chức năng nâng cao làm việc trên 2 danh sách hiện có.
     *
     * @param facultyList  danh sách khoa (dùng chung với LecturerManager)
     * @param lecturerList danh sách giảng viên (dùng chung với LecturerManager)
     */
    public AdvancedFeatures(FacultyList facultyList, LecturerList lecturerList) {
        this.facultyList = facultyList;
        this.lecturerList = lecturerList;
    }

    /**
     * Chức năng 12: thống kê theo khoa.
     * Với mỗi khoa (theo thứ tự trong danh sách khoa) in một dòng gồm: số giảng viên, tổng giờ giảng
     * và giờ giảng trung bình (làm tròn 1 chữ số; khoa chưa có giảng viên in "-").
     * Dòng cuối "ALL" là tổng của toàn trường. Danh sách khoa rỗng thì báo [INFO].
     * Chỉ đọc dữ liệu, không thay đổi gì.
     */
    public void printStatistics() {
        if (facultyList.isEmpty()) {
            System.out.println("[INFO]  List is empty.");
            return;
        }
        System.out.println(String.format(STAT_HEADER_FORMAT, "ID", "Faculty", "Lecturers", "Total hours",
                "Avg hours"));
        System.out.println(TABLE_LINE);
        int totalCount = 0;
        long totalHours = 0;
        for (Faculty faculty : facultyList) {
            int count = 0;
            // long vì tổng nhiều giá trị int (mỗi giá trị tới 2^31 - 1) có thể tràn int
            long hours = 0;
            for (Lecturer lecturer : lecturerList) {
                if (lecturer.getFaculty() == faculty) {
                    // So sánh THAM CHIẾU đối tượng Faculty (không phải String): mỗi giảng viên trỏ tới
                    // đúng đối tượng khoa trong facultyList
                    count++;
                    hours += lecturer.getTeachingHours();
                }
            }
            System.out.println(String.format(STAT_FORMAT, faculty.getFacultyID(), faculty.getFacultyName(), count,
                    hours, formatAverage(hours, count)));
            totalCount += count;
            totalHours += hours;
        }
        System.out.println(TABLE_LINE);
        System.out.println(String.format(STAT_FORMAT, "ALL", "All faculties", totalCount, totalHours,
                formatAverage(totalHours, totalCount)));
    }

    /**
     * Chức năng 13: xóa khoa theo mã.
     * <ol>
     *   <li>Danh sách khoa rỗng: báo [INFO]. Nhập mã; không tìm thấy thì báo [INFO].</li>
     *   <li>Khoa còn giảng viên: báo [ERROR] và KHÔNG xóa (nếu xóa, lecturers.txt sẽ tham chiếu tới
     *       khoa không tồn tại và các dòng đó bị bỏ qua ở lần chạy sau).</li>
     *   <li>In thông tin khoa và hỏi xác nhận Y/N; Y thì xóa và nhắc dùng chức năng 5 để lưu.</li>
     * </ol>
     *
     * @return true nếu đã xóa; false nếu danh sách rỗng, không tìm thấy, khoa còn giảng viên hoặc người dùng hủy
     */
    public boolean removeFaculty() {
        if (facultyList.isEmpty()) {
            System.out.println("[INFO]  List is empty.");
            return false;
        }
        String id = Inputter.inputString("Enter faculty ID to remove: ").toUpperCase();
        int index = facultyList.searchID(id);
        if (index < 0) {
            System.out.println("[INFO]  Faculty " + id + " not found.");
            return false;
        }
        Faculty faculty = facultyList.get(index);
        int count = countLecturers(faculty);
        if (count > 0) {
            System.out.println("[ERROR] Faculty " + id + " still has " + count
                    + " lecturer(s). Move or remove them first.");
            return false;
        }
        FacultyList.printHeader();
        FacultyList.printRow(faculty);
        if (!Inputter.confirm("Delete " + id + "? (Y/N): ")) {
            System.out.println("[INFO]  Delete cancelled.");
            return false;
        }
        facultyList.remove(index);
        System.out.println("[OK]    Faculty " + id + " removed. Use option 5 to save the change.");
        return true;
    }

    /**
     * Đếm số giảng viên thuộc một khoa (so sánh tham chiếu đối tượng Faculty).
     *
     * @param faculty khoa cần đếm, là đối tượng lấy từ facultyList
     * @return số giảng viên của khoa đó (0 nếu chưa có)
     */
    private int countLecturers(Faculty faculty) {
        int count = 0;
        for (Lecturer lecturer : lecturerList) {
            if (lecturer.getFaculty() == faculty) {
                count++;
            }
        }
        return count;
    }

    /**
     * Tính giờ giảng trung bình để hiển thị.
     *
     * @param total tổng số giờ giảng
     * @param count số giảng viên
     * @return trung bình làm tròn 1 chữ số (ví dụ "150.0"), hoặc "-" nếu {@code count} = 0 (tránh chia cho 0)
     */
    private static String formatAverage(long total, int count) {
        if (count == 0) {
            return "-";
        }
        return String.format("%.1f", (double) total / count);
    }
}
