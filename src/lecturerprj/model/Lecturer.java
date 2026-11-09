package lecturerprj.model;

/**
 * Giảng viên, thuộc đúng một khoa {@link Faculty} (quan hệ association).
 *
 * <p>Định dạng 1 dòng trong lecturers.txt (QUY_UOC.md mục 5.2):
 * {@code lecturerID, facultyID, fullName, academicRank, teachingHours}
 * Trong file lưu MÃ khoa; trong chương trình lưu ĐỐI TƯỢNG Faculty, nên khi sửa tên/mã khoa
 * thì mọi giảng viên của khoa đó tự thấy thay đổi.
 *
 * <p>Hiện thực {@link Comparable} để {@code Collections.sort} sắp xếp theo tên khoa rồi mã giảng viên
 * (chức năng 6). Việc kiểm tra ràng buộc dữ liệu do {@code LecturerList} và {@code Inputter} đảm nhận.
 *
 * @author TV3 - (điền họ tên)
 */
public class Lecturer implements Comparable<Lecturer> {

    /** Số trường của một dòng trong lecturers.txt, dùng để kiểm tra dòng thiếu/thừa trường khi đọc file. */
    public static final int FIELD_COUNT = 5;

    /** Mẫu một dòng bảng hiển thị (QUY_UOC.md mục 6): mã, tên khoa, họ tên, học hàm, số giờ. */
    public static final String SCREEN_FORMAT = "%-6s | %-25s | %-22s | %-20s | %5d";

    /** Mẫu dòng tiêu đề, cùng độ rộng cột với {@link #SCREEN_FORMAT}. */
    public static final String HEADER_FORMAT = "%-6s | %-25s | %-22s | %-20s | %5s";

    /** Chuỗi phân tách các trường khi ghi file (dấu phẩy + 1 dấu cách). */
    private static final String FILE_SEPARATOR = ", ";

    /** Mã giảng viên, luôn lưu chữ in hoa và đã trim, ví dụ "L01". Không trùng trong LecturerList. */
    private String lecturerID;

    /** Khoa của giảng viên: tham chiếu tới đúng đối tượng Faculty nằm trong FacultyList. */
    private Faculty faculty;

    /** Họ tên giảng viên, ví dụ "Nguyen An". */
    private String fullName;

    /** Học hàm / chức danh, ví dụ "Senior Lecturer", "Associate Professor". */
    private String academicRank;

    /** Số giờ giảng, không âm. */
    private int teachingHours;

    /**
     * Tạo giảng viên rỗng: các chuỗi là "", chưa có khoa (null), số giờ là 0.
     * Chỉ dùng khi cần đối tượng tạm; đối tượng này CHƯA hợp lệ vì chưa có khoa.
     */
    public Lecturer() {
        this("", null, "", "", 0);
    }

    /**
     * Tạo giảng viên với đầy đủ thông tin. Các giá trị được đưa qua setter nên mã tự trim và
     * đổi sang chữ in hoa, các chuỗi khác tự trim, giá trị null của chuỗi đổi thành chuỗi rỗng.
     *
     * @param lecturerID    mã giảng viên, ví dụ "l01" sẽ được lưu thành "L01"
     * @param faculty       khoa của giảng viên, phải là đối tượng lấy từ FacultyList
     * @param fullName      họ tên
     * @param academicRank  học hàm, ví dụ "Senior Lecturer"
     * @param teachingHours số giờ giảng, nơi gọi phải đảm bảo không âm
     */
    public Lecturer(String lecturerID, Faculty faculty, String fullName, String academicRank, int teachingHours) {
        setLecturerID(lecturerID);
        setFaculty(faculty);
        setFullName(fullName);
        setAcademicRank(academicRank);
        setTeachingHours(teachingHours);
    }

    // ===================== Getters / Setters =====================

    /**
     * Lấy mã giảng viên.
     *
     * @return mã giảng viên (chữ in hoa, đã trim), ví dụ "L01"
     */
    public String getLecturerID() {
        return lecturerID;
    }

    /**
     * Gán mã giảng viên. Tự trim và đổi sang chữ in hoa để mọi phép so sánh mã thống nhất;
     * null được đổi thành chuỗi rỗng. Không kiểm tra trùng mã: việc đó do
     * {@code LecturerList.searchID} làm trước khi gọi.
     *
     * @param lecturerID mã giảng viên mới
     */
    public void setLecturerID(String lecturerID) {
        this.lecturerID = (lecturerID == null) ? "" : lecturerID.trim().toUpperCase();
    }

