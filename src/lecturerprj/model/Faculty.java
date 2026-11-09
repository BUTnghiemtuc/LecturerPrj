package lecturerprj.model;

import java.math.BigDecimal;

/**
 * Khoa trong trường đại học (ví dụ: F01 - Computer Science).
 * Một khoa có thể có nhiều giảng viên {@link Lecturer}; mỗi giảng viên giữ tham chiếu tới đúng
 * một đối tượng Faculty (quan hệ association).
 *
 * <p>Định dạng 1 dòng trong facultys.txt (QUY_UOC.md mục 5.1):
 * {@code facultyID, facultyName, office, researchBudget}
 *
 * <p>Lớp này chỉ chứa dữ liệu (đóng gói bằng thuộc tính private + getter/setter) và cách chuyển
 * đối tượng thành dòng file ({@link #toString()}). Việc kiểm tra ràng buộc (không rỗng, không trùng,
 * ngân sách lớn hơn 0) do {@code FacultyList} và {@code Inputter} đảm nhận trước khi gọi setter.
 *
 * @author TV2 - (điền họ tên)
 */
public class Faculty {

    /** Số trường của một dòng trong facultys.txt, dùng để kiểm tra dòng thiếu/thừa trường khi đọc file. */
    public static final int FIELD_COUNT = 4;

    /** Chuỗi phân tách các trường khi ghi file (dấu phẩy + 1 dấu cách). */
    private static final String FILE_SEPARATOR = ", ";

    /** Mã khoa, luôn lưu chữ in hoa và đã trim, ví dụ "F01". Không trùng trong FacultyList. */
    private String facultyID;

    /** Tên khoa, ví dụ "Computer Science". Dùng để sắp xếp và tìm giảng viên (chức năng 6, 7). */
    private String facultyName;

    /** Phòng làm việc của khoa, ví dụ "A301". */
    private String office;

    /** Ngân sách nghiên cứu của khoa (đơn vị đồng), phải lớn hơn 0. */
    private double researchBudget;

    /**
     * Tạo khoa rỗng: các chuỗi là "" và ngân sách là 0.
     * Chỉ dùng khi cần một đối tượng tạm rồi gán dần bằng setter; đối tượng này CHƯA hợp lệ
     * (ngân sách chưa lớn hơn 0) nên không được thêm thẳng vào danh sách.
     */
    public Faculty() {
        this("", "", "", 0);
    }

    /**
     * Tạo khoa với đầy đủ thông tin. Các giá trị được đưa qua setter nên mã khoa tự trim và
     * đổi sang chữ in hoa, các chuỗi khác tự trim, giá trị null đổi thành chuỗi rỗng.
     *
     * @param facultyID      mã khoa, ví dụ "f01" sẽ được lưu thành "F01"
     * @param facultyName    tên khoa
     * @param office         phòng làm việc
     * @param researchBudget ngân sách nghiên cứu, nơi gọi phải đảm bảo lớn hơn 0
     */
    public Faculty(String facultyID, String facultyName, String office, double researchBudget) {
        setFacultyID(facultyID);
        setFacultyName(facultyName);
        setOffice(office);
        setResearchBudget(researchBudget);
    }

    // ===================== Getters / Setters =====================

    /**
     * Lấy mã khoa.
     *
     * @return mã khoa (chữ in hoa, đã trim), ví dụ "F01"
     */
    public String getFacultyID() {
        return facultyID;
    }

    /**
     * Gán mã khoa. Tự trim và đổi sang chữ in hoa để mọi phép so sánh mã trong chương trình
     * thống nhất; null được đổi thành chuỗi rỗng để tránh NullPointerException.
     * Không kiểm tra trùng mã: việc đó do {@code FacultyList.searchID} làm trước khi gọi.
     *
     * @param facultyID mã khoa mới
     */
    public void setFacultyID(String facultyID) {
        this.facultyID = (facultyID == null) ? "" : facultyID.trim().toUpperCase();
    }

    /**
     * Lấy tên khoa.
     *
     * @return tên khoa, ví dụ "Computer Science"
     */
    public String getFacultyName() {
        return facultyName;
    }

    /**
     * Gán tên khoa (tự trim; null đổi thành chuỗi rỗng).
     *
     * @param facultyName tên khoa mới
     */
    public void setFacultyName(String facultyName) {
        this.facultyName = (facultyName == null) ? "" : facultyName.trim();
    }

    /**
     * Lấy phòng làm việc của khoa.
     *
     * @return phòng làm việc, ví dụ "A301"
     */
    public String getOffice() {
        return office;
    }

    /**
     * Gán phòng làm việc (tự trim; null đổi thành chuỗi rỗng).
     *
     * @param office phòng làm việc mới
     */
    public void setOffice(String office) {
        this.office = (office == null) ? "" : office.trim();
    }

    /**
     * Lấy ngân sách nghiên cứu.
     *
     * @return ngân sách nghiên cứu (lớn hơn 0 nếu đối tượng hợp lệ)
     */
    public double getResearchBudget() {
        return researchBudget;
    }

    /**
     * Gán ngân sách nghiên cứu. Setter không tự kiểm tra; nơi gọi ({@code Inputter.inputPositiveDouble}
     * hoặc {@code FacultyList.loadFromFile}) đã đảm bảo giá trị lớn hơn 0.
     *
     * @param researchBudget ngân sách nghiên cứu mới
     */
    public void setResearchBudget(double researchBudget) {
        this.researchBudget = researchBudget;
    }

    // ===================== Chuỗi =====================

    /**
     * Tạo chuỗi để GHI FILE (không dùng để hiển thị), ví dụ:
     * {@code F01, Computer Science, A301, 600000000}.
     * Các trường cách nhau bằng ", ", ngân sách ghi dạng số thường (không phải 6.0E8)
     * để lần đọc sau {@code Double.parseDouble} đọc lại đúng giá trị.
     * Được {@code FacultyList.saveToFile} gọi cho từng khoa, và {@code Menu} gọi khi in danh sách chọn khoa.
     *
     * @return một dòng đúng định dạng facultys.txt
     */
    @Override
    public String toString() {
        return facultyID + FILE_SEPARATOR + facultyName + FILE_SEPARATOR + office + FILE_SEPARATOR
                + budgetToPlainString();
    }

    /**
     * Chuyển ngân sách thành chuỗi số thường để ghi file.
     * Với số nguyên cho kết quả y hệt {@code String.format(Locale.US, "%.0f", ...)} (600000000);
     * dùng BigDecimal để nếu người dùng nhập số lẻ (1500.5) thì không bị làm tròn mất phần lẻ.
     *
     * @return ngân sách dạng chuỗi, không có ký hiệu khoa học và không có số 0 thừa ở cuối
     */
    private String budgetToPlainString() {
        return BigDecimal.valueOf(researchBudget).stripTrailingZeros().toPlainString();
    }
}
