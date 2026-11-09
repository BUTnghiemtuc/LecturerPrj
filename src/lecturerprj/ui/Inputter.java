package lecturerprj.ui;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Nhập dữ liệu từ bàn phím có kiểm tra (phần mở rộng).
 * Mọi hàm nhập đều lặp: nhập sai thì in {@code [ERROR] ...} và hỏi lại cho đến khi đúng,
 * nên nơi gọi luôn nhận được giá trị hợp lệ và chương trình không bao giờ dừng vì nhập sai.
 *
 * <p>Mỗi loại dữ liệu có 2 phiên bản:
 * <ul>
 *   <li>bản thường (dùng khi THÊM mới): bắt buộc nhập;</li>
 *   <li>bản có {@code oldValue} (dùng khi CẬP NHẬT): in giá trị cũ trong ngoặc, nhấn Enter để giữ nguyên.</li>
 * </ul>
 *
 * <p>QUY TẮC: cả chương trình chỉ dùng DUY NHẤT Scanner {@link #SC} này.
 * Không tạo Scanner mới, không close(). Luôn đọc bằng nextLine() rồi tự chuyển kiểu.
 *
 * @author TV4 - (điền họ tên)
 */
public class Inputter {

    /** Ký tự phân tách trường trong file dữ liệu, không được xuất hiện trong dữ liệu chữ. */
    public static final String SEPARATOR = ",";

    /** Scanner dùng chung cho toàn chương trình (đọc từ bàn phím). */
    private static final Scanner SC = new Scanner(System.in);

    /**
     * Chặn tạo đối tượng: lớp tiện ích chỉ có method static.
     */
    private Inputter() {
        // Lớp tiện ích, chỉ có method static, không tạo đối tượng.
    }

    /**
     * Nhập chuỗi bắt buộc (dùng khi thêm mới: mã, tên, phòng, học hàm...).
     * Chuỗi được trim; nếu rỗng hoặc chứa dấu phẩy (ký tự phân tách của file dữ liệu)
     * thì báo lỗi và hỏi lại.
     *
     * @param prompt câu hỏi in ra trước khi nhập, ví dụ "Enter faculty name: "
     * @return chuỗi hợp lệ đã trim (không rỗng, không có dấu phẩy)
     */
    public static String inputString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = readLine().trim();
            if (value.isEmpty()) {
                System.out.println("[ERROR] Value must not be empty. Please try again.");
            } else if (value.contains(SEPARATOR)) {
                System.out.println("[ERROR] Value must not contain ','. Please try again.");
            } else {
                return value;
            }
        }
    }

    /**
     * Nhập chuỗi khi CẬP NHẬT. In giá trị cũ trong ngoặc, ví dụ "Enter office [A301]: ".
     * Nhấn Enter (bỏ trống) để giữ giá trị cũ; chuỗi mới chứa dấu phẩy thì báo lỗi và hỏi lại.
     *
     * @param prompt   câu hỏi, không có dấu ": " ở cuối (hàm tự thêm), ví dụ "Enter office"
     * @param oldValue giá trị hiện tại, được in trong ngoặc và trả về nếu người dùng bỏ trống
     * @return chuỗi mới đã trim, hoặc {@code oldValue} nếu người dùng chỉ nhấn Enter
     */
    public static String inputString(String prompt, String oldValue) {
        while (true) {
            System.out.print(prompt + " [" + oldValue + "]: ");
            String value = readLine().trim();
            if (value.isEmpty()) {
                return oldValue;
            }
            if (value.contains(SEPARATOR)) {
                System.out.println("[ERROR] Value must not contain ','. Please try again.");
            } else {
                return value;
            }
        }
    }

    /**
     * Nhập số nguyên trong khoảng [min, max]. Bỏ trống, nhập chữ hoặc số ngoài khoảng:
     * báo lỗi và hỏi lại. Dùng cho lựa chọn menu (0..n) và teachingHours (min = 0,
     * max = Integer.MAX_VALUE).
     *
     * @param prompt câu hỏi in ra trước khi nhập, ví dụ "Enter teaching hours: "
     * @param min    giá trị nhỏ nhất được phép
     * @param max    giá trị lớn nhất được phép
     * @return số nguyên hợp lệ trong khoảng [min, max]
     */
    public static int inputInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String value = readLine().trim();
            if (value.isEmpty()) {
                System.out.println("[ERROR] Value must not be empty. Please try again.");
                continue;
            }
            Integer number = parseIntInRange(value, min, max);
            if (number != null) {
                return number;
            }
        }
    }

    /**
     * Nhập số nguyên khi CẬP NHẬT: như {@link #inputInt(String, int, int)} nhưng in giá trị cũ
     * trong ngoặc và nhấn Enter để giữ nguyên, ví dụ "Enter teaching hours [120]: ".
     *
     * @param prompt   câu hỏi, không có dấu ": " ở cuối (hàm tự thêm)
     * @param min      giá trị nhỏ nhất được phép
     * @param max      giá trị lớn nhất được phép
     * @param oldValue giá trị hiện tại, trả về nếu người dùng bỏ trống
     * @return số mới hợp lệ trong khoảng [min, max], hoặc {@code oldValue} nếu bỏ trống
     */
    public static int inputInt(String prompt, int min, int max, int oldValue) {
        while (true) {
            System.out.print(prompt + " [" + oldValue + "]: ");
            String value = readLine().trim();
            if (value.isEmpty()) {
                return oldValue;
            }
            Integer number = parseIntInRange(value, min, max);
            if (number != null) {
                return number;
            }
        }
    }

    /**
     * Nhập số thực lớn hơn 0 (dùng cho researchBudget khi thêm khoa).
     * Bỏ trống, nhập chữ, NaN/Infinity, số âm hoặc 0: báo lỗi và hỏi lại.
     *
     * @param prompt câu hỏi in ra trước khi nhập, ví dụ "Enter research budget: "
     * @return số thực hữu hạn lớn hơn 0
     */
    public static double inputPositiveDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = readLine().trim();
            if (value.isEmpty()) {
                System.out.println("[ERROR] Value must not be empty. Please try again.");
                continue;
            }
            Double number = parsePositiveDouble(value);
            if (number != null) {
                return number;
            }
        }
    }

    /**
     * Nhập số thực lớn hơn 0 khi CẬP NHẬT: như {@link #inputPositiveDouble(String)} nhưng in giá trị cũ
     * (dạng số thường, không phải 6.0E8) trong ngoặc và nhấn Enter để giữ nguyên.
     *
     * @param prompt   câu hỏi, không có dấu ": " ở cuối (hàm tự thêm)
     * @param oldValue giá trị hiện tại, trả về nếu người dùng bỏ trống
     * @return số mới lớn hơn 0, hoặc {@code oldValue} nếu bỏ trống
     */
    public static double inputPositiveDouble(String prompt, double oldValue) {
        while (true) {
            System.out.print(prompt + " [" + toPlainString(oldValue) + "]: ");
            String value = readLine().trim();
            if (value.isEmpty()) {
                return oldValue;
            }
            Double number = parsePositiveDouble(value);
            if (number != null) {
                return number;
            }
        }
    }

    /**
     * Hỏi Có/Không, ví dụ trước khi xóa: "Delete L01? (Y/N): ".
     * Chỉ chấp nhận Y hoặc N (không phân biệt hoa/thường); nhập khác thì báo lỗi và hỏi lại.
     *
     * @param prompt câu hỏi in ra, nên kết thúc bằng "(Y/N): "
     * @return true nếu người dùng nhập Y/y, false nếu nhập N/n
     */
    public static boolean confirm(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = readLine().trim();
            if (value.equalsIgnoreCase("Y")) {
                return true;
            }
            if (value.equalsIgnoreCase("N")) {
                return false;
            }
            System.out.println("[ERROR] Please enter Y or N.");
        }
    }

    // ===================== Hàm hỗ trợ (private) =====================

    /**
     * Đọc một dòng từ bàn phím bằng Scanner dùng chung.
     * Nếu luồng nhập đã bị đóng (Ctrl+D / Ctrl+Z, hoặc hết dữ liệu khi chạy bằng file) thì không thể
     * hỏi lại được nữa, nên ném lỗi để {@code LecturerManager} dừng chương trình gọn gàng
     * thay vì lặp vô hạn.
     *
     * @return dòng vừa đọc (chưa trim)
     * @throws IllegalStateException nếu luồng nhập đã đóng
     */
    private static String readLine() {
        try {
            return SC.nextLine();
        } catch (NoSuchElementException e) {
            throw new IllegalStateException("Input stream closed.", e);
        }
    }

    /**
     * Chuyển số thực thành chuỗi số thường để in làm giá trị cũ: 600000000 thay vì 6.0E8,
     * và không làm tròn mất phần lẻ (1500.5 vẫn là 1500.5).
     *
     * @param number số cần chuyển
     * @return chuỗi số thường, không có số 0 thừa ở cuối
     */
    private static String toPlainString(double number) {
        return BigDecimal.valueOf(number).stripTrailingZeros().toPlainString();
    }

    /**
     * Chuyển chuỗi sang int và kiểm tra nằm trong [min, max].
     * Sai định dạng hoặc ngoài khoảng thì tự in thông báo [ERROR] phù hợp.
     *
     * @param value chuỗi người dùng nhập (đã trim, không rỗng)
     * @param min   giá trị nhỏ nhất được phép
     * @param max   giá trị lớn nhất được phép
     * @return số hợp lệ, hoặc null nếu không hợp lệ (nơi gọi sẽ hỏi lại)
     */
    private static Integer parseIntInRange(String value, int min, int max) {
        try {
            int number = Integer.parseInt(value);
            if (number < min || number > max) {
                // Không có cận trên thực sự (teachingHours) thì đừng in con số 2147483647 khó hiểu
                String range = (max == Integer.MAX_VALUE) ? "at least " + min : "between " + min + " and " + max;
                System.out.println("[ERROR] Value must be " + range + ". Please try again.");
                return null;
            }
            return number;
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] '" + value + "' is not a valid integer. Please try again.");
            return null;
        }
    }

    /**
     * Chuyển chuỗi sang double và kiểm tra lớn hơn 0.
     * Chặn cả NaN và Infinity vì {@code Double.parseDouble} chấp nhận 2 chuỗi này.
     * Không hợp lệ thì tự in thông báo [ERROR] phù hợp.
     *
     * @param value chuỗi người dùng nhập (đã trim, không rỗng)
     * @return số hợp lệ lớn hơn 0, hoặc null nếu không hợp lệ (nơi gọi sẽ hỏi lại)
     */
    private static Double parsePositiveDouble(String value) {
        try {
            double number = Double.parseDouble(value);
            if (Double.isNaN(number) || Double.isInfinite(number)) {
                System.out.println("[ERROR] '" + value + "' is not a valid number. Please try again.");
                return null;
            }
            if (number <= 0) {
                System.out.println("[ERROR] Value must be greater than 0. Please try again.");
                return null;
            }
            return number;
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] '" + value + "' is not a valid number. Please try again.");
            return null;
        }
    }
}
