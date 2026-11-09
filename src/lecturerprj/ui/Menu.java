package lecturerprj.ui;

import java.util.ArrayList;

/**
 * Hiển thị danh sách lựa chọn và lấy lựa chọn của người dùng.
 * Dùng generic {@code <E>} nên in được mọi loại danh sách (String, Faculty, ...):
 * mỗi phần tử được in bằng {@code toString()} của chính nó (đa hình).
 *
 * <p>Quy ước: các mục được đánh số từ 1; số 0 luôn là thoát/hủy.
 * Nhập chữ hoặc số ngoài khoảng: báo [ERROR] và hỏi lại, không làm dừng chương trình
 * (dùng {@code Inputter.inputInt}).
 *
 * @author TV5 - (điền họ tên)
 */
public class Menu {

    /** Câu hỏi khi chọn một mục. */
    private static final String CHOICE_PROMPT = "Enter your choice: ";

    /**
     * Chặn tạo đối tượng: lớp tiện ích chỉ có method static.
     */
    private Menu() {
        // Lớp tiện ích, chỉ có method static, không tạo đối tượng.
    }

    /**
     * Hiện menu và trả về SỐ thứ tự người dùng chọn. Dùng cho menu chính trong LecturerManager.
     * In các mục {@code 1..n} kèm dòng {@code 0. Exit}, rồi đọc lựa chọn trong khoảng 0..n
     * (nhập sai thì hỏi lại).
     *
     * @param options danh sách mục cần in (in bằng toString của từng phần tử)
     * @param <E>     kiểu phần tử của danh sách
     * @return số người dùng chọn, từ 0 (thoát) đến {@code options.size()}
     */
    public static <E> int int_getChoice(ArrayList<E> options) {
        printOptions(options);
        System.out.println(" 0. Exit");
        return Inputter.inputInt(CHOICE_PROMPT, 0, options.size());
    }

    /**
     * Hiện danh sách và trả về chính PHẦN TỬ người dùng chọn (không phải số thứ tự).
     * Dùng cho {@code FacultyList.getUserChoice()} để chọn khoa khi thêm/sửa giảng viên, nhờ vậy
     * người dùng không thể gõ tay một mã khoa không tồn tại.
     * In các mục {@code 1..n} kèm dòng {@code 0. Cancel}; danh sách rỗng thì báo [INFO].
     *
     * @param options danh sách để chọn
     * @param <E>     kiểu phần tử của danh sách
     * @return phần tử được chọn, hoặc null nếu danh sách rỗng / null hoặc người dùng chọn 0 (hủy)
     */
    public static <E> E ref_getChoice(ArrayList<E> options) {
        if (options == null || options.isEmpty()) {
            System.out.println("[INFO]  List is empty.");
            return null;
        }
        printOptions(options);
        System.out.println(" 0. Cancel");
        int choice = Inputter.inputInt(CHOICE_PROMPT, 0, options.size());
        if (choice == 0) {
            return null;
        }
        return options.get(choice - 1);
    }

    /**
     * In các mục của danh sách, mỗi mục một dòng, đánh số từ 1 (ví dụ " 1. List all Facultys").
     * Dùng chung cho {@link #int_getChoice(ArrayList)} và {@link #ref_getChoice(ArrayList)}.
     *
     * @param options danh sách cần in
     * @param <E>     kiểu phần tử của danh sách
     */
    private static <E> void printOptions(ArrayList<E> options) {
        for (int i = 0; i < options.size(); i++) {
            System.out.printf("%2d. %s%n", i + 1, options.get(i));
        }
    }
}
