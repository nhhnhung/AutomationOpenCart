import com.opencart.helpers.ExcelHelper;

public class test {
    public static void main(String []args) throws Exception {
        ExcelHelper excel = new ExcelHelper();
        excel.setExcelFile("./datatest.xlsx", "Sheet1");

        System.out.println(excel.getCellData("STT", 1));
        System.out.println(excel.getCellData("Họ và tên", 1));
        excel.setCellData("pass", 3, 1);
        System.out.println(excel.getCellData(1, 3));
    }
}