    /**
     * Lấy khoa của giảng viên.
     *
     * @return đối tượng Faculty (không phải mã khoa), hoặc null nếu chưa gán
     */
    public Faculty getFaculty() {
        return faculty;
    }

    /**
     * Gán khoa cho giảng viên. Phải truyền đối tượng lấy từ FacultyList (qua menu chọn khoa
     * hoặc {@code facultyList.searchID} khi đọc file) để giữ đúng liên kết.
     *
     * @param faculty khoa mới
     */
    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    /**
     * Lấy họ tên giảng viên.
     *
     * @return họ tên
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Gán họ tên (tự trim; null đổi thành chuỗi rỗng).
     *
     * @param fullName họ tên mới
     */
    public void setFullName(String fullName) {
        this.fullName = (fullName == null) ? "" : fullName.trim();
    }

    /**
     * Lấy học hàm / chức danh.
     *
     * @return học hàm, ví dụ "Lecturer"
     */
    public String getAcademicRank() {
        return academicRank;
    }

    /**
     * Gán học hàm / chức danh (tự trim; null đổi thành chuỗi rỗng).
     *
     * @param academicRank học hàm mới
     */
    public void setAcademicRank(String academicRank) {
        this.academicRank = (academicRank == null) ? "" : academicRank.trim();
    }

    /**
     * Lấy số giờ giảng.
     *
     * @return số giờ giảng (không âm nếu đối tượng hợp lệ)
     */
    public int getTeachingHours() {
        return teachingHours;
    }

    /**
     * Gán số giờ giảng. Setter không tự kiểm tra; nơi gọi ({@code Inputter.inputInt} với min = 0
     * hoặc {@code LecturerList.loadFromFile}) đã đảm bảo giá trị không âm.
     *
     * @param teachingHours số giờ giảng mới
     */
    public void setTeachingHours(int teachingHours) {
        this.teachingHours = teachingHours;
    }

    // ===================== Chuỗi & so sánh =====================

    /**
     * Tạo chuỗi để GHI FILE (không dùng để hiển thị), ví dụ:
     * {@code L01, F01, Nguyen An, Senior Lecturer, 180}.
     * Cột 2 là MÃ khoa ({@code faculty.getFacultyID()}); nếu chưa có khoa thì để trống.
     * Được {@code LecturerList.saveToFile} gọi cho từng giảng viên.
     *
     * @return một dòng đúng định dạng lecturers.txt
     */
    @Override
    public String toString() {
        String facultyID = (faculty == null) ? "" : faculty.getFacultyID();
        return lecturerID + FILE_SEPARATOR + facultyID + FILE_SEPARATOR + fullName + FILE_SEPARATOR
                + academicRank + FILE_SEPARATOR + teachingHours;
    }

    /**
     * Tạo chuỗi để HIỂN THỊ lên màn hình (một dòng của bảng giảng viên), theo mẫu QUY_UOC.md mục 6:
     * {@code "%-6s | %-25s | %-22s | %-20s | %5d"}. Khác {@link #toString()}: cột 2 là TÊN khoa
     * thay vì mã khoa, các cột được căn thẳng hàng.
     * Dùng ở chức năng 6, 7, 9, 10.
     *
     * @return một dòng của bảng giảng viên, ví dụ
     *         {@code L01    | Computer Science          | Nguyen An              | Senior Lecturer      |   180}
     */
    public String screenString() {
        return String.format(SCREEN_FORMAT, lecturerID, getFacultyName(), fullName, academicRank, teachingHours);
    }

    /**
     * So sánh hai giảng viên để sắp xếp (chức năng 6, 7):
     * <ol>
     *   <li>theo tên khoa tăng dần, không phân biệt hoa/thường;</li>
     *   <li>nếu cùng tên khoa thì theo mã giảng viên tăng dần.</li>
     * </ol>
     * {@code Collections.sort} gọi hàm này thông qua interface {@link Comparable}.
     *
     * @param other giảng viên cần so sánh
     * @return số âm nếu giảng viên này đứng trước {@code other}, 0 nếu bằng nhau, số dương nếu đứng sau
     */
    @Override
    public int compareTo(Lecturer other) {
        int result = getFacultyName().compareToIgnoreCase(other.getFacultyName());
        if (result == 0) {
            result = lecturerID.compareToIgnoreCase(other.lecturerID);
        }
        return result;
    }

    /**
     * Lấy tên khoa của giảng viên một cách an toàn (dùng cho hiển thị và sắp xếp).
     *
     * @return tên khoa, hoặc chuỗi rỗng nếu chưa gán khoa (tránh NullPointerException)
     */
    private String getFacultyName() {
        return (faculty == null) ? "" : faculty.getFacultyName();
    }
}
